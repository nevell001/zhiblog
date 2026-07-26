import { vi } from 'vitest'

// Mock 全局对象
global.console = {
  ...console,
  error: vi.fn(),
  warn: vi.fn(),
  log: vi.fn()
}

// Mock require 函数
global.require = vi.fn((path: string) => {
  if (path === '@/assets/images/profile.jpg') {
    return '/assets/images/profile.jpg'
  }
  return null
})

// Mock localStorage
const localStorageMock = {
  getItem: vi.fn(),
  setItem: vi.fn(),
  removeItem: vi.fn(),
  clear: vi.fn()
}

global.localStorage = localStorageMock as Storage

// Mock sessionStorage
const sessionStorageMock = {
  getItem: vi.fn(),
  setItem: vi.fn(),
  removeItem: vi.fn(),
  clear: vi.fn()
}

global.sessionStorage = sessionStorageMock as Storage
