<template>
	<view>
		<view class="my-header">
			<view class="my-header__background">
				<open-data type="userAvatarUrl"></open-data>
				<!-- <image src="../../../static/logo.png" mode="aspectFill"></image> -->
			</view>
			<view class="my-header__logo">
				<view class="my-header__logo-box">
					<open-data type="userAvatarUrl"></open-data>
					<!-- <image src="/static/logo.png" mode="aspectFill"></image> -->
				</view>
				<open-data class="my-header__name" type="userNickName"></open-data>
			</view>
		</view>
		
		
		<view class="my-content">
			<view class="my-content__list">
				<view class="my-content__list-title">
					<uni-icons class="icons" type="star" size="48" color="#666"></uni-icons>
					<text style="font-weight: bold;font-size: 32rpx;">咨询师资格审核</text>
				</view>
				<uni-icons style="margin-top:25rpx" type="arrowright" size="28" color="#666" @tap="open"></uni-icons>
			</view>
			<view class="my-content__list">
				<view class="my-content__list-title">
					<uni-icons class="icons" type="help" size="48" color="#666"></uni-icons>
					<text style="font-weight: bold;font-size: 32rpx;">意见反馈</text>
				</view>
				<uni-icons style="margin-top:25rpx" type="arrowright" size="28" color="#666" @tap="navigate1"></uni-icons>
			</view>
		</view>
		
	</view>
</template>

<script>
	import uniIcons from "@/components/uni-icons/uni-icons.vue"
	export default {
		components: {uniIcons},
		data() {
			return {
				
			}
		},
		methods: {
			open(){
				uni.navigateTo({
					url:'./qualify/qualify'
				})
			},
			navigate1(){
				uni.navigateTo({
					url:'./Feedback/Feedback'
				})
			}
		}
	}
</script>

<style lang="scss">
page {
	background-color: "#f5f5f5";
}
.my-header {
	position: relative;
	padding-bottom: 30rpx;
	.my-header__background {
		position: absolute;
		top: 0;
		bottom: 0;
		left: 0;
		right: 0;
		filter: blur(16rpx);
		opacity: 0.5;
		image {
			width: 100%;
			height: 100%;
		}
	}
	.my-header__logo {
		display: flex;
		flex-direction: column;
		align-items: center;
		padding-top: 60rpx;
		.my-header__logo-box {
			width: 150rpx;
			height: 150rpx;
			border-radius: 50%;
			overflow: hidden;
			image {
				width: 100%;
				height: 100%;
			}
		}
		.my-header__name {
			margin-top: 30rpx;
			font-size: 32rpx;
			font-family: 'Open Sans', sans-serif;
			font-weight: bolder;
			color: #000000
		}	
	}
	.my-header__info {
		margin-top: 50rpx;
		display: flex;
		.my-header__info-box {
			display: flex;
			flex-direction: column;
			justify-content: center;
			align-items: center;
			width: 100%;
			font-size: 24rpx;
			color: "#999";
			.my-header__info-title {
				font-size: 28rpx;
				color: "#333";
			}
		}
	}
}
.my-content {
	padding-top:45rpx;
	.my-content__list {
		display: flex;
		justify-content: space-between;
		padding: 30rpx;
		margin-bottom: 20rpx;
		background-color: "#ffffff";
		color: "#333";
		font-size: 28rpx;
		.my-content__list-title {
			display: flex;
			align-items: center;
			.icons {
				margin-right: 10px;
			}
		}
	}
}
</style>
