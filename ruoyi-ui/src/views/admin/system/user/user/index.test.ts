import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import UserManage from './index.vue'

describe('UserManage 视图组件测试', () => {
  it('应该导出 UserManage 组件', () => {
    expect(UserManage).toBeDefined()
    expect(typeof UserManage).toBe('object')
  })
})