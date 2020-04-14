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
        startActivity(new Intent(this, FirstActivity.class));
    }

    public void handleClick2(View view) {
        startActivity(new Intent(this, SecondActivity.class));
    }
}
