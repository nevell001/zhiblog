import { createApp } from 'vue'

import Cookies from 'js-cookie'

// 导入 highlight.js 用于 Quill 编辑器
import hljs from 'highlight.js'
import 'highlight.js/styles/github.css'
window.hljs = hljs

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import 'element-plus/theme-chalk/dark/css-vars.css'
import locale from 'element-plus/es/locale/lang/zh-cn'

import '@/assets/styles/index.scss' // global css

import App from './App';
import { createPinia } from 'pinia';
import router from './router';
import { useSettingsStore } from './stores/settings';
import directive from './directive' // directive

// 注册指令
import plugins from './plugins' // plugins
import { download } from '@/utils/request'

// svg图标
import 'virtual:svg-icons-register'
import SvgIcon from '@/components/SvgIcon'
import elementIcons from '@/components/SvgIcon/svgicon'

import './permission' // permission control

import { useDict } from '@/utils/dict'
import { getConfigKey } from "@/api/system/config"
import { parseTime, resetForm, addDateRange, handleTree, selectDictLabel, selectDictLabels } from '@/utils/ruoyi'

// 分页组件
import Pagination from '@/components/Pagination'
// 自定义表格工具组件
import RightToolbar from '@/components/RightToolbar'
// 富文本组件
import Editor from "@/components/Editor"
// 文件上传组件
import FileUpload from "@/components/FileUpload"
// 图片上传组件
import ImageUpload from "@/components/ImageUpload"
// 图片预览组件
import ImagePreview from "@/components/ImagePreview"
// 字典标签组件
import DictTag from '@/components/DictTag'

const app = createApp(App)

// 创建并挂载Pinia实例
const store = createPinia()
app.use(store)
app.use(router)

// 全局方法挂载
app.config.globalProperties.useDict = useDict;
app.config.globalProperties.download = download;
app.config.globalProperties.parseTime = parseTime;
app.config.globalProperties.resetForm = resetForm;
app.config.globalProperties.handleTree = handleTree;
app.config.globalProperties.addDateRange = addDateRange;
app.config.globalProperties.getConfigKey = getConfigKey;
app.config.globalProperties.selectDictLabel = selectDictLabel;
app.config.globalProperties.selectDictLabels = selectDictLabels;

// 全局组件挂载
app.component('DictTag', DictTag);
app.component('Pagination', Pagination);
app.component('FileUpload', FileUpload);
app.component('ImageUpload', ImageUpload);
app.component('ImagePreview', ImagePreview);
app.component('RightToolbar', RightToolbar);
app.component('Editor', Editor);
app.component('svg-icon', SvgIcon);

app.use(plugins);
app.use(elementIcons);

// 初始化设置store
const settingsStore = useSettingsStore();

directive(app)

// 使用element-plus 并且设置全局的大小
app.use(ElementPlus, {
  locale: locale,
  // 支持 large、default、small
  size: Cookies.get('size') || 'default'
})

app.mount('#app')

// 在开发环境下暴露Vue应用实例和调试功能
import { exposeVueApp } from './utils/menuDebug'
import { exposeCompatibilityTest } from './utils/compatibilityTest'

exposeVueApp(app)
exposeCompatibilityTest(app)

// 调试路由注册情况
import { debugRoutes } from './utils/routeDebug'
import { testBlogRoutes } from './utils/testBlogRoutes'
debugRoutes()

// 延迟测试博客动态路由匹配，确保路由完全注册后再测试
setTimeout(() => {
  try {
    console.log('开始测试博客路由...');
    testBlogRoutes();
  } catch (error) {
    console.error('博客路由测试出错:', error);
  }
}, 1000)

// 开发环境下执行loadView性能测试
if (import.meta.env.DEV) {
  import('./utils/loadViewPerformanceTest.js').then(({ runLoadViewPerformanceTest }) => {
    console.log('执行loadView性能测试...');
    setTimeout(() => {
      runLoadViewPerformanceTest().catch(err => {
        console.error('性能测试执行失败:', err);
      });
    }, 1000);
  });
}
