package com.xiaoyang.poweroperation.mine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.xiaoyang.poweroperation.R;
import com.xiaoyang.poweroperation.data.entity.User;
import com.xiaoyang.poweroperation.di.component.DaggerPhoneBookComponent;
import com.xiaoyang.poweroperation.mine.adapter.PhoneBookAdapter;
import com.xiaoyang.poweroperation.mine.contract.PhoneBookContract;
import com.xiaoyang.poweroperation.mine.presenter.PhoneBookPresenter;
import com.xylib.base.utils.StringUtil;
import com.xylib.base.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class PhoneBookActivity extends BaseActivity<PhoneBookPresenter> implements PhoneBookContract.View {

    @BindView(R.id.toolbar_back)
    RelativeLayout toolbarBack;
    @BindView(R.id.tv_toolbar_title_tv)
    TextView tvToolbarTitleTv;
    @BindView(R.id.img_right)
    ImageView imgRight;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.rlv_phone_book)
    RecyclerView rlvPhoneBook;
    private List<User> mDataList;
    private PhoneBookAdapter mAdapter;
    private String phoneInfo;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerPhoneBookComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_phone_book; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        tvToolbarTitleTv.setText("通讯录");
        initPhoneBookRecycleView();
        getPhoneBookList();
    }

    private void initPhoneBookRecycleView() {
        mDataList = new ArrayList<>();
        mAdapter = new PhoneBookAdapter(R.layout.item_phone_book_view, mDataList);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            phoneInfo = mDataList.get(position).getMobilePhoneNumber();
            showSimpleBottomSheetList("选择通讯方式", false);

        });
        rlvPhoneBook.setAdapter(mAdapter);
        rlvPhoneBook.setLayoutManager(new LinearLayoutManager(Utils.getApp()));
    }

    private void showSimpleBottomSheetList(CharSequence title, boolean withMark) {
        QMUIBottomSheet.BottomListSheetBuilder builder = new QMUIBottomSheet.BottomListSheetBuilder(this);
        builder.setGravityCenter(true)
                .setSkinManager(QMUISkinManager.defaultInstance(this))
                .setTitle(title)
                .setAddCancelBtn(true)
                .setAllowDrag(true)
                .setNeedRightMark(false)
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        switch (tag) {
                            case "phone":
                                if (StringUtil.isNotBlank(phoneInfo)) {
                                    callPhone(phoneInfo);
                                }
                                break;
                            case "chat":
                                Intent intent = new Intent(PhoneBookActivity.this, ChatActivity.class);
                                startActivity(intent);
                                break;
                            case "video":
                                Intent videoIntent = new Intent(PhoneBookActivity.this, OnLineHelpActivity.class);
                                startActivity(videoIntent);
                                break;
                        }

                        dialog.dismiss();
                    }
                });
        if (withMark) {
            builder.setCheckedIndex(40);
        }
        builder.addItem("拨打电话", "phone");
        builder.addItem("在线联系", "chat");
        builder.addItem("视频连线", "video");
        builder.build().show();
    }

    /**
     * 获取通讯录
     */
    private void getPhoneBookList() {
        BmobQuery<User> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> object, BmobException e) {
                if (e == null) {
                    Log.v("yxy", "查询成功" + object.size());
                    mDataList = object;
                    mAdapter.replaceData(object);
                } else {
                    Log.e("BMOB", e.toString());

                }
            }
        });
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

    private void callPhone(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
