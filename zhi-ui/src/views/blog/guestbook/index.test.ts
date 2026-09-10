import { describe, it, expect, vi, beforeEach } from 'vitest'
import { readFileSync } from 'node:fs'
import { resolve } from 'node:path'
import Guestbook from './index.vue'

vi.mock('@/api/blog/message')
vi.mock('@/api/blog/setting')
vi.mock('@/api/blog/page')

const source = readFileSync(resolve(__dirname, 'index.vue'), 'utf8')

describe('留言板页面测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('应该导出留言板组件', () => {
    expect(Guestbook).toBeDefined()
    expect(typeof Guestbook).toBe('object')
  })

  it('应该包在 BlogLayout 中', () => {
    expect(source).toContain('<BlogLayout>')
    expect(source).toContain("from '@/components/BlogLayout.vue'")
  })

  it('应该有顶部标题与说明', () => {
    expect(source).toContain('留言板')
    expect(source).toContain('guestbook-desc')
    expect(source).toContain('共 {{ messageCount }} 条留言')
  })

  it('应该包含昵称/邮箱/网站/内容表单与校验规则', () => {
    expect(source).toContain('prop="nickname"')
    expect(source).toContain('prop="email"')
    expect(source).toContain('prop="website"')
    expect(source).toContain('prop="content"')
    expect(source).toContain(':rules="rules"')
  })

  it('内容应该是 500 字上限并带字数统计', () => {
    expect(source).toContain('maxlength="500"')
    expect(source).toContain('show-word-limit')
    expect(source).toContain('留言内容长度不能超过500个字符')
  })

  it('提交按钮应该有 loading 状态', () => {
    expect(source).toContain(':loading="submitting"')
  })

  it('应该展示昵称/网站/时间/内容与管理员回复高亮', () => {
    expect(source).toContain('{{ item.nickname }}')
    expect(source).toContain('item.website')
    expect(source).toContain('formatTime(item.createTime)')
    expect(source).toContain('{{ item.content }}')
    expect(source).toContain('item.replyContent')
    expect(source).toContain('管理员回复')
    expect(source).toContain('message-reply')
  })

  it('不应该展示邮箱等隐私字段', () => {
    expect(source).not.toContain('item.email')
  })

  it('应该使用分页组件', () => {
    expect(source).toContain('<pagination')
    expect(source).toContain('v-model:page="pageNum"')
    expect(source).toContain('v-model:limit="pageSize"')
    expect(source).toContain(':total="total"')
  })

  it('应该有空状态', () => {
    expect(source).toContain('<el-empty')
  })

  it('提交成功后应该按 comment_review 开关区分提示并刷新列表', () => {
    expect(source).toContain('comment_review')
    expect(source).toContain('留言已提交，审核通过后展示')
    expect(source).toContain('留言成功')
    expect(source).toContain('await Promise.all([loadMessages(), loadMessageCount()])')
  })
})
