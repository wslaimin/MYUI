package com.lm.myui_demo;

import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.view.View;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyXmlParser {
    static List<MyExampleItem> parse(final Context context) throws IOException, XmlPullParserException {
        List<MyExampleItem> list = new ArrayList<>();
        String name;
        MyExampleItem item = null;
        MyExampleItem child = null;
        final XmlResourceParser parser = context.getResources().getXml(R.xml.examples);
        int type;
        int deep = 0;
        while ((type = parser.next()) != XmlResourceParser.END_DOCUMENT) {
            name = parser.getName();
            if ("example".equals(name)) {
                if (type == XmlResourceParser.START_TAG) {
                    deep++;
                    item = new MyExampleItem();
                    list.add(item);
                } else {
                    deep--;
                    if(item.children!=null){
                        final ArrayList<MyExampleItem> children = new ArrayList<>();
                        children.addAll(item.children);
                        item.activity=MainActivity.class.getName();
                    }
                }
            } else if ("children".equals(name)) {
                if (type == XmlResourceParser.START_TAG) {
                    deep++;
                    item.children = new ArrayList<>();
                } else {
                    deep--;
                }
            } else if ("child".equals(name) && type == XmlResourceParser.START_TAG) {
                child = new MyExampleItem();
                item.children.add(child);
            } else if ("drawable".equals(name)) {
                if (deep == 1) {
                    item.drawable = context.getResources().getIdentifier(parser.nextText(), "drawable", context.getPackageName());
                } else {
                    child.drawable = context.getResources().getIdentifier(parser.nextText(), "drawable", context.getPackageName());
                }
            } else if ("text".equals(name)) {
                if (deep == 1) {
                    item.text = parser.nextText();
                } else {
                    child.text = parser.nextText();
                }
            } else if ("activity".equals(name)) {
                if (deep == 1) {
                    item.activity=parser.nextText();
                } else {
                    child.activity=parser.nextText();
                }
            }
        }
        return list;
    }
}
