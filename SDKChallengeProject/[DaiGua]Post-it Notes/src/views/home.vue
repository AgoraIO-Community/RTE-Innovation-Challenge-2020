<!-- 登陆后的主页 -->
<template>
  <div class="home col-direction">

    <p class="logo pramirytext blod">便 利 贴</p>
    <p class="logoen pramirytext blod">Post-it Notes</p>
    <div class="row-direction content col-center">
      <div class="cursor center col-direction" @click="openJoinClass">
        <img src="@/assets/home1.png" style="height:200px">
        <h4 class="blod">加 入 课 堂</h4>
      </div>
      <div v-if="$route.query.role==='2'" class="cursor center col-direction" @click="openHistoryClass">
        <img src="@/assets/home2.png">
        <h4 class="blod ">历 史 课 堂</h4>
      </div>
    </div>
    <el-dialog title="进入课堂" :visible.sync="dialogFormVisible">
      <el-form class="row-direction col-center">
        <span class="blod">课堂号：</span>
        <el-input v-model="room" autocomplete="off" />

      </el-form>
      <el-form v-if="$route.query.role==='1'" class="row-direction col-center">
        <span class="blod">课堂名称：</span>
        <el-input v-model="roomname" autocomplete="off" />

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="joinClass">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { joinClass } from '@/api/local.js'
export default {
  data() {
    return {
      dialogFormVisible: false,
      room: '',
      roomname: null
    }
  },
  // 方法集合
  methods: {
    openJoinClass() {
      this.dialogFormVisible = true
    },
    // 确定加入房间
    joinClass() {
      this.dialogFormVisible = false
      let time = null
      if (this.$route.query.role === '1') {
        time = new Date().getTime()
      }
      joinClass(this.room, this.roomname, this.$route.query.uid, time).then(
        res => {
          this.$router.push({ path: '/classroom', query: { uid: this.$route.query.uid, room: this.room, role: this.$route.query.role, rname: res.class.rname }})
        }
      )
    },
    // 历史课堂
    openHistoryClass() {
      this.$router.push({ path: '/historyClass', query: { uid: this.$route.query.uid, role: this.$route.query.role }})
    }

  }
}
</script>
<style lang='scss' scoped>
.home{
    height:100%;
}
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
.content{
    height: calc(100% - 100px);
    display: flex;
    justify-content: space-evenly;

}
.el-form{
    width: fit-content;
    margin:auto;
}
/deep/ .el-form-item__content{
    margin:0 !important;
}
.el-input{
        margin:7px;
        height:44px;
        width: 500px;
    }

     /deep/ .el-input__inner{
      height:44px;
        width: 500px;
        color: #030303;
    }
</style>
