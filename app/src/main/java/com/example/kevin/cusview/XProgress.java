package com.example.kevin.cusview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by xianda on 2016/11/4.
 *
 * Bug：蚊子大小后续扩展  蚊子-->圆锯；
 */
public class XProgress extends View {

    //控件默认高度
    private final float DEFAULT_HEIGHT = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80f, getResources().getDisplayMetrics());
    //控件默认宽度
    private final float DEFTAULT_WIDTH = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics());
    //完成进度默认高度
    private final float LINE_DEFAULT_HEIGHT = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, getResources().getDisplayMetrics());
    //圆角矩形默认长度
    private final float ROUNDRECT_DEFAULT_WIDTH = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40f, getResources().getDisplayMetrics());
    //控件主体位置
    //圆角矩形位置
    private final float MTEXT_SIZE = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics());
    //圆角矩形默认高度
    private final float ROUNDRECT_DEFAULT_HEIGHT = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, getResources().getDisplayMetrics());
    //水滴半径
    private final float DROP_RADIUS = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, getResources().getDisplayMetrics());
    //水滴高度
    private final float DROP_HEIGTH = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 26f, getResources().getDisplayMetrics());

/*    //进度位置
    private float rectLeft, rectTop, rectRight, rectBottom;
    //默认线位置
    private float lineLeft, lineTop, lineRight, lineBottom;*/
    private float mLeft, mTop, mRight, mBottm;
    private float roundRectLeft, roundRectTop, roundRectRight, roundRectBottom, roundRectRX, roundRectRY, roundRectWidth, roundRectHeight;
    private Paint mDefPaint;
    private float centerY;
    //阶梯个数
    private int mRoundRectNum;
    //单位
    private String mUnit;
    //当前位置限额箱数
    private int[] mSteps;
    //线段个数
    private int mLineNum;
    private String[] mTxts;
    private float mLineSize;
    //控件宽高
    private int mWidth;
    private int mHeight;
    //线段默认左边位置集合
    private ArrayList<Float> mLefts;
    //水滴位置
    private int mPos;
    //文字书写笔
    private Paint mPencil;
    private Canvas mCanvas;
    ;

    public XProgress(Context context) {
        this(context, null);
    }

    public XProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mLefts = new ArrayList<>();

        //默认画笔
        mDefPaint = new Paint();
        mDefPaint.setColor(Color.GRAY);
        mDefPaint.setAntiAlias(true);
        mDefPaint.setDither(true);
        mDefPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mDefPaint.setStrokeWidth(2f);

        //铅笔
        mPencil = new Paint();
        mPencil.setColor(Color.BLACK);
        mPencil.setAntiAlias(true);
        mPencil.setDither(true);
        mPencil.setStyle(Paint.Style.FILL);
        mPencil.setTextSize(MTEXT_SIZE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mCanvas = canvas;
        //计算进度位置
        int index = calcLines();
        float ratio = calcPosOnLine(index);
        //默认直线
        for (int i = 0, size = mLefts.size(); i < size; i++) {

//            Path path = new Path();
//            path.addRect(rectF, Path.Direction.CW);

            if (i < index) {//进度绘制
                mDefPaint.setColor(Color.RED);
                mLeft = mLefts.get(i);
                mRight = i == size - 1 ? mWidth : mLeft + mLineSize;

                mPencil.setColor(Color.RED);
            } else if (i == index) {//默认回执
                mDefPaint.setColor(Color.RED);
                mLeft = mLefts.get(i);
                //水滴位置
                mRight = mLeft + mLineSize * ratio;
                RectF rectF = new RectF(mLeft, mTop, mRight, mBottm);
                canvas.drawRect(rectF, mDefPaint);

                //回执水滴
                drawDrop();

                mDefPaint.setColor(Color.GRAY);
                mLeft = mRight + 1;
                mRight = i == size - 1 ? mWidth : mLeft + mLineSize;

                mPencil.setColor(Color.GRAY);
            } else {
                mDefPaint.setColor(Color.GRAY);
                mLeft = mLefts.get(i);
                mRight = i == size - 1 ? mWidth : mLeft + mLineSize;
                mPencil.setColor(Color.GRAY);
            }
            RectF rectF = new RectF(mLeft, mTop, mRight, mBottm);
            canvas.drawRect(rectF, mDefPaint);
            drawText(mTxts[i], mLefts.get(i) + mLineSize / 2f, centerY - 20f);
        }


        //阶梯圆角举行
        for (int i = 0; i < mRoundRectNum; i++) {
            if (i < index) {
                //边框圆角
                mDefPaint.setStyle(Paint.Style.FILL);
                mDefPaint.setColor(Color.RED);
                mPencil.setColor(Color.WHITE);

            } else {
                mDefPaint.setStyle(Paint.Style.FILL);
                mDefPaint.setColor(Color.WHITE);
                drawRoundRect(i);
                mDefPaint.setStyle(Paint.Style.STROKE);
                mDefPaint.setColor(Color.GRAY);
                mPencil.setColor(Color.BLACK);
            }
            drawRoundRect(i);
            //计算文字位置
            String text = mSteps[i] + mUnit;
            drawText(text, roundRectLeft + roundRectWidth / 2f, roundRectTop + roundRectHeight / 2f);
        }
    }

    //画水滴
    private void drawDrop() {
        mDefPaint.setStyle(Paint.Style.FILL);
        Path path = new Path();
        path.moveTo(mRight,mBottm);
        path.lineTo(mRight - DROP_RADIUS,centerY + DROP_HEIGTH);
        path.lineTo(mRight + DROP_RADIUS,centerY + DROP_HEIGTH);
        path.lineTo(mRight,mBottm);
        mCanvas.drawPath(path,mDefPaint);
        mCanvas.drawCircle(mRight,mBottm + DROP_HEIGTH,DROP_RADIUS,mDefPaint);
        mPencil.setColor(Color.WHITE);
        drawText(mPos+"",mRight,mBottm + DROP_HEIGTH);
    }

    /**
     * 花圆角居心
     *
     * @param i
     */
    private void drawRoundRect(int i) {
        roundRectLeft = mLefts.get(i) + mLineSize;
        roundRectRight = roundRectLeft + roundRectWidth;
        RectF rectF = new RectF(roundRectLeft, roundRectTop, roundRectRight, roundRectBottom);
        mCanvas.drawRoundRect(rectF, roundRectRX, roundRectRY, mDefPaint);
    }

    private void drawText(String text, float x, float y) {
        Rect rect = new Rect();
        mPencil.getTextBounds(text, 0, text.length(), rect);
        float width = rect.width();
        float height = rect.height();
        x = x - width / 2f;
        y = y + height / 2f - 4f;
        //重新计算圆角举行宽度
//        roundRectWidth = Math.max(roundRectWidth, width + 14f);
        mCanvas.drawText(text, x, y, mPencil);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float height = DEFAULT_HEIGHT;
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            height = MeasureSpec.getSize(heightMeasureSpec);
        } else {
            height = Math.min(height, MeasureSpec.getSize(heightMeasureSpec));
        }
        float width = DEFTAULT_WIDTH;
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
            width = MeasureSpec.getSize(widthMeasureSpec);
        } else {
            width = Math.min(width, MeasureSpec.getSize(widthMeasureSpec));
        }
        setMeasuredDimension(MeasureSpec.makeMeasureSpec((int) width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec((int) height, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //控件宽高
        mWidth = getWidth();
        mHeight = getHeight();
        //纵轴中点
        centerY = mHeight / 2f;

        //默认线位置
        mTop = centerY - LINE_DEFAULT_HEIGHT / 2f;
        mBottm = mTop + LINE_DEFAULT_HEIGHT;

        //圆角举行位置
        roundRectWidth = ROUNDRECT_DEFAULT_WIDTH;
        roundRectHeight = ROUNDRECT_DEFAULT_HEIGHT;
        roundRectTop = centerY - roundRectHeight / 2f;
        roundRectBottom = centerY + roundRectHeight / 2f;
        roundRectRX = (roundRectBottom - roundRectTop) / 2f;
        roundRectRY = roundRectRX;

        //计算线段平均长度
        mLineSize = (mWidth - roundRectWidth * mRoundRectNum) / (mLineNum + 1);

        //计算线段位置
        for (int i = 0; i < mLineNum; i++) {
            mLeft = i * (mLineSize + roundRectWidth);
//            mLineSize += roundRectWidth;
            mLefts.add(mLeft);
        }
    }

    /**
     *
     * @param steps 阶梯内容集合
     * @param txts 线上展示蚊子
     * @param pos 当前阶梯箱数
     * @param unit 阶梯单位
     */
    public void setRoundRectNum(int[] steps, String[] txts, int pos, String unit) {
        mRoundRectNum = steps.length;
        mLineNum = txts.length;
        if (mLineNum < mRoundRectNum) {
            throw new IndexOutOfBoundsException("参数 txts.length 必须 > steps.length");
        }
        if (pos < 0) {
            throw new RuntimeException("参数 pos 不能小雨 0");
        }
        mSteps = steps;
        mTxts = txts;
        mUnit = unit;
        mPos = pos;
        invalidate();
    }

    /**
     * 计算彩色线条数
     */
    private int calcLines(){
        int index = Arrays.binarySearch(mSteps, mPos);
        return index < 0 ? -1 - index : index + 1;
    }

    /**
     * 计算所线条位置
     */
    private float calcPosOnLine(int i){
        float ratio = 0f;
        if (i > 0) {
            if (i == mSteps.length) {//当前进度在无线区域  , 估计多余40
                ratio = (float)(mPos - mSteps[i - 1]) / (float)(mPos + 40 - mSteps[i - 1]);
            } else {//可控范围内
                ratio = (float)(mPos - mSteps[i - 1]) / (float)(mSteps[i] - mSteps[i - 1]);
            }
        } else if (i == 0) {
            ratio = (float)mPos / (float)mSteps[0];
        } else {
            throw new IndexOutOfBoundsException("当前进度位置不能小余零");
        }
        return ratio;

    }
}
