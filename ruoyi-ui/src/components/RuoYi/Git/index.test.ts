import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import RuoYiGit from './index.vue'

describe('RuoYi Git 组件测试', () => {
  it('应该导出 RuoYiGit 组件', () => {
    expect(RuoYiGit).toBeDefined()
    expect(typeof RuoYiGit).toBe('object')
  })
})
