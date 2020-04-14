package com.bill.breathbubble;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.Nullable;

/**
 * Created by Bill on 2020/4/10.
 * Describe ：
 */
public class BreatheBubbleView extends View {

    private List<BubbleEntity> mBubbleList;
    private final float mRadius = 120;
    private int TOUCH_SLOP;
    private Random mRandom;

    private int mViewWidth, mViewHeight;

    private List<String> mTitleList;

    private Paint mTextPaint;
    private Bitmap[] mImgBitmap;
    private Bitmap[] mSelectImgBitmap;
    private Thread mThread;

    private boolean mQuit = false;

    public BreatheBubbleView(Context context) {
        super(context);
        init();
    }

    public BreatheBubbleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BreatheBubbleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setClickable(true);
        setFocusable(true);
        setFocusableInTouchMode(true);

        TOUCH_SLOP = ViewConfiguration.get(getContext()).getScaledTouchSlop();

        mRandom = new Random();

        float mTextSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 14, getContext().getResources().getDisplayMetrics());

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(mTextSize);

        mThread = new Thread(mRunnable);
    }

    private void initData(int width, int height, int size) {
        mBubbleList = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            BubbleEntity bean = new BubbleEntity();
            bean.mRadius = mRadius;

            // 20-50
            int dx = mRandom.nextInt(30) + 20;
            int dy = mRandom.nextInt(30) + 20;

            bean.mDx = mRandom.nextBoolean() ? dx : -dx;
            bean.mDy = mRandom.nextBoolean() ? dy : -dy;

            bean.mCx = mRandom.nextInt((int) (width - 2 * bean.mRadius)) + bean.mRadius;
            bean.mCy = mRandom.nextInt((int) (height - 2 * bean.mRadius)) + bean.mRadius;

            // 0.01-0.02
            bean.velocity = (mRandom.nextInt(1000) + 1000) / 100000f;

            checkBorder(bean, width, height);
            checkCollide(bean, width, height);

            mBubbleList.add(bean);
        }

    }

    // 检测边界
    private void checkBorder(BubbleEntity entity, int width, int height) {
        if (entity.mCx + entity.mDx - entity.mRadius < 0) {
            entity.mCx = entity.mRadius - entity.mDx;
        }
        if (entity.mCx + entity.mDx + entity.mRadius > width) {
            entity.mCx = width - entity.mRadius - entity.mDx;
        }
        if (entity.mCy + entity.mDx - entity.mRadius < 0) {
            entity.mCy = entity.mRadius - entity.mDy;
        }
        if (entity.mCy + entity.mDy + entity.mRadius > height) {
            entity.mCy = height - entity.mRadius - entity.mDy;
        }
    }

    // 检测碰撞
    private void checkCollide(BubbleEntity entity, int width, int height) {
        for (int i = 0; i < mBubbleList.size(); i++) {
            BubbleEntity bean = mBubbleList.get(i);
            if (checkInCircle(entity, bean)) {
                entity.mCx = mRandom.nextInt((int) (width - 2 * entity.mRadius)) + entity.mRadius;
                entity.mCy = mRandom.nextInt((int) (height - 2 * entity.mRadius)) + entity.mRadius;
                checkBorder(entity, width, height);
                checkCollide(entity, width, height);
            }
        }
    }

    private boolean checkInCircle(BubbleEntity bean1, BubbleEntity bean2) {
        boolean isInCircle;
        if (Math.sqrt(Math.pow(bean1.mCx - bean2.mCx, 2) + Math.pow(bean1.mCy - bean2.mCy, 2))
                <= bean1.mRadius + bean2.mRadius) {
            isInCircle = true;
        } else if (Math.sqrt(Math.pow(bean1.mCx + bean1.mDx - bean2.mCx, 2) +
                Math.pow(bean1.mCy + bean1.mDy - bean2.mCy, 2)) <= bean1.mRadius + bean2.mRadius) {
            isInCircle = true;
        } else if (Math.sqrt(Math.pow(bean1.mCx - bean2.mDx - bean2.mCx, 2) +
                Math.pow(bean1.mCy - bean2.mDy - bean2.mCy, 2)) <= bean1.mRadius + bean2.mRadius) {
            isInCircle = true;
        } else if (Math.sqrt(Math.pow(bean1.mCx + bean1.mDx - bean2.mCx - bean2.mDx, 2) +
                Math.pow(bean1.mCy + bean1.mDy - bean2.mCy - bean2.mDy, 2)) <= bean1.mRadius + bean2.mRadius) {
            isInCircle = true;
        } else {
            isInCircle = false;
        }
        return isInCircle;
    }

    public void setData(List<String> list) {
        if (list == null)
            return;

        mTitleList = list;
        if (mViewWidth > 0 && mViewHeight > 0) {
            loadData();
        }
    }

    private void loadData() {
        initData(mViewWidth, mViewHeight, mTitleList.size());
        initDrawData();
        invalidate();
        mThread.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;

        if (mTitleList != null) {
            loadData();
        }

    }

    public void onDestroy() {
        mQuit = true;
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            while (true) {

                if (mQuit) {
                    return;
                }

                final long startTime = AnimationUtils.currentAnimationTimeMillis();

                for (int i = 0; i < mBubbleList.size(); i++) {
                    BubbleEntity bean = mBubbleList.get(i);
                    bean.move();
                }

                long needSleepTime = AnimationUtils.currentAnimationTimeMillis() - startTime;
                final long sleepTime = 16 - needSleepTime;

                if (sleepTime > 0) {
                    SystemClock.sleep(sleepTime);
                }

                postInvalidate();
            }
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCircle(canvas);
    }

    private void initDrawData() {
        if (mBubbleList == null)
            return;

        mImgBitmap = new Bitmap[mBubbleList.size()];
        for (int i = 0; i < mBubbleList.size(); i++) {
            mImgBitmap[i] = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.icon_qp_2);
        }

        mSelectImgBitmap = new Bitmap[mBubbleList.size()];
        for (int i = 0; i < mBubbleList.size(); i++) {
            mSelectImgBitmap[i] = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.icon_qp_select);
        }
    }

    private void drawCircle(Canvas canvas) {
        if (mBubbleList == null)
            return;

        for (int i = 0; i < mBubbleList.size(); i++) {
            BubbleEntity bean = mBubbleList.get(i);

            float curCX = bean.getDrawX();
            float curCY = bean.getDrawY();
            float radius = bean.mRadius;

            // 绘制气泡
            Rect rect = new Rect((int) (curCX - radius), (int) (curCY - radius), (int) (curCX + radius), (int) (curCY + radius));
            if (bean.isSelect)
                canvas.drawBitmap(mSelectImgBitmap[i], null, rect, null);
            else
                canvas.drawBitmap(mImgBitmap[i], null, rect, null);

            // 绘制文字
            String text = mTitleList.get(i) + i;
            if (bean.textWidth == 0 || bean.textHeight == 0) {
                Rect mBound = new Rect();
                mTextPaint.getTextBounds(text, 0, text.length(), mBound);
                bean.textWidth = mBound.width();
                bean.textHeight = mBound.height();
            }
            if (bean.isSelect)
                mTextPaint.setColor(Color.parseColor("#FFFFFF"));
            else
                mTextPaint.setColor(Color.parseColor("#EB5252"));

            canvas.drawText(text, curCX - bean.textWidth / 2f, curCY + bean.textHeight / 2f, mTextPaint);
        }
    }

    private float beforeX, beforeY; // 处理点击

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                beforeX = event.getX();
                beforeY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                float x = event.getX();
                float y = event.getY();
                if (Math.abs(x - beforeX) < TOUCH_SLOP && Math.abs(y - beforeY) < TOUCH_SLOP) {
                    checkClickBubble(x, y);
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    // 检测点击某个气泡
    private void checkClickBubble(float x, float y) {
        if (mBubbleList == null)
            return;

        for (int i = 0; i < mBubbleList.size(); i++) {
            BubbleEntity bean = mBubbleList.get(i);
            if (checkInCircle(x, y, 0, bean.getDrawX(), bean.getDrawY(), bean.mRadius)) {
                bean.isSelect = !bean.isSelect;
                break;
            }
        }
    }

    // 根据平方根公式计算两点之间距离判断是否点击在圆内
    private boolean checkInCircle(float x1, float y1, float r1, float x2, float y2, float r2) {
        boolean isInCircle;
        if (Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)) <= r1 + r2) {
            isInCircle = true;
        } else {
            isInCircle = false;
        }
        return isInCircle;
    }
}