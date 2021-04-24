package com.campuscard.app.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.campuscard.app.ui.entity.ChartDataEntity;

import java.util.List;

/**
 * 饼图
 */
public class BreadView extends View {
    private List<ChartDataEntity> mBeans;
    private int mHeight, mWidth;//宽高
    private static final int DEFAULT_RADIUS = 200;
    private int mRadius = DEFAULT_RADIUS; //外圆的半径
    private float nowCurrent = 0;
    private int centerX, centerY;//中心坐标
    private Paint mTextPaint;//画文字的画笔
    private Paint mPaint;//扇形的画笔
    private RectF mPointF = new RectF(-50, -50, 50, 50);

    public BreadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 设置
     *
     * @param mBeans
     */
    public void setmBeans(List<ChartDataEntity> mBeans) {
        this.mBeans = mBeans;

    }

    /**
     * 初始化饼图画笔
     * 初始化文字画笔
     */
    public void init() {

        mPaint = new Paint();
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setTextSize(40);
        mTextPaint.setStrokeWidth(3);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.BLACK);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBeans == null)
            return;

        centerX = (getRight() - getLeft()) / 2;
        centerY = (getBottom() - getTop()) / 2;

        int min = mHeight > mWidth ? mWidth : mHeight;
        if (mRadius > min / 2) {
            mRadius = (int) ((min - getPaddingTop() - getPaddingBottom()) / 3.5);
        }

        canvas.save();
        drawBread(canvas);
        canvas.restore();

        //线与文字
        canvas.save();
        drawLineAndText(canvas);
        canvas.restore();
    }

    /**
     * 获取 总长度
     *
     * @return
     */
    public float getAllSize() {
        float i = 0f;
        if (mBeans != null) {
            for (ChartDataEntity bean : mBeans) {
                i += bean.getSize();
            }
        }
        return i;
    }

    /**
     * 画饼图
     *
     * @param canvas
     */
    public void drawBread(Canvas canvas) {
        canvas.translate((getWidth() + getPaddingLeft() - getPaddingRight()) / 2, (getHeight() + getPaddingTop() - getPaddingBottom()) / 2);
        RectF mF = new RectF(-mRadius + 50, -mRadius + 50, mRadius - 50, mRadius - 50);
        Log.e("yxs", "半径：" + mRadius);
        centerX = (getRight() - getLeft()) / 2;
        centerY = (getBottom() - getTop()) / 2;
        float AllSize = getAllSize();
        for (ChartDataEntity bean : mBeans) {
            mPaint.setColor(Color.parseColor(bean.getColor()));
            float f = (bean.getSize() / AllSize) * 360f;
            canvas.drawArc(mF, nowCurrent, f, true, mPaint);
            //            canvas.drawText(bean.getTop() ,);
            nowCurrent += f;
        }

        //饼图上再画一个白色的园，行程圆环
        mPaint.setColor(Color.parseColor("#ffffff"));
        int whiteF = mRadius - 100;
        RectF mF2 = new RectF(-whiteF, -whiteF, whiteF, whiteF);
        canvas.drawArc(mF2, 0, 360, true, mPaint);
    }

    //画线与文字
    private void drawLineAndText(Canvas canvas) {
        int start = 0;
        canvas.translate((getWidth() + getPaddingLeft() - getPaddingRight()) / 2, (getHeight() + getPaddingTop() - getPaddingBottom()) / 2);
        mPaint.setStrokeWidth(4);
        for (int i = 0; i < mBeans.size(); i++) {
            float angles = ((mBeans.get(i).getSize() * 1.0f / getAllSize()) * 360);
            Log.e("yxs", "上：" + mBeans.get(i).getTop() + "---下：" + mBeans.get(i).getBottom());
            drawLine(canvas, start, angles, mBeans.get(i).getTop(), mBeans.get(i).getBottom(), Color.parseColor(mBeans.get(i).getColor()));
            start += angles;
        }
    }

    //画线与文字
    private void drawLine(Canvas canvas, int start, float angles, String top, String bottom, int color) {
        mPaint.setColor(color);
        float stopX, stopY;
        stopX = (float) ((mRadius + 40) * Math.cos((2 * start + angles) / 2 * Math.PI / 180));
        stopY = (float) ((mRadius + 40) * Math.sin((2 * start + angles) / 2 * Math.PI / 180));

        //画横线
        int dx;//判断横线是画在左边还是右边


        int endX;

        canvas.drawLine(
                (float) ((mRadius - 20) * Math.cos((2 * start + angles) / 2 * Math.PI / 180))
                //                , dy > 0 ? (float) ((mRadius - 20) * Math.sin((2 * start + angles) / 2 * Math.PI / 180)) - 20 : (float) ((mRadius - 20) * Math.sin((2 * start + angles) / 2 * Math.PI / 180)) + 20,
                , (float) ((mRadius - 20) * Math.sin((2 * start + angles) / 2 * Math.PI / 180)),
                stopX,
                stopY,
                mPaint
        );
        canvas.drawCircle((float) ((mRadius - 20) * Math.cos((2 * start + angles) / 2 * Math.PI / 180))
                , (float) ((mRadius - 20) * Math.sin((2 * start + angles) / 2 * Math.PI / 180))
                , 10,
                mPaint
        );

        if (stopX > 0) {
            endX = (centerX - getPaddingRight() - 20);
        } else {
            endX = (-centerX + getPaddingLeft() + 20);
        }
        dx = (int) (endX - stopX);

        //画横线
        canvas.drawLine(stopX, stopY,
                dx < 0 ? endX + 150 : endX - 150, stopY, mPaint
        );

        //测量文字大小
        Rect rect = new Rect();
        mTextPaint.getTextBounds(top, 0, top.length(), rect);
        int w = rect.width();
        int h = rect.height();
        int offset = 20;//文字在横线的偏移量
        //画文字
        canvas.drawText(top, 0, top.length(), dx < 0 ? endX + offset + 150 : endX - w - offset - 150, stopY + h, mTextPaint);

        //测量百分比大小
        mTextPaint.getTextBounds(bottom, 0, bottom.length(), rect);
        w = rect.width() - 10;
        //画百分比
        canvas.drawText(bottom, 0, bottom.length(), dx < 0 ? endX + offset + 150 : endX - w - offset - 150, stopY - 5, mTextPaint);

    }

}
