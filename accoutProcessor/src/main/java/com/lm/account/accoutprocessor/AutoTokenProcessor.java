package com.lm.account.accoutprocessor;

import com.google.auto.service.AutoService;
import com.lm.account.annotations.MyAutoToken;
import com.lm.account.annotations.MyToken;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.tools.Diagnostic;

@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes({"com.lm.account.annotations.MyAutoToken"})
@AutoService(Processor.class)
public class AutoTokenProcessor extends AbstractProcessor {
    private int resultCode = 1000;
    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
        messager = processingEnvironment.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        messager.printMessage(Diagnostic.Kind.NOTE, "start process");
        Set<? extends Element> annotations = roundEnvironment.getElementsAnnotatedWith(MyAutoToken.class);
        for (Element element : annotations) {
            createJava(element);
        }
        return true;
    }

    private void createJava(Element element) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(element.getSimpleName().toString());
        builder.addModifiers(Modifier.STATIC);

        TypeElement classElement = (TypeElement) element.getEnclosingElement();
        int parentType = parentType(classElement);

        if (parentType != 1 && parentType != 2) {
            throw new RuntimeException("@MyAutoToken must be declared on activity or fragment method");
        }

        ClassName thisClassName = ClassName.get(classElement);
        builder.addParameter(thisClassName, parentType == 1 ? "activity" : "fragment", Modifier.FINAL);


        ExecutableElement executableElement = (ExecutableElement) element;
        List<? extends VariableElement> variables = executableElement.getParameters();

        List<String> parameters = new ArrayList<>();
        boolean hasMyToken = false;
        if (variables != null) {
            for (VariableElement variableElement : variables) {
                if (variableElement.getAnnotation(MyToken.class) != null) {
                    parameters.add("token");
                    hasMyToken = true;
                } else {
                    parameters.add(variableElement.getSimpleName().toString());
                    builder.addParameter(ClassName.get(variableElement.asType()), variableElement.getSimpleName().toString(), Modifier.FINAL);
                }
            }
        }

        if (hasMyToken) {
            MyAutoToken myAutoToken = element.getAnnotation(MyAutoToken.class);
            boolean receiveResult = myAutoToken.receiveResult();

            ClassName accountName = ClassName.get("android.accounts", "Account");
            ClassName accountManagerName = ClassName.get("android.accounts", "AccountManager");
            ClassName accountManagerCallbackName = ClassName.get("android.accounts", "AccountManagerCallback");
            ClassName accountManagerFutureName = ClassName.get("android.accounts", "AccountManagerFuture");
            ClassName bundleName = ClassName.get("android.os", "Bundle");
            ClassName intentName = ClassName.get("android.content", "Intent");
            ClassName contextName = ClassName.get("android.content", "Context");

            StringBuilder sb = new StringBuilder();
            if (parentType == 1) {
                sb.append("activity.");
            } else {
                sb.append("fragment.");
            }
            if (receiveResult) {
                sb.append("startActivityForResult(intent");
                sb.append(",").append(resultCode).append(")");
            } else {
                sb.append("startActivity(intent)");
            }

            TypeSpec startActivity = TypeSpec.anonymousClassBuilder("")
                    .addSuperinterface(ParameterizedTypeName.get(accountManagerCallbackName, bundleName))
                    .addMethod(MethodSpec.methodBuilder("run")
                            .addAnnotation(Override.class)
                            .addModifiers(Modifier.PUBLIC)
                            .addParameter(ParameterizedTypeName.get(accountManagerFutureName, bundleName), "future")
                            .beginControlFlow("try")
                            .addStatement("$T intent = future.getResult().getParcelable(AccountManager.KEY_INTENT)", intentName)
                            .addStatement(sb.toString())
                            .nextControlFlow("catch ($T e)", Exception.class)
                            .addStatement("e.printStackTrace()")
                            .endControlFlow()
                            .build()).build();
            MethodSpec addAccount = MethodSpec.methodBuilder("addAccount")
                    .addModifiers(Modifier.STATIC)
                    .addParameter(ClassName.get(classElement), parentType == 1 ? "activity" : "fragment", Modifier.FINAL)
                    .addParameter(accountManagerName, "am")
                    .addParameter(String.class, "accountType")
                    .addStatement("am.addAccount(accountType,null,null,null,null,$L,null)", startActivity).build();

            sb.setLength(0);
            if (parentType == 1) {
                sb.append("activity.");
            } else {
                sb.append("fragment.");
            }
            sb.append(element.getSimpleName()).append("(");
            for (String parameter : parameters) {
                sb.append(parameter).append(",");
            }
            if (sb.charAt(sb.length() - 1) == ',') {
                sb.deleteCharAt(sb.length() - 1);
            }
            sb.append(")");

            TypeSpec tokenCallback = TypeSpec.anonymousClassBuilder("")
                    .addSuperinterface(ParameterizedTypeName.get(accountManagerCallbackName, bundleName))
                    .addMethod(MethodSpec.methodBuilder("run")
                            .addAnnotation(Override.class)
                            .addModifiers(Modifier.PUBLIC)
                            .addParameter(ParameterizedTypeName.get(accountManagerFutureName, bundleName), "future")
                            .beginControlFlow("try")
                            .addStatement("String token = future.getResult().getString(AccountManager.KEY_AUTHTOKEN)")
                            .beginControlFlow("if (token == null)")
                            .addStatement("$N($L,am,accountType)", addAccount, parentType == 1 ? "activity" : "fragment")
                            .nextControlFlow("else")
                            .addStatement(sb.toString())
                            .endControlFlow()
                            .nextControlFlow("catch ($T e)", Exception.class)
                            .addStatement("e.printStackTrace()")
                            .endControlFlow()
                            .build()).build();

            MethodSpec autoTokenMethod = builder.addStatement("final String accountType=$L", parentType == 1 ? "activity.getPackageName()" : "fragment.getContext().getPackageName()")
                    .addStatement("final $T am =(AccountManager)$L.getSystemService($T.ACCOUNT_SERVICE)", accountManagerName, parentType == 1 ? "activity" : "fragment.getContext()", contextName)
                    .addStatement("$T[] accounts = am.getAccountsByType(accountType)", accountName)
                    .beginControlFlow("if (accounts == null)")
                    .addStatement("$N($L,am,accountType)", addAccount, parentType == 1 ? "activity" : "fragment")
                    .nextControlFlow("else")
                    .addStatement("Account account=null")
                    .addStatement("long latestTime = 0")
                    .beginControlFlow("for(Account a : accounts)")
                    .addStatement("String timeStr = am.getUserData(a, \"time\")")
                    .beginControlFlow("try")
                    .addStatement("long time = Long.parseLong(timeStr)")
                    .beginControlFlow("if (time > latestTime)")
                    .addStatement("latestTime = time")
                    .addStatement("account = a")
                    .endControlFlow()
                    .nextControlFlow("catch (NumberFormatException e)")
                    .addStatement("e.printStackTrace()")
                    .endControlFlow()
                    .endControlFlow()
                    .beginControlFlow("if (account == null)")
                    .addStatement("$N($L,am,accountType)", addAccount, parentType == 1 ? "activity" : "fragment")
                    .addStatement("return")
                    .endControlFlow()
                    .addStatement("String token = am.peekAuthToken(account, accountType)")
                    .beginControlFlow("if (token == null)")
                    .addStatement("am.getAuthToken(account, accountType, null, null,$L,null)", tokenCallback)
                    .nextControlFlow("else")
                    .addStatement(sb.toString())
                    .endControlFlow()
                    .endControlFlow()
                    .build();


            MethodSpec.Builder onResultMethodBuilder = MethodSpec.methodBuilder(autoTokenMethod.name + "WithToken")
                    .addModifiers(Modifier.STATIC);

            TypeSpec autoToken;
            if (receiveResult) {
                sb.setLength(0);
                sb.append(autoTokenMethod.name).append("(");
                List<ParameterSpec> ps = autoTokenMethod.parameters;
                if (ps != null) {
                    for (ParameterSpec p : ps) {
                        onResultMethodBuilder.addParameter(p);
                        sb.append(p.name).append(",");
                    }
                }

                if (sb.charAt(sb.length() - 1) == ',') {
                    sb.deleteCharAt(sb.length() - 1);
                }
                sb.append(")");

                MethodSpec onResultMethod = onResultMethodBuilder.addParameter(int.class, "requestCode")
                        .addParameter(int.class, "resultCode")
                        .beginControlFlow("if(resultCode== $T.RESULT_OK&&requestCode==$L)", ClassName.get("android.app", "Activity"), resultCode)
                        .addStatement(sb.toString())
                        .endControlFlow().build();

                autoToken = TypeSpec.classBuilder(classElement.getSimpleName() + "WithAutoToken")
                        .addModifiers(Modifier.PUBLIC)
                        .addMethod(autoTokenMethod)
                        .addMethod(addAccount)
                        .addMethod(onResultMethod)
                        .build();
            } else {
                autoToken = TypeSpec.classBuilder(classElement.getSimpleName() + "WithAutoToken")
                        .addModifiers(Modifier.PUBLIC)
                        .addMethod(autoTokenMethod)
                        .addMethod(addAccount)
                        .build();
            }

            PackageElement packageElement = (PackageElement) classElement.getEnclosingElement();
            JavaFile javaFile = JavaFile.builder(packageElement.getQualifiedName().toString(), autoToken).build();
            try {
                javaFile.writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }

            resultCode++;
        }
    }

    private int parentType(TypeElement typeElement) {
        if (typeElement == null) {
            return 0;
        }
        String className = typeElement.getQualifiedName().toString();
        if ("android.app.Activity".equals(className)) {
            return 1;
        } else if ("androidx.fragment.app.Fragment".equals(className) || "android.app.Fragment".equals(className)) {
            return 2;
        } else {
            return parentType((TypeElement) ((DeclaredType) typeElement.getSuperclass()).asElement());
        }

    }

}