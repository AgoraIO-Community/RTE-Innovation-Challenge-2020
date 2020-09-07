<template>
  <div class="app">
    <li-load @loadFinishEvent="loadEvent"></li-load>
    <div style="width: 100%;height: 45%;"></div>
    <div class="loading">正在加载群聊信息...</div>
  </div>
</template>

<script>
import load from '../loading';
export default {
  components: {
    'li-load': load
  },
  data () {
    return {
      load: false
    };
  },
  methods: {
    loadEvent () {
      this.$router.replace({path: '/room'});
    }
  }
};
</script>

<style lang="less" scoped>
.app {
  width: 100%;
  height: 100%;
  background: #fff;
  .loading {
    width: 100%;
    height: 100%;
    font-size: 14px;
    color: #499eff;
    font-family: "Microsoft YaHei";
    text-align: center;
  }
}
</style>
