import { describe, it, expect } from 'vitest'
import {
  isEmpty,
  isHttp,
  isExternal,
  validEmail,
  validURL,
  validLowerCase,
  validUpperCase,
  validAlphabets,
  isArray,
  isString,
  isPathMatch
} from '@/utils/validate'

describe('验证工具函数', () => {
  describe('isEmpty', () => {
    it('应该正确识别空值', () => {
      expect(isEmpty(null)).toBe(true)
      expect(isEmpty('')).toBe(true)
      expect(isEmpty(undefined)).toBe(true)
      expect(isEmpty('undefined')).toBe(true)
    })

    it('应该正确识别非空值', () => {
      expect(isEmpty('text')).toBe(false)
      expect(isEmpty('0')).toBe(false)
    })
  })

  describe('isHttp', () => {
    it('应该正确识别 HTTP/HTTPS URL', () => {
      expect(isHttp('http://example.com')).toBe(true)
      expect(isHttp('https://example.com')).toBe(true)
      expect(isHttp('ftp://example.com')).toBe(false)
    })
  })

  describe('isExternal', () => {
    it('应该正确识别外部链接', () => {
      expect(isExternal('https://example.com')).toBe(true)
      expect(isExternal('http://example.com')).toBe(true)
      expect(isExternal('mailto:test@example.com')).toBe(true)
      expect(isExternal('tel:1234567890')).toBe(true)
      expect(isExternal('/internal/path')).toBe(false)
    })
  })

  describe('validEmail', () => {
    it('应该正确验证有效的邮箱地址', () => {
      expect(validEmail('test@example.com')).toBe(true)
      expect(validEmail('user.name+tag@example.co.uk')).toBe(true)
      expect(validEmail('user123@test-domain.com')).toBe(true)
    })

    it('应该拒绝无效的邮箱地址', () => {
      expect(validEmail('invalid')).toBe(false)
      expect(validEmail('@example.com')).toBe(false)
      expect(validEmail('user@')).toBe(false)
    })
  })

  describe('validURL', () => {
    it('应该正确验证有效的 URL', () => {
      expect(validURL('https://www.example.com')).toBe(true)
      expect(validURL('http://example.com')).toBe(true)
      expect(validURL('ftp://example.com')).toBe(true)
    })

    it('应该拒绝无效的 URL', () => {
      expect(validURL('not-a-url')).toBe(false)
      expect(validURL('')).toBe(false)
    })
  })

  describe('validLowerCase', () => {
    it('应该正确验证小写字母', () => {
      expect(validLowerCase('abc')).toBe(true)
      expect(validLowerCase('ABC')).toBe(false)
      expect(validLowerCase('Abc')).toBe(false)
    })
  })

  describe('validUpperCase', () => {
    it('应该正确验证大写字母', () => {
      expect(validUpperCase('ABC')).toBe(true)
      expect(validUpperCase('abc')).toBe(false)
      expect(validUpperCase('Abc')).toBe(false)
    })
  })

  describe('validAlphabets', () => {
    it('应该正确验证字母', () => {
      expect(validAlphabets('abc')).toBe(true)
      expect(validAlphabets('ABC')).toBe(true)
      expect(validAlphabets('Abc')).toBe(true)
      expect(validAlphabets('abc123')).toBe(false)
    })
  })

  describe('isArray', () => {
    it('应该正确识别数组', () => {
      expect(isArray([])).toBe(true)
      expect(isArray([1, 2, 3])).toBe(true)
      expect(isArray('not array')).toBe(false)
      expect(isArray(null)).toBe(false)
    })
  })

  describe('isString', () => {
    it('应该正确识别字符串', () => {
      expect(isString('text')).toBe(true)
      expect(isString('')).toBe(true)
      expect(isString(new String('text'))).toBe(true)
      expect(isString(123)).toBe(false)
      expect(isString(null)).toBe(false)
    })
  })

  describe('isPathMatch', () => {
    it('应该正确匹配路径模式', () => {
      expect(isPathMatch('/api/*', '/api/test')).toBe(true)
      expect(isPathMatch('/api/*', '/api/test/other')).toBe(false)
      // 路径模式中的 ** 实际行为需要根据具体实现确定
      // 这里我们只测试基本的单层匹配
      expect(isPathMatch('/api/test', '/api/other')).toBe(false)
    })
  })
})
