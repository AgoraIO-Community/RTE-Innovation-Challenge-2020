import request from '@/utils/requestOuer.js'
// 登录
export function login(username, password) {
  return request({
    url: '/user/login',
    method: 'get',
    params: {
      username, password
    }
  })
}
// 获取token
export function getToken(uid, channelName) {
  return request({
    url: '/token/getToken',
    method: 'get',
    params: {
      uid,
      channelName,
      appid: process.env.VUE_APP_APP_ID
    }
  })
}
// 加入房间
export function joinClass(room, rname, userId, time) {
  return request({
    url: '/class/join',
    method: 'get',
    params: {
      room, rname, userId, time
    }
  })
}
// 上传笔记 /note/noteUpdate
export function noteUpdate(room, userId, time, file) {
  const formdata = new FormData()
  formdata.append('room', room)
  formdata.append('userId', userId)
  formdata.append('time', time)
  formdata.append('file', file)
  return request({
    url: '/note/noteUpdate',
    method: 'post',
    data: formdata
  })
}
// 获取用户笔记
export function getUserNote(room, userId) {
  return request({
    url: '/note/getUserNote',
    method: 'get',
    params: {
      room, userId
    }
  })
}
// 上传查词截图 /note/noteUpdate
export function uploadWordPic(file, xpos, ypos) {
  const formdata = new FormData()
  formdata.append('ypos', ypos)
  formdata.append('xpos', xpos)
  formdata.append('file', file)
  return request({
    url: '/ocr',
    method: 'post',
    data: formdata
  })
}
// 确认查询该词
export function ensureWord(room, query) {
  return request({
    url: '/spi',
    method: 'get',
    params: {
      room, query
    }
  })
}
// 获取参加过的直播

export function historyClass(userId) {
  return request({
    url: '/class/gerUserVideoHistory',
    method: 'get',
    params: {
      userId
    }
  })
}
// 获取课程云图

export function getCloud(classId) {
  return request({
    url: '/class/getCloud',
    method: 'get',
    params: {
      classId
    }
  })
}
// 上传直播录制视频
export function uploadVideo(videoName, room) {
  return request({
    url: '/class/uploadVideo',
    method: 'get',
    params: {
      videoName, room
    }
  })
}
