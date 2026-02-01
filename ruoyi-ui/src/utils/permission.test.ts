import { describe, it, expect, beforeEach, vi } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useUserStore } from '@/stores/user'
import { checkPermi, checkRole } from './permission'

describe('Permission 工具函数测试', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
  })

  describe('checkPermi', () => {
    it('应该导出 checkPermi 函数', () => {
      expect(checkPermi).toBeDefined()
      expect(typeof checkPermi).toBe('function')
    })

    it('应该处理空权限数组', () => {
      const result = checkPermi([])
      expect(result).toBe(false)
    })

    it('应该处理超级管理员权限', () => {
      const userStore = useUserStore()
      userStore.permissions = ['*:*:*']
      const result = checkPermi(['system:user:add'])
      expect(result).toBe(true)
    })

    it('应该检查权限是否存在', () => {
      const userStore = useUserStore()
      userStore.permissions = ['system:user:list', 'system:user:add']
      const result = checkPermi(['system:user:list'])
      expect(result).toBe(true)
    })

    it('权限不存在时应该返回 false', () => {
      const userStore = useUserStore()
      userStore.permissions = ['system:user:list']
      const result = checkPermi(['system:user:add'])
      expect(result).toBe(false)
    })
  })

  describe('checkRole', () => {
    it('应该导出 checkRole 函数', () => {
      expect(checkRole).toBeDefined()
      expect(typeof checkRole).toBe('function')
    })

    it('应该处理空角色数组', () => {
      const result = checkRole([])
      expect(result).toBe(false)
    })

    it('应该处理超级管理员角色', () => {
      const userStore = useUserStore()
      userStore.roles = ['admin']
      const result = checkRole(['user'])
      expect(result).toBe(true)
    })

    it('应该检查角色是否存在', () => {
      const userStore = useUserStore()
      userStore.roles = ['user', 'editor']
      const result = checkRole(['user'])
      expect(result).toBe(true)
    })

    it('角色不存在时应该返回 false', () => {
      const userStore = useUserStore()
      userStore.roles = ['user']
      const result = checkRole(['admin'])
      expect(result).toBe(false)
    })
  })
})