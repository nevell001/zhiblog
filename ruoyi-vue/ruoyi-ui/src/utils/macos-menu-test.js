// 🍎 macOS 菜单功能测试脚本
// 在浏览器控制台中运行此脚本来测试菜单功能

console.log('🚀 开始 macOS 菜单功能测试...');

// 检查 Vue 应用是否已加载
function checkVueApp() {
  if (window.Vue && window.__VUE_APP__) {
    console.log('✅ Vue 应用已加载');
    return true;
  } else {
    console.warn('⚠️ Vue 应用未完全加载，请稍后再试');
    return false;
  }
}

// 检查路由配置
function checkRoutes() {
  console.group('🔍 检查路由配置');
  
  const router = window.__VUE_APP__?.router;
  if (!router) {
    console.error('❌ 路由实例未找到');
    return false;
  }
  
  const routes = router.getRoutes();
  console.log('📊 总路由数量:', routes.length);
  
  // 检查关键路由
  const keyRoutes = [
    '/admin/system',
    '/admin/monitor', 
    '/admin/tool',
    '/admin/blog',
    '/admin/statistics'
  ];
  
  let foundRoutes = 0;
  keyRoutes.forEach(routePath => {
    const exists = routes.some(r => r.path === routePath);
    console.log(`${exists ? '✅' : '❌'} ${routePath}: ${exists ? '存在' : '缺失'}`);
    if (exists) foundRoutes++;
  });
  
  console.log(`📈 路由完整性: ${foundRoutes}/${keyRoutes.length} (${Math.round(foundRoutes/keyRoutes.length*100)}%)`);
  console.groupEnd();
  
  return foundRoutes === keyRoutes.length;
}

// 检查用户权限
function checkPermissions() {
  console.group('🔐 检查用户权限');
  
  const store = window.__VUE_APP__?.store;
  if (!store) {
    console.error('❌ Store 实例未找到');
    return false;
  }
  
  const userState = store.state.user;
  if (!userState) {
    console.error('❌ 用户状态未找到');
    return false;
  }
  
  console.log('👤 用户信息:', {
    token: userState.token ? '✅ 已登录' : '❌ 未登录',
    roles: userState.roles || [],
    permissions: userState.permissions || []
  });
  
  // 检查关键权限
  const keyPermissions = [
    'system:user:list',
    'monitor:online:list', 
    'tool:gen:list',
    'admin',
    'editor'
  ];
  
  const userPermissions = userState.permissions || [];
  let foundPermissions = 0;
  
  keyPermissions.forEach(perm => {
    const hasPermission = userPermissions.includes(perm);
    console.log(`${hasPermission ? '✅' : '❌'} ${perm}: ${hasPermission ? '有权限' : '无权限'}`);
    if (hasPermission) foundPermissions++;
  });
  
  console.log(`📈 权限覆盖率: ${foundPermissions}/${keyPermissions.length} (${Math.round(foundPermissions/keyPermissions.length*100)}%)`);
  console.groupEnd();
  
  return foundPermissions > 0;
}

// 模拟菜单点击测试
function testMenuClicks() {
  console.group('🖱️ 测试菜单点击');
  
  const menuItems = document.querySelectorAll('.el-menu-item');
  console.log(`📊 找到菜单项数量: ${menuItems.length}`);
  
  // 查找关键菜单项
  const keyMenus = [
    '系统管理',
    '系统监控',
    '系统工具', 
    '博客管理',
    '数据统计'
  ];
  
  let foundMenus = 0;
  let clickableMenus = 0;
  
  keyMenus.forEach(menuName => {
    const menuItem = Array.from(menuItems).find(item => 
      item.textContent?.includes(menuName)
    );
    
    if (menuItem) {
      foundMenus++;
      const isVisible = menuItem.offsetParent !== null;
      const isEnabled = !menuItem.classList.contains('is-disabled');
      
      console.log(`${isVisible ? '✅' : '❌'} ${menuName}: ${isVisible ? '可见' : '隐藏'} | ${isEnabled ? '可用' : '禁用'}`);
      
      if (isVisible && isEnabled) {
        clickableMenus++;
        
        // 模拟点击
        console.log(`🖱️ 模拟点击: ${menuName}`);
        menuItem.click();
        
        // 检查是否有响应
        setTimeout(() => {
          const hasResponse = menuItem.classList.contains('is-active') || 
                             window.location.hash.includes(menuName);
          console.log(`${hasResponse ? '✅' : '❌'} ${menuName} 点击响应: ${hasResponse ? '正常' : '无响应'}`);
        }, 500);
      }
    } else {
      console.log(`❌ ${menuName}: 未找到`);
    }
  });
  
  console.log(`📈 菜单可用性: ${clickableMenus}/${foundMenus} (${foundMenus > 0 ? Math.round(clickableMenus/foundMenus*100) : 0}%)`);
  console.groupEnd();
  
  return clickableMenus > 0;
}

// 检查控制台错误
function checkConsoleErrors() {
  console.group('🐛 检查控制台错误');
  
  const originalError = console.error;
  const errors = [];
  
  // 临时捕获错误
  console.error = function(...args) {
    errors.push(args);
    originalError.apply(console, args);
  };
  
  setTimeout(() => {
    console.error = originalError; // 恢复原始方法
    
    if (errors.length > 0) {
      console.warn(`⚠️ 捕获到 ${errors.length} 个错误:`);
      errors.forEach((error, index) => {
        console.log(`${index + 1}.`, ...error);
      });
    } else {
      console.log('✅ 未发现控制台错误');
    }
    
    console.groupEnd();
  }, 1000);
}

// 主测试函数
function runMacOSMenuTest() {
  console.log('='.repeat(50));
  console.log('🍎 macOS 菜单系统诊断测试');
  console.log('='.repeat(50));
  
  const results = {
    vueApp: checkVueApp(),
    routes: checkRoutes(),
    permissions: checkPermissions(),
    menuClicks: testMenuClicks()
  };
  
  setTimeout(() => {
    checkConsoleErrors();
    
    // 生成测试报告
    console.group('📋 测试报告');
    const passedTests = Object.values(results).filter(Boolean).length;
    const totalTests = Object.keys(results).length;
    const successRate = Math.round(passedTests / totalTests * 100);
    
    console.log(`📊 测试通过率: ${passedTests}/${totalTests} (${successRate}%)`);
    
    if (successRate === 100) {
      console.log('🎉 所有测试通过！菜单系统应该正常工作。');
    } else if (successRate >= 75) {
      console.log('⚠️ 大部分测试通过，可能存在轻微问题。');
    } else {
      console.log('❌ 多个测试失败，需要进一步检查。');
    }
    
    console.log('🔧 建议:');
    if (!results.routes) console.log('- 检查路由配置文件');
    if (!results.permissions) console.log('- 检查用户权限设置');
    if (!results.menuClicks) console.log('- 检查菜单组件和事件绑定');
    
    console.groupEnd();
    console.log('='.repeat(50));
  }, 2000);
}

// 自动运行测试
if (document.readyState === 'complete') {
  runMacOSMenuTest();
} else {
  window.addEventListener('load', runMacOSMenuTest);
}

// 导出到全局，方便手动调用
window.runMacOSMenuTest = runMacOSMenuTest;

console.log('✅ macOS 菜单测试脚本已加载，运行 window.runMacOSMenuTest() 开始测试');