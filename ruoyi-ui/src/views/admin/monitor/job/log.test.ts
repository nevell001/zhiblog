import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import JobLog from './log.vue'

describe('JobLog 视图组件测试', () => {
  it('应该导出 JobLog 组件', () => {
    expect(JobLog).toBeDefined()
    expect(typeof JobLog).toBe('object')
  })
})