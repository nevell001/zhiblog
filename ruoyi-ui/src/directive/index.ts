import type { App, Directive } from 'vue'
import hasRole from './permission/hasRole'
import hasPermi from './permission/hasPermi'
import copyText from './common/copyText'

export default function directive(app: App): void {
  // 暂时禁用权限指令以排查问题
  // app.directive('hasRole', hasRole)
  // app.directive('hasPermi', hasPermi)
  app.directive('copyText', copyText)
}
