import { describe, expect, it } from 'vitest'
import { readFileSync } from 'node:fs'
import { resolve } from 'node:path'

const sourcePath = resolve(process.cwd(), 'vite.config.ts')

describe('Vite dev proxy route boundaries', () => {
  it('should keep public blog page routes out of API proxy matching', () => {
    const source = readFileSync(sourcePath, 'utf-8')

    expect(source).toContain("'^/blog/api/'")
    expect(source).not.toContain("'^/blog/(article|tag|category|setting|comment)/'")
  })
})
