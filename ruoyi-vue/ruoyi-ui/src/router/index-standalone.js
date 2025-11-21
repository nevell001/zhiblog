import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/blog'
  },
  {
    path: '/blog',
    component: () => import('@/views/blog/index')
  },
  {
    path: '/login',
    component: () => import('@/views/login')
  },
  {
    path: '/admin',
    component: () => import('@/layout/index.vue'),
    redirect: '/admin/dashboard',
    children: [
      {
        path: 'dashboard',
        component: () => import('@/views/admin/dashboard/index.vue')
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/404'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router