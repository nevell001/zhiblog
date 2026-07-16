import request from '@/utils/request'
import type { Menu, MenuParams } from '@/types'

/**
 * 查询菜单列表
 */
export function listMenu(query?: MenuParams): Promise<any> {
  return request({
    url: '/system/menu/list',
    method: 'get',
    params: query
  })
}

/**
 * 查询菜单详细
 */
export function getMenu(menuId: number): Promise<any> {
  return request({
    url: '/system/menu/' + menuId,
    method: 'get'
  })
}

/**
 * 查询菜单下拉树结构
 */
export function treeselect(): Promise<any> {
  return request({
    url: '/system/menu/treeselect',
    method: 'get'
  })
}

/**
 * 根据角色ID查询菜单下拉树结构
 */
export function roleMenuTreeselect(roleId: number): Promise<any> {
  return request({
    url: '/system/menu/roleMenuTreeselect/' + roleId,
    method: 'get'
  })
}

/**
 * 新增菜单
 */
export function addMenu(data: Partial<Menu> | Record<string, any>): Promise<any> {
  return request({
    url: '/system/menu',
    method: 'post',
    data: data
  })
}

/**
 * 修改菜单
 */
export function updateMenu(data: Partial<Menu> | Record<string, any>): Promise<any> {
  return request({
    url: '/system/menu',
    method: 'put',
    data: data
  })
}

/**
 * 删除菜单
 */
export function delMenu(menuId: number): Promise<any> {
  return request({
    url: '/system/menu/' + menuId,
    method: 'delete'
  })
}
