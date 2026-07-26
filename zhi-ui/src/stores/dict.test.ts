import { describe, it, expect, beforeEach } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useDictStore } from './dict'

describe('Dict Store 测试', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
  })

  describe('初始状态', () => {
    it('应该初始化为空对象', () => {
      const store = useDictStore()
      expect(store.dict).toEqual({})
    })
  })

  describe('getDict getter', () => {
    it('应该返回指定类型的字典数据', () => {
      const store = useDictStore()
      store.setDict({
        status: [
          { label: '启用', value: '1' },
          { label: '禁用', value: '0' }
        ]
      })
      const statusDict = store.getDict('status')
      expect(statusDict).toHaveLength(2)
      expect(statusDict[0].label).toBe('启用')
    })

    it('当字典类型不存在时应该返回 undefined', () => {
      const store = useDictStore()
      const notExistDict = store.getDict('not_exist')
      expect(notExistDict).toBeUndefined()
    })
  })

  describe('setDict action', () => {
    it('应该设置字典数据', () => {
      const store = useDictStore()
      store.setDict({
        status: [{ label: '启用', value: '1' }]
      })
      expect(store.dict.status).toHaveLength(1)
    })

    it('应该支持设置多个字典类型', () => {
      const store = useDictStore()
      store.setDict({
        status: [{ label: '启用', value: '1' }],
        gender: [
          { label: '男', value: '1' },
          { label: '女', value: '2' }
        ]
      })
      expect(Object.keys(store.dict)).toHaveLength(2)
      expect(store.dict.status).toHaveLength(1)
      expect(store.dict.gender).toHaveLength(2)
    })

    it('应该合并已存在的字典', () => {
      const store = useDictStore()
      store.setDict({
        status: [{ label: '启用', value: '1' }]
      })
      store.setDict({
        gender: [{ label: '男', value: '1' }]
      })
      expect(store.dict.status).toHaveLength(1)
      expect(store.dict.gender).toHaveLength(1)
    })

    it('应该覆盖已存在的字典类型', () => {
      const store = useDictStore()
      store.setDict({
        status: [{ label: '启用', value: '1' }]
      })
      store.setDict({
        status: [{ label: '正常', value: '1' }]
      })
      expect(store.dict.status).toHaveLength(1)
      expect(store.dict.status[0].label).toBe('正常')
    })
  })

  describe('removeDict action', () => {
    it('应该删除指定类型的字典', () => {
      const store = useDictStore()
      store.setDict({
        status: [{ label: '启用', value: '1' }]
      })
      store.removeDict('status')
      expect(store.dict.status).toBeUndefined()
    })

    it('删除不存在的字典应该不报错', () => {
      const store = useDictStore()
      expect(() => store.removeDict('not_exist')).not.toThrow()
    })
  })

  describe('clearDict action', () => {
    it('应该清空所有字典', () => {
      const store = useDictStore()
      store.setDict({
        status: [{ label: '启用', value: '1' }],
        gender: [{ label: '男', value: '1' }]
      })
      store.clearDict()
      expect(store.dict).toEqual({})
    })

    it('清空空字典应该不报错', () => {
      const store = useDictStore()
      expect(() => store.clearDict()).not.toThrow()
    })
  })

  describe('使用场景', () => {
    it('应该支持完整的字典管理流程', () => {
      const store = useDictStore()

      // 设置字典
      store.setDict({
        status: [
          { label: '启用', value: '1' },
          { label: '禁用', value: '0' }
        ]
      })

      // 获取字典
      const statusDict = store.getDict('status')
      expect(statusDict).toHaveLength(2)

      // 更新字典
      store.setDict({
        status: [
          { label: '正常', value: '1' },
          { label: '禁用', value: '0' }
        ]
      })
      expect(store.getDict('status')[0].label).toBe('正常')

      // 删除字典
      store.removeDict('status')
      expect(store.getDict('status')).toBeUndefined()
    })

    it('应该支持同时管理多个字典', () => {
      const store = useDictStore()

      store.setDict({
        status: [{ label: '启用', value: '1' }],
        gender: [{ label: '男', value: '1' }],
        type: [{ label: '类型1', value: '1' }]
      })

      expect(store.getDict('status')).toHaveLength(1)
      expect(store.getDict('gender')).toHaveLength(1)
      expect(store.getDict('type')).toHaveLength(1)

      // 只删除其中一个字典
      store.removeDict('gender')

      expect(store.getDict('status')).toHaveLength(1)
      expect(store.getDict('gender')).toBeUndefined()
      expect(store.getDict('type')).toHaveLength(1)
    })
  })
})
