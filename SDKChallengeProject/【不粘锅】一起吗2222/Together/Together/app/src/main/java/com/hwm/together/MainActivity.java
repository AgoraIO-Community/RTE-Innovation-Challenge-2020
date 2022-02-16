package com.hwm.together;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import com.hwm.together.view.activity.BaseActivity;
import com.hwm.together.view.activity.LoginActivity;
import com.hwm.together.view.activity.NavigationFragmentActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler.removeCallbacks(counterDown);
        handler.postDelayed(counterDown,500);

    }

    Handler handler=new Handler();
    Runnable counterDown=new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);//NavigationFragmentActivity
            startActivity(intent);
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(counterDown);
    }
}
