package com.lm.myui_demo;

import android.content.Context;
import android.content.res.XmlResourceParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

//TODO: change drawable,text,activity to example's attributes ?
public class MyXmlParser {
    static MyExampleItem parse(Context context) throws IOException, XmlPullParserException {
        Stack<MyExampleItem> stack = new Stack<>();
        final XmlResourceParser parser = context.getResources().getXml(R.xml.examples);
        int type;
        String name;
        MyExampleItem exampleItem = null;
        boolean pushed=false;
        while ((type = parser.next()) != XmlResourceParser.END_DOCUMENT) {
            name = parser.getName();
            if ("example".equals(name)) {
                if (type == XmlResourceParser.START_TAG) {
                    exampleItem = new MyExampleItem();
                    pushed = false;
                } else if (type == XmlResourceParser.END_TAG && !pushed) {
                    stack.push(exampleItem);
                    pushed=true;
                }
            } else if ("children".equals(name)) {
                if (type == XmlResourceParser.START_TAG) {
                    exampleItem.flag = 1;
                    exampleItem.activity=MainActivity.class.getName();
                    stack.push(exampleItem);
                    pushed = true;
                } else if (type == XmlResourceParser.END_TAG) {
                    MyExampleItem parentItem = null;
                    List<MyExampleItem> children = new ArrayList<>();
                    while (stack.size() > 0) {
                        MyExampleItem item = stack.pop();
                        if (item.flag == 1) {
                            parentItem = item;
                            break;
                        }
                        children.add(item);
                    }
                    Collections.reverse(children);
                    parentItem.children = children;
                    parentItem.flag = 0;
                    stack.push(parentItem);
                }
            } else if ("drawable".equals(name)) {
                exampleItem.drawable = context.getResources().getIdentifier(parser.nextText(), "drawable", context.getPackageName());
            } else if ("text".equals(name)) {
                exampleItem.text = parser.nextText();
            } else if ("activity".equals(name)) {
                exampleItem.activity = parser.nextText();
                System.out.println("123");
            }
        }
        return stack.pop();
    }
}
