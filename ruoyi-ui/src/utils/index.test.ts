import { describe, it, expect } from 'vitest'
import {
  formatDate,
  formatTime,
  getQueryObject,
  byteLength,
  cleanArray,
  param,
  param2Obj,
  html2Text,
  objectMerge,
  toggleClass,
  getTime,
  debounce,
  deepClone,
  uniqueArr,
  createUniqueString,
  hasClass,
  addClass,
  removeClass,
  makeMap,
  titleCase,
  camelCase,
  isNumberStr
} from './index'

describe('Utils 工具函数测试', () => {
  describe('formatDate', () => {
    it('应该导出 formatDate 函数', () => {
      expect(formatDate).toBeDefined()
      expect(typeof formatDate).toBe('function')
    })

    it('应该格式化日期', () => {
      const date = new Date('2024-01-01T00:00:00Z')
      const result = formatDate(date)
      expect(result).toBeDefined()
      expect(typeof result).toBe('string')
    })

    it('空值应该返回空字符串', () => {
      expect(formatDate(null as any)).toBe('')
      expect(formatDate('')).toBe('')
    })
  })

  describe('formatTime', () => {
    it('应该导出 formatTime 函数', () => {
      expect(formatTime).toBeDefined()
      expect(typeof formatTime).toBe('function')
    })

    it('应该格式化时间为相对时间', () => {
      const now = Date.now()
      const result = formatTime(now - 1000) // 1秒前
      expect(result).toBeDefined()
    })
  })

  describe('getQueryObject', () => {
    it('应该导出 getQueryObject 函数', () => {
      expect(getQueryObject).toBeDefined()
      expect(typeof getQueryObject).toBe('function')
    })

    it('应该解析 URL 查询参数', () => {
      const result = getQueryObject('?a=1&b=2')
      expect(result).toBeDefined()
      expect(typeof result).toBe('object')
    })
  })

  describe('byteLength', () => {
    it('应该导出 byteLength 函数', () => {
      expect(byteLength).toBeDefined()
      expect(typeof byteLength).toBe('function')
    })

    it('应该计算字节长度', () => {
      const result = byteLength('hello')
      expect(result).toBe(5)
    })
  })

  describe('cleanArray', () => {
    it('应该导出 cleanArray 函数', () => {
      expect(cleanArray).toBeDefined()
      expect(typeof cleanArray).toBe('function')
    })

    it('应该清理数组中的空值', () => {
      const arr = [1, null, 2, undefined, 3, '']
      const result = cleanArray(arr)
      expect(result).toEqual([1, 2, 3])
    })
  })

  describe('param', () => {
    it('应该导出 param 函数', () => {
      expect(param).toBeDefined()
      expect(typeof param).toBe('function')
    })

    it('应该将对象转换为 URL 参数', () => {
      const result = param({ a: 1, b: 2 })
      expect(result).toBeDefined()
      expect(typeof result).toBe('string')
    })
  })

  describe('param2Obj', () => {
    it('应该导出 param2Obj 函数', () => {
      expect(param2Obj).toBeDefined()
      expect(typeof param2Obj).toBe('function')
    })

    it('应该将 URL 参数转换为对象', () => {
      const result = param2Obj('a=1&b=2')
      expect(result).toBeDefined()
      expect(typeof result).toBe('object')
    })
  })

  describe('html2Text', () => {
    it('应该导出 html2Text 函数', () => {
      expect(html2Text).toBeDefined()
      expect(typeof html2Text).toBe('function')
    })

    it('应该将 HTML 转换为纯文本', () => {
      const result = html2Text('<p>hello</p>')
      expect(result).toBeDefined()
    })
  })

  describe('objectMerge', () => {
    it('应该导出 objectMerge 函数', () => {
      expect(objectMerge).toBeDefined()
      expect(typeof objectMerge).toBe('function')
    })

    it('应该合并对象', () => {
      const target = { a: 1 }
      const source = { b: 2 }
      const result = objectMerge(target, source)
      expect(result).toHaveProperty('a', 1)
      expect(result).toHaveProperty('b', 2)
    })
  })

  describe('toggleClass', () => {
    it('应该导出 toggleClass 函数', () => {
      expect(toggleClass).toBeDefined()
      expect(typeof toggleClass).toBe('function')
    })
  })

  describe('getTime', () => {
    it('应该导出 getTime 函数', () => {
      expect(getTime).toBeDefined()
      expect(typeof getTime).toBe('function')
    })

    it('应该返回时间或日期对象', () => {
      const resultStart = getTime('start')
      const resultEnd = getTime('end')
      expect(resultStart).toBeDefined()
      expect(resultEnd).toBeDefined()
    })
  })

  describe('debounce', () => {
    it('应该导出 debounce 函数', () => {
      expect(debounce).toBeDefined()
      expect(typeof debounce).toBe('function')
    })
  })

  describe('deepClone', () => {
    it('应该导出 deepClone 函数', () => {
      expect(deepClone).toBeDefined()
      expect(typeof deepClone).toBe('function')
    })

    it('应该深拷贝对象', () => {
      const obj = { a: 1, b: { c: 2 } }
      const cloned = deepClone(obj)
      expect(cloned).toEqual(obj)
      expect(cloned).not.toBe(obj)
      expect(cloned.b).not.toBe(obj.b)
    })
  })

  describe('uniqueArr', () => {
    it('应该导出 uniqueArr 函数', () => {
      expect(uniqueArr).toBeDefined()
      expect(typeof uniqueArr).toBe('function')
    })

    it('应该去重数组', () => {
      const arr = [1, 2, 2, 3, 3, 3]
      const result = uniqueArr(arr)
      expect(result).toEqual([1, 2, 3])
    })
  })

  describe('createUniqueString', () => {
    it('应该导出 createUniqueString 函数', () => {
      expect(createUniqueString).toBeDefined()
      expect(typeof createUniqueString).toBe('function')
    })

    it('应该创建唯一字符串', () => {
      const str1 = createUniqueString()
      const str2 = createUniqueString()
      expect(str1).not.toBe(str2)
    })
  })

  describe('hasClass', () => {
    it('应该导出 hasClass 函数', () => {
      expect(hasClass).toBeDefined()
      expect(typeof hasClass).toBe('function')
    })
  })

  describe('addClass', () => {
    it('应该导出 addClass 函数', () => {
      expect(addClass).toBeDefined()
      expect(typeof addClass).toBe('function')
    })
  })

  describe('removeClass', () => {
    it('应该导出 removeClass 函数', () => {
      expect(removeClass).toBeDefined()
      expect(typeof removeClass).toBe('function')
    })
  })

  describe('makeMap', () => {
    it('应该导出 makeMap 函数', () => {
      expect(makeMap).toBeDefined()
      expect(typeof makeMap).toBe('function')
    })

    it('应该创建映射函数', () => {
      const map = makeMap('a,b,c')
      expect(map('a')).toBe(true)
      expect(map('b')).toBe(true)
      expect(map('c')).toBe(true)
      expect(map('d')).toBeFalsy() // 返回 undefined 或 false
    })

    it('应该支持大小写不敏感模式', () => {
      const map = makeMap('a,b,c', true)
      expect(map('A')).toBe(true)
      expect(map('B')).toBe(true)
      expect(map('C')).toBe(true)
      expect(map('D')).toBeFalsy() // 返回 undefined 或 false
    })
  })

  describe('titleCase', () => {
    it('应该导出 titleCase 函数', () => {
      expect(titleCase).toBeDefined()
      expect(typeof titleCase).toBe('function')
    })

    it('应该转换为首字母大写', () => {
      const result = titleCase('hello')
      expect(result).toBe('Hello')
    })
  })

  describe('camelCase', () => {
    it('应该导出 camelCase 函数', () => {
      expect(camelCase).toBeDefined()
      expect(typeof camelCase).toBe('function')
    })

    it('应该转换下划线命名为驼峰命名', () => {
      const result = camelCase('hello_world')
      expect(result).toBe('helloWorld')
    })

    it('应该处理多个下划线', () => {
      const result = camelCase('hello_world_test')
      expect(result).toBe('helloWorldTest')
    })
  })

  describe('isNumberStr', () => {
    it('应该导出 isNumberStr 函数', () => {
      expect(isNumberStr).toBeDefined()
      expect(typeof isNumberStr).toBe('function')
    })

    it('应该识别数字字符串', () => {
      expect(isNumberStr('123')).toBe(true)
      expect(isNumberStr('abc')).toBe(false)
    })
  })
})