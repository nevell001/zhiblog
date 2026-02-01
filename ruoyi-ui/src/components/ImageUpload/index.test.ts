import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import ImageUpload from './index.vue'

describe('ImageUpload 组件测试', () => {
  it('应该导出 ImageUpload 组件', () => {
    expect(ImageUpload).toBeDefined()
    expect(typeof ImageUpload).toBe('object')
  })
})