import {} from '@/leanCloud/global'
import AV from 'leancloud-storage'
import * as DB from '@/leanCloud/global'
import AVLiveQuery from 'leancloud-storage/live-query'

const Query = {
  /**
   * 游戏大厅数据获取
   */
  TeamListQuery: () => {
    const query = new AV.Query('tb_Team')
    // const query = new AV.Query('tb_Team')
    query.equalTo('delete', false)
    query.notEqualTo('hidden', true)
    query.notEqualTo('status', false)
    query.include('type')
    query.include('publisher')
    query.include('participants')
    query.include('game')
    query.include('conversation')
    query.include('taskType')
    query.descending('updatedAt')
    query.limit(200)
    return query
  },
  /**
   * 游戏大厅数据获取
   */
  TeamListLiveQuery: (gameId, areaId) => {
    const query = new AVLiveQuery.Query('tb_Team')
    if (gameId && gameId !== -1) {
      var game = AV.Object.createWithoutData(DB.DB_GAME, gameId)
      query.equalTo('game', game)
    }

    if (areaId && areaId !== -1) {
      var area = AV.Object.createWithoutData(DB.DB_TEAM_AREA, areaId)
      query.equalTo('area', area)
    }
    // query.equalTo('delete', false)
    // query.notEqualTo('status', false)
    // query.notEqualTo('hidden', true)
    query.include('type')
    query.include('publisher')
    query.include('participants')
    query.include('game')
    query.include('conversation')
    query.include('taskType')
    query.descending('updatedAt')
    query.limit(200)
    return query
  },
  /**
   * 游戏列表数据获取
   */
  GameListQuery: () => {
    const query = new AV.Query('tb_Game')
    query.equalTo('isShow', true)
    query.include('taskType')
    query.ascending('sort')
    return query
  },
  /**
   * 游戏列表数据获取
   */
  TeamAreaQuery: () => {
    const query = new AV.Query(DB.DB_TEAM_AREA)
    query.equalTo('delete', false)
    query.ascending('sort')
    return query
  },
  /**
   * 用户组队历史列表数据获取
   */
  UserHistoryQuery: userId => {
    const query = new AV.Query('tb_User_History')
    query.equalTo('userObjectId', userId)
    query.include('team')
    query.include('team.publisher')
    return query
  },
  UserHistoryQueryArr: arr => {
    const queryArr = arr.map(item => {
      const q = new AV.Query(DB.DB_USER_HISTORY)
      q.equalTo('userObjectId', item)

      return q
    })
    const query = AV.Query.or(...queryArr)
    return query
  },
  TeamInfoQuery: () => {
    const query = new AV.Query(DB.DB_TEAM)
    query.include('game')
    query.include('type')
    query.include('publisher')
    query.include('participants')
    query.include('conversation')
    return query
  },
  UserInfoQuery: id => {
    const query = AV.Object.createWithoutData(DB.DB_USER, id)
    return query
  },
  UserByUserIdQuery: userId => {
    const query = new AV.Query(DB.DB_USER)
    query.equalTo('userId', userId)
    return query
  },
  UserInfoExtraQuery: id => {
    const query = new AV.Query(DB.DB_User_Extra)
    query.equalTo('user', id)
    return query
  },
  getMatchTeam(gameId, gameTypeArr) {
    const queryArr = gameTypeArr.map(item => {
      const query = new AV.Query(DB.DB_TEAM)
      const game = AV.Object.createWithoutData(DB.DB_GAME, gameId)
      query.equalTo('game', game)
      const gameType = AV.Object.createWithoutData(DB.DB_GAME_TYPE, item.objectId)
      query.equalTo('type', gameType)

      query.equalTo('delete', false)
      query.equalTo('isLock', false)
      query.notEqualTo('status', false)

      return query
    })

    const query = AVLiveQuery.Query.or(...queryArr)
    query.descending('updatedAt')
    return query
  },
  // 获取关注的人
  getIsFollowQuery(userId) {
    const query = AV.User.current().followeeQuery()
    const userObj = AV.Object.createWithoutData(DB.DB_USER, userId)
    query.equalTo('followee', userObj)
    return query
  },
  getRencentQuery() {
    const user = AV.User.current()
    const query = new AV.Query(DB.DB_TEAM_HISTORY)
    query.equalTo('user', user)
    query.limit(30)
    query.descending('updatedAt')
    query.include('teamer')
  },
  getPrizeRecordQuery() {
    const user = AV.User.current()
    const source = AV.Object.createWithoutData(DB.DB_PRIZE_SOURCE, '5baba1a917d009005ed5e863')
    const query = new AV.Query(DB.TB_PRIZE_USER)
    query.equalTo('source', source)
    query.equalTo('user', user)
    return query
  }
}
export default Query
