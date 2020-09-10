package com.hwm.together.view.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hwm.together.R;
import com.hwm.together.util.JsonParser;
import com.hwm.together.util.VoiceUtil;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class ChatActivity extends BaseActivity {
    private static final String TAG = "ChatActivity";
    private int inputStyle = 0;//0-文字输入；1-语音输入
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private String resultType = "json";

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
                VoiceUtil.getInstance(ChatActivity.this).startVoice(mRecognizerDialogListener,mRecognizerListener);
//                Toast.makeText(ChatActivity.this,"长按",Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    /**
     * 听写UI监听器
     */
    public RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            printResult(results);
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
        }

    };

    /**
     * 听写监听器。
     */
    public RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            //            LogUtil.logByD(results.getResultString());
            Log.d(TAG, results.getResultString());
            if (resultType.equals("json")) {
                printResult(results);
            }else if(resultType.equals("plain")) {
                //                buffer.append(results.getResultString());
                //                mResultText.setText(buffer.toString());
                //                mResultText.setSelection(mResultText.length());
            }
            if (isLast) {
                // TODO 最后的结果

            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            //            LogUtil.logByD("返回音频数据：" + data.length);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //  if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //      String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //      Log.d(TAG, "session id =" + sid);
            //  }
        }
    };

    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }
        Log.i(TAG,resultBuffer.toString());

        setSpeechResult(resultBuffer.toString());

    }

    public void setSpeechResult(String result){
        final String resultMsg = result;
        changeInput.setBackground(getResources().getDrawable(R.drawable.icon_record));
        pressToSpeak.setVisibility(View.GONE);
        message.setVisibility(View.VISIBLE);
        message.setText(resultMsg);
        inputStyle = 0;
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
