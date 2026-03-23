import { describe, it, expect, beforeEach, vi } from 'vitest'
import request from '@/utils/request'
import * as jobApi from './job'

// Mock request
vi.mock('@/utils/request', () => ({
  default: vi.fn(() => Promise.resolve({ data: {} }))
}))

describe('Monitor Job API 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('listJob', () => {
    it('应该导出 listJob 函数', () => {
      expect(jobApi.listJob).toBeDefined()
      expect(typeof jobApi.listJob).toBe('function')
    })

    it('应该调用定时任务列表接口', () => {
      jobApi.listJob({ pageNum: 1, pageSize: 10 })
      expect(request).toHaveBeenCalledWith({
        url: '/monitor/job/list',
        method: 'get',
        params: { pageNum: 1, pageSize: 10 }
      })
    })
  })

  describe('getJob', () => {
    it('应该导出 getJob 函数', () => {
      expect(jobApi.getJob).toBeDefined()
      expect(typeof jobApi.getJob).toBe('function')
    })

    it('应该调用定时任务详情接口', () => {
      jobApi.getJob(123)
      expect(request).toHaveBeenCalledWith({
        url: '/monitor/job/123',
        method: 'get'
      })
    })
  })

  describe('addJob', () => {
    it('应该导出 addJob 函数', () => {
      expect(jobApi.addJob).toBeDefined()
      expect(typeof jobApi.addJob).toBe('function')
    })

    it('应该调用新增定时任务接口', () => {
      const jobData = { jobName: 'test', jobGroup: 'default' }
      jobApi.addJob(jobData)
      expect(request).toHaveBeenCalledWith({
        url: '/monitor/job',
        method: 'post',
        data: jobData
      })
    })
  })

  describe('updateJob', () => {
    it('应该导出 updateJob 函数', () => {
      expect(jobApi.updateJob).toBeDefined()
      expect(typeof jobApi.updateJob).toBe('function')
    })

    it('应该调用修改定时任务接口', () => {
      const jobData = { jobId: 123, jobName: 'updated' }
      jobApi.updateJob(jobData)
      expect(request).toHaveBeenCalledWith({
        url: '/monitor/job',
        method: 'put',
        data: jobData
      })
    })
  })

  describe('delJob', () => {
    it('应该导出 delJob 函数', () => {
      expect(jobApi.delJob).toBeDefined()
      expect(typeof jobApi.delJob).toBe('function')
    })

    it('应该调用删除定时任务接口', () => {
      jobApi.delJob(123)
      expect(request).toHaveBeenCalledWith({
        url: '/monitor/job/123',
        method: 'delete'
      })
    })
  })

  describe('changeJobStatus', () => {
    it('应该导出 changeJobStatus 函数', () => {
      expect(jobApi.changeJobStatus).toBeDefined()
      expect(typeof jobApi.changeJobStatus).toBe('function')
    })

    it('应该调用任务状态修改接口', () => {
      jobApi.changeJobStatus(123, '1')
      expect(request).toHaveBeenCalledWith({
        url: '/monitor/job/changeStatus',
        method: 'put',
        data: { jobId: 123, status: '1' }
      })
    })
  })

  describe('runJob', () => {
    it('应该导出 runJob 函数', () => {
      expect(jobApi.runJob).toBeDefined()
      expect(typeof jobApi.runJob).toBe('function')
    })

    it('应该调用定时任务立即执行接口', () => {
      jobApi.runJob(123, 'default')
      expect(request).toHaveBeenCalledWith({
        url: '/monitor/job/run',
        method: 'put',
        data: { jobId: 123, jobGroup: 'default' }
      })
    })
  })
})
