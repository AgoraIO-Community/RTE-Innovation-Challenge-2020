<template>
  <div class="room">
    <CanvasDrawer @stream="streamHandler" ref="drawer" v-show="prac" />
    <div class="frow pd20v fwrap" id="videos">
      <div style="height:200px;width:300px;">
        <Player
          :uid="el.id"
          :stream="el.stream"
          v-for="el in streams"
          :key="el.id"
        />
      </div>
    </div>
    <button @click="leave">退出房间</button>
  </div>
</template>

<script>
// @ is an alias to /src
import Vue from "vue";
import { mapState, mapActions, mapGetters } from "vuex";
import { rtc, joinRoom } from "../rtc";
import { createRoom, getRooms } from "../api";
import CanvasDrawer from "@/components/CanvasDrawer.vue";
import Player from "@/components/Player.vue";
let localStream;
export default {
  name: "Home",
  components: { CanvasDrawer, Player },
  data() {
    return {
      rstatus: 0,
      roomId: "",
      streams: [],
      room: null,
      members: [],
      prac: 0,
    };
  },
  created() {
    const id = this.$route.params.id;
    if (!id) {
      return this.$router.replace("/");
    }
    this.room = this.rooms.find((el) => el.id === id);
    if (!this.room) {
      return this.$router.replace("/");
    }
    this.init();
    joinRoom(this.room.id, this.uid, this.isMine ? 0 : 1).then((e) => {
      if (e) {
        this.rstatus = 1;
        const { client } = rtc;
        if (client) {
          console.log("publish stream");
          client.setClientRole("audience");
          this.publish();
        }
      } else {
        this.rstatus = 0;
      }
    });
  },
  beforeDestroy() {},
  methods: {
    ...mapActions(["joinRoom"]),
    init() {
      const client = rtc.client;
      client.on("stream-added", (evt) => {
        let { stream } = evt;
        console.log("[agora] [stream-added] stream-added", stream.getId());
        client.subscribe(stream);
      });

      client.on("stream-subscribed", (evt) => {
        let { stream } = evt;
        console.log("[agora] [stream-subscribed] stream-added", stream.getId());
        const id = stream.getId();
        if (!this.streams.find((el) => el.id === id)) {
          this.streams.push({
            id,
            stream,
          });
        }
        console.log(this.streams);
      });

      client.on("stream-removed", (evt) => {
        let { stream } = evt;
        console.log("[agora] [stream-removed] stream-removed", stream.getId());
        const id = stream.getId();
        this.streams = this.streams.filter((el) => el.id !== id);
      });

      client.on("peer-online", (evt) => {
        this.members.push(evt.uid);
        console.warn(`Peer ${evt.uid} is online`);
      });

      client.on("peer-leave", (evt) => {
        console.log(`Peer ${evt.uid} already leave`);
        const i = this.members.findIndex((el) => el === evt.uid);
        if (i > -1) {
          this.members.splice(i, 1);
        }
        this.streams = this.streams.filter((el) => el.getId() !== evt.uid);
      });
    },
    leave() {
      rtc.client.unpublish(localStream);
      this.streams.forEach((el) => {
        rtc.client.unsubscribe(el.stream);
      });
      rtc.client.leave();
      this.$router.replace("/");
    },
    streamHandler(e) {
      localStream = e;
      console.log("----------local-----------------");
      console.log(e);
      console.log("----------local-----------------");
      this.publish();
    },
    publish() {
      if (this.published || this.rstatus !== 1) {
        return;
      }
      this.published = true;
      console.log("publish");
      rtc.client.publish(localStream, (err) => {
        console.log(err);
      });
    },
  },
  computed: {
    ...mapState(["uid", "rooms"]),
  },
};
</script>

<style scoped>
.fcol.rev {
  flex-direction: column-reverse;
}
</style>
