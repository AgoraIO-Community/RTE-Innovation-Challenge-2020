/*
 * 匿名用户登录窗口
 * @Author: he.xiaoxue
 * @Date: 2018-09-09 11:39:38
 * @Last Modified by: he.xiaoxue
 * @Last Modified time: 2018-12-18 23:38:46
 */
<template>
  <transition name="mask-bg-fade">
    <div class="modal">
      <!--遮罩 -->
      <div class="mask_bg" @click="closeModal"></div>
      <transition name="slide-fade">
        <!-- 弹窗内容 -->
        <div class="modal-container by-wechat" v-if="loginBy=='wechat'">
          <div class="header">
            <span class="title large">微信登录</span>
            <img :src="closeIcon" alt="" class="close" @click="closeModal">
          </div>
          <div class="body">
            <p class="white small">使用微信扫描下方二维码码登录</p>
            <div id="login_container"></div>
          </div>
        </div>
        <div class="modal-container by-code" v-if="loginBy=='code'">
          <div class="header">
            <span class="title large">登录飞聊语音</span>
            <img :src="closeIcon" alt="" class="close" @click="closeModal">
          </div>
          <div class="body">
            <div class="form-group">
              <span class="label small">手机号：</span>
              <span class="error" v-if="phoneErrorText!=''">{{phoneErrorText}}</span>
              <input type="text" placeholder="请输入手机号" name="loginid" v-model="phoneNumber" autocomplete="off">
            </div>
            <div class="form-group">
              <span class="label small">验证码：</span>
              <span class="error" v-if="codeErrorText!=''" @keyup="codeErrorText=''">{{codeErrorText}}</span>
              <div>
                <input type="text" class="code" placeholder="请输入验证码" v-model="code">
                <span class="green small code-text c-p" @click="getCode">{{codeObj.text}}</span>
              </div>
            </div>
          </div>
          <div class="footer">
            <span class="btn-xs green-btn" @click="onSubmit">{{isLogining?'登录中...':'登录'}}</span>
          </div>
        </div>
      </transition>
    </div>
  </transition>
</template>
<script>
import closeIcon from '@/assets/close.png'
import { login } from '@/api/login'

export default {
  name: 'UserLogin',
  props: {
    loginBy: String
  },
  data() {
    return {
      closeIcon: closeIcon,
      phoneErrorText: '',
      phoneNumber: '',
      codeErrorText: '',
      code: '',
      codeObj: {
        num: 60,
        text: '获取验证码'
      },
      isLogining: false
    }
  },
  created() {
    /*   var obj = new WxLogin({
      self_redirect: true,
      id: 'login_container',
      appid: 'wxcd1d0b740f17ebe8',
      scope: 'snsapi_login',
      redirect_uri: 'http%3a%2f%2flocalhost%3a8080%2fteam%2f5b94880b67f356005c45f78a',
      style: 'black'
    })
    console.log(obj) */
  },
  mounted() {
    if (this.loginBy === 'wechat') {
      this.getWxcode()
    }
  },
  methods: {
    // 关闭弹窗
    closeModal() {
      this.$emit('closeModal')
    },
    getWxcode() {
      const wHref = window.location.href
      const url = encodeURI(wHref.replace('wwww.', ''))
      console.log(url)
      var obj = new WxLogin({
        self_redirect: false,
        id: 'login_container',
        appid: 'wxcd1d0b740f17ebe8',
        scope: 'snsapi_login',
        redirect_uri: url,
        href:
          'data:text/css;base64,LmltcG93ZXJCb3ggLnFyY29kZSB7d2lkdGg6IDIwMHB4O30KLmltcG93ZXJCb3ggLnRpdGxlIHtkaXNwbGF5OiBub25lO30KLmltcG93ZXJCb3ggLmluZm8ge2Rpc3BsYXk6IG5vbmU7fQouc3RhdHVzX2ljb24ge2Rpc3BsYXk6IG5vbmV9'
      })
      console.log(obj)
    },
    // 登录
    onSubmit() {
      if (this.isLogining) return
      this.isLogining = true
      const phoneExp = /^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\d{8}$/
      // 验证
      if (!this.phoneNumber.match(phoneExp)) {
        this.phoneErrorText = '手机号输入错误'
        this.isLogining = false
        return
      }
      if (!this.code) {
        this.codeErrorText = '请输入验证码'
        this.isLogining = false
        return
      }
      // 登录请求
      login(this.AV, this.phoneNumber, this.code)
        .then(res => {
          this.isLogining = false
          // FIXME: 用户登录成功后聊天室回调
          this.$emit('loginSucess')
          this.closeModal()
          console.log('>>>>> 用户登录成功')
        })
        .catch(error => {
          this.isLogining = false
          console.error(error)
        })
    },
    clearError() {
      this.phoneErrorText = ''
    },
    getCode() {
      if (this.codeObj.num < 60) return
      const phoneExp = /^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\d{8}$/
      // 验证
      if (!this.phoneNumber.match(phoneExp)) {
        this.phoneErrorText = '手机号输入错误'
        return
      }
      const mobilePhoneNumber = this.phoneNumber
      this.AV.Cloud.requestSmsCode({
        mobilePhoneNumber: mobilePhoneNumber, // 目标手机号
        sign: '飞聊社 ' // 控制台预设的短信签名
      }).then(
        function() {
          // 调用成功
        },
        function(err) {
          console.log(err)
          // 调用失败
        }
      )
      const count = () => {
        setTimeout(() => {
          this.codeObj.num--
          const num = this.codeObj.num < 10 ? '0' + this.codeObj.num : this.codeObj.num
          this.codeObj.text = num + '秒后重新获取'
          if (this.codeObj.num > 0) {
            count()
          } else {
            this.codeObj = {
              num: 60,
              text: '获取验证码'
            }
          }
        }, 1000)
      }
      count()
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/styles/mixn.scss';
.modal-container {
  .header {
    .title {
      color: #00d95f;
    }
  }
  &.by-wechat {
    width: 300px;
    height: 320px;
    margin-left: -130px;
    margin-top: -177px;
    padding: 30px 0;
    background-color: #242730;
    .header {
      padding: 0 30px;
    }
    .body {
      p {
        margin-top: 20px;
        margin-bottom: 20px;
      }
    }
  }
  &.by-code {
    width: px2rem(260);
    height: px2rem(300);
    margin-left: px2rem(-130);
    margin-top: px2rem(-177);
    padding: px2rem(30) px2rem(26);
    background-color: #242730;
    .header {
      padding: 0 px2rem(30);
    }
    .body {
      margin-top: px2rem(40);
      .form-group {
        text-align: left;
        + .form-group {
          margin-top: px2rem(20);
          ~ .submit {
            margin-top: px2rem(35);
            margin-bottom: px2rem(30);
          }
        }
        .label {
          color: #ccc;
          line-height: px2rem(30);
          padding-left: px2rem(10);
        }
        input {
          display: block;
          background: #14161e;
          padding: 0 px2rem(15);
          height: px2rem(40);
          line-height: px2rem(40);
          border-radius: px2rem(20);
          border: none;
          width: 100%;
          outline: none;
          color: #fff;
        }
        .code {
          width: px2rem(160);
          display: inline-block;
        }
        .code-text {
          width: px2rem(100);
          display: inline-block;
          text-align: center;
          white-space: nowrap;
        }
      }
    }
    .green-btn {
      display: inline-block;
      height: px2rem(40);
      line-height: px2rem(40);
      border-radius: px2rem(20);
      width: 100%;
      margin-top: px2rem(40);
      text-align: center;
      font-weight: bold;
      padding: 0;
    }
  }
}
</style>
<style>
.by-wechat iframe {
  height: 220px !important;
  width: 210px !important;
}
</style>
