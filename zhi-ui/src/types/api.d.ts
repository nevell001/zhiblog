/**
 * API 模块类型定义
 */

/**
 * 分页查询参数
 */
export interface PageParams {
  pageNum?: number
  pageSize?: number
  [key: string]: any
}

/**
 * 删除参数
 */
export interface DeleteParams {
  id?: number | number[]
}

/**
 * 状态变更参数
 */
export interface StatusParams {
  id?: number | number[]
  status: number
}

/**
 * 文章查询参数
 */
export interface ArticleParams extends PageParams {
  title?: string
  categoryId?: number
  status?: number
  isTop?: number
  tagId?: number
}

/**
 * 用户查询参数
 */
export interface UserParams extends PageParams {
  userName?: string
  phonenumber?: string
  status?: string
  deptId?: number
  roleId?: number
}

/**
 * 角色查询参数
 */
export interface RoleParams extends PageParams {
  roleName?: string
  roleKey?: string
  status?: string
}

/**
 * 部门查询参数
 */
export interface DeptParams extends PageParams {
  deptName?: string
  status?: string
  parentId?: number
}

/**
 * 菜单查询参数
 */
export interface MenuParams extends PageParams {
  menuName?: string
  status?: string
  menuType?: string
}

/**
 * 字典查询参数
 */
export interface DictParams extends PageParams {
  dictName?: string
  dictType?: string
  status?: string
}

/**
 * 岗位查询参数
 */
export interface PostParams extends PageParams {
  postCode?: string
  postName?: string
  status?: string
}

/**
 * 标签查询参数
 */
export interface TagParams extends PageParams {
  name?: string
  status?: string
}

/**
 * 通用查询响应
 */
export interface QueryResult<T = any> {
  code: number
  msg: string
  rows: T[]
  data?: T[]
  total: number
}

export interface DataResult<T = any> {
  code?: number
  msg?: string
  data: T
}

/**
 * 通用操作响应
 */
export interface OperResult {
  code: number
  msg: string
}
