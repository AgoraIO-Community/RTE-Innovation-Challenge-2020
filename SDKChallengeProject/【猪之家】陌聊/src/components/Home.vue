<template>
  <div class="hello">
    <h3>您的在线私人会所</h3>
    <h5>一键创建临时房间或者输入邀请码加入房间</h5>
    <div style="width:200px;">
      <el-button
        style="width:100%"
        round
        type="primary"
        @click="createRoom"
        >创建</el-button
      >
    </div>
    <div style="width:200px;margin-top:20px">
      <el-button
        style="width:100%"
        round
        type="primary"
        @click="joinRoom"
        >加入</el-button
      >
    </div>
  </div>
</template>

<script>
import {  joinChannel } from "../share";
// rtc, genRoom,
export default {
  data() {
    return {
      
    };
  },
  props: {
    msg: String,
  },
  computed: {
    canJoin() {
      return !this.disableJoin;
    },
  },
  methods: {
    joinRoom() {},
    createRoom() {
      const uid = joinChannel("test");
      if (uid) {
        this.$emit("join");
      }
    },
    leaveEvent() {
      this.disableJoin = false;
      this.rtc
        .leaveChannel()
        .then(() => {
          this.$message({
            message: "Leave Success",
            type: "success",
          });
        })
        .catch((err) => {
          this.$message.error("Leave Failure");
          console.log("leave error", err);
        });
      this.localStream = null;
      this.remoteStreams = [];
    },
    judge(detail) {
      this.$notify({
        title: "Notice",
        message: `Please enter the ${detail}`,
        position: "top-left",
        type: "warning",
      });
    },
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.hello {
  padding: 32px;
}
</style>
