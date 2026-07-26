import type { DepOptimizationOptions } from 'vite'

const elementPlusStyleDeps = [
  'base',
  'button',
  'dropdown',
  'dropdown-menu',
  'dropdown-item',
  'avatar',
  'icon',
  'form',
  'form-item',
  'input',
  'card',
  'row',
  'col',
  'scrollbar',
  'menu',
  'sub-menu',
  'menu-item',
  'tag',
  'tooltip',
  'drawer',
  'switch',
  'divider',
  'select',
  'option',
  'color-picker',
  'breadcrumb',
  'breadcrumb-item',
  'dialog',
  'tabs',
  'radio-group',
  'radio-button',
  'tab-pane',
  'alert',
  'upload',
  'input-number',
  'table',
  'table-column',
  'pagination',
  'transfer',
  'checkbox',
  'skeleton',
  'skeleton-item',
  'radio',
  'image'
]

export const devOptimizeDeps: DepOptimizationOptions = {
  include: [
    'element-plus/es',
    ...elementPlusStyleDeps.map(name => `element-plus/es/components/${name}/style/css`),
    '@vueup/vue-quill'
  ]
}
