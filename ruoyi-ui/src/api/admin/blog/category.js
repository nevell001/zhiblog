import request from '@/utils/request'

// 查询分类列表
export function listCategory(query) {
  return request({
    url: '/system/category/list',
    method: 'get',
    params: query
  })
}

// 查询分类详细
export function getCategory(id) {
  return request({
    url: '/system/category/' + id,
    method: 'get'
  })
}

// 新增分类
export function addCategory(data) {
  return request({
    url: '/system/category',
    method: 'post',
    data: data
  })
}

// 修改分类
export function updateCategory(data) {
  return request({
    url: '/system/category',
    method: 'put',
    data: data
  })
}

// 删除分类
export function delCategory(ids) {
  return request({
    url: '/system/category/' + ids,
    method: 'delete'
  })
}


// 获取分类下拉树
export function categoryTreeSelect() {
  return request({
    url: '/system/category/treeSelect',
    method: 'get'
  })
}

