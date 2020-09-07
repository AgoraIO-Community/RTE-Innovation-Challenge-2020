/*
 * 房间设置 标题 公告
 * @Author: he.xiaoxue
 * @Date: 2018-09-06 11:24:34
 * @Last Modified by: he.xiaoxue
 * @Last Modified time: 2018-12-26 23:00:56
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
            <span class="large white">房间设置</span>
            <img :src="closeIcon" alt="" class="close" @click="closeModal">
          </div>
          <div class="body small">
            <div class="input-group">
              <label for="">房间标题：</label>
              <input type="text" class="white" v-model="title" @keyup.enter="enSure" maxlength="24">
              <span class="count">{{this.title.length||0}}/24</span>
            </div>
            <!-- <div class="input-group">
              <label for="">房间公告：</label>
              <textarea  @keyup.enter="enSure" maxlength="48" rows="3" placeholder="说一点什么." class="white" v-model="desc"></textarea>
              <span class="count">{{this.desc.length||0}}/48</span>
            </div> -->
           <div class="input-group">
              <label for="">调整房间人数：</label>
              <span v-for="item in numList" :key="item" @click="changeMaxnum(item)" class="btn" :class="{active:item==activeMaxNum}">{{item}}人</span>
            </div>

          </div>
          <div class="footer">
            <div class="btn-xs green-btn" @click="enSure">确认修改</div>
          </div>
        </div>
      </transition>
    </div>
  </transition>
</template>

<script>
// icon
import closeIcon from '@/assets/close.png'

import { DB_TEAM } from '@/leanCloud/global'

export default {
  props: {
    teamInfo: Object
  },
  data() {
    return {
      show: true,
      closeIcon: closeIcon,
      title: null, // 房间名
      desc: null, // 公告
      numList: [2, 4, 5, 6, 8],
      activeMaxNum: 2
    }
  },
  created() {
    this.title = this.teamInfo.title
    this.desc = this.teamInfo.desc
    this.activeMaxNum = this.teamInfo.maxnum;
  },
  methods: {
    // 关闭弹窗
    closeModal() {
      this.$emit('closeModal')
    },
     changeMaxnum(num) {
      this.activeMaxNum = num
    },
    titleInput() {},
    // 确认关闭房间
    enSure() {
      if (this.activeMaxNum === this.teamInfo.maxnum && this.title === this.teamInfo.title) {
        this.$emit('closeModal')
        return
      }
      if (this.title == '') {
        return
      }
      const teamObj = this.AV.Object.createWithoutData(DB_TEAM, this.teamInfo.objectId)
      teamObj.set('title', this.title)
      teamObj.set('maxnum',this.activeMaxNum)
      teamObj
        .save()
        .then(() => {
          const data = {
            title: this.title,
            desc: this.desc
          }
          this.$emit('update', data)
          this.$emit('closeModal')
        })
        .catch(error => {
          this.$emit('closeModal')
        })
    }
  }
}</script>
<style lang="scss" scoped>
@import '@/styles/mixn.scss';

.modal-container {
  width: px2rem(400);
  min-height: px2rem(340);
  margin-left: px2rem(-200);
  margin-top: px2rem(-170);
  padding: px2rem(30);
  background-color: #242730;

  .body {
    .input-group {
      text-align: left;
      margin-top: px2rem(30);
      position: relative;
      .count {
        position: absolute;
        right: 10px;
        bottom: 10px;
      }
      input,
      textarea {
        width: 100%;
        margin-top: 8px;
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
  }
  .green-btn {
    margin-top: px2rem(30);
    display: block;
    line-height: px2rem(40);
  }
 .btn {
      padding: 9px 12px;
      display: inline-block;
      color:#1e2027;
      background-color: #777;
      border-radius: 4px;
      margin-bottom: 10px;
      cursor: pointer;
      min-width: 30px;
      text-align: center;
      + .btn {
        margin-left: 10px;
      }
      &:nth-child(6n + 1) {
        margin-left: 0;
      }
      &.active,
      &:hover {
        color: #1e2027;
        background-color: #00d95f;
        border-radius: 4px;
      }
    }

}
</style>
