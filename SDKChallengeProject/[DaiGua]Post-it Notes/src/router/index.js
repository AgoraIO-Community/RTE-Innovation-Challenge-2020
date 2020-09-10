import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/home.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/home',
    name: 'Home',
    component: Home
  },

  {
    path: '/about',
    name: 'About',

    component: () =>
      import(/* webpackChunkName: "about" */ '../views/About.vue')
  },
  {
    path: '/login',
    name: 'Login',

    component: () => import('../views/login.vue')
  },
  {
    path: '/classroom',
    name: 'classroom',

    component: () => import('../views/classroom.vue')
  },
  {
    path: '/Sketchpad',
    name: 'Sketchpad',
    component: () => import('../views/Sketchpad.vue')
  },
  {
    path: '/screenShot',
    name: 'screenShot',
    component: () => import('../views/screenShot.vue')
  },
  {
    path: '/review',
    name: 'review',
    component: () => import('../views/review.vue')
  },

  {
    path: '/historyClass',
    name: 'historyClass',
    component: () => import('../views/historyClass.vue')
  },
  {
    path: '/test',
    name: 'test',
    component: () => import('../views/test.vue')
  }

]

const router = new VueRouter({
  routes,
  // mode: 'history', // 去除路径中中的#
  scrollBehavior(to, from, savePosition) {
    if (savePosition) {
      return savePosition // 页面跳转时保存页面的滚动位置，保证返回时页面能保持在之前的状态
    } else {
      return {
        x: 0,
        y: 0
      }
    }
  }
})

export default router
