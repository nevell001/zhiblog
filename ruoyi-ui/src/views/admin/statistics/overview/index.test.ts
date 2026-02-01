import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import OverviewStatistics from './index.vue'

describe('OverviewStatistics 视图组件测试', () => {
  it('应该导出 OverviewStatistics 组件', () => {
    expect(OverviewStatistics).toBeDefined()
    expect(typeof OverviewStatistics).toBe('object')
  })
})