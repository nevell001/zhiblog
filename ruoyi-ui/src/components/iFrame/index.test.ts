import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import iFrame from './index.vue'

describe('iFrame 组件测试', () => {
  it('应该导出 iFrame 组件', () => {
    expect(iFrame).toBeDefined()
    expect(typeof iFrame).toBe('object')
  })
})