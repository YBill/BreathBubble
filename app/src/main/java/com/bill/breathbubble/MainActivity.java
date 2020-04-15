package com.bill.breathbubble;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void handleClick1(View view) {
        Intent intent = new Intent(this, ThirdActivity.class);
        intent.putExtra("selectAll", true);
        intent.putExtra("forbid", true);
        startActivity(intent);
    }

    public void handleClick2(View view) {
        Intent intent = new Intent(this, ThirdActivity.class);
        intent.putExtra("selectAll", true);
        intent.putExtra("forbid", false);
        startActivity(intent);
    }

    public void handleClick3(View view) {
        Intent intent = new Intent(this, ThirdActivity.class);
        intent.putExtra("selectAll", false);
        intent.putExtra("forbid", false);
        startActivity(intent);
    }
}
