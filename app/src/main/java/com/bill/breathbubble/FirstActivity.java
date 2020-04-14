package com.bill.breathbubble;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class FirstActivity extends AppCompatActivity {

    private BreatheBubbleSurfaceView breathView;
    private List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        breathView = findViewById(R.id.breath_view);

        String[] title = new String[]{
                "社会民生", "文化教育", "趣味知识", "育儿百科", "金融地产",
                "养生达人", "体育范", "前沿科技", "看房看车", "医疗健康"
        };

        list = Arrays.asList(title);

        breathView.postDelayed(new Runnable() {
            @Override
            public void run() {
                breathView.setData(list);
            }
        }, 2000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        breathView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        breathView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        breathView.onDestroy();
    }
}
