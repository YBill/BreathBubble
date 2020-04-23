package com.bill.breathbubble;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;

import java.util.List;
import java.util.Random;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

/**
 * Created by Bill on 2020/4/23.
 * Describe ：
 */
public class BreatheBubbleView3 extends ConstraintLayout {

    private Context mContext;

    private static final int BUBBLE_NUM = 12;
    private static final int CENTER_INDEX = 3;

    private Attribute[] attributes = new Attribute[BUBBLE_NUM];
    private AppCompatTextView[] textViews = new AppCompatTextView[BUBBLE_NUM];

    private static final int[] angles = new int[]{
            298, 247, 352, 0, 198, 47, 96, 147, 50, 78, 108, 136
    }; // 角度
    private static final int[] radii = new int[]{
            106, 97, 103, 0, 115, 102, 100, 98, 199, 178, 182, 187
    }; // 半径
    private static final int[] centerPosition = new int[]{
            89, 129, 169, 135
    }; // 基准气泡位置

    public BreatheBubbleView3(Context context) {
        super(context);
        init();
    }

    public BreatheBubbleView3(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BreatheBubbleView3(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mContext = getContext();
        this.setBackgroundColor(Color.parseColor("#999999"));
    }

    public void setData(List<String> list, int width, int height) {
        if (list == null || list.size() != BUBBLE_NUM)
            return;

        float scale = getScale(width, height);
//        scale = 1;

        initData(list, scale);
        addChild(scale);
    }

    private void addChild(float scale) {
        ConstraintSet set = new ConstraintSet();

        textViews[CENTER_INDEX] = getText(attributes[CENTER_INDEX], scale);
        this.addView(textViews[CENTER_INDEX]);

        int left = (int) dpToPx(centerPosition[0] * scale);
        int top = (int) dpToPx(centerPosition[1] * scale);
        int right = (int) dpToPx(centerPosition[2] * scale);
        int bottom = (int) dpToPx(centerPosition[3] * scale);
        set.connect(textViews[CENTER_INDEX].getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, left);
        set.connect(textViews[CENTER_INDEX].getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, top);
        set.connect(textViews[CENTER_INDEX].getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, right);
        set.connect(textViews[CENTER_INDEX].getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, bottom);
        set.constrainWidth(textViews[CENTER_INDEX].getId(), attributes[CENTER_INDEX].size);
        set.constrainHeight(textViews[CENTER_INDEX].getId(), attributes[CENTER_INDEX].size);

        for (int i = 0; i < attributes.length; i++) {
            if (i == CENTER_INDEX)
                i++;

            Attribute attribute = attributes[i];
            textViews[i] = getText(attribute, scale);
            this.addView(textViews[i]);
            set.constrainCircle(textViews[i].getId(), textViews[CENTER_INDEX].getId(), attribute.radius, attribute.angle);
            set.constrainWidth(textViews[i].getId(), attribute.size);
            set.constrainHeight(textViews[i].getId(), attribute.size);
        }

        set.applyTo(this);
    }

    private AppCompatTextView getText(final Attribute attribute, float scale) {
        final AppCompatTextView textView = new AppCompatTextView(mContext);
        textView.setId(View.generateViewId());
        textView.setText(attribute.text);
        textView.setTextSize(14 * scale);
        textView.setTextColor(Color.parseColor("#FF0707"));
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundResource(R.mipmap.interest_recommend_bubble);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attribute.isSelect = !attribute.isSelect;
                if (attribute.isSelect) {
                    textView.setBackgroundResource(R.mipmap.interest_recommend_bubble_select);
                    textView.setTextColor(Color.parseColor("#FFFFFF"));
                } else {
                    textView.setTag(false);
                    textView.setBackgroundResource(R.mipmap.interest_recommend_bubble);
                    textView.setTextColor(Color.parseColor("#FF0707"));
                }
            }
        });
        return textView;
    }

    public void move() {
        AnimatorSet animSet = new AnimatorSet();
        AnimatorSet.Builder builder = null;

        int index = 0;
        for (AppCompatTextView textView : textViews) {
            float x = attributes[index].moveX;
            float y = attributes[index].moveY;
            index++;

            ObjectAnimator titleXAnimator = ObjectAnimator.ofFloat(textView, "translationX", 0f, x);
            titleXAnimator.setRepeatMode(ValueAnimator.REVERSE);
            titleXAnimator.setRepeatCount(ValueAnimator.INFINITE);
            ObjectAnimator titleYAnimator = ObjectAnimator.ofFloat(textView, "translationY", 0f, y);
            titleYAnimator.setRepeatMode(ValueAnimator.REVERSE);
            titleYAnimator.setRepeatCount(ValueAnimator.INFINITE);

            if (builder == null) {
                builder = animSet.play(titleXAnimator).with(titleYAnimator);
            } else {
                builder.with(titleXAnimator).with(titleYAnimator);
            }

        }

        animSet.setDuration(1400);
        animSet.start();
    }

    private void initData(List<String> list, float scale) {
        Random random = new Random();
        final float moveDistance = 3;

        for (int i = 0; i < BUBBLE_NUM; i++) {
            int size = 80;
            if (i == 3)
                size = 100;
            else if (i == 2 || i == 4 || i == 8 || i == 11)
                size = 90;
            size *= scale;

            int radius = (int) (radii[i] * scale);

            float moveX = random.nextBoolean() ? moveDistance : -moveDistance;
            float moveY = random.nextBoolean() ? moveDistance : -moveDistance;
            moveX *= scale;
            moveY *= scale;

            attributes[i] = new Attribute(list.get(i), size, angles[i], radius, moveX, moveY);
        }

    }

    private float getScale(int width, int height) {
        if (width == 0 || height == 0) {
            return 1;
        }

        // 设计稿尺寸
        float dw = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 358+30, getResources().getDisplayMetrics());
        float dh = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 364+30, getResources().getDisplayMetrics());

        float scale = width / dw;
        float scaleH = height / dh;
        if (scaleH < scale)
            scale = scaleH;

        return scale;
    }

    class Attribute {
        int size;
        int angle;
        int radius;
        String text;
        float moveX;
        float moveY;
        boolean isSelect;

        public Attribute(String text, int size, int angle, int radius, float moveX, float moveY) {
            this.text = text;
            this.size = (int) dpToPx(size);
            this.angle = angle;
            this.radius = (int) dpToPx(radius);
            this.moveX = dpToPx(moveX);
            this.moveY = dpToPx(moveY);
        }
    }

    private float dpToPx(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, mContext.getResources().getDisplayMetrics());
    }


}
