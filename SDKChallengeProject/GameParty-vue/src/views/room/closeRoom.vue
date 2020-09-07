/*
 * 退出房间弹窗
 * @Author: he.xiaoxue
 * @Date: 2018-09-06 10:34:05
 * @Last Modified by: he.xiaoxue
 * @Last Modified time: 2018-09-06 11:08:01
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
            <div class="title white">确定返回大厅？</div>
            <div class="small gray">返回大厅将会退出当前语音房间</div>
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
  width: px2rem(300);
  min-height: px2rem(130);
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
    &.cancel-btn {
      border: solid 1px #00d95f;
      color: #00d95f;
      box-sizing: border-box;
    }
  }
}
</style>
