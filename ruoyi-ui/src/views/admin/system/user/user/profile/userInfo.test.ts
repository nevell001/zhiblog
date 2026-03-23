import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import UserInfo from './userInfo.vue'

describe('UserInfo 视图组件测试', () => {
  it('应该导出 UserInfo 组件', () => {
    expect(UserInfo).toBeDefined()
    expect(typeof UserInfo).toBe('object')
  })
})
