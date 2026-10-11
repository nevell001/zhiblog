import request from '@/utils/request'
import type { FriendLink } from '@/types'

/**
 * 查询前台友链列表（用于前台展示）
 */
export function getFrontFriendLinkList(): Promise<FriendLink[] & { data?: FriendLink[] }> {
  return request({
    url: '/system/friendLink/front/list',
    method: 'get',
    headers: { isToken: false }
  })
}

/**
 * 前台申请友链表单
 */
export interface FriendLinkApplyForm {
  /** 网站名称 */
  name: string
  /** 网站地址 */
  url: string
  /** 联系邮箱 */
  email?: string
  /** 站点描述 */
  description?: string
  /** 图形验证码（开启验证码时必填） */
  code?: string
  /** 验证码标识（开启验证码时必填） */
  uuid?: string
}

/**
 * 前台申请友链（提交后进入后台待审核）
 */
export function applyFriendLink(data: FriendLinkApplyForm): Promise<any> {
  return request({
    url: '/system/friendLink/apply',
    method: 'post',
    data: data,
    headers: { isToken: false }
  })
}
