package com.bill.breathbubble;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewConfiguration;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Bill on 2020-04-08.
 */
public class BreatheBubbleSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private List<BubbleEntity> mBubbleList;
    private DrawThread mDrawThread;
    private final float mRadius = 120;
    private int TOUCH_SLOP;
    private Random mRandom;

    private int mViewWidth, mViewHeight;

    private List<String> mTitleList;

    public BreatheBubbleSurfaceView(Context context) {
        super(context);
        init();
    }

    public BreatheBubbleSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BreatheBubbleSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setClickable(true);
        setFocusable(true);
        setFocusableInTouchMode(true);

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        // 背景透明
        setZOrderOnTop(true);
        holder.setFormat(PixelFormat.TRANSLUCENT);

        mDrawThread = new DrawThread(getContext());
        mDrawThread.start();

        TOUCH_SLOP = ViewConfiguration.get(getContext()).getScaledTouchSlop();

        mRandom = new Random();
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
        mDrawThread.initData(mBubbleList, mTitleList);
    }

    public void onResume() {
        Log.e("Bill", "onResume");
        synchronized (mDrawThread) {
            mDrawThread.mRunning = true;
            mDrawThread.notify();
        }
    }

    public void onPause() {
        Log.e("Bill", "onPause");
        synchronized (mDrawThread) {
            mDrawThread.mRunning = false;
            mDrawThread.notify();
        }

    }

    public void onDestroy() {
        Log.e("Bill", "onDestroy");
        synchronized (mDrawThread) {
            mDrawThread.mQuit = true;
            mDrawThread.notify();
        }

        getHolder().removeCallback(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.e("Bill", "surfaceCreated");
        synchronized (mDrawThread) {
            mDrawThread.mSurfaceHolder = holder;
            mDrawThread.notify();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.e("Bill", "surfaceDestroyed");
        synchronized (mDrawThread) {
            mDrawThread.mSurfaceHolder = holder;
            mDrawThread.notify();           //唤醒
            while (mDrawThread.mActive) {
                try {
                    mDrawThread.wait();     //等待
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private static class DrawThread extends Thread {

        Context mContext;
        boolean mRunning, mActive, mQuit;
        List<BubbleEntity> mList;
        SurfaceHolder mSurfaceHolder;
        Paint mPaint;
        Paint mTextPaint;

        Bitmap[] mImgBitmap;
        Bitmap[] mSelectImgBitmap;

        List<String> titleList;

        public DrawThread(Context context) {
            this.mContext = context;
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

            float mTextSize = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_SP, 14, context.getResources().getDisplayMetrics());

            mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mTextPaint.setTextSize(mTextSize);

        }

        private void initData(List<BubbleEntity> list, List<String> titleList) {
            this.mList = list;
            this.titleList = titleList;
            mImgBitmap = new Bitmap[mList.size()];
            for (int i = 0; i < mList.size(); i++) {
                mImgBitmap[i] = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_qp_2);
            }

            mSelectImgBitmap = new Bitmap[mList.size()];
            for (int i = 0; i < mList.size(); i++) {
                mSelectImgBitmap[i] = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_qp_select);
            }
        }

        @Override
        public void run() {
            while (true) {

                long needSleepTime = 0;

                synchronized (this) {

                    if (!processDrawThreadState()) {
                        return;
                    }

                    if (mRunning && mSurfaceHolder != null) {
                        //动画开始时间
                        final long startTime = AnimationUtils.currentAnimationTimeMillis();
                        //处理画布并进行绘制
                        processDrawCanvas();
                        //绘制时间
                        needSleepTime = AnimationUtils.currentAnimationTimeMillis() - startTime;
                    }

                }

                processDrawThreadSleep(needSleepTime);


            }

        }

        private boolean processDrawThreadState() {
            //处理没有运行 或者 Holder 为 null 的情况
            while (mSurfaceHolder == null || !mRunning) {
                if (mActive) {
                    mActive = false;
                    notify();   //唤醒
                }
                if (mQuit)
                    return false;
                try {
                    wait();     //等待
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //其他情况肯定是活动状态
            if (!mActive) {
                mActive = true;
                notify();       //唤醒
            }
            return true;
        }

        private void processDrawThreadSleep(long drawTime) {
            //需要睡眠时间
            final long needSleepTime = 16 - drawTime;

            if (needSleepTime > 0) {
                try {
                    Thread.sleep(needSleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void processDrawCanvas() {
            Canvas mCanvas = null;
            try {
                mCanvas = mSurfaceHolder.lockCanvas(); //加锁画布
                if (mCanvas != null) {          //防空保护
                    //清屏操作
                    mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    drawCircle(mCanvas);    //真正开始画 SurfaceView 的地方
                }
            } catch (Exception ignored) {

            } finally {
                if (mCanvas != null) {
                    mSurfaceHolder.unlockCanvasAndPost(mCanvas); //释放canvas锁，并显示视图
                }
            }
        }

        private void drawCircle(Canvas canvas) {
//            canvas.drawColor(Color.TRANSPARENT);
            if (mList == null)
                return;

            for (int i = 0; i < mList.size(); i++) {
                BubbleEntity bean = mList.get(i);

                bean.move();

                float curCX = bean.getDrawX();
                float curCY = bean.getDrawY();
                float radius = bean.mRadius;

                /*LinearGradient linearGradient = new LinearGradient(curCX - bean.mRadius,
                        curCY - bean.mRadius, curCX - bean.mRadius, curCY + bean.mRadius,
                        new int[]{Color.RED, Color.BLUE}, null, LinearGradient.TileMode.REPEAT);
                mPaint.setShader(linearGradient);
                canvas.drawCircle(curCX, curCY, bean.mRadius, mPaint);*/

                // 绘制气泡
                Rect rect = new Rect((int) (curCX - radius), (int) (curCY - radius), (int) (curCX + radius), (int) (curCY + radius));
                if (bean.isSelect)
                    canvas.drawBitmap(mSelectImgBitmap[i], null, rect, null);
                else
                    canvas.drawBitmap(mImgBitmap[i], null, rect, null);

                // 绘制文字
                String text = titleList.get(i) + i;
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
