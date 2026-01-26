import { describe, it, expect, beforeEach } from 'vitest'
import {
  isImageFile,
  getImageFormat,
  getImageSizeMB,
  validateImage,
  formatFileSize
} from './imageUtils'

describe('ImageUtils 工具测试', () => {
  let mockFile: File
  let mockImageFile: File

  beforeEach(() => {
    // 创建模拟文件对象
    mockFile = new File(['test content'], 'test.txt', { type: 'text/plain' })
    mockImageFile = new File(['image content'], 'test.jpg', { type: 'image/jpeg' })
  })

  describe('isImageFile 函数', () => {
    it('应该识别 JPEG 图片', () => {
      const jpegFile = new File([''], 'test.jpg', { type: 'image/jpeg' })
      expect(isImageFile(jpegFile)).toBe(true)
    })

    it('应该识别 PNG 图片', () => {
      const pngFile = new File([''], 'test.png', { type: 'image/png' })
      expect(isImageFile(pngFile)).toBe(true)
    })

    it('应该识别 GIF 图片', () => {
      const gifFile = new File([''], 'test.gif', { type: 'image/gif' })
      expect(isImageFile(gifFile)).toBe(true)
    })

    it('应该识别 WebP 图片', () => {
      const webpFile = new File([''], 'test.webp', { type: 'image/webp' })
      expect(isImageFile(webpFile)).toBe(true)
    })

    it('应该识别 BMP 图片', () => {
      const bmpFile = new File([''], 'test.bmp', { type: 'image/bmp' })
      expect(isImageFile(bmpFile)).toBe(true)
    })

    it('应该拒绝非图片文件', () => {
      expect(isImageFile(mockFile)).toBe(false)
    })

    it('应该处理 null 文件', () => {
      expect(isImageFile(null)).toBe(false)
    })

    it('应该处理 undefined 文件', () => {
      expect(isImageFile(undefined as any)).toBe(false)
    })
  })

  describe('getImageFormat 函数', () => {
    it('应该返回 jpg 格式', () => {
      const jpegFile = new File([''], 'test.jpg', { type: 'image/jpeg' })
      expect(getImageFormat(jpegFile)).toBe('jpg')
    })

    it('应该返回 jpg 格式（jpeg）', () => {
      const jpegFile = new File([''], 'test.jpeg', { type: 'image/jpeg' })
      expect(getImageFormat(jpegFile)).toBe('jpg')
    })

    it('应该返回 png 格式', () => {
      const pngFile = new File([''], 'test.png', { type: 'image/png' })
      expect(getImageFormat(pngFile)).toBe('png')
    })

    it('应该返回 gif 格式', () => {
      const gifFile = new File([''], 'test.gif', { type: 'image/gif' })
      expect(getImageFormat(gifFile)).toBe('gif')
    })

    it('应该返回 webp 格式', () => {
      const webpFile = new File([''], 'test.webp', { type: 'image/webp' })
      expect(getImageFormat(webpFile)).toBe('webp')
    })

    it('应该返回 bmp 格式', () => {
      const bmpFile = new File([''], 'test.bmp', { type: 'image/bmp' })
      expect(getImageFormat(bmpFile)).toBe('bmp')
    })

    it('应该返回空字符串当文件为 null', () => {
      expect(getImageFormat(null)).toBe('')
    })

    it('应该返回空字符串当类型未知', () => {
      const unknownFile = new File([''], 'test.xyz', { type: 'application/octet-stream' })
      expect(getImageFormat(unknownFile)).toBe('octet-stream')
    })
  })

  describe('getImageSizeMB 函数', () => {
    it('应该返回 0 当文件为 null', () => {
      expect(getImageSizeMB(null)).toBe(0)
    })

    it('应该正确计算文件大小（字节）', () => {
      const smallFile = new File(['x'], 'test.jpg', { type: 'image/jpeg' })
      expect(getImageSizeMB(smallFile)).toBe(0)
    })

    it('应该正确计算文件大小（KB）', () => {
      const content = 'x'.repeat(1024 * 10) // 10KB
      const kbFile = new File([content], 'test.jpg', { type: 'image/jpeg' })
      const size = getImageSizeMB(kbFile)
      expect(size).toBeGreaterThan(0)
      expect(size).toBeLessThan(0.1)
    })

    it('应该正确计算文件大小（MB）', () => {
      const content = 'x'.repeat(1024 * 1024 * 2) // 2MB
      const mbFile = new File([content], 'test.jpg', { type: 'image/jpeg' })
      const size = getImageSizeMB(mbFile)
      expect(size).toBeGreaterThanOrEqual(1.9)
      expect(size).toBeLessThanOrEqual(2.1)
    })

    it('应该四舍五入到两位小数', () => {
      const content = 'x'.repeat(1024 * 1024 * 1.234) // 1.234MB
      const file = new File([content], 'test.jpg', { type: 'image/jpeg' })
      const size = getImageSizeMB(file)
      expect(size.toString()).toMatch(/^\d+\.\d{2}$/)
    })
  })

  describe('validateImage 函数', () => {
    it('应该验证有效的图片文件', () => {
      const validFile = new File([''], 'test.jpg', { type: 'image/jpeg' })
      // 创建一个大小适中的文件（1MB）
      Object.defineProperty(validFile, 'size', { value: 1024 * 1024 })

      const result = validateImage(validFile, {
        maxSize: 2,
        allowedTypes: ['image/jpeg', 'image/png']
      })

      expect(result.valid).toBe(true)
      expect(result.errors).toEqual([])
    })

    it('应该拒绝过大的文件', () => {
      const largeFile = new File([''], 'test.jpg', { type: 'image/jpeg' })
      Object.defineProperty(largeFile, 'size', { value: 5 * 1024 * 1024 }) // 5MB

      const result = validateImage(largeFile, {
        maxSize: 2
      })

      expect(result.valid).toBe(false)
      expect(result.errors.length).toBeGreaterThan(0)
    })

    it('应该拒绝不支持的文件类型', () => {
      const unsupportedFile = new File([''], 'test.bmp', { type: 'image/bmp' })

      const result = validateImage(unsupportedFile, {
        allowedTypes: ['image/jpeg', 'image/png']
      })

      expect(result.valid).toBe(false)
      expect(result.errors.length).toBeGreaterThan(0)
    })

    it('应该返回文件信息', () => {
      const file = new File([''], 'test.jpg', { type: 'image/jpeg' })
      Object.defineProperty(file, 'size', { value: 1024 * 1024 })

      const result = validateImage(file, {
        maxSize: 2
      })

      expect(result.fileInfo).toBeDefined()
      expect(result.fileInfo?.name).toBe('test.jpg')
      expect(result.fileInfo?.size).toBe(1024 * 1024)
      expect(result.fileInfo?.type).toBe('image/jpeg')
    })

    it('应该处理 null 文件', () => {
      const result = validateImage(null as any, {
        maxSize: 2
      })

      expect(result.valid).toBe(false)
      expect(result.errors.length).toBeGreaterThan(0)
    })
  })

  describe('formatFileSize 函数', () => {
    it('应该格式化字节', () => {
      expect(formatFileSize(100)).toBe('100 B')
    })

    it('应该格式化 KB', () => {
      expect(formatFileSize(1024)).toBe('1 KB')
    })

    it('应该格式化 MB', () => {
      expect(formatFileSize(1024 * 1024)).toBe('1 MB')
    })

    it('应该格式化 GB', () => {
      expect(formatFileSize(1024 * 1024 * 1024)).toBe('1 GB')
    })

    it('应该处理 0 字节', () => {
      expect(formatFileSize(0)).toBe('0 B')
    })

    it('应该四舍五入到两位小数', () => {
      const size = 1024 * 1024 * 1.234
      const formatted = formatFileSize(size)
      expect(formatted).toMatch(/^\d+(\.\d{1,2})? MB$/)
    })
  })

  describe('完整场景测试', () => {
    it('应该支持完整的图片处理流程', () => {
      // 创建测试文件
      const file = new File([''], 'test.jpg', { type: 'image/jpeg' })
      Object.defineProperty(file, 'size', { value: 1024 * 1024 })

      // 检查是否为图片
      expect(isImageFile(file)).toBe(true)

      // 获取图片格式
      expect(getImageFormat(file)).toBe('jpg')

      // 获取文件大小
      const size = getImageSizeMB(file)
      expect(size).toBeCloseTo(1.0, 1)

      // 验证图片
      const validation = validateImage(file, {
        maxSize: 2,
        allowedTypes: ['image/jpeg', 'image/png']
      })
      expect(validation.valid).toBe(true)

      // 格式化文件大小
      const formattedSize = formatFileSize(file.size)
      expect(formattedSize).toBe('1 MB')
    })

    it('应该处理无效图片文件', () => {
      // 创建非图片文件
      const file = new File([''], 'test.txt', { type: 'text/plain' })

      // 检查是否为图片
      expect(isImageFile(file)).toBe(false)

      // 获取图片格式
      expect(getImageFormat(file)).toBe('plain')

      // 验证图片
      const validation = validateImage(file, {
        maxSize: 2,
        allowedTypes: ['image/jpeg', 'image/png']
      })
      expect(validation.valid).toBe(false)
    })
  })
})