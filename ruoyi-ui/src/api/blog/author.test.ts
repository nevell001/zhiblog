import { describe, it, expect, vi } from 'vitest'
import { getSkillsList, getExperienceList } from './author'

// Mock request module
vi.mock('@/utils/request', () => ({
  request: vi.fn()
}))

describe('Author API 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('getSkillsList', () => {
    it('应该导出 getSkillsList 函数', () => {
      expect(getSkillsList).toBeDefined()
      expect(typeof getSkillsList).toBe('function')
    })
  })

  describe('getExperienceList', () => {
    it('应该导出 getExperienceList 函数', () => {
      expect(getExperienceList).toBeDefined()
      expect(typeof getExperienceList).toBe('function')
    })
  })
})