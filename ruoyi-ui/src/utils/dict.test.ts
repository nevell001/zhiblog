import { describe, it, expect, vi, beforeEach } from 'vitest'
import { useDict } from './dict'
import { useDictStore } from '@/stores/dict'
import { getDicts } from '@/api/system/dict/data'

// Mock API 和 store
vi.mock('@/api/system/dict/data')
vi.mock('@/stores/dict')

describe('useDict Hook 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('应该导出 useDict 函数', () => {
    expect(useDict).toBeDefined()
    expect(typeof useDict).toBe('function')
  })

  it('应该返回响应式引用对象', () => {
    const dictStoreMock = {
      getDict: vi.fn(() => null),
      setDict: vi.fn()
    }

    vi.mocked(useDictStore).mockReturnValue(dictStoreMock)
    vi.mocked(getDicts).mockResolvedValue({
      data: [
        { dictLabel: '标签1', dictValue: '1', listClass: 'tag', cssClass: 'class1' },
        { dictLabel: '标签2', dictValue: '2', listClass: 'tag', cssClass: 'class2' }
      ]
    })

    const result = useDict('dictType')

    expect(result).toBeDefined()
    expect(result.dictType.value).toBeDefined()
    expect(typeof result.dictType.value).toBe('object')
  })

  it('当字典不存在时应该调用 API 获取数据', async () => {
    const dictStoreMock = {
      getDict: vi.fn(() => null),
      setDict: vi.fn()
    }
    const getDictsMock = vi.mocked(getDicts)
    getDictsMock.mockResolvedValue({
      data: [
        { dictLabel: '选项1', dictValue: '1', listClass: 'list', cssClass: 'css1' }
      ]
    })

    vi.mocked(useDictStore).mockReturnValue(dictStoreMock)

    useDict('dictType')

    // 等待异步操作完成
    await new Promise(resolve => setTimeout(resolve, 0))

    expect(dictStoreMock.getDict).toHaveBeenCalledWith('dictType')
    expect(getDictsMock).toHaveBeenCalledWith('dictType')
    expect(dictStoreMock.setDict).toHaveBeenCalledWith('dictType', expect.any(Array))
  })

  it('当字典已存在时应该使用缓存数据', async () => {
    const cachedData = [
      { dictLabel: '缓存1', dictValue: '1', listClass: 'list', cssClass: 'css1' }
    ]
    const dictStoreMock = {
      getDict: vi.fn(() => cachedData),
      setDict: vi.fn()
    }
    const getDictsMock = vi.mocked(getDicts)

    vi.mocked(useDictStore).mockReturnValue(dictStoreMock)

    useDict('dictType')

    // 等待异步操作完成
    await new Promise(resolve => setTimeout(resolve, 0))

    expect(dictStoreMock.getDict).toHaveBeenCalledWith('dictType')
    expect(getDictsMock).not.toHaveBeenCalled()
    expect(dictStoreMock.setDict).not.toHaveBeenCalled()
  })

  it('应该正确处理多个字典类型', async () => {
    const dictStoreMock = {
      getDict: vi.fn(() => null),
      setDict: vi.fn()
    }
    const getDictsMock = vi.mocked(getDicts)
    getDictsMock
      .mockResolvedValueOnce({
        data: [{ dictLabel: '类型1', dictValue: '1', listClass: 'list', cssClass: 'css1' }]
      })
      .mockResolvedValueOnce({
        data: [{ dictLabel: '类型2', dictValue: '2', listClass: 'tag', cssClass: 'css2' }]
      })

    vi.mocked(useDictStore).mockReturnValue(dictStoreMock)

    const result = useDict('dictType1', 'dictType2')

    // 等待异步操作完成
    await new Promise(resolve => setTimeout(resolve, 0))

    expect(result.dictType1.value).toEqual([
      { label: '类型1', value: '1', elTagType: 'list', elTagClass: 'css1' }
    ])
    expect(result.dictType2.value).toEqual([
      { label: '类型2', value: '2', elTagType: 'tag', elTagClass: 'css2' }
    ])
    expect(dictStoreMock.setDict).toHaveBeenCalledTimes(2)
  })

  it('应该正确格式化字典数据', async () => {
    const dictStoreMock = {
      getDict: vi.fn(() => null),
      setDict: vi.fn()
    }
    const getDictsMock = vi.mocked(getDicts)
    getDictsMock.mockResolvedValue({
      data: [
        {
          dictLabel: '状态',
          dictValue: '1',
          listClass: 'status',
          cssClass: 'tag-success'
        }
      ]
    })

    vi.mocked(useDictStore).mockReturnValue(dictStoreMock)

    const result = useDict('statusType')

    // 等待异步操作完成
    await new Promise(resolve => setTimeout(resolve, 0))

    expect(result.statusType.value).toEqual([
      {
        label: '状态',
        value: '1',
        elTagType: 'status',
        elTagClass: 'tag-success'
      }
    ])
  })
})
