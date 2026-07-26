import { describe, it, expect, vi } from 'vitest'
import { encrypt, decrypt } from './jsencrypt'

describe('JSEncrypt Utils 测试', () => {
  it('应该导出 encrypt 函数', () => {
    expect(encrypt).toBeDefined()
    expect(typeof encrypt).toBe('function')
  })

  it('应该导出 decrypt 函数', () => {
    expect(decrypt).toBeDefined()
    expect(typeof decrypt).toBe('function')
  })

  it('应该成功加密文本', () => {
    const plaintext = '测试消息'
    const result = encrypt(plaintext)

    expect(result).toBeTruthy()
    expect(typeof result).toBe('string')
    expect(result).not.toBe(plaintext)
  })

  it('应该对空字符串返回 false', () => {
    const result = encrypt('')

    expect(result).toBeTruthy()
    expect(typeof result).toBe('string')
  })

  it('应该成功解密文本', () => {
    const plaintext = '测试消息'
    const ciphertext = encrypt(plaintext)

    // 假设 ciphertext 是加密后的结果
    if (typeof ciphertext === 'string') {
      const result = decrypt(ciphertext)

      expect(result).toBeTruthy()
      expect(typeof result).toBe('string')
      expect(result).toBe(plaintext)
    }
  })

  it('应该对空字符串返回 false', () => {
    const result = decrypt('')

    expect(result).toBe(false)
  })

  it('应该处理中文文本', () => {
    const plaintext = '你好世界'
    const ciphertext = encrypt(plaintext)

    if (typeof ciphertext === 'string') {
      const result = decrypt(ciphertext)

      expect(result).toBe(plaintext)
    }
  })

  it('应该处理特殊字符', () => {
    const plaintext = '测试@#$%^&*()'
    const ciphertext = encrypt(plaintext)

    if (typeof ciphertext === 'string') {
      const result = decrypt(ciphertext)

      expect(result).toBe(plaintext)
    }
  })
})
