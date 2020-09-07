import axios from 'axios'

export function getWxAccessToken(code) {
  const params = {
    appid: 'wxcd1d0b740f17ebe8',
    secret: '59a0e2e5dbf1408f5770a5d446f9d0fe',
    code: code,
    grant_type: 'authorization_code'
  }
  const url = 'https://api.weixin.qq.com/sns/oauth2/access_token'
  return axios({
    url: url,
    method: 'get',
    headers: { 'Access-Control-Allow-Origin': '*' },
    params: params
  })
}
