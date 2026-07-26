import { describe, expect, it } from 'vitest'
import { devOptimizeDeps } from './optimizeDeps'

describe('devOptimizeDeps', () => {
  it('pre-bundles frequently discovered admin dependencies', () => {
    expect(devOptimizeDeps.include).toEqual(
      expect.arrayContaining([
        'element-plus/es',
        'element-plus/es/components/form/style/css',
        'element-plus/es/components/table/style/css',
        'element-plus/es/components/dialog/style/css',
        'element-plus/es/components/upload/style/css',
        '@vueup/vue-quill'
      ])
    )
  })
})
