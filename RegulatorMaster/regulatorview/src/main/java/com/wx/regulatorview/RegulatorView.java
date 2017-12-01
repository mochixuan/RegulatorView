package com.wx.regulatorview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by wangxuan on 2017/11/29.
 */

public class RegulatorView extends View {

    private Paint mPaint;

    private float mSecondCircleRadius;
    private float mThreeCircleRadius;

    private int mFirstCircleColor;
    private int mSecondCircleColor;
    private int mSecondCircleShadowColor;
    private int mThreeCircleColor;

    private int mSecondCircleWidth;
    private float mSecondCircleShadowRadius ;
    private float mCurShadowRadius ;
    private int mThreeCircleWidth;
    private int mGap1Width;

    private int mThreeRingAngle ;

    private float mSecondScale;

    private int[] mColors;

    private int mCurAngle;

    private float mCenterX;
    private float mCenterY;

    private int mPreX;
    private int mPreY;

    private int mPointerColor ;
    private float mPointScale  ;     //长度
    private float mPointerWidth ;

    private String mSymbol; //符号 如温度的°，或者%
    private int mSymbolColor ;
    private float mSymbolTextSize ;

    private String mCenterTitle;
    private int mCenterTitleColor ;
    private float mCenterTitleTextSize ;

    private String mBottomTitle;
    private int mBottomTitleColor ;
    private float mBottomTitleTextSize ;

    private float mBottomCenterGap ;
    private float mSymbolCenterGap ;
    private float mSymbolMoveTopGap ;

    private OnProgressChangeListener mProgressChangeListener;

    private ObjectAnimator mBacklightAnim;
    private int mBacklightAnimDurtion = 3000;
    private boolean isOpenBacklightAnim = false;

    private boolean isForbidSlide  = false;

    public RegulatorView(Context context) {
        this(context, null);
    }

    public RegulatorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RegulatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.RegulatorView);
        mFirstCircleColor = typedArray.getColor(R.styleable.RegulatorView_first_circle_color,0xfff0f0f0);
        mSecondCircleColor = typedArray.getColor(R.styleable.RegulatorView_second_circle_color,0xfff5f5f5);
        mSecondCircleShadowColor = typedArray.getColor(R.styleable.RegulatorView_secondcircle_shadow_color,0x66ff0000);
        mThreeCircleColor = typedArray.getColor(R.styleable.RegulatorView_three_circle_color,0xffe0e0e0);

        mSecondCircleWidth = (int) typedArray.getDimension(R.styleable.RegulatorView_second_circle_width,DensityUtil.dptopx(context, 24f));
        mCurShadowRadius = mSecondCircleShadowRadius = typedArray.getDimension(R.styleable.RegulatorView_secondcircle_shadow_width,DensityUtil.dptopx(context,10f));
        mThreeCircleWidth = (int) typedArray.getDimension(R.styleable.RegulatorView_three_circle_width,DensityUtil.dptopx(context, 20f));
        mGap1Width = (int) typedArray.getDimension(R.styleable.RegulatorView_gap1_width,DensityUtil.dptopx(context, 24f));

        mThreeRingAngle = typedArray.getInteger(R.styleable.RegulatorView_three_ring_angle,300);
        mSecondScale = typedArray.getFloat(R.styleable.RegulatorView_second_scale,0.25f);

        mPointerColor = typedArray.getColor(R.styleable.RegulatorView_pointer_color,0xffff0000);
        mPointScale = typedArray.getFloat(R.styleable.RegulatorView_pointer_scale,0.5f);
        mPointerWidth = typedArray.getDimension(R.styleable.RegulatorView_pointer_width,DensityUtil.dptopx(context,4f));

        mSymbol = typedArray.getString(R.styleable.RegulatorView_symbol);
        mCenterTitle = typedArray.getString(R.styleable.RegulatorView_center_title);
        mBottomTitle = typedArray.getString(R.styleable.RegulatorView_bottom_title);

        mSymbolColor = typedArray.getColor(R.styleable.RegulatorView_symbol_color,0xff444444);
        mCenterTitleColor = typedArray.getColor(R.styleable.RegulatorView_center_title_color,0xff333333);
        mBottomTitleColor = typedArray.getColor(R.styleable.RegulatorView_bottom_title_color,0xff949494);

        mSymbolTextSize = typedArray.getDimension(R.styleable.RegulatorView_symbol_size,DensityUtil.sptopx(context,24f));
        mCenterTitleTextSize = typedArray.getDimension(R.styleable.RegulatorView_center_title_size,DensityUtil.sptopx(context,46f));
        mBottomTitleTextSize = typedArray.getDimension(R.styleable.RegulatorView_bottom_title_size,DensityUtil.sptopx(context,12f));


        mBottomCenterGap = typedArray.getDimension(R.styleable.RegulatorView_bottomcenter_gap,DensityUtil.dptopx(context,16f));
        mSymbolCenterGap = typedArray.getDimension(R.styleable.RegulatorView_symbolcenter_gap,DensityUtil.dptopx(context,2f));
        mSymbolMoveTopGap  = typedArray.getDimension(R.styleable.RegulatorView_symbolmovetop_gap,DensityUtil.dptopx(context,4f));

        mBacklightAnimDurtion = typedArray.getInteger(R.styleable.RegulatorView_backlight_durtion,3000);
        isOpenBacklightAnim = typedArray.getBoolean(R.styleable.RegulatorView_isopen_backlightanim,false);
        isForbidSlide = typedArray.getBoolean(R.styleable.RegulatorView_is_forbid_slide,false);

        typedArray.recycle();

        mCurAngle = (360-mThreeRingAngle)/2 + 90;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        setLayerType(LAYER_TYPE_SOFTWARE, null); //没办法了，要关闭硬件加速
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isOpenBacklightAnim) {
            onStartBacklightAnim();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //防止内存泄漏先关闭动画
        onEndBacklightAnim();
        onStopAutoFlingAnim();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mCenterX = getMeasuredWidth() / 2f;
        mCenterY = getMeasuredHeight() / 2f;

        drawAllCircle(canvas);

        drawPointer(canvas);

        drawText(canvas);

        if (mProgressChangeListener != null) {
            float progress = (mCurAngle+90+mThreeRingAngle/2f)%360;
            mProgressChangeListener.onProgress(progress/mThreeRingAngle);
        }
    }

    /**
     * 绘制所以的圆
     * @param canvas
     */
    private void drawAllCircle(Canvas canvas) {
        //绘制第二个圆(圆环)
        mSecondCircleRadius = Math.min(getMeasuredWidth(), getMeasuredHeight()) * mSecondScale;
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mSecondCircleColor);
        mPaint.setShadowLayer(mCurShadowRadius, 0, 0, mSecondCircleShadowColor);
        mPaint.setStrokeWidth(mSecondCircleWidth);
        canvas.drawCircle(mCenterX, mCenterY, mSecondCircleRadius, mPaint);
        mPaint.setShadowLayer(0, 0, 0, Color.RED);

        //绘制第一个圆(实心圆)
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(0);
        mPaint.setColor(mFirstCircleColor);
        canvas.drawCircle(mCenterX, mCenterY, mSecondCircleRadius - mSecondCircleWidth / 2, mPaint);

        //绘制第三个圆(外圆环)
        mThreeCircleRadius = Math.min(getMeasuredWidth(), getMeasuredHeight()) * mSecondScale + mGap1Width + mSecondCircleWidth / 2f;
        RectF rectF = new RectF();
        rectF.left = mCenterX - mThreeCircleRadius;
        rectF.right = mCenterX + mThreeCircleRadius;
        rectF.top = mCenterY - mThreeCircleRadius;
        rectF.bottom = mCenterY + mThreeCircleRadius;
        int startAngle = (360 - mThreeRingAngle) / 2 + 90;
        mPaint.setStrokeWidth(mThreeCircleWidth);
        mPaint.setColor(mThreeCircleColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawArc(rectF, startAngle, mThreeRingAngle, false, mPaint);

        //绘制颜色
        if (mColors != null && mColors.length != 0) {
            canvas.save();
            canvas.rotate(90, mCenterX, mCenterY);
            if (mColors.length == 1) {
                mPaint.setColor(mColors[0]);
            } else if (mColors.length>1) {
                SweepGradient sweepGradient = new SweepGradient(mCenterX, mCenterY, mColors, null);
                mPaint.setShader(sweepGradient);
            }
            canvas.drawArc(rectF, (360 - mThreeRingAngle) / 2, mThreeRingAngle, false, mPaint);
            mPaint.setShader(null);
            canvas.restore();
        }

    }

    /**
     * 绘制指针
     * @param canvas
     */
    private void drawPointer(Canvas canvas) {
        mPaint.setColor(mPointerColor);
        mPaint.setStrokeWidth(mPointerWidth);
        canvas.save();
        canvas.rotate(mCurAngle,mCenterX,mCenterY);
        canvas.drawLine(mCenterX+mSecondCircleRadius-mSecondCircleWidth*mPointScale/2f,mCenterY,mCenterX+mSecondCircleRadius + mSecondCircleWidth*mPointScale/2f,mCenterY,mPaint);
        canvas.restore();
    }

    /**
     * 绘制文字
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        float centerTitleHight = 0f;
        float centerLength = 0f;
        if (mCenterTitle != null && mCenterTitle.length()>0) {
            mPaint.setColor(mCenterTitleColor);
            mPaint.setStrokeWidth(1);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setTextSize(mCenterTitleTextSize);
            mPaint.setTextAlign(Paint.Align.CENTER);
            centerTitleHight = -mPaint.ascent() - mPaint.descent();
            centerLength = mPaint.measureText(mCenterTitle);
            canvas.drawText(mCenterTitle,mCenterX,mCenterY + centerTitleHight/2f,mPaint);
        }
        if (mBottomTitle != null && mBottomTitle.length()>0) {
            mPaint.setColor(mBottomTitleColor);
            mPaint.setTextSize(mBottomTitleTextSize);
            float bottomTitleHeight = -mPaint.ascent() - mPaint.descent();
            canvas.drawText(mBottomTitle,mCenterX,mCenterY+bottomTitleHeight/2f+centerTitleHight/2f+mBottomCenterGap,mPaint);
        }
        if (mSymbol != null && mSymbol.length()>0) {
            mPaint.setColor(mSymbolColor);
            mPaint.setTextSize(mSymbolTextSize);
            mPaint.setTextAlign(Paint.Align.LEFT);
            float symbolHight = -mPaint.ascent() - mPaint.descent();
            canvas.drawText(mSymbol,mCenterX+centerLength/2f+mSymbolCenterGap,mCenterY-centerTitleHight/2f+symbolHight-mSymbolMoveTopGap,mPaint);
        }
    }

    /**
     * 只有当单机在第三个圆环上才接管触摸事件，并开始处理触摸事件
     * 为防止小范围抖动(很小),但优化下更好：
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (isForbidSlide) return false;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onStopAutoFlingAnim();  //有动画立刻停止
                boolean isTakeOver = isTakeOverTouch(event.getX(),event.getY()); //第一次单机在圆环上才接管触摸
                if (isTakeOver) {
                    mPreX = (int) event.getX();
                    mPreY = (int) event.getY();
                    refreshAngle(mPreX,mPreY,true);
                    return true;
                } else {
                    return false;
                }
            case MotionEvent.ACTION_MOVE:
                refreshAngle((int) event.getX(),(int) event.getY(),false);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true; //consumption
    }

    /**
     * 判断是否接管触摸 两种情况
     */
    private boolean isTakeOverTouch(float downX,float downY) {

        //加个0.5防止有些人眼神或手不好点不到圆弧上
        float minRadius = mThreeCircleRadius - mThreeCircleWidth/2f - 0.5f;
        float maxRadius = mThreeCircleRadius + mThreeCircleWidth/2f + 0.5f;

        //到按下点到圆心的距离
        float distanceCircle = (float) Math.abs(Math.sqrt((downX-mCenterX)*(downX-mCenterX)+(downY-mCenterY)*(downY-mCenterY)));
        if (distanceCircle >= minRadius && distanceCircle <= maxRadius) {
            if (mThreeRingAngle > 180 && downY > mCenterY) {
                float angle = (float) (Math.atan(Math.abs(downX-mCenterX)/Math.abs(downY-mCenterY))*180/Math.PI);
                if ((360-mThreeRingAngle)/2f > angle) {
                    return false;
                }
            } else if (mThreeRingAngle <= 180) {
                if (downY>mCenterY) {
                    return false;
                } else {
                    float angle = (float) (Math.atan(Math.abs(downX-mCenterX)/Math.abs(downY-mCenterY))*180/Math.PI);
                    if (angle > (360-mThreeRingAngle)/2f) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 计算滑动的角度，这里为了增加性能消耗做两点处理
     *  1.触摸点x,y与上一次进行对比 (理由：当手指放在上面不动会持续调用ACTION_MOVE,这时不需要刷新界面)
     *  2.判断滑动是否改动角度角度不变也不要刷新减小性能消耗
     *  tip: 当触摸到临界的焦点的默认为左边，个人认为开关左边挡位较小，误触时也不会有大问题
     *  tip1: 为什么取整，防止小范围抖动也可以提供性能
     */
    private void refreshAngle(int moveX,int moveY,boolean idDown) {
        if (!idDown && moveX == mPreX && moveY == mPreY) {
            return;
        }
        //相对于以中心竖直画一条Y轴的夹角
        int relativeAngle = (int) (Math.atan(Math.abs(moveX-mCenterX)/Math.abs(moveY-mCenterY))*180/Math.PI);
        //计算象限
        int angle ;
        if (moveX > mCenterX) {
            if (moveY > mCenterY) {
                angle = 90 - relativeAngle;
            } else {
                angle = 270 + relativeAngle;
            }
        } else {
            if (moveY > mCenterY) {
                angle = 90 + relativeAngle;
            } else {
                angle = 270 - relativeAngle;
            }
        }

        if (mCurAngle == angle ) return;

        float semiAngel;
        float rightAngle;
        float leftAngle;
        if ( mThreeRingAngle>180 ) {
            semiAngel  = (360-mThreeRingAngle)/2f;
            rightAngle = 90 - semiAngel;
            leftAngle = 90 + semiAngel;
            if (angle <= rightAngle || angle>=leftAngle) {
                mCurAngle = angle;
                invalidate();
            }
        } else {
            semiAngel  = mThreeRingAngle/2f;
            rightAngle = 270 + semiAngel;
            leftAngle = 270 - semiAngel;
            if (rightAngle >= angle && angle>=leftAngle) {
                mCurAngle = angle;
                invalidate();
            }
        }

    }

    public void setProgressChangeListener(OnProgressChangeListener progressChangeListener) {
        this.mProgressChangeListener = progressChangeListener;
    }

    /**
     * 开启背光灯动画
     */
    private void onStartBacklightAnim() {
        if (mBacklightAnim != null && mBacklightAnim.isRunning()) {
            return;
        }
        mBacklightAnim = ObjectAnimator.ofFloat(this,"curShadowRadius",0,mSecondCircleShadowRadius);
        mBacklightAnim.setDuration(mBacklightAnimDurtion);
        mBacklightAnim.setRepeatCount(ValueAnimator.INFINITE);
        mBacklightAnim.setRepeatMode(ValueAnimator.REVERSE);
        mBacklightAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mCurShadowRadius = mSecondCircleShadowRadius;
            }
        });
        mBacklightAnim.start();
    }

    private void onEndBacklightAnim() {
        if (mBacklightAnim != null && mBacklightAnim.isRunning()) {
            mBacklightAnim.removeAllListeners();
            mBacklightAnim.cancel();
            mBacklightAnim = null;
        }
    }

    public void setIsOpenBacklightAnim(boolean isOpen) {
        this.isOpenBacklightAnim = isOpen;
        if (isOpen) {
            onStartBacklightAnim();
        } else {
            onEndBacklightAnim();
        }
    }

    private void setCurShadowRadius(float curShadowRadius) {
        this.mCurShadowRadius = curShadowRadius;
        invalidate();
    }

    public void setThreeCircleColors(int[] colors) {
        this.mColors = colors;
        invalidate();
    }

    private ValueAnimator mAutoFlingAnim;
    public void setCurAngle(float progress,boolean isWantAnim) {
        if (isWantAnim) {
            if (mAutoFlingAnim != null && mAutoFlingAnim.isRunning()) {
                mAutoFlingAnim.cancel();
                mAutoFlingAnim.removeAllUpdateListeners();
                mAutoFlingAnim = null;
            }
            mAutoFlingAnim = new ValueAnimator();
            float curProgress = ((mCurAngle+90+mThreeRingAngle/2f)%360)/mThreeRingAngle;
            mAutoFlingAnim.setFloatValues(curProgress,progress);
            mAutoFlingAnim.setDuration((long) (Math.abs(progress-curProgress)*2000));
            mAutoFlingAnim.setInterpolator(new LinearInterpolator());
            mAutoFlingAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float value = (float) valueAnimator.getAnimatedValue();
                    int angle = (int) (value*mThreeRingAngle + (360-mThreeRingAngle)/2f+90)%360;
                    if (mCurAngle != angle) {
                        mCurAngle = angle;
                        invalidate();
                    }
                }
            });
            mAutoFlingAnim.start();
        } else {
            this.mCurAngle = (int) (progress*mThreeRingAngle + (360-mThreeRingAngle)/2f+90)%360;
            invalidate();
        }
    }

    private void onStopAutoFlingAnim() {
        if (mAutoFlingAnim != null && mAutoFlingAnim.isRunning()) {
            mAutoFlingAnim.cancel();
            mAutoFlingAnim.removeAllUpdateListeners();
            mAutoFlingAnim = null;
        }
    }

    public void setSecondCircleShadowColor(int shadowColor) {
        this.mSecondCircleShadowColor = shadowColor;
        invalidate();
    }

    public void setPointerColor(int pointerColor) {
        this.mPointerColor = pointerColor;
    }

    public void setCenterTitle(String title) {
        this.mCenterTitle = title;
        invalidate();
    }

    public boolean isForbidSlide() {
        return isForbidSlide;
    }

    public void setIsForbidSlide(boolean forbidSlide) {
        this.isForbidSlide = forbidSlide;
    }

    public void setBottomTitle(String title) {
        this.mBottomTitle = title;
        invalidate();
    }

    public void setSymbol(String symbol) {
        this.mSymbol = symbol;
        invalidate();
    }
}
