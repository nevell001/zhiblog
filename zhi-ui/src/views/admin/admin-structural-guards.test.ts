import { describe, expect, it } from 'vitest'
import { readFileSync, readdirSync } from 'node:fs'
import { resolve } from 'node:path'

/**
 * 后台页面的两条结构守卫（读源码做跨文件一致性检查，不是行为断言）。
 *
 * 审计问题：① 24 个后台列表页在请求失败时不会复位 loading —— 接口一报错页面就永远转圈、
 * 无法重试；② 管理端 Markdown 预览直接把渲染结果塞进 v-html，与前台详情页的消毒处理不一致。
 */
const ADMIN_DIR = resolve(process.cwd(), 'src/views/admin')

function adminVueFiles(): string[] {
  return (readdirSync(ADMIN_DIR, { recursive: true }) as string[])
    .filter(name => name.endsWith('.vue'))
    .map(name => resolve(ADMIN_DIR, name))
}

describe('后台页面结构守卫', () => {
  it('设置 loading 的页面必须有复位路径（promise .finally 或 try/finally）', () => {
    const offenders = adminVueFiles()
      .filter(file => readFileSync(file, 'utf-8').includes('loading.value = true'))
      .filter(file => !readFileSync(file, 'utf-8').includes('finally'))
      .map(file => file.replace(resolve(process.cwd(), '') + '/', ''))

    expect(
      offenders,
      `以下页面在请求失败时不会复位 loading，接口一报错就永久转圈：\n  ${offenders.join('\n  ')}`
    ).toEqual([])
  })

  it('后台 v-html 的内容必须经过消毒', () => {
    const offenders = adminVueFiles()
      .filter(file => /v-html=/.test(readFileSync(file, 'utf-8')))
      .filter(file => !/sanitize(ArticleContent|Html|Comment)/.test(readFileSync(file, 'utf-8')))
      .map(file => file.replace(resolve(process.cwd(), '') + '/', ''))

    expect(offenders, `以下后台页面把未消毒内容交给 v-html：\n  ${offenders.join('\n  ')}`).toEqual(
      []
    )
  })
})
