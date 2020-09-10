<template>
	<view 
	 class="wyb-drop-down-box"
	 :style="{
		 '--duration': duration + 'ms',
		 '--autoContentTop': autoContentTop}">
		
		<view class="wyb-drop-down-container" @tap.stop.prevent @touchmove.stop.prevent>
			<view 
			 class="wyb-drop-down-header"
			 :style="{
				 zIndex: zIndex,
				 backgroundColor: bgColor.header}">
				<view 
				 class="wyb-drop-down-header-item"
				 v-for="(item,index) in options"
				 :key="item.header"
				 @tap.stop="onHeaderTap(index)"
				 :style="{fontSize: fontSize.header + 'rpx'}">
					<text
					 class="wyb-drop-down-header-item-label"
					 :style="{
						 fontWeight: headerActiveIndex === index && dropOver && activeWeight ? 'bold' : 'normal',
						 color: headerActiveIndex === index && dropOver ? activeColor: defaultColor}">{{item.header}}</text>
					<text
					 v-if="dropIcon === 'fill'"
					 class="iconfont icon-down-fill wyb-drop-down-header-item-icon"
					 :class="[headerActiveIndex === index && dropOver ? 'wyb-drop-down-header-item-icon-active' : '']"
					 :style="{
						 fontSize: fontSize.header - 5 + 'rpx',
						 color: headerActiveIndex === index && dropOver ? activeColor: defaultColor}" />
					<text
					 v-if="dropIcon==='line'"
					 class="iconfont icon-down wyb-drop-down-header-item-icon"
					 :class="[headerActiveIndex === index && dropOver ? 'wyb-drop-down-header-item-icon-active' : '']"
					 :style="{
						 fontSize: fontSize.header - 5 + 'rpx',
						 transformOrigin: '50% 45%',
						 color: headerActiveIndex === index && dropOver ? activeColor: defaultColor}" />
					<view class="wyb-drop-down-vline" v-if="index !== options.length - 1" />
				</view>
			</view>
			
			<scroll-view 
			 v-if="dropDown"
			 class="wyb-drop-down-content" 
			 :class="[dropOver ? 'wyb-drop-down-content-active' : '']"
			 :scroll-y="scroll"
			 :enable-flex="true"
			 :scroll-anchoring="true"
			 :style="{
				 zIndex: zIndex - 1,
				 fontSize: fontSize.content + 'rpx',
				 backgroundColor: bgColor.content,
				 borderBottomLeftRadius: radius + 'px',
				 borderBottomRightRadius: radius + 'px',
				 minHeight: minHeight + 'rpx',
				 height: autoHeight ? 'auto' : minHeight + 'rpx',
				 maxHeight: autoHeight && maxHeight ? maxHeight + 'rpx' : 'auto'}">
				 <view class="wyb-drop-down-content-box" v-for="(item,index) in options" :key="contentBoxKey(index)">
				 	<view v-if="item['custom'] && headerActiveIndex === index && dropDown" class="wyb-drop-down-content-slot">
						<slot></slot>
					</view>
					<view
					 v-if="!item['custom'] && headerActiveIndex === index && dropDown"
					 class="wyb-drop-down-content-item"
					 v-for="(content,zIndex) in item['contents']"
					 :key="content"
					 @tap.stop="onContentItemsTap(zIndex)">
						<text
						 class="wyb-drop-down-content-item-label"
						 :style="{color: contentActiveIndexList[headerActiveIndex]['index'] === zIndex && dropOver ? activeColor: defaultColor}">
							{{content}}
						</text>
						<text
						 v-if="contentActiveIndexList[headerActiveIndex]['index'] === zIndex && dropOver"
						 :style="{color: activeColor}"
						 class="iconfont icon-selected wyb-drop-down-content-item-icon" />
						<view class="wyb-drop-down-line" v-if="zIndex !== options[headerActiveIndex].contents.length - 1" />
					</view>
				 </view>
			</scroll-view>
		</view>
		
		<view
		 v-if="dropDown"
		 class="wyb-drop-down-mask"
		 :class="[dropOver ? 'wyb-drop-down-mask-active' : '']"
		 @tap.stop="close"
		 @touchmove.stop.prevent
		 :style="{
			 zIndex: zIndex - 2,
			 height: screenHeight + 'px',
			 backgroundColor: 'rgba(0, 0, 0, ' + maskAlpha + ')'}" />
	</view>
</template>

<script>
	export default {
		data() {
			return {
				dropDown: false,
				dropOver: false,
				duration: 500,
				contents: this.options[0].contents || [0],
				headerActiveIndex: 0,
				contentActiveIndexList: []
			}
		},
		computed: {
			autoContentTop() {
				return `${44 + this.rpxToPx(100)}px`
			},
			screenHeight() {
				return uni.getSystemInfoSync().screenHeight
			},
			screenWidth() {
				return uni.getSystemInfoSync().screenWidth
			},
			contentBoxKey() {
				return function(index) {
					return `option${index}`
				}
			}
		},
		props: {
			options: {
				type: Array,
				default() {
					return [{
						header: 'A',
						contents: ['1', '2']
					}]
				}
			},
			defaultIndexList: {
				type: Array,
				default() {
					return []
				}
			},
			autoHeight: {
				type: Boolean,
				default: true
			},
			minHeight: {
				type: [String, Number],
				default: 10
			},
			maxHeight: {
				type: [String, Number],
				default: 600
			},
			scroll: {
				type: Boolean,
				default: true
			},
			radius: {
				type: [String, Number],
				default: '0'
			},
			activeColor: {
				type: String,
				default: '#2979ff'
			},
			activeWeight: {
				type: Boolean,
				default: true
			},
			defaultColor: {
				type: String,
				default: '#333'
			},
			bgColor: {
				type: Object,
				default() {
					return {
						header: '#fff',
						content: '#fff'
					}
				}
			},
			dropIcon: {
				type: String,
				default: 'fill'
			},
			fontSize: {
				type: Object,
				default() {
					return {
						header: 30,
						content: 30
					}
				}
			},
			maskAlpha: {
				type: [String, Number],
				default: '0.5'
			},
			zIndex: {
				type: Number,
				default: 500
			}
		},
		mounted() {
			if (this.defaultIndexList.length === 0) {
				this.options.forEach((item, index) => {
					if (!item.custom) {
						this.contentActiveIndexList.push({headerIndex: index, index: 0})
					} else {
						this.contentActiveIndexList.push({headerIndex: index, custom: true})
					}
				})
			} else {
				let i = 0
				this.options.forEach((item, index) => {
					if (!item.custom) {
						this.contentActiveIndexList.push([...this.defaultIndexList][i])
						i++
					} else {
						this.contentActiveIndexList.push({headerIndex: index, custom: true})
					}
				})
			}
		},
		methods: {
			onHeaderTap(index) {
				let item = this.options[index]
				if (Object.is(this.headerActiveIndex, index) && this.dropOver) {
					this.close()
				} else {
					this.headerActiveIndex = index
					if (item.custom) {
						this.$emit('change', {
							headerIndex: index,
							header: this.options[index].header
						})
					}
					this.dropDown = true
					this.$nextTick(() => {
						this.dropOver = true
						this.$emit('show')
					})
				}
			},
			onContentItemsTap(index) {
				this.contentActiveIndexList[this.headerActiveIndex]['index'] = index
				this.$forceUpdate()
				let event = {
					headerIndex: this.headerActiveIndex,
					header: this.options[this.headerActiveIndex]['header'],
					contentIndex: index,
					content: this.options[this.headerActiveIndex]['contents'][index],
					contentActiveIndexList: this.contentActiveIndexList
				}
				this.$emit('select', event)
			},
			close() {
				this.dropOver = false
				setTimeout(() => {
					this.dropDown = false
					this.$emit('hide')
				}, this.duration)
			},
			rpxToPx(rpx) {
				return rpx / 750 * this.screenWidth
			}
		}
	}
</script>

<style>
	@import './iconfont.css';
	.wyb-drop-down-mask {
		position: fixed;
		top: 44px;
		/* #ifndef H5 */
		top: 0;
		/* #endif */
		left: 0;
		bottom: 0;
		right: 0;
		opacity: 0;
		transition: opacity var(--duration);
		z-index: 498;
	}
	
	.wyb-drop-down-mask-active {
		opacity: 1;
		transition: opacity var(--duration);
	}
	
	.wyb-drop-down-header {
		position: fixed;
		top: 44px;
		/* #ifndef H5 */
		top: 0;
		/* #endif */
		left: 0;
		right: 0;
		display: flex;
		flex-direction: row;
		background-color: #fff;
		z-index: 500;
	}
	
	.wyb-drop-down-header-item {
		flex: 1;
		height: 100rpx;
		font-size: 30rpx;
		border-bottom: 1px solid #eee;
		display: flex;
		flex-direction: row;
		align-items: center;
		justify-content: center;
		position: relative;
	}
	
	.wyb-drop-down-header-item-label {
		display: flex;
		flex-direction: row;
		align-items: center;
		justify-content: flex-end;
	}
	
	.wyb-drop-down-header-item-icon {
		margin-left: 20rpx;
		display: flex;
		flex-direction: row;
		align-items: center;
		justify-content: flex-start;
		transform-origin: 50% 40%;
		transform: rotate(0);
		transition: transform var(--duration);
	}
	
	.wyb-drop-down-header-item-icon-active {
		transform: rotate(180deg);
		transition: transform var(--duration);
	}
	
	.wyb-drop-down-content {
		position: fixed;
		top: var(--autoContentTop);
		/* #ifndef H5 */
		top: 100rpx;
		/* #endif */
		left: 0;
		right: 0;
		z-index: 499;
		display: flex;
		flex-direction: column;
		align-items: flex-start;
		background-color: #fff;
		transform: translateY(-100%);
		transition: transform var(--duration);
	}
	
	.wyb-drop-down-content-active {
		transform: translateY(0);
		transition: transform var(--duration);
	}
	
	.wyb-drop-down-content-item {
		width: 100%;
		height: 100rpx;
		font-size: 30rpx;
		display: flex;
		flex-direction: column;
		position: relative;
	}
	
	.wyb-drop-down-content-item-label {
		width: 90%;
		height: 100%;
		display: flex;
		flex-direction: row;
		align-items: center;
		justify-content: flex-start;
		padding-left: 50rpx;
	}
	
	.wyb-drop-down-content-item-icon {
		position: absolute;
		top: 50%;
		right: 40rpx;
		font-size: 40rpx;
		transform: translateY(-50%);
	}
	
	.wyb-drop-down-content-box {
		width: 100%;
	}
	
	.wyb-drop-down-content-slot {
		width: 100%;
		height: 100%;
	}
	
	.wyb-drop-down-vline {
		width: 1px;
		height: 40rpx;
		background-color: #eee;
		position: absolute;
		right: 0;
	}

	.wyb-drop-down-line {
		width: 100%;
		height: 1px;
		background-color: #eee;
		margin-left: 50rpx;
	}
</style>
