import { describe, it, expect, beforeEach } from 'vitest'
import {
  parseTime,
  addDateRange,
  selectDictLabel,
  selectDictLabels,
  handleTree,
  getBlob,
  blobValidate,
  tansParams
} from './ruoyi'

describe('RuoYi 工具测试', () => {
  describe('parseTime 函数', () => {
    it('应该返回 null 当没有参数时', () => {
      expect(parseTime()).toBe(null)
    })

    it('应该返回 null 当参数为空时', () => {
      expect(parseTime('')).toBe(null)
    })

    it('应该格式化 Date 对象', () => {
      const date = new Date('2025-01-26T10:30:45')
      const result = parseTime(date)
      expect(result).toBe('2025-01-26 10:30:45')
    })

    it('应该格式化时间戳（秒）', () => {
      const timestamp = 1706267445 // 2025-01-26 10:30:45
      const result = parseTime(timestamp)
      expect(result).toMatch(/^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}$/)
    })

    it('应该格式化时间戳（毫秒）', () => {
      const timestamp = 1706267445000 // 2025-01-26 10:30:45
      const result = parseTime(timestamp)
      expect(result).toMatch(/^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}$/)
    })

    it('应该格式化日期字符串', () => {
      const dateStr = '2025-01-26 10:30:45'
      const result = parseTime(dateStr)
      expect(result).toBe('2025-01-26 10:30:45')
    })

    it('应该支持自定义格式', () => {
      const date = new Date('2025-01-26T10:30:45')
      const result = parseTime(date, '{y}-{m}-{d}')
      expect(result).toBe('2025-01-26')
    })

    it('应该支持星期格式化', () => {
      const date = new Date('2025-01-26T10:30:45') // 星期日
      const result = parseTime(date, '{y}-{m}-{d} 星期{a}')
      expect(result).toBe('2025-01-26 星期日')
    })

    it('应该处理 ISO 日期字符串', () => {
      const isoDate = '2025-01-26T10:30:45.000Z'
      const result = parseTime(isoDate)
      expect(result).toMatch(/^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}$/)
    })
  })

  describe('addDateRange 函数', () => {
    it('应该添加默认日期范围', () => {
      const params = { name: 'test' }
      const dateRange = ['2025-01-01', '2025-01-31']
      const result = addDateRange(params, dateRange)

      expect(result.params).toEqual({
        beginTime: '2025-01-01',
        endTime: '2025-01-31'
      })
    })

    it('应该添加自定义属性名的日期范围', () => {
      const params = { name: 'test' }
      const dateRange = ['2025-01-01', '2025-01-31']
      const result = addDateRange(params, dateRange, 'CreateTime')

      expect(result.params).toEqual({
        beginCreateTime: '2025-01-01',
        endCreateTime: '2025-01-31'
      })
    })

    it('应该处理空日期范围', () => {
      const params = { name: 'test' }
      const result = addDateRange(params, [])

      expect(result.params).toEqual({
        beginTime: undefined,
        endTime: undefined
      })
    })

    it('应该处理非数组日期范围', () => {
      const params = { name: 'test' }
      const result = addDateRange(params, '2025-01-01' as any)

      expect(result.params).toEqual({
        beginTime: undefined,
        endTime: undefined
      })
    })

    it('应该保留原有参数', () => {
      const params = { name: 'test', status: 1 }
      const dateRange = ['2025-01-01', '2025-01-31']
      const result = addDateRange(params, dateRange)

      expect(result.name).toBe('test')
      expect(result.params).toEqual({
        beginTime: '2025-01-01',
        endTime: '2025-01-31'
      })
    })

    it('应该处理已有 params 对象', () => {
      const params = { name: 'test', params: { type: 1 } }
      const dateRange = ['2025-01-01', '2025-01-31']
      const result = addDateRange(params, dateRange)

      expect(result.params).toEqual({
        type: 1,
        beginTime: '2025-01-01',
        endTime: '2025-01-31'
      })
    })
  })

  describe('selectDictLabel 函数', () => {
    const dictData = [
      { value: '0', label: '正常' },
      { value: '1', label: '停用' },
      { value: '2', label: '删除' }
    ]

    it('应该返回对应的标签', () => {
      const result = selectDictLabel(dictData, '0')
      expect(result).toBe('正常')
    })

    it('应该返回原始值当值不存在时', () => {
      const result = selectDictLabel(dictData, '3')
      expect(result).toBe('3')
    })

    it('应该返回空字符串当值为 undefined', () => {
      const result = selectDictLabel(dictData, undefined)
      expect(result).toBe('')
    })

    it('应该处理数字类型的值', () => {
      const result = selectDictLabel(dictData, 1)
      expect(result).toBe('停用')
    })

    it('应该处理空数组', () => {
      const result = selectDictLabel([], '0')
      expect(result).toBe('0')
    })
  })

  describe('selectDictLabels 函数', () => {
    const dictData = [
      { value: '0', label: '正常' },
      { value: '1', label: '停用' },
      { value: '2', label: '删除' }
    ]

    it('应该返回多个标签', () => {
      const result = selectDictLabels(dictData, '0,1')
      expect(result).toBe('正常,停用')
    })

    it('应该处理空字符串', () => {
      const result = selectDictLabels(dictData, '')
      expect(result).toBe('')
    })

    it('应该处理不存在的值', () => {
      const result = selectDictLabels(dictData, '0,3')
      expect(result).toBe('正常,3')
    })
  })

  describe('handleTree 函数', () => {
    const data = [
      { id: 1, parentId: 0, name: '根节点' },
      { id: 2, parentId: 1, name: '子节点1' },
      { id: 3, parentId: 1, name: '子节点2' },
      { id: 4, parentId: 2, name: '孙节点1' }
    ]

    it('应该构建树形结构', () => {
      const result = handleTree(data, 'id', 'parentId', 'children')

      expect(result).toHaveLength(1)
      expect(result[0].id).toBe(1)
      expect(result[0].children).toHaveLength(2)
      expect(result[0].children![0].children).toHaveLength(1)
    })

    it('应该处理空数组', () => {
      const result = handleTree([], 'id', 'parentId', 'children')
      expect(result).toEqual([])
    })

    it('应该支持自定义字段名', () => {
      const customData = [
        { key: 1, parentKey: 0, name: '根节点' },
        { key: 2, parentKey: 1, name: '子节点' }
      ]
      const result = handleTree(customData, 'key', 'parentKey', 'children')

      expect(result).toHaveLength(1)
      expect(result[0].key).toBe(1)
      expect(result[0].children).toHaveLength(1)
    })
  })

  describe('blobValidate 函数', () => {
    it('应该验证非 JSON Blob', () => {
      const blob = new Blob(['%PDF'], { type: 'application/pdf' })
      const result = blobValidate(blob)
      expect(result).toBe(true)
    })

    it('应该拒绝 JSON Blob', () => {
      const blob = new Blob(['{"key":"value"}'], { type: 'application/json' })
      const result = blobValidate(blob)
      expect(result).toBe(false)
    })

    it('应该处理文本 Blob', () => {
      const blob = new Blob(['text content'], { type: 'text/plain' })
      const result = blobValidate(blob)
      expect(result).toBe(true)
    })

    it('应该处理空 Blob', () => {
      const blob = new Blob([], { type: 'application/pdf' })
      const result = blobValidate(blob)
      expect(result).toBe(true)
    })
  })

  describe('tansParams 函数', () => {
    it('应该转换对象为 URL 参数', () => {
      const params = { name: 'test', age: 25 }
      const result = tansParams(params)
      expect(result).toBe('name=test&age=25&')
    })

    it('应该处理空对象', () => {
      const result = tansParams({})
      expect(result).toBe('')
    })

    it('应该处理特殊字符', () => {
      const params = { name: 'test name', email: 'test@example.com' }
      const result = tansParams(params)
      expect(result).toContain('name=')
      expect(result).toContain('email=')
      expect(result).toContain('&')
    })

    it('应该处理 null 和 undefined 值', () => {
      const params = { name: 'test', age: null, email: undefined }
      const result = tansParams(params)
      expect(result).toContain('name=test')
      expect(result).not.toContain('age=')
      expect(result).not.toContain('email=')
    })

    it('应该处理空字符串值', () => {
      const params = { name: 'test', age: '' }
      const result = tansParams(params)
      expect(result).toContain('name=test')
      expect(result).not.toContain('age=')
    })
  })

  describe('完整场景测试', () => {
    it('应该支持完整的日期处理流程', () => {
      // 格式化日期
      const date = new Date('2025-01-26T10:30:45')
      const formatted = parseTime(date)
      expect(formatted).toBe('2025-01-26 10:30:45')

      // 添加日期范围
      const params = { name: 'test' }
      const dateRange = ['2025-01-01', '2025-01-31']
      const result = addDateRange(params, dateRange)
      expect(result.params.beginTime).toBe('2025-01-01')
      expect(result.params.endTime).toBe('2025-01-31')
    })

    it('应该支持完整的字典处理流程', () => {
      const dictData = [
        { value: '0', label: '正常' },
        { value: '1', label: '停用' }
      ]

      // 获取单个标签
      const label = selectDictLabel(dictData, '0')
      expect(label).toBe('正常')

      // 获取多个标签
      const labels = selectDictLabels(dictData, '0,1')
      expect(labels).toBe('正常,停用')
    })

    it('应该支持完整的树形结构处理流程', () => {
      const data = [
        { id: 1, parentId: 0, name: '根节点' },
        { id: 2, parentId: 1, name: '子节点' }
      ]

      const tree = handleTree(data, 'id', 'parentId', 'children')
      expect(tree).toHaveLength(1)
      expect(tree[0].children).toHaveLength(1)
    })
  })
})