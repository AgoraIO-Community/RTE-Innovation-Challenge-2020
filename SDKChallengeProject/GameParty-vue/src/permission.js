import router from './router/index'
import NProgress from 'nprogress' // 进度条
import 'nprogress/nprogress.css'
import AV from 'leancloud-storage' // 第三方sdk
NProgress.configure({ showSpinner: false }) // NProgress Configuration

// const whiteList = ['/login', '/teambycode/:teamCode'] // 不重定向白名单

const whiteNameList = ['teams', 'Login', 'ChatRoomCode', 'AboutUs']

router.beforeEach((to, from, next) => {
  NProgress.start()
  const currentUser = AV.User.current()
  // 当前有登录用户且非匿名登录用户
  if (currentUser && !currentUser.isAnonymous()) {
    if (to.path === '/login') {
      next({ path: '/' })
      NProgress.done() // if current page is dashboard will not trigger	afterEach hook, so manually handle it
    } else {
      next()
    }
  } else {
    // debugger
    if (whiteNameList.indexOf(to.name) !== -1) {
      next()
    } else {
      // next('/login')
      next({ path: '/' })

      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done() // 结束Progress
})
