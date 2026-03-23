import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import ArticleStatistics from './index.vue'

describe('ArticleStatistics 视图组件测试', () => {
  it('应该导出 ArticleStatistics 组件', () => {
    expect(ArticleStatistics).toBeDefined()
    expect(typeof ArticleStatistics).toBe('object')
  })
})
