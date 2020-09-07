<template>
  <div class="login-footer small">
    <div class="download" v-show="showDownload">
      <div class="platform" v-for="item in platformList" :key="item.text">
        <div class="qrcode">
          <img :src="item.img" alt="">
        </div>
        <span>{{item.text}}</span>
      </div>
    </div>
    <div class="left small">
      <span>下载《G游语音》APP<br>免费领steam游戏大作</span>
    </div>
    <div class="right">
      <span class="btn-xs green-btn" @mouseover="showDownload=true" @mouseleave="showDownload=false">APP下载</span>
      <span class="btn-xs white-btn" @click="intoAbout">关于我们</span>
    </div>
  </div>
</template>
<script>
import androidIcon from '@/assets/android.png'
import iosIcon from '@/assets/qrcode-ios.png'
export default {
  name: 'LoginFooter',
  data() {
    return {
      androidIcon: androidIcon,
      showDownload: false,
      platformList: [
        {
          img: androidIcon,
          text: '安卓即将上线'
        },
        {
          img: iosIcon,
          text: 'IOS端扫码下载'
        },
        {
          img: androidIcon,
          text: '微信公众号'
        }
      ]
    }
  },
  methods: {
    intoAbout() {
      this.$router.push({ name: 'AboutUs' })
    }
  }
}
</script>
