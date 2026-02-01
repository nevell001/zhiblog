import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import ImagePreview from './index.vue'

describe('ImagePreview 组件测试', () => {
  it('应该导出 ImagePreview 组件', () => {
    expect(ImagePreview).toBeDefined()
    expect(typeof ImagePreview).toBe('object')
  })
})