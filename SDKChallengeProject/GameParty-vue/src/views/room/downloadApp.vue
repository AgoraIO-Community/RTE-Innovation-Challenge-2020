<template>
  <div class="download-app-modal modal">
    <div class="mask_bg" @click="closeModal"></div>
    <div class="download-content">
      <img :src="closeIcon" alt="" class="close" @click="closeModal">
      <img src="../../assets/xuanchuan.png" alt="飞聊语音" class="logo">
      <p class="word">下载APP即可使用全部功能</p>
      <a href="https://itunes.apple.com/cn/app/id1433790115?mt=8" target="_blank">
        <span class="btn" @click="downloadApp">下载APP
      </span>
      </a>
    </div>
  </div>
</template>
<script>
import closeIcon from '@/assets/mobile-close.png'
export default {
  data() {
    return {
      closeIcon: closeIcon
    }
  },
  methods: {
    closeModal() {
      this.$emit('closeModal')
    },
    downloadApp() {
      this.$emit('downloadApp')
    }
  }
}
</script>

<style lang="scss" scoped>
.modal {
  z-index: 9;
}
.download-content {
  position: absolute;
  left: 50%;
  top: 50%;
  width: 300px;
  height: 250px;
  margin-left: -150px;
  margin-top: -125px;
  background-color: #ffffff;
  border-radius: 10px;
  .close {
    position: absolute;
    right: 13px;
    top: 13px;
    width: 22px;
    height: 22px;
    cursor: pointer;
  }
  .logo {
    width: 140px;
    margin: 25px 0;
  }
  .word {
    font-size: 12px;
    color: #333;
  }
  .btn {
    width: 204px;
    height: 40px;
    line-height: 40px;
    font-size: 16px;
    margin-top: 13px;
    display: inline-block;
    background-color: #24c04d;
    border-radius: 45px;
    color: #ffffff;
  }
}
</style>
