import request from '@/utils/request.js'
const appid = process.env.VUE_APP_APP_ID

// 获取云端录制资源
export function acquireAgora(cname, uid) {
  return request({
    url: '/apps/' + appid + '/cloud_recording/acquire',
    method: 'post',
    data: {
      cname,
      uid,
      clientRequest: {

      }
    }
  })
}
const mode = 'mix'
// 开始云端录制
export function startAgora(resourceid, cname, uid, token) {
  return request({
    url: `/apps/${appid}/cloud_recording/resourceid/${resourceid}/mode/${mode}/start`,
    method: 'post',
    data: {
      cname,
      uid,
      clientRequest: {
        token,
        storageConfig: {
          secretKey: 'ZoJte8JACJEBwWdIUdVGlyE4smm543',
          region: 3,
          accessKey: 'LTAI4G6pfjNrzevjHMskwrxY',
          bucket: 'cmz-daigua',
          vendor: 2
        },
        recordingConfig: {
          audioProfile: 0,
          channelType: 0,
          maxIdleTime: 30,
          transcodingConfig: {
            width: 640,
            height: 360,
            fps: 15,
            bitrate: 500
          }
        }
      }
    }
  })
}
// 停止云端录制
export function stopAgora(resourceid, sid, cname, uid) {
  return request({
    url: `/apps/${appid}/cloud_recording/resourceid/${resourceid}/sid/${sid}/mode/${mode}/stop`,
    method: 'post',
    data: {
      cname,
      uid,
      clientRequest: {
      }
    }
  })
}
// 查询云端录制状态

export function queryAgora(resourceid, sid) {
  return request({
    url: `/apps/${appid}/cloud_recording/resourceid/${resourceid}/sid/${sid}/mode/${mode}/query`,
    method: 'get'
  })
}
