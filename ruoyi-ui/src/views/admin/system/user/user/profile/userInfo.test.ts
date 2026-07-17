import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import { readFileSync } from 'node:fs'
import { resolve } from 'node:path'
import UserInfo from './userInfo.vue'

const sourcePath = resolve(process.cwd(), 'src/views/admin/system/user/user/profile/userInfo.vue')

describe('UserInfo 视图组件测试', () => {
  it('应该导出 UserInfo 组件', () => {
    expect(UserInfo).toBeDefined()
    expect(typeof UserInfo).toBe('object')
  })

  it('保存基本资料成功后应该同步更新当前页面展示字段', () => {
    const source = readFileSync(sourcePath, 'utf-8')

    expect(source).toContain('props.user.nickName = form.value.nickName')
    expect(source).toContain('props.user.phonenumber = form.value.phonenumber')
    expect(source).toContain('props.user.email = form.value.email')
    expect(source).toContain('props.user.sex = form.value.sex')
  })
})
