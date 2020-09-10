<template>
	<view class="content">
		<img class="poster-img" src="https://vkceyugu.cdn.bspapp.com/VKCEYUGU-wangshanyw/b0307370-ef32-11ea-9dfb-6da8e309e0d8.jpg"
		 mode="scaleToFill" alt="" srcset="">
		<view class="words-content">
			{{wordsContent}}
		</view>
		<button class="share-btn" @tap="shareEvn('simple')">生成海报并分享</button>
		<!-- 分享弹窗-->
		<view class="share-pro">
			<view class="share-pro-mask" v-if="deliveryFlag"></view>
			<view class="share-pro-dialog" :class="deliveryFlag?'open':'close'">
				<view class="close-btn" @tap="closeShareEvn">
					<span class="font_family">&#xe81d;</span>
				</view>
				<view class="share-pro-title">分享</view>
				<view class="share-pro-body">
					<view class="share-item">
						<button open-type="share">
							<view class="font_family share-icon">&#xe786;</view>
							<view>分享给好友</view>
						</button>
					</view>
					<view class="share-item" @tap="handleShowPoster">
						<view class="font_family share-icon">&#xe600;</view>
						<view>生成分享图片</view>
					</view>
				</view>
			</view>
		</view>
		<hchPoster ref="hchPoster" @cancel="canvasCancel" :simpleFlag="simpleFlag" :posterBgFlag="posterBgFlag"
		 :canvasAttr.sync="posterObj" />
		<!-- <view :hidden="canvasFlag"> -->
		<!-- 海报 要放外面放组件里面 会找不到 canvas-->
		<!-- </view> -->
	</view>
</template>

<script>
	import hchPoster from "@/components/hch-poster/hch-poster.vue"
	export default {
		components: {
			hchPoster
		},
		data() {
			return {
				wordsContent: '',
				deliveryFlag: false,
				posterBgFlag: true, //是否展示海报背景图
				simpleFlag: true, //是否展示简单版海报
				// canvasFlag: true,//是否隐藏海报
				posterSimpleData: { //简单版的海报
					marginLR: 70,
					marginTB: 45,
					radius: 0.05,
					innerLR: 40,
					innerTB: 35,
					fillColor: '#000000',
					title: "",
					titleFontSize: 20,
					titleLineHeight: 24,
					posterCodeUrl: "https://vkceyugu.cdn.bspapp.com/VKCEYUGU-wangshanyw/cb1e56d0-ef40-11ea-8ff1-d5dcf8779628.jpg",
					codeWidth: 0.25,
					codeRatio: 1,
					codeRadius: 0.5,
					codeMT: 50,
					posterBgUrl: 'https://vkceyugu.cdn.bspapp.com/VKCEYUGU-wangshanyw/b0307370-ef32-11ea-9dfb-6da8e309e0d8.jpg',
					codeML: 140,
					desTextMT: 70,
					desTextML: 240,
				},
				posterObj: {}
			}
		},
		onLoad() {
			uniCloud.callFunction({
				name: "RandomGetDailyWords",
			}).then(res => {
				this.posterSimpleData.title = res.result.data["splitList"]
				this.wordsContent = res.result.data["content"]
			})
		},
		methods: {
			/**
			 * @description: 生成海报
			 * @param {type} 
			 * @return {type} 
			 * @author: hch
			 */
			handleShowPoster() {
				// this.canvasFlag = false
				this.$refs.hchPoster.posterShow()
				this.deliveryFlag = false
			},
			/**
			 * @description: 分享弹窗
			 * @param {type} 
			 * @return {type} 
			 */
			shareEvn(type) {
				if (type === 'simple') {
					this.simpleFlag = true
					this.posterObj = this.posterSimpleData
				} else {
					this.simpleFlag = false
					this.posterObj = this.posterData
				}
				this.deliveryFlag = true
			},
			/**
			 * @description: 关闭分享弹窗
			 * @param {type} 
			 * @return {type} 
			 */
			closeShareEvn() {
				this.deliveryFlag = false
			},
			/**
			 * @description: 取消海报
			 * @param {type} 
			 * @return {type} 
			 */
			canvasCancel(val) {
				// this.canvasFlag = val
			}
		}
	}
</script>

<style lang="scss">
	@font-face {
		font-family: 'font_family';
		/* project id 1991769 */
		src: url('//at.alicdn.com/t/font_1991769_u8wpg8jfhpq.eot');
		src: url('//at.alicdn.com/t/font_1991769_u8wpg8jfhpq.eot?#iefix') format('embedded-opentype'),
			url('//at.alicdn.com/t/font_1991769_u8wpg8jfhpq.woff2') format('woff2'),
			url('//at.alicdn.com/t/font_1991769_u8wpg8jfhpq.woff') format('woff'),
			url('//at.alicdn.com/t/font_1991769_u8wpg8jfhpq.ttf') format('truetype'),
			url('//at.alicdn.com/t/font_1991769_u8wpg8jfhpq.svg#iconfont') format('svg');
	}

	.font_family {
		font-family: "font_family" !important;
		font-size: 16px;
		font-style: normal;
		-webkit-font-smoothing: antialiased;
		-webkit-text-stroke-width: 0.2px;
		-moz-osx-font-smoothing: grayscale;
	}

	/* 按钮去掉边框 */
	button::after {
		border: none;
	}

	button {
		line-height: 1;
		color: #1c1c1c;
		font-size: 28rpx;
		background: none;
	}

	.content {
		margin-top: 25rpx;
		display: flex;
		position: relative;
		flex-direction: column;
		border-radius: 80rpx;
		margin-left: 25rpx;
		margin-right: 25rpx;
		width: 700rpx;
		height: 1000rpx;

		image {
			width: 700rpx;
			height: 1000rpx;
			border-radius: 80rpx;
			//z-index: -1;

		}
	}

	.words-content {
		margin-top: 250rpx;
		margin-left: 100rpx;
		font-size: 45rpx;
		color: '#666666';
		width: 500rpx;
		position: absolute;
		z-index: 2;
		justify-content: center;
		text-align: center;
		line-height: 80rpx;
	}


	.share-btn {
		margin-top: 100rpx;
		margin-right: 100rpx;
		width: 500rpx;
		font-size: 50rpx;
		color: #fefefe;
		height: 60px;
		background: linear-gradient(to bottom, #fbaf51 0%, #F49255 100%);
		/* W3C */
		border: none;
		border-radius: 100px;
		position: relative;
		border-bottom: 8px solid #e76c3c;

		padding: 10px 0px 0px 0px;
		font-weight: 600;
		font-family: 'Open Sans', sans-serif;
		text-shadow: 5px 3px 2px rgba(0, 0, 0, .4);
		text-align: middle;
		text-indent: -5px;
		box-shadow: 3px 3px 3px 0px rgba(0, 0, 0, .2);
		cursor: pointer;

		/* Just for presentation */
		display: block;
		margin-bottom: 20px;
	}

	.hover {
		background: linear-gradient(to bottom right, #F49255, #FB5655);
	}

	/*每个页面公共css */
	.content {
		text-align: center;
		height: 100%;
	}

	.share-pro {
		display: flex;
		align-items: center;
		justify-content: flex-end;
		flex-direction: column;
		z-index: 5;
		line-height: 1;
		box-sizing: border-box;

		.share-pro-mask {
			width: 100%;
			height: 100%;
			position: fixed;
			top: 0;
			right: 0;
			bottom: 0;
			left: 0;
			background: rgba(0, 0, 0, 0.5);
		}

		.share-pro-dialog {
			width: 750rpx;
			height: 310rpx;
			overflow: hidden;
			background-color: #fff;
			border-radius: 24rpx 24rpx 0px 0px;
			position: relative;
			box-sizing: border-box;
			position: fixed;
			bottom: 0;

			.close-btn {
				padding: 20rpx 15rpx;
				position: absolute;
				top: 0rpx;
				right: 29rpx;
			}

			.share-pro-title {
				font-size: 28rpx;
				color: #1c1c1c;
				padding: 28rpx 41rpx;
				background-color: #f7f7f7;
			}

			.share-pro-body {
				display: flex;
				flex-direction: row;
				justify-content: space-around;
				font-size: 28rpx;
				color: #1c1c1c;

				.share-item {
					display: flex;
					flex-direction: column;
					justify-content: center;
					justify-content: space-around;

					.share-icon {
						text-align: center;
						font-size: 70rpx;
						margin-top: 39rpx;
						margin-bottom: 16rpx;
						color: #42ae3c;
					}

					&:nth-child(2) {
						.share-icon {
							color: #ff5f33;
						}
					}
				}
			}
		}

		/* 显示或关闭内容时动画 */

		.open {
			transition: all 0.3s ease-out;
			transform: translateY(0);
		}

		.close {
			transition: all 0.3s ease-out;
			transform: translateY(310rpx);
		}
	}

	.canvas {
		position: fixed !important;
		top: 0 !important;
		left: 0 !important;
		display: block !important;
		width: 100% !important;
		height: 100% !important;
		z-index: 10;
	}
</style>
