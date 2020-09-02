/*
 * @Author: he.xiaoxue
 * @Date: 2018-08-30 16:20:39
 * @Last Modified by: he.xiaoxue
 * @Last Modified time: 2018-09-24 21:26:00
 */
import Query from '@/leanCloud/ggQuery'
import AV from 'leancloud-storage'

import CryptoJS from 'crypto-js'

/**
 * 获取组队详细信息
 */
const getTeamFun = (key, value) => {
  return new Promise((resolve, reject) => {
    const query = Query.TeamInfoQuery()
    query.equalTo(key, value)
    query
      .first()
      .then(results => {
        // results = results.toJSON()
        if (results) {
          resolve(results.toJSON())
        } else {
          const error = {
            code: 404,
            msg: 'no team'
          }
          reject(error)
        }
      })
      .catch(error => {
        reject(error)
      })
  })
}

/**
 * 获取组队详细信息
 */
export function getTeam(teamId) {
  return getTeamFun('objectId', teamId)
}
/**
 * 获取组队详细信息
 */
export function getTeamByCode(teamCode) {
  return getTeamFun('teamCode', teamCode)
}
/**
 * 获取队员的头像
 */
export function getParticipants(participants) {
  const objects = []
  participants.forEach(function(item) {
    objects.push(Query.UserInfoQuery(item))
  })
  return new Promise((resolve, reject) => {
    AV.Object.fetchAll(objects)
      .then(results => {
        results = results.map(item => {
          item = item.toJSON()
          item.isMute = false
          item.isOnline = true
          item.isSpeak = false
          return item
        })
        resolve(results)
      })
      .catch(error => {
        reject(error)
      })
  })
}
export function getUserByUserId(userId) {
  return new Promise((resolve, reject) => {
    const query = Query.UserByUserIdQuery(userId)
    query
      .first()
      .then(results => {
        resolve(results.toJSON())
      })
      .catch(error => {
        reject(error)
      })
  })
}
export function getUserExtra(userId) {
  return new Promise((resolve, reject) => {
    const query = Query.UserInfoExtraQuery(userId)
    query
      .first()
      .then(res => {
        console.log(res)
        if (res) {
          res = res.toJSON()
          console.log(res)
          resolve(res)
        } else {
          resolve()
        }
      })
      .catch(error => {
        reject(error)
      })
  })
}
export function messageFormat(msgArr) {
  const newArr = []

  msgArr = msgArr.map(msg => {
    msg = msg.toJSON()
    const rep = msg.text.match(/\:/)
    if (rep && !(msg.attributes && msg.attributes.isWill)) {
      msg.sendName = msg.text.substring(0, rep.index)
      msg.sendText = msg.text.substring(rep.index + 1)
      newArr.push({ type: 'msg', content: msg })
    }
  })
  return newArr
}
export function isFollow(userId) {
  return new Promise((resolve, reject) => {
    const query = Query.getIsFollowQuery(userId)
    query
      .first()
      .then(res => {
        if (res) {
          res = res.toJSON()
          resolve(res)
        } else {
          resolve()
        }
      })
      .catch(error => {
        reject(error)
      })
  })
}

export function des() {
  // DES加密 Pkcs7填充方式
  const encryptByDES = (message, key) => {
    const keyHex = CryptoJS.enc.Utf8.parse(key)
    const encrypted = CryptoJS.TripleDES.encrypt(message, keyHex, {
      iv: CryptoJS.enc.Utf8.parse('01234567'),
      mode: CryptoJS.mode.CBC,
      padding: CryptoJS.pad.Pkcs7
    })
    return encrypted.toString()
  }

  // DES解密
  const decryptByDES = (ciphertext, key) => {
    const keyHex = CryptoJS.enc.Utf8.parse(key)
    // direct decrypt ciphertext
    const options = { mode: CryptoJS.mode.CBC, padding: CryptoJS.pad.Pkcs7, iv: CryptoJS.enc.Utf8.parse('01234567') } // 向量
    const decrypted = CryptoJS.TripleDES.decrypt({ ciphertext: CryptoJS.enc.Base64.parse(ciphertext) }, keyHex, options)
    return decrypted.toString(CryptoJS.enc.Utf8)
  }

  const _key = 'guyuyincomdevelopeeeeeee'
  const _password = '20174'

  // 加密
  this.codeEncrypt = encryptByDES(_password, _key)
  // 解密
  decryptByDES(_password, _key)
}

export function xor_enc(str, key) {
  let crytxt = ''
  let k
  const keylen = key.length
  for (let i = 0; i < str.length; i++) {
    k = i % keylen
    crytxt += String.fromCharCode(str.charCodeAt(i) ^ key.charCodeAt(k))
  }
  return crytxt
}

export function getAllVideo() {
  //
}
