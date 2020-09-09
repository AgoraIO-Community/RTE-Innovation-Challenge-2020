
<template>
  <div ref="wraper" class="wraper">
    <div class="save-button ">
      <div class="col-direction cursor" style="z-index:3" @click="noteUpdateHandle">
        <i class="el-icon-upload" />
        保存
      </div>

    </div>
    <div class="canvas-wraper">
      <canvas id="canvas" ref="canvas" />

      <div v-show="fontControl" class="fontControl">
        <el-select v-model="fontSize" placeholder="请选择" style="width:80px;height: 35px;">
          <el-option
            v-for="item in 15"
            :key="item*2"
            :label="2*item+'px'"
            :value="item*2"
          />
        </el-select>
      </div>
      <div class="controlPanel">
        <!-- <div class="el-divider el-divider--vertical" /> -->
        <div
          v-for="(item,idx) in toolsArr"
          :key="idx"
          :class="[initIdx==idx ? 'contro-item active' : 'contro-item']"
          @click="handleTools(item, idx)"
        >
          <i :class="'iconfont' + item.icon" />
          <div class="el-divider el-divider--vertical" />
        </div>
        <el-color-picker v-model="drawColor" size="small" />

        <div>
          <i chass="el-icon-download" @click="downLoadImage" />

        </div>
      </div>
    <!-- <img style="width:500px;height:500px;" :src="imageBase64" alt=""> -->
    </div>
  </div>
</template>
<script>
import base2File from '@/mixin/base2FileMixin.js'
import { noteUpdate } from '@/api/local.js'
import { fabric } from 'fabric'
export default {
  mixins: [base2File],
  props: {
    backImg: {
      type: String,
      default: 'https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=4077286071,2250159328&fm=15&gp=0.jpg'
    },
    savaImg: {
      type: Boolean,
      default: false
    },
    room: {
      type: String,
      default: ''
    },
    uid: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      fontControl: false, // 字体选择
      fontSize: 18,
      currentTool: '',
      done: false,
      fabricObj: null,
      initIdx: 0,
      toolsArr: [

        {
          name: 'pencil',
          icon: ' icon-pencil'
        },
        {
          name: 'text',
          icon: ' icon-ziti'
        },
        {
          name: 'line',
          icon: ' icon-line'
        },
        {
          name: 'arrow',
          icon: ' icon-arrow'
        },
        {
          name: 'xuxian',
          icon: ' icon-xuxian'
        },

        {
          name: 'juxing',
          icon: ' icon-juxing'
        },
        {
          name: 'cricle',
          icon: ' icon-yuanxing'
        },
        {
          name: 'ellipse',
          icon: ' icon-tuoyuanxing'
        },
        {
          name: 'equilateral', // 三角形
          icon: ' icon-sanjiaoxing'
        },
        {
          name: 'remove',
          icon: ' icon-remove'
        },
        {
          name: 'undo',
          icon: ' icon-huitui'
        },
        {
          name: 'redo',
          icon: ' icon-xiangqian'
        },
        {
          name: 'reset',
          icon: ' icon-reset'
        }
      ],
      mouseFrom: {},
      mouseTo: {},
      moveCount: 1,
      doDrawing: false,
      fabricHistoryJson: [],
      mods: 0,
      drawingObject: null, // 绘制对象
      drawColor: '#E34F51',
      drawWidth: 2,
      imageBase64: '',
      zoom: window.zoom ? window.zoom : 1
    }
  },
  computed: {
    canvasWidth() {
      return window.innerWidth
    }
  },
  watch: {
    backImg() {
      this.addBackImg()
    },
    savaImg(newV, oldv) {
      if (newV) {
        this.downLoadImage()
      }
    },
    drawColor() {
      console.log('this.drawColor', this.drawColor)
      // 绑定画板事件
      this.fabricObjAddEvent()
    }

  },
  mounted() {
    // 初始化canvas
    this.initCanvas()
  },
  methods: {
    // 上传笔记
    noteUpdateHandle() {
      console.log('+++++++++++++++++++++++++++noteUpdateHandle')
      const base64URl = this.fabricObj.toDataURL({
        formart: 'png',
        multiplier: 2
      })
      this.imageBase64 = this.base64toFile(base64URl)

      noteUpdate(this.room, this.uid, new Date().getTime(), this.imageBase64).then(
        res => {
          this.$message.success('上传成功')
          this.$emit('save-note')
        }
      )
    },
    initCanvas() {
      // const _this = this
      // 初始化fabric的canvas对象，
      //         object:added 添加图层
      // object:modified 编辑图层
      // object:removed 移除图层
      // selection:created 初次选中图层
      // selection:updated 图层选择变化
      // selection:cleared 清空图层选中
      this.fabricObj = new fabric.Canvas('canvas', {
        isDrawingMode: true,
        selectable: false,
        selection: false,
        devicePixelRatio: true, // Retina 高清屏 屏幕支持
        height: window.innerHeight, // 让画布同视窗大小
        width: window.innerHeight * 1.8, // 让画布同视窗大小
        // width: window.innerWidth, // 让画布同视窗大小
        backgroundImage: '' // 背景图片
      })
      this.addBackImg()
      this.fabricObj.freeDrawingBrush.color = '#E34F51'
      this.fabricObj.freeDrawingBrush.width = 2
      // this.fabricObj.setWidth(this.canvasWidth)
      // this.fabricObj.setHeight(500)

      // 绑定画板事件
      this.fabricObjAddEvent()
    },
    // 读取图片地址，设置画布背景
    addBackImg() {
      fabric.Image.fromURL(this.backImg, (img) => {
        console.log('this.fabricObj.width / img.width,', (this.fabricObj.width - img.width) / 2, this.fabricObj.width, img.width, this.fabricObj.height, img.height, this.fabricObj.width / img.width)
        img.set({

          // 通过scale来设置图片大小，这里设置和画布一样大
          // scaleX: this.fabricObj.height / img.height,

          scaleX: this.fabricObj.width / img.width,
          scaleY: this.fabricObj.height / img.height
        })

        // 设置背景
        this.fabricObj.setBackgroundImage(img, this.fabricObj.renderAll.bind(this.fabricObj)
        )
        this.fabricObj.renderAll()
      }, { crossOrigin: 'anonymous' })
    },
    // 时间监听
    fabricObjAddEvent() {
      this.fabricObj.on({
        'mouse:down': (o) => {
          this.mouseFrom.x = o.pointer.x
          this.mouseFrom.y = o.pointer.y
          this.doDrawing = true
          if (this.currentTool === 'text') {
            this.drawText()
          }
        },
        'mouse:up': (o) => {
          this.mouseTo.x = o.pointer.x
          this.mouseTo.y = o.pointer.y
          this.drawingObject = null
          this.moveCount = 1
          this.doDrawing = false
          this.updateModifications(true)
        },
        'mouse:move': (o) => {
          if (this.moveCount % 2 && !this.doDrawing) {
            // 减少绘制频率
            return
          }
          this.moveCount++
          this.mouseTo.x = o.pointer.x
          this.mouseTo.y = o.pointer.y
          this.drawing()
        },
        // 对象移动时间
        'object:moving': (e) => {
          e.target.opacity = 0.5
        },
        // 增加对象
        'object:added': (e) => {
          // debugger
        },
        'object:modified': (e) => {
          e.target.opacity = 1
          // const object = e.target
          this.updateModifications(true)
        },
        'selection:created': (e) => {
          if (e.target._objects) {
            // 多选删除
            var etCount = e.target._objects.length
            for (var etindex = 0; etindex < etCount; etindex++) {
              this.fabricObj.remove(e.target._objects[etindex])
            }
          } else {
            // 单选删除
            this.fabricObj.remove(e.target)
          }
          this.fabricObj.discardActiveObject() // 清楚选中框
          this.updateModifications(true)
        }
      })
    },
    // 储存历史记录
    updateModifications(savehistory) {
      if (savehistory) {
        this.fabricHistoryJson.push(JSON.stringify(this.fabricObj))
      }
    },
    // canvas 历史后退
    undo() {
      const state = this.fabricHistoryJson
      if (this.mods < state.length) {
        this.fabricObj.clear().renderAll()
        this.fabricObj.loadFromJSON(state[state.length - 1 - this.mods - 1])
        this.fabricObj.renderAll()
        this.mods += 1
      }
    },
    // 前进
    redo() {
      const state = this.fabricHistoryJson
      if (this.mods > 0) {
        this.fabricObj.clear().renderAll()
        this.fabricObj.loadFromJSON(state[state.length - 1 - this.mods + 1])
        this.fabricObj.renderAll()
        this.mods -= 1
      }
    },
    transformMouse(mouseX, mouseY) {
      return { x: mouseX / this.zoom, y: mouseY / this.zoom }
    },
    resetObj() {
      this.fabricObj.selectable = false
      this.fabricObj.selection = false
      this.fabricObj.skipTargetFind = true
      // 清除文字对象
      if (this.textboxObj) {
        this.textboxObj.exitEditing()
        this.textboxObj = null
      }
    },
    handleTools(tools, idx) {
      this.initIdx = idx
      this.currentTool = tools.name
      this.fabricObj.isDrawingMode = false
      this.resetObj()
      switch (tools.name) {
        case 'pencil':
          this.fontControl = false
          this.fabricObj.isDrawingMode = true
          break
        case 'text':
          this.fontControl = true
          break
        case 'remove':
          this.fontControl = false
          this.fabricObj.selection = true
          this.fabricObj.skipTargetFind = false
          this.fabricObj.selectable = true
          break
        case 'reset':
          this.fontControl = false
          this.fabricObj.clear()
          this.addBackImg()
          break
        case 'redo':
          this.fontControl = false
          this.redo()
          break
        case 'undo':
          this.fontControl = false
          this.undo()
          break
        default:
          this.fontControl = false
          break
      }
    },
    // 绘制文字对象
    drawText() {
      this.textboxObj = new fabric.Textbox(' ', {
        left: this.mouseFrom.x,
        top: this.mouseFrom.y,
        width: 220,
        fontSize: this.fontSize,
        fill: this.drawColor,
        hasControls: true
      })
      this.fabricObj.add(this.textboxObj)
      this.textboxObj.enterEditing()
      this.textboxObj.hiddenTextarea.focus()
      this.updateModifications(true)
    },
    drawing() {
      //  const _this = this
      if (this.drawingObject) {
        this.fabricObj.remove(this.drawingObject)
      }
      let fabricObject = null
      const path = 'M ' + this.mouseFrom.x + ' ' +
              this.mouseFrom.y +
              ' L ' +
              this.mouseTo.x +
              ' ' +
              this.mouseFrom.y +
              ' L ' +
              this.mouseTo.x +
              ' ' +
              this.mouseTo.y +
              ' L ' +
              this.mouseFrom.x +
              ' ' +
              this.mouseTo.y +
              ' L ' +
              this.mouseFrom.x +
              ' ' +
              this.mouseFrom.y +
              ' z'
      const radius = Math.sqrt((this.mouseTo.x - this.mouseFrom.x) * (this.mouseTo.x - this.mouseFrom.x) + (this.mouseTo.y - this.mouseFrom.y) * (this.mouseTo.y - this.mouseFrom.y)) / 2
      const left = this.mouseFrom.x
      const top = this.mouseFrom.y
      // const ellipse = Math.sqrt((this.mouseTo.x - left) * (this.mouseTo.x - left) + (this.mouseTo.y - top) * (this.mouseTo.y - top)) / 2
      const height = this.mouseTo.y - this.mouseFrom.y
      switch (this.currentTool) {
        case 'pencil':
          console.log('peccil')
          this.fabricObj.isDrawingMode = true
          break
        case 'line':
          fabricObject = new fabric.Line([this.mouseFrom.x, this.mouseFrom.y, this.mouseTo.x, this.mouseTo.y], {
            stroke: this.drawColor,
            strokeWidth: this.drawWidth
          })
          break
        case 'arrow':
          fabricObject = new fabric.Path(this.drawArrow(this.mouseFrom.x, this.mouseFrom.y, this.mouseTo.x, this.mouseTo.y, 17.5, 17.5), {
            stroke: this.drawColor,
            fill: this.drawColor,
            strokeWidth: this.drawWidth
          })
          break
        case 'xuxian': // doshed line
          fabricObject = new fabric.Line([this.mouseFrom.x, this.mouseFrom.y, this.mouseTo.x, this.mouseTo.y], {
            strokeDashArray: [10, 3],
            stroke: this.drawColor,
            strokeWidth: this.drawWidth
          })
          break
        case 'juxing': // 矩形

          fabricObject = new fabric.Path(path, {
            left: this.mouseFrom.x,
            top: this.mouseFrom.y,
            stroke: this.drawColor,
            strokeWidth: this.drawWidth,
            fill: 'rgba(255, 255, 255, 0)'
          })
          break
        case 'cricle': // 正圆
          fabricObject = new fabric.Circle({
            left: this.mouseFrom.x,
            top: this.mouseFrom.y,
            stroke: this.drawColor,
            fill: 'rgba(255, 255, 255, 0)',
            radius: radius,
            strokeWidth: this.drawWidth
          })
          break
        case 'ellipse': // 椭圆

          fabricObject = new fabric.Ellipse({
            left: left,
            top: top,
            stroke: this.drawColor,
            fill: 'rgba(255, 255, 255, 0)',
            originX: 'center',
            originY: 'center',
            rx: Math.abs(left - this.mouseTo.x),
            ry: Math.abs(top - this.mouseTo.y),
            strokeWidth: this.drawWidth
          })
          break
        case 'equilateral': // 等边三角形

          fabricObject = new fabric.Triangle({
            top: this.mouseFrom.y,
            left: this.mouseFrom.x,
            width: Math.sqrt(Math.pow(height, 2) + Math.pow(height / 2.0, 2)),
            height: height,
            stroke: this.drawColor,
            strokeWidth: this.drawWidth,
            fill: 'rgba(255,255,255,0)'
          })
          break
        case 'remove':
          break
        default:
          // statements_def'
          break
      }
      if (fabricObject) {
        this.fabricObj.add(fabricObject)
        this.drawingObject = fabricObject
      }
    },
    // 书写箭头的方法
    drawArrow(fromX, fromY, toX, toY, theta, headlen) {
      theta = typeof theta !== 'undefined' ? theta : 30
      headlen = typeof theta !== 'undefined' ? headlen : 10
      // 计算各角度和对应的P2,P3坐标
      const angle = Math.atan2(fromY - toY, fromX - toX) * 180 / Math.PI
      const angle1 = (angle + theta) * Math.PI / 180
      const angle2 = (angle - theta) * Math.PI / 180
      const topX = headlen * Math.cos(angle1)
      const topY = headlen * Math.sin(angle1)
      const botX = headlen * Math.cos(angle2)
      const botY = headlen * Math.sin(angle2)
      let arrowX = fromX - topX
      let arrowY = fromY - topY
      let path = ' M ' + fromX + ' ' + fromY
      path += ' L ' + toX + ' ' + toY
      arrowX = toX + topX
      arrowY = toY + topY
      path += ' M ' + arrowX + ' ' + arrowY
      path += ' L ' + toX + ' ' + toY
      arrowX = toX + botX
      arrowY = toY + botY
      path += ' L ' + arrowX + ' ' + arrowY
      return path
    },
    downLoadImage() {
      this.done = true
      // 生成双倍像素比的图片
      const base64URl = this.fabricObj.toDataURL({
        formart: 'png',
        multiplier: 2
      })
      this.imageBase64 = base64URl
      this.done = false
      console.log('this.imageBase64', this.imageBase64)
    }
  }
}
</script>

<style lang="scss" scoped>
@import "../assets/initCss.css";
@import "../font/iconfont.css";
  .wraper{
    //position: relative;
    width: 100%;
    height: 100%;
    background-color: #000;
    .canvas-wraper{
      height: 100%;
      width: calc(100% - 70px);
      margin-left: 70px;
      overflow: hidden;
      position: relative;
      display: flex;
      justify-content: center;
      /deep/ .canvas-container{
        height: 100% !important;
        width: 100%;

        canvas{
          height: 100% !important;
        }
      }
    }
    .fontControl{
      background-color: rgba($color: #000000, $alpha: 0.7);
      border-radius: 5px;
      border:1px #000 solid;
      margin-bottom: 2px;
      margin-left: -390px;
      position: absolute;
      bottom: 85px;
      left: 50%;
      background-color: #fff;
      /deep/ .el-input__inner{
        height: 35px;
      }

    }
    .controlPanel{
      position: absolute;
      bottom: 20px;
    border-radius: 30px;
    font-size: 20px;
    padding: 7px 20px;
    position: absolute;
    bottom: 20px;
    left: 50%;
    background-color: rgba($color: #000000, $alpha: 0.7);
    text-align: center;
    -webkit-transform: translateX(-50%);
    transform: translateX(-50%);
    color: #fff;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    .el-divider{
          height: 25px;
          margin:0 15px;
        }
      .contro-item{
        display: flex;
        flex-direction: row;
        align-items: center;
        flex-basis: 100px;
        text-align: center;
        color: #fff;
        cursor: pointer;

        i{
          font-size: 30px;
        }
        &.active{
          color: #e34f51;

        }
      }
    }
    // .download{
    //   img{
    //     width: 100%;
    //   }
    // }
    .save-button{
      padding-top: 90px;
      position: absolute;
      top:0;
      left: 0;
      width: 70px;
      background-color: #333;
      height: 100%;
        color: #fff;
        i{
          font-size: 30px;

        }
      }
  }
</style>
