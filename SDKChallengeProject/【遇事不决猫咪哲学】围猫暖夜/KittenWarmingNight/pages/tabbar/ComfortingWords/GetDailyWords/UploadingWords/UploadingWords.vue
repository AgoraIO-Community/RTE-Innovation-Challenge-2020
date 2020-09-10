<template>
	<view>
		<view class="tips">
			<view>
				分享你最爱的一句话心灵慰藉吧~
			</view>
			<view>
				注意不要超过36个字噢~
			</view>
		</view>
		<view class="letter">
			<image :src="image" mode="scaleToFill"></image>
			<textarea v-model="wordsContent" style="margin-top:120rpx;margin-left:10rpx;width:460rpx;height:700rpx;;overflow:scroll;resize:none;" :maxlength="maxlength" placeholder="最近你最喜欢哪一句心灵鸡汤呀？" />
		</view>
		<button class="button-normal" style="width:500rpx;margin-top:70rpx;color: #fefefe" @tap="upload">上传我的心灵慰藉</button>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				id: this.$store.state.vuex_wx_openid.toString(),
				image: "https://vkceyugu.cdn.bspapp.com/VKCEYUGU-wangshanyw/f2cf3470-ea8f-11ea-b244-a9f5e5565f30.png",
				maxlength: 36,
				wordsContent:""
			}
		},
		methods: {
			upload(){
				uni.showLoading({
					title: '上传中...',
				})
				uniCloud.callFunction({
					name:"UploadDailyWords",
					data:{
						"userID":this.id,
						"content": this.wordsContent,
					},
					success: res => {
					    console.log(res.result)
					    this.wordsContent=""
					}
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
.tips{
	margin-top:30rpx;
	font-size:40rpx;
	font-weight: bolder;
	text-align: center;
}
.letter {
	display: flex;
	padding-top: 40rpx;
	height: 850rpx;
	padding-left: 40rpx;
	padding-right: 40rpx;
	justify-content: center;
	image {
		position: relative;
		height: 100%;
		width: 100%;
	}
	textarea {
		justify-content: center;
		position:absolute;
		padding-top: 20rpx;
		padding-left: 20rpx;
		padding-right: 20rpx;
		padding-bottom: 20rpx;
	}
}
.btn {
	margin-bottom: 35rpx;
	justify-items: center;
	width: 500rpx;
	height: 100rpx; 
	background: #FFC53D;
	border-radius: 10rpx;
}
</style>
