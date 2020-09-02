const user = {
  state: {
    name: '',
    avatar: '',
    phone: ''
  },
  mutations: {
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_PHONE: (state, phone) => {
      state.phone = phone
    }
  },

  actions: {
    // 登录
    // commit 调用mutations 修改vuex数据
    Login({ commit }, userInfo) {
      // return new Promise((resolve, reject) => {
      // 请求
      commit('SET_NAME', userInfo.name)
      commit('SET_AVATAR', userInfo.avatar)
      commit('SET_PHONE', userInfo.phone)
      // })
    },

    // 获取用户信息
    GetInfo({ commit, state }) {},

    // 登出
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        /*  logout(state.token)
          .then(() => {
            commit('SET_TOKEN', '')
            commit('SET_ROLES', [])
            resolve()
          })
          .catch((error) => {
            reject(error)
          }) */
      })
    },

    // 前端 登出
    FedLogOut({ commit }) {
      return new Promise(resolve => {
        // 清除token
        commit('SET_TOKEN', '')
        resolve()
      })
    }
  }
}

export default user
