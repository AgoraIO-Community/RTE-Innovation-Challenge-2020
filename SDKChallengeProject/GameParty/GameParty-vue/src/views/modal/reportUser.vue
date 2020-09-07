/*
 * 举报用户弹窗
 * @Author: he.xiaoxue
 * @Date: 2018-09-25 23:45:49
 * @Last Modified by: he.xiaoxue
 * @Last Modified time: 2018-09-26 00:29:23
 */
<template>
  <transition name="mask-bg-fade">
    <div class="modal">
      <!--遮罩 -->
      <div class="mask_bg" @click="closeModal"></div>
      <transition name="slide-fade">
        <!-- 弹窗内容 -->
        <div class="modal-container sm">
          <div class="header">
            <span class="large white">举报用户</span>
            <img :src="closeIcon" alt="" class="close" @click="closeModal">
          </div>
          <div class="body small">
            <div class="input-group">
              <label for="">举报类型：</label>
              <div>
                <span class="report-item" v-for="item in reportList" @click="changeReportKind(item)" :key="item" :class="{active:reportKind==item}">{{item}}</span>
              </div>
            </div>
            <div class="input-group">
              <label for="">描述：</label>
              <textarea @keyup.enter="enSure" maxlength="40" rows="3" placeholder="描述该用户的行为..." class="white" v-model="desc"></textarea>
              <span class="count">{{this.desc.length||0}}/40</span>
            </div>
          </div>
          <div class="footer">
            <div class="btn-xs green-btn" @click="enSure">提交</div>
          </div>
        </div>
      </transition>
    </div>
  </transition>
</template>

<script>
// icon
import closeIcon from '@/assets/close.png'

export default {
  props: {
    userInfo: Object
  },
  data() {
    return {
      show: true,
      closeIcon: closeIcon,
      title: null, // 房间名
      desc: '', // 描述
      reportList: ['言语攻击', '恶意宣传', '涉黄', '诈骗', '恶意骚扰', '散布谣言', '聚众搞事'],
      reportKind: '言语攻击'
    }
  },
  created() {
    //
  },
  methods: {
    // 关闭弹窗
    closeModal() {
      this.$emit('closeModal')
    },
    titleInput() {},
    // 确认关闭房间
    enSure() {
      //
    },
    changeReportKind(value) {
      //
      this.reportKind = value
    }
  }
}
</script>
<style lang="scss" scoped>
@import '@/styles/mixn.scss';

.modal-container {
  width: 350px;
  min-height: 320px;
  margin-left: -200px;
  margin-top: -170px;
  padding: px2rem(30);
  background-color: #242730;

  .body {
    .input-group {
      text-align: left;
      margin-top: px2rem(30);
      position: relative;
      label {
        margin-bottom: px2rem(20);
        display: block;
      }
      .count {
        position: absolute;
        right: 10px;
        bottom: 10px;
      }
      input,
      textarea {
        width: 100%;
      }

      input {
        background: transparent;
        border: none;
        border-bottom: 1px solid #3a3d45;
        line-height: 25px;
        outline: none;
        &:focus {
          outline: none;
        }
      }
      .textarea-con {
        background-color: #1e2027;
        -moz-box-sizing: border-box;
        -webkit-box-sizing: border-box;
        box-sizing: border-box;
      }
      textarea {
        height: px2rem(100);
        background: transparent;
        border-radius: 4px;
        border: none;
        resize: none;
        padding: px2rem(15);

        background-color: #1e2027;
        &:focus {
          outline: none;
        }
      }
    }
    .report-item {
      min-width: 80px;
      padding: px2rem(9) 0;
      text-align: center;
      background-color: #31343c;
      border-radius: 4px;
      color: #777777;
      display: inline-block;
      margin-bottom: px2rem(10);
      cursor: pointer;
      + .report-item {
        margin-left: px2rem(10);
      }
      &:nth-child(4n + 1) {
        margin-left: 0;
      }
      &.active {
        background-color: #00d95f;
        color: #1e2027;
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
