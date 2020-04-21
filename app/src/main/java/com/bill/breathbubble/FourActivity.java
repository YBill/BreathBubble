package com.bill.breathbubble;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;

import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

public class FourActivity extends AppCompatActivity {

    private AppCompatTextView[] textViews = new AppCompatTextView[12];
    private Attribute[] attributes = new Attribute[12];

    private float moveX, moveY;

    private Random mRandom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four);
        textViews[0] = findViewById(R.id.tv_1);
        textViews[1] = findViewById(R.id.tv_2);
        textViews[2] = findViewById(R.id.tv_3);
        textViews[3] = findViewById(R.id.tv_4);
        textViews[4] = findViewById(R.id.tv_5);
        textViews[5] = findViewById(R.id.tv_6);
        textViews[6] = findViewById(R.id.tv_7);
        textViews[7] = findViewById(R.id.tv_8);
        textViews[8] = findViewById(R.id.tv_9);
        textViews[9] = findViewById(R.id.tv_10);
        textViews[10] = findViewById(R.id.tv_11);
        textViews[11] = findViewById(R.id.tv_12);

        for (final AppCompatTextView textView : textViews) {
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (textView.getTag() == null || !(boolean) textView.getTag()) {
                        textView.setTag(true);
                        textView.setBackgroundResource(R.mipmap.interest_recommend_bubble_select);
                        textView.setTextColor(Color.parseColor("#FFFFFF"));
                    } else {
                        textView.setTag(false);
                        textView.setBackgroundResource(R.mipmap.interest_recommend_bubble);
                        textView.setTextColor(Color.parseColor("#FF0707"));
                    }
                }
            });
        }

        moveX = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
        moveY = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
        Log.e("Bill", moveX + "|" + moveY);

        mRandom = new Random();

        attributes[0] = new Attribute(moveX, moveY);
        attributes[1] = new Attribute(-moveX, moveY);
        attributes[2] = new Attribute(-moveX, -moveY);
        attributes[3] = new Attribute(0, -moveY);
        attributes[4] = new Attribute(-moveX, moveY);
        attributes[5] = new Attribute(moveX, -moveY);
        attributes[6] = new Attribute(-moveX, -moveY);
        attributes[7] = new Attribute(moveX, moveY);
        attributes[8] = new Attribute(-moveX, -moveY);
        attributes[9] = new Attribute(-moveX, moveY);
        attributes[10] = new Attribute(moveX, -moveY);
        attributes[11] = new Attribute(-moveX, moveY);


        float designWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 375, getResources().getDisplayMetrics());

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        float screenWidth = wm.getDefaultDisplay().getWidth();

        float scale = screenWidth / designWidth;

        moveX = moveX * scale;
        moveY = moveY * scale;

        Log.e("Bill", designWidth + "|" + screenWidth + "|" + scale + "|" + moveX);

        View bubbleView = findViewById(R.id.bubble_view);
        bubbleView.setScaleX(scale);
        bubbleView.setScaleX(scale);
    }

    public void handleClick(View view) {
        AnimatorSet animSet = new AnimatorSet();
        AnimatorSet.Builder builder = null;

        int index = 0;
        for (AppCompatTextView textView : textViews) {
            float x = attributes[index].x;
            float y = attributes[index].y;
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

        animSet.setDuration(1000);
        animSet.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    class Attribute {
        float x;
        float y;

        Attribute(float x, float y) {
            this.x = x;
            this.y = y;
        }

    }
}
