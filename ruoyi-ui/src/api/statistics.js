import request from '@/utils/request'

// 获取数据概览统计
export function getStatisticsOverview() {
  return request({
    url: '/statistics/overview',
    method: 'get'
  })
}

// 获取文章统计
export function getArticleStatistics() {
  return request({
    url: '/statistics/article',
    method: 'get'
  })
}

// 获取用户统计
export function getUserStatistics() {
  return request({
    url: '/statistics/user',
    method: 'get'
  })
}

// 获取文章发布趋势数据
export function getArticleTrend() {
  return request({
    url: '/statistics/article/trend',
    method: 'get'
  })
}

// 获取用户活跃度数据
export function getUserActivity() {
  return request({
    url: '/statistics/user/activity',
    method: 'get'
  })
}

// 获取用户注册趋势数据
export function getUserRegisterTrend() {
  return request({
    url: '/statistics/user/register-trend',
    method: 'get'
  })
}

// 获取用户角色分布数据
export function getUserRoleDistribution() {
  return request({
    url: '/statistics/user/role-distribution',
    method: 'get'
  })
}

// 获取文章分类分布数据
export function getArticleCategoryDistribution() {
  return request({
    url: '/statistics/article/category-distribution',
    method: 'get'
  })
}

// 获取热门标签数据
export function getHotTags() {
  return request({
    url: '/statistics/article/hot-tags',
    method: 'get'
  })
}
