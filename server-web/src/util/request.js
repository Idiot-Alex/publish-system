import axios from 'axios'
import qs from 'qs'
import { Message, Notification } from 'element-ui'
import store from '../store'

const CODE_510 = 510

// 创建axios实例
const service = axios.create({
  timeout: 5000,
  baseURL: 'http://127.0.0.1:8080/',
  headers: {
    'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
  },
  responseType: 'json'
})

// request拦截器
service.interceptors.request.use(
  config => {
    // 非常重要 参数转化成 字符串
    config.data = qs.stringify(config.data)

    if (store.getters.token) {
      // 让每个请求携带自定义token 请根据实际情况自行修改
      config.headers['token'] = store.getters.token
    }
    return config
  },
  error => {
    // Do something with request error
    // console.log(error) // for debug
    Promise.reject(error)
  }
)

// response 拦截器
service.interceptors.response.use(
  response => {
    /**
     * code为非0是抛错 可结合自己业务进行修改
     */
    const res = response.data
    if (res === null) {
      Message({
        message: res ? res.msg : 'Server Error',
        type: 'error',
        duration: 5 * 1000
      })
      return Promise.reject('error')
    }

    // token 失效  CODE_510
    if (res !== null && res.code === CODE_510) {
      Notification({
        type: 'error',
        title: 'message',
        message: 'Invalid login info. Please login again'
      })
      this.store.dispatch('FedLogOut').then(() => {
        // 为了重新实例化vue-router对象 避免bug
        location.reload()
      })
    }
    return response.data
  },
  error => {
    // 不弹出错误信息提示了
    console.log(error) // for debug
    return Promise.reject(error)
  }
)

export default service
