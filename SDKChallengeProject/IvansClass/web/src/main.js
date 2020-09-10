import Vue from 'vue'
import App from './App.vue'
import './plugins/element.js'
import router from './router'

Vue.config.productionTip = false

router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = to.meta.title
  }
  const type = to.meta.type
  // 判断该路由是否需要登录权限
  if (type === 'login') {
    if (window.localStorage.getItem('token')) {
      next()
    } else {
      next('/login')
    }
  } else {
    next() // 确保一定要有next()被调用
  }
})

new Vue({
  el: '#app',
  router,
  render: h => h(App)
}).$mount('#app')
