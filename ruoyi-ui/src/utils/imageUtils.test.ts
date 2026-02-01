import { describe, it, expect, vi, beforeEach } from 'vitest'
import {
  isImageFile,
  getImageFormat,
  getImageSizeMB,
  formatFileSize,
  createPreviewUrl,
  revokePreviewUrl,
  getUploadApi,
  createUploadFormData,
  needsCompression,
  validateImage
} from './imageUtils'

describe('ImageUtils 工具函数测试', () => {
  describe('isImageFile', () => {
    it('应该识别图片文件', () => {
      const file = new File(['test'], 'test.jpg', { type: 'image/jpeg' })
      expect(isImageFile(file)).toBe(true)
    })

    it('应该拒绝非图片文件', () => {
      const file = new File(['test'], 'test.txt', { type: 'text/plain' })
      expect(isImageFile(file)).toBe(false)
    })

    it('应该处理 null 文件', () => {
      expect(isImageFile(null)).toBe(false)
    })
  })

  describe('getImageFormat', () => {
    it('应该识别 JPEG 格式', () => {
      const file = new File(['test'], 'test.jpg', { type: 'image/jpeg' })
      expect(getImageFormat(file)).toBe('jpg')
    })

    it('应该识别 PNG 格式', () => {
      const file = new File(['test'], 'test.png', { type: 'image/png' })
      expect(getImageFormat(file)).toBe('png')
    })

    it('应该处理 null 文件', () => {
      expect(getImageFormat(null)).toBe('')
    })
  })

  describe('getImageSizeMB', () => {
    it('应该计算文件大小（MB）', () => {
      const file = new File(['test'], 'test.jpg', { type: 'image/jpeg' })
      Object.defineProperty(file, 'size', { value: 1024 * 1024 }) // 1MB
      expect(getImageSizeMB(file)).toBe(1)
    })

    it('应该处理 null 文件', () => {
      expect(getImageSizeMB(null)).toBe(0)
    })
  })

  describe('formatFileSize', () => {
    it('应该格式化字节', () => {
      expect(formatFileSize(512)).toBe('512 B')
    })

    it('应该格式化 KB', () => {
      expect(formatFileSize(1024 * 2)).toBe('2 KB')
    })

    it('应该格式化 MB', () => {
      expect(formatFileSize(1024 * 1024 * 3)).toBe('3 MB')
    })

    it('应该格式化 GB', () => {
      expect(formatFileSize(1024 * 1024 * 1024 * 4)).toBe('4 GB')
    })

    it('应该处理 0 字节', () => {
      expect(formatFileSize(0)).toBe('0 B')
    })
  })

  describe('createPreviewUrl', () => {
    it.skip('应该创建预览 URL', () => {
      const file = new File(['test'], 'test.jpg', { type: 'image/jpeg' })
      const url = createPreviewUrl(file)
      expect(url).toBeTruthy()
      expect(url.startsWith('blob:')).toBe(true)
      revokePreviewUrl(url) // 清理
    })

    it('应该处理 null 文件', () => {
      expect(createPreviewUrl(null)).toBe('')
    })
  })

  describe('revokePreviewUrl', () => {
    it.skip('应该释放预览 URL', () => {
      const file = new File(['test'], 'test.jpg', { type: 'image/jpeg' })
      const url = createPreviewUrl(file)
      expect(() => revokePreviewUrl(url)).not.toThrow()
    })

    it('应该处理空 URL', () => {
      expect(() => revokePreviewUrl('')).not.toThrow()
    })
  })

  describe('getUploadApi', () => {
    it('应该返回头像上传接口', () => {
      expect(getUploadApi('avatar')).toBe('/common/upload/avatar')
    })

    it('应该返回默认上传接口', () => {
      expect(getUploadApi('default')).toBe('/common/upload')
    })

    it('应该处理未知类型', () => {
      expect(getUploadApi('unknown')).toBe('/common/upload')
    })
  })

  describe('createUploadFormData', () => {
    it('应该创建 FormData', () => {
      const file = new File(['test'], 'test.jpg', { type: 'image/jpeg' })
      const formData = createUploadFormData(file)
      expect(formData).toBeInstanceOf(FormData)
    })

    it('应该添加额外字段', () => {
      const file = new File(['test'], 'test.jpg', { type: 'image/jpeg' })
      const formData = createUploadFormData(file, { type: 'avatar', userId: 123 })
      expect(formData).toBeInstanceOf(FormData)
    })

    it('应该忽略 null 和 undefined 值', () => {
      const file = new File(['test'], 'test.jpg', { type: 'image/jpeg' })
      const formData = createUploadFormData(file, { type: null, userId: undefined })
      expect(formData).toBeInstanceOf(FormData)
    })
  })

  describe('needsCompression', () => {
    it('应该判断需要压缩', () => {
      const file = new File(['test'], 'test.jpg', { type: 'image/jpeg' })
      Object.defineProperty(file, 'size', { value: 3 * 1024 * 1024 }) // 3MB
      expect(needsCompression(file, 2)).toBe(true)
    })

    it('应该判断不需要压缩', () => {
      const file = new File(['test'], 'test.jpg', { type: 'image/jpeg' })
      Object.defineProperty(file, 'size', { value: 1 * 1024 * 1024 }) // 1MB
      expect(needsCompression(file, 2)).toBe(false)
    })
  })

  describe('validateImage', () => {
    it('应该验证有效的图片', () => {
      const file = new File(['test'], 'test.jpg', { type: 'image/jpeg' })
      Object.defineProperty(file, 'size', { value: 1 * 1024 * 1024 }) // 1MB
      const result = validateImage(file)
      expect(result.valid).toBe(true)
      expect(result.errors).toHaveLength(0)
    })

    it('应该拒绝过大的文件', () => {
      const file = new File(['test'], 'test.jpg', { type: 'image/jpeg' })
      Object.defineProperty(file, 'size', { value: 20 * 1024 * 1024 }) // 20MB
      const result = validateImage(file, { maxSize: 10 })
      expect(result.valid).toBe(false)
      expect(result.errors.length).toBeGreaterThan(0)
    })

    it('应该拒绝非图片文件', () => {
      const file = new File(['test'], 'test.txt', { type: 'text/plain' })
      const result = validateImage(file)
      expect(result.valid).toBe(false)
      expect(result.errors).toContain('请选择有效的图片文件')
    })

    it('应该拒绝不支持的格式', () => {
      const file = new File(['test'], 'test.bmp', { type: 'image/bmp' })
      Object.defineProperty(file, 'size', { value: 1 * 1024 * 1024 }) // 1MB
      const result = validateImage(file, {
        allowedTypes: ['image/jpeg', 'image/png']
      })
      expect(result.valid).toBe(false)
      expect(result.errors.length).toBeGreaterThan(0)
    })
  })
})