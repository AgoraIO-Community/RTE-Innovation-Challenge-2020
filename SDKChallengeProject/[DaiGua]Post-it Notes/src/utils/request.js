import axios from 'axios'
// import Cookies from 'js-cookie'
import { Message } from 'element-ui'
// import JsCookie from 'js-cookie'// 需要安装js-cookie插件

import router from '@/router'
// import store from '@/store'

// 是否正在刷新的标记
// let isRefreshing = false
// let timer = null

// create an axios instance

// 自定义判断元素类型JS
const toType = (obj) => {
  return ({}).toString.call(obj).match(/\s([a-zA-Z]+)/)[1].toLowerCase()
}

// 参数过滤函数
// const filterNull = (o) => {
//   for (var key in o) {
//     if (o[key] === null) {
//       delete o[key]
//     }
//     if (toType(o[key]) === 'string') {
//       o[key] = o[key].trim()
//       if (o[key] === '') {
//         o[key] = null
//       }
//     } else if (toType(o[key]) === 'object') {
//       o[key] = filterNull(o[key])
//     } else if (toType(o[key]) === 'array') {
//       o[key] = filterNull(o[key])
//     }
//   }
//   return o || {}
// }
const service = axios.create({
  // baseURL: process.env.VUE_APP_BASE_API,
  baseURL: 'https://api.agora.io/v1',

  withCredentials: true
  // timeout: 3000
  // timeout: 100
})

// request interceptor
service.defaults.withCredentials = true// 允许跨越时携带cookie并不是加上就能跨域
service.interceptors.request.use(
  config => {
    // config.headers['content-type'] = 'application/json;charset=UTF-8'
    // console.log('config', config)
    // config.data = filterNull(config.data)
    // config.params = filterNull(config.params)
    config.headers['Authorization'] = 'Basic ODU5MzFjY2ZmOWE5NDM4ZWI4OGE1NTVhZTk1OGNkNzI6ZjNkODRjOWYzNTA2NDZhN2JmZjU1NWIyNWY1ZjIxYmY='
    // if (config.url === '/audit/import/enroll') {
    //   config.timeout = 50000
    // }

    console.log('config=============', config)
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(
  response => {
    return response.data
    // } else {
    //   return res
    // }
  },
  error => {
    let message = error.message
    if (
      error.code === 'ECONNABORTED' ||
      error.code === 'ETIMEDOUT'
    ) {
      message = '请求超时，请稍后重试'
    }
    Message({
      message: message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
