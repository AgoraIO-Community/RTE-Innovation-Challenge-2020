package com.hwm.together.util;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

public class VoiceUtil {
    private static final String TAG = "VoiceUtil";
    private SpeechRecognizer mIat;
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private RecognizerDialog mIatDialog;
    private static Context mContext;
    int ret = 0; // 函数调用返回值
    private static VoiceUtil instance = null;
    private String resultType = "json";
    private Activity mActivity;

    //语音合成
    // 语音合成对象
    private SpeechSynthesizer mTts;

    // 默认发音人
    private String voicer = "xiaoyan";

    private VoiceUtil() {
    }

    public synchronized static VoiceUtil getInstance(Context context) {
        mContext = context;
        if (instance == null) {
            instance = new VoiceUtil();
        }
        return instance;
    }

    private void showToastMsg(Context context,String string){
        Toast.makeText(context,string,Toast.LENGTH_SHORT).show();
    }

    public void startVoice(RecognizerDialogListener mRecognizerDialogListener,RecognizerListener mRecognizerListener) {
        initVoice(mRecognizerDialogListener,mRecognizerListener);
    }

    public void startSpeak(int gender,String texts){//0-女，1-男
        if(gender==1){
            voicer = "xiaoyu";
        }else {
            voicer = "xiaoyan";
        }
        initSpeak();
        setSpeakParam();
        mTts.stopSpeaking();
        int code = mTts.startSpeaking(texts, mTtsListener);
        if (code != ErrorCode.SUCCESS) {
            Log.i(TAG,"语音合成失败,错误码: " + code+",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
        }
    }

    public void stopSpeak(){
        mTts.stopSpeaking();
    }

    private void initVoice(RecognizerDialogListener mRecognizerDialogListener,RecognizerListener mRecognizerListener) {
        mIat = SpeechRecognizer.createRecognizer(mContext, mInitListener);
        mIatDialog = new RecognizerDialog(mContext, mInitListener);
        setParam();

        if (true) {//true
            mIatDialog.setListener(mRecognizerDialogListener);
            mIatDialog.show();
            showToastMsg(mContext, "请开始说话…");
        } else {
            ret = mIat.startListening(mRecognizerListener);
            if (ret != ErrorCode.SUCCESS) {
                showToastMsg(mContext, "听写失败,错误码：" + ret);
            } else {
                showToastMsg(mContext, "请开始说话…");
            }
        }
    }

    private void initSpeak(){
        mTts = SpeechSynthesizer.createSynthesizer(mContext, mTtsInitListener);
    }

    /**
     * 参数设置
     *
     * @param
     * @return
     */
    public void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, resultType);
        // 设置语言
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置语言区域
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "1");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");

    }

    /**
     * 参数设置
     * @return
     */
    private void setSpeakParam(){
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if(mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            //支持实时音频返回，仅在synthesizeToUri条件下支持
            //mTts.setParameter(SpeechConstant.TTS_DATA_NOTIFY, "1");
            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
            //设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, "50");
            //设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, "60");
            //设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, "60");
        }else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");

        }
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "pcm");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/tts.pcm");
    }


    /**
     * 初始化监听器----语音识别
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
//            LogUtil.logByD("SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {

            }
        }
    };

    /**
     * 初始化监听----语音合成
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d(TAG, "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                Log.i(TAG,"初始化失败,错误码："+code+",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };


    /**
     * 听写监听器。
     */
//    private RecognizerListener mRecognizerListener = new RecognizerListener() {
//
//        @Override
//        public void onBeginOfSpeech() {
//            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
//        }
//
//        @Override
//        public void onError(SpeechError error) {
//            // Tips：
//            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
//            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
//        }
//
//        @Override
//        public void onEndOfSpeech() {
//            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
//        }
//
//        @Override
//        public void onResult(RecognizerResult results, boolean isLast) {
////            LogUtil.logByD(results.getResultString());
//            Log.d(TAG, results.getResultString());
//            if (resultType.equals("json")) {
//                printResult(results);
//            }else if(resultType.equals("plain")) {
////                buffer.append(results.getResultString());
////                mResultText.setText(buffer.toString());
////                mResultText.setSelection(mResultText.length());
//            }
//            if (isLast) {
//                // TODO 最后的结果
//
//            }
//        }
//
//        @Override
//        public void onVolumeChanged(int volume, byte[] data) {
////            LogUtil.logByD("返回音频数据：" + data.length);
//        }
//
//        @Override
//        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
//            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
//            // 若使用本地能力，会话id为null
//            //  if (SpeechEvent.EVENT_SESSION_ID == eventType) {
//            //      String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
//            //      Log.d(TAG, "session id =" + sid);
//            //  }
//        }
//    };

    /**
     * 合成回调监听。
     */

    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
            Log.i(TAG,"开始播放");
        }

        @Override
        public void onSpeakPaused() {
            Log.i(TAG,"暂停播放");
        }

        @Override
        public void onSpeakResumed() {
            Log.i(TAG,"继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            // 合成进度
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {

        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                Log.i(TAG,"播放完成");
            } else if (error != null) {
                Log.i(TAG,error.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}

            //当设置SpeechConstant.TTS_DATA_NOTIFY为1时，抛出buf数据
			/*if (SpeechEvent.EVENT_TTS_BUFFER == eventType) {
						byte[] buf = obj.getByteArray(SpeechEvent.KEY_EVENT_TTS_BUFFER);
						Log.e("MscSpeechLog", "buf is =" + buf);
					}*/

        }
    };

    /**
     * 听写UI监听器
     */
//    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
//        public void onResult(RecognizerResult results, boolean isLast) {
//            printResult(results);
//        }
//
//        /**
//         * 识别回调错误.
//         */
//        public void onError(SpeechError error) {
//        }
//
//    };
//
//    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    /*
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

        ChatActivity chatActivity = (ChatActivity) mContext;
        chatActivity.setSpeechResult(resultBuffer.toString());
    }
    */
}
