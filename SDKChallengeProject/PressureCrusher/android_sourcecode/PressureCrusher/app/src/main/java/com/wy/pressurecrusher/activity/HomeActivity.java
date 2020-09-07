package com.wy.pressurecrusher.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wy.pressurecrusher.R;
import com.wy.pressurecrusher.animation.animation.ExplosionField;
import com.wy.pressurecrusher.base.BaseListAdapter;
import com.wy.pressurecrusher.base.ViewCreator;
import com.wy.pressurecrusher.mode.RecordItem;
import com.wy.pressurecrusher.utils.FileUtils;
import com.wy.pressurecrusher.utils.SharedPreferencesUtils;
import com.wy.pressurecrusher.utils.ToastUtil;
import com.wy.pressurecrusher.view.BaseTititleBar;
import com.wy.pressurecrusher.view.ViewRecord;

import java.io.File;
import java.io.IOException;
import java.time.format.TextStyle;
import java.util.ArrayList;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;

import static com.wy.pressurecrusher.view.ViewRecord.RECORD_DIR;


/**
 * 首页
 *
 * @author wy
 */
public class HomeActivity extends BaseActivity implements ViewRecord.RecordListener {

    public static final String PREF_KEY_RECORD = "pressurecrusher_pref_record";
    private static final int PERMISSION_REQ_ID_RECORD_AUDIO = 20;

    private String recordFiledir;

    private ViewRecord viewRecord;
    private Button btnClear;
    private ImageView imgSmile;

    private ArrayList<RecordItem> mRecordList;
    private BaseListAdapter<RecordItem, RecordViewHolder> mAapter;
    private GridView gridView;

    private RtcEngine mRtcEngine; // Tutorial Step 1
    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() { // Tutorial Step 1

        /**
         * MEDIA_ENGINE_AUDIO_EVENT_MIXING_PLAY
         * MEDIA_ENGINE_AUDIO_EVENT_MIXING_STOPPED
         * */
        @Override
        public void onAudioMixingStateChanged(int i, int i1) {
            super.onAudioMixingStateChanged(i, i1);
        }

    };
    private MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BaseTititleBar tititleBar = findViewById(R.id.title_bar);
        tititleBar.setLeftGone();
        tititleBar.setTitleMid(getString(R.string.app_name));
        TextPaint paint = tititleBar.getMidTv().getPaint();
        paint.setFakeBoldText(true);

        recordFiledir = getFilesDir().getPath() + File.separator + RECORD_DIR;
        FileUtils.createOrExistsDir(recordFiledir);
        gridView = findViewById(R.id.grid_view);
        viewRecord = findViewById(R.id.view_record);
        imgSmile = findViewById(R.id.img_smile);
        btnClear = findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearRecord();
            }
        });

        mAapter = new BaseListAdapter<RecordItem, RecordViewHolder>(new ViewCreator<RecordItem, RecordViewHolder>() {
            @Override
            public RecordViewHolder createHolder(int position, ViewGroup parent) {
                return new RecordViewHolder(LayoutInflater.from(HomeActivity.this).inflate(R.layout.view_record_item, null));
            }

            @Override
            public void bindData(int position, RecordViewHolder holder, final RecordItem data) {
                holder.tvName.setText(data.name);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mPlayer == null) {
                            mPlayer = new MediaPlayer();
                        }
                        if (mPlayer.isPlaying()) {
                            mPlayer.stop();
                            return;
                        }
                        try {
                            mPlayer.reset();
                            mPlayer.setDataSource(data.filePath);
                            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mp) {
                                    mp.start();
                                }
                            });
                            mPlayer.prepareAsync();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (SecurityException e) {
                            e.printStackTrace();
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        gridView.setAdapter(mAapter);
        viewRecord.setRecordListener(this);
        emptyDataView();
        loadRecordData();
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO, PERMISSION_REQ_ID_RECORD_AUDIO)) {
            initAgoraEngineAndJoinChannel();
        }
    }

    private void initAgoraEngineAndJoinChannel() {
        initializeAgoraEngine();     // Tutorial Step 1
//        joinChannel();               // Tutorial Step 2
    }

    public boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this,
                permission)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{permission},
                    requestCode);
            return false;
        }
        return true;
    }

    private void loadRecordData() {
        String value = (String) SharedPreferencesUtils.getData(this, PREF_KEY_RECORD, "");
        ArrayList<RecordItem> datalist = new Gson().fromJson(value, new TypeToken<ArrayList<RecordItem>>() {
        }.getType());
        if (mRecordList == null) {
            mRecordList = new ArrayList<>();
        } else {
            mRecordList.clear();
        }
        if (datalist != null) {
            mRecordList.addAll(datalist);
        }

        mAapter.update(mRecordList);
        if (!mRecordList.isEmpty()) {
            visibleDataView();
        }

    }

    private void clearRecord() {
        mRecordList.clear();
        mAapter.clear();
        emptyDataView();
        new ExplosionField(this).explode(gridView, null);
        ToastUtil.customToastGravity(this, "您所有的压力都被粉粹了 棒！(≧▽≦)", 4000, Gravity.CENTER, 0, 0);
        FileUtils.deleteFilesInDir(getFilesDir().getPath() + File.separator + RECORD_DIR);
        SharedPreferencesUtils.remove(this, PREF_KEY_RECORD);
    }

    private void saveRecordData() {
        if (mRecordList == null || mRecordList.isEmpty()) {
            return;
        }
        String value = new Gson().toJson(mRecordList);
        SharedPreferencesUtils.setData(this, PREF_KEY_RECORD, value);
    }

    private void emptyDataView() {
        btnClear.setVisibility(View.GONE);
        gridView.setVisibility(View.GONE);
        imgSmile.setVisibility(View.VISIBLE);
    }


    private void visibleDataView() {
        gridView.setVisibility(View.VISIBLE);
        btnClear.setVisibility(View.VISIBLE);
        imgSmile.setVisibility(View.GONE);
    }

    class RecordViewHolder extends BaseListAdapter.ViewHolder {
        private TextView tvName;

        public RecordViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
        }

    }

    // Tutorial Step 1
    private void initializeAgoraEngine() {
        try {
            mRtcEngine = RtcEngine.create(getBaseContext(), getString(R.string.agora_app_id), mRtcEventHandler);
            mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION);
        } catch (Exception e) {
            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
    }


    // Tutorial Step 2
    private void joinChannel() {
//        String accessToken = getString(R.string.agora_access_token);
//        if (TextUtils.equals(accessToken, "") || TextUtils.equals(accessToken, "#YOUR ACCESS TOKEN#")) {
//            accessToken = null; // default, no token
//        }

        // Allows a user to join a channel.
//        mRtcEngine.joinChannel(accessToken, "", "Extra Optional Data", 0); // if you do not specify the uid, we will generate the uid for you
    }

    // Tutorial Step 3
    private void leaveChannel() {
        //mRtcEngine.leaveChannel();
    }

    // Tutorial Step 4
    private void onRemoteUserLeft(int uid, int reason) {
    }

    // Tutorial Step 6
    private void onRemoteUserVoiceMuted(int uid, boolean muted) {
    }

    @Override
    public void recordSuccess(RecordItem recordItem) {
        mRecordList.add(recordItem);
        mAapter.addData(recordItem);
        mAapter.notifyDataSetChanged();
        visibleDataView();
    }

    @Override
    public boolean canRecord() {
        return mRecordList.size() < ViewRecord.MAX_RECORD_CNT;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSION_REQ_ID_RECORD_AUDIO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initAgoraEngineAndJoinChannel();
                } else {
                    finish();
                }
                break;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        leaveChannel();
        RtcEngine.destroy();
        mRtcEngine = null;
        saveRecordData();
    }

}
