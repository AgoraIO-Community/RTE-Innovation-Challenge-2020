<template>
  <transition name="mask-bg-fade">
    <div class="modal">
      <!--遮罩 -->
      <div class="mask_bg" @click="closeModal"></div>
      <transition name="slide-fade">
        <!-- 弹窗内容 -->
        <div class="modal-container user-info">
          <div class="header small">
            <span class="base gray c-p" v-show="userId!=userInfo.objectId" @click="reportUser">举报</span>
            <img :src="closeIcon" alt="" class="close" @click="closeModal">
          </div>
          <div class="body small white">
            <img :src="userInfo.avatar|avatarFilter" alt="" class="avatar">
            <p class="large">{{userInfo.username}}</p>
            <div class="stem-sex ">
              <!-- <p>SteamID：{{userInfo.steamID||'-'}}</p> -->
              <p class="gray">
                <span class="sex " :class="{man:userInfo.sex}"></span>{{userInfo.age}}岁 · {{userInfo.lastAddress||'-'}}
              </p>
            </div>
            <div class="achievement">
              <div class="tdzl">
                <p class="count base">{{userInfo.tuandui}}</p>
                <p class="x-samll">团队主力</p>
              </div>
              <div class="zjdy">
                <p class="count base"> {{userInfo.zuijia}}</p>
                <p class="x-samll">最佳队友</p>
              </div>
              <div class="tdyh">
                <p class="count base">{{userInfo.taidu}}</p>
                <p class="x-samll">态度友好</p>
              </div>
              <div class="lyzr">
                <p class="count base">{{userInfo.leyu}}</p>
                <p class="x-samll">乐于助人</p>
              </div>
              <p class="x-samll gray">下载APP即可为TA点赞</p>
            </div>
          </div>
          <div class="footer small white">
            <div v-show="userId!=userInfo.objectId">
              <span class="owner" @click="followSet">{{isFollow?'取消关注':'关注TA'}}</span>
              <span class="owner" v-if="isWoner" @click="kickedOneMember">踢出</span>
            </div>
          </div>
        </div>
      </transition>
    </div>
  </transition>
</template>
<script>
// icon
import closeIcon from '@/assets/close.png'
import AvatarDefault from '@/assets/avatar-default.png'

import { isFollow } from '@/api/chatRoom'
export default {
  name: 'UserInfo',
  props: {
    show: Boolean, // 显示条件
    userInfo: Object, // 用户信息
    isWoner: Boolean // 是否为队长
  },
  filters: {
    avatarFilter(avatar) {
      if (avatar) {
        return avatar.url
      } else {
        return AvatarDefault
      }
    }
  },
  data() {
    return {
      closeIcon: closeIcon,
      userId: null,
      isFollow: false
    }
  },
  computed: {
    // 从Vuex中获取存储的游戏列表
    gameList() {
      return this.$store.state.game.gameTypeList
    }
  },
  created() {
    this.userId = this.AV.User.current().toJSON().objectId
    isFollow(this.userInfo.objectId).then(res => {
      if (res) {
        this.isFollow = true
      }
    })
    /*  this.C = this.AV.User.current().toJSON()
    console.log(this.userInfo) */
  },
  methods: {
    // 关闭弹窗
    closeModal() {
      this.$emit('closeModal')
    },

    // 提交房间
    roomSubmit() {
      this.$emit('closeModal')
      // TODO: 提交请求
    },
    // 关注用户
    followSet() {
      if (!this.isFollow) {
        this.AV.User.current()
          .follow(this.userInfo.objectId)
          .then(() => {
            this.isFollow = !this.isFollow
          })
      } else {
        this.AV.User.current()
          .unfollow(this.userInfo.objectId)
          .then(() => {
            this.isFollow = !this.isFollow
          })
      }
    },
    // 踢出用户
    kickedOneMember() {
      this.$emit('kickedOneMember', this.userInfo.objectId)
    },

    // 举报用户
    reportUser() {
      this.$emit('reportUser', this.userInfo.objectId)
    }
  }
}</script>
