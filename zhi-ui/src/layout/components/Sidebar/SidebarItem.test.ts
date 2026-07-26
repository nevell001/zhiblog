import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import SidebarItem from './SidebarItem.vue'

describe('SidebarItem 组件测试', () => {
  it('应该导出 SidebarItem 组件', () => {
    expect(SidebarItem).toBeDefined()
    expect(typeof SidebarItem).toBe('object')
  })
})
