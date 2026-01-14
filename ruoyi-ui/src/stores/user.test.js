import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'

// Mock the require function globally before any imports
const originalRequire = global.require
global.require = vi.fn((path) => {
  if (path === '@/assets/images/profile.jpg') {
    return '/assets/images/profile.jpg'
  }
  // For other paths, try to use the original require or return null
  try {
    return originalRequire ? originalRequire(path) : null
  } catch (e) {
    return null
  }
})

// Mock API
vi.mock('@/api/login', () => ({
  login: vi.fn(),
  logout: vi.fn(),
  getInfo: vi.fn()
}))

// Mock auth
vi.mock('@/utils/auth', () => ({
  getToken: vi.fn(),
  setToken: vi.fn(),
  removeToken: vi.fn()
}))

// 获取模拟的函数
import { login as mockLogin, logout as mockLogout, getInfo as mockGetInfo } from '@/api/login'
import { getToken as mockGetToken, setToken as mockSetToken, removeToken as mockRemoveToken } from '@/utils/auth'

// 设置 mockGetToken 的默认返回值
mockGetToken.mockReturnValue('initial-token')

// Import the store after mocking
import { useUserStore } from '@/stores/user'

// Mock API
vi.mock('@/api/login', () => ({
  login: vi.fn(),
  logout: vi.fn(),
  getInfo: vi.fn()
}))

// Mock auth
vi.mock('@/utils/auth', () => ({
  getToken: vi.fn(),
  setToken: vi.fn(),
  removeToken: vi.fn()
}))

// 获取模拟的函数
import { login as mockLogin, logout as mockLogout, getInfo as mockGetInfo } from '@/api/login'
import { getToken as mockGetToken, setToken as mockSetToken, removeToken as mockRemoveToken } from '@/utils/auth'

// 设置 mockGetToken 的默认返回值
mockGetToken.mockReturnValue('initial-token')

describe('User Store 测试', () => {
  let userStore

  beforeEach(() => {
    setActivePinia(createPinia())
    userStore = useUserStore()
    mockGetToken.mockReturnValue('initial-token')
    vi.clearAllMocks()
  })

  afterEach(() => {
    vi.restoreAllMocks()
  })

  describe('state 初始化', () => {
    it('应该正确初始化状态', () => {
      expect(userStore.token).toBeDefined()
      expect(userStore.name).toBe('')
      expect(userStore.avatar).toBe('')
      expect(userStore.roles).toEqual([])
      expect(userStore.permissions).toEqual([])
    })

    it('应该从 getToken 获取初始 token', () => {
      mockGetToken.mockReturnValue('test-token')
      setActivePinia(createPinia())
      const newUserStore = useUserStore()
      expect(newUserStore.token).toBe('test-token')
    })
  })

  describe('login action', () => {
    it('应该成功登录并设置 token', async () => {
      const userInfo = {
        username: 'admin',
        password: 'admin123',
        code: '1234',
        uuid: 'uuid-123'
      }
      const mockResponse = { token: 'new-token' }
      mockLogin.mockResolvedValue(mockResponse)

      await expect(userStore.login(userInfo)).resolves.toBeUndefined()

      expect(mockLogin).toHaveBeenCalledWith('admin', 'admin123', '1234', 'uuid-123')
      expect(mockSetToken).toHaveBeenCalledWith('new-token')
      expect(userStore.token).toBe('new-token')
    })

    it('应该处理登录失败', async () => {
      const userInfo = {
        username: 'admin',
        password: 'wrong-password',
        code: '1234',
        uuid: 'uuid-123'
      }
      const error = new Error('登录失败')
      mockLogin.mockRejectedValue(error)

      await expect(userStore.login(userInfo)).rejects.toThrow('登录失败')
      expect(mockLogin).toHaveBeenCalledWith('admin', 'wrong-password', '1234', 'uuid-123')
      expect(mockSetToken).not.toHaveBeenCalled()
    })

    it('应该去除用户名首尾空格', async () => {
      const userInfo = {
        username: '  admin  ',
        password: 'admin123',
        code: '1234',
        uuid: 'uuid-123'
      }
      mockLogin.mockResolvedValue({ token: 'token' })

      await userStore.login(userInfo)

      expect(mockLogin).toHaveBeenCalledWith('admin', 'admin123', '1234', 'uuid-123')
    })
  })

  describe('getInfo action', () => {
    it('应该成功获取用户信息', async () => {
      const mockResponse = {
        user: {
          userName: 'admin',
          avatar: '/avatar.jpg'
        },
        roles: ['admin'],
        permissions: ['*:*:*']
      }
      mockGetInfo.mockResolvedValue(mockResponse)

      const result = await userStore.getInfo()

      expect(mockGetInfo).toHaveBeenCalled()
      expect(userStore.name).toBe('admin')
      expect(userStore.roles).toEqual(['admin'])
      expect(userStore.permissions).toEqual(['*:*:*'])
      expect(result).toEqual(mockResponse)
    })

    it('应该处理空头像', async () => {
      const mockResponse = {
        user: {
          userName: 'admin',
          avatar: ''
        },
        roles: ['admin'],
        permissions: ['*:*:*']
      }
      mockGetInfo.mockResolvedValue(mockResponse)

      await userStore.getInfo()

      expect(userStore.avatar).toContain('profile.jpg')
    })

    it('应该处理 null 头像', async () => {
      const mockResponse = {
        user: {
          userName: 'admin',
          avatar: null
        },
        roles: ['admin'],
        permissions: ['*:*:*']
      }
      mockGetInfo.mockResolvedValue(mockResponse)

      await userStore.getInfo()

      expect(userStore.avatar).toContain('profile.jpg')
    })

    it('应该使用默认角色当 roles 为空', async () => {
      const mockResponse = {
        user: {
          userName: 'admin',
          avatar: '/avatar.jpg'
        },
        roles: [],
        permissions: []
      }
      mockGetInfo.mockResolvedValue(mockResponse)

      await userStore.getInfo()

      expect(userStore.roles).toEqual(['ROLE_DEFAULT'])
    })

    it('应该处理获取信息失败', async () => {
      const error = new Error('获取用户信息失败')
      mockGetInfo.mockRejectedValue(error)

      await expect(userStore.getInfo()).rejects.toThrow('获取用户信息失败')
    })
  })

  describe('logOut action', () => {
    it('应该成功登出并清除状态', async () => {
      userStore.token = 'test-token'
      userStore.roles = ['admin']
      userStore.permissions = ['*:*:*']
      mockLogout.mockResolvedValue({})

      await expect(userStore.logOut()).resolves.toBeUndefined()

      expect(mockLogout).toHaveBeenCalled()
      expect(userStore.token).toBe('')
      expect(userStore.roles).toEqual([])
      expect(userStore.permissions).toEqual([])
      expect(mockRemoveToken).toHaveBeenCalled()
    })

    it('应该处理登出失败', async () => {
      const error = new Error('登出失败')
      mockLogout.mockRejectedValue(error)

      await expect(userStore.logOut()).rejects.toThrow('登出失败')
    })
  })

  describe('完整场景测试', () => {
    it('应该支持完整的登录流程', async () => {
      // 登录
      const userInfo = {
        username: 'admin',
        password: 'admin123',
        code: '1234',
        uuid: 'uuid-123'
      }
      mockLogin.mockResolvedValue({ token: 'login-token' })
      await userStore.login(userInfo)

      expect(userStore.token).toBe('login-token')

      // 获取用户信息
      const mockResponse = {
        user: {
          userName: 'admin',
          avatar: '/avatar.jpg'
        },
        roles: ['admin'],
        permissions: ['*:*:*']
      }
      mockGetInfo.mockResolvedValue(mockResponse)
      await userStore.getInfo()

      expect(userStore.name).toBe('admin')
      expect(userStore.roles).toEqual(['admin'])
      expect(userStore.permissions).toEqual(['*:*:*'])

      // 登出
      mockLogout.mockResolvedValue({})
      await userStore.logOut()

      expect(userStore.token).toBe('')
      expect(userStore.roles).toEqual([])
      expect(userStore.permissions).toEqual([])
    })
  })
})