package com.bill.breathbubble;

/**
 * Created by Bill on 2020-04-08.
 */
public class BubbleEntity {

    public float mCx, mCy; // 初始圆心坐标
    public float mRadius; // 初始半径
    public float mDx, mDy; // 圆心偏移坐标
    public float velocity; // 气泡偏移速度

    public boolean offsetLimit; // 判断气泡偏移到偏移坐标后再偏移回去
    public float textWidth, textHeight; // 储存文本宽高
    private float curVelocity; // 临时存储气泡偏移速度
    private float mDrawX, mDrawY; // 最终绘制的气泡坐标
    public boolean isSelect; // 是否被选择

    public BubbleEntity() {

    }

    public BubbleEntity(float mCx, float mCy, float mRadius) {
        this.mCx = mCx;
        this.mCy = mCy;
        this.mRadius = mRadius;
    }

    public BubbleEntity(float mCx, float mCy, float mRadius, float mDx, float mDy, float velocity) {
        this.mCx = mCx;
        this.mCy = mCy;
        this.mRadius = mRadius;
        this.mDx = mDx;
        this.mDy = mDy;
        this.velocity = velocity;
    }

    public float getDrawX() {
        if (mDrawX == 0)
            mDrawX = mCx;
        return mDrawX;
    }

    public float getDrawY() {
        if (mDrawY == 0)
            mDrawY = mCy;
        return mDrawY;
    }

    public void move() {
        if (offsetLimit) {
            curVelocity += velocity;
            if (curVelocity > 1f) {
                curVelocity = 1f;
                offsetLimit = false;
            }
        } else {
            curVelocity -= velocity;
            if (curVelocity < 0f) {
                curVelocity = 0f;
                offsetLimit = true;
            }
        }

        //根据当前帧变化量计算圆心偏移后的位置
        mDrawX = mCx + mDx * curVelocity;
        mDrawY = mCy + mDy * curVelocity;
    }

}
