import { describe, expect, it } from 'vitest'
import { readdirSync, readFileSync } from 'node:fs'
import { join, relative, resolve } from 'node:path'

const scanRoots = [
  resolve(process.cwd(), 'src/views/admin'),
  resolve(process.cwd(), 'src/views/system')
]

function listVueFiles(dir: string): string[] {
  return readdirSync(dir, { withFileTypes: true }).flatMap(entry => {
    const filePath = join(dir, entry.name)
    if (entry.isDirectory()) {
      return listVueFiles(filePath)
    }
    return filePath.endsWith('.vue') ? [filePath] : []
  })
}

function getLine(source: string, index: number): number {
  return source.slice(0, index).split('\n').length
}

function getCreationButtons() {
  return scanRoots.flatMap(root =>
    listVueFiles(root).flatMap(file => {
      const source = readFileSync(file, 'utf8')
      const buttons = [...source.matchAll(/<el-button[\s\S]*?<\/el-button>/g)]
      return buttons
        .filter(match => {
          const block = match[0]
          return (
            />\s*(新增|创建|添加用户)\s*</.test(block) ||
            (/handleAdd|openCreateTable|openSelectUser/.test(block) && /Plus/.test(block))
          )
        })
        .map(match => ({
          file: relative(process.cwd(), file),
          line: getLine(source, match.index ?? 0),
          block: match[0].replace(/\s+/g, ' ').trim()
        }))
    })
  )
}

describe('后台新增按钮配色', () => {
  it('所有后台新增入口都应该使用 primary 体系', () => {
    const buttons = getCreationButtons()

    expect(buttons.length).toBeGreaterThan(0)
    expect(buttons).toEqual(
      buttons.map(button => ({
        ...button,
        block: expect.stringContaining('type="primary"')
      }))
    )
  })
})
