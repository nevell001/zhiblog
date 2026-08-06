import { describe, it, expect, beforeEach, vi } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useBlogUserStore } from './blogUser'

// Mock API functions
vi.mock('@/api/blog/auth', () => ({
  blogRegister: vi.fn(),
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
})
