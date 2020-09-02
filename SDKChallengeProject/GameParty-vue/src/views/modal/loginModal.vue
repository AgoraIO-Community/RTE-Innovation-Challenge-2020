/*
 * 大厅用户登录
 * @Author: he.xiaoxue
 * @Date: 2018-09-28 23:26:49
 * @Last Modified by: he.xiaoxue
 * @Last Modified time: 2018-12-26 21:51:45
 */
<template>
  <transition name="mask-bg-fade">
    <div class="modal">
      <!--遮罩 -->
      <div class="mask_bg" @click="closeModal"></div>
      <transition name="slide-fade">
        <!-- 弹窗内容 -->
        <div class="modal-container">
          <div class="header">
            <div class="title">
              <img src="../../assets/logo_1.png" alt="">
              <span class="large green">Hi,你好</span>
            </div>
            <img :src="closeIcon" alt="" class="close" @click="closeModal">
          </div>
            <div class="body small">
              <p>飞聊为你提供了最纯粹的语音服务,使用 浏览器 即可与他人语音</p>
            </div>
            <div class="footer" v-if="way=='gift'">
              <p class="small">现在,登录飞聊创建房间以 链接形式 邀请好友或陌生人加入</p>
              <div class="btn-group">
                <span class="white-btn" @click="closeModal">先逛逛</span> 
                <span class="green-btn" @click="loginBy('wechat')">微信登录</span>
                <span class="green-btn" @click="loginBy('code')">手机号登录</span>
              </div>
            </div>
            <!-- <div class="footer" v-else>
              <p class="small">扫码加入QQ群或者直接加QQ群：313055838，提供账号ID即可领取。</p>
              <div class="qq-code">
                <img src="../../assets/qq_code.png" alt="加入QQ群">
              </div>
              </div> -->
            </div>
      </transition>
    </div>
  </transition>
</template>

<script>
// icon
import closeIcon from '@/assets/close.png'

export default {
  props: {
    way: String
  },
  data() {
    return {
      show: true,
      closeIcon: closeIcon
    }
  },
  created() {
    //
  },
  methods: {
    // 关闭弹窗
    closeModal() {
      this.$emit('closeModal')
    },
    titleInput() {},
    // 确认关闭房间
    enSure() {
      //
    },
    loginBy(way) {
      this.$emit('loginBy', way)
    }
  }
}
</script>
<style lang="scss" scoped>
@import '@/styles/mixn.scss';

.modal-container {
  width: px2rem(380);
  min-height: px2rem(220);
  margin-left: px2rem(-200);
  margin-top: px2rem(-130);
  padding: px2rem(30);
  color: #fff;
  background-color: #242730;
  text-align: left;
  background-image: url(../../assets/welcome_bg.png);
  .title {
    font-weight: bold;
    img {
      vertical-align: top;
      margin-right: 5px;
    }
  }
  p {
    margin-top: px2rem(25);
    word-break: break-all;
  }
  .btn-group {
    text-align: right;
    margin-top: px2rem(74);
    .white-btn,
    .green-btn {
      padding: px2rem(7) px2rem(20);
      min-width: px2rem(50);
      border-radius: px2rem(23);
      display: inline-block;
      margin-left: px2rem(14);
      text-align: center;
      cursor: pointer;
    }
    .white-btn {
      border: solid 1px #00d95f;
      color: #00d95f;
    }
  }
  .qq-code{
    text-align: center;
    img{
      width: px2rem(84);
    }
  }
}
</style>
