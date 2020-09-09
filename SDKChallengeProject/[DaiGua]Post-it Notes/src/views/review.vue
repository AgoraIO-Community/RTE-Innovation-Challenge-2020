
<template>
  <div class="col-direction" style="height:100%;overflow:hidden;">
    <p class="logo pramirytext blod col-center"> <i class="el-icon-back" @click="back" /> 课堂回顾</p>
    <div class="row-direction contentBox">
      <div class="flex2 ">
        <video
          id="my-video"
          class="video-js vjs-default-skin box"
          controls
          preload="auto"
        >
          <!--  src="https://videos8.jsyunbf.com/20190717/s6DaVnKb/index.m3u8" -->
          <!-- src="https://cmz-daigua.oss-cn-beijing.aliyuncs.com/3b7c84e64d4d12f5bc3304a3fa91ebe9_123123123.m3u8" -->
          <source
            :src="'https://cmz-daigua.oss-cn-beijing.aliyuncs.com/' + classInfo.video"
            type="application/x-mpegURL"
          >
        </video>
      </div>
      <div class="flex1">
        <el-timeline>
          <el-timeline-item v-for="(note,index) in noteList" :key="index" :timestamp="note.timeForm" placement="top" class="noteBox">
            <el-card>
              <!-- <div id="abcd" v-viewer style="width:90%;" class="images"> <img :src="note.note"> </div> -->
              <img style="width:90%;" class="imgContent" :src="note.note" @click="goThisTime(note.time)" @dblclick="openImgHandle(note.note)">
            </el-card>
          </el-timeline-item>

        </el-timeline>
      </div>
    </div>
    <bigPic v-if="bigPicVisi" :img-src="nowImg" @clickit="bigPicVisi = false" />
  </div>
</template>
<script>
import bigPic from '@/components/bigPic'
import { getUserNote } from '@/api/local.js'
import { formatDate } from '@/utils/time.js'
import videojs from 'video.js'
import 'videojs-contrib-hls'
import 'video.js/dist/video-js.css'
export default {
  components: {
    bigPic
  },
  data() {
    return {
      nowImg: '',
      bigPicVisi: false,
      myPlayer: null,
      noteList: [],
      classInfo: JSON.parse(this.$route.query.roomInfo)
    }
  },
  mounted() {
    this.myPlayer = videojs('my-video', {
      fluid: true,
      playbackRates: [0.5, 1.0, 1.5, 2.0]
    })
    videojs('my-video',
      function() {
        this.myPlayer.play()
      })
    this.getUserNoteHandle()
  },
  methods: {
    back() {
      this.$router.replace({ path: '/historyClass', query: { uid: this.$route.query.uid, role: this.$route.query.role }})
    },
    // 放大图片
    openImgHandle(src) {
      this.nowImg = src
      this.bigPicVisi = true
    },
    // 跳转到某事件点进行播放
    goThisTime(time) {
      const timeTemp = parseInt(time) - parseInt(this.classInfo.time) - 12500
      console.log(' @click="goThisTime"', timeTemp, parseInt(time), parseInt(this.classInfo.time))
      this.myPlayer.currentTime(timeTemp / 1000)
    },
    // 获取用户笔记
    getUserNoteHandle() {
      getUserNote(this.classInfo.rid, this.$route.query.uid).then(
        res => {
          console.log('res', res)
          this.noteList = res.data
          this.noteList.forEach(note => {
            note.timeForm = formatDate(+note.time)
          })
        }
      )
    }
  }
}
</script>

<style lang='scss' scoped>
.flex2{
  display: flex;
  flex:2;
}

.logo{
    text-align: start;
    font-size: 20px;
    margin:20px ;
    position: absolute;
    top:0;
    left: 0;
    z-index:9999;
    .el-icon-back{
      font-size: 30px;
      color: #fff;
      margin-right: 30px;
    }
}
.logoen{
    text-align: start;
    font-size: 16px;
    margin:-0 80px;
}
.contentBox{
height:100%;
}
.box {
    width: 100%;
    height: 100%;
    /deep/ .vjs-big-play-button{
      margin:400px 500px;
    }
}
.el-timeline{
  width:100%;
  height:100%;
  overflow: auto;
  padding: 20px;
  /deep/ .el-timeline-item__timestamp{
    text-align: start;
  }
}

</style>
