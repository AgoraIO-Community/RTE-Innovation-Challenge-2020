<template>
	<view class="content">
		<view class="container">
			<view class="container-box">
				<image class="image" :src="image" mode="scaleToFill"></image>
				<button class="button-normal" @tap="open">抽签</button>
				<button class="button-normal" @tap="upload">上传签文</button>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				image: "https://vkceyugu.cdn.bspapp.com/VKCEYUGU-wangshanyw/3fffb060-e948-11ea-9dfb-6da8e309e0d8.png",
			}
		},
		methods: {
			open(){
				uni.navigateTo({
					url: './ExhibitingWords/ExhibitingWords'
				})
			},
			upload(){
				uni.navigateTo({
					url: './UploadingWords/UploadingWords'
				})
			}
		}
	}
</script>

<style lang="scss">
.container {
	padding-top: 40rpx;
	.container-box {
		display: flex;
		flex-direction: column;
		width: 500rpx;
		height: 900rpx;
		margin-left: 125rpx;
		image {
			width: 100%;
			height: 100%;
		}
		button {
			margin-bottom: 60rpx;
		}
	}
}
	
</style>
