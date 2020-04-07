import { login } from '@/api/user'
import { getToken, setToken, setValue, getValueByKey } from '@/util/auth'

export default {
  state: {
    token: getToken(),
    userId: getValueByKey('userId'),
    userName: getValueByKey('userName')
  },
  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_USER_ID: (state, userId) => {
      state.userId = userId
    },
    SET_USER_NAME: (state, userName) => {
      state.userName = userName
    }
  },
  actions: {
    // 登录
    Login({ commit }, userInfo) {
      return new Promise((resolve, reject) => {
        login(userInfo.userName, userInfo.userPassword).then(response => {
          const data = response.data
          if (data !== undefined) {
            setToken(data.token)
            setValue('userId', data.userId)
            setValue('userName', data.userName)
            commit('SET_TOKEN', data.token)
            commit('SET_USER_ID', data.userId)
            commit('SET_USER_NAME', data.userName)
          }
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    }
  }
}