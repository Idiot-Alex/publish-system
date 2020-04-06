
import router from './router'
// Progress 进度条
import NProgress from 'nprogress'
// Progress 进度条样式
import 'nprogress/nprogress.css'
// 验权
import { getToken } from '@/util/auth'

// 不重定向白名单
const whiteList = [
  '/login'
]

router.beforeEach((to, from, next) => {
  NProgress.start()
  // 检测白名单
  if (whiteList.indexOf(to.path) !== -1) {
    next()
    return
  }
  // 验证是否登录
  if (getToken()) {
    if (to.path === '/login') {
      next({ path: '/' })
      NProgress.done() // if current page is dashboard will not trigger	afterEach hook, so manually handle it
    }
    next()
  } else {
    next('/login')
    NProgress.done()
  }
})

router.afterEach(() => {
  NProgress.done() // 结束Progress
})
