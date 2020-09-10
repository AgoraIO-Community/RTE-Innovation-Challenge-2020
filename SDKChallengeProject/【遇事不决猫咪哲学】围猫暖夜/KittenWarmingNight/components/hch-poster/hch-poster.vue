<!--
 * @Description: 生成海报组件
 * @Version: 1.0.0
 * @Autor: hch
 * @Date: 2020-08-07 14:48:41
 * @LastEditors: hch
 * @LastEditTime: 2020-08-10 11:02:27
 * 保存海报按钮和关闭按钮 在html代码中写出来 绑定点击方法然后透明 再用canvas 覆盖
-->

<template>
  <view class="canvas-content" v-show="canvasShow">
    <!-- 遮罩层 -->
    <view class="canvas-mask"></view>
    <canvas class="canvas" canvas-id="myCanvas"></canvas><!-- 海报 -->
    <!-- 关闭按钮 -->
    <!-- <cover-image class="canvas-close-btn" @tap="handleCanvasCancel" src="https://huangchunhongzz.gitee.io/imgs/poster/close_btn.png" alt="" srcset=""></cover-image> -->
    <view class="button-wrapper">
      <!-- 保存海报按钮 -->
      <cover-view class="save-btn" @tap="handleSaveCanvasImage">保存海报</cover-view>
      <cover-view class="save-btn cancel-btn" @tap="handleCanvasCancel">取消</cover-view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      canvasShow:false,
      attrs:{//简单版的海报
         marginLR: 70,
         marginTB: 45,
         radius: 0.05,
		 innerLR: 40,
		 innerTB: 35,
         fillColor: '#000000',
         title: ["越过最深的荆棘能看到最美的风景"],
         titleFontSize: 20,
         titleLineHeight: 24,
	     posterCodeUrl: "https://vkceyugu.cdn.bspapp.com/VKCEYUGU-wangshanyw/cb1e56d0-ef40-11ea-8ff1-d5dcf8779628.jpg",
         codeWidth: 0.25,
         codeRatio: 1,
         codeRadius: 0.5,
         codeMT: 20,
         posterBgUrl:'https://vkceyugu.cdn.bspapp.com/VKCEYUGU-wangshanyw/b0307370-ef32-11ea-9dfb-6da8e309e0d8.jpg',
      //   codeML:140,
         desTextMT:70,
         desTextML:240,
       },
    }
  },
  props: {
    canvasAttr:{
      type:Object,
      default:{
        marginLR: 70,
        marginTB: 45,
        radius: 0.05,
        fillColor: '#000000',
        innerLR: 40,
        innerTB: 35,
        //posterRatio: 1.3,
        //posterImgUrl: "https://huangchunhongzz.gitee.io/imgs/poster/product.png",
        title: ["越过最深的荆棘能看到最美的风景"],
        titleFontSize: 20,
        titleLineHeight: 24,
        posterCodeUrl: "https://vkceyugu.cdn.bspapp.com/VKCEYUGU-wangshanyw/cb1e56d0-ef40-11ea-8ff1-d5dcf8779628.jpg",
        codeWidth: 0.25,
        codeRatio: 1,
        codeRadius: 0.5,
        codeMT: 20,
        //
		//codeName: "记忆之王",
        //codeNameMT: 20,
        //tips: "长按/扫描识别查看商品",
        posterBgUrl:'https://vkceyugu.cdn.bspapp.com/VKCEYUGU-wangshanyw/b0307370-ef32-11ea-9dfb-6da8e309e0d8.jpg',
        codeML:140,
        desTextMT:70,
        desTextML:240,
      }
    },
    posterBgFlag:{//是否展示海报背景图
      type:Boolean,
      default:false
    },
    simpleFlag:{//是否展示简单版海报
      type:Boolean,
      default:false
    },
  },
  mounted() {
    this.system = this.getSystem()
  },
  methods: {
    /**
     * @description: 展示海报
     * @param {type} 
     * @return {type} 
     * @author: hch
     */
    posterShow(){
      this.canvasShow = true
      Object.assign(this.attrs,this.canvasAttr)
      this.attrs={
        marginLR: this.attrs.marginLR*this.systemScale,
        marginTB: this.attrs.marginTB*this.systemScale,
        innerLR: this.attrs.innerLR*this.systemScale,
        innerTB: this.attrs.innerTB*this.systemScale,
        radius: this.attrs.radius,
        fillColor: this.attrs.fillColor,
        //posterRatio: this.attrs.posterRatio,
        //posterImgUrl:this.attrs.posterImgUrl,
        title: this.attrs.title,
        titleFontSize: this.attrs.titleFontSize*this.systemScale,
        titleLineHeight: this.attrs.titleLineHeight*this.systemScale,
        posterCodeUrl: this.attrs.posterCodeUrl,
        codeWidth: this.attrs.codeWidth*this.systemScale,
        codeRatio: this.attrs.codeRatio,
        codeRadius: this.attrs.codeRadius,
        codeMT: this.attrs.codeMT*this.systemScale,
        //codeName: this.attrs.codeName,
        //codeNameMT: this.attrs.codeNameMT*this.systemScale,
        //tips: this.attrs.tips,
        posterBgUrl: this.attrs.posterBgUrl,
        codeML: this.attrs.codeML*this.systemScale,
        desTextMT:this.attrs.desTextMT*this.systemScale,
        desTextML:this.attrs.desTextML*this.systemScale,
      }
      if(this.simpleFlag){
        this.creatSimplePoster(this.attrs)
      }else{
        this.creatPoster(this.attrs)
      }
    },
    /**
     * @description: 生成海报
     * @param {number} marginTB 上下距离
     * @param {number} marginLR 左右距离
     * @param {number} innerLR 上下内边距
     * @param {number} innerTB 左右内边距
     * @param {number} radius 圆角
     * @param {number} fillColor 海报填充背景色
     * @param {number} posterRatio 海报主图片宽高比例
     * @param {number} posterImgUrl 海报主图片url
     * @param {number} title 海报的title
     * @param {number} titleFontSize 字体大小
     * @param {number} titleLineHeight 标题文本的行高大小
     * @param {number} posterCodeUrl 小程序码图片
     * @param {number} codeWidth 小程序码的占屏幕的百分比
     * @param {number} codeRatio 小程序码的宽度比例
     * @param {number} codeRadius 小程序码的圆角
     * @param {number} codeMT 小程序码的margin-top值
     * @param {number} codeName 小程序码名字
     * @param {number} codeNameMT 小程序码名字的margin-top值
     * @param {number} tips 提示语
     * @author: hch
     */
    async creatPoster(canvasAttr){
      wx.showLoading({
        title: "生成海报中..."
      })
      const ctx = wx.createCanvasContext("myCanvas",this)
      ctx.draw()//清空之前的海报
      // 根据设备屏幕大小和距离屏幕上下左右距离，及圆角绘制背景
      this.roundRect(ctx,canvasAttr.marginLR,canvasAttr.marginTB,this.system.w-2*canvasAttr.marginLR,(this.scale*this.system.w)-2*canvasAttr.marginTB-100, (this.system.w-2*canvasAttr.marginLR)*canvasAttr.radius,canvasAttr.fillColor );
      if(this.posterBgFlag){
         await this.creatBgImg(ctx,canvasAttr)
      }
      //let imgAttr = await this.creatImg(ctx,canvasAttr)
      // 绘制标题 textY 绘制文本的y位置
      let textY = this.creatTitle(ctx,canvasAttr) //
      // 绘制小程序码
      this.creatCode(ctx,canvasAttr,textY)
      // 小程序的名称
      //this.creatCodeText(ctx,canvasAttr,canvasAttr.codeName,textY,16,"#2f1709")
      // 长按/扫描识别查看商品
      //this.creatCodeText(ctx,canvasAttr,canvasAttr.tips,textY+30,14,"#2f1709")
      wx.hideLoading()
    },
    /**
     * @description: 生成海报
     * @param {number} marginTB 上下距离
     * @param {number} marginLR 左右距离
     * @param {number} innerLR 上下内边距
     * @param {number} innerTB 左右内边距
     * @param {number} radius 圆角
     * @param {number} fillColor 海报填充背景色
     * @param {number} posterRatio 海报主图片宽高比例
     * @param {String} posterImgUrl 海报主图片url
     * @param {String} title 海报的title
     * @param {number} titleFontSize 字体大小
     * @param {number} titleLineHeight 标题文本的行高大小
     * @param {String} posterCodeUrl 小程序码图片
     * @param {number} codeWidth 小程序码的占屏幕的百分比
     * @param {number} codeRatio 小程序码的宽度比例
     * @param {number} codeRadius 小程序码的圆角
     * @param {number} codeMT 小程序码的margin-top值
     * @param {String} codeName 小程序码名字
     * @param {number} codeNameMT 小程序码名字的margin-top值
     * @param {String} tips 提示语
     * @param {number} codeML 小程序码左边距离
     * @param {number} desTextMT 纵向文本描述 上边距
     * @param {number} desTextML 纵向文本描述 左边距
     * @author: hch
     */
    async creatSimplePoster(canvasAttr){
      wx.showLoading({
        title: "生成海报中..."
      })
      const ctx = wx.createCanvasContext("myCanvas",this)
      ctx.draw()//清空之前的海报
      // 根据设备屏幕大小和距离屏幕上下左右距离，及圆角绘制背景
      this.roundRect(ctx,canvasAttr.marginLR,canvasAttr.marginTB,this.system.w-2*canvasAttr.marginLR,(this.scale*this.system.w)-2*canvasAttr.marginTB-100, (this.system.w-2*canvasAttr.marginLR)*canvasAttr.radius,canvasAttr.fillColor );
      if(this.posterBgFlag){
         await this.creatBgImg(ctx,canvasAttr)
      }
	  this.creatVerticalTitle(ctx,canvasAttr)
      // 绘制小程序码
      this.creatSimpleCode(ctx,canvasAttr,canvasAttr.marginTB)
      // 绘制纵向文本
      wx.hideLoading()
    },
    /**
      * 
      * @param {CanvasContext} ctx canvas上下文
      * @param {number} x 圆角矩形选区的左上角 x坐标
      * @param {number} y 圆角矩形选区的左上角 y坐标
      * @param {number} w 圆角矩形选区的宽度
      * @param {number} h 圆角矩形选区的高度
      * @param {number} r 圆角的半径
      * @param {String} fillColor 天聪颜色
      */
    roundRect(ctx, x, y, w, h, r, fillColor='#ffffff') {
      ctx.save()
      ctx.beginPath()
      // 绘制左上角圆弧
      ctx.arc(x + r, y + r, r, Math.PI, Math.PI * 1.5)
      // 绘制border-top
      // 画一条线 x终点、y终点
      ctx.lineTo(x + w - r, y)
      // 绘制右上角圆弧
      ctx.arc(x + w - r, y + r, r, Math.PI * 1.5, Math.PI * 2)
      // 绘制border-right
      ctx.lineTo(x + w, y + h - r)
      // 绘制右下角圆弧
      ctx.arc(x + w - r, y + h - r, r, 0, Math.PI * 0.5)
      // 绘制左下角圆弧
      ctx.arc(x + r, y + h - r, r, Math.PI * 0.5, Math.PI)
      // 绘制border-left
      ctx.lineTo(x, y + r)
      // 填充颜色
      ctx.setFillStyle('#ffffff')
      ctx.fill()
      // 剪切，剪切之后的绘画绘制剪切区域内进行，需要save与restore 这个很重要 不然没办法保存
      ctx.clip()
    },
    /**
     * @description: 获取设备信息
     * @param {type} 
     * @return {type} 
     * @author: hch
     */
    getSystem(){
      let system = wx.getSystemInfoSync()
      this.scale = 667 / 375 //按照苹果留 375*667比例 其他型号手机等比例缩放 显示
      this.systemScale = system.windowWidth / 375 //按照苹果留 375*667比例 其他型号手机等比例缩放 显示
      return {w:system.windowWidth,h:system.windowHeight}
    },
    /**
     * @description: 生成海报背景图
     * @param {type} 
     * @return {type} 
     * @author: hch
     */
    creatBgImg(ctx,canvasAttr){
      let _this = this
      return new Promise((resolve,reject)=>{
        wx.getImageInfo({
          src: canvasAttr.posterBgUrl,
          success (res) {
            const imgAttr = res//海报背景展示图片信息
            let sale = res.width/(_this.system.w-2*canvasAttr.marginLR)
            ctx.drawImage(res.path,canvasAttr.marginLR,canvasAttr.marginTB, _this.system.w-2*canvasAttr.marginLR,(_this.scale*_this.system.w)-2*canvasAttr.marginTB-100)
            ctx.restore()
            ctx.draw(true)
            resolve()
          },
          fail(res) {
            console.log("fail -> res", res)
            uni.showToast({
              title: "海报背景图下载异常",
              duration: 2000,
              icon: "none"
            })
          }
        })
      })
    },
    /**
     * @description: 生成头部海报图
     * @param {type} 
     * @return {type} 
     * @author: hch
     */
    creatImg(ctx,canvasAttr){
      let _this = this
      return new Promise((resolve,reject)=>{
        wx.getImageInfo({
          src: canvasAttr.posterImgUrl,
          success (res) {
            ctx.restore()
            ctx.draw(true)
            const imgAttr = res//海报展示图片信息
            let sale = res.width/(_this.system.w-2*canvasAttr.marginLR-2*canvasAttr.innerLR)
            _this.roundRect(ctx,canvasAttr.marginLR+canvasAttr.innerLR,canvasAttr.marginTB+canvasAttr.innerLR, _this.system.w-2*canvasAttr.marginLR-2*canvasAttr.innerLR,(_this.system.w-2*canvasAttr.marginLR-2*canvasAttr.innerLR)/canvasAttr.posterRatio, (_this.system.w-2*canvasAttr.marginLR)*canvasAttr.radius );
            ctx.drawImage(res.path,canvasAttr.marginLR+canvasAttr.innerLR,canvasAttr.marginTB+canvasAttr.innerLR, _this.system.w-2*canvasAttr.marginLR-2*canvasAttr.innerLR,(_this.system.w-2*canvasAttr.marginLR-2*canvasAttr.innerLR)/canvasAttr.posterRatio)
            ctx.restore()
            ctx.draw(true)
            resolve({imgAttr,sale})
          },
          fail(res) {
            console.log("fail -> res", res)
            uni.showToast({
              title: "海报主图下载异常",
              duration: 2000,
              icon: "none"
            })
          }
        })

      })
    },
    /**
     * @description: 生成横向文本标题
     * @param {type} 
     * @return {type} 
     * @author: hch
     */
    creatTitle(ctx,canvasAttr){
      ctx.restore() //恢复之前保存的绘图上下文
      ctx.save() 
      ctx.setFontSize(canvasAttr.titleFontSize)
      let textY = this.drawText(ctx, canvasAttr.title,canvasAttr.marginLR+canvasAttr.innerLR, (this.system.w-2*canvasAttr.marginLR-2*canvasAttr.innerLR)/canvasAttr.posterRatio+2*canvasAttr.innerLR+canvasAttr.marginTB, this.system.w-2*canvasAttr.marginLR-2*canvasAttr.innerLR, canvasAttr.titleLineHeight)
      ctx.draw(true)
      return textY
    },
    /**
     * @description: 生成纵向文本标题
     * @param {type} 
     * @return {type} 
     * @author: hch
     */
    creatVerticalTitle(ctx,canvasAttr){
      ctx.restore() //恢复之前保存的绘图上下文
      ctx.save() 
      ctx.setFontSize(canvasAttr.titleFontSize)
      canvasAttr.title.forEach((element,i) => {
        this.drawText(ctx, element, canvasAttr.marginTB+canvasAttr.desTextML-(i+1)*30,canvasAttr.marginLR+canvasAttr.desTextMT, 5, canvasAttr.titleLineHeight)
      });
      ctx.draw(true)
    },
    /**
     * @param {Object} ctx canvas上下文
     * @param {String} text 需要输入的文本
     * @param {Number} x X轴起始位置
     * @param {Number} y Y轴起始位置
     * @param {Number} maxWidth 单行最大宽度
     * @param {Number} lineHeight 行高
     */
    drawText(ctx, text, x, y, maxWidth=375, lineHeight=30) {
        let canvas = ctx.canvas;
        let arrText = text.split('');
        let line = '';
        for (let n = 0; n < arrText.length; n++) {
            let testLine = line + arrText[n];
            let metrics = ctx.measureText(testLine);
            let testWidth = metrics.width;
            if (testWidth > maxWidth && n > 0) {
                ctx.fillText(line, x, y);
                line = arrText[n];
                y += lineHeight;
            } else {
                line = testLine;
            }
        }
        ctx.fillText(line, x, y);
        return y
    },
    /**
     * @description: 生成居中文本
     * @param {type} 
     * @return {type} 
     * @author: hch
     */
    creatCodeText(ctx,canvasAttr,text,textY,fontSize=14,fillStyle="#2f1709"){
      ctx.setFontSize(fontSize)
      ctx.setFillStyle(fillStyle) //文字颜色：默认黑色
      let metrics = ctx.measureText(text);
      let testWidth = metrics.width;
      ctx.fillText(text,(this.system.w-testWidth)/2,textY+canvasAttr.codeMT+this.system.w*canvasAttr.codeWidth+canvasAttr.codeNameMT+10)
      // 小程序的名称end
    },
    /**
     * @description: 生成文本（带中间划线的）
     * @param {type} 
     * @return {type} 
     * @author: hch
     */
    creatTextLine(){

    },
    /**
     * @description: 小程序码
     * @param {type} 
     * @return {type} 
     * @author: hch
     */
    creatCode(ctx,canvasAttr,textY){
      const _this = this
      wx.getImageInfo({
        src: canvasAttr.posterCodeUrl,
        success(res) {
          ctx.restore()
          ctx.draw(true)
          _this.roundRect(ctx, 150, 800, _this.system.w*canvasAttr.codeWidth, _this.system.w*canvasAttr.codeWidth*canvasAttr.codeRatio, (_this.system.w*canvasAttr.codeWidth*canvasAttr.codeRatio)*canvasAttr.codeRadius );
          ctx.drawImage(res.path, 150, 800, _this.system.w*canvasAttr.codeWidth, _this.system.w*canvasAttr.codeWidth*canvasAttr.codeRatio)
          ctx.restore()
          ctx.draw(true)
        },
        fail() {
          uni.showToast({ title: "海报生成失败", duration: 2000, icon: "none" })
        }
      })
    },
    /**
     * @description: 小程序码
     * @param {type} 
     * @return {type} 
     * @author: hch
     */
    creatSimpleCode(ctx,canvasAttr){
      const _this = this
      wx.getImageInfo({
        src: canvasAttr.posterCodeUrl,
        success(res) {
          ctx.restore()
          ctx.draw(true)
          _this.roundRect(ctx, canvasAttr.marginLR+canvasAttr.innerLR, _this.system.w-canvasAttr.marginTB-canvasAttr.innerTB, _this.system.w*canvasAttr.codeWidth, _this.system.w*canvasAttr.codeWidth*canvasAttr.codeRatio, (_this.system.w*canvasAttr.codeWidth*canvasAttr.codeRatio)*canvasAttr.codeRadius );
          ctx.drawImage(res.path,canvasAttr.marginLR+canvasAttr.innerLR, _this.system.w-canvasAttr.marginTB-canvasAttr.innerTB,_this.system.w*canvasAttr.codeWidth, _this.system.w*canvasAttr.codeWidth*canvasAttr.codeRatio)
          ctx.restore()
          ctx.draw(true)
        },
        fail() {
          uni.showToast({ title: "海报生成失败", duration: 2000, icon: "none" })
        }
      })
    },
    /**
     * @description: 保存到系统相册
     * @param {type} 
     * @return {type} 
     * @author: hch
     */
    handleSaveCanvasImage() {
      const canvasAttr=this.attrs
      wx.showLoading({
        title: "保存中..."
      })
      let _this = this
      // 把画布转化成临时文件
      uni.canvasToTempFilePath({
        x: canvasAttr.marginLR,
        y: canvasAttr.marginTB,
        width: this.system.w-2*canvasAttr.marginLR, // 画布的宽
        height: (this.scale*this.system.w)-2*canvasAttr.marginTB-100, // 画布的高
        destWidth: (this.system.w-2*canvasAttr.marginLR) * 5,
        destHeight: ((this.scale*this.system.w)-2*canvasAttr.marginTB-100) * 5,
        canvasId: "myCanvas",
        success(res) {
          //保存图片至相册
          uni.saveImageToPhotosAlbum({
            filePath: res.tempFilePath,
            success(res) {
              wx.hideLoading()
              uni.showToast({
                title: "图片保存成功，可以去分享啦~",
                duration: 2000,
                icon: "none"
              })
              _this.handleCanvasCancel()
            },
            fail() {
              uni.showToast({
                title: "保存失败，稍后再试",
                duration: 2000,
                icon: "none"
              })
              wx.hideLoading()
            }
          })
        },
        fail(res) {
          console.log("fail -> res", res)
          uni.showToast({
            title: "保存失败，稍后再试",
            duration: 2000,
            icon: "none"
          })
          wx.hideLoading()
        }
      },this)
    },
    /**
     * @description: 取消海报
     * @param {type} 
     * @return {type} 
     * @author: hch
     */
    handleCanvasCancel() {
      this.canvasShow = false
      this.$emit("cancel", true)
    }

  }
}
</script>

<style lang="scss">
.content {
  text-align: center;
  height: 100%;
}
.canvas-content {
  .canvas-mask {
    width: 100%;
    height: 100%;
    position: fixed;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    background: rgba(0, 0, 0, 0.5);
    z-index: 9;
  }
  // .canvas {
  //   position: fixed !important;
  //   top: 0 !important;
  //   left: 0 !important;
  //   display: block !important;
  //   width: 100% !important;
  //   height: 100% !important;
  //   z-index: 9;
  // }
  .button-wrapper {
    width: 100%;
    height: 72rpx;
    position: fixed;
    bottom: 20rpx;
    display: flex;
    justify-content: space-around;
    z-index: 16;
  }

  .save-btn {
    font-size: 30rpx;
    line-height: 72rpx;
    color: #fff;
    background: $uni-btn-color;
    width: 40%;
    height: 100%;
    text-align: center;
    border-radius: 45rpx;
    border-radius: 36rpx;
    z-index: 10;
  }
  .cancel-btn{
    background: #fff;
    color: $uni-btn-color;
  }
  .canvas-close-btn {
    position: fixed;
    width: 60rpx;
    height: 60rpx;
    padding: 20rpx;
    top: 30rpx;
    right: 0;
    z-index: 12;
  }
}
</style>
