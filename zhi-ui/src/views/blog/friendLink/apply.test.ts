import { describe, it, expect } from 'vitest'
import { readFileSync } from 'node:fs'
import { resolve } from 'node:path'
import FriendLinkApply from './apply.vue'

const source = readFileSync(resolve(__dirname, 'apply.vue'), 'utf8')

describe('友链申请页测试', () => {
  it('应该导出友链申请组件', () => {
    expect(FriendLinkApply).toBeDefined()
    expect(typeof FriendLinkApply).toBe('object')
  })

  it('应该声明组件 name 为 BlogFriendLinkApply', () => {
    expect(source).toContain('name="BlogFriendLinkApply"')
  })

  it('应该读取 friend_link_apply_enabled 设置开关', () => {
    expect(source).toContain('friend_link_apply_enabled')
    // 判定统一走 store.isFeatureEnabled（全站唯一口径），不再手写比较
    expect(source).toContain("isFeatureEnabled('friend_link_apply_enabled')")
    expect(source).toContain('getBlogSettingsAnonymous')
  })

  it('开关关闭时应该展示友好提示而不是白屏', () => {
    expect(source).toContain('本站暂未开放友链申请')
    expect(source).toContain('apply-closed')
    expect(source).toContain('返回首页')
  })

  it('应该按 captchaEnabled 展示可刷新的图形验证码', () => {
    expect(source).toContain("import { getCodeImg } from '@/api/blog/auth'")
    expect(source).toContain('v-if="captchaEnabled"')
    expect(source).toContain("'data:image/jpeg;base64,'")
    expect(source).toContain('@click="refreshCaptcha"')
  })

  it('提交时应该携带 code 与 uuid', () => {
    expect(source).toContain('code: form.code')
    expect(source).toContain('uuid: form.uuid')
  })

  it('提交失败后应该刷新验证码并展示后端错误信息', () => {
    expect(source).toContain('refreshCaptcha()')
    expect(source).toContain('ElMessage.error')
    expect(source).toContain("error?.message || '提交失败，请稍后重试'")
  })
})
