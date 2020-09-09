<template>
  <div class="touch-fish-area">
    <div class="input-container" v-show="type===null">
      <div class="touch-fish-area-container">
        <span class="touch-fish-area-tag">fish@<・)))><<</span>
        <div
          class="touch-fish-area-input"
          ref="touchFishAreaInput"
          contenteditable
          @keydown.enter.prevent="handleEnter"
          @paste.prevent="handlePaste"
        ></div>
      </div>
    </div>

    <video v-show="type==='video'" ref="videoElement" autoplay controls></video>
    <webview
      v-show="type==='web'"
      :src="webviewURL"
      ref="webviewElement"
      enableremotemodule="false"
    ></webview>
  </div>
</template>
<script lang="ts">
import { Component, Vue, Prop, Watch, Ref } from "vue-property-decorator";
import path from "path";
import { ipcRenderer, WebviewTag } from "electron";
import flv from "flv.js";
import Mousetrap from "mousetrap";

import webviewCodeExecute from "@/config/webviewCodeExecute";

let flvPlayer!: flv.Player | null;

@Component
export default class TouchFishArea extends Vue {
  @Prop({ default: "" }) private dropFilePath!: string;

  @Ref() private videoElement!: HTMLVideoElement;

  @Ref() private webviewElement!: WebviewTag;

  @Ref() private touchFishAreaInput!: HTMLDivElement;

  private type: "video" | "web" | null = null;

  private inputValue: string = "";

  private webviewURL: string = "";

  private mounted(): void {
    flvPlayer = null;
    Mousetrap.bind(["ctrl+w"], () => {
      this.reset();
    });
  }

  private handlePaste(e: any): void {
    const text = e.clipboardData.getData("text/plain");
    document.execCommand("insertText", false, text);
  }

  private handleEnter(): void {
    const inputValue: string = this.touchFishAreaInput.innerHTML;
    if (/(http)(s)?(:\/\/)/.test(inputValue)) {
      this.webviewURL = inputValue;
      this.type = "web";
      flvPlayer = null;
      this.videoElement.src = "";
      (this.webviewElement as WebviewTag).addEventListener("dom-ready", () => {
        // if (process.env.NODE_ENV === "development") {
        //   (this.webviewElement as WebviewTag).openDevTools();
        // }
        const index = webviewCodeExecute.findIndex((e) =>
          e.reg.test((this.webviewElement as WebviewTag).getURL())
        );
        if (index !== -1) {
          (this.webviewElement as WebviewTag).executeJavaScript(
            webviewCodeExecute[index].code,
            true
          );
        }
      });
    }
    if (inputValue === "Agora") {
      alert("We Are The Best！！！");
    }
    this.touchFishAreaInput.innerHTML = "";
  }

  @Watch("dropFilePath")
  private handleDropFilePathChange(): void {
    if (
      path.extname(this.dropFilePath) === ".flv" ||
      path.extname(this.dropFilePath) === ".mp4"
    ) {
      if (flv.isSupported()) {
        this.type = "video";
        this.touchFishAreaInput.innerHTML = "";
        this.webviewURL = "";
        flvPlayer = flv.createPlayer({
          type: path.extname(this.dropFilePath).substring(1),
          url: "file://" + this.dropFilePath
        });
        flvPlayer.attachMediaElement(this.videoElement as HTMLVideoElement);
        flvPlayer.load();
        flvPlayer.play();
        flvPlayer.on("error", console.log);
      }
    } else {
      alert("暂不支持该文件类型");
    }
  }

  private reset(): void {
    this.type = null;
    this.touchFishAreaInput.innerHTML = "";
    this.webviewURL = "";
    flvPlayer = null;
    this.videoElement.src = "";
  }
}
</script>
<style lang='scss' scoped>
.touch-fish-area {
  width: 100%;
  height: 100%;
  background: #000;
  .input-container {
    position: relative;
    width: 100%;
    height: 100%;
    .touch-fish-area-container {
      display: block;
      width: 100%;
      height: 100%;
      overflow: auto;
      box-sizing: border-box;
      padding: 8px;
      outline: none;
      border: 0;
      margin: 0;
      background: transparent;

      &::-webkit-scrollbar {
        width: 0;
        overflow: hidden;
      }

      .touch-fish-area-tag {
        position: absolute;
        top: 8px;
        left: 8px;
        font-size: 12px;
        line-height: 18px;
        letter-spacing: 1.5px;
        color: #fff;
        font-weight: bolder;
        pointer-events: none;
      }

      .touch-fish-area-input {
        display: block;
        outline: none;
        word-wrap: break-word;
        white-space: normal;
        box-sizing: border-box;
        width: 100%;
        height: 100%;
        font-size: 12px;
        line-height: 18px;
        // letter-spacing: 1.5px;
        color: #0ae826;
        font-weight: bolder;
        text-indent: 115px;
      }
    }
  }

  video {
    width: 100%;
    height: 100%;
  }

  webview {
    width: 100%;
    height: 100%;
  }
}
</style>