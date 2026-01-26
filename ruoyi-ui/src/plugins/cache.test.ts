import { describe, it, expect, beforeEach, afterEach, vi } from 'vitest'
import cache from './cache'

describe('Cache 插件测试', () => {
  beforeEach(() => {
    // 清空所有存储
    sessionStorage.clear()
    localStorage.clear()
  })

  afterEach(() => {
    // 清空所有存储
    sessionStorage.clear()
    localStorage.clear()
  })

  describe('session 缓存', () => {
    describe('set 方法', () => {
      it('应该设置 sessionStorage 值', () => {
        cache.session.set('test-key', 'test-value')
        expect(sessionStorage.getItem('test-key')).toBe('test-value')
      })

      it('应该处理 null key', () => {
        cache.session.set(null as any, 'test-value')
        expect(sessionStorage.getItem('null')).toBeNull()
      })

      it('应该处理 null value', () => {
        cache.session.set('test-key', null as any)
        expect(sessionStorage.getItem('test-key')).toBeNull()
      })

      it('应该处理空字符串 key', () => {
        cache.session.set('', 'test-value')
        expect(sessionStorage.getItem('')).toBe('test-value')
      })
    })

    describe('get 方法', () => {
      it('应该获取 sessionStorage 值', () => {
        sessionStorage.setItem('test-key', 'test-value')
        const result = cache.session.get('test-key')
        expect(result).toBe('test-value')
      })

      it('应该返回 null 当 key 不存在时', () => {
        const result = cache.session.get('non-existent-key')
        expect(result).toBeNull()
      })

      it('应该返回 null 当 key 为 null 时', () => {
        const result = cache.session.get(null as any)
        expect(result).toBeNull()
      })
    })

    describe('setJSON 方法', () => {
      it('应该设置 JSON 对象', () => {
        const data = { name: 'test', value: 123 }
        cache.session.setJSON('test-json', data)
        const result = sessionStorage.getItem('test-json')
        expect(result).toBe(JSON.stringify(data))
      })

      it('应该设置 JSON 数组', () => {
        const data = [1, 2, 3, 4, 5]
        cache.session.setJSON('test-array', data)
        const result = sessionStorage.getItem('test-array')
        expect(result).toBe(JSON.stringify(data))
      })

      it('应该处理 null 值', () => {
        cache.session.setJSON('test-null', null as any)
        expect(sessionStorage.getItem('test-null')).toBeNull()
      })
    })

    describe('getJSON 方法', () => {
      it('应该获取并解析 JSON 对象', () => {
        const data = { name: 'test', value: 123 }
        sessionStorage.setItem('test-json', JSON.stringify(data))
        const result = cache.session.getJSON('test-json')
        expect(result).toEqual(data)
      })

      it('应该获取并解析 JSON 数组', () => {
        const data = [1, 2, 3, 4, 5]
        sessionStorage.setItem('test-array', JSON.stringify(data))
        const result = cache.session.getJSON('test-array')
        expect(result).toEqual(data)
      })

      it('应该返回 null 当 key 不存在时', () => {
        const result = cache.session.getJSON('non-existent-key')
        expect(result).toBeNull()
      })
    })

    describe('remove 方法', () => {
      it('应该删除 sessionStorage 值', () => {
        sessionStorage.setItem('test-key', 'test-value')
        cache.session.remove('test-key')
        expect(sessionStorage.getItem('test-key')).toBeNull()
      })

      it('应该处理删除不存在的 key', () => {
        cache.session.remove('non-existent-key')
        expect(sessionStorage.getItem('non-existent-key')).toBeNull()
      })
    })
  })

  describe('local 缓存', () => {
    describe('set 方法', () => {
      it('应该设置 localStorage 值', () => {
        cache.local.set('test-key', 'test-value')
        expect(localStorage.getItem('test-key')).toBe('test-value')
      })

      it('应该处理 null key', () => {
        cache.local.set(null as any, 'test-value')
        expect(localStorage.getItem('null')).toBeNull()
      })

      it('应该处理 null value', () => {
        cache.local.set('test-key', null as any)
        expect(localStorage.getItem('test-key')).toBeNull()
      })
    })

    describe('get 方法', () => {
      it('应该获取 localStorage 值', () => {
        localStorage.setItem('test-key', 'test-value')
        const result = cache.local.get('test-key')
        expect(result).toBe('test-value')
      })

      it('应该返回 null 当 key 不存在时', () => {
        const result = cache.local.get('non-existent-key')
        expect(result).toBeNull()
      })

      it('应该返回 null 当 key 为 null 时', () => {
        const result = cache.local.get(null as any)
        expect(result).toBeNull()
      })
    })

    describe('setJSON 方法', () => {
      it('应该设置 JSON 对象', () => {
        const data = { name: 'test', value: 123 }
        cache.local.setJSON('test-json', data)
        const result = localStorage.getItem('test-json')
        expect(result).toBe(JSON.stringify(data))
      })

      it('应该设置 JSON 数组', () => {
        const data = [1, 2, 3, 4, 5]
        cache.local.setJSON('test-array', data)
        const result = localStorage.getItem('test-array')
        expect(result).toBe(JSON.stringify(data))
      })

      it('应该处理 null 值', () => {
        cache.local.setJSON('test-null', null as any)
        expect(localStorage.getItem('test-null')).toBeNull()
      })
    })

    describe('getJSON 方法', () => {
      it('应该获取并解析 JSON 对象', () => {
        const data = { name: 'test', value: 123 }
        localStorage.setItem('test-json', JSON.stringify(data))
        const result = cache.local.getJSON('test-json')
        expect(result).toEqual(data)
      })

      it('应该获取并解析 JSON 数组', () => {
        const data = [1, 2, 3, 4, 5]
        localStorage.setItem('test-array', JSON.stringify(data))
        const result = cache.local.getJSON('test-array')
        expect(result).toEqual(data)
      })

      it('应该返回 null 当 key 不存在时', () => {
        const result = cache.local.getJSON('non-existent-key')
        expect(result).toBeNull()
      })
    })

    describe('remove 方法', () => {
      it('应该删除 localStorage 值', () => {
        localStorage.setItem('test-key', 'test-value')
        cache.local.remove('test-key')
        expect(localStorage.getItem('test-key')).toBeNull()
      })

      it('应该处理删除不存在的 key', () => {
        cache.local.remove('non-existent-key')
        expect(localStorage.getItem('non-existent-key')).toBeNull()
      })
    })
  })

  describe('完整场景测试', () => {
    it('应该支持完整的 session 缓存操作流程', () => {
      // 设置值
      cache.session.set('key1', 'value1')
      expect(cache.session.get('key1')).toBe('value1')

      // 设置 JSON
      const data = { name: 'test', value: 123 }
      cache.session.setJSON('key2', data)
      expect(cache.session.getJSON('key2')).toEqual(data)

      // 删除值
      cache.session.remove('key1')
      expect(cache.session.get('key1')).toBeNull()
    })

    it('应该支持完整的 local 缓存操作流程', () => {
      // 设置值
      cache.local.set('key1', 'value1')
      expect(cache.local.get('key1')).toBe('value1')

      // 设置 JSON
      const data = { name: 'test', value: 123 }
      cache.local.setJSON('key2', data)
      expect(cache.local.getJSON('key2')).toEqual(data)

      // 删除值
      cache.local.remove('key1')
      expect(cache.local.get('key1')).toBeNull()
    })

    it('应该支持 session 和 local 缓存独立操作', () => {
      // session 缓存
      cache.session.set('key', 'session-value')
      expect(cache.session.get('key')).toBe('session-value')

      // local 缓存
      cache.local.set('key', 'local-value')
      expect(cache.local.get('key')).toBe('local-value')

      // 验证两者独立
      expect(cache.session.get('key')).toBe('session-value')
      expect(cache.local.get('key')).toBe('local-value')
    })
  })
})