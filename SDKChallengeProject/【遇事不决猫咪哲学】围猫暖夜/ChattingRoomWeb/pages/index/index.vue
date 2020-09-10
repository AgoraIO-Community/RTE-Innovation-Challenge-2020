<template>
	<view>
		<view class="hello">
			<view class="img-box">
				<image src="../../static/WebsiteBG.jpg" mode="aspectFill"></image>
			</view>
			<view class='agora-box'>
				<view class="agora-input">
					<view class="text-agora">输入私语号</view>
					<view class="code-input-box">
						<input v-model="option.channel" placeholder="Channel Name" clearable style="border-radius:50rpx;border:5rpx solid #F49255;width: 500rpx;margin-left:125rpx"></el-input>
					</view>
				</view>
			</view>	
			<view class="agora-button">
				<button type="default" class="joinBtn" @tap="joinEvent" :disabled='showJoin' hover-class="hover">join</button>
				<button type="default" class="leaveBtn" @tap='leaveEvent' plain :disabled='!showJoin' hover-class="hover">leave</button>
			</view>
			<view class="uni-list">
				<view class="uni-list-cell uni-list-cell-pd">
				    <view class="uni-list-cell-db">是否开启音频</view>
				    <switch :checked="status.audio" @change="ChangeAudioStatus" color="#F49255" />
				</view>
				<view class="uni-list-cell uni-list-cell-pd">
				    <view class="uni-list-cell-db">是否开启视频</view>
				    <switch  :checked="status.video" @change="ChangeVideoStatus" color="#F49255"/>
				</view>
			</view>
			<view class="agora-view">
				<view class="agora-video1">
					<StreamPlayer :stream="localStream" :domId="localStream.getId()" v-if="localStream"></StreamPlayer>
				</view>
				<view class="agora-video2" :key="index" v-for="(remoteStream, index) in remoteStreams">
					<StreamPlayer :stream="remoteStream" :domId="remoteStream.getId()"></StreamPlayer>
				</view>
			</view>
		</view>   
	</view>
</template>

<script>
import jpCoded from '@/components/jp-coded/jp-coded.vue'
import RTCClient from "@/utils/agora-rtc-client.js";
import StreamPlayer from "@/components/stream-player/stream-player.vue";
import { log } from '@/utils/utils.js'

export default {
  components: {
    StreamPlayer
  },
  data () {
    return {
      option: {
        appid: '',
        token: '',
        uid: null,
        channel: '',
      },
	  status: {
		audio:false,
		video:false,
	  },
      showJoin: false,
      localStream: null,
      remoteStreams: [],
    }
  },
  props: {
    msg: String
  },
  methods: {
    joinEvent () {
      if(!this.option.appid) {
        this.judge('Appid')
        return
      }
      if(!this.option.channel) {
        this.judge('Channel Name')
        return
      }
      this.rtc.joinChannel(this.option).then(() => {
        this.$message({
          message: 'Join Success',
          type: 'success'
        });
        this.rtc.publishStream().then(() => {
          this.$message({
            message: 'Publish Success',
            type: 'success'
          });
          this.localStream = this.rtc.localStream;
		  this.localStream.muteAudio();
		  this.localStream.muteVideo();
        }).catch((err) => {
          this.$message.error('Publish Failure');
          log('publish local error', err)
        })
      }).catch((err) => {
        this.$message.error('Join Failure');
        log('join channel error', err)
      })
      this.showJoin = true
    },
	ChangeAudioStatus(){
		if(!this.status.audio){
			this.localStream.unmuteAudio();
			log('Unmute Audio success');
		}
		if(this.status.audio){
			this.localStream.muteAudio();
			log('Mute Audio success');
		}
		this.status.audio = !this.status.audio;
	},
	ChangeVideoStatus(){
		if(!this.status.video){
			this.localStream.unmuteVideo();
			log('Unmute Video success');
		}
		if(this.status.video){
			this.localStream.muteVideo();
			log('Mute Video success');
		}
		this.status.video = !this.status.video;
	},
    leaveEvent () {
      this.showJoin = false
      this.rtc.leaveChannel().then(() => {
        this.$message({
          message: 'Leave Success',
          type: 'success'
        });
      }).catch((err) => {
        this.$message.error('Leave Failure')
        log('leave error', err)
      })
      this.localStream = null
      this.remoteStreams = []
    },
    judge(detail) {
      this.$notify({
        title: 'Notice',
        message: `Please enter the ${detail}`,
        position: 'top-left',
        type: 'warning'
      });
    },
  },
  created() {
    this.rtc = new RTCClient()
    let rtc = this.rtc
    rtc.on('stream-added', (evt) => {
      let {stream} = evt
      log("[agora] [stream-added] stream-added", stream.getId())
      rtc.client.subscribe(stream)
    })
      
    rtc.on('stream-subscribed', (evt) => {
      let {stream} = evt
      log("[agora] [stream-subscribed] stream-added", stream.getId())
      if (!this.remoteStreams.find((it) => it.getId() === stream.getId())) {
        this.remoteStreams.push(stream)
      }
    })

    rtc.on('stream-removed', (evt) => {
      let {stream} = evt
      log('[agora] [stream-removed] stream-removed', stream.getId())
      this.remoteStreams = this.remoteStreams.filter((it) => it.getId() !== stream.getId())
    }) 
  }
 }
</script>


<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss">
  .code-input-box{
  	display:flex;
  	flex-direction: column;
  	width:750rpx;
  	height:60rpx;
  	margin-top:10rpx;
  }
  .img-box{
	padding-top: 25rpx;
	width:100%;
	height:200rpx;
	text-align: center;
	border-radius: 100rpx;
	margin:auto;
	image {
      width:680rpx;
	  height:200rpx;
	  margin-left:35rpx;
	  margin-right:35rpx;
      border-radius: 100rpx;
	}
  }
  .agora-box {
	  padding-top:10rpx;
  }
  .agora-title {
	padding-top:15rpx;
    font-family: Avenir, Helvetica, Arial, sans-serif;
    font-size: 32px;
    font-weight: bold;
    text-align: center;
    color: #303133;
  }
  .agora-view {
	width:700rpx;
	height:700rpx;
    display: flex;
  }
  .agora-video1 {
	margin-top: 50rpx;
    width: 330rpx;
    height: 280rpx;
	margin-left:30rpx;
  }
  .agora-video2 {
  	margin-top: 50rpx;
    width: 330rpx;
    height: 280rpx;
	margin-left:30rpx;
  }
  .agora-input {
    margin-top: 5rpx;
	text-align: center;
	margin:auto;
  }
  .agora-text {
	margin-top: 5rpx;
    font-size: 35rpx;
	text-align: center;
	font-weight: bold;
  }
  .agora-button {
	margin-top: 10rpx;
	text-align: center;
	font-size: 10rpx;
	font-weight: bold;
	padding-bottom:18rpx;
  }
  .text-agora {
	margin-top:12px;
    font-weight: bold;
	margin-bottom:12px;
  }
  .joinBtn {
	width: 200rpx;
	height: 50rpx; 
	margin-top: 0rpx;
	border-radius: 100rpx;
	font-size: 20rpx;
	color:"#fff";
	font-weight: bold;
	background: linear-gradient(to bottom right, #F8B33A, #F49255);
	margin-left:150rpx;
	float:left;
	margin-bottom:18rpx;
  }
  .leaveBtn { 
	width: 200rpx;
	height: 50rpx; 
    font-size: 20rpx;
    font-weight: bold;
	margin-top: 0rpx;
	color:"#fff";
	border-radius: 100rpx;
	margin-right:150rpx;
	background: linear-gradient(to bottom right, #F8B33A, #F49255);
	float:right;
	margin-bottom:18rpx;
  }	
.hover {
  background: linear-gradient(to bottom right, #F49255, #FB5655);
}
</style>
