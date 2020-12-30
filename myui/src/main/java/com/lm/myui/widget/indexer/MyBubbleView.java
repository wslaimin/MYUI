package com.lm.myui.widget.indexer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;

public class MyBubbleView extends View {
    private int size=200;
    private int padding=6;
    private float fraction=0.6f;
    private String text;
    private Path path=new Path();
    private int color;
    private int textColor;
    private int textSize;
    private Paint paint;
    private PointF center;
    private Rect rect=new Rect();

    public MyBubbleView(Context context) {
        super(context);
        paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        setTextSize(TypedValue.COMPLEX_UNIT_PX,66);
        setTextColor(Color.WHITE);
        setColor(0x7F000000);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(size,size);
        calculatePoints();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBubble(canvas);
        drawText(canvas);
    }

    public void setText(String text){
        this.text=text;
        invalidate();
    }

    public void setColor(int color){
        this.color=color;
        invalidate();
    }

    public void setTextColor(int color){
        textColor=color;
        invalidate();
    }

    public void setTextSize(int unit,int size){
        textSize= (int)TypedValue.applyDimension(unit,size,getResources().getDisplayMetrics());
        paint.setTextSize(textSize);
        invalidate();
    }

    private void calculatePoints(){
        path.reset();

        float radius=(size-2*padding)/(2+fraction);
        center=new PointF(padding+radius,size/2f);
        path.moveTo(center.x+radius+fraction*radius,center.y);
        path.cubicTo(center.x+radius,center.y-0.55f*radius,center.x+0.55f*radius,center.y-radius,center.x,center.y-radius);
        path.cubicTo(center.x-0.55f*radius,center.y-radius,center.x-radius,center.y-0.55f*radius,center.x-radius,center.y);
        path.cubicTo(center.x-radius,center.y+0.55f*radius,center.x-0.55f*radius,center.y+radius,center.x,center.y+radius);
        path.cubicTo(center.x+0.55f*radius,center.y+radius,center.x+radius,center.y+0.55f*radius,center.x+radius+fraction*radius,center.y);
        path.close();
    }

    private void drawBubble(Canvas canvas){
        paint.setColor(color);
        canvas.drawPath(path,paint);
    }

    private void drawText(Canvas canvas){
        if(text==null){
            return;
        }
        paint.setColor(textColor);
        paint.getTextBounds(text,0,text.length(),rect);
        canvas.drawText(text,center.x-rect.width()/2f,center.y+rect.height()/2f,paint);
    }
}