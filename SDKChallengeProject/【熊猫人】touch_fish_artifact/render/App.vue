<template>
  <div id='app'>
    <router-view />
  </div>
</template>

<script lang='ts'>
import { Component, Vue } from "vue-property-decorator";

@Component
export default class App extends Vue {}
</script>

<style lang='scss' scoped>
#app {
  width: 100vw;
  height: 100vh;
  background-image: radial-gradient(
    circle at 0% 0%,
    #373b52,
    #252736 51%,
    #1d1e26
  );
}
</style>
