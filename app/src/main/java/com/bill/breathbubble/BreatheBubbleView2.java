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
import android.util.Log;
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
public class BreatheBubbleView2 extends View {

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

    public BreatheBubbleView2(Context context) {
        super(context);
        init();
    }

    public BreatheBubbleView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BreatheBubbleView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

    public float dp2Px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getContext().getApplicationContext().getResources().getDisplayMetrics());
    }

    private float realX(int width, float x) {
        return width * x / 375;
    }

    private float realY(int height, float y) {
        return height * y / 422;
    }

    private void initData(int width, int height, int size) {
        mBubbleList = new ArrayList<>(size);

        Log.e("Bill", width + "|" + height);

        float radius0 = realX(width, 80) / 2;
        float y0 = realY(height, 113) + radius0;
        float x0 = realX(width, 13) + radius0;
        BubbleEntity bean0 = new BubbleEntity(x0, y0, radius0, 20, 20, 0.00950f);
        mBubbleList.add(bean0);

        float radius1 = radius0;
        float y1 = realY(height, 200) + radius1;
        float x1 = realX(width, 18) + radius1;
        BubbleEntity bean1 = new BubbleEntity(x1, y1, radius1, -20, 20, 0.00950f);
        mBubbleList.add(bean1);

        float radius2 = realX(width, 90) / 2;
        float y2 = realY(height, 55) + radius2;
        float x2 = realX(width, 87) + radius2;
        BubbleEntity bean2 = new BubbleEntity(x2, y2, radius2, -20, -20, 0.00950f);
        mBubbleList.add(bean2);

        float radius3 = realX(width, 100) / 2;
        float y3 = realY(height, 152) + radius3;
        float x3 = realX(width, 97) + radius3;
        BubbleEntity bean3 = new BubbleEntity(x3, y3, radius3, 0, -40, 0.010f);
        mBubbleList.add(bean3);

        float radius4 = radius2;
        float y4 = realY(height, 266) + radius4;
        float x4 = realX(width, 66) + radius4;
        BubbleEntity bean4 = new BubbleEntity(x4, y4, radius4, -20, 20, 0.00950f);
        mBubbleList.add(bean4);

        float radius5 = radius0;
        float y5 = realY(height, 92) + radius5;
        float x5 = realX(width, 181) + radius5;
        BubbleEntity bean5 = new BubbleEntity(x5, y5, radius5, 20, -20, 0.00950f);
        mBubbleList.add(bean5);

        float radius6 = radius0;
        float y6 = realY(height, 172) + radius6;
        float x6 = realX(width, 206) + radius6;
        BubbleEntity bean6 = new BubbleEntity(x6, y6, radius6, -20, -20, 0.00950f);
        mBubbleList.add(bean6);

        float radius7 = radius0;
        float y7 = realY(height, 244) + radius7;
        float x7 = realX(width, 160) + radius7;
        BubbleEntity bean7 = new BubbleEntity(x7, y7, radius7, 20, 20, 0.00950f);
        mBubbleList.add(bean7);

        float radius8 = radius2;
        float y8 = realY(height, 28) + radius8;
        float x8 = realX(width, 254) + radius8;
        BubbleEntity bean8 = new BubbleEntity(x8, y8, radius8, -20, -20, 0.00950f);
        mBubbleList.add(bean8);

        float radius9 = radius0;
        float y9 = realY(height, 126) + radius9;
        float x9 = realX(width, 281) + radius9;
        BubbleEntity bean9 = new BubbleEntity(x9, y9, radius9, -20, 20, 0.00950f);
        mBubbleList.add(bean9);

        float radius10 = radius0;
        float y10 = realY(height, 216) + radius10;
        float x10 = realX(width, 281) + radius10;
        BubbleEntity bean10 = new BubbleEntity(x10, y10, radius10, 20, -20, 0.00950f);
        mBubbleList.add(bean10);

        float radius11 = radius0;
        float y11 = realY(height, 287) + radius11;
        float x11 = realX(width, 232) + radius11;
        BubbleEntity bean11 = new BubbleEntity(x11, y11, radius11, -5, 20, 0.00950f);
        mBubbleList.add(bean11);

        for (BubbleEntity bubbleEntity : mBubbleList) {
            bubbleEntity.isSelect = selectAll;
            if (forbid) {
                bubbleEntity.velocity = 0;
            }
        }

        /*for (int i = 0; i < size; i++) {
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
        }*/

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

    private boolean selectAll;
    private boolean forbid;

    public void setData(List<String> list, boolean selectAll, boolean forbid) {
        if (list == null)
            return;

        this.selectAll = selectAll;
        this.forbid = forbid;

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
//                    SystemClock.sleep(sleepTime);
                }

                SystemClock.sleep(8);

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
            mImgBitmap[i] = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.interest_recommend_bubble);
        }

        mSelectImgBitmap = new Bitmap[mBubbleList.size()];
        for (int i = 0; i < mBubbleList.size(); i++) {
            mSelectImgBitmap[i] = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.interest_recommend_bubble_select);
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
            if (bean.isSelect) {
                mTextPaint.setColor(Color.parseColor("#FFFFFF"));
                mTextPaint.setFakeBoldText(true);
            } else {
                mTextPaint.setColor(Color.parseColor("#FF0707"));
                mTextPaint.setFakeBoldText(false);
            }

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

        boolean isSelect = false;
        for (BubbleEntity bubbleEntity : mBubbleList) {
            if (bubbleEntity.isSelect) {
                isSelect = true;
                break;
            }
        }

        if (bubbleClickListener != null)
            bubbleClickListener.onClick(isSelect);
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

    private BubbleClickListener bubbleClickListener;

    public void setBubbleClickListener(BubbleClickListener bubbleClickListener) {
        this.bubbleClickListener = bubbleClickListener;
    }

    public interface BubbleClickListener {
        void onClick(boolean isSelect);
    }
}