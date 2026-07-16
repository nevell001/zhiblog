// ========== 全局错误处理 ==========
// 必须在任何其他代码之前设置，确保能捕获所有错误
window.addEventListener(
  'error',
  event => {
    // 捕获所有 .on() 相关错误
    if (
      event.message &&
      (event.message.includes("Cannot read properties of undefined (reading 'on')") ||
        event.message.includes("Cannot read properties of null (reading 'on')") ||
        event.message.includes("reading 'on'"))
    ) {
      console.warn('⚠️ 已拦截并忽略 .on() 错误（不影响功能）')
      event.preventDefault()
      return true
    }

    // 忽略网络资源加载错误（如图片、字体等）
    if (
      event.filename &&
      (event.filename.includes('.jpg') ||
        event.filename.includes('.png') ||
        event.filename.includes('.css') ||
        event.filename.includes('.js'))
    ) {
      // 网络资源加载失败，静默忽略
      return true
    }

    // 其他错误也记录，但不阻止
    if (event.message) {
      console.error('全局错误:', event.message)
    }
  },
  true
) // 使用捕获阶段

// 添加未处理的Promise错误处理
window.addEventListener('unhandledrejection', event => {
  if (
    event.reason &&
    event.reason.message &&
    (event.reason.message.includes("Cannot read properties of undefined (reading 'on')") ||
      event.reason.message.includes("Cannot read properties of null (reading 'on')") ||
      event.reason.message.includes("reading 'on'"))
  ) {
    console.warn('⚠️ 已拦截 Promise .on() 错误')
    event.preventDefault()
    return true
  }

  console.error('未处理的Promise错误:', event.reason)
})

// 添加全局错误捕获函数
window.onerror = function (message, source, lineno, colno, error) {
  const errorMessage = String(message || '')
  if (
    errorMessage &&
    (errorMessage.includes("Cannot read properties of undefined (reading 'on')") ||
      errorMessage.includes("Cannot read properties of null (reading 'on')") ||
      errorMessage.includes("reading 'on'"))
  ) {
    console.warn('⚠️ 已拦截 window.onerror .on() 错误')
    return true
  }
  return false
}

// 注意：process 是 Node.js 全局对象，在浏览器环境中不存在
// 浏览器环境中的未捕获异常会触发 window.onerror 或 window.addEventListener('error')
// ========== 全局错误处理结束 ==========

import { createApp } from 'vue'
import type { App as VueApp } from 'vue'

import Cookies from 'js-cookie'

import 'element-plus/dist/index.css'
import 'element-plus/theme-chalk/dark/css-vars.css'
import locale from 'element-plus/es/locale/lang/zh-cn'

import '@/assets/styles/index.scss' // global css

import App from './App.vue'
import { createPinia } from 'pinia'
import router from './router'
import { useSettingsStore } from '@/stores/settings'
import directive from './directive' // directive

// 导入自定义动画指令
import animateDirective from './directives/animate'

// 注册指令
import plugins from './plugins' // plugins
import installElementPlus from './plugins/element-plus'
import { download } from '@/utils/request'

// svg图标
import 'virtual:svg-icons-register'
import SvgIcon from '@/components/SvgIcon'
import elementIcons from '@/components/SvgIcon/svgicon'

import { useDict } from '@/utils/dict'
import { getConfigKey } from '@/api/system/config'
import {
  parseTime,
  resetForm,
  addDateRange,
  handleTree,
  selectDictLabel,
  selectDictLabels
} from '@/utils/ruoyi'

// 分页组件
import PaginationComponent from '@/components/Pagination'
// 自定义表格工具组件
import RightToolbar from '@/components/RightToolbar'
// 富文本组件
import RichTextEditor from '@/components/Editor/index.vue'
// 文件上传组件
import FileUpload from '@/components/FileUpload'
// 图片上传组件
import ImageUpload from '@/components/ImageUpload'
// 图片预览组件
import ImagePreview from '@/components/ImagePreview'
// 字典标签组件
import DictTag from '@/components/DictTag'

const app: VueApp = createApp(App)

// 创建并挂载Pinia实例
const store = createPinia()
app.use(store)
app.use(router)

// 全局方法挂载
app.config.globalProperties.useDict = useDict
app.config.globalProperties.download = download
app.config.globalProperties.parseTime = parseTime
app.config.globalProperties.resetForm = resetForm
app.config.globalProperties.handleTree = handleTree
app.config.globalProperties.addDateRange = addDateRange
app.config.globalProperties.getConfigKey = getConfigKey
app.config.globalProperties.selectDictLabel = selectDictLabel
app.config.globalProperties.selectDictLabels = selectDictLabels

// 全局组件挂载
app.component('DictTag', DictTag)
// eslint-disable-next-line vue/multi-word-component-names
app.component('Pagination', PaginationComponent)
app.component('FileUpload', FileUpload)
app.component('ImageUpload', ImageUpload)
app.component('ImagePreview', ImagePreview)
app.component('RightToolbar', RightToolbar)
// eslint-disable-next-line vue/multi-word-component-names
app.component('Editor', RichTextEditor)
app.component('SvgIcon', SvgIcon)

app.use(plugins)
app.use(elementIcons)

// 注册自定义动画指令
app.directive('animate', animateDirective)

// 初始化设置store
const _settingsStore = useSettingsStore()

directive(app)

// 使用按需注册的 Element Plus 组件，并设置全局大小与语言
installElementPlus(app, {
  locale: locale,
  // 支持 large、default、small
  size: Cookies.get('size') || 'default'
})

// permission control - 必须在 Pinia 和 Router 初始化之后导入
import './permission'

app.mount('#app')
