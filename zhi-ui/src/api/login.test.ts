import { describe, it, expect, vi, beforeEach } from 'vitest'
import { logout, getCodeImg } from './login'
import request from '@/utils/request'

// Mock request module
vi.mock('@/utils/request')
const mockRequest = vi.mocked(request)

describe('login API', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('logout', () => {
    it('应该导出 logout 函数', () => {
      expect(logout).toBeDefined()
      expect(typeof logout).toBe('function')
    })

    it('应该调用 POST /logout', async () => {
      mockRequest.mockResolvedValue({ code: 200 })

      await logout()

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/logout',
        method: 'post'
      })
    })
  })

  describe('getCodeImg', () => {
    it('应该导出 getCodeImg 函数', () => {
      expect(getCodeImg).toBeDefined()
      expect(typeof getCodeImg).toBe('function')
    })

    it('应该调用 GET /captchaImage 并设置 isToken: false 和超时时间', async () => {
      mockRequest.mockResolvedValue({ img: 'base64-image-data' })

      await getCodeImg()

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/captchaImage',
        method: 'get',
        headers: {
          isToken: false
        },
        timeout: 20000
      })
    })
  })
})
