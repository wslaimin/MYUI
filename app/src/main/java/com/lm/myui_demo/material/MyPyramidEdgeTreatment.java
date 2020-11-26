package com.lm.myui_demo.material;

import androidx.annotation.NonNull;
import com.google.android.material.shape.EdgeTreatment;
import com.google.android.material.shape.ShapePath;

public class MyPyramidEdgeTreatment extends EdgeTreatment {
    private float size;

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    //positive direction is edge point to view inside
    @Override
    public void getEdgePath(float length, float center, float interpolation, @NonNull ShapePath shapePath) {
        float centerY = size / 2;
        shapePath.lineTo(0, centerY);
        shapePath.lineTo(length / 2, 0);
        shapePath.lineTo(length, centerY);
        shapePath.lineTo(length, 0);
        shapePath.lineTo(0, 0);
    }
}
