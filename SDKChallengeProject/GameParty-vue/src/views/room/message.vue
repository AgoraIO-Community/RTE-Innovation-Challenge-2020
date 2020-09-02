/*
 * @Author: he.xiaoxue
 * @Date: 2018-09-02 01:02:01
 * @Last Modified by: he.xiaoxue
 * @Last Modified time: 2018-09-05 23:20:06
 */
<template>
  <div class="message" :class="type|classFilter">
    <!-- 新建房间的提示 -->
    <template v-if="type=='tips'">
      注意：房间内文明聊天，素质交友，禁止发布涉及暴力、色情、诈骗等信息
    </template>
    <!-- 邀请新人提示 -->
    <template v-if="type=='create'">
      <div class="invite-inner">
        <span>创建房间成功,立即邀请好友一起组队吧！</span>
        <span class="green c-p m-l">邀请</span>
      </div>
    </template>
    <!-- 邀请新人提示 -->
    <template v-if="type=='newMember'">
      <div class="invite-inner">
        <span class="green m-r" v-if="msgInfo.userName">{{msgInfo.userName}}</span>
        <span>{{msgInfo.text||'加入了房间队伍'}}</span>
      </div>
    </template>
    <!-- 组内发言 模板-->
    <template v-if="type=='msg'">
      <span>{{msgInfo.sendName}}：</span>
      <span class="white">{{msgInfo.sendText}}</span>
    </template>
  </div>
</template>

<script>
export default {
  name: 'Message',
  props: {
    type: String,
    msgInfo: null
  },
  filters: {
    classFilter(type) {
      const classObj = {
        tips: 'tips',
        create: 'invite',
        newMember: 'invite',
        msg: 'chat-item'
      }
      return classObj[type]
    }
  }
}
</script>
