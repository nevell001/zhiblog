import { describe, it, expect, vi, beforeEach } from 'vitest'
import SizeSelect from '../index.vue'
import { useAppStore } from '@/stores/app'
import { ref } from 'vue'

// Mock store
vi.mock('@/stores/app')
const appStoreMock = {
  size: 'large',
  setSize: vi.fn(),
  $reset: vi.fn()
}

describe('SizeSelect 组件测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    // 重置 store mock
    appStoreMock.size = 'large'
    appStoreMock.$reset()
  })

  it('应该导出 SizeSelect 组件', () => {
    expect(SizeSelect).toBeDefined()
    expect(typeof SizeSelect).toBe('object')
  })

  it('应该初始化为较大尺寸', () => {
    const { size, setSize, $reset } = appStoreMock

    SizeSelect.setup({ size, setSize, $reset })

    expect(size.value).toBe('large')
  })

  it('应该有正确的选项列表', () => {
    const sizeOptions = [
      { label: '较大', value: 'large' },
      { label: '默认', value: 'default' },
      { label: '稍小', value: 'small' }
    ]

    expect(SizeSelect.options).toEqual(sizeOptions)
  })

  it('应该触发 emit when 点击选项时', async () => {
    const { size, setSize } = appStoreMock

    SizeSelect.setup({ size, setSize, $reset })

    // 点击"较小"选项
    await SizeSelect.handleSetSize('small')

    expect(setSize).toHaveBeenCalledWith('small')
    expect(size.value).toBe('small')
  })

  it('应该触发 emit when 点击选项时', async () => {
    const { size, setSize } = appStoreMock

    SizeSelect.setup({ size, setSize, $reset })

    // 点击"较大"选项
    await SizeSelect.handleSetSize('large')

    expect(setSize).toHaveBeenCalledWith('large')
    expect(size.value).toBe('large')
  })

  it('应该重新加载页面', () => {
    const reload = vi.fn()
    appStoreMock.$reset.mockReturnValue()

    SizeSelect.setup({ size, setSize, $reset })

    await SizeSelect.handleSetSize('default')

    expect(reload).toHaveBeenCalled()
  })

  it('应该处理无效的大小设置', async () => {
    const { size, setSize, $reset } = appStoreMock

    SizeSelect.setup({ size, setSize, $reset })

    await SizeSelect.handleSetSize('invalid-size')

    expect(setSize).not.toHaveBeenCalled()
    expect(window.location.reload).not.toHaveBeenCalled()
  })

  it('应该显示当前选中的大小', () => {
    const { size, setSize, $reset } = appStoreMock

    SizeSelect.setup({ size, setSize, $reset })

    await SizeSelect.handleSetSize('large')

    const selectedSize = computed(() => {
      if (size.value === 'large') return '较大'
      if (size.value === 'default') return '默认'
      if (size.value === 'small') return '稍小'
      return '未知'
    })

    expect(selectedSize.value).toBe('较大')
  })
})
