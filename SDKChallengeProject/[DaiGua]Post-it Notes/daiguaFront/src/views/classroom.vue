<template>
  <div class="home row-direction agora-theme">
    <div class="triangle-left" />
    <span class="name-tag">{{ $route.query.rname }}</span>
    <div v-show="!searchWordVisi" class="button-box" style="margin: 0">
      <div class="col-direction">
        <!-- <button id="join" class="btn btn-raised btn-primary waves-effect waves-light" @click="joinHandle">JOIN</button>
        <button id="leave" class="btn btn-raised btn-primary waves-effect waves-light" @click="leaveHandle">LEAVE</button>

        <button id="unpublish" class="btn btn-raised btn-primary waves-effect waves-light" @click="unpublishHandle">UNPUBLISH</button>
        <button v-show="!sketchpadShow" class="btn btn-raised btn-primary waves-effect waves-light" @click="openSketchpad">记笔记</button>
        <button v-show="sketchpadShow" class="btn btn-raised btn-primary waves-effect waves-light" @click="recoverSketchpad">保存</button>
        <button class="btn btn-raised btn-primary waves-effect waves-light" @click="searchWord">查词</button> -->
        <!-- <button class="btn btn-raised btn-primary waves-effect waves-light" @click="startAgoraRecord">开始录制</button>
        <button class="btn btn-raised btn-primary waves-effect waves-light" @click="stopRecord">停止录制</button>
        <button class="btn btn-raised btn-primary waves-effect waves-light" @click="queryAgoraHandle">查询录制</button> -->
        <!-- <button id="publish" class="btn btn-raised btn-primary waves-effect waves-light" @click="publishHandle">PUBLISH</button> -->
        <!-- <button class="btn btn-raised btn-primary waves-effect waves-light" @click="stopTest">结束</button>
        <button class="btn btn-raised btn-primary waves-effect waves-light" @click="startTest">开始</button> -->
        <img v-if="$route.query.role==='2'" class="icon-button" src="@/assets/note.png" @click="openSketchpad">
        <img v-if="$route.query.role==='2'" class="icon-button" src="@/assets/searchWord.png" @click="searchWord">
        <!-- <img v-if="$route.query.role==='1'" class="icon-button" src="@/assets/record.png" @click="startAgoraRecord"> -->
        <!-- <img v-if="$route.query.role==='1'" class="icon-button" src="@/assets/stopRecord.png" @click="stopRecord"> -->
        <img v-if="$route.query.role==='1'&&!shareScreenVisi" class="icon-button" src="@/assets/shareScreen.png" @click="createScreenStreamHandle">
        <img v-if="$route.query.role==='1'&&shareScreenVisi" class="icon-button" src="@/assets/stopShare.png" @click="stopScreenStreamHandle">
        <i class="icon-button el-icon-phone" @click="leaveHandle" />
      </div>
    </div>

    <div id="video" :class="[searchWordVisi||sketchpadShow?'remote-video-box-small':'remote-video-box']" @click="videoBox">

      <div v-if="$route.query.role==='2'" id="remote_video_panel_" ref="remote_video_panel_" class="video-view" @click="recoverSketchpad">
        <div id="remote_video_" class="video-placeholder" />
        <div id="remote_video_info_" :class="[show ?'video-profile':'video-profilehide hide']" />
        <div id="video_autoplay_" class="autoplay-fallback hide" />
      </div>
      <div v-if="$route.query.role==='1'" class="video-view">
        <div id="local_stream" class="video-placeholder" />
        <div id="local_video_info" class="video-profile hide" />
        <div id="video_autoplay_local" class="autoplay-fallback hide" />
      </div>
    </div>

    <div v-if="sketchpadShow" class="sketchpad-box">
      <sketchpad ref="sketchpad" :room="$route.query.room" :uid="$route.query.uid" :back-img="nowImg" :save-img="savaImg" @save-note="recoverSketchpad" />

    </div>
    <canvas v-show="false" id="videoCanvas" :width="videoCanvasWidth" :height="videoCanvasHeight" />

    <screen-shot v-if="searchWordVisi" :img-url="nowImg" :img-arr="picArrUrl" @screen-shot-finish="screenShotFinishHandle" @sreen="sreenHandle" />
    <!-- <img :src="nowImg"> -->
    <div v-show="!sketchpadShow" class="button-box-right">
      <div v-if="$route.query.role==='2'" class="col-direction center cursor" @click="seeNoteHandle">
        <i class="icon-button el-icon-right" />
        <span style="color:#fff;">查看</span>
        <span style="color:#fff;">笔记</span>
      </div>
    </div>
    <el-drawer
      v-if="drawer"
      title="已记笔记"
      :visible.sync="drawer"
      :with-header="false"
    >
      <note v-if="drawer" ref="noteShow" />
    </el-drawer>

  </div>
</template>

<script>
import note from './noteDrawer'
import sketchpad from './Sketchpad'
// import '@/utils/vendor/jquery.min.js'
import M from '@/utils/vendor/materialize.min.js'
import videoMixin from '@/mixin/videoMixin.js'
import superVideo from '@/mixin/superVideo.js'
import screenShot from './screenShot'
import AgoraRTC from 'agora-rtc-sdk'
// import { image } from 'html2canvas/dist/types/css/types/image'
import { acquireAgora, startAgora, stopAgora, queryAgora } from '@/api/agora.js'
import { getToken, uploadVideo } from '@/api/local.js'
export default {
  name: 'Home',
  components: {
    sketchpad,
    screenShot,
    note
  },
  mixins: [videoMixin, superVideo],
  data() {
    return {
      drawer: false,
      shareScreenVisi: false,
      microList: null,
      resourceId: '',
      startId: '',
      bigPicUrl: '', // 截图搜索要用到的图
      searchWordVisi: false, // 截图搜索要用到的图是否显示
      imgTemp: '',
      savaImg: false, // 保存图片
      videoCanvasWidth: window.innerHeight * 1.8, // 截图canvas的宽高
      // videoCanvasWidth: window.innerWidth,
      videoCanvasHeight: window.innerHeight,
      nowImg: '', // 当前截图
      AgoraRTC,
      screenShotVisible: false, // 截图插件是否显示
      sketchpadShow: false, // 画板是否显示
      microphoneId: '',

      cameraId: '',
      videoList: [],
      show: false,
      idlist: [],
      cameraResolution: '',
      resolutionList: [],
      resolutions: [
        {
          name: 'default',
          value: 'default'
        },
        {
          name: '480p',
          value: '480p'
        },
        {
          name: '720p',
          value: '720p'
        },
        {
          name: '1080p',
          value: '1080p'
        }
      ],
      fields: ['appID', 'channel'],
      joined: false, // 分享屏幕
      rtc: {
        client: null,
        joined: false,
        published: false,
        localStream: null,
        screenLocalStream: null,
        remoteStreams: [],
        params: {}
      },
      superrtc: {
        client: null,
        joined: false,
        published: false,
        localStream: null,
        screenLocalStream: null,
        remoteStreams: [],
        params: {}
      },
      appID: process.env.VUE_APP_APP_ID,
      channel: this.$route.query.room,
      token: '006958756c9151b47e385a3a021ed29cb3bIADuzwmsXqUlI4+dkOjf27k1UBYZ7C5VOrdVZfYO9AxC1JH5cAAAAAAAEABfAtRhWKZHXwEAAQBXpkdf',
      uid: 527841,
      supertoken: null,
      superuid: '9999',
      codec: 'vp8',
      mode: 'rtc'

    }
  },
  mounted() {
    this.uid = this.$route.query.uid
    // console.log('agora sdk version: ' + this.AgoraRTC.VERSION + ' compatible: ' + this.AgoraRTC.checkSystemRequirements())
    // This will fetch all the devices and will populate the UI for every device. (Audio and Video)
    console.log('this.uid', this.uid)
    this.getDevices(function(devices) {
      devices.audios.forEach(function(audio) {
        this.microList = audio
      })
      devices.videos.forEach(function(video) {
        this.videoList = video
      })
      // To populate UI with different camera resolutions
      this.resolutions.forEach(function(resolution) {
        this.resolutionList = resolution
      })
      M.AutoInit()
    })
    // 开始会议
    this.getTokenHandle()
    // This will start the join functions with all the configuration selected by the user.
    // this.joinHandle()
  },
  methods: {
    // 查看笔记
    seeNoteHandle() {
      this.drawer = true
    },
    // 获取token
    getTokenHandle() {
      getToken(this.$route.query.uid, this.$route.query.room).then(
        res => {
          this.token = res.token
          // 开始会议
          this.joinHandle()
        }
      )
    },
    // 获取token
    getsuperTokenHandle() {
      getToken(this.superuid, this.$route.query.room).then(
        res => {
          this.supertoken = res.token
          // 开始会议
          this.joinsuperHandle()
        }
      )
    },
    sreenHandle() {
      console.log('jjj')
    },
    // 点击视频窗口
    videoBox() {
      if (this.searchWordVisi) {
        // 查词
      } else if (this.sketchpadShow) {
        // 记笔记
        this.$refs.sketchpad.noteUpdateHandle()
        this.recoverSketchpad
      }
    },
    openSketchpad() {
      // this.centerDialogVisible = true
      // this.sketchpadShow = true
      // this.savecanvas()
      this.videoShot()
      this.searchWordVisi = false
      this.savaImg = false
      this.sketchpadShow = true
    },
    // 保存笔记
    recoverSketchpad() {
      // this.sketchpadShow = false
      // this.savaImg = true
      // console.log('保存笔记')
      // this.$refs.noteShow.getUserNoteHandle()
      this.sketchpadShow = false
      this.searchWordVisi = false
    },
    // 搜索单词释义，先截图再调截小图
    searchWord() {
      this.searchWordVisi = true
      this.videoShot()
      this.screenShotVisible = true
    },
    // 截图完成
    screenShotFinishHandle() {
      console.log('截图完成')
      this.screenShotVisible = false
      this.searchWordVisi = false
    },
    // // video 截图
    videoShot() {
      const video = document.querySelectorAll('video')[0] // 获取前台要截图的video对象，
      const canvas = document.querySelector('#videoCanvas') // 获取前台的canvas对象，用于作图
      // console.log('const video', canvas, this.videoCanvasWidth, this.videoCanvasHeight)
      const ctx = canvas.getContext('2d') // 设置canvas绘制2d图，
      ctx.drawImage(video, 0, 0, this.videoCanvasWidth, this.videoCanvasHeight)
      const images = canvas.toDataURL('image/png') // canvas的api中的toDataURL()保存图像
      this.nowImg = images
      // console.log('image', images)

      // this.downloadImage(images)
    },
    // 获取录制id
    acquireAgoraHandle() {
      acquireAgora(this.$route.query.room, this.superuid).then(
        res => {
          this.resourceId = res.resourceId
          this.startAgoraRecord()
        }
      )
    },
    // 开始录制
    startAgoraRecord() {
      startAgora(this.resourceId, this.$route.query.room, this.superuid, this.supertoken).then(
        res => {
          console.log('-----开始录制', res)
          this.startId = res.sid
        }
      )
    },
    // 查询录制状态
    queryAgoraHandle() {
      queryAgora(this.resourceId, this.startId).then(
        res => {
          console.log('-----查询录制状态', res)
        }
      )
    },
    // 停止录制
    stopRecord() {
      stopAgora(this.resourceId, this.startId, this.$route.query.room, this.superuid).then(
        res => {
          console.log('-----停止录制', res)
          this.uploadVideoHandle(res.serverResponse.fileList)
        }
      )
    },
    // 上传视频
    uploadVideoHandle(file) {
      uploadVideo(file, this.$route.query.room).then(
        res => {
          this.$router.push({ path: '/home', query: { uid: this.$route.query.uid, role: this.$route.query.role }})
        }
      )
    }
  }

}
</script>
<style lang="scss" >
@import '../utils/assets/common.css';
 .video-placeholder{
  div{
    video{
      width: auto !important;
      position: static !important;
       height: 100%;
       object-fit: fill !important;
      left: 0 !important;
    }
  }
}
</style>
<style lang="scss" scoped>
.home{
  width: 100%;
  height: 100%;
  .triangle-left {
     position: absolute;
  top:10px;
  left: 250px;
    z-index:3;
    width: 0;
    height: 0;
    border-top: 22px solid transparent;
    border-left: 22px solid RGB(156,216,252);
    border-bottom: 22px solid transparent;
}
.name-tag{
  padding: 7px 20px;
  font-size: 20px;
  z-index:3;
  position: absolute;
  top:10px;
  left: 10px;
  width:240px;
  color: #333;
  background: linear-gradient(to right,RGB(47,124,247) ,RGB(156,216,252));

}
 .video-view,  .video-placeholder,  #local_stream, #local_video_info{
    width:100%;
      height:100%;
      // overflow: hidden;
 }
  .remote-video-box{
    background-color: #000;
    width:calc(100% - 140px);
    height:100%;
    margin-left: 70px;
    margin-right:70px;
    position: relative;
    -webkit-transition: all .2s ease;
    -moz-transition:  all .2s ease;
    transition:  all .2s ease;
    .video-view,.video-placeholder{
      width:100%;
      height:100%;
    }
  }
  .remote-video-box-small{
    background-color: #333;
    width:480px;
    height: 273px;
    position: absolute;
    top: 0;
    right: 0;
    z-index: 9999;
    -webkit-transition: all .2s ease;
    -moz-transition:  all .2s ease;
    transition:  all .2s ease;
  }
  .sketchpad-box{
    position: relative;
     width:100%;
    height:100%;
  }
  .button-box{
    padding-top: 60px;
    position: absolute;
    top:0;
    left: 0;
    width: 70px;
    background-color: #333;
    height: 100%;
    display: flex;
    justify-content: center;

    .icon-button{
      width: 30px;
      height: 30px;
      margin:20px auto;
      font-size: 30px;
      color: crimson;
    }
  }
  .button-box-right{
    position: absolute;
    top:0;
    right: 0;
    width: 70px;
    background-color: #333;
    height: 100%;
    display: flex;
    justify-content: center;
    .icon-button{
      width: 30px;
      height: 30px;
      margin:20px auto;
      font-size: 30px;
      color: #fff;
    }
  }
  /deep/ .el-drawer{
    background-color:rgba($color: #ccc, $alpha: 0.8);
    .el-drawer__body {
    flex: 1;
    overflow: auto;
    }
  }
}
</style>
