package com.example.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.myapplication.view.ChatActivity;
import com.example.myapplication.view.RegisterActivity;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler.removeCallbacks(counterDown);
        handler.postDelayed(counterDown,3000);

    }

    Handler handler=new Handler();
    Runnable counterDown=new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
            startActivity(intent);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(counterDown);
    }
}
