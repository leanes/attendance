package com.example.attendance.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by 陈振聪 on 2017/2/13.
 */
public class MyCardView extends TextView {
    private Paint mPaint ;
    public MyCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint() ;
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(2);
        mPaint.setAlpha(40);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int x = getMeasuredWidth() / 2 ;
        int y = getMeasuredHeight() / 2 ;
        int key = getMeasuredWidth() / 18 ;
        canvas.drawLine(x - key , y , x + key , y , mPaint);
        canvas.drawLine(x , y - key , x , y + key , mPaint);
    }
}
