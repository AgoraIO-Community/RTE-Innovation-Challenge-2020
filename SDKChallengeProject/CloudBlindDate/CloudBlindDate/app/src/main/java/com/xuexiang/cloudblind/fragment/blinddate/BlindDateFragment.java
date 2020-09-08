/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xuexiang.cloudblind.fragment.blinddate;

import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xuexiang.cloudblind.R;
import com.xuexiang.cloudblind.adapter.BlindDateAdapter;
import com.xuexiang.cloudblind.adapter.entity.BlindRoom;
import com.xuexiang.cloudblind.core.BaseFragment;
import com.xuexiang.cloudblind.fragment.blinddate.room.BlindDateRoomFragment;
import com.xuexiang.xaop.annotation.Permission;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.xuexiang.xaop.consts.PermissionConsts.CAMERA;
import static com.xuexiang.xaop.consts.PermissionConsts.MICROPHONE;
import static com.xuexiang.xaop.consts.PermissionConsts.STORAGE;

/**
 * @author xuexiang
 * @since 2019-10-30 00:19
 */
@Page(anim = CoreAnim.none)
public class BlindDateFragment extends BaseFragment {

    public static final int INDEX_BLIND_DATE = 1;

    public static final int PAGE_SIZE = 10;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private BlindDateAdapter mAdapter;

    private int mPageIndex = 0;

    /**
     * @return 返回为 null意为不需要导航栏
     */
    @Override
    protected TitleBar initTitle() {
        return null;
    }

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_blind_date;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        WidgetUtils.initGridRecyclerView(recyclerView, 2, 0);
        recyclerView.setAdapter(mAdapter = new BlindDateAdapter());
    }

    @Override
    protected void initListeners() {
        //下拉刷新
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            // TODO: 2020-02-25 这里只是模拟了网络请求
            refreshLayout.getLayout().postDelayed(() -> {
                mPageIndex = 0;
                mAdapter.refresh(getBlindRoomList(mPageIndex));
                refreshLayout.finishRefresh();
            }, 300);
        });
        //上拉加载
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            // TODO: 2020-02-25 这里只是模拟了网络请求
            refreshLayout.getLayout().postDelayed(() -> {
                mPageIndex++;
                mAdapter.loadMore(getBlindRoomList(mPageIndex));
                refreshLayout.finishLoadMore();
            }, 300);
        });
        mAdapter.setOnItemClickListener((itemView, item, position) -> gotoRoom(item));
        refreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果
    }

    @Permission({STORAGE, CAMERA, MICROPHONE})
    private void gotoRoom(BlindRoom room) {
        openNewPage(BlindDateRoomFragment.class, BlindDateRoomFragment.KEY_BLIND_ROOM, room);
    }

    /**
     * 模拟了网络请求
     *
     * @param pageIndex
     * @return
     */
    private List<BlindRoom> getBlindRoomList(int pageIndex) {
        List<BlindRoom> list = new ArrayList<>();
        int index;
        for (int i = 0; i < PAGE_SIZE; i++) {
            index = pageIndex * PAGE_SIZE + i + 1;
            list.add(new BlindRoom().setRoomIcon(R.drawable.ic_love)
                    .setRoomName(index + "号相亲室")
                    .setTag("同城")
                    .setTitle("爱在南京No." + index)
                    .setChannelName("BlindRoom" + index)
                    .setEncryptionKey("abc" + index)
                    .setEncryptionModeIndex(0)
            );
        }
        return list;
    }
}
