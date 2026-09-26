import { describe, expect, it } from 'vitest'
import { FRIEND_LINK_STATUS, auditStatusFor } from './status'

describe('友链审核状态映射', () => {
  it('通过应落库为 0（前台列表只展示 status=0）', () => {
    expect(auditStatusFor('approve')).toBe(0)
    expect(auditStatusFor('approve')).toBe(FRIEND_LINK_STATUS.NORMAL)
  })

  it('拒绝应落库为 1（停用，不出现在前台）', () => {
    expect(auditStatusFor('reject')).toBe(1)
    expect(auditStatusFor('reject')).toBe(FRIEND_LINK_STATUS.DISABLED)
  })

  it('待审核语义应为 2，与前台申请写入一致', () => {
    expect(FRIEND_LINK_STATUS.PENDING).toBe(2)
  })
})
