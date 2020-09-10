<template>
	<view>
			<wyb-drop-down ref="dropDown" :options="options" @select="onModeSelect">
			</wyb-drop-down>
			<view class="record-container">
				<view class="status" style="font-size:30rpx;padding-top:100rpx;padding-bottom:25rpx;font-weight: bord;font-size: 30rpx;">录音中{{intIntervalTime}}s</view>
			</view>
			<view class="progress-box">
				<u-line-progress style="width:80%" round="true" height="35rpx" active-color="#F49255" :show-percent="false" :striped-active="true" :striped="true" :percent="intIntervalTime*100/30"></u-line-progress>
			</view>
			<button class="button-normal" style="margin-top:60rpx;color:#fefefe" @tap="uploadMP3">丢出瓶子</button>
			<button class="button-normal" style="color:#fefefe" @tap="playVoice">播放录音</button>
			<button class="record-button"  @touchstart="startRecord" @touchend="endRecord">按住说话,松开结束</button>
		</view>
</template>

<script>
	const recorderManager = uni.getRecorderManager();
	const innerAudioContext = uni.createInnerAudioContext();
	innerAudioContext.autoplay = true;
	
	import wybDropDown from '@/components/wyb-drop-down/wyb-drop-down.vue'
	export default {
		components: {
			wybDropDown
		},
		data() {
			return {
				text: "uni-app",
				voicePath: "",
				isRecord: false, // 记录状态,录音中或者是未开始
				intervalTime: 0,
				timer: null,
				chooseMode: "CodingMode",
				id:this.$store.state.vuex_wx_openid.toString(),
				fileID:"",
				options: [{
				    header: '选择一个最适合聆听的心情吧~',
				    // contents在自定义开启时可以不用传
				    contents: ['代码ING', '有点孤独', '伤心忧郁', '迷茫欸',"爆肝中"],
				}],
			}
		},
		onLoad() {
			// #ifdef MP-WEIXIN
			wx.setInnerAudioOption({
			    mixWithOther: true,
			    obeyMuteSwitch: false,
			});
			// #endif
		    let that = this;
		    recorderManager.onStop(function(res) {
				console.log("录音停止了" + JSON.stringify(res)) //返回录音的临时保存地址, 可用于后面的播放
				that.voicePath = res.tempFilePath
		    });
		},
		computed: {
		    intIntervalTime() {
				let that = this;
				// 用于显示整数的秒数
				console.log(Math.round(that.intervalTime));
				return Math.round(that.intervalTime);
		    }
		},
		methods: {
			startRecord() {
				let that = this;
			    that.timer = setInterval(() => {
			        that.intervalTime += 0.5;
			        if (that.intervalTime >= 0.5 && !that.isRecord) {
						console.log("开始录音");
						that.isRecord = true;
						that.intervalTime = 0;
						recorderManager.start({
							duration: 30000,
							format: "mp3"
						});
			        }
			    }, 500);
			},
			endRecord() {
				let that = this;
			    if (that.intervalTime <= 0.5) {
					console.log("录音太短了!");
			    }
				clearInterval(that.timer);
			    if (that.isRecord) {
					setTimeout(() => {
						recorderManager.stop();
						that.isRecord = false;
						console.log(that.isRecord);
			        }, 500); //延迟小段时间停止录音, 更好的体验
			    }
			},
			playVoice() {
				let that = this;
			    console.log("播放录音");
			    if (that.voicePath) {
			        innerAudioContext.src = that.voicePath;
			        innerAudioContext.play();
					console.log('成功播放')
			    }
			},
			onModeSelect(res){
				let that = this;
				that.index=res.contentIndex
				if(res.contentIndex == 0){
					that.chooseMode="CodingMode"
				}
				if(res.contentIndex == 1){
					that.chooseMode="LonelyMode"
				}
				if(res.contentIndex == 2){
					that.chooseMode="UpsetMode"
				}
				if(res.contentIndex == 3){
					that.chooseMode="ConfuseMode"
				}
				if(res.contentIndex == 4){
					that.chooseMode="HardWorkingMode"
				}
				console.log(that.chooseMode)
			},
			uploadMP3(){
				let that = this;
				uni.showLoading({
					title: '上传中...',
				})
				uniCloud.uploadFile({
					filePath: that.voicePath,
					cloudPath: that.id+'-words.mp3',
				}).then(res => {
					console.log(res)
					that.fileID = res.fileID
					console.log(res.fileID)
					uniCloud.callFunction({
						name:'UploadtoDifferentModes',
						data: {
							"Mode": that.chooseMode,
							"fileID": that.fileID,
							"userID": that.id,
							"index": that.index
						}
					}).then(res => {
						console.log(res)
					}).catch(error => {
						console.log(error)
					})
				}).catch(error => {
					console.log(error)
				})
				uni.hideLoading()
				uni.showModal({
					title: "上传成功！",
					content: "感谢你的分享~"
				})
			}
		}
	}
</script>

<style lang="scss">
.record-container {
		padding-top: 100upx;
	}

	.status {
		text-align: center;
	}

	.uni-list-cell {
		display: float;
		justify-content: flex-start;
		margin-left: 60rpx;
		margin-right: 60rpx;
		text-align: center;
	}
	
	.check-wrap {
		.header {
			font-size: 50rpx;
			margin-top: 80rpx;
			margin-bottom: 30rpx;
			font-weight: bold;
		}
	}
	
	.record-button {
		font-size: 50rpx;
		width: 60%;
		margin-top: 50rpx;
		margin-bottom: 60rpx;
		color:#fefefe;
		
		height: 60px;
		background: linear-gradient(to bottom, #fbaf51 0%, #F49255 100%);
		/* W3C */
		border: none;
		border-radius: 100px;
		position: relative;
		border-bottom: 8px solid #e76c3c;
			
		padding: 0px 0px 60px 0px;
		font-weight: 600;
		font-family: 'Open Sans', sans-serif;
		text-shadow: 5px 3px 2px rgba(0, 0, 0, .4);
		font-size: 25px;
		text-align: middle;
		text-indent: -5px;
		box-shadow: 3px 3px 3px 0px rgba(0, 0, 0, .2);
		cursor: pointer;
			
	}

</style>
