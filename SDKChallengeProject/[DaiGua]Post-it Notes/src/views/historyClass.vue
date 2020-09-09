<!-- 历史课堂 -->
<template>
  <div class="col-direction">
    <div class="row-direction col-center">
      <i class="el-icon-back" style="font-size:30px;margin-left:40px;" @click="back" />
      <div class="col-direction">
        <p class="logo pramirytext blod">便 利 贴</p>
        <p class="logoen pramirytext blod">Post-it Notes</p>
      </div>
    </div>

    <div class="history-class">
      <el-card v-for="(cla,index) in classList" :key="index" :body-style="{ padding: '0px' }">
        <img :src="cla.cloudpng" class="image" @click="openClassRecord(cla)">
        <div style="padding: 14px;" class="col-direction content">
          <span>课堂号 : {{ cla.rid }}</span>
          <!-- <span>课堂名称 : {{cla.rname}}</span> -->
          <span>课堂难词 : {{ cla.queryword }}</span>
          <span>开始时间 ：{{ cla.timeForm }}</span>
          <span>教师名 ： {{ cla.teacher }}</span>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
import { formatDate } from '@/utils/time.js'
import { historyClass } from '@/api/local.js'
export default {
  data() {
    return {
      classList: []
    }
  },
  mounted() {
    this.historyClassHandle()
  },
  // 方法集合
  methods: {
    openClassRecord(room) {
      this.$router.replace({ path: '/review', query: { uid: this.$route.query.uid, roomInfo: JSON.stringify(room), role: this.$route.query.role }})
    },
    back() {
      this.$router.replace({ path: '/home', query: { uid: this.$route.query.uid, role: this.$route.query.role }})
    },
    // 查看历史课堂
    historyClassHandle() {
      historyClass(this.$route.query.uid).then(
        res => {
          this.classList = res.data
          this.classList.forEach(note => {
            note.timeForm = formatDate(+note.time)
          })
        }
      )
    }
  }
}
</script>
<style lang='scss' scoped>
.logo{
    text-align: start;
    font-size: 20px;
    margin:20px 80px;
}
.logoen{
    text-align: start;
    font-size: 16px;
    margin:-10px 80px;
}
.history-class{
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  margin:30px;
  .el-card{
    margin:10px;
    img{
      width:320px;
      height: 180px;
    }
  }
  .content{
    span{
      margin:5px;
      text-align: start;
    }
  }
}
</style>
