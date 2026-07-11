import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'

vi.mock('tinymce/tinymce', () => ({
  default: {
    init: vi.fn()
  }
}))
vi.mock('tinymce/themes/silver', () => ({}))
vi.mock('tinymce/icons/default', () => ({}))
vi.mock('tinymce/models/dom', () => ({}))
vi.mock('tinymce/plugins/advlist', () => ({}))
vi.mock('tinymce/plugins/autolink', () => ({}))
vi.mock('tinymce/plugins/lists', () => ({}))
vi.mock('tinymce/plugins/link', () => ({}))
vi.mock('tinymce/plugins/image', () => ({}))
vi.mock('tinymce/plugins/charmap', () => ({}))
vi.mock('tinymce/plugins/preview', () => ({}))
vi.mock('tinymce/plugins/anchor', () => ({}))
vi.mock('tinymce/plugins/searchreplace', () => ({}))
vi.mock('tinymce/plugins/visualblocks', () => ({}))
vi.mock('tinymce/plugins/code', () => ({}))
vi.mock('tinymce/plugins/fullscreen', () => ({}))
vi.mock('tinymce/plugins/insertdatetime', () => ({}))
vi.mock('tinymce/plugins/media', () => ({}))
vi.mock('tinymce/plugins/table', () => ({}))
vi.mock('tinymce/plugins/help', () => ({}))
vi.mock('tinymce/plugins/wordcount', () => ({}))

const importLoadTinymce = async () => {
  const module = await import('./tinymce')
  return module.loadTinymce
}

describe('tinymce 工具测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  afterEach(() => {
    vi.restoreAllMocks()
    vi.resetModules()
  })

  describe('loadTinymce', () => {
    it('应该导出 loadTinymce 函数', async () => {
      const loadTinymce = await importLoadTinymce()
      expect(loadTinymce).toBeDefined()
      expect(typeof loadTinymce).toBe('function')
    })

    it('应该返回一个 Promise', async () => {
      const loadTinymce = await importLoadTinymce()
      const result = loadTinymce()
      expect(result).toBeInstanceOf(Promise)
    })

    it('应该动态导入 TinyMCE 核心库', async () => {
      const loadTinymce = await importLoadTinymce()
      const result = await loadTinymce()
      expect(result).toEqual({ init: expect.any(Function) })
    })

    it('应该处理导入错误', async () => {
      const consoleErrorSpy = vi.spyOn(console, 'error').mockImplementation(() => {})
      vi.doMock('tinymce/tinymce', () => {
        throw new Error('Failed to load')
      })

      const loadTinymce = await importLoadTinymce()
      const result = await loadTinymce()
      expect(result).toBe(null)
      consoleErrorSpy.mockRestore()
    })

    it('应该在导入失败时记录错误', async () => {
      const consoleErrorSpy = vi.spyOn(console, 'error').mockImplementation(() => {})

      try {
        vi.doMock('tinymce/tinymce', () => {
          throw new Error('Failed to load')
        })
        const loadTinymce = await importLoadTinymce()
        const result = await loadTinymce()
        expect(result).toBe(null)
        expect(consoleErrorSpy).toHaveBeenCalledWith('Failed to load TinyMCE:', expect.any(Error))
      } finally {
        consoleErrorSpy.mockRestore()
      }
    })
  })
})
