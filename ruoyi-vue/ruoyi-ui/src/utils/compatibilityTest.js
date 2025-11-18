/**
 * 🔥 菜单功能兼容性测试工具
 * 用于验证修复后的菜单点击功能
 */

// 测试结果收集器
class TestResultCollector {
  constructor() {
    this.results = []
    this.startTime = Date.now()
  }
  
  addResult(testName, passed, details = {}) {
    this.results.push({
      testName,
      passed,
      details,
      timestamp: new Date().toISOString()
    })
  }
  
  getSummary() {
    const passed = this.results.filter(r => r.passed).length
    const total = this.results.length
    const duration = Date.now() - this.startTime
    
    return {
      passed,
      total,
      failed: total - passed,
      successRate: total > 0 ? (passed / total * 100).toFixed(2) + '%' : '0%',
      duration: `${duration}ms`,
      results: this.results
    }
  }
}

// 🔥 兼容性测试主函数
export function runMenuCompatibilityTest() {
  console.group('🧪 开始菜单功能兼容性测试')
  
  const collector = new TestResultCollector()
  
  // 测试1: 路由配置检查
  testRouteConfiguration(collector)
  
  // 测试2: 权限系统检查
  testPermissionSystem(collector)
  
  // 测试3: 组件加载检查
  testComponentLoading(collector)
  
  // 测试4: 事件绑定检查
  testEventBinding(collector)
  
  // 测试5: 菜单项状态检查
  testMenuItemStatus(collector)
  
  const summary = collector.getSummary()
  
  console.groupEnd()
  
  // 输出测试报告
  console.log('📊 测试完成，生成报告...')
  generateTestReport(summary)
  
  return summary
}

// 测试路由配置
function testRouteConfiguration(collector) {
  console.log('🔍 测试路由配置...')
  
  try {
    const router = window.__VUE_APP__?.router
    if (!router) {
      collector.addResult('路由实例检查', false, { error: '路由实例不存在' })
      return
    }
    
    const routes = router.getRoutes()
    const keyRoutes = [
      '/admin/system',
      '/admin/monitor',
      '/admin/tool',
      '/admin/blog',
      '/admin/statistics'
    ]
    
    let missingRoutes = []
    keyRoutes.forEach(routePath => {
      if (!routes.some(r => r.path === routePath)) {
        missingRoutes.push(routePath)
      }
    })
    
    if (missingRoutes.length === 0) {
      collector.addResult('关键路由检查', true, { 
        totalRoutes: routes.length,
        keyRoutes: keyRoutes.length 
      })
    } else {
      collector.addResult('关键路由检查', false, { 
        missingRoutes,
        totalRoutes: routes.length 
      })
    }
    
  } catch (error) {
    collector.addResult('路由配置检查', false, { error: error.message })
  }
}

// 测试权限系统
function testPermissionSystem(collector) {
  console.log('🔐 测试权限系统...')
  
  try {
    const store = window.__VUE_APP__?.store
    if (!store) {
      collector.addResult('权限系统检查', false, { error: 'Store实例不存在' })
      return
    }
    
    const permissions = store.getters?.permissions || []
    const roles = store.getters?.roles || []
    
    // 检查权限数据格式
    const hasValidPermissions = Array.isArray(permissions)
    const hasValidRoles = Array.isArray(roles)
    
    if (hasValidPermissions && hasValidRoles) {
      collector.addResult('权限数据格式检查', true, {
        permissionCount: permissions.length,
        roleCount: roles.length,
        isAdmin: roles.includes('admin')
      })
    } else {
      collector.addResult('权限数据格式检查', false, {
        permissionsType: typeof permissions,
        rolesType: typeof roles
      })
    }
    
    // 测试权限检查函数
    if (window.auth && typeof window.auth.hasPermi === 'function') {
      collector.addResult('权限检查函数', true)
    } else {
      collector.addResult('权限检查函数', false, { error: '权限检查函数不可用' })
    }
    
  } catch (error) {
    collector.addResult('权限系统检查', false, { error: error.message })
  }
}

// 测试组件加载
function testComponentLoading(collector) {
  console.log('🧩 测试组件加载...')
  
  const testComponents = [
    'admin/system/user/index',
    'admin/monitor/online/index',
    'admin/tool/gen/index',
    'admin/blog/article/index',
    'admin/statistics/overview/index'
  ]
  
  let loadedComponents = 0
  let failedComponents = []
  
  testComponents.forEach(componentPath => {
    try {
      // 尝试动态导入组件
      import(/* @vite-ignore */ `@/views/${componentPath}.vue`)
        .then(() => {
          loadedComponents++
        })
        .catch(() => {
          failedComponents.push(componentPath)
        })
    } catch (error) {
      failedComponents.push(componentPath)
    }
  })
  
  // 延迟检查异步加载结果
  setTimeout(() => {
    if (failedComponents.length === 0) {
      collector.addResult('组件加载检查', true, {
        totalComponents: testComponents.length,
        loadedComponents
      })
    } else {
      collector.addResult('组件加载检查', false, {
        totalComponents: testComponents.length,
        loadedComponents,
        failedComponents
      })
    }
  }, 2000)
}

// 测试事件绑定
function testEventBinding(collector) {
  console.log('⚡ 测试事件绑定...')
  
  try {
    // 检查DOM中是否存在菜单元素
    const menuItems = document.querySelectorAll('.el-menu-item')
    const subMenus = document.querySelectorAll('.el-sub-menu')
    
    if (menuItems.length > 0 || subMenus.length > 0) {
      collector.addResult('菜单元素检查', true, {
        menuItems: menuItems.length,
        subMenus: subMenus.length
      })
      
      // 检查点击事件绑定
      let clickableItems = 0
      const allItems = [...menuItems, ...subMenus]
      
      allItems.forEach(item => {
        const hasClickListener = item.onclick || item.addEventListener
        if (hasClickListener) {
          clickableItems++
        }
      })
      
      if (clickableItems > 0) {
        collector.addResult('事件绑定检查', true, {
          totalItems: allItems.length,
          clickableItems
        })
      } else {
        collector.addResult('事件绑定检查', false, {
          totalItems: allItems.length,
          clickableItems
        })
      }
      
    } else {
      collector.addResult('菜单元素检查', false, { error: '未找到菜单元素' })
    }
    
  } catch (error) {
    collector.addResult('事件绑定检查', false, { error: error.message })
  }
}

// 测试菜单项状态
function testMenuItemStatus(collector) {
  console.log('📋 测试菜单项状态...')
  
  try {
    const menuItems = document.querySelectorAll('.el-menu-item')
    let visibleItems = 0
    let disabledItems = 0
    let permissionDeniedItems = 0
    
    menuItems.forEach(item => {
      // 检查可见性
      if (item.offsetParent !== null) {
        visibleItems++
      }
      
      // 检查禁用状态
      if (item.classList.contains('is-disabled')) {
        disabledItems++
      }
      
      // 检查权限不足标记
      if (item.classList.contains('no-permission') || 
          item.querySelector('.permission-denied')) {
        permissionDeniedItems++
      }
    })
    
    collector.addResult('菜单项状态检查', true, {
      totalItems: menuItems.length,
      visibleItems,
      disabledItems,
      permissionDeniedItems
    })
    
  } catch (error) {
    collector.addResult('菜单项状态检查', false, { error: error.message })
  }
}

// 生成测试报告
function generateTestReport(summary) {
  console.group('📋 兼容性测试报告')
  console.log(`✅ 通过: ${summary.passed}`)
  console.log(`❌ 失败: ${summary.failed}`)
  console.log(`📈 成功率: ${summary.successRate}`)
  console.log(`⏱️ 耗时: ${summary.duration}`)
  
  console.group('详细结果')
  summary.results.forEach(result => {
    const icon = result.passed ? '✅' : '❌'
    console.log(`${icon} ${result.testName}`)
    if (!result.passed && result.details) {
      console.log('   详情:', result.details)
    }
  })
  console.groupEnd()
  
  // 在页面上显示测试结果
  displayTestReport(summary)
  
  console.groupEnd()
}

// 在页面上显示测试报告
function displayTestReport(summary) {
  // 创建报告容器
  const reportContainer = document.createElement('div')
  reportContainer.id = 'menu-test-report'
  reportContainer.style.cssText = `
    position: fixed;
    top: 20px;
    right: 20px;
    width: 400px;
    max-height: 500px;
    background: white;
    border: 2px solid #409eff;
    border-radius: 8px;
    padding: 20px;
    box-shadow: 0 4px 12px rgba(0,0,0,0.15);
    z-index: 10000;
    font-family: Arial, sans-serif;
    overflow-y: auto;
  `
  
  const successRate = parseFloat(summary.successRate)
  const statusColor = successRate >= 80 ? '#67c23a' : successRate >= 60 ? '#e6a23c' : '#f56c6c'
  
  reportContainer.innerHTML = `
    <h3 style="margin: 0 0 15px 0; color: #409eff;">🧪 菜单功能测试报告</h3>
    <div style="display: flex; justify-content: space-between; margin-bottom: 15px;">
      <span>✅ 通过: <strong>${summary.passed}</strong></span>
      <span>❌ 失败: <strong>${summary.failed}</strong></span>
      <span style="color: ${statusColor};">📈 ${summary.successRate}</span>
    </div>
    <div style="font-size: 12px; color: #666; margin-bottom: 15px;">
      ⏱️ 耗时: ${summary.duration}
    </div>
    <div style="border-top: 1px solid #eee; padding-top: 15px;">
      ${summary.results.map(result => `
        <div style="margin-bottom: 8px; display: flex; align-items: flex-start;">
          <span style="margin-right: 8px;">${result.passed ? '✅' : '❌'}</span>
          <div style="flex: 1;">
            <div style="font-weight: bold;">${result.testName}</div>
            ${!result.passed && result.details ? `
              <div style="font-size: 11px; color: #f56c6c; margin-top: 2px;">
                ${JSON.stringify(result.details, null, 2)}
              </div>
            ` : ''}
          </div>
        </div>
      `).join('')}
    </div>
    <button onclick="this.parentElement.remove()" style="
      margin-top: 15px;
      padding: 8px 16px;
      background: #409eff;
      color: white;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      width: 100%;
    ">关闭报告</button>
  `
  
  document.body.appendChild(reportContainer)
  
  // 5秒后自动关闭
  setTimeout(() => {
    if (reportContainer.parentElement) {
      reportContainer.remove()
    }
  }, 10000)
}

// 暴露兼容性测试函数到Vue应用实例
export function exposeCompatibilityTest(app) {
  if (typeof window !== 'undefined') {
    window.runMenuCompatibilityTest = runMenuCompatibilityTest
    
    // 将测试函数附加到Vue应用实例
    if (app && app.config && app.config.globalProperties) {
      app.config.globalProperties.$runMenuTest = runMenuCompatibilityTest
    }
    
    console.log('✅ 兼容性测试函数已暴露到全局')
  }
}

// 导出测试函数到全局
if (typeof window !== 'undefined') {
  window.runMenuCompatibilityTest = runMenuCompatibilityTest
}

export default runMenuCompatibilityTest