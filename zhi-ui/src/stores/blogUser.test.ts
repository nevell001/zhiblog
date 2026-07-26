import { describe, it, expect, beforeEach, vi } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useBlogUserStore } from './blogUser'
import { blogLogin, getBlogUserInfo, blogLogout } from '@/api/blog/auth'

// Mock API functions
vi.mock('@/api/blog/auth', () => ({
  blogLogin: vi.fn(),
  blogRegister: vi.fn(),
  blogLogout: vi.fn(),
  getBlogUserInfo: vi.fn(),
  sendRegisterCode: vi.fn()
}))

// Mock localStorage
const localStorageMock = {
  getItem: vi.fn(() => ''),
  setItem: vi.fn(),
  removeItem: vi.fn()
}
global.localStorage = localStorageMock as any

describe('BlogUser Store 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    localStorageMock.getItem.mockReturnValue('')
    setActivePinia(createPinia())
  })

  describe('初始状态', () => {
    it('应该初始化为默认值', () => {
      const store = useBlogUserStore()
      expect(store.token).toBe('')
      expect(store.name).toBe('')
      expect(store.avatar).toBe('')
      expect(store.email).toBe('')
      expect(store.userId).toBe(0)
      expect(store.isLoggedIn).toBe(false)
    })
  })

  describe('isLogin getter', () => {
    it('token 为空时应该返回 false', () => {
      const store = useBlogUserStore()
      expect(store.isLogin).toBe(false)
    })

    it('token 存在且 isLoggedIn 为 true 时应该返回 true', () => {
      const store = useBlogUserStore()
      store.token = 'test-token'
      store.isLoggedIn = true
      expect(store.isLogin).toBe(true)
    })
  })

  describe('login action', () => {
    it('应该成功登录并设置 token', async () => {
      const store = useBlogUserStore()
      vi.mocked(blogLogin).mockResolvedValue({ data: 'test-token' })
      vi.mocked(getBlogUserInfo).mockResolvedValue({
        data: {
          user: {
            userName: 'Test User',
            avatar: '/avatar/test.jpg',
            email: 'test@example.com',
            userId: 1
          }
        }
      })

      await store.login({ username: 'test', password: '123456' })

      expect(store.token).toBe('test-token')
      expect(store.isLoggedIn).toBe(true)
      expect(store.name).toBe('Test User')
    })
  })

  describe('getUserInfo action', () => {
    it('应该成功获取用户信息', async () => {
      const store = useBlogUserStore()
      vi.mocked(getBlogUserInfo).mockResolvedValue({
        data: {
          user: {
            userName: 'Test User',
            avatar: '/avatar/test.jpg',
            email: 'test@example.com',
            userId: 1
          }
        }
      })

      await store.getUserInfo()

      expect(store.name).toBe('Test User')
      expect(store.email).toBe('test@example.com')
      expect(store.userId).toBe(1)
      expect(store.isLoggedIn).toBe(true)
    })
  })

  describe('logOut action', () => {
    it('应该成功退出登录', async () => {
      const store = useBlogUserStore()
      store.token = 'test-token'
      store.name = 'Test User'
      store.isLoggedIn = true

      vi.mocked(blogLogout).mockResolvedValue({})

      await store.logOut()

      expect(store.token).toBe('')
      expect(store.name).toBe('')
      expect(store.isLoggedIn).toBe(false)
    })
  })
})
