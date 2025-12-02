// 菜单展开功能修复验证脚本
window.testMenuFix = {
  // 检查Element Plus菜单组件配置
  checkMenuConfig() {
    const menu = document.querySelector('.sidebar-container .el-menu');
    if (!menu) {
      console.error('❌ 未找到菜单容器');
      return false;
    }

    console.log('✅ 菜单容器找到');

    // 检查关键配置
    const config = {
      router: menu._component?.router,
      uniqueOpened: menu._component?.uniqueOpened,
      collapseTransition: menu._component?.collapseTransition
    };

    console.log('📋 菜单配置:', config);

    const isValid = config.router === true && config.uniqueOpened === false && config.collapseTransition === false;

    if (isValid) {
      console.log('✅ 菜单配置正确');
    } else {
      console.error('❌ 菜单配置有问题:', config);
    }

    return isValid;
  },

  // 检查子菜单组件
  checkSubMenus() {
    const subMenus = document.querySelectorAll('.el-sub-menu');
    console.log(`📊 找到 ${subMenus.length} 个子菜单`);

    subMenus.forEach((subMenu, index) => {
      const title = subMenu.querySelector('.el-sub-menu__title');
      const indexAttr = subMenu.getAttribute('index');
      const hasChildren = subMenu.querySelector('.el-menu');

      console.log(`子菜单 ${index + 1}:`, {
        title: title?.textContent?.trim(),
        index: indexAttr,
        hasChildren: !!hasChildren,
        classes: subMenu.className
      });
    });

    return subMenus.length > 0;
  },

  // 模拟点击子菜单标题
  testSubMenuClick() {
    const subMenuTitles = document.querySelectorAll('.el-sub-menu__title');

    if (subMenuTitles.length === 0) {
      console.warn('⚠️ 未找到可点击的子菜单标题');
      return false;
    }

    console.log(`🖱️ 找到 ${subMenuTitles.length} 个可点击的子菜单标题`);

    // 测试第一个子菜单的展开/收起
    const firstTitle = subMenuTitles[0];
    const firstSubMenu = firstTitle.closest('.el-sub-menu');

    if (firstSubMenu) {
      const wasOpened = firstSubMenu.classList.contains('is-opened');
      console.log(`📌 第一个子菜单当前状态: ${wasOpened ? '展开' : '收起'}`);

      // 模拟点击
      firstTitle.click();

      // 检查状态变化
      setTimeout(() => {
        const isOpened = firstSubMenu.classList.contains('is-opened');
        console.log(`📌 点击后状态: ${isOpened ? '展开' : '收起'}`);

        if (wasOpened !== isOpened) {
          console.log('✅ 子菜单展开/收起功能正常');
        } else {
          console.error('❌ 子菜单展开/收起功能异常');
        }
      }, 100);

      return true;
    }

    return false;
  },

  // 检查菜单项的路由配置
  checkMenuItemsRouting() {
    const menuItems = document.querySelectorAll('.el-menu-item');
    console.log(`📊 找到 ${menuItems.length} 个菜单项`);

    let validItems = 0;
    menuItems.forEach((item, index) => {
      const indexAttr = item.getAttribute('index');
      const text = item.textContent?.trim();

      if (indexAttr && indexAttr.startsWith('/')) {
        validItems++;
        console.log(`菜单项 ${index + 1}: "${text}" -> ${indexAttr}`);
      } else {
        console.warn(`⚠️ 菜单项 ${index + 1}: "${text}" 缺少有效路径 (${indexAttr})`);
      }
    });

    console.log(`✅ ${validItems}/${menuItems.length} 个菜单项配置正确`);
    return validItems > 0;
  },

  // 检查CSS样式
  checkStyles() {
    const styles = [
      '.el-sub-menu__title',
      '.el-sub-menu .el-menu',
      '.nest-menu .el-menu-item'
    ];

    let allGood = true;
    styles.forEach(selector => {
      const elements = document.querySelectorAll(selector);
      if (elements.length > 0) {
        console.log(`✅ 样式选择器 ${selector} 匹配 ${elements.length} 个元素`);
      } else {
        console.warn(`⚠️ 样式选择器 ${selector} 未匹配到元素`);
        allGood = false;
      }
    });

    return allGood;
  },

  // 运行所有测试
  runAll() {
    console.log('🧪 开始菜单展开功能修复验证...\n');

    const results = {
      menuConfig: this.checkMenuConfig(),
      subMenus: this.checkSubMenus(),
      routing: this.checkMenuItemsRouting(),
      styles: this.checkStyles()
    };

    console.log('\n📋 测试结果汇总:');
    Object.entries(results).forEach(([key, value]) => {
      const status = value ? '✅ 通过' : '❌ 失败';
      console.log(`${key}: ${status}`);
    });

    const allPassed = Object.values(results).every(result => result);
    console.log(`\n🎯 总体结果: ${allPassed ? '✅ 所有测试通过' : '❌ 存在问题需要修复'}`);

    // 测试交互功能
    setTimeout(() => {
      console.log('\n🖱️ 测试交互功能...');
      this.testSubMenuClick();
    }, 1000);

    return allPassed;
  }
};

console.log('🚀 菜单展开功能测试脚本已加载');
console.log('💡 使用 window.testMenuFix.runAll() 运行所有测试');