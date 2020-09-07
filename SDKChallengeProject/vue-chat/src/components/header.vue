<template>
    <div class="header">
        <div class="left" @click="leftHandleEvent" v-show="config.left">
            <img src="../images/1.png" v-if="config.left === '1'"/>
            <img src="../images/2.png" v-if="config.left === '2'"/>
            <img src="../images/3.png" v-if="config.left === '3'"/>
            <img src="../images/4.png" v-if="config.left === '4'"/>
            <img src="../images/5.png" v-if="config.left === '5'"/>
            <img src="../images/6.png" v-if="config.left === '6'"/>
            <img src="../images/7.png" v-if="config.left === '7'"/>
            <img src="../images/8.png" v-if="config.left === '8'"/>
            <img src="../images/9.png" v-if="config.left === '9'"/>
            <img src="../images/10.png" v-if="config.left === '10'"/>
            <img src="../images/connect.png" v-show="config.left === 'conn'"/>
        </div>
        <div class="center">
            <slot name="title"></slot>
        </div>
        <div class="right" @click="rightHandleEvent" v-show="config.right">{{config.right}}</div>
        <div class="wait" v-show="loading">
            <div class="loading">
                <div class="main2"></div>
            </div>
        </div>
    </div>
</template>
<script>
export default {
  props: ['Img', 'config'],
  data () {
    return {
      loading: false
    };
  },
  components: {},
  methods: {
    leftHandleEvent () {
      this.$emit('headerLeftEvent');
    },
    rightHandleEvent () {
      this.$emit('headerRightEvent');
    }
  }
};
</script>

<style lang="less" scoped>
    @import '../common/css/styleFunc';

    .header {
        font-family: "Microsoft YaHei";
        width: 100%;
        height: 50px;
        top: 0;
        left: 0;
        line-height: 50px;
        background: #499eff;
        color: #fff;
        position: fixed;
        z-index: 10;
        .left {
            width: 15%;
            height: 50px;
            float: left;
            text-align: center;
            line-height: 50px;
            position: relative;
            img {
                width: 60%;
                height: 60%;
                border-radius: 10px;
                .absoluteCenter;
            }
        }
        .center {
            width: 70%;
            height: 50px;
            float: left;
        }
        .right {
            width: 15%;
            height: 50px;
            float: left;
            text-align: center;
            line-height: 50px;
        }
        .wait {
            width: 100%;
            height: 100%;
            position: fixed;
            top: 0;
            left: 0;
            z-index: 1000;
            .loading {
                width: 100%;
                height: 100%;
                position: relative;
                .main {
                    width: 40px;
                    height: 40px;
                    .absoluteCenter;
                    display: inline-block;
                    padding: 0;
                    border-radius: 100%;
                    border: 5px solid;
                    border-top-color: #128d91;
                    border-bottom-color: #fff;
                    border-left-color: #fff;
                    border-right-color: #128d91;
                    -webkit-animation: loader4 1s ease-in-out infinite;
                    animation: waitAn 1s ease-in-out infinite;
                }
                .main2 {
                    .absoluteCenter;
                    overflow: hidden;
                    display: inline-block;
                    padding: 0;
                    width: 1em;
                    height: 1em;
                    border-radius: 50%;
                    font-size: 35px;
                    text-indent: -9999em;
                    -webkit-animation: load 1.7s infinite ease;
                    animation: load 1.7s infinite ease;
                    z-index: 9999;
                }
            }
        }
    }

    @keyframes waitAn {
        from {
            transform: rotate(0deg);
        }
        to {
            transform: rotate(360deg);
        }
    }

    @keyframes load {
        0% {
            -webkit-transform: rotate(0deg);
            transform: rotate(0deg);
            box-shadow: -0.11em -0.83em 0 -0.4em #128d91, -0.11em -0.83em 0 -0.42em #128d91, -0.11em -0.83em 0 -0.44em #128d91, -0.11em -0.83em 0 -0.46em #128d91, -0.11em -0.83em 0 -0.477em #128d91;
        }
        5%,
        95% {
            box-shadow: -0.11em -0.83em 0 -0.4em #128d91, -0.11em -0.83em 0 -0.42em #128d91, -0.11em -0.83em 0 -0.44em #128d91, -0.11em -0.83em 0 -0.46em #128d91, -0.11em -0.83em 0 -0.477em #128d91;
        }
        30% {
            box-shadow: -0.11em -0.83em 0 -0.4em #128d91, -0.51em -0.66em 0 -0.42em #128d91, -0.75em -0.36em 0 -0.44em #128d91, -0.83em -0.03em 0 -0.46em #128d91, -0.81em 0.21em 0 -0.477em #128d91;
        }
        55% {
            box-shadow: -0.11em -0.83em 0 -0.4em #128d91, -0.29em -0.78em 0 -0.42em #128d91, -0.43em -0.72em 0 -0.44em #128d91, -0.52em -0.65em 0 -0.46em #128d91, -0.57em -0.61em 0 -0.477em #128d91;
        }
        100% {
            -webkit-transform: rotate(360deg);
            transform: rotate(360deg);
            box-shadow: -0.11em -0.83em 0 -0.4em #128d91, -0.11em -0.83em 0 -0.42em #128d91, -0.11em -0.83em 0 -0.44em #128d91, -0.11em -0.83em 0 -0.46em #128d91, -0.11em -0.83em 0 -0.477em #128d91;
        }
    }
</style>
