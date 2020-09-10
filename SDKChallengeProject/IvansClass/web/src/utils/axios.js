import axios from 'axios'
import qs from 'qs'

axios.interceptors.request.use((request) => {
  // 在发送请求之前做某件事
  var token = ''
  if (localStorage.getItem('token')) {
    token = localStorage.getItem('token')
  }

  if (token) {
    token = 'Bearer ' + token
    request.headers.common.Authorization = token
  }
  if (request.method === 'post') {
    request.data = qs.stringify(request.data)
  }
  return request
}, (error) => {
  console.log('错误的传参')
  return Promise.reject(error)
})

export default axios // 导出axios
