<template>
  <div class="about-us">
    <div class="header">
      <div class="content">
        <div class="tab base">
          <span @click="intoHome">首页</span>
          <span class="active">关于G游</span>
        </div>
      </div>
    </div>
    <div class="body">
      <div class="content">
        <div class="title"><img :src="titleIcon" alt=""></div>
        <div class="group" v-for="(item,index) in list" :key="index">
          <p class="large">{{item.large}}</p>
          <p class="small">{{item.small}}</p>
        </div>
         <div class="group" >
          <p class="large">联系我们</p>
          <p class="small"><span>QQ交流群:313055838</span><span>投稿邮箱:374949369@qq.com</span></p>
          <p class="small"><span>商务合作:2206749767@qq.com</span><span>简历投递:374949369@qq.com</span></p>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import titleIcon from '@/assets/about-title.png'

export default {
  data() {
    return {
      titleIcon: titleIcon,
      list: [
        {
          large: '联机游戏玩家集结地',
          small:
            'G游是一款由优思佳团队出品的一款全平台开黑端游语音产品。力求解决国内单机玩家数量分散，玩家开黑组队等一系列问题。'
        },
        {
          large: '始于游戏,忠于玩家',
          small:
            'G游的员工都热爱游戏，并将这份热情投入到工作之中。如果你有什么意见,欢迎通过【意见反馈】将你的意见反馈给我们，G游会聆听每一份玩家的意见。'
        },
        {
          large: '共同建设游戏文化',
          small:
            'G游致力于传播更好的中国游戏文化，如果你也热爱游戏并喜欢制作游戏专题视频，欢迎对我们进行投稿，被采纳的视频将获得100-1000元不等的现金奖励。'
        },
        {
          large: '加入G游',
          small:
            'G游还很年轻,我们长期招聘各种技术和运营人员，请将个人简历发送至简历投递邮箱。不论你是年轻的大学生还是混迹江湖都老司机，不论你想全职还是兼职，G游都欢迎你的加入！现招聘：web前端工程师、iOS工程师、安卓工程师、后台工程师、UI设计师、运营推广、视频剪辑、视频创意'
        }
      ]
    }
  },
  methods: {
    intoHome() {
      this.$router.push('/')
    }
  }
}
</script>
