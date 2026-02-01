import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import DictData from './data.vue'

describe('DictData 视图组件测试', () => {
  it('应该导出 DictData 组件', () => {
    expect(DictData).toBeDefined()
    expect(typeof DictData).toBe('object')
  })
})