<template>
  <div class="hello">
    <div v-if="ready">准备就绪</div>
    <div v-else-if="fail">创建失败，请重新再试</div>
    <div v-else>创建中...</div>
    <div style="width: 200px; margin: 20px 0">
      <el-button style="width: 100%" round type="primary" @click="invite"
        >邀请</el-button
      >
    </div>
    <h4>用户列表</h4>
    <div v-for="uid in members" :key="'u-' + uid" class="member">{{ uid }}</div>
    <div
      class="agora-video"
      :key="index"
      v-for="(remoteStream, index) in remoteStreams"
    >
      <Player :stream="remoteStream" :domId="remoteStream.getId()"></Player>
    </div>
  </div>
</template>

<script>
import Player from "./Player";
import { log } from "../utils/utils";
import { rtc, createStream } from "../share";

export default {
  components: {
    Player,
  },
  data() {
    return {
      ready: 0,
      fail: 0,
      localStream: null,
      remoteStreams: [],
      members: [],
    };
  },

  methods: {
    invite() {
      alert("邀请码已经复制到粘贴板");
    },
    async createStream() {
      const stream = await createStream();
      if (stream) {
        this.localStream = stream;
        if (await this.pushStream()) {
          this.ready = true;
        }
      }
    },
    pushStream() {
      return new Promise((res) => {
        let tok = setTimeout(() => {
          res(true);
        }, 1000);
        this.client.publish(this.localStream, (err) => {
          clearTimeout(tok);
          console.log(err);
          this.fail = true;
          res(false);
        });
      });
    },
    initEvents() {
      const client = this.client;
      client.on("stream-added", (evt) => {
        let { stream } = evt;
        log("[agora] [stream-added] stream-added", stream.getId());
        client.subscribe(stream);
      });

      client.on("stream-subscribed", (evt) => {
        let { stream } = evt;
        log("[agora] [stream-subscribed] stream-added", stream.getId());
        if (!this.remoteStreams.find((it) => it.getId() === stream.getId())) {
          this.remoteStreams.push(stream);
        }
      });

      client.on("stream-removed", (evt) => {
        let { stream } = evt;
        log("[agora] [stream-removed] stream-removed", stream.getId());
        this.remoteStreams = this.remoteStreams.filter(
          (it) => it.getId() !== stream.getId()
        );
      });

      client.on("peer-online", (evt) => {
        this.members.push(evt.uid);
        this.$message(`Peer ${evt.uid} is online`);
      });

      client.on("peer-leave", (evt) => {
        this.$message(`Peer ${evt.uid} already leave`);
        const i = this.members.findIndex((el) => el === evt.uid);
        if (i > -1) {
          this.members.splice(i, 1);
        }
        this.remoteStreams = this.remoteStreams.filter(
          (it) => it.getId() !== evt.uid
        );
      });
    },
  },
  created() {
    this.client = rtc.client;
    this.initEvents();
    this.createStream();
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.hello {
  display: flex;
  flex-direction: column;
  padding: 20px;
}
.member {
  height: 20px;
  background-origin: 1px solid lightgrey;
}
</style>
