import { describe, it, expect } from 'vitest'
import { readFileSync } from 'node:fs'
import { resolve } from 'node:path'
import BlogSetting from './index.vue'

const sourcePath = resolve(process.cwd(), 'src/views/admin/blog/setting/index.vue')

describe('BlogSetting 视图组件测试', () => {
  it('应该导出 BlogSetting 组件', () => {
    expect(BlogSetting).toBeDefined()
    expect(typeof BlogSetting).toBe('object')
  })

  it('应该在后台博客设置中提供应用主题切换入口', () => {
    const source = readFileSync(sourcePath, 'utf-8')

    expect(source).toContain('<el-tab-pane label="界面主题" name="theme">')
    expect(source).toContain(':model-value="settingsStore.appTheme"')
    expect(source).toContain('@change="handleAppThemeChange"')
    expect(source).toContain("value: 'default'")
    expect(source).toContain("value: 'mo-blog'")
    expect(source).toContain('settingsStore.setAppTheme')
  })

  it('不应该继续使用旧的高饱和彩色渐变作为后台设置页装饰', () => {
    const source = readFileSync(sourcePath, 'utf-8')

    expect(source).not.toMatch(/#4a7bff|#6b8cff|#f093fb|#f5576c|#4facfe|#00f2fe/i)
  })

  it('开关类设置应改动即生效（绑定 applySwitch 且带失败回滚）', () => {
    const source = readFileSync(sourcePath, 'utf-8')

    expect(source).toContain('@change="applySwitch(\'friend_link_apply_enabled\')"')
    expect(source).toContain('@change="applySwitch(\'friend_link_enabled\')"')
    expect(source).toContain('async function applySwitch')
    expect(source).toContain('updateSettingValueByKey(key, storedValue)')
    // 保存失败要把开关回滚，避免界面与数据库不一致
    expect(source).toContain('originalSettings.value[key]')
  })

  it('应该在后台提供友链申请入口开关', () => {
    const source = readFileSync(sourcePath, 'utf-8')

    expect(source).toContain('prop="friend_link_apply_enabled"')
    expect(source).toContain('v-model="settingsMap.friend_link_apply_enabled"')
    // 两个开关标签需能一眼区分：页脚友链列表 vs 友链申请入口
    expect(source).toContain('label="友链申请入口"')
    expect(source).toContain('label="页脚友链列表"')
    expect(source).toContain("'friend_link_apply_enabled'")
    expect(source).toContain('friend_link_apply_enabled: true')
  })
})
