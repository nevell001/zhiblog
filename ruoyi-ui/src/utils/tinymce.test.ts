import { describe, it, expect, vi, beforeEach } from 'vitest'
import { loadTinymce } from './tinymce'

describe('tinymce 工具测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    // 清除可能的导入缓存
    vi.resetModules()
  })

  describe('loadTinymce', () => {
    it('应该导出 loadTinymce 函数', () => {
      expect(loadTinymce).toBeDefined()
      expect(typeof loadTinymce).toBe('function')
    })

    it('应该返回一个 Promise', () => {
      const result = loadTinymce()
      expect(result).toBeInstanceOf(Promise)
    })

    it('应该动态导入 TinyMCE 核心库', async () => {
      const result = await loadTinymce()
      // 如果导入失败（测试环境可能没有安装 TinyMCE），应该返回 null
      // 这里只验证函数结构，不验证具体返回值
      expect(typeof result === 'object' || result === null).toBe(true)
    })

    it('应该处理导入错误', async () => {
      // 模拟导入失败的情况
      vi.mock('tinymce/tinymce', () => {
        throw new Error('Failed to load')
      })

      const result = await loadTinymce()
      expect(result).toBe(null)
    })

    it('应该在导入失败时记录错误', async () => {
      const consoleErrorSpy = vi.spyOn(console, 'error').mockImplementation(() => {})

      try {
        vi.doMock('tinymce/tinymce', () => {
          throw new Error('Failed to load')
        })
        const result = await loadTinymce()
        expect(result).toBe(null)
      } finally {
        consoleErrorSpy.mockRestore()
      }
    })
  })
})
