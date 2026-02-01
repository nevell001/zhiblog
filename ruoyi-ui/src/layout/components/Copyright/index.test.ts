import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import Copyright from './index.vue'

describe('Copyright 组件测试', () => {
  it('应该导出 Copyright 组件', () => {
    expect(Copyright).toBeDefined()
    expect(typeof Copyright).toBe('object')
  })
})