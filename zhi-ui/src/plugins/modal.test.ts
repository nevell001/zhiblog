import { describe, it, expect, beforeEach, afterEach, vi } from 'vitest'

// Mock Element Plus components
const mockElMessage = {
  info: vi.fn(),
  error: vi.fn(),
  success: vi.fn(),
  warning: vi.fn()
}

const mockElMessageBox = {
  alert: vi.fn(() => Promise.resolve()),
  confirm: vi.fn(() => Promise.resolve()),
  prompt: vi.fn(() => Promise.resolve())
}

const mockElNotification = {
  info: vi.fn(),
  error: vi.fn(),
  success: vi.fn(),
  warning: vi.fn()
}

const mockElLoading = {
  service: vi.fn(() => ({ close: vi.fn() }))
}

vi.mock('@/plugins/element-plus-service', () => ({
  ElMessage: mockElMessage,
  ElMessageBox: mockElMessageBox,
  ElNotification: mockElNotification,
  ElLoading: mockElLoading
}))

describe('modal 插件测试', () => {
  let modal: any

  beforeEach(async () => {
    // 重新导入模块以获取最新的 mock
    const modalModule = await import('./modal')
    modal = modalModule.default
  })

  afterEach(() => {
    vi.clearAllMocks()
  })

  describe('msg 方法', () => {
    it('应该调用 ElMessage.info', () => {
      modal.msg('测试消息')
      expect(mockElMessage.info).toHaveBeenCalledWith('测试消息')
    })

    it('应该传递正确的消息内容', () => {
      const message = '这是一条信息'
      modal.msg(message)
      expect(mockElMessage.info).toHaveBeenCalledTimes(1)
      expect(mockElMessage.info).toHaveBeenCalledWith(message)
    })
  })

  describe('msgError 方法', () => {
    it('应该调用 ElMessage.error', () => {
      modal.msgError('错误消息')
      expect(mockElMessage.error).toHaveBeenCalledWith('错误消息')
    })
  })

  describe('msgSuccess 方法', () => {
    it('应该调用 ElMessage.success', () => {
      modal.msgSuccess('成功消息')
      expect(mockElMessage.success).toHaveBeenCalledWith('成功消息')
    })
  })

  describe('msgWarning 方法', () => {
    it('应该调用 ElMessage.warning', () => {
      modal.msgWarning('警告消息')
      expect(mockElMessage.warning).toHaveBeenCalledWith('警告消息')
    })
  })

  describe('alert 方法', () => {
    it('应该调用 ElMessageBox.alert', () => {
      modal.alert('弹出提示')
      expect(mockElMessageBox.alert).toHaveBeenCalledWith('弹出提示', '系统提示')
    })
  })

  describe('alertError 方法', () => {
    it('应该调用 ElMessageBox.alert 并设置 error 类型', () => {
      modal.alertError('错误提示')
      expect(mockElMessageBox.alert).toHaveBeenCalledWith('错误提示', '系统提示', {
        type: 'error'
      })
    })
  })

  describe('alertSuccess 方法', () => {
    it('应该调用 ElMessageBox.alert 并设置 success 类型', () => {
      modal.alertSuccess('成功提示')
      expect(mockElMessageBox.alert).toHaveBeenCalledWith('成功提示', '系统提示', {
        type: 'success'
      })
    })
  })

  describe('alertWarning 方法', () => {
    it('应该调用 ElMessageBox.alert 并设置 warning 类型', () => {
      modal.alertWarning('警告提示')
      expect(mockElMessageBox.alert).toHaveBeenCalledWith('警告提示', '系统提示', {
        type: 'warning'
      })
    })
  })

  describe('notify 方法', () => {
    it('应该调用 ElNotification.info', () => {
      modal.notify('通知内容')
      expect(mockElNotification.info).toHaveBeenCalledWith('通知内容')
    })
  })

  describe('notifyError 方法', () => {
    it('应该调用 ElNotification.error', () => {
      modal.notifyError('错误通知')
      expect(mockElNotification.error).toHaveBeenCalledWith('错误通知')
    })
  })

  describe('notifySuccess 方法', () => {
    it('应该调用 ElNotification.success', () => {
      modal.notifySuccess('成功通知')
      expect(mockElNotification.success).toHaveBeenCalledWith('成功通知')
    })
  })

  describe('notifyWarning 方法', () => {
    it('应该调用 ElNotification.warning', () => {
      modal.notifyWarning('警告通知')
      expect(mockElNotification.warning).toHaveBeenCalledWith('警告通知')
    })
  })

  describe('confirm 方法', () => {
    it('应该调用 ElMessageBox.confirm', () => {
      modal.confirm('确认内容')
      expect(mockElMessageBox.confirm).toHaveBeenCalledWith('确认内容', '系统提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
    })

    it('应该返回 Promise', () => {
      expect(modal.confirm('确认内容')).toBeInstanceOf(Promise)
    })
  })

  describe('prompt 方法', () => {
    it('应该调用 ElMessageBox.prompt', () => {
      modal.prompt('请输入')
      expect(mockElMessageBox.prompt).toHaveBeenCalledWith('请输入', '系统提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
    })

    it('应该返回 Promise', () => {
      expect(modal.prompt('请输入')).toBeInstanceOf(Promise)
    })
  })

  describe('loading 方法', () => {
    it('应该调用 ElLoading.service', () => {
      modal.loading('加载中...')
      expect(mockElLoading.service).toHaveBeenCalledWith({
        lock: true,
        text: '加载中...',
        background: 'rgba(0, 0, 0, 0.7)'
      })
    })
  })

  describe('closeLoading 方法', () => {
    it('应该关闭 loading 实例', () => {
      const mockClose = vi.fn()
      mockElLoading.service.mockReturnValue({ close: mockClose })

      modal.loading('加载中')
      modal.closeLoading()

      expect(mockClose).toHaveBeenCalled()
    })

    it('应该在 loading 实例不存在时不报错', () => {
      // 不调用 loading，直接调用 closeLoading
      expect(() => modal.closeLoading()).not.toThrow()
    })
  })
})
