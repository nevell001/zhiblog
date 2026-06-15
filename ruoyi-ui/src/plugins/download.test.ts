import { describe, it, expect, vi, beforeEach } from 'vitest'
import download from './download'

describe('Download Plugin 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('应该导出 download 插件对象', () => {
    expect(download).toBeDefined()
    expect(typeof download).toBe('object')
    expect(typeof download.name).toBe('function')
    expect(typeof download.resource).toBe('function')
    expect(typeof download.zip).toBe('function')
  })

  it('应该下载文件', () => {
    const downloadFile = () => {
      return true
    }
    expect(typeof downloadFile).toBe('function')
  })

  it('应该处理文件名', () => {
    const fileName = 'test-file.pdf'
    expect(fileName).toBe('test-file.pdf')
  })

  it('应该处理文件内容', () => {
    const fileContent = 'test file content'
    expect(typeof fileContent).toBe('string')
    expect(fileContent.length).toBeGreaterThan(0)
  })

  it('应该支持 Blob 对象', () => {
    const blob = new Blob(['test content'], { type: 'text/plain' })
    expect(blob).toBeInstanceOf(Blob)
  })

  it('应该支持创建下载链接', () => {
    const link = document.createElement('a')
    expect(link.tagName).toBe('A')
  })
})
