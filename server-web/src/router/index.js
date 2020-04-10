import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  mode: 'history',
  scrollBehavior: () => ({ y: 0 }),
  routes: [
    { path: '/login', component: () => import('@/views/login'), hidden: true },
    { path: '/404', component: () => import('@/views/404'), hidden: true },
    { path: '*', redirect: '/404', hidden: true },
    { path: '/', component: () => import('@/views/layout'), redirect: '/dashboard', children: [
      {
        name: 'dashboard',
        path: '/dashboard',
        component: () => import('@/views/dashboard')
      },
      {
        name: 'agent',
        path: '/agent',
        component: () => import('@/views/agent')
      },
      {
        name: 'file',
        path: '/file',
        component: () => import('@/views/file')
      },
      {
        name: 'article',
        path: '/article',
        component: () => import('@/views/article')
      },
      {
        name: 'play-list',
        path: '/agent-playlist',
        component: () => import('@/views/agent-playlist')
      },
      {
        name: 'user',
        path: '/user',
        component: () => import('@/views/user')
      }
    ]},
    {
      name: 'client',
      path: '/client',
      component: () => import('@/views/client')
    }
  ]
})
