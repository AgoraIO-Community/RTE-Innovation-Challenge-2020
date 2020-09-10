package com.xiaoyang.poweroperation.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.xiaoyang.poweroperation.R;
import com.xiaoyang.poweroperation.account.LoginActivity;
import com.xiaoyang.poweroperation.data.entity.User;
import com.xiaoyang.poweroperation.di.component.DaggerMineComponent;
import com.xiaoyang.poweroperation.main.contract.MineContract;
import com.xiaoyang.poweroperation.main.presenter.MinePresenter;
import com.xiaoyang.poweroperation.mine.ChatActivity;
import com.xiaoyang.poweroperation.mine.MessageActivity;
import com.xiaoyang.poweroperation.mine.MineInfoActivity;
import com.xiaoyang.poweroperation.mine.OnLineHelpActivity;
import com.xiaoyang.poweroperation.mine.PhoneBookActivity;
import com.xiaoyang.poweroperation.mine.QAActivity;
import com.xiaoyang.poweroperation.mine.SelectionActivity;
import com.xiaoyang.poweroperation.mine.SettingActivity;
import com.xiaoyang.poweroperation.mine.SignRecordActivity;
import com.xiaoyang.poweroperation.worktab.SignActivity;
import com.xylib.base.widgets.roundedimageview.RoundedImageView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;


/**
 * Package:
 * ClassName:      MineFragment
 * Author:         xiaoyangyan
 * CreateDate:     2020/8/9 22:54
 * Description:   个人中心
 */
public class MineFragment extends BaseFragment<MinePresenter> implements MineContract.View {


    @BindView(R.id.img_photo)
    RoundedImageView imgPhoto;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.ll_sign)
    LinearLayout llSign;
    @BindView(R.id.ll_message)
    LinearLayout llMessage;
    @BindView(R.id.ll_book)
    LinearLayout llBook;
    @BindView(R.id.rl_sign_record)
    RelativeLayout rlSignRecord;
    @BindView(R.id.rl_message_record)
    RelativeLayout rlMessageRecord;
    @BindView(R.id.rl_service)
    RelativeLayout rlService;
    @BindView(R.id.rl_login_out)
    RelativeLayout rlLoginOut;
    @BindView(R.id.rl_video)
    RelativeLayout rlVideo;

    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerMineComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setUserInfo();
    }

    /**
     * 设置用户信息
     */
    private void setUserInfo() {
        if (BmobUser.isLogin()) {
            User user = BmobUser.getCurrentUser(User.class);
            if (user != null) {
                tvName.setText(user.getNickname());
            }
        } else {
            ArmsUtils.snackbarText("用户未登录");
        }
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void showMessage(@NonNull String message) {

    }

    @OnClick({R.id.ll_book, R.id.rl_sign_record, R.id.rl_message_record, R.id.rl_service, R.id.rl_video, R.id.rl_login_out,
            R.id.ll_sign, R.id.img_setting, R.id.ll_mine})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.ll_book:
                ArmsUtils.startActivity(PhoneBookActivity.class);
                break;
            case R.id.ll_sign:
                ArmsUtils.startActivity(SignActivity.class);
                break;
            case R.id.rl_sign_record:
                ArmsUtils.startActivity(SignRecordActivity.class);
                break;
            case R.id.ll_mine:
                ArmsUtils.startActivity(MineInfoActivity.class);
                break;
            case R.id.img_setting:
                ArmsUtils.startActivity(SettingActivity.class);
                break;
            case R.id.rl_message_record:
                intent = new Intent(getActivity(), MessageActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_service:
                ArmsUtils.startActivity(QAActivity.class);
                break;
            case R.id.rl_video:
                intent = new Intent(getActivity(), PhoneBookActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_login_out:
                intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
        }
    }


}
