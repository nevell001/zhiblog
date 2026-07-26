import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import ArticleTOC from './ArticleTOC.vue'

describe('ArticleTOC 组件测试', () => {
  it('应该导出 ArticleTOC 组件', () => {
    expect(ArticleTOC).toBeDefined()
    expect(typeof ArticleTOC).toBe('object')
  })
})
