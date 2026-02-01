import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import UserAvatar from './userAvatar.vue'

describe('UserAvatar 视图组件测试', () => {
  it('应该导出 UserAvatar 组件', () => {
    expect(UserAvatar).toBeDefined()
    expect(typeof UserAvatar).toBe('object')
  })
})