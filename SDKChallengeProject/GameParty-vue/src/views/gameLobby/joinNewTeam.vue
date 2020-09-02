/*
 * 加入新队伍弹窗
 * @Author: he.xiaoxue
 * @Date: 2018-09-24 22:01:05
 * @Last Modified by: he.xiaoxue
 * @Last Modified time: 2018-09-24 22:54:07
 */
<template>
  <transition name="mask-bg-fade">
    <div class="modal" v-if="show">
      <!--遮罩 -->
      <div class="mask_bg" @click="closeModal"></div>
      <transition name="slide-fade">
        <!-- 弹窗内容 -->
        <div class="modal-container sm">
          <div class="body">
            <div class="large white">加入该房间会退出当前所在房间，确定加入？</div>
          </div>
          <div class="footer">
            <span class="btn-xs cancel-btn" @click="closeModal">取消</span>
            <span class="btn-xs green-btn" @click="enSure">确定</span>
          </div>
        </div>
      </transition>
    </div>
  </transition>
</template>

<script>
export default {
  data() {
    return {
      show: true
    }
  },
  methods: {
    // 关闭弹窗
    closeModal() {
      this.$emit('closeModal')
    },
    // 确认关闭房间
    enSure() {
      this.$emit('enSure')
    }
  }
}
</script>
<style lang="scss" scoped>
@import '@/styles/mixn.scss';
.modal-container {
  width: px2rem(400);
  min-height: px2rem(100);
  margin-left: px2rem(-180);
  margin-top: px2rem(-95);
  padding: px2rem(30);
  .body {
    margin-bottom: px2rem(40);
    .title {
      font-size: px2rem(20);
      margin-bottom: 7px;
    }
  }

  .btn-xs {
    display: inline-block;
    height: px2rem(40);
    line-height: px2rem(40);
    border-radius: px2rem(20);
    width: px2rem(120);
    font-weight: bold;
    padding: 0;
    cursor: pointer;
    + .btn-xs {
      margin-left: px2rem(60);
    }
    &.cancel-btn {
      border: solid 1px #00d95f;
      color: #00d95f;
      box-sizing: border-box;
    }
  }
}
</style>
