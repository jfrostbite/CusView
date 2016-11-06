package com.example.kevin.cusview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by kevin on 2016/11/4.
 */

public class CusView extends View {

    private Paint mPaint;

    public CusView(Context context) {
        this(context,null);
    }

    public CusView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
//        mPaint.setColor(Color.GREEN);
        //抗锯齿
        mPaint.setAntiAlias(true);

        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
                0.5f,0,0,0,0,
                0,0.5f,0,0,0,
                0,0,0.5f,0,0,
                0,0,0,1,0
        });
        mPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        //平滑清晰
        mPaint.setDither(true);

        //设置滤镜
        mPaint.setMaskFilter(new BlurMaskFilter(20f, BlurMaskFilter.Blur.INNER));
//        mPaint.setColorFilter(new LightingColorFilter(0xffffff00,0));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.imac);
        canvas.drawBitmap(bitmap,10f,10f,mPaint);
    }
}
