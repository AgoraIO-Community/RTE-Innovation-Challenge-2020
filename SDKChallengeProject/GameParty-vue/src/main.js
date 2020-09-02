import Vue from 'vue'

import '@/styles/index.scss' // css文件

// import AgoraRTC from './packages/Agora/AgoraRTCSDK-2.3.1' // 封装组件

// Vue.prototype.AgoraRTC = AgoraRTC // 绑定全局变量

// 第三方SDK 以及初始化
import AV from 'leancloud-storage'
import AVLiveQuery from 'leancloud-storage/live-query'
// const AVLiveQuery = require('leancloud-storage/live-query')
AVLiveQuery.init({
  appId: 'NSnWnyEL3aKpDdMA5Co9Jbcy-gzGzoHsz',
  appKey: 'FTQCB3XNYjvWaiivrg2DNSRj'
})
AV.init({
  appId: 'NSnWnyEL3aKpDdMA5Co9Jbcy-gzGzoHsz',
  appKey: 'FTQCB3XNYjvWaiivrg2DNSRj'
})
// 初始化即时通讯 SDK
var { Realtime } = require('leancloud-realtime')
const realtime = new Realtime({
  appId: 'NSnWnyEL3aKpDdMA5Co9Jbcy-gzGzoHsz',
  appKey: 'FTQCB3XNYjvWaiivrg2DNSRj'
})
Vue.prototype.AV = AV // 绑定全局变量
Vue.prototype.LeanRT = { realtime: realtime } // 绑定全局变量

// 主文件 路由 app存储
import App from './App.vue'
import router from './router/index'
import store from './store/index'

import vuescroll from 'vuescroll' // scroll组件
import 'vuescroll/dist/vuescroll.css'

import hx from './packages/index' // 封装组件

import '@/permission' // 权限控制

Vue.use(vuescroll)
Vue.use(hx)

// 空的Vue实例作为中央事件总线
const bus = new Vue()
Vue.prototype.bus = bus

Vue.config.productionTip = false // 阻止启动生产消息

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
