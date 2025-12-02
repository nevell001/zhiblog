// 菜单展开功能测试脚本
// 在浏览器控制台运行此脚本来测试二级菜单展开功能

console.log('🚀 开始测试二级菜单展开功能...');

// 1. 检查Element Plus Menu组件的基本状态
function checkMenuComponentStatus() {
  const elMenu = document.querySelector('.el-menu');
  const elSubMenus = document.querySelectorAll('.el-sub-menu');

  console.log('\n📊 菜单组件状态检查:');
  console.log(`- el-menu容器: ${elMenu ? '✅ 存在' : '❌ 不存在'}`);
  console.log(`- el-sub-menu数量: ${elSubMenus.length}`);

  if (elMenu) {
    const uniqueOpened = elMenu.getAttribute('unique-opened');
    const collapse = elMenu.getAttribute('collapse');
    console.log(`- unique-opened: ${uniqueOpened}`);
    console.log(`- collapse: ${collapse}`);
  }

  return { elMenu, elSubMenus };
}

// 2. 测试子菜单展开/收起
function testSubmenuToggle() {
  console.log('\n🔄 测试子菜单展开/收起功能:');

  const elSubMenus = document.querySelectorAll('.el-sub-menu');
  if (elSubMenus.length === 0) {
    console.log('❌ 未找到子菜单项');
    return false;
  }

  elSubMenus.forEach((subMenu, index) => {
    const title = subMenu.querySelector('.el-sub-menu__title');
    const isInitiallyOpened = subMenu.classList.contains('is-opened');

    console.log(`\n子菜单 ${index + 1}:`);
    console.log(`- 标题文本: ${title ? title.textContent.trim() : '未知'}`);
    console.log(`- 初始状态: ${isInitiallyOpened ? '展开' : '收起'}`);

    if (title) {
      // 点击测试
      console.log(`- 执行点击操作...`);
      title.click();

      // 等待DOM更新
      setTimeout(() => {
        const isNowOpened = subMenu.classList.contains('is-opened');
        console.log(`- 点击后状态: ${isNowOpened ? '展开' : '收起'}`);
        console.log(`- 状态是否改变: ${isInitiallyOpened !== isNowOpened ? '✅ 是' : '❌ 否'}`);
      }, 100);
    }
  });

  return true;
}

// 3. 检查CSS样式是否正确应用
function checkMenuStyles() {
  console.log('\n🎨 检查菜单样式:');

  const subMenuTitles = document.querySelectorAll('.el-sub-menu__title');
  const menuItems = document.querySelectorAll('.el-menu-item');

  console.log(`- 子菜单标题数量: ${subMenuTitles.length}`);
  console.log(`- 菜单项数量: ${menuItems.length}`);

  // 检查pointer-events
  subMenuTitles.forEach((title, index) => {
    const computedStyle = window.getComputedStyle(title);
    const pointerEvents = computedStyle.getPropertyValue('pointer-events');
    const cursor = computedStyle.getPropertyValue('cursor');

    console.log(`\n子菜单标题 ${index + 1}:`);
    console.log(`- pointer-events: ${pointerEvents}`);
    console.log(`- cursor: ${cursor}`);
    console.log(`- text内容: "${title.textContent.trim()}"`);
  });

  return subMenuTitles.length > 0;
}

// 4. 测试路径解析功能
function testPathResolution() {
  console.log('\n🔧 测试路径解析:');

  // 模拟resolvePath函数调用
  const testCases = [
    { parent: '/admin/blog', child: 'article', expected: '/admin/blog/article' },
    { parent: '/admin', child: 'system', expected: '/admin/system' },
    { parent: '', child: '/admin/dashboard', expected: '/admin/dashboard' }
  ];

  testCases.forEach((testCase, index) => {
    console.log(`\n测试用例 ${index + 1}:`);
    console.log(`- parentPath: "${testCase.parent}"`);
    console.log(`- childPath: "${testCase.child}"`);
    console.log(`- 期望结果: "${testCase.expected}"`);
  });

  return true;
}

// 5. 检查事件监听器冲突
function checkEventListeners() {
  console.log('\n🎧 检查事件监听器:');

  const menuContainer = document.querySelector('.sidebar-container');
  const elMenu = document.querySelector('.el-menu');

  console.log('- 菜单容器:', menuContainer ? '✅ 存在' : '❌ 不存在');
  console.log('- el-menu:', elMenu ? '✅ 存在' : '❌ 不存在');

  // 检查是否有多余的事件监听器
  if (menuContainer && typeof getEventListeners === 'function') {
    const containerListeners = getEventListeners(menuContainer);
    const menuListeners = getEventListeners(elMenu);

    console.log('- 容器事件监听器:', containerListeners ? Object.keys(containerListeners) : '无法检查');
    console.log('- el-menu事件监听器:', menuListeners ? Object.keys(menuListeners) : '无法检查');
  }

  return menuContainer && elMenu;
}

// 6. 提供手动测试指导
function provideManualTestGuide() {
  console.log('\n📋 手动测试指导:');
  console.log('请手动执行以下步骤来验证二级菜单功能:');
  console.log('\n1. 点击任意有子菜单的项目');
  console.log('2. 观察子菜单是否展开');
  console.log('3. 再次点击相同项目');
  console.log('4. 观察子菜单是否收起');
  console.log('5. 点击其他子菜单项目');
  console.log('6. 观察是否能同时展开多个子菜单');
  console.log('7. 在展开状态下点击子菜单项');
  console.log('8. 观察是否能正确跳转');

  console.log('\n预期行为:');
  console.log('✅ 点击子菜单标题应能展开/收起子菜单');
  console.log('✅ 应该能同时展开多个子菜单 (unique-opened=false)');
  console.log('✅ 点击子菜单项应能正确跳转');
  console.log('✅ 子菜单应该有适当的缩进显示');
}

// 执行所有测试
function runAllTests() {
  console.log('🔍 开始全面诊断...');

  const statusChecks = checkMenuComponentStatus();
  const submenuTest = testSubmenuToggle();
  const styleChecks = checkMenuStyles();
  const pathTest = testPathResolution();
  const eventChecks = checkEventListeners();

  provideManualTestGuide();

  console.log('\n📊 测试总结:');
  console.log(`- 组件状态: ${statusChecks.elMenu ? '✅' : '❌'}`);
  console.log(`- 子菜单功能: ${submenuTest ? '✅' : '❌'}`);
  console.log(`- 样式应用: ${styleChecks ? '✅' : '❌'}`);
  console.log(`- 路径解析: ${pathTest ? '✅' : '❌'}`);
  console.log(`- 事件检查: ${eventChecks ? '✅' : '❌'}`);

  console.log('\n✨ 测试完成！如果发现问题，请查看上述具体错误信息。');
}

// 自动运行测试
runAllTests();

// 也可以单独调用特定测试
window.testMenu = {
  checkComponents: checkMenuComponentStatus,
  testSubmenu: testSubmenuToggle,
  checkStyles: checkMenuStyles,
  testPaths: testPathResolution,
  checkEvents: checkEventListeners,
  runAll: runAllTests
};

console.log('\n💡 您也可以调用 window.testMenu.runAll() 重新运行测试');