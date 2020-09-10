<template>
  <div class="home" @click="handleJoin">
    <LottieAnimation :animationData="animationData" loop />
    <div class="lds-ellipsis" v-show="loading">
      <div></div>
      <div></div>
      <div></div>
      <div></div>
    </div>
  </div>
</template>

<script lang='ts'>
import { Component, Vue } from "vue-property-decorator";
import { ipcRenderer } from "electron";
import LottieAnimation from "@/components/LottieAnimation.vue";
import animationData from "@/assets/fish-in-bowl.json";
import { CHANNEL_TIME } from "@/config/config";

@Component({
  components: {
    LottieAnimation
  }
})
export default class Home extends Vue {
  private channel: string = "";

  private loading: boolean = false;

  private animationData = animationData;

  private async handleJoin(): Promise<void> {
    this.channel = Math.floor(
      new Date().getTime() / (CHANNEL_TIME * 1000)
    ).toString();
    console.log("joining channel:", this.channel);
    this.loading = true;
    const res = await ipcRenderer.invoke("getToken", this.channel);
    res.channelName = this.channel;
    this.loading = false;
    this.$store.commit("tokenInfo", res);
    this.$router.replace("Fish");
    console.log(res);
  }
}
</script>

<style lang='scss' scoped>
.home {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
  display: flex;
  justify-content: center;
  align-items: center;

  .lds-ellipsis {
    display: inline-block;
    position: absolute;
    width: 80px;
    height: 80px;
    z-index: 10;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
  }
  .lds-ellipsis div {
    position: absolute;
    top: 33px;
    width: 13px;
    height: 13px;
    border-radius: 50%;
    background: #fff;
    animation-timing-function: cubic-bezier(0, 1, 1, 0);
  }
  .lds-ellipsis div:nth-child(1) {
    left: 8px;
    animation: lds-ellipsis1 0.6s infinite;
  }
  .lds-ellipsis div:nth-child(2) {
    left: 8px;
    animation: lds-ellipsis2 0.6s infinite;
  }
  .lds-ellipsis div:nth-child(3) {
    left: 32px;
    animation: lds-ellipsis2 0.6s infinite;
  }
  .lds-ellipsis div:nth-child(4) {
    left: 56px;
    animation: lds-ellipsis3 0.6s infinite;
  }
  @keyframes lds-ellipsis1 {
    0% {
      transform: scale(0);
    }
    100% {
      transform: scale(1);
    }
  }
  @keyframes lds-ellipsis3 {
    0% {
      transform: scale(1);
    }
    100% {
      transform: scale(0);
    }
  }
  @keyframes lds-ellipsis2 {
    0% {
      transform: translate(0, 0);
    }
    100% {
      transform: translate(24px, 0);
    }
  }
}
</style>
