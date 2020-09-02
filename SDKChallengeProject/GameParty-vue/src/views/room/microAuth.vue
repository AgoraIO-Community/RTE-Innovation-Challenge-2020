/*
 * 麦克风权限提示
 * @Author: he.xiaoxue
 * @Date: 2018-09-17 22:45:34
 * @Last Modified by: he.xiaoxue
 * @Last Modified time: 2018-10-03 17:42:25
 */

<template>
  <transition name="mask-bg-fade">
    <div class="modal">
      <!--遮罩 -->
      <div class="mask_bg" @click="closeModal"></div>
      <transition name="slide-fade">
        <div class="modal-container" :class="{system:openWay!='auth'}">
           <!-- 语音权限 -->
          <div class="body" v-if="openWay=='auth'">
            <div class="title base white">请允许浏览器的语音权限才可以与队友语音</div>
            <p class="chrome small">Chrome浏览器在右上角设置为继续允许即可</p>
            <img :src="chromeImg">
          </div>
           <!-- 浏览器不支持 -->
          <div class="body " v-else>
            <img :src="systemImg" class="img-system">
            <div class="title base white">您当前浏览器版本不支持语音，建议您:</div>
            <p class="chrome small text-left">1.将浏览器更新到最新版本</p>
            <p class="chrome small text-left">2.使用推荐的浏览器：<span class="green">Chrome浏览器</span>、<span class="green">搜狗浏览器</span>、<span class="green">QQ浏览器</span></p>
            <p class="chrome small text-left">3.点击下载<a class="download-href" href= "http://file.guyuyin.com/b1005120761fbd26bb6d.zip"><span class="green">飞聊语音桌面版</span></a></p>
          </div>
          <div class="footer">
            <div class="btn-xs green-btn small" @click="closeModal">知道了</div>
          </div>
        </div>
      </transition>
    </div>
  </transition>
</template>

<script>
import chromeImg from '@/assets/chrome-set.png'
import systemImg from '@/assets/yuyin-auth.png'
export default {
  props: {
    openWay: String
  },
  data() {
    return {
      show: true,
      chromeImg: chromeImg,
      systemImg: systemImg
    }
  },
  created() {
    //
  },
  methods: {
    // 关闭弹窗
    closeModal() {
      this.$emit('closeModal')
    }
  }
}</script>
<style lang="scss" scoped>
@import '@/styles/mixn.scss';

.modal-container {
  width: px2rem(304);
  min-height: px2rem(250);
  min-width: 250px;
  margin-left: px2rem(-152);
  margin-top: px2rem(-125);
  padding: px2rem(30) px2rem(24);
  background-color: #242730;
  &.system {
    //  width: px2rem(294);
  }
  .text-left {
    text-align: left;
  }
  .body {
    .title {
      margin-bottom: px2rem(24);
    }
    .chrome {
      color: #ccc;
      margin-bottom: px2rem(10);
    }
    .download-href{
      &:hover{
        text-decoration: none;
      }
    }
    img {
      width: 100%;
      margin-top: 5px;
      &.img-system {
        width: px2rem(124);
        margin: 0 auto px2rem(20);
      }
    }
  }
  .green-btn {
    margin-top: px2rem(30);
    display: block;
    line-height: px2rem(40);
  }
}
</style>
