<template>
  <div>
    <user-top-menu></user-top-menu>
    <div>
      <div class="video-grid" id="video">
        <div class="teacher-container">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>老师：</span>
            </div>
            <div id="teacher_video_123456" class="video-placeholder" style="width: 900px;height: 900px">
            </div>
          </el-card>
        </div>
        <div class="video-view">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>学生</span>
            </div>
            <div>
              <el-row type="flex" class="row-bg" justify="space-around">
                <el-col :span="6" v-for="(item) in userList" :key="item.id">
                  <div :id="'remote_video_'+(item.user_id)" class="video-placeholder">

                  </div>
                  <p>
                    {{item.user.account}}
                  </p>
                </el-col>
              </el-row>
            </div>
          </el-card>
        </div>
        <el-row style="margin-top: 15px">
          <el-button @click="join">进入教室</el-button>
          <el-button type="danger" @click="leave">离开教室</el-button>
        </el-row>
      </div>
    </div>
    <div>
    </div>
  </div>
</template>
<script>

import { enterLiveRoom } from '../../utils/api'
import UserTopMenu from '../../components/UserTopMenu'
import AgoraRTC from 'agora-rtc-sdk'

export default {
  name: 'classShow',
  components: { UserTopMenu },
  mounted () {
    // const APP_ID = 'ca6347f04b994c7a9428590b5786466c'
    // const Token = '006ca6347f04b994c7a9428590b5786466cIABHnwxtOomIqP0QRrCFicRDGJUbiUZk8Ecp4C9XEiG1F/DKyEEAAAAAEACPKt3urWFaXwEAAQCtYVpf'

    this.class_id = this.$route.query.class_id
    this.live_id = this.$route.query.live_id
    this.initData()
  },
  data () {
    return {
      liveStart: false,
      class_id: 0,
      userList: [],
      liveInfo: {},
      roomInfo: {},
      userInfo: {},
      param: {
        page: 1,
        rows: 10
      },
      classInfo: {},
      liveList: [],
      clientConfig: {
        mode: 'live',
        codec: 'h264'
      },
      rtc: {
        client: null,
        joined: false,
        published: false,
        localStream: null,
        remoteStreams: [],
        params: {}
      },
      params: {
        appId: 'ca6347f04b994c7a9428590b5786466c',
        accountName: 'Ivan',
        uid: '',
        token: '006ca6347f04b994c7a9428590b5786466cIABeYZZsf+wz66jrLIbusU4vCs/O+6HQX860TUnSERNNBRV2prIAAAAAEACPKt3uTDlbXwEAAQBLOVtf',
        channelName: '' // 频道就是房间号roomId
      },
      localAudioTrack: null,
      localVideoTrack: null
    }
  },
  methods: {
    initData () {
      const param = {
        class_id: this.class_id,
        live_id: this.live_id
      }
      const that = this
      enterLiveRoom(param).then((res) => {
        that.userInfo = res.data.data.userInfo
        that.roomInfo = res.data.data.liveRoomInfo
        that.liveInfo = res.data.data.liveInfo
        that.userList = res.data.data.roomUsers
        that.params.channelName = res.data.data.liveRoomInfo.room_name
        that.params.uid = res.data.data.userInfo.id
        that.params.accountName = res.data.data.userInfo.account
        that.init()
      })
    },
    init () {
      this.rtc.client = AgoraRTC.createClient(this.clientConfig)
      this.handleEvents()
      AgoraRTC.getDevices(function (items) {
        console.log(items)
      })
    },
    handleEvents () {
      this.rtc.client.on('error', (err) => {
        this.$message.error(err)
      })

      this.rtc.client.on('stream-published', function (evt) {
      })
      const that = this
      this.rtc.client.on('stream-added', function (evt) {
        const remoteStream = evt.stream
        const id = remoteStream.getId()
        console.log('stream-added' + id)
        if (id !== that.params.uid) {
          that.rtc.client.subscribe(remoteStream, function (err) {
            console.log('stream subscribe failed', err)
          })
        }
        console.log('stream-added remote-uid:' + id)
      })

      this.rtc.client.on('stream-subscribed', function (evt) {
        const remoteStream = evt.stream
        const id = remoteStream.getId()
        console.log('===============')
        console.log(id)
        console.log(remoteStream)
        console.log('===============')
        // this.rtc.remoteStreams.push(remoteStream)
        // addView(id)
        // remoteStream.play('remote_video_' + id)
        remoteStream.play('teacher_video_123456')
        // Toast.info("stream-subscribed remote-uid: " + id)
        // console.log("stream-subscribed remote-uid: ", id)
      })

      this.rtc.client.on('stream-removed', function (evt) {
        const remoteStream = evt.stream
        const id = remoteStream.getId()
        if (remoteStream.isPlaying()) {
          remoteStream.stop()
        }
        this.rtc.remoteStreams = this.rtc.remoteStreams.filter(function (stream) {
          return stream.getId() !== id
        })
        // removeView(id)
        // console.log("stream-removed remote-uid: ", id)
      })
    },
    join () {
      this.rtc.client.init(this.params.appId, () => {
        this.rtc.client.join(this.params.token, this.params.channelName, this.params.uid, (uid) => {
          this.rtc.joined = true
          this.rtc.localStream = AgoraRTC.createStream({
            streamID: uid,
            audio: true,
            video: true,
            screen: false,
            microphoneId: '',
            cameraId: ''
          })
          const that = this
          this.rtc.localStream.init(function () {
            that.rtc.localStream.play('remote_video_' + uid)
            that.publish()
          })
        })
      })
    },
    publish () {
      if (this.rtc.published) {
        this.$message.error('您已进入课堂')
        return
      }
      const oldState = this.rtc.published

      this.rtc.client.publish(this.rtc.localStream, function (err) {
        this.rtc.published = oldState
        this.$message.error(err)
      })
      this.rtc.published = true
    },
    unpublish () {

    },
    leave () {
      const that = this
      this.rtc.client.leave(function () {
        if (that.rtc.localStream.isPlaying()) {
          that.rtc.localStream.stop()
        }
        // close stream
        that.rtc.localStream.close()
      })
    }
  }
}
</script>

<style>
  .course-container {
    width: 960px;
    margin: 30px auto;
  }

  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }

  .clearfix:after {
    clear: both
  }

  .text {
    font-size: 14px;
    text-align: left;
  }

  .item {
    padding: 18px 0;
  }

  /*.box-card {*/
  /*  width: 960px;*/
  /*}*/

  #video {
    margin: 15px auto;
    width: 960px;
  }

  .video-placeholder {
    height: 200px;
    border: 1px solid #eee;
    box-shadow: 0 0 5px #909399;
  }

  .video-placeholder video {
    left: 0;
    top: 0;
  }
</style>
