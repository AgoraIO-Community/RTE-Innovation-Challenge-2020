<template>
	<view class="content">
		<view>
			<swiper class="swiper" :indicator-dots="indicatorDots" :indicator-active-color="color" :autoplay="autoplay"
			 :circular="circular">
				<swiper-item v-for="item in images">
					<image :src="item.url" mode="aspectFill" />
				</swiper-item>
			</swiper>
		</view>
		<view class="option">
			<view class="option-box">
				<button type="default" plain="true" @click="navigate1">
					<image :src="button_image1" mode="aspectFill"></image>
				</button>
			</view>
			<view class="option-title-contain">
				<text class="option-title" style="margin:100rpx 15rpx 100rpx 90rpx;">每日一签</text>
			</view>



			<view class="option-title-contain">
				<text class="option-title" style="margin:100rpx 90rpx 100rpx 15rpx;">语音漂流瓶</text>
			</view>
			<view class="option-box">
				<button type="default" plain="true" @click="navigate3">
					<image :src="button_image3" mode="aspectFill"></image>
				</button>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				circular: true,
				color: "#666",
				indicatorDots: true,
				autoplay: false,
				images: [{
						url: "https://vkceyugu.cdn.bspapp.com/VKCEYUGU-wangshanyw/874dbde0-f325-11ea-b244-a9f5e5565f30.png"
					},
					{
						url: "https://vkceyugu.cdn.bspapp.com/VKCEYUGU-wangshanyw/8e12d5c0-f325-11ea-8bd0-2998ac5bbf7e.png"
					},
					{
						url: 'https://vkceyugu.cdn.bspapp.com/VKCEYUGU-wangshanyw/6706d7f0-f326-11ea-8bd0-2998ac5bbf7e.png'
					}
				],
				button_image1: "https://vkceyugu.cdn.bspapp.com/VKCEYUGU-wangshanyw/8b18d170-e926-11ea-81ea-f115fe74321c.png",
				button_image3: "https://vkceyugu.cdn.bspapp.com/VKCEYUGU-wangshanyw/29a28870-e929-11ea-b997-9918a5dda011.jpg"
			}
		},
		methods: {
			open() {
				console.log('打开每日一签')
			},
			navigate1() {
				uni.navigateTo({
					url: '/pages/tabbar/ComfortingWords/GetDailyWords/GetDailyWords',
				})
			},
			navigate2() {
				uni.navigateTo({
					url: '/pages/tabbar/ComfortingWords/FloatingBottles/FloatingBottles',
				})
			},
			navigate3() {
				uni.navigateTo({
					url: '/pages/tabbar/ComfortingWords/HearingWords/HearingWords',
				})
			}

		}
	}
</script>

<style lang="scss">
	.swiper {
		display: flex;
		height: 450rpx;
		margin-top: 100rpx;
		margin-bottom: 100rpx;
		margin-left: 30rpx;
		margin-right: 30rpx;
		justify-content: center;

		image {
			height: 100%;
			width: 100%;
			justify-content: center;
		}
	}

	.option {
		width: 100%;
		justify-content: center;

		.option-box {
			display: flex;
			width: 50%;
			height: 230rpx;
			border-radius: 50%;
			margin-bottom: 80rpx;
			box-sizing: border-box;
			float: right;
			overflow: hidden;

			image {
				width: 100%;
				height: 100%;
			}

			button {
				width: 230rpx;
				height: 230rpx;
				border: None;
				padding: 0;
			}

		}

		.option-title-contain {
			display: flex;
			width: 50%;
			height: 270rpx;
			border-radius: 50%;
			margin-bottom: 60rpx;
			box-sizing: border-box;
			float: right;
		}

		.option-title {
			position: relative;
			display: flex;
			font-size: 40rpx;
			font-weight: bold;
			font: Tahoma, Helvetica, Arial, '宋体', sans-serif;
			text-shadow: 2px 2px #8f673c;
			color: #f3a530;
		}
	}
</style>