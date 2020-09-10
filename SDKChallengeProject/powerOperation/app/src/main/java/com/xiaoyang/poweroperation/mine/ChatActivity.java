package com.xiaoyang.poweroperation.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.xiaoyang.poweroperation.R;
import com.xiaoyang.poweroperation.app.AppApplication;
import com.xiaoyang.poweroperation.app.utils.ImageUtil;
import com.xiaoyang.poweroperation.app.utils.MessageUtil;
import com.xiaoyang.poweroperation.data.entity.MessageBean;
import com.xiaoyang.poweroperation.data.entity.MessageListBean;
import com.xiaoyang.poweroperation.data.entity.User;
import com.xiaoyang.poweroperation.di.component.DaggerChatComponent;
import com.xiaoyang.poweroperation.mine.adapter.ChatMessageAdapter;
import com.xiaoyang.poweroperation.mine.contract.ChatContract;
import com.xiaoyang.poweroperation.mine.presenter.ChatPresenter;
import com.xiaoyang.poweroperation.ui.widgets.chart.ChatManager;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.bmob.v3.BmobUser;
import io.agora.rtm.ErrorInfo;
import io.agora.rtm.ResultCallback;
import io.agora.rtm.RtmChannel;
import io.agora.rtm.RtmChannelAttribute;
import io.agora.rtm.RtmChannelListener;
import io.agora.rtm.RtmChannelMember;
import io.agora.rtm.RtmClient;
import io.agora.rtm.RtmClientListener;
import io.agora.rtm.RtmFileMessage;
import io.agora.rtm.RtmImageMessage;
import io.agora.rtm.RtmMediaOperationProgress;
import io.agora.rtm.RtmMessage;
import io.agora.rtm.RtmMessageType;
import io.agora.rtm.RtmStatusCode;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * Package:
 * ClassName:      ChatActivity
 * Author:         xiaoyangyan
 * CreateDate:     2020/9/7 15:45
 * Description:   聊天
 */
public class ChatActivity extends BaseActivity<ChatPresenter> implements ChatContract.View {

    @BindView(R.id.message_title)
    TextView mTitleTextView;
    @BindView(R.id.selection_chat_btn)
    TextView selectionChatBtn;
    @BindView(R.id.selection_img_btn)
    ImageView selectionImgBtn;
    @BindView(R.id.message_edittiext)
    EditText mMsgEditText;
    @BindView(R.id.message_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.big_image)
    ImageView mBigImage;
    private List<MessageBean> mMessageBeanList = new ArrayList<>();
    private ChatMessageAdapter mMessageAdapter;

    private boolean mIsPeerToPeerMode = true;
    private String mUserId = "";
    private String mPeerId = "";
    private String mChannelName = "";
    private int mChannelMemberCount = 1;

    private ChatManager mChatManager;
    private RtmClient mRtmClient;
    private RtmClientListener mClientListener;
    private RtmChannel mRtmChannel;
    private boolean mIsInChat = false;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerChatComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_chat; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mChatManager = AppApplication.getInstance().getChatManager();
        mRtmClient = mChatManager.getRtmClient();
        User user = BmobUser.getCurrentUser(User.class);
        if (user != null) {
            mUserId = user.getUserId() + "";
        }
        init();
        loginChat();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mIsInChat) {
            loginChat();
        }
    }

    private void loginChat() {
        mIsInChat = true;
        mRtmClient.login(null, mUserId, new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void responseInfo) {
                Log.i(TAG, "login success");
                runOnUiThread(() -> {
                    Intent intent = new Intent(ChatActivity.this, SelectionActivity.class);
                    intent.putExtra(MessageUtil.INTENT_EXTRA_USER_ID, mUserId);
                    startActivity(intent);
                });
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                Log.i(TAG, "login failed: " + errorInfo.getErrorCode());
                runOnUiThread(() -> {
                    mIsInChat = false;
                });
            }
        });
    }

    private void init() {
        mClientListener = new MyRtmClientListener();
        mChatManager.registerListener(mClientListener);
        Intent intent = getIntent();
        mIsPeerToPeerMode = intent.getBooleanExtra(MessageUtil.INTENT_EXTRA_IS_PEER_MODE, true);
        String targetName = intent.getStringExtra(MessageUtil.INTENT_EXTRA_TARGET_NAME);

        if (mIsPeerToPeerMode) {
            mPeerId = targetName;
            mTitleTextView.setText(mPeerId);

            // load history chat records
            MessageListBean messageListBean = MessageUtil.getExistMessageListBean(mPeerId);
            if (messageListBean != null) {
                mMessageBeanList.addAll(messageListBean.getMessageBeanList());
            }

            // load offline messages since last chat with this peer.
            // Then clear cached offline messages from message pool
            // since they are already consumed.
            MessageListBean offlineMessageBean = new MessageListBean(mPeerId, mChatManager);
            mMessageBeanList.addAll(offlineMessageBean.getMessageBeanList());
            mChatManager.removeAllOfflineMessages(mPeerId);
        } else {
            mChannelName = targetName;
            mChannelMemberCount = 1;
            mTitleTextView.setText(MessageFormat.format("{0}({1})", mChannelName, mChannelMemberCount));
            createAndJoinChannel();
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mMessageAdapter = new ChatMessageAdapter(this, mMessageBeanList, message -> {
            if (message.getMessage().getMessageType() == RtmMessageType.IMAGE) {
                if (!TextUtils.isEmpty(message.getCacheFile())) {
                    Glide.with(this).load(message.getCacheFile()).into(mBigImage);
                    mBigImage.setVisibility(View.VISIBLE);
                } else {
                    ImageUtil.cacheImage(this, mRtmClient, (RtmImageMessage) message.getMessage(), new ResultCallback<String>() {
                        @Override
                        public void onSuccess(String file) {
                            message.setCacheFile(file);
                            runOnUiThread(() -> {
                                Glide.with(ChatActivity.this).load(file).into(mBigImage);
                                mBigImage.setVisibility(View.VISIBLE);
                            });
                        }

                        @Override
                        public void onFailure(ErrorInfo errorInfo) {

                        }
                    });
                }
            }
        });
        mRecyclerView = findViewById(R.id.message_list);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mMessageAdapter);

        mMsgEditText = findViewById(R.id.message_edittiext);
        mBigImage = findViewById(R.id.big_image);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mIsPeerToPeerMode) {
            MessageUtil.addMessageListBeanList(new MessageListBean(mPeerId, mMessageBeanList));
        } else {
            leaveAndReleaseChannel();
        }
        mChatManager.unregisterListener(mClientListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                Uri resultUri = result.getUri();
//
//                final String file = resultUri.getPath();
//                ImageUtil.uploadImage(this, mRtmClient, file, new ResultCallback<RtmImageMessage>() {
//                    @Override
//                    public void onSuccess(final RtmImageMessage rtmImageMessage) {
//                        runOnUiThread(() -> {
//                            MessageBean messageBean = new MessageBean(mUserId, rtmImageMessage, true);
//                            messageBean.setCacheFile(file);
//                            mMessageBeanList.add(messageBean);
//                            mMessageAdapter.notifyItemRangeChanged(mMessageBeanList.size(), 1);
//                            mRecyclerView.scrollToPosition(mMessageBeanList.size() - 1);
//
//                            if (mIsPeerToPeerMode) {
//                                sendPeerMessage(rtmImageMessage);
//                            } else {
//                                sendChannelMessage(rtmImageMessage);
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onFailure(ErrorInfo errorInfo) {
//
//                    }
//                });
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                result.getError().printStackTrace();
//            }
//        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.selection_chat_btn:
                String msg = mMsgEditText.getText().toString();
                if (!msg.equals("")) {
                    RtmMessage message = mRtmClient.createMessage();
                    message.setText(msg);

                    MessageBean messageBean = new MessageBean(mUserId, message, true);
                    mMessageBeanList.add(messageBean);
                    mMessageAdapter.notifyItemRangeChanged(mMessageBeanList.size(), 1);
                    mRecyclerView.scrollToPosition(mMessageBeanList.size() - 1);

                    if (mIsPeerToPeerMode) {
                        sendPeerMessage(message);
                    } else {
                        sendChannelMessage(message);
                    }
                }
                mMsgEditText.setText("");
                break;
            case R.id.selection_img_btn:
//                CropImage.activity().start(this);
                break;
            case R.id.big_image:
                mBigImage.setVisibility(View.GONE);
                break;
        }
    }

    public void onClickFinish(View v) {
        finish();
    }

    /**
     * API CALL: send message to peer
     */
    private void sendPeerMessage(final RtmMessage message) {
        mRtmClient.sendMessageToPeer(mPeerId, message, mChatManager.getSendMessageOptions(), new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // do nothing
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                // refer to RtmStatusCode.PeerMessageState for the message state
                final int errorCode = errorInfo.getErrorCode();
                runOnUiThread(() -> {
                    switch (errorCode) {
                        case RtmStatusCode.PeerMessageError.PEER_MESSAGE_ERR_TIMEOUT:
                        case RtmStatusCode.PeerMessageError.PEER_MESSAGE_ERR_FAILURE:
                            showToast(getString(R.string.send_msg_failed));
                            break;
                        case RtmStatusCode.PeerMessageError.PEER_MESSAGE_ERR_PEER_UNREACHABLE:
                            showToast(getString(R.string.peer_offline));
                            break;
                        case RtmStatusCode.PeerMessageError.PEER_MESSAGE_ERR_CACHED_BY_SERVER:
                            showToast(getString(R.string.message_cached));
                            break;
                    }
                });
            }
        });
    }

    /**
     * API CALL: create and join channel
     */
    private void createAndJoinChannel() {
        // step 1: create a channel instance
        mRtmChannel = mRtmClient.createChannel(mChannelName, new MyChannelListener());
        if (mRtmChannel == null) {
            showToast(getString(R.string.join_channel_failed));
            finish();
            return;
        }

        Log.e("channel", mRtmChannel + "");

        // step 2: join the channel
        mRtmChannel.join(new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void responseInfo) {
                Log.i(TAG, "join channel success");
                getChannelMemberList();
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                Log.e(TAG, "join channel failed");
                runOnUiThread(() -> {
                    showToast(getString(R.string.join_channel_failed));
                    finish();
                });
            }
        });
    }

    /**
     * API CALL: get channel member list
     */
    private void getChannelMemberList() {
        mRtmChannel.getMembers(new ResultCallback<List<RtmChannelMember>>() {
            @Override
            public void onSuccess(final List<RtmChannelMember> responseInfo) {
                runOnUiThread(() -> {
                    mChannelMemberCount = responseInfo.size();
                    refreshChannelTitle();
                });
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                Log.e(TAG, "failed to get channel members, err: " + errorInfo.getErrorCode());
            }
        });
    }

    /**
     * API CALL: send message to a channel
     */
    private void sendChannelMessage(RtmMessage message) {
        mRtmChannel.sendMessage(message, new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                // refer to RtmStatusCode.ChannelMessageState for the message state
                final int errorCode = errorInfo.getErrorCode();
                runOnUiThread(() -> {
                    switch (errorCode) {
                        case RtmStatusCode.ChannelMessageError.CHANNEL_MESSAGE_ERR_TIMEOUT:
                        case RtmStatusCode.ChannelMessageError.CHANNEL_MESSAGE_ERR_FAILURE:
                            showToast(getString(R.string.send_msg_failed));
                            break;
                    }
                });
            }
        });
    }

    /**
     * API CALL: leave and release channel
     */
    private void leaveAndReleaseChannel() {
        if (mRtmChannel != null) {
            mRtmChannel.leave(null);
            mRtmChannel.release();
            mRtmChannel = null;
        }
    }

    /**
     * API CALLBACK: rtm event listener
     */
    class MyRtmClientListener implements RtmClientListener {

        @Override
        public void onConnectionStateChanged(final int state, int reason) {
            runOnUiThread(() -> {
                switch (state) {
                    case RtmStatusCode.ConnectionState.CONNECTION_STATE_RECONNECTING:
                        showToast(getString(R.string.reconnecting));
                        break;
                    case RtmStatusCode.ConnectionState.CONNECTION_STATE_ABORTED:
                        showToast(getString(R.string.account_offline));
                        setResult(MessageUtil.ACTIVITY_RESULT_CONN_ABORTED);
                        finish();
                        break;
                }
            });
        }

        @Override
        public void onMessageReceived(final RtmMessage message, final String peerId) {
            runOnUiThread(() -> {
                if (peerId.equals(mPeerId)) {
                    MessageBean messageBean = new MessageBean(peerId, message, false);
                    messageBean.setBackground(getMessageColor(peerId));
                    mMessageBeanList.add(messageBean);
                    mMessageAdapter.notifyItemRangeChanged(mMessageBeanList.size(), 1);
                    mRecyclerView.scrollToPosition(mMessageBeanList.size() - 1);
                } else {
                    MessageUtil.addMessageBean(peerId, message);
                }
            });
        }

        @Override
        public void onImageMessageReceivedFromPeer(final RtmImageMessage rtmImageMessage, final String peerId) {
            runOnUiThread(() -> {
                if (peerId.equals(mPeerId)) {
                    MessageBean messageBean = new MessageBean(peerId, rtmImageMessage, false);
                    messageBean.setBackground(getMessageColor(peerId));
                    mMessageBeanList.add(messageBean);
                    mMessageAdapter.notifyItemRangeChanged(mMessageBeanList.size(), 1);
                    mRecyclerView.scrollToPosition(mMessageBeanList.size() - 1);
                } else {
                    MessageUtil.addMessageBean(peerId, rtmImageMessage);
                }
            });
        }

        @Override
        public void onFileMessageReceivedFromPeer(RtmFileMessage rtmFileMessage, String s) {

        }

        @Override
        public void onMediaUploadingProgress(RtmMediaOperationProgress rtmMediaOperationProgress, long l) {

        }

        @Override
        public void onMediaDownloadingProgress(RtmMediaOperationProgress rtmMediaOperationProgress, long l) {

        }

        @Override
        public void onTokenExpired() {

        }

        @Override
        public void onPeersOnlineStatusChanged(Map<String, Integer> map) {

        }
    }

    /**
     * API CALLBACK: rtm channel event listener
     */
    class MyChannelListener implements RtmChannelListener {
        @Override
        public void onMemberCountUpdated(int i) {

        }

        @Override
        public void onAttributesUpdated(List<RtmChannelAttribute> list) {

        }

        @Override
        public void onMessageReceived(final RtmMessage message, final RtmChannelMember fromMember) {
            runOnUiThread(() -> {
                String account = fromMember.getUserId();
                Log.i(TAG, "onMessageReceived account = " + account + " msg = " + message);
                MessageBean messageBean = new MessageBean(account, message, false);
                messageBean.setBackground(getMessageColor(account));
                mMessageBeanList.add(messageBean);
                mMessageAdapter.notifyItemRangeChanged(mMessageBeanList.size(), 1);
                mRecyclerView.scrollToPosition(mMessageBeanList.size() - 1);
            });
        }

        @Override
        public void onImageMessageReceived(final RtmImageMessage rtmImageMessage, final RtmChannelMember rtmChannelMember) {
            runOnUiThread(() -> {
                String account = rtmChannelMember.getUserId();
                Log.i(TAG, "onMessageReceived account = " + account + " msg = " + rtmImageMessage);
                MessageBean messageBean = new MessageBean(account, rtmImageMessage, false);
                messageBean.setBackground(getMessageColor(account));
                mMessageBeanList.add(messageBean);
                mMessageAdapter.notifyItemRangeChanged(mMessageBeanList.size(), 1);
                mRecyclerView.scrollToPosition(mMessageBeanList.size() - 1);
            });
        }

        @Override
        public void onFileMessageReceived(RtmFileMessage rtmFileMessage, RtmChannelMember rtmChannelMember) {

        }

        @Override
        public void onMemberJoined(RtmChannelMember member) {
            runOnUiThread(() -> {
                mChannelMemberCount++;
                refreshChannelTitle();
            });
        }

        @Override
        public void onMemberLeft(RtmChannelMember member) {
            runOnUiThread(() -> {
                mChannelMemberCount--;
                refreshChannelTitle();
            });
        }
    }

    private int getMessageColor(String account) {
        for (int i = 0; i < mMessageBeanList.size(); i++) {
            if (account.equals(mMessageBeanList.get(i).getAccount())) {
                return mMessageBeanList.get(i).getBackground();
            }
        }
        return MessageUtil.COLOR_ARRAY[MessageUtil.RANDOM.nextInt(MessageUtil.COLOR_ARRAY.length)];
    }

    private void refreshChannelTitle() {
        String titleFormat = getString(R.string.channel_title);
        String title = String.format(titleFormat, mChannelName, mChannelMemberCount);
        mTitleTextView.setText(title);
    }

    private void showToast(final String text) {
        runOnUiThread(() -> Toast.makeText(ChatActivity.this, text, Toast.LENGTH_SHORT).show());
    }

    /**
     * API CALL: logout from RTM server
     */
    private void doLogout() {
        mRtmClient.logout(null);
        MessageUtil.cleanMessageListBeanList();
    }

}
