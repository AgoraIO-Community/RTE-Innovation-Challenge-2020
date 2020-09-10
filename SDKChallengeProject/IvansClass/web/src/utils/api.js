import axios from './axios'

export function login (param) {
  return axios({
    url: '/api/login',
    method: 'POST',
    data: param
  })
}

// 注册
export function register (param) {
  return axios({
    url: '/api/register',
    method: 'POST',
    data: param
  })
}

// 获取课程 列表
export function fetchCourses (param) {
  return axios({
    url: '/api/courses',
    method: 'GET',
    params: param
  })
}
// 获取课程详情
export function fetchCourse (param) {
  return axios({
    url: '/api/courses/show',
    method: 'GET',
    params: param
  })
}

// 获取课程班级列表
export function fetchClasses (param) {
  return axios({
    url: '/api/courses/classes',
    method: 'GET',
    params: param
  })
}
// 获取课程班级详情
export function fetchClass (param) {
  console.log(param)
  return axios({
    url: '/api/courses/classes/show',
    method: 'GET',
    params: param
  })
}
// 获取课程班级直播列表
export function fetchLives (param) {
  return axios({
    url: '/api/courses/classes/lives',
    method: 'GET',
    params: param
  })
}

// 获取课程班级直播教室详情
export function enterLiveRoom (param) {
  return axios({
    url: '/api/user/live/enter',
    method: 'POST',
    data: param
  })
}

// 获取课程班级列表
export function joinClass (param) {
  return axios({
    url: '/api/user/class/join',
    method: 'POST',
    data: param
  })
}

// 获取用户直播
export function fetchUserLives (param) {
  return axios({
    url: '/api/user/lives',
    method: 'GET',
    params: param
  })
}

// 获取用户课程
export function fetchUserCourses (param) {
  return axios({
    url: '/api/user/courses',
    method: 'GET',
    params: param
  })
}

// 获取用户班级
export function fetchUserClasses (param) {
  return axios({
    url: '/api/user/classes',
    method: 'GET',
    params: param
  })
}
