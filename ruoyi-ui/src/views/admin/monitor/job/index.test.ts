import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import JobManage from './index.vue'

describe('JobManage 视图组件测试', () => {
  it('应该导出 JobManage 组件', () => {
    expect(JobManage).toBeDefined()
    expect(typeof JobManage).toBe('object')
  })
})
