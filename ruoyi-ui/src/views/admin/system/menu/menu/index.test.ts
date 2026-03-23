import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import MenuManage from './index.vue'

describe('MenuManage 视图组件测试', () => {
  it('应该导出 MenuManage 组件', () => {
    expect(MenuManage).toBeDefined()
    expect(typeof MenuManage).toBe('object')
  })
})
