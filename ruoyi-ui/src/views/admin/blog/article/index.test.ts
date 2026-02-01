import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import ArticleManage from './index.vue'

describe('ArticleManage 视图组件测试', () => {
  it('应该导出 ArticleManage 组件', () => {
    expect(ArticleManage).toBeDefined()
    expect(typeof ArticleManage).toBe('object')
  })
})