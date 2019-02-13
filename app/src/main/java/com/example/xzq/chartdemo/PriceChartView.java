package com.example.xzq.chartdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @file:PriceChartView.java
 * @date: 2019/1/28
 * @author： xieziqi
 * @describe: 价格走势图
 */
public class PriceChartView extends View {
    private final String TAG = "PriceChartView";
    private double maxPrice = 6550, minPrice = 6450;
    private int mWidth, mHeight, mYMaxHeight;
    private Paint mPaint;
    //高价到底价
    private List<Double> mPriceList = new ArrayList<>();
    private List<DatePriceBean> mDateList = new ArrayList<>();

    private float yRatio = 0.875f;//y轴坐标系占高度比

    private int mScaleWidth;//刻度宽度
    private float yMaxTextWidth;//y 最宽的文字宽度
    private float yLeftOffets;//y轴的左偏移量
    private int xStepSize = 7;//X轴的一格代表的天数
    private float xRightSpace;//右边空出来的宽度，固定值，可设置

    private float dividerWidth;//分割线的宽度
    private float dividerSpaceWidth;//分割线间隔

    private float dividerHeight;//分割线的高度

    private float redPointRadius;//实心红点的半径
    private float redStrokePointRadius;//描边红点的半径

    private float topOffset;//上边边空出来的宽度，固定值，可设置
    private float connectLineHeight;//连接线的高度

    private int mCurrentIndex;

    private RectF mTipsRectBackground = new RectF();
    private int mTipsBackgroundColor;//提示框颜色
    private int mTipsBackgroundRadius;//提示框圆角半径
    private int mTipsHeight, mTipsWidth;
    private int mTipsDateTextColor;//日期的文字颜色
    private int mTriangleHeight;//三角形的高
    private int mTriangleBottomWidth;//三角形的底长度

    private PointF mCurrentIndexPoint = new PointF();//选中的点的坐标

    private int yTextColor;//Y轴文字颜色
    private int xTextColor;//X轴文字颜色
    private int xyColor;//X轴Y轴颜色
    private int minOrMaxPriceDividerColor;//价格最大值或者最小值时候分割线的颜色
    private int normalPriceDividerColor;//正常时候分割线的颜色
    private int chartLineColor;//走势连接点的颜色
    private int currentLineColor;//选中的目标中线颜色

    public PriceChartView(Context context) {
        super(context);
        init(context);
    }

    public PriceChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    private void init(Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mScaleWidth = DimenUtil.dip2px(context, 5);
        xRightSpace = DimenUtil.dip2px(context, 30);
        topOffset = DimenUtil.dip2px(context, 30);
        dividerWidth = DimenUtil.dip2px(context, 4);
        dividerHeight = DimenUtil.dip2px(context, 1f);
        connectLineHeight = DimenUtil.dip2px(context, 1.5f);
        redPointRadius = DimenUtil.dip2px(context, 2.5f);
        redStrokePointRadius = DimenUtil.dip2px(context, 3f);
        dividerSpaceWidth = DimenUtil.dip2px(context, 2);
        mTipsHeight = DimenUtil.dip2px(context, 16);
        mTipsWidth = DimenUtil.dip2px(context, 95);
        mTipsBackgroundRadius = DimenUtil.dip2px(context, 1);
        mTriangleHeight = DimenUtil.dip2px(context, 4);
        mTriangleBottomWidth = DimenUtil.dip2px(context, 7);


        mTipsBackgroundColor = Color.parseColor("#595959");
        mTipsDateTextColor = Color.parseColor("#999999");
        yTextColor = Color.parseColor("#333333");
        xTextColor = Color.parseColor("#666666");
        xyColor = Color.parseColor("#979797");
        minOrMaxPriceDividerColor = Color.parseColor("#979797");
        normalPriceDividerColor = Color.parseColor("#f0f0f0");
        chartLineColor = Color.parseColor("#FF2600");
        currentLineColor = Color.parseColor("#979797");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthResult = 0;
        //view根据xml中layout_width和layout_height测量出对应的宽度和高度值，
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        switch (widthSpecMode) {
            case MeasureSpec.UNSPECIFIED:
                widthResult = widthSpecSize;
                break;
            case MeasureSpec.AT_MOST://wrap_content时候
//                widthResult = getContentWidth();
                break;
            case MeasureSpec.EXACTLY:
                widthResult = widthSpecSize;
                break;
        }

        int heightResult = 0;
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        switch (heightSpecMode) {
            case MeasureSpec.UNSPECIFIED:
                heightResult = heightSpecSize;
                break;
            case MeasureSpec.AT_MOST://wrap_content时候
//                heightResult = getContentHeight();
                break;
            case MeasureSpec.EXACTLY:
                heightResult = heightSpecSize;
                break;
        }
        mWidth = widthResult;
        mHeight = heightResult;
        setMeasuredDimension(widthResult, heightResult);
        mYMaxHeight = (int) (mHeight - topOffset);

    }

    public void setData(List<Double> priceList, List<DatePriceBean> dataList, double maxPrice, double minPrice) {
        if (BeanUtils.isEmpty(priceList) || BeanUtils.isEmpty(dataList) || maxPrice < 0 || minPrice < 0) {
            return;
        }
        this.mPriceList.clear();
        this.mPriceList.addAll(priceList);
        this.mDateList.clear();
        this.mDateList.addAll(dataList);

        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
        mCurrentIndex = mDateList.size() - 1;
        Collections.sort(priceList, (o1, o2) -> (int) (o2 - o1));
        Log.d(TAG, priceList.toString());
//        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (BeanUtils.isEmpty(mPriceList) || BeanUtils.isEmpty(mDateList)) {
            return;
        }
        canvas.translate(0, topOffset);
        drawY(canvas);//Y轴
        drawX(canvas);//X轴
        drawDivider(canvas);//分割线
        drawChart(canvas);//走势图
        canvas.translate(0, -topOffset);
        drawTips(canvas, mCurrentIndex);//价格提示
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (checkNeedUpdateIndex(event.getX())) {
                    invalidate();
                }
                Log.d(TAG, "onTouchEvent downX = " + event.getX());
                return true;
            case MotionEvent.ACTION_MOVE:
                if (checkNeedUpdateIndex(event.getX())) {
                    invalidate();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private boolean checkNeedUpdateIndex(float downX) {
        boolean needUpdate = false;
        float xWidth = mWidth - yLeftOffets;
        float dayStepWidth = (xWidth - xRightSpace) / (mDateList.size() - 1);//一天的宽度
        int newIndex = (int) (Math.abs(downX) / dayStepWidth) - 1;
        if (newIndex > mDateList.size() - 1) {
            newIndex = mDateList.size() - 1;
        }
        if (newIndex < 0) {
            newIndex = 0;
        }
        if (newIndex != mCurrentIndex) {
            needUpdate = true;
            mCurrentIndex = newIndex;
        }
        return needUpdate;
    }


    private void drawTips(Canvas canvas, int index) {
        mPaint.setColor(currentLineColor);
        mPaint.setStrokeWidth(DimenUtil.dip2px(getContext(), 0.4f));
        DatePriceBean bean = mDateList.get(index);
        //Y轴高度
        float yHeight = mYMaxHeight * yRatio;
        float xWidth = mWidth - yLeftOffets;

        float circleY = mCurrentIndexPoint.y + topOffset;
        //算出圆心X坐标
        float dayStepWidth = (xWidth - xRightSpace) / (mDateList.size() - 1);//一天的宽度
        float circleX = dayStepWidth * (index) + yLeftOffets;

        //画出第一根线 在圆点之上的
        canvas.drawLine(circleX, topOffset, circleX, circleY - redStrokePointRadius, mPaint);
        //画出第一根线 在圆点之下的
        canvas.drawLine(circleX, circleY + redStrokePointRadius, circleX, yHeight + topOffset, mPaint);

        //画提示框的背景
        mPaint.setColor(mTipsBackgroundColor);
        mPaint.setStyle(Paint.Style.FILL);
        mTipsRectBackground.top = topOffset - mTipsHeight - mTriangleHeight;
        mTipsRectBackground.bottom = mTipsRectBackground.top + mTipsHeight;
        if (index == 0) {
            mTipsRectBackground.left = yLeftOffets;
        } else {
            float halfRect = mTipsWidth / 2;
            if ((mWidth - yLeftOffets) / 2 > circleX) {
                //圆心点偏左边
                if (circleX - yLeftOffets >= halfRect) {
                    //可以居中显示
                    mTipsRectBackground.left = circleX - halfRect;
                } else {
                    mTipsRectBackground.left = yLeftOffets;
                }
            } else {
                //圆心点偏右边
                if (circleX + halfRect <= mWidth) {
                    //可以居中显示
                    mTipsRectBackground.left = circleX - halfRect;
                } else {
                    mTipsRectBackground.left = mWidth - mTipsWidth;
                }
            }
        }

        mTipsRectBackground.right = mTipsRectBackground.left + mTipsWidth;
        int roundRadius = index == 0 ? 0 : mTipsBackgroundRadius;
        canvas.drawRoundRect(mTipsRectBackground, roundRadius, roundRadius, mPaint);
        //画三角形
        Path path = new Path();
        path.moveTo(circleX, topOffset);
        path.lineTo(circleX + mTriangleBottomWidth / 2, topOffset - mTriangleHeight);
        if (index == 0) {
            path.lineTo(circleX, topOffset - mTriangleHeight);
        } else {
            path.lineTo(circleX - mTriangleBottomWidth / 2, topOffset - mTriangleHeight);
        }
        path.close();
        canvas.drawPath(path, mPaint);
        //画文案
        String date = bean.date + "：";//日期
        String price = "¥" + bean.price;//价格
        float tipsTotalWidth = getFontWidth(mPaint, date + price);
        float leftPadding = (mTipsWidth - tipsTotalWidth) / 2;//算出左边padding，让文字居中

        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        float baseline = (mTipsRectBackground.bottom + mTipsRectBackground.top - fontMetrics.bottom - fontMetrics.top) / 2;
        mPaint.setColor(mTipsDateTextColor);
        canvas.drawText(date, mTipsRectBackground.left + leftPadding, baseline, mPaint);

        mPaint.setColor(Color.WHITE);
        float textWidth = getFontWidth(mPaint, date);
        canvas.drawText(price, mTipsRectBackground.left + leftPadding + textWidth, baseline, mPaint);
    }

    private void drawChart(Canvas canvas) {
        mPaint.setColor(chartLineColor);
        mPaint.setStrokeWidth(connectLineHeight);
        float textHeight = getFontHeight(mPaint);
        //Y轴高度
        float yHeight = mYMaxHeight * yRatio - textHeight;
        float xWidth = mWidth - yLeftOffets;
        double maxInList = mPriceList.get(0);
        double minInList = mPriceList.get(mPriceList.size() - 1);
        double delta = maxInList - minInList;

        for (int i = 0; i < mDateList.size(); i++) {
            DatePriceBean datePriceBean = mDateList.get(i);
            //先算出圆心的Y坐标
            float startY = yHeight - (float) (datePriceBean.price - minInList) / (float) delta * yHeight + textHeight;
            //算出圆心X坐标
            float dayStepWidth = (xWidth - xRightSpace) / (mDateList.size() - 1);//一天的宽度
            float startX = dayStepWidth * i + yLeftOffets;
            //画出连接线
            if (i < mDateList.size() - 1) {
                float nextStartX = dayStepWidth * (i + 1) + yLeftOffets;
                float nextStartY = yHeight - (float) (mDateList.get(i + 1).price - minInList) / (float) delta * yHeight + textHeight;


                if (nextStartY != startY) {
                    canvas.drawLine(startX, startY, nextStartX, nextStartY, mPaint);
                } else {
                    float radius = i == mCurrentIndex ? redStrokePointRadius : redPointRadius;
                    canvas.drawLine(startX + radius, startY, nextStartX - radius, nextStartY, mPaint);
                }
            }


            //画出圆点
            if (i != mCurrentIndex) {
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(startX, startY, redPointRadius, mPaint);
            } else {
                mCurrentIndexPoint.x = startX;
                mCurrentIndexPoint.y = startY;

                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setColor(Color.WHITE);
                canvas.drawCircle(startX, startY, redStrokePointRadius, mPaint);

                mPaint.setColor(chartLineColor);
                mPaint.setStyle(Paint.Style.STROKE);
                canvas.drawCircle(startX, startY, redStrokePointRadius, mPaint);


            }
        }

    }


    private void drawDivider(Canvas canvas) {

        float textHeight = getFontHeight(mPaint);
        //Y轴高度
        float yHeight = mYMaxHeight * yRatio - textHeight;
        float xWidth = mWidth - yLeftOffets;

        float everyPartHeight = yHeight / (mPriceList.size() - 1);
        for (int i = 0; i < mPriceList.size(); i++) {
            double value = mPriceList.get(i);
            float startY = textHeight + everyPartHeight * i;
            if (i != mPriceList.size() - 1)
                realDrawDivider(startY, xWidth, value == maxPrice || value == minPrice, canvas);
        }
        if (!mPriceList.contains(maxPrice)) {
            //最高价的还没画出来
            if (mPriceList.get(0) > maxPrice) {
                double maxInList = mPriceList.get(0);
                double minInList = mPriceList.get(mPriceList.size() - 1);
                double delta = maxInList - minInList;
                float startY = yHeight - (float) (maxPrice - minInList) / (float) delta * yHeight + textHeight;
                realDrawDivider(startY, xWidth, true, canvas);
            } else {
                //todo
            }
        }
        if (!mPriceList.contains(minPrice)) {
            //最低价的还没画出来
            if (mPriceList.get(mPriceList.size() - 1) < minPrice) {
                double maxInList = mPriceList.get(0);
                double minInList = mPriceList.get(mPriceList.size() - 1);
                double delta = maxInList - minInList;
                float startY = yHeight - (float) (minPrice - minInList) / (float) delta * yHeight + textHeight;
                realDrawDivider(startY, xWidth, true, canvas);
            } else {
                //todo
            }
        }
    }

    private void realDrawDivider(float startY, float xWidth, boolean isMinOrMax, Canvas canvas) {
        if (isMinOrMax) {
            mPaint.setColor(minOrMaxPriceDividerColor);
        } else {
            mPaint.setColor(normalPriceDividerColor);
        }
        int dividerCount = (int) (xWidth / (dividerSpaceWidth + dividerWidth));
        mPaint.setStrokeWidth(dividerHeight);
        startY = startY + dividerHeight / 4;
        for (int i = 0; i < dividerCount; i++) {
            float startX = i * (dividerSpaceWidth + dividerWidth) + yLeftOffets;
            canvas.drawLine(startX, startY, startX + dividerWidth, startY, mPaint);
        }
    }


    private void drawX(Canvas canvas) {
        mPaint.setColor(xyColor);
        mPaint.setStrokeWidth(DimenUtil.dip2px(getContext(), 0.5f));
        //Y轴高度
        float yHeight = mYMaxHeight * yRatio;
        float xWidth = mWidth - yLeftOffets;
        //画出X轴
        canvas.drawLine(yLeftOffets, yHeight, mWidth, yHeight, mPaint);
        int stepCount = mDateList.size() / xStepSize + 1;
        float dayStepWidth = (xWidth - xRightSpace) / (mDateList.size() - 1);//一天的宽度
        float stepWidth = dayStepWidth * (xStepSize - 1);
        float textHeight = getFontHeight(mPaint);
        //画出刻度
        for (int i = 0; i < mDateList.size(); i++) {
            if (i == 0 || (i + 1) % xStepSize == 0) {
                Log.d(TAG, "i = " + i + " ,stepCount = " + stepCount + " ,size = " + mDateList.size() + " ,stepWidth = " + stepWidth);
                //刻度
                mPaint.setColor(xyColor);
                canvas.drawLine(yLeftOffets + dayStepWidth * i, yHeight, yLeftOffets + dayStepWidth * i, yHeight + mScaleWidth, mPaint);
                //日期
                String date = mDateList.get(i).date;
                float textWidth = getFontWidth(mPaint, date);
                float startX = yLeftOffets + dayStepWidth * i - textWidth / 2;
                float startY = yHeight + mScaleWidth + textHeight;
                mPaint.setColor(xTextColor);
                canvas.drawText(date, startX, startY, mPaint);
            }
        }
    }


    private void drawY(Canvas canvas) {

        mPaint.setTextSize(DimenUtil.sp2px(getContext(), 9));
        mPaint.setStrokeWidth(DimenUtil.dip2px(getContext(), 0.5f));
        //Y轴高度
        float yHeight = mYMaxHeight * yRatio;

        float textHeight = getFontHeight(mPaint);


        float everyPartHeight = (yHeight - textHeight) / (mPriceList.size() - 1);
        //文字开始的X坐标
        int textStartX = 0;
        float maxWidth = 0;
        for (Double value : mPriceList) {
            //计算出最长的文字
            maxWidth = Math.max(maxWidth, getFontWidth(mPaint, String.valueOf(value)));
        }
        yMaxTextWidth = maxWidth;
        //左边偏移值
        float leftOffset = maxWidth + mScaleWidth + DimenUtil.dip2px(getContext(), 4);
        yLeftOffets = leftOffset;
        mPaint.setColor(xyColor);
        canvas.drawLine(leftOffset, textHeight, leftOffset, yHeight, mPaint);
        for (int i = 0; i < mPriceList.size(); i++) {
            Double value = mPriceList.get(i);
            //Y轴上的刻度
            mPaint.setColor(xyColor);
            canvas.drawLine(leftOffset - mScaleWidth,
                    textHeight + everyPartHeight * i,
                    leftOffset, textHeight + everyPartHeight * i, mPaint);
            //Y轴上的值
            mPaint.setColor(yTextColor);
            canvas.drawText(String.valueOf(value), textStartX, textHeight * 1.25f + everyPartHeight * i, mPaint);
        }

    }

    public void setxStepSize(int xStepSize) {
        this.xStepSize = xStepSize;
    }

    private float getFontHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        //文字基准线的下部距离-文字基准线的上部距离 = 文字高度
//        return (int) (Math.ceil(fm.descent - fm.ascent) + 2);
        return fm.descent - fm.ascent;
    }

    private float getFontWidth(Paint paint, String text) {
        return paint.measureText(text);
    }

    public static class DatePriceBean {
        private String date;
        private double price;

        public DatePriceBean() {
        }

        public DatePriceBean(String date, double price) {
            this.date = date;
            this.price = price;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }
    }
}
