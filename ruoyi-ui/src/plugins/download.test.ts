import { describe, it, expect, beforeEach, afterEach, vi } from 'vitest'
import axios from 'axios'
import { ElMessage, ElLoading } from 'element-plus'
import { saveAs } from 'file-saver'
import { getToken } from '@/utils/auth'
import errorCode from '@/utils/errorCode'
import { blobValidate } from '@/utils/ruoyi'
import download from './download'

// Mock dependencies
vi.mock('axios')
vi.mock('element-plus')
vi.mock('file-saver')
vi.mock('@/utils/auth', () => ({
  getToken: vi.fn(() => 'test-token')
}))
vi.mock('@/utils/errorCode', () => ({
  default: { '401': '未授权', 'default': '系统错误' }
}))
vi.mock('@/utils/ruoyi', () => ({
  blobValidate: vi.fn((data: any) => data instanceof Blob)
}))

describe('download 插件测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    vi.stubGlobal('import', {
      meta: {
        env: {
          VITE_APP_BASE_API: '/dev-api'
        }
      }
    })
  })

  describe('name 方法', () => {
    it('应该调用 axios 发起下载请求', () => {
      ;(axios as any).mockResolvedValue({
        data: new Blob(['test']),
        headers: { 'download-filename': encodeURIComponent('test.txt') }
      })

      download.name('test.txt', true)

      expect(axios).toHaveBeenCalledWith({
        method: 'get',
        url: '/dev-api/common/download?fileName=test.txt&delete=true',
        responseType: 'blob',
        headers: { Authorization: 'Bearer test-token' }
      })
    })

    it('应该在成功时调用 saveAs', async () => {
      const mockBlob = new Blob(['test content'])
      ;(axios as any).mockResolvedValue({
        data: mockBlob,
        headers: { 'download-filename': encodeURIComponent('test.txt') }
      })

      download.name('test.txt')

      // 等待 Promise 解析
      await new Promise(resolve => setTimeout(resolve, 10))

      expect(saveAs).toHaveBeenCalledWith(mockBlob, 'test.txt')
    })

    it('应该在默认情况下不删除文件', () => {
      ;(axios as any).mockResolvedValue({
        data: new Blob(['test']),
        headers: { 'download-filename': 'test.txt' }
      })

      download.name('test.txt')

      expect(axios).toHaveBeenCalledWith(
        expect.objectContaining({
          url: expect.stringContaining('&delete=true')
        })
      )
    })
  })

  describe('resource 方法', () => {
    it('应该调用 axios 发起资源下载请求', () => {
      ;(axios as any).mockResolvedValue({
        data: new Blob(['test']),
        headers: { 'download-filename': 'resource.txt' }
      })

      download.resource('/common/test.txt')

      expect(axios).toHaveBeenCalledWith({
        method: 'get',
        url: '/dev-api/common/download/resource?resource=%2Fcommon%2Ftest.txt',
        responseType: 'blob',
        headers: { Authorization: 'Bearer test-token' }
      })
    })

    it('应该在成功时调用 saveAs', async () => {
      const mockBlob = new Blob(['resource content'])
      ;(axios as any).mockResolvedValue({
        data: mockBlob,
        headers: { 'download-filename': 'resource.txt' }
      })

      download.resource('/common/test.txt')

      await new Promise(resolve => setTimeout(resolve, 10))

      expect(saveAs).toHaveBeenCalledWith(mockBlob, 'resource.txt')
    })
  })

  describe('zip 方法', () => {
    it('应该显示加载状态', () => {
      ;(axios as any).mockResolvedValue({
        data: new Blob(['zip content']),
        headers: {}
      })

      download.zip('/common/export', 'test.zip')

      expect(ElLoading.service).toHaveBeenCalledWith({
        text: '正在下载数据，请稍候',
        background: 'rgba(0, 0, 0, 0.7)'
      })
    })

    it('应该在成功时关闭加载状态并保存文件', async () => {
      const mockBlob = new Blob(['zip content'], { type: 'application/zip' })
      const mockClose = vi.fn()
      ;(ElLoading.service as any).mockReturnValue({ close: mockClose })
      ;(axios as any).mockResolvedValue({
        data: mockBlob,
        headers: {}
      })

      download.zip('/common/export', 'test.zip')

      await new Promise(resolve => setTimeout(resolve, 10))

      expect(mockClose).toHaveBeenCalled()
      expect(saveAs).toHaveBeenCalledWith(mockBlob, 'test.zip')
    })

    it('应该在失败时关闭加载状态并显示错误消息', async () => {
      const mockClose = vi.fn()
      ;(ElLoading.service as any).mockReturnValue({ close: mockClose })
      ;(axios as any).mockRejectedValue(new Error('Network error'))

      download.zip('/common/export', 'test.zip')

      await new Promise(resolve => setTimeout(resolve, 10))

      expect(mockClose).toHaveBeenCalled()
      expect(ElMessage.error).toHaveBeenCalledWith('下载文件出现错误，请联系管理员！')
    })
  })

  describe('saveAs 方法', () => {
    it('应该调用 file-saver 的 saveAs 函数', () => {
      const mockBlob = new Blob(['test'])
      download.saveAs(mockBlob, 'test.txt')

      expect(saveAs).toHaveBeenCalledWith(mockBlob, 'test.txt')
    })

    it('应该支持传递选项参数', () => {
      const mockBlob = new Blob(['test'])
      const opts = { autoBom: true }
      download.saveAs(mockBlob, 'test.txt', opts)

      expect(saveAs).toHaveBeenCalledWith(mockBlob, 'test.txt', opts)
    })
  })

  describe('printErrMsg 方法', () => {
    it('应该解析错误 blob 并显示错误消息', async () => {
      const errorBlob = new Blob(['{"code": 401, "msg": "未授权"}'])
      ;(ElMessage.error as any).mockClear()

      await download.printErrMsg(errorBlob)

      expect(ElMessage.error).toHaveBeenCalledWith('未授权')
    })

    it('应该使用默认错误代码当没有匹配的代码时', async () => {
      const errorBlob = new Blob(['{"code": 999, "msg": "未知错误"}'])
      ;(ElMessage.error as any).mockClear()

      await download.printErrMsg(errorBlob)

      expect(ElMessage.error).toHaveBeenCalledWith('未知错误')
    })

    it('应该使用默认错误消息当没有提供消息时', async () => {
      const errorBlob = new Blob(['{"code": 999}'])
      ;(ElMessage.error as any).mockClear()

      await download.printErrMsg(errorBlob)

      expect(ElMessage.error).toHaveBeenCalledWith('系统错误')
    })
  })
})
