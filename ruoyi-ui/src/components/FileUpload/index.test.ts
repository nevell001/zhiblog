import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import FileUpload from './index.vue'

describe('FileUpload 组件测试', () => {
  it('应该导出 FileUpload 组件', () => {
    expect(FileUpload).toBeDefined()
    expect(typeof FileUpload).toBe('object')
  })
})
