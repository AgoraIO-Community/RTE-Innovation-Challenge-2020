import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'login',
    component: () => import('../pages/login/login')
  }, {
    path: '/register',
    name: 'Register',
    component: () => import('../pages/register/register')
  }, {
    path: '/courses',
    name: 'Courses',
    component: () => import('../pages/course/index'),
    meta: {
      title: '首页',
      type: 'login' // 是否需要判断是否登录,这里是需要判断
    }
  }, {
    path: '/courses/show',
    name: 'CoursesShow',
    component: () => import('../pages/course/show'),
    meta: {
      title: '首页',
      type: 'login' // 是否需要判断是否登录,这里是需要判断
    }
  }, {
    path: '/class/show',
    name: 'CoursesShow',
    component: () => import('../pages/class/show'),
    meta: {
      title: '首页',
      type: 'login' // 是否需要判断是否登录,这里是需要判断
    }
  }, {
    path: '/live/show',
    name: 'LiveShow',
    component: () => import('../pages/class/live'),
    meta: {
      title: '直播页面',
      type: 'login' // 是否需要判断是否登录,这里是需要判断
    }
  }, {
    path: '/user/courses',
    name: 'UserCourses',
    component: () => import('../pages/user/courses'),
    meta: {
      title: '首页',
      type: 'login' // 是否需要判断是否登录,这里是需要判断
    }
  }, {
    path: '/user/lives',
    name: 'UserLives',
    component: () => import('../pages/user/lives'),
    meta: {
      title: '首页',
      type: 'login' // 是否需要判断是否登录,这里是需要判断
    }
  }, {
    path: '/user/classes',
    name: 'UserClasses',
    component: () => import('../pages/user/class'),
    meta: {
      title: '首页',
      type: 'login' // 是否需要判断是否登录,这里是需要判断
    }
  }]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
