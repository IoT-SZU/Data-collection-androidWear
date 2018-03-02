package com.example.tommy.androidwear;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Tommy on 2017/9/9.
 */

public class DataView extends View {
    private static final String TAG = "DataView";
    private int width;
    private int height;

    private float drawLength;
    private float drawHeight;

    private ArrayList<Float> dataList = new ArrayList<>();

    Paint paint;

    public DataView(Context context) {
        super(context);
    }

    public DataView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DataView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setData(int width, int height) {
        this.width = width;
        this.height = height;
        Log.i(TAG,"setData" + width + "  " + height);
        initPaint();
    }

    private void initPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(16);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
    }


    public void updateView(float value){
        if (dataList == null ){
            Log.i(TAG,"updateView:------------ null");
            return;
        }
        dataList.add(value);
    //    HighPass.highPass(dataList);
        if (dataList.size()>80){
            dataList.remove(0);
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(0,150);
        initPaint();
        for (int i=0; i<dataList.size(); i++) {
            if (i > 0) {
                canvas.drawLine((i-1)*5, dataList.get(i-1)*50, i*5, dataList.get(i)*50, paint);
            }
        }
//        float timeline = 0;
//        float step = width / (dataList.size() - 1);
//        for (int i = 1; i < dataList.size(); ++i) {
//            canvas.drawLine(timeline, normalize(dataList.get(i - 1), 0.1f), timeline + step, normalize(dataList.get(i), 0.1f), paint);
//            timeline += step;
//        }
    }
}
