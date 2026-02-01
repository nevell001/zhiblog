import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import DeptManage from './index.vue'

describe('DeptManage 视图组件测试', () => {
  it('应该导出 DeptManage 组件', () => {
    expect(DeptManage).toBeDefined()
    expect(typeof DeptManage).toBe('object')
  })
})