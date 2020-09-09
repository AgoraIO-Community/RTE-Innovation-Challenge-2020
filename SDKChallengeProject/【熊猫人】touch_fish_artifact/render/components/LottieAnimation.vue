<template>
  <div class='lottie-animation-container' ref='lottieAnimationContainer'></div>
</template>
<script>
import lottie from "lottie-web";

export default {
  props: {
    animationData: Object,
    autoplay: {
      type: Boolean,
      default() {
        return true;
      }
    },
    loop: {
      type: Boolean,
      default() {
        return false;
      }
    }
  },
  data: () => {
    return {
      animation: null
    };
  },
  mounted() {
    this.animation = lottie.loadAnimation({
      container: this.$refs.lottieAnimationContainer, // the dom element that will contain the animation
      renderer: "svg",
      loop: this.loop,
      autoplay: this.autoplay,
      animationData: this.animationData
    });
  }
};
</script>
<style lang='scss' scoped>
.lottie-animation-container {
  display: block;
  width: 100%;
  height: 100%;
}
</style>