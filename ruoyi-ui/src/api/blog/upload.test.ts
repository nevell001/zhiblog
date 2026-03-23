import { describe, it, expect, vi, beforeEach } from 'vitest'
import { uploadAvatar, uploadCover, uploadImage } from './upload'

// Mock request module
vi.mock('@/utils/request', () => ({
  request: vi.fn()
}))

describe('Upload API 测试', () => {
  const request = vi.fn()
  const mockRequest = vi.mocked(request)

  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('uploadAvatar', () => {
    it('应该导出 uploadAvatar 函数', () => {
      expect(uploadAvatar).toBeDefined()
      expect(typeof uploadAvatar).toBe('function')
    })

    it('应该调用文件上传', async () => {
      mockRequest.mockResolvedValue({ code: 200, url: '/uploads/avatar.jpg' })

      const formData = new FormData()
      formData.append('file', new File([''], 'avatar.jpg'))

      await uploadAvatar(formData)

      expect(mockRequest).toHaveBeenCalled()
    })
  })

  describe('uploadCover', () => {
    it('应该导出 uploadCover 函数', () => {
      expect(uploadCover).toBeDefined()
      expect(typeof uploadCover).toBe('function')
    })

    it('应该调用文件上传', async () => {
      mockRequest.mockResolvedValue({ code: 200, url: '/uploads/cover.jpg' })

      const formData = new FormData()
      formData.append('file', new File([''], 'cover.jpg'))

      await uploadCover(formData)

      expect(mockRequest).toHaveBeenCalled()
    })
  })

  describe('uploadImage', () => {
    it('应该导出 uploadImage 函数', () => {
      expect(uploadImage).toBeDefined()
      expect(typeof uploadImage).toBe('function')
    })

    it('应该调用文件上传', async () => {
      mockRequest.mockResolvedValue({ code: 200, url: '/uploads/image.jpg' })

      const formData = new FormData()
      formData.append('file', new File([''], 'image.jpg'))

      await uploadImage(formData)

      expect(mockRequest).toHaveBeenCalled()
    })
  })
})
