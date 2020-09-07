<template>
  <transition name="fade" v-if="isShow">
    <div class="hx-toast">
      <div class="mask"></div>
      <div class="msg">{{message}}</div>
    </div>
  </transition>
</template>
<script type="text/javascript">
export default {
  name: 'toast',
  data() {
    return {
      message: '',
      time: null,
      isShow: true
    }
  },
  mounted() {
    if (this.time) {
      this.close()
    }
    this.bus.$on('changeToastMsg', msg => {
      this.message = msg
    })
    this.bus.$on('closeToastMsg', msg => {
      this.isShow = false
    })
  },
  methods: {
    close() {
      window.setTimeout(() => {
        this.isShow = false
      }, this.time)
    },
    changeMsg(msg) {
      this.message = msg
    }
  }
}
</script>
<style lang="scss">
.hx-toast {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  .mask {
    width: 100%;
    height: 100%;
    background: rgba(1, 1, 1, 0.4);
  }
  .msg {
    position: absolute;
    left: 50%;
    top: 50%;
    width: 530px;
    height: 80px;
    line-height: 80px;
    color: #00d95f;
    font-weight: bold;
    padding: 0 30px;
    margin-left: -275px;
    margin-top: -40px;
    background-color: #1a1b20;
    border-radius: 4px;
    font-size: 18px;
  }
}
</style>
