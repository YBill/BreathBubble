package com.bill.breathbubble;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class FiveActivity extends AppCompatActivity {

    private FrameLayout bubbleGroup;

    private List<String> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .navigationBarColor(R.color.color_f7f7f7)
                .init();
        setContentView(R.layout.activity_five);
        bubbleGroup = findViewById(R.id.fl_bubble_group);

        initData();

        final BreatheBubbleView3 constraintLayout = new BreatheBubbleView3(this);
        bubbleGroup.addView(constraintLayout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) constraintLayout.getLayoutParams();
        params.gravity = Gravity.CENTER;

        bubbleGroup.post(new Runnable() {
            @Override
            public void run() {
                int width = bubbleGroup.getWidth();
                int height = bubbleGroup.getHeight();
                constraintLayout.setData(mList, width, height);
            }
        });

        findViewById(R.id.tv_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraintLayout.move();
            }
        });


    }

    private void initData() {
        String[] str = new String[]{"健康", "美食", "文化", "时政", "社会", "体育",
                "军事", "教育", "科技", "法制", "国际", "财经"};

        mList = Arrays.asList(str);
    }

}
