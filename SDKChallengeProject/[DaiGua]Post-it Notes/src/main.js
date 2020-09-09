import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui'
import './style/index.scss' // global css
import 'element-ui/lib/theme-chalk/index.css'
import VueCropper from 'vue-cropper'
// import hls from 'videojs-contrib-hls'
import VideoPlayer from 'vue-video-player'

Vue.use(VideoPlayer)
// Vue.use(hls)
Vue.use(VueCropper)
Vue.use(ElementUI)
Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
