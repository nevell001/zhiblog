// 菜单调试工具函数

/**
 * 调试菜单元素的z-index层级和交互属性
 */
export function debugMenuElements() {
  console.log('开始调试菜单元素...');
  
  // 查找所有菜单相关的元素
  const menuItems = document.querySelectorAll('.el-menu-item, .el-sub-menu');
  const menuTitles = document.querySelectorAll('.menu-title');
  const spanElements = document.querySelectorAll('.menu-title span');
  
  console.log(`找到 ${menuItems.length} 个菜单项`);
  console.log(`找到 ${menuTitles.length} 个菜单标题`);
  console.log(`找到 ${spanElements.length} 个菜单内span元素`);
  
  // 检查每个菜单项的z-index和可点击性
  menuItems.forEach((item, index) => {
    const computedStyle = window.getComputedStyle(item);
    const zIndex = computedStyle.getPropertyValue('z-index');
    const pointerEvents = computedStyle.getPropertyValue('pointer-events');
    const position = computedStyle.getPropertyValue('position');
    const display = computedStyle.getPropertyValue('display');
    const visibility = computedStyle.getPropertyValue('visibility');
    
    console.log(`\n菜单项 ${index + 1}:`);
    console.log(`  文本内容: ${item.textContent.trim()}`);
    console.log(`  z-index: ${zIndex}`);
    console.log(`  pointer-events: ${pointerEvents}`);
    console.log(`  position: ${position}`);
    console.log(`  display: ${display}`);
    console.log(`  visibility: ${visibility}`);
    
    // 检查是否有重叠的元素
    const rect = item.getBoundingClientRect();
    console.log(`  位置: ${rect.left}, ${rect.top}, ${rect.width}, ${rect.height}`);
    
    // 检查事件监听器
    const listeners = getEventListeners(item);
    console.log(`  事件监听器数量: ${Object.keys(listeners).length}`);
    Object.keys(listeners).forEach(eventType => {
      console.log(`    ${eventType}: ${listeners[eventType].length} 个`);
    });
    
    // 手动触发点击事件测试
    item.clickTest = function() {
      console.log(`手动触发菜单项 ${index + 1} 点击事件`);
      const event = new MouseEvent('click', {
        bubbles: true,
        cancelable: true,
        view: window
      });
      this.dispatchEvent(event);
    };
  });
  
  // 专门检查所有span元素的属性
  spanElements.forEach((span, index) => {
    const computedStyle = window.getComputedStyle(span);
    const zIndex = computedStyle.getPropertyValue('z-index');
    const pointerEvents = computedStyle.getPropertyValue('pointer-events');
    const position = computedStyle.getPropertyValue('position');
    const cursor = computedStyle.getPropertyValue('cursor');
    
    console.log(`\n菜单span元素 ${index + 1}:`);
    console.log(`  文本内容: ${span.textContent.trim()}`);
    console.log(`  z-index: ${zIndex}`);
    console.log(`  pointer-events: ${pointerEvents}`);
    console.log(`  position: ${position}`);
    console.log(`  cursor: ${cursor}`);
    
    // 检查父元素
    const parent = span.parentElement;
    if (parent) {
      console.log(`  父元素: ${parent.tagName.toLowerCase()}`);
    }
    
    // 手动触发点击事件测试
    span.clickTest = function() {
      console.log(`手动触发菜单span ${index + 1} 点击事件`);
      const event = new MouseEvent('click', {
        bubbles: true,
        cancelable: true,
        view: window
      });
      this.dispatchEvent(event);
    };
    
    // 临时添加直接的点击事件
    span.addEventListener('click', function(e) {
      console.log(`菜单span ${index + 1} 直接点击事件被触发`);
      // 确保事件能够传播
      console.log('事件冒泡状态:', !e.propagationStopped);
    }, false);
  });
  
  console.log('\n调试完成。可以在控制台使用以下方法手动测试菜单项点击:');
  console.log('menuItems[0].clickTest()');
  console.log('spanElements[0].clickTest()');
}

/**
 * 获取元素的事件监听器（浏览器控制台API）
 * @param {HTMLElement} element 要检查的元素
 * @returns {Object} 事件监听器对象
 */
function getEventListeners(element) {
  // 这是浏览器控制台API的模拟实现
  // 在实际浏览器控制台中，getEventListeners是原生API
  return {}; // 返回空对象，实际环境中会由浏览器API提供
}

/**
 * 添加额外的菜单事件监听器进行调试
 */
export function addMenuEventListeners() {
  console.log('开始添加菜单事件监听器...');
  
  // 为整个菜单容器添加事件监听
  const menuContainer = document.querySelector('.sidebar-container');
  if (menuContainer) {
    console.log('为菜单容器添加事件监听器');
    
    // 捕获阶段的点击事件
    menuContainer.addEventListener('click', (e) => {
      console.log('捕获阶段 - 菜单容器点击事件:', e.target);
      console.log('目标元素层级:', getElementHierarchy(e.target));
    }, true);
    
    // 冒泡阶段的点击事件
    menuContainer.addEventListener('click', (e) => {
      console.log('冒泡阶段 - 菜单容器点击事件:', e.target);
    }, false);
  }
  
  // 为每个菜单项添加事件监听
  const menuItems = document.querySelectorAll('.el-menu-item, .el-sub-menu');
  menuItems.forEach((item, index) => {
    // 捕获阶段的点击事件
    item.addEventListener('click', (e) => {
      console.log(`捕获阶段 - 菜单项 ${index + 1} 点击事件:`, e.target);
    }, true);
    
    // 冒泡阶段的点击事件
    item.addEventListener('click', (e) => {
      console.log(`冒泡阶段 - 菜单项 ${index + 1} 点击事件:`, e.target);
      // 尝试获取路由
      const route = item.getAttribute('index');
      if (route) {
        console.log(`菜单项路由: ${route}`);
        // 尝试使用备选导航方案
        tryAlternativeNavigation(route);
      }
    }, false);
  });
  
  // 为每个span元素直接添加点击事件
  const spanElements = document.querySelectorAll('.menu-title span, .el-menu-item span');
  spanElements.forEach((span, index) => {
    // 捕获阶段的点击事件
    span.addEventListener('click', (e) => {
      console.log(`捕获阶段 - 菜单span ${index + 1} 点击事件:`, e.target);
    }, true);
    
    // 冒泡阶段的点击事件 - 添加直接导航
    span.addEventListener('click', (e) => {
      console.log(`冒泡阶段 - 菜单span ${index + 1} 点击事件:`, e.target);
      
      // 尝试从最近的菜单项获取路由
      const menuItem = e.target.closest('.el-menu-item, .el-sub-menu');
      if (menuItem) {
        const route = menuItem.getAttribute('index');
        if (route) {
          console.log(`从span点击获取路由: ${route}`);
          // 尝试使用备选导航方案
          tryAlternativeNavigation(route);
        }
      }
    }, false);
  });
  
  console.log('\n事件监听器添加完成。可以在控制台查看事件触发情况。');
}

/**
 * 获取元素的DOM层级路径
 * @param {HTMLElement} element 要检查的元素
 * @returns {string} DOM层级路径
 */
function getElementHierarchy(element) {
  const path = [];
  let current = element;
  
  while (current && current.tagName) {
    const tag = current.tagName.toLowerCase();
    const id = current.id ? `#${current.id}` : '';
    const classes = Array.from(current.classList).join('.');
    const classStr = classes ? `.${classes}` : '';
    
    path.unshift(`${tag}${id}${classStr}`);
    current = current.parentElement;
  }
  
  return path.join(' > ');
}

/**
 * 尝试使用备选导航方案
 * @param {string} route 路由路径
 */
function tryAlternativeNavigation(route) {
  // 检查路由是否有效
  if (!route || route.trim() === '') return;
  
  console.log(`尝试备选导航方案到: ${route}`);
  
  // 方案1: 使用Vue Router
  if (window.$router && typeof window.$router.push === 'function') {
    try {
      console.log('方案1: 使用Vue Router');
      window.$router.push(route).then(() => {
        console.log('Vue Router导航成功');
      }).catch(error => {
        console.log('Vue Router导航失败，尝试其他方案:', error);
        tryDirectNavigation(route);
      });
      return;
    } catch (e) {
      console.log('Vue Router执行失败:', e);
    }
  }
  
  // 方案2: 直接修改location
  tryDirectNavigation(route);
}

/**
 * 直接修改location进行导航
 * @param {string} route 路由路径
 */
function tryDirectNavigation(route) {
  console.log('方案2: 使用window.location.href');
  try {
    // 确保路由格式正确
    let url = route;
    if (!url.startsWith('/')) {
      url = '/' + url;
    }
    
    console.log(`尝试直接导航到: ${url}`);
    // 注意：这里只输出日志，不实际执行导航
    console.log('在实际环境中，这里会执行: window.location.href = url');
  } catch (e) {
    console.error('直接导航失败:', e);
  }
}

/**
 * 暴露Vue应用实例到window对象，方便在控制台调试
 * @param {Object} app Vue应用实例
 */
export function exposeVueApp(app) {
  // 支持生产环境也可以调试
  window.$vueApp = app;
  console.log('Vue应用实例已暴露到window.$vueApp');
  
  // 暴露路由对象
  if (app.config && app.config.globalProperties && app.config.globalProperties.$router) {
    window.$router = app.config.globalProperties.$router;
    console.log('路由对象已暴露到window.$router');
  }
  
  // 暴露调试方法
  window.debugMenu = function() {
    debugMenuElements();
    addMenuEventListeners();
  };
  
  window.testMenuNavigation = function(index) {
    const menuItems = document.querySelectorAll('.el-menu-item, .el-sub-menu');
    if (menuItems[index]) {
      const route = menuItems[index].getAttribute('index');
      if (route) {
        tryAlternativeNavigation(route);
      }
    }
  };
  
  console.log('\n调试工具已就绪:');
  console.log('1. window.$vueApp - Vue应用实例');
  console.log('2. window.$router - 路由对象');
  console.log('3. window.debugMenu() - 重新运行菜单调试');
  console.log('4. window.testMenuNavigation(index) - 测试特定菜单的导航功能');
}