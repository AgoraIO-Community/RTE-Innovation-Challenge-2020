<!--  -->
<template>
  <div style="height:100%">
    <img id="downImg" :src="dataurl" alt="">
    <div id="nodeBox" slot="content" class="content-body">
      <div class="body-box">
        <vue-qr
          :text="downloadData.url"
          :margin="0"
          color-dark="#000"
          color-light="#fff"
          :logo-src="downloadData.icon"
          :logo-scale="0.3"
          :size="200"
        />
      </div>
    </div>
  </div>
</template>

<script>
import html2canvas from 'html2canvas'
export default {
  data() {
    return {

    }
  },
  // 方法集合
  methods: {
    //   截取图片
    setImage() {
      const that = this
      var canvas2 = document.createElement('canvas')
      // let _canvas = document.getElementById('child');
      const _canvas = document.getElementById('nodeBox')
      var w = parseInt(window.getComputedStyle(_canvas).width)
      var h = parseInt(window.getComputedStyle(_canvas).height)
      // 将canvas画布放大若干倍，然后盛放在较小的容器内，就显得不模糊了
      canvas2.width = w * 4
      canvas2.height = h * 4
      canvas2.style.width = w + 'px'
      canvas2.style.height = h + 'px'

      // 可以按照自己的需求，对context的参数修改,translate指的是偏移量
      var context = canvas2.getContext('2d')
      context.scale(2, 2)
      html2canvas(document.getElementById('nodeBox'), {
        canvas: canvas2
      }).then(function(canvas) {
        var blob = that.getBlob(canvas)
        var oMyForm = new FormData()
        var fileName = 'mobile' + '.jpg'
        oMyForm.append('file', blob, fileName)
        //              oMyForm.append("fileName", fileName);
        oMyForm.append('fileType', 'image')
        oMyForm.append('user_id', that)
      })
    },
    getBlob(canvas) { // 获取blob对象
      var data = canvas.toDataURL('image/jpeg', 1)
      this.dataurl = data
      console.log(this.dataurl)
      data = data.split(',')[1]
      data = window.atob(data)
      var ia = new Uint8Array(data.length)
      for (var i = 0; i < data.length; i++) {
        ia[i] = data.charCodeAt(i)
      }
      return new Blob([ia], {
        type: 'image/jpeg'
      })
    }
  }
}
</script>
<style lang='scss' scoped>
</style>
