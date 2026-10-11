import { describe, it, expect, vi, beforeEach } from 'vitest'
import { getFrontFriendLinkList, applyFriendLink } from './friendLink'
import type { FriendLinkApplyForm } from './friendLink'
import type { FriendLink } from '@/types'
import request from '@/utils/request'

// Mock request module
vi.mock('@/utils/request')
const mockRequest = vi.mocked(request)

describe('Friend Link API 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('getFrontFriendLinkList', () => {
    it('应该导出 getFrontFriendLinkList 函数', () => {
      expect(getFrontFriendLinkList).toBeDefined()
      expect(typeof getFrontFriendLinkList).toBe('function')
    })

    it('应该调用 GET /system/friendLink/front/list', async () => {
      const friendLinks: FriendLink[] = [
        {
          id: 1,
          name: '友链1',
          url: 'https://link1.com',
          logo: '',
          orderNum: 0,
          createTime: '',
          updateTime: ''
        }
      ]
      mockRequest.mockResolvedValue(friendLinks)

      const result = await getFrontFriendLinkList()

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/system/friendLink/front/list',
        method: 'get',
        headers: { isToken: false }
      })
      expect(result).toEqual(friendLinks)
    })
  })

  describe('applyFriendLink', () => {
    it('应该导出 applyFriendLink 函数', () => {
      expect(applyFriendLink).toBeDefined()
      expect(typeof applyFriendLink).toBe('function')
    })

    it('应该调用 POST /system/friendLink/apply 并携带 code 与 uuid', async () => {
      mockRequest.mockResolvedValue({ code: 200 })

      const payload: FriendLinkApplyForm = {
        name: '测试站点',
        url: 'https://apply.example.com',
        email: 'apply@example.com',
        description: '一句话介绍',
        code: '1234',
        uuid: 'uuid-abc-123'
      }
      await applyFriendLink(payload)

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/system/friendLink/apply',
        method: 'post',
        data: payload,
        headers: { isToken: false }
      })
    })

    it('未开启验证码时应该允许不传 code 与 uuid', async () => {
      mockRequest.mockResolvedValue({ code: 200 })

      const payload: FriendLinkApplyForm = {
        name: '无验证码站点',
        url: 'https://nocaptcha.example.com'
      }
      await applyFriendLink(payload)

      expect(mockRequest).toHaveBeenCalledWith({
        url: '/system/friendLink/apply',
        method: 'post',
        data: payload,
        headers: { isToken: false }
      })
    })
  })
})
