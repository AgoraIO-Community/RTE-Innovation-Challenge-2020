package com.example.myapplication.view;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.util.VoiceUtil;

public class ChatActivity extends Activity {
    private int inputStyle = 0;//0-文字输入；1-语音输入

    private EditText message;
    private Button sendBtn;
    private ImageButton changeInput;
    private TextView pressToSpeak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initView();
    }

    private void initView(){
        message = findViewById(R.id.chat_message_et);
        sendBtn = findViewById(R.id.chat_send_btn);
        changeInput = findViewById(R.id.chat_change_input_img);
        pressToSpeak = findViewById(R.id.chat_press_to_speak);

        sendBtn.setEnabled(false);
        message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sendAble();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        pressToSpeak.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                VoiceUtil.getInstance(ChatActivity.this).startVoice();
//                Toast.makeText(ChatActivity.this,"长按",Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    public void setSpeechResult(final String result){
        final String resultMsg = result;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                changeInput.setBackground(getResources().getDrawable(R.drawable.icon_record));
                pressToSpeak.setVisibility(View.GONE);
                message.setVisibility(View.VISIBLE);
                message.setText(resultMsg);
                inputStyle = 0;
            }
        });
    }

    public void back(View view){
        finish();
    }

    public void changeInput(View view){
        if(inputStyle==0){//当前为文字输入
            changeInput.setBackground(getResources().getDrawable(R.drawable.icon_keyboard));
            pressToSpeak.setVisibility(View.VISIBLE);
            message.setVisibility(View.GONE);
            sendBtn.setEnabled(false);
            inputStyle = 1;
            return;
        }else {//当前为语音输入
            changeInput.setBackground(getResources().getDrawable(R.drawable.icon_record));
            pressToSpeak.setVisibility(View.GONE);
            message.setVisibility(View.VISIBLE);
            sendAble();
            inputStyle = 0;
        }
    }

    public void sendMessage(View view){
        VoiceUtil.getInstance(ChatActivity.this).startSpeak(1,message.getText().toString());
    }


    private void sendAble(){
        String msgString = message.getText().toString();
        if(msgString.length()==0){
            sendBtn.setEnabled(false);
        }else {
            sendBtn.setEnabled(true);
        }
    }
}
