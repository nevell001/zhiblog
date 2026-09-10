import { describe, it, expect, vi, beforeEach } from 'vitest'
import { upload, uploadAvatar } from './upload'
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
})
