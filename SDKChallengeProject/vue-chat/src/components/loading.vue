<template>
    <div>
        <div class="base load-once" :class="{remove: !loadState}" v-show="loadStatus"><span class="text">鸡年大吉</span></div><!--初始化load，只有一次-->
        <!--<transition name="fade">-->
            <!--<div class="base load" v-if="load"></div>&lt;!&ndash;加载等待&ndash;&gt;-->
        <!--</transition>-->
    </div>
</template>
<script>
export default {
  data () {
    return {
      loadState: true,
      loadStatus: true
    };
  },
  mounted () {
    var me = this;
    setTimeout(function () {
        me.loadState = false;
        setTimeout(function () {
            me.loadStatus = false;
            me.$emit('loadFinishEvent');
        }, 500);
    }, 1500);
  }
};
</script>

<style lang="less">
.base {
  width: 100%;
  height: 100%;
  position: fixed;
  z-index: 100;
  display: block;
}

.load-once {
  background: #499eff url(../images/ji.png) no-repeat 45% 45%;
  background-size: 120px;
  opacity: 1;
  transition: all 0.5s;
  &.remove {
      opacity: 0;
  }
  .text {
    width: 100%;
    height: 100%;
    color: #fff;
    text-align: center;
    font-size: 20px;
    font-family: 'Microsoft YaHei', arial, sans-serif, 'Droid Sans Fallback';
    padding-top: 102%;
    display: block;
    box-sizing: border-box;
  }
}
</style>
