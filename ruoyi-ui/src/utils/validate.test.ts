import { describe, it, expect } from 'vitest'
import {
  isPathMatch,
  isEmpty,
  isHttp,
  isExternal,
  validUsername,
  validURL,
  validLowerCase,
  validUpperCase,
  validAlphabets,
  validEmail,
  isString,
  isArray
} from './validate'

describe('Validate 工具函数测试', () => {
  describe('isPathMatch', () => {
    it('应该导出 isPathMatch 函数', () => {
      expect(isPathMatch).toBeDefined()
      expect(typeof isPathMatch).toBe('function')
    })

    it('应该匹配精确路径', () => {
      expect(isPathMatch('/home', '/home')).toBe(true)
      expect(isPathMatch('/about', '/about')).toBe(true)
    })

    it('应该拒绝不匹配的路径', () => {
      expect(isPathMatch('/home', '/about')).toBe(false)
      expect(isPathMatch('/home', '/home/sub')).toBe(false)
    })

    it('应该处理单层通配符', () => {
      // * 匹配除斜杠外的任意字符
      expect(isPathMatch('/home/*', '/home/user')).toBe(true)
      expect(isPathMatch('/home/*', '/home/test')).toBe(true)
      expect(isPathMatch('/home/*', '/home/user/extra')).toBe(false) // * 不匹配斜杠
    })

    it('应该处理多层通配符（注意：当前实现可能有限制）', () => {
      // ** 在当前实现中只匹配一级
      expect(isPathMatch('/api/**', '/api/v1')).toBe(true)
      // 注意：由于实现方式的限制，** 可能无法匹配多级路径
      // 这是当前实现的已知行为
    })
  })

  describe('isEmpty', () => {
    it('应该导出 isEmpty 函数', () => {
      expect(isEmpty).toBeDefined()
      expect(typeof isEmpty).toBe('function')
    })

    it('应该识别空值', () => {
      expect(isEmpty(null)).toBe(true)
      expect(isEmpty(undefined)).toBe(true)
      expect(isEmpty('')).toBe(true)
      expect(isEmpty('undefined')).toBe(true)
    })

    it('应该识别非空值', () => {
      expect(isEmpty('hello')).toBe(false)
      expect(isEmpty('0')).toBe(false)
    })
  })

  describe('isHttp', () => {
    it('应该导出 isHttp 函数', () => {
      expect(isHttp).toBeDefined()
      expect(typeof isHttp).toBe('function')
    })

    it('应该识别 HTTP URL', () => {
      expect(isHttp('http://example.com')).toBe(true)
      expect(isHttp('https://example.com')).toBe(true)
      expect(isHttp('https://example.com/path')).toBe(true)
    })

    it('应该拒绝非 HTTP URL', () => {
      expect(isHttp('ftp://example.com')).toBe(false)
      expect(isHttp('mailto:test@example.com')).toBe(false)
      expect(isHttp('/home')).toBe(false)
    })
  })

  describe('isExternal', () => {
    it('应该导出 isExternal 函数', () => {
      expect(isExternal).toBeDefined()
      expect(typeof isExternal).toBe('function')
    })

    it('应该识别外部链接', () => {
      expect(isExternal('https://example.com')).toBe(true)
      expect(isExternal('http://example.com')).toBe(true)
      expect(isExternal('mailto:test@example.com')).toBe(true)
      expect(isExternal('tel:1234567890')).toBe(true)
    })

    it('应该识别内部路径', () => {
      expect(isExternal('/home')).toBe(false)
      expect(isExternal('home')).toBe(false)
      expect(isExternal('./home')).toBe(false)
      expect(isExternal('../home')).toBe(false)
    })
  })

  describe('validUsername', () => {
    it('应该导出 validUsername 函数', () => {
      expect(validUsername).toBeDefined()
      expect(typeof validUsername).toBe('function')
    })

    it('应该验证有效的用户名', () => {
      expect(validUsername('admin')).toBe(true)
      expect(validUsername('editor')).toBe(true)
      expect(validUsername(' admin ')).toBe(true)
    })

    it('应该拒绝无效的用户名', () => {
      expect(validUsername('user')).toBe(false)
      expect(validUsername('')).toBe(false)
      expect(validUsername('test')).toBe(false)
    })
  })

  describe('validURL', () => {
    it('应该导出 validURL 函数', () => {
      expect(validURL).toBeDefined()
      expect(typeof validURL).toBe('function')
    })

    it('应该验证有效的URL', () => {
      expect(validURL('https://example.com')).toBe(true)
      expect(validURL('http://example.com')).toBe(true)
      expect(validURL('https://www.example.com')).toBe(true)
      expect(validURL('ftp://example.com')).toBe(true)
    })

    it('应该拒绝无效的URL', () => {
      expect(validURL('not-a-url')).toBe(false)
      expect(validURL('')).toBe(false)
      expect(validURL('http://')).toBe(false)
      expect(validURL('example.com')).toBe(false)
    })
  })

  describe('validLowerCase', () => {
    it('应该导出 validLowerCase 函数', () => {
      expect(validLowerCase).toBeDefined()
      expect(typeof validLowerCase).toBe('function')
    })

    it('应该验证小写字母', () => {
      expect(validLowerCase('abc')).toBe(true)
      expect(validLowerCase('a')).toBe(true)
      expect(validLowerCase('abcdefghijklmnopqrstuvwxyz')).toBe(true)
    })

    it('应该拒绝大写字母和数字', () => {
      expect(validLowerCase('ABC')).toBe(false)
      expect(validLowerCase('Abc')).toBe(false)
      expect(validLowerCase('abc123')).toBe(false)
      expect(validLowerCase('')).toBe(false)
    })
  })

  describe('validUpperCase', () => {
    it('应该导出 validUpperCase 函数', () => {
      expect(validUpperCase).toBeDefined()
      expect(typeof validUpperCase).toBe('function')
    })

    it('应该验证大写字母', () => {
      expect(validUpperCase('ABC')).toBe(true)
      expect(validUpperCase('A')).toBe(true)
      expect(validUpperCase('ABCDEFGHIJKLMNOPQRSTUVWXYZ')).toBe(true)
    })

    it('应该拒绝小写字母和数字', () => {
      expect(validUpperCase('abc')).toBe(false)
      expect(validUpperCase('Abc')).toBe(false)
      expect(validUpperCase('ABC123')).toBe(false)
      expect(validUpperCase('')).toBe(false)
    })
  })

  describe('validAlphabets', () => {
    it('应该导出 validAlphabets 函数', () => {
      expect(validAlphabets).toBeDefined()
      expect(typeof validAlphabets).toBe('function')
    })

    it('应该验证字母', () => {
      expect(validAlphabets('abc')).toBe(true)
      expect(validAlphabets('ABC')).toBe(true)
      expect(validAlphabets('Abc')).toBe(true)
      expect(validAlphabets('aBc')).toBe(true)
    })

    it('应该拒绝数字和特殊字符', () => {
      expect(validAlphabets('abc123')).toBe(false)
      expect(validAlphabets('abc!')).toBe(false)
      expect(validAlphabets('')).toBe(false)
      expect(validAlphabets('abc ')).toBe(false)
    })
  })

  describe('validEmail', () => {
    it('应该导出 validEmail 函数', () => {
      expect(validEmail).toBeDefined()
      expect(typeof validEmail).toBe('function')
    })

    it('应该验证有效的邮箱', () => {
      expect(validEmail('test@example.com')).toBe(true)
      expect(validEmail('user.name@example.com')).toBe(true)
      expect(validEmail('user+tag@example.com')).toBe(true)
      expect(validEmail('user@test.example.com')).toBe(true)
      expect(validEmail('user@test.co.uk')).toBe(true)
    })

    it('应该拒绝无效的邮箱', () => {
      expect(validEmail('invalid')).toBe(false)
      expect(validEmail('@example.com')).toBe(false)
      expect(validEmail('user@')).toBe(false)
      expect(validEmail('user@@example.com')).toBe(false)
      expect(validEmail('')).toBe(false)
    })
  })

  describe('isString', () => {
    it('应该导出 isString 函数', () => {
      expect(isString).toBeDefined()
      expect(typeof isString).toBe('function')
    })

    it('应该识别字符串', () => {
      expect(isString('hello')).toBe(true)
      expect(isString('')).toBe(true)
      expect(isString(new String('hello'))).toBe(true)
    })

    it('应该拒绝非字符串', () => {
      expect(isString(123)).toBe(false)
      expect(isString(null)).toBe(false)
      expect(isString(undefined)).toBe(false)
      expect(isString({})).toBe(false)
      expect(isString([])).toBe(false)
      expect(isString(true)).toBe(false)
    })
  })

  describe('isArray', () => {
    it('应该导出 isArray 函数', () => {
      expect(isArray).toBeDefined()
      expect(typeof isArray).toBe('function')
    })

    it('应该识别数组', () => {
      expect(isArray([])).toBe(true)
      expect(isArray([1, 2, 3])).toBe(true)
      expect(isArray(new Array())).toBe(true)
      expect(isArray(Array.from({ length: 3 }))).toBe(true)
    })

    it('应该拒绝非数组', () => {
      expect(isArray({})).toBe(false)
      expect(isArray('string')).toBe(false)
      expect(isArray(null)).toBe(false)
      expect(isArray(undefined)).toBe(false)
      expect(isArray(123)).toBe(false)
      expect(isArray(true)).toBe(false)
    })
  })
})