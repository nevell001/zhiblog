import { describe, it, expect, vi, beforeEach } from 'vitest'
import {
  listTable,
  listDbTable,
  getGenTable,
  updateGenTable,
  importTable,
  createTable,
  previewTable,
  delTable,
  genCode,
  synchDb
} from './gen'
import request from '@/utils/request'

vi.mock('@/utils/request')
const mockRequest = vi.mocked(request)

describe('Tool Gen API 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('listTable 应调用 GET /tool/gen/list', () => {
    listTable({ pageNum: 1, pageSize: 10 })
    expect(mockRequest).toHaveBeenCalledWith(
      expect.objectContaining({ url: '/tool/gen/list', method: 'get' })
    )
  })

  it('listDbTable 应调用 GET /tool/gen/db/list', () => {
    listDbTable()
    expect(mockRequest).toHaveBeenCalledWith(
      expect.objectContaining({ url: '/tool/gen/db/list', method: 'get' })
    )
  })

  it('getGenTable 应调用 GET /tool/gen/:id', () => {
    getGenTable(1)
    expect(mockRequest).toHaveBeenCalledWith(
      expect.objectContaining({ url: '/tool/gen/1', method: 'get' })
    )
  })

  it('updateGenTable 应调用 PUT /tool/gen', () => {
    updateGenTable({ tableId: 1 })
    expect(mockRequest).toHaveBeenCalledWith(
      expect.objectContaining({ url: '/tool/gen', method: 'put' })
    )
  })

  it('importTable 应调用 POST /tool/gen/importTable', () => {
    importTable({ tables: ['a'] })
    expect(mockRequest).toHaveBeenCalledWith(
      expect.objectContaining({ url: '/tool/gen/importTable', method: 'post' })
    )
  })

  it('createTable 应调用 POST /tool/gen/createTable', () => {
    createTable({ tableName: 't' })
    expect(mockRequest).toHaveBeenCalledWith(
      expect.objectContaining({ url: '/tool/gen/createTable', method: 'post' })
    )
  })

  it('previewTable 应调用 GET /tool/gen/preview/:id', () => {
    previewTable(1)
    expect(mockRequest).toHaveBeenCalledWith(
      expect.objectContaining({ url: '/tool/gen/preview/1', method: 'get' })
    )
  })

  it('delTable 应调用 DELETE /tool/gen/:id', () => {
    delTable(1)
    expect(mockRequest).toHaveBeenCalledWith(
      expect.objectContaining({ url: '/tool/gen/1', method: 'delete' })
    )
  })

  it('genCode 应调用 GET /tool/gen/genCode/:name', () => {
    genCode('user')
    expect(mockRequest).toHaveBeenCalledWith(
      expect.objectContaining({ url: '/tool/gen/genCode/user', method: 'get' })
    )
  })

  it('synchDb 应调用 GET /tool/gen/synchDb/:name', () => {
    synchDb('user')
    expect(mockRequest).toHaveBeenCalledWith(
      expect.objectContaining({ url: '/tool/gen/synchDb/user', method: 'get' })
    )
  })
})
