import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import RoleManage from './index.vue'

describe('RoleManage 视图组件测试', () => {
  it('应该导出 RoleManage 组件', () => {
    expect(RoleManage).toBeDefined()
    expect(typeof RoleManage).toBe('object')
  })
})
