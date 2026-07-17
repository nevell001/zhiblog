import vue from '@vitejs/plugin-vue'

import createAutoImport from './auto-import'
import createSvgIcon from './svg-icon'
import createCompression from './compression'
import createSetupExtend from './setup-extend'
import createComponents from './components'

export default function createVitePlugins(viteEnv, isBuild = false) {
  const vitePlugins = [vue()]

  // 自动导入插件
  vitePlugins.push(createAutoImport())

  // Element Plus 组件按需导入
  vitePlugins.push(createComponents())

  // SVG 图标插件
  vitePlugins.push(createSvgIcon(isBuild))

  // setup 插件
  vitePlugins.push(createSetupExtend())

  return vitePlugins
}
