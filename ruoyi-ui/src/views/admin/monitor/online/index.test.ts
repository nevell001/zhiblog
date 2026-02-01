import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import OnlineUser from './index.vue'

describe('OnlineUser 视图组件测试', () => {
  it('应该导出 OnlineUser 组件', () => {
    expect(OnlineUser).toBeDefined()
    expect(typeof OnlineUser).toBe('object')
  })
})