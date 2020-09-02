import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/views/login/loign'
import Layout from '@/views/Layout/Layout'
import More from '@/views/gameLobby/teams'
import Room from '@/views/room/chatRoom'
import RecentUser from '@/views/recent/recentUser'
import FocusUser from '@/views/recent/recentUser'
import AboutUser from '@/views/app/about'
// D:\xiaoxue\web\src\views\login\loign.vue
// import Home from './views/Home.vue'

Vue.use(Router)

export default new Router({
  base: process.env.BASE_URL,
  mode: 'history',
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/team/:teamId',
      name: 'chatRoom',
      component: Room
      // meta: {
      //   title: '飞聊语音房间'
      // }
    },
    {
      path: '/teambycode/:teamCode',
      name: 'ChatRoomCode',
      component: Room,
      alias: '/:teamCode'
      // meta:{
      //   title:'飞聊语音房间'
      // }
    },
    {
      path: '/',
      // name: 'Home',
      component: Layout,
      children: [
        {
          path: '',
          name: 'teams',
          component: More
        }
      ]
    },
    {
      path: '/g',
      component: Layout,
      children: [
        {
          path: 'recent',
          name: 'RecentUser',
          component: RecentUser
        },
        {
          path: 'focus',
          name: 'FocusUser',
          component: FocusUser
        }
      ]
    },
    {
      path: '/a/about',
      name: 'AboutUs',
      component: AboutUser
      /*  children: [
        {
          path: 'about',
          name: 'AboutUser',
          component: AboutUser
        }
      ] */
    },
    {
      path: '/1/404', // 404页面
      name: 'notfound',
      component: () => import('@/views/404'),
      hidden: true
    },
    {
      path: '*', // 未匹配的路径跳转到404页面 必须放在最后
      redirect: '/1/404',
      hidden: true
    }
  ]
})
