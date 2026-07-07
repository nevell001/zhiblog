import { describe, it, expect, vi, beforeEach } from 'vitest'
import { getSkillsList, getExperienceList } from './author'

// Mock localStorage
const localStorageMock = {
  getItem: vi.fn(),
  setItem: vi.fn(),
  clear: vi.fn()
}

describe('Author API 测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    // @ts-expect-error - mocking localStorage
    global.localStorage = localStorageMock
  })

  describe('getSkillsList', () => {
    it('应该导出 getSkillsList 函数', () => {
      expect(getSkillsList).toBeDefined()
      expect(typeof getSkillsList).toBe('function')
    })

    it('应该从 localStorage 获取技能列表', async () => {
      localStorageMock.getItem.mockReturnValue('[{"name": "Vue"}, {"name": "TypeScript"}]')

      const result = await getSkillsList()

      expect(localStorageMock.getItem).toHaveBeenCalledWith('blog_skills')
      expect(result.data).toEqual([{ name: 'Vue' }, { name: 'TypeScript' }])
    })

    it('应该在没有数据时返回空数组', async () => {
      localStorageMock.getItem.mockReturnValue(null)

      const result = await getSkillsList()

      expect(result.data).toEqual([])
    })
  })

  describe('getExperienceList', () => {
    it('应该导出 getExperienceList 函数', () => {
      expect(getExperienceList).toBeDefined()
      expect(typeof getExperienceList).toBe('function')
    })

    it('应该从 localStorage 获取经历列表', async () => {
      localStorageMock.getItem.mockReturnValue('[{"company": "公司A", "role": "开发工程师"}]')

      const result = await getExperienceList()

      expect(localStorageMock.getItem).toHaveBeenCalledWith('blog_experience')
      expect(result.data).toEqual([{ company: '公司A', role: '开发工程师' }])
    })

    it('应该在没有数据时返回空数组', async () => {
      localStorageMock.getItem.mockReturnValue(null)

      const result = await getExperienceList()

      expect(result.data).toEqual([])
    })
  })
})
