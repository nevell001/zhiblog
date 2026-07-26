// 获取博主技能列表
export function getSkillsList(): Promise<{ data: any[] }> {
  try {
    const skillsData = localStorage.getItem('blog_skills')
    if (skillsData) {
      return Promise.resolve({ data: JSON.parse(skillsData) })
    }
    return Promise.resolve({ data: [] })
  } catch (error) {
    return Promise.reject(error)
  }
}

// 获取博主经历列表
export function getExperienceList(): Promise<{ data: any[] }> {
  try {
    const experienceData = localStorage.getItem('blog_experience')
    if (experienceData) {
      return Promise.resolve({ data: JSON.parse(experienceData) })
    }
    return Promise.resolve({ data: [] })
  } catch (error) {
    return Promise.reject(error)
  }
}
