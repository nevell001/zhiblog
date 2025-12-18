import request from '@/utils/request'

// 查询友链列表
export function listFriendLink(query) {
  return request({
    url: '/system/friendLink/list',
    method: 'get',
    params: query
  }).then(response => {
    // 确保返回的数据格式正确
    if (response && response.code === 200 && response.data) {
      return response.data
    } else {
      throw new Error('获取友链列表数据格式错误')
    }
  }).catch(error => {
    console.error('查询友链列表失败:', error)
    throw error
  })
}

// 查询友链详细
export function getFriendLink(id) {
  return request({
    url: '/system/friendLink/' + id,
    method: 'get'
  }).then(response => {
    if (response && response.code === 200) {
      return response
    } else {
      throw new Error('获取友链详情失败')
    }
  }).catch(error => {
    console.error('查询友链详情失败:', error)
    throw error
  })
}

// 新增友链
export function addFriendLink(data) {
  return request({
    url: '/system/friendLink',
    method: 'post',
    data: data
  }).then(response => {
    if (response && response.code === 200) {
      return response
    } else {
      throw new Error(response.message || '新增友链失败')
    }
  }).catch(error => {
    console.error('新增友链失败:', error)
    throw error
  })
}

// 修改友链
export function updateFriendLink(data) {
  return request({
    url: '/system/friendLink',
    method: 'put',
    data: data
  }).then(response => {
    if (response && response.code === 200) {
      return response
    } else {
      throw new Error(response.message || '修改友链失败')
    }
  }).catch(error => {
    console.error('修改友链失败:', error)
    throw error
  })
}

// 删除友链
export function delFriendLink(id) {
  return request({
    url: '/system/friendLink/' + id,
    method: 'delete'
  }).then(response => {
    if (response && response.code === 200) {
      return response
    } else {
      throw new Error(response.message || '删除友链失败')
    }
  }).catch(error => {
    console.error('删除友链失败:', error)
    throw error
  })
}

// 查询前台友链列表（用于前台展示）
export function getFrontFriendLinkList() {
  return request({
    url: '/system/friendLink/front/list',
    method: 'get'
  }).then(response => {
    if (response && response.code === 200) {
      return response
    } else {
      throw new Error('获取前台友链列表失败')
    }
  }).catch(error => {
    console.error('查询前台友链列表失败:', error)
    throw error
  })
}

