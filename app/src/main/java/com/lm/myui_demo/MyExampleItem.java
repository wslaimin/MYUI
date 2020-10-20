package com.lm.myui_demo;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public class MyExampleItem implements Parcelable {
    int drawable;
    String text;
    String activity;
    List<MyExampleItem> children;

    public MyExampleItem(){}

    protected MyExampleItem(Parcel in) {
        drawable = in.readInt();
        text = in.readString();
        activity = in.readString();
        children = in.createTypedArrayList(MyExampleItem.CREATOR);
    }

    public static final Creator<MyExampleItem> CREATOR = new Creator<MyExampleItem>() {
        @Override
        public MyExampleItem createFromParcel(Parcel in) {
            return new MyExampleItem(in);
        }

        @Override
        public MyExampleItem[] newArray(int size) {
            return new MyExampleItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(drawable);
        dest.writeString(text);
        dest.writeString(activity);
        dest.writeTypedList(children);
    }
}
