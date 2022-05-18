import { createRouter, createWebHashHistory, RouteRecordRaw } from 'vue-router'
// import routes from 'virtual:generated-pages'
// console.log(routes,'打印生成自动生成的路由')

let constantRoutes = [
  {
    path: '/',
    component: () => import('@/pages/index.vue'),
  },
  {
    path: '/webtools',
    component: () => import('@/pages/webtools/index.vue'),
    children: [
      {
        path: "/webtools/commuse",
        component: () => import('@/pages/webtools/components/commuse.vue'),
      },
      {
        path: "/webtools/thing",
        component: () => import('@/pages/webtools/components/thing.vue'),
      },
      {
        path: "/webtools/role",
        component: () => import('@/pages/webtools/components/role.vue'),
      },
      {
        path: "/webtools/holyrelic",
        component: () => import('@/pages/webtools/components/holyrelic.vue'),
      },
      {
        path: "/webtools/monster",
        component: () => import('@/pages/webtools/components/monster.vue'),
      },
      {
        path: "/webtools/other",
        component: () => import('@/pages/webtools/components/other.vue'),
      },
      {
        path: "/webtools/weapon",
        component: () => import('@/pages/webtools/components/weapon.vue'),
      },
      {
        path: "/webtools/personnel",
        component: () => import('@/pages/webtools/components/personnel.vue'),
      },
      {
        path: "/webtools/LocationManager",
        component: () => import('@/pages/webtools/components/LocationManager.vue'),
      },
      {
        path: "/webtools/scene",
        component: () => import('@/pages/webtools/components/scene.vue'),
      },
      {
        path: "/webtools/quest",
        component: () => import('@/pages/webtools/components/quest.vue'),
      },
      {
        path: "/webtools/login",
        component: () => import('@/pages/webtools/components/login.vue'),
      }
    ]
  }

]



//导入生成的路由数据
const router = createRouter({
  history: createWebHashHistory(),
  routes: constantRoutes,
})

export default router
