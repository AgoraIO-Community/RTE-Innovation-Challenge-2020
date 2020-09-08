package com.wy.pressurecrusher.view;

import android.content.Context;
import android.media.MediaRecorder;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wy.pressurecrusher.R;
import com.wy.pressurecrusher.mode.RecordItem;
import com.wy.pressurecrusher.utils.ToastUtil;

import java.io.File;
import java.io.IOException;

public class ViewRecord extends LinearLayout {

    public static final int MAX_RECORD_CNT = 12;
    public static final String RECORD_DIR = "record";

    private View btnPlay;
    private View btnEnd;
    private View layoutRecording;
    private TextView tvRecordName;
    private RecordListener mRecordListener;
    private MediaRecorder mMediaRecorder;
    private String fileName;
    private String recordFiledir;
    private String filePath;

    public interface RecordListener {
        void recordSuccess(RecordItem recordItem);

        boolean canRecord();
    }

    public ViewRecord(Context context) {
        super(context);
        initView();
    }

    public ViewRecord(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ViewRecord(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.view_record, this);
        btnPlay = findViewById(R.id.btn_play);
        btnEnd = findViewById(R.id.btn_end);
        tvRecordName = findViewById(R.id.tv_record_name);
        layoutRecording = findViewById(R.id.layout_recording);

        btnPlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRecordListener == null || !mRecordListener.canRecord()) {
                    ToastUtil.showShort(v.getContext(), "先将粉粹机里的压力处理掉吧^_^");
                    return;
                }
                fileName = System.currentTimeMillis() + "";
                btnEndVisible();
                startRecord(fileName);
            }
        });
        btnEnd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPlayVisible();
                stopRecord();
                saveRecord();
            }
        });
        recordFiledir = getContext().getFilesDir().getPath() + File.separator +RECORD_DIR;
        btnPlayVisible();
    }

    public void setRecordListener(RecordListener recordListener) {
        this.mRecordListener = recordListener;
    }

    private void btnPlayVisible() {
        layoutRecording.setVisibility(View.GONE);
        btnPlay.setVisibility(View.VISIBLE);
    }

    private void btnEndVisible() {
        btnPlay.setVisibility(View.GONE);
        layoutRecording.setVisibility(View.VISIBLE);
    }

    private void saveRecord() {
        RecordItem recordItem = new RecordItem();
        recordItem.filePath = filePath;
        recordItem.name = fileName.substring(8);
        if (mRecordListener != null) {
            mRecordListener.recordSuccess(recordItem);
        }
    }

    public void startRecord(String filename) {
        // 开始录音
        /* ①Initial：实例化MediaRecorder对象 */
        if (mMediaRecorder == null)
            mMediaRecorder = new MediaRecorder();
        try {
            /* ②setAudioSource/setVedioSource */
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置麦克风
            /*
             * ②设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP(3gp格式
             * ，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
             */
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            /* ②设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default 声音的（波形）的采样 */
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            /* ③准备 */
            filePath = recordFiledir+File.separator+filename + ".m4a";
            mMediaRecorder.setOutputFile(filePath);
            mMediaRecorder.prepare();
            /* ④开始 */
            mMediaRecorder.start();
        } catch (IllegalStateException e) {
            Log.d("=========", e.toString());
        } catch (IOException e) {
            Log.d("===========", e.toString());
        }
    }


    public void stopRecord() {
        try {
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;
        } catch (RuntimeException e) {
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }


}
