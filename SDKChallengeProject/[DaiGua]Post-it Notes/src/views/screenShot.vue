<!-- 截图组件 -->
<template>
  <!-- <div class="cut-content" :style="{height:cutheight+'px',width:cutwidth+'px'}"> -->
  <div
    v-loading="loading"
    class="cut-content"
    element-loading-text="拼命加载中"
    element-loading-spinner="el-icon-loading"
    element-loading-background="rgba(0, 0, 0, 0.8)"
  >
    <div class="cut">
      <vue-cropper
        ref="cropper"
        :img="option.img"
        :output-size="option.size"
        :output-type="option.outputType"
        :info="true"
        :full="option.full"
        :fixed="fixed"
        :fixed-number="fixedNumber"
        :can-move="option.canMove"
        :can-move-box="option.canMoveBox"
        :fixed-box="option.fixedBox"
        :original="option.original"
        :auto-crop="option.autoCrop"
        :auto-crop-width="option.autoCropWidth"
        :auto-crop-height="option.autoCropHeight"
        :center-box="option.centerBox"
        :high="option.high"
        mode="contain"
        @real-time="realTime"
        @img-load="imgLoad"
      />
      <div v-show="toolPanelVisi" class="toolPanel">
        <i class="el-icon-close" style="color:red" @click="clearCrop" />
        <i class="el-icon-check" style="color:green" @click="finish()" />
      </div>
    </div>
    <el-dialog
      title="提示"
      :visible.sync="ensureWordVisi"
      width="30%"
    >
      <p>
        <span>确认查询</span>
        <span v-if="wordRight" style="font-weight:bold;margin:0 10px;font-size:16px;">{{ wordValue }}</span>
        <el-input v-else v-model="wordValue" />
        <i type="primary" class="el-icon-edit" circle @click="wordRight = false" />
      </p>
      <span slot="footer" class="dialog-footer">
        <el-button @click="ensureWordVisi = false">取 消</el-button>
        <el-button type="primary" @click="ensureWordHandle">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import base2File from '@/mixin/base2FileMixin.js'
import { VueCropper } from 'vue-cropper'
import { uploadWordPic, ensureWord } from '@/api/local.js'
export default {
  name: 'ScreenShotDemo',
  components: {
    VueCropper
  },
  mixins: [base2File],
  props: {
    imgUrl: {
      type: String,
      default: 'https://qn-qn-kibey-static-cdn.app-echo.com/goodboy-weixin.PNG'
    }
  },
  data() {
    return {
      loading: false,
      ensureWordVisi: false,
      wordValue: '',
      wordRight: true,
      cutheight: window.innerHeight,
      cutwidth: window.innerHeight * 1.8,
      toolPanelVisi: false,
      model: false,
      modelSrc: '',
      crap: false,
      previews: {},
      lists: [
        {
          img: 'https://qn-qn-kibey-static-cdn.app-echo.com/goodboy-weixin.PNG'
        },
        {
          img: 'https://avatars2.githubusercontent.com/u/15681693?s=460&v=4'
        }
      ],
      option: {
        // 裁剪图片的地址
        img: this.imgUrl,
        // 裁剪生成图片的质量
        size: 1,
        // 是否输出原图比例的截图
        full: false,
        // 裁剪生成图片的格式
        outputType: 'png',
        // 上传图片是否可以移动
        canMove: true,
        // 固定截图框大小 不允许改变
        fixedBox: false,
        // 上传图片按照原始比例渲染
        original: false,
        // 截图框能否拖动
        canMoveBox: true,
        // 是否默认生成截图框
        autoCrop: false,
        // 只有自动截图开启 宽度高度才生效
        // 默认生成截图框宽度
        autoCropWidth: 200,
        // 默认生成截图框高度
        autoCropHeight: 150,
        // 截图框是否被限制在图片里面
        centerBox: true,
        // 是否按照设备的dpr 输出等比例图片
        high: true
      },
      show: true,
      // 是否开启截图框宽高固定比例
      fixed: false,
      // 截图框的宽高比例
      fixedNumber: [1, 2],
      // 裁剪框的大小信息
      info: true,
      // canScale 图片是否允许滚轮缩放
      canScale: true,
      // infoTrue  true 为展示真实输出图片宽高 false 展示看到的截图框宽高:
      infoTrue: true,
      // maxImgSize  限制图片最大宽度和高度
      maxImgSize: 2000,
      // enlarge 图片根据截图框输出比例倍数
      enlarge: 1,
      // mode  图片默认渲染方式
      mode: 'contain'
    }
  },
  mounted() {
    this.startCrop()
  },
  methods: {
    /**
     * 内置方法 通过this.$refs.cropper 调用
     this.$refs.cropper.startCrop() 开始截图
     this.$refs.cropper.stopCrop() 停止截图
     this.$refs.cropper.clearCrop() 清除截图
     this.$refs.cropper.changeScale() 修改图片大小 正数为变大 负数变小
     this.$refs.cropper.getImgAxis() 获取图片基于容器的坐标点
     this.$refs.cropper.getCropAxis() 获取截图框基于容器的坐标点
     this.$refs.cropper.goAutoCrop 自动生成截图框函数
     this.$refs.cropper.rotateRight() 向右边旋转90度
     this.$refs.cropper.rotateLeft() 向左边旋转90度
     图片加载的回调 imgLoad 返回结果success, error
     获取截图信息
     this.$refs.cropper.cropW 截图框宽度
     this.$refs.cropper.cropH 截图框高度
     */
    changeImg() {
      this.option.img = this.lists[~~(Math.random() * this.lists.length)].img
    },
    startCrop() {
      // start
      console.log('this.$refs.cropper.getCropAxis()', this.$refs.cropper.getCropAxis())

      this.crap = true
      this.$refs.cropper.startCrop()
    },
    stopCrop() {
      //  stop
      this.crap = false
      this.$refs.cropper.stopCrop()
    },
    clearCrop() {
      // clear
      this.toolPanelVisi = false
      this.$refs.cropper.clearCrop()
    },
    refreshCrop() {
      // clear
      this.$refs.cropper.refresh()
    },
    changeScale(num) {
      num = num || 1
      this.$refs.cropper.changeScale(num)
    },
    rotateLeft() {
      this.$refs.cropper.rotateLeft()
    },
    rotateRight() {
      this.$refs.cropper.rotateRight()
    },
    finish(type) {
      // 输出
      this.loading = true
      // var test = window.open('about:blank')
      // test.document.body.innerHTML = '图片生成中..'
      this.$refs.cropper.getCropData((data) => {
        this.model = true
        this.modelSrc = this.base64toFile(data)
        console.log('finishother', data)
        uploadWordPic(this.modelSrc).then(
          res => {
            this.loading = false
            console.log('res', res)
            // const aplitArr = res.data.split(' ')
            // this.wordValue = aplitArr[aplitArr.length - 2]
            this.wordValue = res[0].word
            this.ensureWordVisi = true
          }
        )
      })
    },
    // 确认查询该词
    ensureWordHandle() {
      this.$emit('screen-shot-finish')
      ensureWord(this.$route.query.room, this.wordValue).then(
        res => {
          this.ensureWordVisi = false
          this.$notify({
            title: '查词结果',
            message: res.wiki,
            duration: 0
          })
        }
      )
    },
    // 实时预览函数
    realTime(data) {
      if (data.h > 0 || data.w > 0) {
        this.toolPanelVisi = true
      } else {
        this.toolPanelVisi = false
      }
      this.previews = data
      this.$emit('sreen', this.previews)
      console.log('实时预览函数', data)
    },

    // finish2(type) {
    //   this.$refs.cropper2.getCropData((data) => {
    //     this.model = true
    //     this.modelSrc = data
    //   })
    // },
    // finish3(type) {
    //   this.$refs.cropper3.getCropData((data) => {
    //     this.model = true
    //     this.modelSrc = data
    //   })
    // },
    down(type) {
      // event.preventDefault()
      var aLink = document.createElement('a')
      aLink.download = 'demo'
      // 输出
      if (type === 'blob') {
        this.$refs.cropper.getCropBlob((data) => {
          this.downImg = window.URL.createObjectURL(data)
          aLink.href = window.URL.createObjectURL(data)
          aLink.click()
        })
      } else {
        this.$refs.cropper.getCropData((data) => {
          this.downImg = data
          aLink.href = data
          aLink.click()
        })
      }
    },
    uploadImg(e, num) {
      // 上传图片
      // this.option.img
      var file = e.target.files[0]
      if (!/\.(gif|jpg|jpeg|png|bmp|GIF|JPG|PNG)$/.test(e.target.value)) {
        alert('图片类型必须是.gif,jpeg,jpg,png,bmp中的一种')
        return false
      }
      var reader = new FileReader()
      reader.onload = (e) => {
        let data
        if (typeof e.target.result === 'object') {
          // 把Array Buffer转化为blob 如果是base64不需要
          data = window.URL.createObjectURL(new Blob([e.target.result]))
        } else {
          data = e.target.result
        }
        if (num === 1) {
          this.option.img = data
        } else if (num === 2) {
          this.example2.img = data
        }
      }
      // 转化为base64
      // reader.readAsDataURL(file)
      // 转化为blob
      reader.readAsArrayBuffer(file)
    },
    imgLoad(msg) {
      console.log(msg)
    }
    //     1、修改图片就修改option.img的路径
    // 2、this.$refs.cropper.startCrop()开始截图

    // 3、this.$refs.cropper.stopCrop() 停止截图

    // 4、this.$refs.cropper.changeScale() 修改图片大小 正数为变大 负数变小

    // 5、this.$refs.cropper.getImgAxis() 获取图片基于容器的坐标点
    // 6、this.$refs.cropper.getCropAxis() 获取截图框基于容器的坐标点

    // 7、this.$refs.cropper.goAutoCrop 自动生成截图框函数
    // 8、this.$refs.cropper.rotateRight() 向右边旋转90度

    // 9、this.$refs.cropper.rotateLeft() 向左边旋转90度

    // 10、realTime实时预览函数

    // 11、获取二进制流数据
    // this.$refs.cropper.getCropBlob((data) => {
    //  data 为二进制流数据
    // })

    // 12、获取base64数据
    // this.$refs.cropper.getCropData((data) => {
    // data为base64数据
    // })
    // 13、uploadImg(e, num)
    // 上传函数，其中;
    // // 转化为base64
    // reader.readAsDataURL(file)
    // // 转化为blob
    // reader.readAsArrayBuffer(file)
  }
}
</script>
<style lang="scss" scoped>
/deep/ .el-input__inner {
    -webkit-appearance: none;
    background-color: #fff;
    background-image: none;
    border-radius: 4px;
    border: 1px solid #dcdfe6;
    box-sizing: border-box;
    color: #606266;
    display: inline-block;
    font-size: inherit;
    height: 40px;
    line-height: 40px;
    outline: none;
    padding: 0 15px;
    transition: border-color .2s cubic-bezier(.645,.045,.355,1);
    width: 100%;
}
/deep/ .el-button{
  display: inline-block;
    line-height: 1;
    white-space: nowrap;
    cursor: pointer;
    background: #fff;
    border: 1px solid #dcdfe6;
    color: #606266;
    -webkit-appearance: none;
    text-align: center;
    box-sizing: border-box;
    outline: none;
    margin: 0 10px;
    transition: .1s;
    font-weight: 500;
    -moz-user-select: none;
    -webkit-user-select: none;
    -ms-user-select: none;
    padding: 12px 20px;
    font-size: 14px;
    border-radius: 4px;
}
  * {
    margin: 0;
    padding: 0;
  }
  .cut-content{
    height: 100%;
    width:100%;
    background-color: #000;
  }
  .cut {
    width: 100%;
    height: 100%;
    /deep/ .vue-cropper{
      background-color: #000;
      background-image:none;
    }

  }
  /deep/ .cropper-crop-box{
      position:relative !important;
  }
  .toolPanel{
    position: absolute;
    top:0;
    left: 50%;
    -webkit-transform: translateX(-50%);
    transform: translateX(-50%);
    padding: 5px 10px;
    background-color: rgba($color: #000000, $alpha: 0.7);
      i {
        font-size: 40px;
        margin:0 10px;

      }

  }
  .c-item {
    max-width: 800px;
    margin: 10px auto;
    margin-top: 20px;
  }
  .content {
    margin: auto;
    max-width: 1200px;
    margin-bottom: 100px;
  }
  .test-button {
    display: flex;
    flex-wrap: wrap;
    align-content: center;
    justify-content: center;
  }
  .btn {
    display: inline-block;
    line-height: 1;
    white-space: nowrap;
    cursor: pointer;
    background: #fff;
    border: 1px solid #c0ccda;
    color: #1f2d3d;
    text-align: center;
    box-sizing: border-box;
    outline: none;
    margin:20px 10px 0px 0px;
    padding: 9px 15px;
    font-size: 14px;
    border-radius: 4px;
    color: #fff;
    background-color: #50bfff;
    border-color: #50bfff;
    transition: all .2s ease;
    text-decoration: none;
    user-select: none;
  }
  .des {
    line-height: 30px;
  }
  code.language-html {
    padding: 10px 20px;
    margin: 10px 0px;
    display: block;
    background-color: #333;
    color: #fff;
    overflow-x: auto;
    font-family: Consolas, Monaco, Droid, Sans, Mono, Source, Code, Pro, Menlo, Lucida, Sans, Type, Writer, Ubuntu, Mono;
    border-radius: 5px;
    white-space: pre;
  }
  .show-info {
    margin-bottom: 50px;
  }
  .show-info h2 {
    line-height: 50px;
  }
  .title {
    display: block;
    text-decoration: none;
    text-align: center;
    line-height: 1.5;
    margin: 20px 0px;
    background-image: -webkit-linear-gradient(left,#3498db,#f47920 10%,#d71345 20%,#f7acbc 30%,#ffd400 40%,#3498db 50%,#f47920 60%,#d71345 70%,#f7acbc 80%,#ffd400 90%,#3498db);
    color: transparent;
    -webkit-background-clip: text;
    background-size: 200% 100%;
    animation: slide 5s infinite linear;
    font-size: 40px;
  }
  .test {
    height: 500px;
  }
  .model {
    position: fixed;
    z-index: 10;
    width: 100vw;
    height: 100vh;
    overflow: auto;
    top: 0;
    left: 0;
    background: rgba(0, 0, 0, 0.8);
  }
  .model-show {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100vw;
    height: 100vh;
  }
  .model img {
    display: block;
    margin: auto;
    max-width: 80%;
    user-select: none;
    background-position: 0px 0px, 10px 10px;
    background-size: 20px 20px;
    background-image: linear-gradient(45deg, #eee 25%, transparent 25%, transparent 75%, #eee 75%, #eee 100%),linear-gradient(45deg, #eee 25%, white 25%, white 75%, #eee 75%, #eee 100%);
  }
  .c-item {
    display: block;
    user-select: none;
  }
  @keyframes slide {
    0%  {
      background-position: 0 0;
    }
    100% {
      background-position: -100% 0;
    }
  }
</style>
