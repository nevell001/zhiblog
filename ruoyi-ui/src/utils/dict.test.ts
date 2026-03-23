import { describe, it, expect, vi, beforeEach } from 'vitest'
import { useDict } from './dict'

// Mock stores and API
vi.mock('@/stores/dict', () => ({
  useDictStore: vi.fn()
}))
vi.mock('@/api/system/dict/data', () => ({
  getDicts: vi.fn()
}))

describe('useDict hook 测试', () => {
  const mockUseDictStore = vi.mocked(useDictStore)
  const mockGetDicts = vi.mocked(getDicts)

  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('应该导出 useDict 函数', () => {
    expect(useDict).toBeDefined()
    expect(typeof useDict).toBe('function')
  })

  it('应该返回响应式对象', () => {
    mockUseDictStore.mockReturnValue({
      getDict: vi.fn(() => undefined),
      setDict: vi.fn(() => undefined)
    })

    const result = useDict('user_type')

    expect(result).toBeDefined()
    expect(typeof result.value).toBe('object')
    expect(typeof result.user_type).toBe('object')
    expect(typeof result.user_type.value).toBe('object')
  })

  it('应该从 store 获取字典', () => {
    const mockDicts = [
      { dictLabel: '管理员', dictValue: '0', listClass: 'primary', cssClass: 'success-tag' }
    ]
    mockUseDictStore.mockReturnValue({
      getDict: vi.fn(() => mockDicts),
      setDict: vi.fn(() => undefined)
    })

    const result = useDict('user_type')

    expect(mockUseDictStore().getDict).toHaveBeenCalledWith('user_type')
  })

  it('应该处理字典不存在的情况', () => {
    mockUseDictStore.mockReturnValue({
      getDict: vi.fn(() => undefined),
      setDict: vi.fn(() => undefined)
    })
    mockGetDicts.mockResolvedValue({ data: [] })

    const result = useDict('user_type')

    expect(mockGetDicts).toHaveBeenCalledWith('user_type')
    expect(mockGetDicts).toHaveBeenCalledOnce()
  })

  it('应该支持多个字典类型', () => {
    const mockUseDictStore = vi.mocked(useDictStore)
    const mockDicts1 = [
      { dictLabel: '管理员', dictValue: '0', listClass: 'primary', cssClass: 'success-tag' }
    ]
    const mockDicts2 = [
      { dictLabel: '普通用户', dictValue: '1', listClass: 'info', cssClass: 'info-tag' }
    ]
    mockUseDictStore.mockReturnValue({
      getDict: vi.fn((type: string) => type === 'user_type' ? mockDicts1 : mockDicts2),
      setDict: vi.fn(() => undefined)
    })

    const result = useDict('user_type', 'status')

    expect(result.user_type.value).toEqual(mockDicts1)
    expect(result.status.value).toEqual(mockDicts2)
  })

  it('应该缓存字典到 store', () => {
    const mockDicts = [
      { dictLabel: '管理员', dictValue: '0', listClass: 'primary', cssClass: 'success-tag' }
    ]
    const mockSetDict = vi.fn()
    mockUseDictStore.mockReturnValue({
      getDict: vi.fn(() => undefined),
      setDict: mockSetDict
    })
    mockGetDicts.mockResolvedValue({ data: mockDicts })

    useDict('user_type')

    expect(mockSetDict).toHaveBeenCalledWith('user_type', mockDicts)
  })
})
