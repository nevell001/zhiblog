import { describe, expect, it, vi, beforeEach, afterEach } from 'vitest'
import { createLogger } from './logger'

describe('logger utility', () => {
  beforeEach(() => {
    vi.spyOn(console, 'warn').mockImplementation(() => {})
    vi.spyOn(console, 'error').mockImplementation(() => {})
  })

  afterEach(() => {
    vi.restoreAllMocks()
  })

  it('should silence diagnostic logs in production by default', () => {
    const logger = createLogger(true)
    logger.warn('hidden warning')
    logger.error('hidden error')

    expect(console.warn).not.toHaveBeenCalled()
    expect(console.error).not.toHaveBeenCalled()
  })

  it('should keep diagnostic logs in development', () => {
    const logger = createLogger(false)
    logger.warn('visible warning')
    logger.error('visible error')

    expect(console.warn).toHaveBeenCalledWith('visible warning')
    expect(console.error).toHaveBeenCalledWith('visible error')
  })
})
