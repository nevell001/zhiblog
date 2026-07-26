import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import DictManage from './index.vue'

describe('DictManage 视图组件测试', () => {
  it('应该导出 DictManage 组件', () => {
    expect(DictManage).toBeDefined()
    expect(typeof DictManage).toBe('object')
  })
})
