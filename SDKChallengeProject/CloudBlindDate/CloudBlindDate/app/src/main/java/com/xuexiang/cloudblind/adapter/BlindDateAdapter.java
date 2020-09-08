/*
 * Copyright (C) 2020 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.cloudblind.adapter;

import androidx.annotation.NonNull;

import com.xuexiang.cloudblind.R;
import com.xuexiang.cloudblind.adapter.entity.BlindRoom;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;

/**
 * @author xuexiang
 * @since 2020/9/6 10:41 PM
 */
public class BlindDateAdapter extends BaseRecyclerAdapter<BlindRoom> {

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.adapter_blind_date_room_list_item;
    }

    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, BlindRoom item) {
        if (item == null) {
            return;
        }
        holder.image(R.id.iv_room_icon, item.RoomIcon);
        holder.text(R.id.tv_room_name, item.RoomName);
        holder.text(R.id.tv_tag, item.Tag);
        holder.text(R.id.tv_title, item.Title);
        holder.text(R.id.tv_number, item.Number + "人在线");

    }
}
