import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import UserStatistics from './index.vue'

describe('UserStatistics 视图组件测试', () => {
  it('应该导出 UserStatistics 组件', () => {
    expect(UserStatistics).toBeDefined()
    expect(typeof UserStatistics).toBe('object')
  })
})