import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import SelectUser from './selectUser.vue'

describe('SelectUser 视图组件测试', () => {
  it('应该导出 SelectUser 组件', () => {
    expect(SelectUser).toBeDefined()
    expect(typeof SelectUser).toBe('object')
  })
})
