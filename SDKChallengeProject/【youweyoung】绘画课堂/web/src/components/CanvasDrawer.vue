<template>
  <div class="ctx-wrapper">
    <div class="tools">
      <div class="frow bd1 cnty">
        <div
          class="color-block"
          :class="{ selected: c === color }"
          v-for="c in colors"
          :key="c"
          @click="color = c"
          :style="{ backgroundColor: c }"
        ></div>

        <button @click="moreColor">更多</button>
      </div>
      <div class="frow">
        <button @click="reset">清空</button>
        <!-- <button @click="backGo">退回</button> -->
        <!-- <button @click="tempSave">暂存</button> -->
        <button @click="changeBg">更换背景色</button>
      </div>
      <div class="frow">
        <div
          class="width-block"
          @click="linewidth = lw"
          :class="{ selected: linewidth === lw }"
          :style="{ height: lw + 'px' }"
          v-for="lw in linewidths"
          :key="'lw-' + lw"
        ></div>
      </div>
    </div>
    <canvas
      ref="board"
      @touchstart.prevent="onStartT"
      @touchend.prevent="onEndT"
      @touchmove.prevent="onMoveT"
      @mousemove="onMove"
      @mousedown.left="onStart"
      @mouseup.left="onEnd"
      id="cvs-board"
      :width="width"
      :height="height"
    ></canvas>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Prop, Watch } from 'vue-property-decorator';
import { wrapCanvasStream } from "../rtc";
function realPosition(e: HTMLElement) {
  let p: HTMLElement | null = e.parentElement;
  let x = e.offsetLeft;
  let y = e.offsetTop;
  while (p) {
    x += p.offsetLeft;
    y += p.offsetTop;
    p = p.parentElement;
  }
  return { x, y };
}
const log = console.log
let ctx: CanvasRenderingContext2D | null = null;
let cvs: any =  null;
const points: Array<[number, number]> = [];
const temps: string[] = [];
@Component({ name: 'canvas-board' })
export default class extends Vue {
  private drawing = false;
  private color = '#000000';
  private linewidth = this.linewidths[0];
  private tempImages: string[] = temps;
  private realWidth = 600;
  private realHeight = 400;
  private realLeft = 0;
  private realTop = 0;
  @Prop({ default: 800 }) private width!: number;
  @Prop({ default: 450 }) private height!: number;
  private hasData = false;
  // private startPoint: { x: number; y: number } | null = null;
  private get linewidths() {
    return [1, 2, 3, 5, 8, 12, 16, 20, 30];
  }
  private get colors() {
    return ['#000000', '#FF0000', '#00FF00', '#FFFFFF', '#0000FF'];
  }
  private size = { width: 800, height: 450 };
  private get widthRatio() {
    return this.width / this.realWidth;
  }
  private get heightRatio() {
    return this.height / this.realHeight;
  }
  private mounted() {
    cvs = this.$refs.board as any;
    this.$nextTick(async () => {
      if (cvs) {
        this.updateRealSize();
        ctx = cvs.getContext('2d');
        this.reset();
        this.$emit('stream',await wrapCanvasStream(cvs.captureStream(20)))
      }
    });
  }
  private updateRealSize() {
    if (cvs) {
      this.realWidth = cvs.clientWidth;
      this.realHeight = cvs.clientHeight;
      const { x, y } = realPosition(cvs);
      this.realLeft = x;
      this.realTop = y;
    }
  }
  private reset() {
    this.color = '#000000';
    this.hasData = false;
    if (ctx) {
      ctx.fillStyle = '#FFFFFF';
      ctx.fillRect(0, 0, this.width, this.height);
    }
  }
  private activate(width: number, x: number, y: number) {
    if (!this.color || !ctx) {
      return;
    }
    this.drawing = true;
    ctx.save();
    ctx.strokeStyle = this.color;
    ctx.lineWidth = width;
    // this.startPoint = { x, y };
    ctx.beginPath();
    ctx.lineJoin = 'round';
    ctx.lineCap = 'round';
    // console.log(x);
    // console.log(y);
    // console.log(this.widthRatio);
    // console.log(this.heightRatio);
    // console.log(this.realWidth);
    // console.log(this.realHeight);
    // points.push([x, y]);
    ctx.moveTo(
      Math.round(x * this.widthRatio),
      Math.round(y * this.heightRatio)
    );
  }
  private updateDraw(x: number, y: number) {
    if (!this.color || !ctx || !this.drawing) {
      return;
    }
    ctx.lineTo(
      Math.round(x * this.widthRatio),
      Math.round(y * this.heightRatio)
    );
    ctx.stroke();
  }
  private finishDraw() {
    if (!ctx || !this.drawing) {
      return;
    }
    this.drawing = false;
    this.hasData = true;
    // this.startPoint = null;
    // console.log(points);
    ctx.restore();
    this.$emit('change', this.getImageSrc());
  }
  private onStart(e: MouseEvent) {
    this.activate(this.linewidth, e.offsetX, e.offsetY);
  }
  private onMove(e: MouseEvent) {
    this.updateDraw(e.offsetX, e.offsetY);
  }
  private onEnd(e: MouseEvent) {
    this.finishDraw();
  }
  private onStartT(e: TouchEvent) {
    const t = e.touches[0];
    if (t) {
      // console.log(t);
      // console.log(e);
      const x = t.pageX - this.realLeft + t.radiusX / 2;
      const y = t.pageY - this.realTop + t.radiusY / 2;
      this.activate(this.linewidth, x, y);
      // console.log({ x, y });
    }
  }
  private onMoveT(e: TouchEvent) {
    const t = e.touches[0];
    if (t) {
      const x = t.pageX - this.realLeft + t.radiusX / 2;
      const y = t.pageY - this.realTop + t.radiusY / 2;
      this.updateDraw(x, y);
    }
  }
  private onEndT(e: TouchEvent) {
    this.finishDraw();
  }
  private getImageSrc() {
    if (ctx && cvs) {
      return cvs.toDataURL();
    }
  }
  private tempSave() {
    if (!this.hasData) {
      return;
    }
    const t = this.getImageSrc();
    if (t) {
      this.tempImages.push(t);
      if (temps.length > 6) {
        this.tempImages.shift();
      }
      console.log(this.tempImages)
    }
  }
  private beforeDestroy() {
    cvs = null;
    ctx = null;
  }
  private backGo() {
    //
  }
  private async moreColor() {
    const color = await this.chooseColor();
    if (color) {
      this.color = color;
      this.colors.push(color)
    }
  }
  private changeBg() {
    if (this.hasData && !confirm('已有数据，确定重置吗')) {
      return;
    }
    this.chooseColor().then(e => {
      if (e && ctx) {
        ctx.save();
        ctx.fillStyle = e;
        ctx.fillRect(0, 0, this.width, this.height);
        ctx.restore();
      }
    });
  }
  private chooseColor(): Promise<string | undefined> {
    const i = document.createElement('input');
    i.type = 'color';
    return new Promise(res => {
      i.onchange = (e: any) => res(e.target?.value);
      i.click();
    });
  }
}
</script>

<style scoped>
.ctx-wrapper {
  padding: 10px;
  background-color: grey;
  margin: 10px 0;
}
#cvs-board {
  width: 100%;
  height: 100%;
  min-height: 200px;
  background-color: white;
}
.tools {
  padding: 0 0 10px 0;
}
.color-block {
  border: 2px solid white;
  width: 2rem;
  height: 2rem;
  flex-grow: 0;
  flex-shrink: 0;
  margin: 4px;
}
.width-block {
  border: 1px solid white;
  width: 2rem;
  margin: 4px;
}
.width-block.selected {
  border: 2px solid lightcoral;
}
.color-block.selected {
  border: 2px solid lightcoral;
}
.temps {
  margin-top: 10px;
  padding: 0.5rem;
  height: 6rem;
  overflow-y: hidden;
  border: 1px solid white;
  overflow-x: auto;
}
.temp-save {
  width: 5rem;
  height: 5rem;
  margin-right: 10px;
  background-color: white;
}
</style>
