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

package com.xuexiang.cloudblind.adapter.entity;

/**
 * 聊天室
 *
 * @author xuexiang
 * @since 2020/9/6 10:42 PM
 */
public class BlindRoom {

    /**
     * 房间图标
     */
    public Object RoomIcon;

    /**
     * 房间名
     */
    public String RoomName;

    /**
     * 标题
     */
    public String Title;

    /**
     * 标签
     */
    public String Tag;

    /**
     * 人数
     */
    public int Number;

    /**
     * 房号
     */
    public String ChannelName;

    /**
     * 密钥
     */
    public String EncryptionKey;

    /**
     * 加密方式
     */
    public int EncryptionModeIndex;

    public BlindRoom() {
        Number = (int) (Math.random() * 30 + 5);
    }

    public Object getRoomIcon() {
        return RoomIcon;
    }

    public BlindRoom setRoomIcon(Object roomIcon) {
        RoomIcon = roomIcon;
        return this;
    }

    public String getRoomName() {
        return RoomName;
    }

    public BlindRoom setRoomName(String roomName) {
        RoomName = roomName;
        return this;
    }

    public String getTitle() {
        return Title;
    }

    public BlindRoom setTitle(String title) {
        Title = title;
        return this;
    }

    public String getTag() {
        return Tag;
    }

    public BlindRoom setTag(String tag) {
        Tag = tag;
        return this;
    }

    public int getNumber() {
        return Number;
    }

    public BlindRoom setNumber(int number) {
        Number = number;
        return this;
    }

    public String getChannelName() {
        return ChannelName;
    }

    public BlindRoom setChannelName(String channelName) {
        ChannelName = channelName;
        return this;
    }

    public String getEncryptionKey() {
        return EncryptionKey;
    }

    public BlindRoom setEncryptionKey(String encryptionKey) {
        EncryptionKey = encryptionKey;
        return this;
    }

    public int getEncryptionModeIndex() {
        return EncryptionModeIndex;
    }

    public BlindRoom setEncryptionModeIndex(int encryptionModeIndex) {
        EncryptionModeIndex = encryptionModeIndex;
        return this;
    }
}
