/*
 * @des:分享房间连接 邀请好友
 * @Author: he.xiaoxue
 * @Date: 2018-09-06 19:36:48
 * @Last Modified by: he.xiaoxue
 * @Last Modified time: 2018-09-19 19:33:14
 */

<template>
  <transition name="mask-bg-fade">
    <div class="modal">
      <!--遮罩 -->
      <div class="mask_bg" @click="closeModal"></div>
      <transition name="slide-fade">
        <!-- 弹窗内容 -->
        <div class="modal-container shareRoom">
          <div class="header">
            <img :src="closeIcon" alt="" class="close" @click="closeModal">
          </div>
          <div class="body small">
            <div class="title white">已复制房间链接,快去分享给好友吧！</div>
            <div class="small gray">您也可点击下方按钮手动复制链接给好友</div>
          </div>
          <div class="footer">
            <div class="btn-xs green-btn" v-clipboard:copy='url' v-clipboard:success='clipboardSuccess'>
              <img :src="hrefIcon" alt="" class="href-icon">复制房间链接</div>
            <p class="white x-small">（好友通过链接直接加入房间，无需任何登录）</p>
          </div>
        </div>
      </transition>
    </div>
  </transition>
</template>

<script>
// icon
import closeIcon from '@/assets/close.png'
import hrefIcon from '@/assets/href.png'

import clipboard from '@/directive/clipboard/index.js' // use clipboard by v-directive

export default {
  props: {
    codeEncrypt: String
  },
  directives: {
    clipboard
  },
  data() {
    return {
      show: true,
      closeIcon: closeIcon,
      hrefIcon: hrefIcon,
      title: null, // 房间名
      desc: null, // 公告
      url: ''
    }
  },
  created() {
    // this.url = 'https://www.guyuyin.com/' + this.codeEncrypt
    console.log(this.$router)
  },
  methods: {
    // 关闭弹窗
    closeModal() {
      this.$emit('closeModal')
    },
    titleInput() {},
    // 确认关闭房间
    enSure() {},
    clipboardSuccess() {
      // 复制成功
      this.$hx_toast({ message: '复制成功链接成功，邀请好友加入队伍', time: 1500 })
      this.$emit('closeModal')
    }
  }
}
</script>
<style lang="scss" scoped>
@import '@/styles/mixn.scss';

.modal-container {
  width: px2rem(346);
  min-height: px2rem(200);
  margin-left: px2rem(-193);
  margin-top: px2rem(-120);
  padding: px2rem(20);
  background-color: #242730;
  background-position: center top;
  background-repeat: no-repeat;
  .body {
    margin-top: px2rem(60);
    margin-bottom: px2rem(40);
    .title {
      font-size: px2rem(20);
      margin-bottom: 7px;
    }
  }
  .green-btn {
    width: px2rem(220);
    margin-top: px2rem(30);
    margin-bottom: px2rem(20);
    margin-left: auto;
    margin-right: auto;
    display: block;
    line-height: px2rem(40);
  }
  .href-icon {
    vertical-align: middle;
    margin-right: 5px;
  }
}
</style>
