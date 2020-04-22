package com.bill.breathbubble;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class FiveActivity extends AppCompatActivity {

    private FrameLayout bubbleGroup;

    private Attribute[] attributes = new Attribute[12];
    private AppCompatTextView[] textViews = new AppCompatTextView[12];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_five);
        bubbleGroup = findViewById(R.id.fl_bubble_group);

        initData();

        final ConstraintLayout constraintLayout = new ConstraintLayout(this);
        constraintLayout.setBackgroundColor(Color.parseColor("#999999"));

        ConstraintSet set = new ConstraintSet();

        textViews[3] = getText(attributes[3].text);
        constraintLayout.addView(textViews[3]);
        set.connect(textViews[3].getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, (int) dpToPx(89));
        set.connect(textViews[3].getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, (int) dpToPx(129));
        set.connect(textViews[3].getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, (int) dpToPx(169));
        set.connect(textViews[3].getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, (int) dpToPx(135));
        set.constrainWidth(textViews[3].getId(), attributes[3].size);
        set.constrainHeight(textViews[3].getId(), attributes[3].size);

        for (int i = 0; i < attributes.length; i++) {
            if (i == 3)
                i++;

            Attribute attribute = attributes[i];
            textViews[i] = getText(attribute.text);
            constraintLayout.addView(textViews[i]);
            set.constrainCircle(textViews[i].getId(), textViews[3].getId(), attribute.radius, attribute.angle);
            set.constrainWidth(textViews[i].getId(), attribute.size);
            set.constrainHeight(textViews[i].getId(), attribute.size);
        }

        set.applyTo(constraintLayout);


        bubbleGroup.addView(constraintLayout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) constraintLayout.getLayoutParams();
        params.gravity = Gravity.CENTER;


        float designWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 375, getResources().getDisplayMetrics());

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        float screenWidth = wm.getDefaultDisplay().getWidth();

        float scale = screenWidth / designWidth;

        Log.e("Bill", designWidth + "|" + screenWidth + "|" + scale);

        constraintLayout.setScaleX(scale);
        constraintLayout.setScaleY(scale);

        constraintLayout.post(new Runnable() {
            @Override
            public void run() {
                Log.e("Bill", constraintLayout.getWidth() + "|" + constraintLayout.getHeight());
            }
        });


    }


    private AppCompatTextView getText(String text) {
        final AppCompatTextView textView = new AppCompatTextView(this);
        textView.setId(View.generateViewId());
        textView.setText(text);
        textView.setTextSize(14);
        textView.setTextColor(Color.parseColor("#FF0707"));
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundResource(R.mipmap.interest_recommend_bubble);
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
        return textView;
    }

    public float dpToPx(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private void initData() {
        attributes[0] = new Attribute("健康", 80, 298, 106, 5, 5);
        attributes[1] = new Attribute("美食", 80, 247, 97, -5, 5);
        attributes[2] = new Attribute("文化", 90, 352, 103, -5, -5);
        attributes[3] = new Attribute("时政", 100, 0, 0, 0, -10);
        attributes[4] = new Attribute("社会", 90, 198, 115, -5, 5);
        attributes[5] = new Attribute("体育", 80, 47, 102, 5, -5);
        attributes[6] = new Attribute("军事", 80, 96, 100, -5, -5);
        attributes[7] = new Attribute("教育", 80, 147, 98, 5, 5);
        attributes[8] = new Attribute("科技", 90, 50, 199, -5, -5);
        attributes[9] = new Attribute("法制", 80, 78, 178, -5, 5);
        attributes[10] = new Attribute("国际", 80, 108, 182, 5, -5);
        attributes[11] = new Attribute("财经", 90, 136, 187, -5, 5);
    }

    class Attribute {
        int size;
        int angle;
        int radius;
        String text;
        float moveX;
        float moveY;

        public Attribute(String text, int size, int angle, int radius, float moveX, float moveY) {
            this.text = text;
            this.size = (int) dpToPx(size);
            this.angle = angle;
            this.radius = (int) dpToPx(radius);
            this.moveX = dpToPx(moveX);
            this.moveY = dpToPx(moveY);
        }
    }

    public void handleClick(View view) {
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

        animSet.setDuration(1000);
        animSet.start();
    }
}
