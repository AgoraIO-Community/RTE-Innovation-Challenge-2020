const game = {
  state: {
    gameTypeList: [], // 游戏列表
    activeAreaId: '',
    prizeHasShow: false
  },
  mutations: {
    SET_GAMES: (state, games) => {
      state.gameTypeList = games
    },
    SET_ACTIVE_AREA: (state, activeAreaId) => {
      state.activeAreaId = activeAreaId
    },
    SET_PRIZE_HAS_SHOW: (state, prizeHasShow) => {
      state.prizeHasShow = prizeHasShow
    }
  },

  actions: {
    GetAllGames({ commit }, games) {
      commit('SET_GAMES', games)
    },
    SetActiveArea({ commit }, id) {
      commit('SET_ACTIVE_AREA', id)
    },
    PrizeHasShow({ commit }, prizeHasShow) {
      commit('SET_PRIZE_HAS_SHOW', prizeHasShow)
    }
  }
}

export default game
