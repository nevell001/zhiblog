// 🧪 路由修复测试脚本
// 在浏览器控制台中运行此脚本来验证路由修复效果

console.log('🔍 开始测试路由修复效果...');

// 测试路由配置
function testRouteConfiguration() {
  console.group('📋 测试路由配置');
  
  // 检查路由实例
  if (window.__VUE_APP__?.router) {
    const routes = window.__VUE_APP__.router.getRoutes();
    console.log('✅ 路由实例存在，总路由数:', routes.length);
    
    // 检查关键路由是否存在
    const keyRoutes = [
      '/admin/system',
      '/admin/monitor',
      '/admin/tool', 
      '/admin/blog',
      '/admin/statistics'
    ];
    
    let foundCount = 0;
    const routeDetails = [];
    
    keyRoutes.forEach(routePath => {
      const route = routes.find(r => r.path === routePath);
      if (route) {
        foundCount++;
        routeDetails.push({
          path: route.path,
          name: route.name,
          hasChildren: !!(route.children && route.children.length),
          childrenCount: route.children ? route.children.length : 0
        });
        console.log(`✅ ${routePath}: 存在 (${route.children?.length || 0} 个子路由)`);
      } else {
        console.log(`❌ ${routePath}: 缺失`);
      }
    });
    
    console.log(`📊 路由完整性: ${foundCount}/${keyRoutes.length} (${Math.round(foundCount/keyRoutes.length*100)}%)`);
    
    // 详细路由信息
    if (routeDetails.length > 0) {
      console.group('📄 详细路由信息');
      routeDetails.forEach(detail => {
        console.log(`${detail.path}:`, detail);
      });
      console.groupEnd();
    }
    
  } else {
    console.error('❌ 路由实例不存在');
  }
  
  console.groupEnd();
}

// 测试组件加载
function testComponentLoading() {
  console.group('🧩 测试组件加载');
  
  // 测试关键组件路径
  const testComponents = [
    'admin/system/user/user/index',
    'admin/system/role/role/index', 
    'admin/monitor/online/index',
    'admin/tool/gen/index',
    'admin/blog/article/article/index',
    'admin/statistics/overview/index'
  ];
  
  let successCount = 0;
  const results = [];
  
  testComponents.forEach(async (componentPath, index) => {
    try {
      console.log(`🔍 测试组件 ${index + 1}: ${componentPath}`);
      
      // 模拟 loadView 函数的路径尝试
      const paths = [
        `@/views/${componentPath}.vue`,
        `@/views/${componentPath}/index.vue`,
        `@/views/admin/${componentPath.replace('admin/', '')}.vue`,
        `@/views/admin/${componentPath.replace('admin/', '')}/index.vue`
      ];
      
      let loaded = false;
      let loadedPath = '';
      
      for (const path of paths) {
        try {
          const module = await import(/* @vite-ignore */ path);
          loaded = true;
          loadedPath = path;
          successCount++;
          break;
        } catch (e) {
          // 继续尝试下一个路径
        }
      }
      
      results.push({
        component: componentPath,
        loaded,
        path: loadedPath
      });
      
      if (loaded) {
        console.log(`✅ ${componentPath} -> ${loadedPath}`);
      } else {
        console.log(`❌ ${componentPath} -> 所有路径都失败`);
      }
      
    } catch (error) {
      console.error(`❌ ${componentPath} 测试失败:`, error.message);
      results.push({
        component: componentPath,
        loaded: false,
        error: error.message
      });
    }
    
    // 所有测试完成后输出总结
    if (results.length === testComponents.length) {
      setTimeout(() => {
        console.log(`📊 组件加载成功率: ${successCount}/${testComponents.length} (${Math.round(successCount/testComponents.length*100)}%)`);
        console.groupEnd();
      }, 1000);
    }
  });
}

// 测试菜单点击
function testMenuClick() {
  console.group('🖱️ 测试菜单点击');
  
  // 查找菜单项
  const menuItems = document.querySelectorAll('.el-menu-item');
  const subMenus = document.querySelectorAll('.el-sub-menu');
  
  console.log(`📊 找到菜单项: ${menuItems.length} 个，子菜单: ${subMenus.length} 个`);
  
  // 查找关键菜单
  const keyMenus = ['系统管理', '系统监控', '系统工具', '博客管理', '数据统计'];
  let foundMenus = 0;
  
  keyMenus.forEach(menuName => {
    const elements = Array.from(menuItems).concat(Array.from(subMenus));
    const menuElement = elements.find(el => 
      el.textContent && el.textContent.includes(menuName)
    );
    
    if (menuElement) {
      foundMenus++;
      const isVisible = menuElement.offsetParent !== null;
      const isEnabled = !menuElement.classList.contains('is-disabled');
      const hasPermission = !menuElement.classList.contains('no-permission');
      
      console.log(`${isVisible ? '✅' : '❌'} ${menuName}: ${isVisible ? '可见' : '隐藏'} | ${isEnabled ? '可用' : '禁用'} | ${hasPermission ? '有权限' : '无权限'}`);
      
      // 如果菜单可见且可用，模拟点击测试
      if (isVisible && isEnabled) {
        setTimeout(() => {
          console.log(`🖱️ 模拟点击: ${menuName}`);
          menuElement.click();
          
          // 检查点击后的反应
          setTimeout(() => {
            const hasError = document.querySelector('.el-message--error');
            const hasWarning = document.querySelector('.el-message--warning');
            const hasSuccess = document.querySelector('.el-message--success');
            
            if (hasError) {
              console.log(`❌ ${menuName} 点击后出现错误`);
            } else if (hasWarning) {
              console.log(`⚠️ ${menuName} 点击后出现警告`);
            } else if (hasSuccess) {
              console.log(`✅ ${menuName} 点击成功`);
            } else {
              console.log(`ℹ️ ${menuName} 点击后无明显反馈`);
            }
          }, 500);
        }, foundMenus * 200);
      }
    } else {
      console.log(`❌ ${menuName}: 未找到`);
    }
  });
  
  console.log(`📊 菜单发现率: ${foundMenus}/${keyMenus.length} (${Math.round(foundMenus/keyMenus.length*100)}%)`);
  console.groupEnd();
}

// 主测试函数
function runRouteFixTest() {
  console.log('='.repeat(60));
  console.log('🧪 路由修复效果测试');
  console.log('='.repeat(60));
  
  testRouteConfiguration();
  
  setTimeout(() => {
    testComponentLoading();
  }, 1000);
  
  setTimeout(() => {
    testMenuClick();
  }, 3000);
  
  setTimeout(() => {
    console.log('='.repeat(60));
    console.log('🎉 测试完成！请查看上述结果');
    console.log('💡 如果仍有问题，请检查控制台错误信息');
    console.log('='.repeat(60));
  }, 5000);
}

// 自动运行测试
runRouteFixTest();

// 导出到全局，方便手动调用
window.runRouteFixTest = runRouteFixTest;

console.log('✅ 路由修复测试脚本已加载');
console.log('💡 手动运行测试: window.runRouteFixTest()');