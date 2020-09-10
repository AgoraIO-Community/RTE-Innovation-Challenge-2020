<template>
	<view>
		<wyb-drop-down ref="dropDown" :options="options" @select="onModeSelect">
		</wyb-drop-down>
		<image class="image1" :src="image_vocal1" mode="scaleToFill"></image>
		<button class="button-normal" style="margin-top:70rpx;color: #fefefe" type="default" @tap="GetMP3">拾取漂流瓶</button>
		<button class="button-normal" style="margin-top:70rpx;color: #fefefe" type="default" @tap="Playvoice">播放</button>
		<button class="button-normal" style="margin-top:100rpx;color: #fefefe" type="default" @tap="navigateUpload">上传</button>
		<image class="image2" :src="image_vocal2" mode="scaleToFill"></image>
	</view>
</template>

<script>
	const innerAudioContext = uni.createInnerAudioContext();
	innerAudioContext.autoplay = true;
	
	import wybDropDown from '@/components/wyb-drop-down/wyb-drop-down.vue'
	export default {
		components: {
			wybDropDown
		},
		data() {
			return {
				index:0,
				Mp3src:"",
				chooseMode:"CodingMode",
				options: [{
				    header: '选择你现在的心情状态',
				    // contents在自定义开启时可以不用传
				    contents: ['代码ING', '有点孤独', '迷茫欸', '伤心忧郁', "爆肝中"],
				}],
				image_vocal1: "https://vkceyugu.cdn.bspapp.com/VKCEYUGU-wangshanyw/874ee720-ed16-11ea-8a36-ebb87efcf8c0.png",
				image_vocal2: "https://vkceyugu.cdn.bspapp.com/VKCEYUGU-wangshanyw/8e749590-ed16-11ea-b997-9918a5dda011.png"
			};
		},
		methods: {
			navigateUpload(){
				uni.navigateTo({
					url: './uploading/uploading'
				})
			},
			onModeSelect(res){
				this.index=res.contentIndex
				if(res.contentIndex == 0){
					this.chooseMode="CodingMode"
				}
				if(res.contentIndex == 1){
					this.chooseMode="LonelyMode"
				}
				if(res.contentIndex == 2){
					this.chooseMode="ConfuseMode"
				}
				if(res.contentIndex == 3){
					this.chooseMode="UpsetMode"
				}
				if(res.contentIndex == 4){
					this.chooseMode="HardWorkingMode"
				}
				console.log(this.chooseMode)
			},
			GetMP3(){
				// let that = this;
				uniCloud.callFunction({
					name:"RandomGetMP3",
					data: {
						Mode: this.chooseMode,
						index: this.index
					}
				}).then(res => {
					this.Mp3src = res.result.data[0].src
					console.log(res.result.data[0].src)
				}).catch(error => {
					console.log(error)
				})
			},
			Playvoice(){
				if (!this.Mp3src){
					uni.showModal({
						title: "抱歉",
						content: "请先抽取一条私语！"
					})
				}
				else {
					innerAudioContext.src=this.Mp3src
					innerAudioContext.onPlay(() => {
						console.log('开始播放');
					});
					innerAudioContext.onError((res) => {
						console.log(res.errMsg);
						console.log(res.errCode);
					});
				}
			}
		}
	}
</script>

<style lang="scss">
.image1 {
	margin-top:100rpx;
	width:600rpx;
	height:100rpx;
	margin-left: auto; 
	margin-right:auto; 
	display:block;
	opacity: 0.6;
}
.image2 {
	margin-top:60rpx;
	width:600rpx;
	height:450rpx;
	margin-left: auto;
	margin-right:auto; 
	display:block;
	opacity: 0.6;
}
.player{
	float:right;
	margin-right:45rpx;
	width:60rpx;
	height:60rpx;
}
</style>
