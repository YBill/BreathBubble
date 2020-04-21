package com.bill.breathbubble;

import android.os.Bundle;

import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

public class ThirdActivity extends AppCompatActivity {

    private BreatheBubbleView2 breathView;
    private List<String> list = new ArrayList<>();

    private AppCompatTextView startView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .navigationBarColor(R.color.color_f7f7f7)
                .init();

        startView = findViewById(R.id.tv_start);
        breathView = findViewById(R.id.breath_view);

        String[] title = new String[]{
                "社会民生", "文化教育", "趣味知识", "育儿百科", "金融地产",
                "养生达人", "体育范", "前沿科技", "看房看车", "医疗健康",
                "国际", "时政"
        };

        list = Arrays.asList(title);

        breathView.setData(list, false, false);

        breathView.setBubbleClickListener(new BreatheBubbleView2.BubbleClickListener() {
            @Override
            public void onClick(boolean isSelect) {
                if (isSelect) {
                    startView.setBackgroundResource(R.drawable.shape_interest_recommend_start_bg_select);
                } else {
                    startView.setBackgroundResource(R.drawable.shape_interest_recommend_start_bg);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        breathView.onDestroy();
    }
}
