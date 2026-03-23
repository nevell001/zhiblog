import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import Editor from './index.vue'

describe('Editor 组件测试', () => {
  it('应该导出 Editor 组件', () => {
    expect(Editor).toBeDefined()
    expect(typeof Editor).toBe('object')
  })

  it('应该有组件名称', () => {
    expect(Editor.name).toBeTruthy()
  })
})
