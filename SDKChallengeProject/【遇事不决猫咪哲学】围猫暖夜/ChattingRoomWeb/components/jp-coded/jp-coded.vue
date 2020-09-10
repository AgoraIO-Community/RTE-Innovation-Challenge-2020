<template>
	<view class="wallet_class">
		<view class="pay-pwd-input" v-if="pawType==='one'" :style="'width:' + width + 'rpx'" @tap="tokey">
			<view class="pay-pwd-grid uni-flex uni-row" v-for="(value, index) in payPwdGrid" :key="index">
				<view :style="('width:'+ width1 + 'rpx;') + ((focusType&&(index==list.length))?borderCheckStyle:'') ">{{value.text}}</view>
			</view>
		</view>
		<view class="input-row" v-if="pawType==='two'" :style="'width:' + width + 'rpx'" @tap="tokey">
			<view class="pay-pwd-grid uni-flex uni-row" v-for="(value, index) in payPwdGrid" :key="index">
				<view :class="'item'" :style="('width:'+ width1 + 'rpx;') + ((focusType&&(index==list.length))?borderCheckStyle:'') ">{{ value.text  }}</view>
			</view>
		</view>
		<input v-if="keyType" :type="inputType" :style="'width:' + width + 'rpx'" :maxlength="places" class="input_info" @input="inputVal"
		 @focus="focus" @blur="blur" />
	</view>
</template>

<script>
	export default {
		name: 'wallet_category',
		props: {
			pawType: { //输入框样式
				type: String,
				default: 'one'
			},
			places: { // 密码框位数
				type: Number,
				default: 6
			},
			width: {
				type: Number,
				default: 750
			},
			borderCheckStyle: {
				type: String,
				default: 'border: 1px solid #f00;'
			},
			codes: {
				type: String,
				default: '123'
			},
			keyType: {
				type: Boolean,
				default: true
			},
			isPwy:{
				type: Boolean,
				default: true
			},
			inputType:{
				type: String,
				default: 'number'
			}
		},
		data() {
			return {
				focusType: false,
				width1: 110,
				list: [],
				payPwdGrid: []
			}
		},
		mounted() {
			this.list = this.codes.split('')
			this.width1 = (this.width - 90) / this.places
			this.payPwdGrid = []
			for (let a = 0; a < this.places; a++) {
				this.payPwdGrid.push({
					text: ''
				})
			}
			if(this.isPwy){
				for (let a = 0; a < this.list.length; a++) {
					this.payPwdGrid[a].text = '●'
				}
			}else{
				for (let a = 0; a < this.list.length; a++) {
					this.payPwdGrid[a].text = this.list[a]
				}
			}
		},
		watch: {
			places() {
				this.list = this.codes.split('')
				this.width1 = (this.width - 90) / this.places
				this.payPwdGrid = []
				for (let a = 0; a < this.places; a++) {
					this.payPwdGrid.push({
						text: ''
					})
				}
				if(this.isPwy){
					for (let a = 0; a < this.list.length; a++) {
						this.payPwdGrid[a].text = '●'
					}
				}else{
					for (let a = 0; a < this.list.length; a++) {
						this.payPwdGrid[a].text = this.list[a]
					}
				}
			},
			codes() {
				this.list = this.codes.split('')
				this.payPwdGrid = []
				for (let a = 0; a < this.places; a++) {
					this.payPwdGrid.push({
						text: ''
					})
				}
				if(this.isPwy){
					for (let a = 0; a < this.list.length; a++) {
						this.payPwdGrid[a].text = '●'
					}
				}else{
					for (let a = 0; a < this.list.length; a++) {
						this.payPwdGrid[a].text = this.list[a]
					}
				}
				this.$emit('inputVal', this.codes);
			}
		},
		methods: {
			focus() {
				this.focusType = true
			},
			blur() {
				this.focusType = false
			},
			tokey(){
				this.$emit('tokey');
			},
			inputVal(e) {
				let inputValues = e.detail.value;
				this.list = inputValues.split('')
				this.payPwdGrid = []
				for (let a = 0; a < this.places; a++) {
					this.payPwdGrid.push({
						text: ''
					})
				}
				if(this.isPwy){
					for (let a = 0; a < this.list.length; a++) {
						this.payPwdGrid[a].text = '●'
					}
				}else{
					for (let a = 0; a < this.list.length; a++) {
						this.payPwdGrid[a].text = this.list[a]
					}
				}
				this.$emit('inputVal', inputValues);
			}
		}
	}
</script>

<style lang="scss" scoped>
	.wallet_class {
		position: relative;
		height: 100%;

		.pay-pwd-input {
			height: 100%;
			line-height: 100%;
			display: flex;
			justify-content: flex-start;

			.pay-pwd-grid {
				margin: 0upx auto;
				height: 100%;
				line-height: 100%;
				display: flex;
				justify-content: center;

				view {
					width: 110upx;
					height: 100%;
					display: flex;
					align-items: center;
					justify-content: center;
					border: #cececd solid 0.1upx;
					border-radius: 10upx;
					font-size: 36upx;
					font-weight: 600;
				}

				.xaunz {
					border: #f00 solid 0.1upx;
				}
			}
		}

		.input-row {
			height: 100%;
			line-height: 100%;
			display: flex;
			justify-content: flex-start;

			.pay-pwd-grid {
				margin: 0upx auto;
				height: 100%;
				line-height: 100%;
				display: flex;
				justify-content: center;

				.item {
					width: 110rpx;
					height: 100%;
					display: flex;
					align-items: center;
					justify-content: center;
					font-size: 36upx;
					font-weight: 600;
					border-bottom: 1px solid #c8c8c8;
				}

				.item-active {
					position: relative;
					transform: scale(1.2);
				}
			}

		}

		.input_info {
			width: 1200rpx;
			height: 100%;
			line-height: 100%;
			opacity: 0;
			position: absolute;
			top: 0rpx;
			left: 0;
		}
	}
</style>
