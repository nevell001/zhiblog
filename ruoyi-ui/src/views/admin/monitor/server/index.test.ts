import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import ServerMonitor from './index.vue'

describe('ServerMonitor 视图组件测试', () => {
  it('应该导出 ServerMonitor 组件', () => {
    expect(ServerMonitor).toBeDefined()
    expect(typeof ServerMonitor).toBe('object')
  })
})
