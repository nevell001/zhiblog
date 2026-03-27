import { describe, it, expect, vi, beforeEach } from 'vitest'
import {
  upload,
  uploadCompressed,
  uploadAvatar,
  uploadThumbnail,
  uploadArticleCover,
  uploadMobileImage
} from './upload'
import request from '@/utils/request'

// Mock request module
vi.mock('@/utils/request')
const mockRequest = vi.mocked(request)

describe('Upload API 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('upload', () => {
    it('应该导出 upload 函数', () => {
      expect(upload).toBeDefined()
      expect(typeof upload).toBe('function')
    })

    it('应该调用通用文件上传', async () => {
      mockRequest.mockResolvedValue({ code: 200, url: '/uploads/file.jpg' })

      const formData = new FormData()
      formData.append('file', new File([''], 'file.jpg'))

      await upload(formData)

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/common/upload',
        method: 'post',
        data: formData,
        headers: { 'Content-Type': 'multipart/form-data' }
      })
    })
  })

  describe('uploadCompressed', () => {
    it('应该导出 uploadCompressed 函数', () => {
      expect(uploadCompressed).toBeDefined()
      expect(typeof uploadCompressed).toBe('function')
    })

    it('应该调用压缩文件上传', async () => {
      mockRequest.mockResolvedValue({ code: 200, url: '/uploads/compressed.jpg' })

      const formData = new FormData()
      await uploadCompressed(formData)

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/common/upload/compressed',
        method: 'post',
        data: formData,
        headers: { 'Content-Type': 'multipart/form-data' }
      })
    })
  })

  describe('uploadAvatar', () => {
    it('应该导出 uploadAvatar 函数', () => {
      expect(uploadAvatar).toBeDefined()
      expect(typeof uploadAvatar).toBe('function')
    })

    it('应该调用头像上传', async () => {
      mockRequest.mockResolvedValue({ code: 200, url: '/uploads/avatar.jpg' })

      const formData = new FormData()
      await uploadAvatar(formData)

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/common/upload/avatar',
        method: 'post',
        data: formData,
        headers: { 'Content-Type': 'multipart/form-data' }
      })
    })
  })

  describe('uploadThumbnail', () => {
    it('应该导出 uploadThumbnail 函数', () => {
      expect(uploadThumbnail).toBeDefined()
      expect(typeof uploadThumbnail).toBe('function')
    })

    it('应该调用缩略图上传', async () => {
      mockRequest.mockResolvedValue({ code: 200, url: '/uploads/thumb.jpg' })

      const formData = new FormData()
      await uploadThumbnail(formData)

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/common/upload/thumbnail',
        method: 'post',
        data: formData,
        headers: { 'Content-Type': 'multipart/form-data' }
      })
    })
  })

  describe('uploadArticleCover', () => {
    it('应该导出 uploadArticleCover 函数', () => {
      expect(uploadArticleCover).toBeDefined()
      expect(typeof uploadArticleCover).toBe('function')
    })

    it('应该调用文章封面图上传', async () => {
      mockRequest.mockResolvedValue({ code: 200, url: '/uploads/cover.jpg' })

      const formData = new FormData()
      await uploadArticleCover(formData)

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/common/upload/article-cover',
        method: 'post',
        data: formData,
        headers: { 'Content-Type': 'multipart/form-data' }
      })
    })
  })

  describe('uploadMobileImage', () => {
    it('应该导出 uploadMobileImage 函数', () => {
      expect(uploadMobileImage).toBeDefined()
      expect(typeof uploadMobileImage).toBe('function')
    })

    it('应该调用移动端适配图片上传', async () => {
      mockRequest.mockResolvedValue({ code: 200, url: '/uploads/mobile.jpg' })

      const formData = new FormData()
      await uploadMobileImage(formData)

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/common/upload/mobile',
        method: 'post',
        data: formData,
        headers: { 'Content-Type': 'multipart/form-data' }
      })
    })
  })
})
