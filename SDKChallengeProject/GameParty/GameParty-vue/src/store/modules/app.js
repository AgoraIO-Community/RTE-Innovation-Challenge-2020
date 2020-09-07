const app = {
  state: {
    games: [], // 游戏列表
    device: 'desktop',
    activeAreaId: ''
  },
  mutations: {
    SET_GAMES: (state, games) => {
      state.games = games
    },
    SET_ACTIVE_AREA: (state, activeAreaId) => {
      state.activeAreaId = activeAreaId
    }
  },

  actions: {
    GetAllGames({ commit }, games) {
      commit('SET_GAMES', games)
    },
    SetActiveArea({ commit }, id) {
      commit('SET_ACTIVE_AREA', id)
    }
  }
}

export default app
