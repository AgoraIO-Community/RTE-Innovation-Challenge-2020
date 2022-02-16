package com.hwm.together.view.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.hwm.together.R;
import com.hwm.together.service.RingService;

import java.util.ArrayList;
import java.util.List;

public class RingingActivity extends BaseActivity {
    private static final String TAG = "RingingActivity";
    private TextView chaseName;
//    private MediaPlayer mMediaPlayer;

    private ImageView animation6,animation5,animation4,animation3,animation2,animation1,animation7,animation8,animation9,animation10,animation11,animation12;
    List<ImageView> animationListRight = new ArrayList<ImageView>();
    List<ImageView> animationListLeft = new ArrayList<ImageView>();

    //手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                |WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                |WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                |WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_ringing);

        initView();
        initAnimation();
        playMusic();
        Log.d(TAG, "onCreate: process id is" + android.os.Process.myPid());
    }

    private void initView(){
        chaseName = findViewById(R.id.ringing_content);
    }

    private void playMusic(){
        Intent intent = new Intent(this, RingService.class);
        startService(intent);
    }

    private void stopMusic(){
        Intent intent = new Intent(this,RingService.class);
        stopService(intent);
    }

    private void initAnimation(){
        animation6 = findViewById(R.id.ringing_animation6);
        animation5 = findViewById(R.id.ringing_animation5);
        animation4 = findViewById(R.id.ringing_animation4);
        animation3 = findViewById(R.id.ringing_animation3);
        animation2 = findViewById(R.id.ringing_animation2);
        animation1 = findViewById(R.id.ringing_animation1);

        animation7 = findViewById(R.id.ringing_animation7);
        animation8 = findViewById(R.id.ringing_animation8);
        animation9 = findViewById(R.id.ringing_animation9);
        animation10 = findViewById(R.id.ringing_animation10);
        animation11 = findViewById(R.id.ringing_animation11);
        animation12 = findViewById(R.id.ringing_animation12);

        animationListLeft.add(animation6);
        animationListLeft.add(animation5);
        animationListLeft.add(animation4);
        animationListLeft.add(animation3);
        animationListLeft.add(animation2);
        animationListLeft.add(animation1);

        animationListRight.add(animation7);
        animationListRight.add(animation8);
        animationListRight.add(animation9);
        animationListRight.add(animation10);
        animationListRight.add(animation11);
        animationListRight.add(animation12);

        for(int i=0;i<6;i++){
            ObjectAnimator animatorLeft = ObjectAnimator.ofFloat(animationListLeft.get(i),"scaleY",0f,0.2f,0.5f,1f,0.8f,0.5f,0.3f,0f);
            animatorLeft.setDuration(600);
            animatorLeft.setRepeatCount(ValueAnimator.INFINITE);
            animatorLeft.setStartDelay(100*i);
            animatorLeft.start();

            ObjectAnimator animatorRight = ObjectAnimator.ofFloat(animationListRight.get(i),"scaleY",0f,0.2f,0.5f,1f,0.8f,0.5f,0.3f,0f);
            animatorRight.setDuration(600);
            animatorRight.setRepeatCount(ValueAnimator.INFINITE);
            animatorRight.setStartDelay(100*i);
            animatorRight.start();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            //手指按下
            x1 = event.getX();
            y1 = event.getY();
        }
        if(event.getAction() == MotionEvent.ACTION_UP){
            x2 = event.getX();
            y2 = event.getY();
            if(y1 - y2 > 50){//向上滑
                finish();
            }else if(y2 -y1 > 50){//向下滑

            }else if(x1 - x2 > 50) {//向左滑

            } else if(x2 - x1 > 50) {//向右滑

            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMusic();
    }
}
