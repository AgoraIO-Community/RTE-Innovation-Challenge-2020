/*
 * @Author: he.xiaoxue
 * @Date: 2018-08-15 10:34:36
 * @Last Modified by: he.xiaoxue
 * @Last Modified time: 2018-12-26 21:21:13
 */
import Query from '@/leanCloud/ggQuery'
import AV from 'leancloud-storage'
import * as DB from '@/leanCloud/global'

export function teamLiveQuery(gameId, areaId) {
  return Query.TeamListLiveQuery(gameId, areaId)
}
/**
 * 获取组队列表
 * @param {String} gameId 游戏id
 */
export function getTeamList(gameId, title, areaId) {
  return new Promise((resolve, reject) => {
    const query = Query.TeamListQuery()
    if (gameId && gameId !== -1) {
      var game = AV.Object.createWithoutData(DB.DB_GAME, gameId)
      query.equalTo('game', game)
    }
    if (title && title !== -1) {
      query.contains('title', title)
    }
    if (areaId && areaId !== -1) {
      var area = AV.Object.createWithoutData(DB.DB_TEAM_AREA, areaId)
      query.equalTo('area', area)
    }
    query.find().then(
      function(results) {
        results = results.map(item => {
          return item.toJSON()
        })
        const res = {
          teamArr: results,
          query: query
        }
        resolve(res)
      },
      function(error) {
        reject(error)
      }
    )
  })
}
/**
 * 获取游戏列表
 */
export function getGameList() {
  return new Promise((resolve, reject) => {
    const query = Query.GameListQuery()
    query.find().then(
      function(results) {
        results = results.map(item => {
          return item.toJSON()
        })
        resolve(results)
      },
      function(error) {
        reject(error)
      }
    )
  })
}
/**
 * 获取用户历史数据
 * @param {String} userId 用户id
 */
export function getUserHistory(userId) {
  return new Promise((resolve, reject) => {
    const query = Query.UserHistoryQuery(userId)
    query.find().then(
      function(results) {
        results = results.map(item => {
          return item.toJSON()
        })
        resolve(results)
      },
      function(error) {
        reject(error)
      }
    )
  })
}
/**
 * 获取游戏列表
 */
export function getAreaList() {
  return new Promise((resolve, reject) => {
    const query = Query.TeamAreaQuery()
    query.find().then(
      function(results) {
        results = results.map(item => {
          return item.toJSON()
        })
        resolve(results)
      },
      function(error) {
        reject(error)
      }
    )
  })
}
/**
 * 插入用户历史数据
 * @param {String} gameId 游戏id
 * @param {string} teamId teamid
 * @param {String} conversationId 对话id
 */
export function insertUserHistory(gameId, teamId, conversationId) {
  const UserHistory = AV.Object.extend(DB.DB_USER_HISTORY)

  const currentUser = AV.User.current()
  const conv = AV.Object.createWithoutData(DB.DB_CONVERSATION, conversationId) // 会话对象
  const game = AV.Object.createWithoutData(DB.DB_GAME, gameId) // 游戏对象
  const team = AV.Object.createWithoutData(DB.DB_TEAM, teamId) // 组队对象
  const userHistory = new UserHistory()
  userHistory.set('userObjectId', currentUser.toJSON().objectId)
  userHistory.set('game', game)
  userHistory.set('team', team)
  userHistory.set('conversation', conv)
  userHistory.set('user', currentUser)
  userHistory
    .save()
    .then(() => {})
    .catch(error => {
      console.log(error)
    })
}
/**
 * @description 根据id删除多条用户历史组队数据
 * @param {Array} idArr user历史表数据id数组
 */
export function deleteUserHistory(idArr) {
  const query = Query.UserHistoryQueryArr(idArr)
  query
    .find()
    .then(results => {
      AV.Object.destroyAll(results)
        .then(() => {})
        .catch(error => {
          console.log(error)
        })
    })
    .catch(error => {
      console.log(error)
    })
}
/**
 * 返回匹配的数据
 * @param {string} gameId gameid
 * @param {Array} gameTypeArr 偏好设置数组
 */
export function getQuickMatchTeam(gameId, gameTypeArr) {
  return new Promise((resolve, reject) => {
    const query = Query.getMatchTeam(gameId, gameTypeArr)
    query
      .find()
      .then(results => {
        results = results.map(item => {
          return item.toJSON()
        })
        resolve({ teamArr: results, query: query })
      })
      .catch(error => {
        reject(error)
      })
  })
}

export function getRencentUser() {
  return new Promise((resolve, reject) => {
    const query = Query.getMatchTeam(gameId, gameTypeArr)
    query
      .find()
      .then(results => {
        results = results.map(item => {
          return item.toJSON()
        })
        resolve({ teamArr: results, query: query })
      })
      .catch(error => {
        reject(error)
      })
  })
}
export function getPrizeRecord() {
  return new Promise((resolve, reject) => {
    const query = Query.getPrizeRecordQuery()
    query
      .first()
      .then(results => {
        if (results) {
          results = results.toJSON()
        }
        resolve(results)
      })
      .catch(error => {
        reject(error)
      })
  })
}
