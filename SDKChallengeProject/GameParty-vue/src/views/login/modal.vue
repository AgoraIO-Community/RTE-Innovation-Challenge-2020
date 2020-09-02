<template>
  <div class="login-modal">
    <div class="title">
      <img src="../../assets/logo.png" alt="" />
      <p class="base green">飞聊语音·小众玩家聚集地</p>
    </div>
    <div class="form small" v-show="loginByTel">
      <div class="form-group">
        <span class="label small">手机号：</span>
        <span class="error" v-if="phoneErrorText!=''">{{phoneErrorText}}</span>
        <input type="text" placeholder="请输入手机号" name="loginid" v-model="phoneNumber" autocomplete="off">
      </div>
      <div class="form-group">
        <span class="label ">验证码：</span>
        <span class="error" v-if="codeErrorText!=''" @keyup="codeErrorText=''">{{codeErrorText}}</span>
        <div>
          <input type="text" class="code" placeholder="请输入验证码" v-model="code">
          <span class="green code-text c-p" @click="getCode">{{codeObj.text}}</span>
        </div>
      </div>
      <div class="form-group submit">
        <span class="green-btn" @click="onSubmit">登录</span>
      </div>
      <p class="white c-p" @click="loginByWechat">使用微信登陆</p>
    </div>
    <div v-show="!loginByTel" class="form">
      <div id="login_container" class="wx-code"></div>
      <p class="small white wx-text">使用微信扫一扫</p>
      <span class="small white c-p byPhone-btn" @click="loginByTel= true">使用手机号登录</span>
    </div>
  </div>
</template>
<script>
import { login } from '@/api/login'
export default {
  name: 'LoginModal',
  data() {
    return {
      loginByTel: true,
      phoneNumber: '',
      code: '',
      phoneErrorText: '',
      codeErrorText: '',
      codeObj: {
        num: 60,
        text: '获取验证码'
      }
    }
  },
  created() {
    const wxCode = this.$route.query.code
    if (wxCode) {
      this.loginByWxcode(wxCode)
    }
  },
  methods: {
    // 登录
    onSubmit() {
      const that = this
      const phoneExp = /^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\d{8}$/
      // 验证
      if (!this.phoneNumber.match(phoneExp)) {
        this.phoneErrorText = '手机号输入错误'
      }
      if (!this.code) {
        this.codeErrorText = '请输入验证码'
      }
      // 登录请求
      login(this.AV, this.phoneNumber, this.code)
        .then(res => {
          console.log('>>>>> 用户登录成功')
          return that.LeanRT.realtime.createIMClient(res.objectId) // 返回用户登录会话
        })
        .then(imClient => {
          console.log('>>>>> 登录会话成功')
          that.LeanRT.imClient = imClient // 将创建的client对象挂在在VUE下的LeanRT中 该对象全局只能有一个
          that.$router.push('/')
        })
        .catch(error => {
          console.log(error)
        })
    },
    clearError() {
      this.phoneErrorText = ''
    },
    getCode() {
      if (this.codeObj.num < 60) return
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
    },
    loginByWechat() {
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
          'data:text/css;base64,LmltcG93ZXJCb3ggLnFyY29kZSB7d2lkdGg6IDE1MHB4O30KLmltcG93ZXJCb3ggLnRpdGxlIHtkaXNwbGF5OiBub25lO30KLmltcG93ZXJCb3ggLmluZm8ge2Rpc3BsYXk6IG5vbmU7fQouc3RhdHVzX2ljb24ge2Rpc3BsYXk6IG5vbmV9CkBtZWRpYSAobWF4LXdpZHRoOiAgMTY4MHB4KSB7Ci5pbXBvd2VyQm94IC5xcmNvZGUge3dpZHRoOiAxMzBweDt9Cn0KQG1lZGlhIChtYXgtd2lkdGg6ICAxNDQwcHgpIHsKLmltcG93ZXJCb3ggLnFyY29kZSB7d2lkdGg6IDExMHB4O30KfQpAbWVkaWEgKG1heC13aWR0aDogIDEwMjRweCkgewouaW1wb3dlckJveCAucXJjb2RlIHt3aWR0aDogMTAwcHg7fQp9'
      })
      this.loginByTel = false
    },
    loginByWxcode(wxCode) {
      let access_token = ''
      let openid = ''
      let current = null
      const wxUserInfo = {}

      const newQuery = {
        code: wxCode
      }
      const routeParam = {
        query: newQuery
      }
      this.$router.push(routeParam)
      // 获取微信code
      this.AV.Cloud.run('getWXUserInfo', { code: wxCode })
        .then(res => {
          res = JSON.parse(res)
          console.log(res)
          return res
        })
        .then(wxInfo => {
          access_token = wxInfo.access_token
          openid = wxInfo.openid
          return this.AV.User.loginWithAuthDataAndUnionId(
            {
              openid: wxInfo.unionid,
              access_token: wxInfo.access_token,
              expires_in: wxInfo.expires_in
            },
            'weixin',
            wxInfo.unionid,
            {
              unionIdPlatform: 'weixin',
              asMainAccount: true
            }
          )
        })
        .then(user => {
          current = user
          this.currentUser = user.toJSON()
          this.currentUserId = this.currentUser.objectId
          if (this.currentUser.username.length > 20) {
            // 判断用户是否登录过
            return this.AV.Cloud.run('getWXUserDetail', { access_token: access_token, openid: openid })
          } else {
            this.$router.push('/')
            return ''
          }
        })
        .then(res => {
          console.log(res)
          if (res) {
            // 设置用户名称头像
            res = JSON.parse(res)
            wxUserInfo.nickname = res.nickname
            wxUserInfo.headimgurl = res.headimgurl

            current.set('username', res.nickname)
            const file = this.AV.File.withURL('avatar', res.headimgurl)
            current.set('avatar', file)
            return current.save()
          }
        })
        .then(res => {
          this.$router.push('/')
        })
        .catch(error => {
          if (error.code == 202) {
            const random = Math.floor(Math.random() * 100)
            current.set('username', wxUserInfo.nickname + random)
            const file = this.AV.File.withURL('avatar', wxUserInfo.headimgurl)
            current.set('avatar', file)
            current.save()
            this.$router.push('/')
          }
          console.log(error)
          this.$hx_toast({ message: '微信登录失败', time: 1500 })
        })
    }
  }
}
</script>
<style scoped>
.error {
  color: red;
}
</style>
