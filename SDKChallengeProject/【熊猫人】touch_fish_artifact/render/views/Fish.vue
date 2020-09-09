<template>
  <div
    class="fishPage"
    @dragleave.prevent
    @dragstart.prevent
    @dragover.prevent
    @drop.prevent="handleDrop"
  >
    <div
      v-for="(item,index) in videoContainerList"
      :key="item.id"
      class="video-container"
      :data-type="item.type"
      :id="item.id"
    >
      <template v-if="item.type === 'fish'">
        <TouchFishArea :dropFilePath="filePath" />
      </template>
      <template v-if="item.type === 'blank' && index !== 0">
        <video
          class="default-video"
          :src="item.videoSrc | formatFilePath"
          autoplay
          muted
          playsinline
          preload
          @ended="handleEnded"
        ></video>
      </template>
    </div>
  </div>
</template>

<script lang='ts'>
import { Component, Vue, Ref, Watch } from "vue-property-decorator";
import TouchFishArea from "@/components/TouchFishArea.vue";
import path from "path";
import os from "os";
import URL from "url";
import AgoraRtcEngine from "agora-electron-sdk";

import {
  APPID,
  REPLACE_RENDER_INTERVAL,
  REPLACE_RENDER_FLOAT_INTERVAL,
  VIDEO_INDEX,
  MAX_RENDER_COUNT,
} from "@/config/config";
import videoList from "@/config/videoList";
import videoEncoderConf from "@/config/videoEncoderConf";
import { ipcRenderer, WebviewTag, app } from "electron";

import Mousetrap from "mousetrap";

@Component({
  components: {
    TouchFishArea
  },
  filters: {
    formatFilePath: (filePath: string) => {
      return typeof filePath === "string"
        ? `file://${path.join(__static, filePath)}`
        : "";
    }
  }
})
export default class Fish extends Vue {
  private countTimer!: NodeJS.Timeout;

  private videoContainerList: VideoContainerInfo[] = [];

  private userList: number[] = [];

  private filePath: string = "";

  private startTimeStamp: number = 0;

  // 使用loop会有闪烁问题
  private handleEnded(e: Event): void {
    (e.target as HTMLVideoElement).currentTime = 0.5;
    (e.target as HTMLVideoElement).play();
  }

  private getVideoSrc(): string {
    const exclude: number[] = [];
    videoList.forEach((e, i) => {
      const tmp = this.videoContainerList.find((item) => {
        if (typeof item.videoSrc === "string") {
          return item.videoSrc === e;
        } else {
          return false;
        }
      });
      if (tmp !== undefined) {
        exclude.push(i);
      }
    });
    const index = this.getRandomNumber(0, videoList.length, exclude);
    const videoPath = videoList[index];
    return videoPath;
  }

  private createStreamRender(uid: number, replaceIndex?: number): void {
    const index: number =
      typeof replaceIndex === "number"
        ? replaceIndex
        : this.videoContainerList.findIndex((e) => e.type === "blank");
    if (index !== -1) {
      this.videoContainerList.splice(index, 1, {
        id: this.getRandomId(),
        uid,
        type: "stream",
        timeOut: (this.videoContainerList[index].timeOut as number) + this.getRandomTime()
      });
      this.$nextTick(() => {
        global.rtcEngine.setupRemoteVideo(
          uid,
          document.getElementById(this.videoContainerList[index].id),
          this.$store.state.channelName
        );
        global.rtcEngine.muteRemoteVideoStream(uid, false);
        this.userList.splice(
          this.userList.findIndex((e) => e === uid),
          1
        );
      });
    }
  }

  private detroyStreamRender(uid: number): void {
    const index: number = this.videoContainerList.findIndex(
      (e) => e.uid === uid
    );
    if (index !== -1) {
      global.rtcEngine.muteRemoteVideoStream(uid, true);
      global.rtcEngine.setupRemoteVideo(
        uid,
        false,
        this.$store.state.channelName
      );
      this.videoContainerList.splice(index, 1, {
        id: this.videoContainerList[index].id,
        uid: null,
        type: "blank",
        videoSrc: this.getVideoSrc()
      });
    }
  }

  private created(): void {
    this.startTimeStamp = parseInt((new Date().getTime() / 1000).toString(), 10);
    for (let i: number = 0; i < MAX_RENDER_COUNT; i++) {
      const data: VideoContainerInfo = {
        id: this.getRandomId(),
        uid: null,
        type: i === 0 ? "self" : "blank",
        videoSrc: this.getVideoSrc(),
        timeOut: this.startTimeStamp + this.getRandomTime()
      };
      if (i === VIDEO_INDEX) {
        data.type = "fish";
      }
      this.videoContainerList.push(data);
    }
  }

  private getRandomTime(): number {
    return this.getRandomNumber(
      REPLACE_RENDER_INTERVAL - REPLACE_RENDER_FLOAT_INTERVAL,
      REPLACE_RENDER_INTERVAL + REPLACE_RENDER_FLOAT_INTERVAL + 1
    );
  }

  private mounted(): void {
    this.initAgora();

    this.createTimeOut();

    Mousetrap.bind(["ctrl+shift+w"], () => {
      console.log("ctrl+shift+w");
      global.rtcEngine.release();
      global.rtcEngine = null;
      this.$router.replace("/");
    });
  }

  private createTimeOut(): void {
    clearInterval(this.countTimer);
    this.countTimer = setInterval(() => {
      const nowTimeStamp = parseInt((new Date().getTime() / 1000).toString(), 10);
      this.videoContainerList.forEach((item, index) => {
        if (typeof item.timeOut === "number" && nowTimeStamp >= item.timeOut) {
          this.randomReplaceStream(index);
        }
      });
    }, 1000);
  }

  private randomReplaceStream(index: number): void {
    if (this.videoContainerList[index].type === "stream") {
      if (this.userList.length > 0) {
        this.detroyStreamRender(this.videoContainerList[index].uid as number);
        this.createStreamRender(
          this.userList[this.getRandomNumber(0, this.userList.length - 1)],
          index
        );
      } else {
        return;
      }
    }

    if (this.videoContainerList[index].type === "blank") {
      const tmp = this.videoContainerList[index];
      tmp.videoSrc = this.getVideoSrc();
      tmp.timeOut = (tmp.timeOut as number) + this.getRandomTime();
      this.$set(this.videoContainerList, index, tmp);
    }
  }

  private getRandomNumber(
    start: number = 1,
    end: number = MAX_RENDER_COUNT,
    exclude?: number[]
  ): number {
    const min: number = Math.ceil(start);
    const max: number = Math.floor(end - 1);
    const num: number = Math.floor(Math.random() * (max - min + 1)) + min;
    if (Array.isArray(exclude) && exclude.indexOf(num) !== -1) {
      return this.getRandomNumber(...arguments);
    } else {
      return num;
    }
  }

  // 解析并播放本地视频
  private handleDrop(e: DragEvent): void {
    // 获取文件列表
    const files = (e.dataTransfer as DataTransfer).files;

    if (files && files.length > 0) {
      // 获取文件路径
      this.filePath = files[0].path;
    }
  }

  // 初始化声网SDK
  private async initAgora(): Promise<any> {
    const sdkLogPath = path.resolve(os.homedir(), "./agoramainsdk.log");
    if (global.rtcEngine) {
      // if rtc engine exists already, you must call release to free it first
      global.rtcEngine.release();
      global.rtcEngine = null;
    }
    const rtcEngine = new AgoraRtcEngine();
    rtcEngine.initialize(APPID);

    rtcEngine.disableAudio();

    rtcEngine.setVideoEncoderConfiguration(videoEncoderConf);

    rtcEngine.setClientRole(1);

    rtcEngine.enableVideo();

    rtcEngine.setVideoRenderFPS(30);

    rtcEngine.setupLocalVideo(
      document.getElementById(this.videoContainerList[0].id)
    );

    this.videoContainerList.splice(0, 1, {
      id: this.videoContainerList[0].id,
      uid: null,
      type: "self"
    });

    // prepare rtmp streaming state listener
    rtcEngine.on(
      "rtmpStreamingStateChanged",
      (url: string, state: any, code: any) => {
        console.log(`rtmpStreamingStateChanged ${url} ${state} ${code}`);
      }
    );

    rtcEngine.on("joinedChannel", (channel: string, uid: any, elapsed: any) => {
      console.log(`join channel success ${channel} ${uid} ${elapsed}`);
      // setup render area for local user
      rtcEngine.setupLocalVideo(
        document.getElementById(this.videoContainerList[0].id)
      );
    });

    rtcEngine.on("userJoined", (uid: number) => {
      // setup render area for joined user
      this.userList.push(uid);
      rtcEngine.setupViewContentMode(uid, 0);
      this.createStreamRender(uid);
    });

    rtcEngine.on("removeStream", (uid: number) => {
      console.log("user leave:", uid);
      if (this.userList.findIndex((e) => e === uid) !== -1) {
        this.userList.splice(
          this.userList.findIndex((e) => e === uid),
          1
        );
      }
      const index = this.videoContainerList.findIndex((e) => e.uid === uid);
      if (index !== -1) {
        this.detroyStreamRender(this.videoContainerList[index].uid as number);
        if (this.userList.length > 0) {
          this.createStreamRender(
            this.userList[this.getRandomNumber(0, this.userList.length - 1)],
            index
          );
        }
      }
    });

    rtcEngine.on("error", (err: any, msg: any) => {
      console.log(`error: code ${err} - ${msg}`);
      if (err === "101" || err === "-1501") {
        alert("不是有效的 App ID。请更换有效的 App ID 重新加入频道。");
      }
    });

    rtcEngine.on("localVideoStateChanged", (err: any, msg: any) => {
      console.log(err);
      if (err === 3 || err === 4) {
        alert("不是有效的 App ID。请更换有效的 App ID 重新加入频道。");
      }
    });

    // 加入频道
    rtcEngine.joinChannel(
      this.$store.state.token,
      this.$store.state.channelName,
      "",
      this.$store.state.uid
    );

    window.addEventListener("beforeunload", (e) => {
      rtcEngine.leaveChannel();
      rtcEngine.once("leaveChannel", () => {
        ipcRenderer.send("leave");
      });
      return false;
    });

    global.rtcEngine = rtcEngine;
  }

  private getRandomId(): string {
    return Math.random().toString(36).slice(-9);
  }
}
</script>

<style lang='scss'>
.fishPage {
  width: 100%;
  height: 100%;

  display: flex;
  justify-content: flex-start;
  align-items: flex-start;
  flex-wrap: wrap;

  overflow: hidden;

  .video-container {
    overflow: hidden;
    width: (100%/3);
    height: (100%/3);

    // canvas {
    //   max-width: 100%;
    //   max-height: 100%;
    // }

    .default-video {
      display: block;
      width: 100%;
      height: 100%;
      background: #000;
      object-fit: fill;
    }

    &[data-type="stream"] {
      background: #000;
    }
  }
}
</style>
