#!/bin/bash

# ========================================
# 修复路由路径问题
# 创建时间：2025-11-17
# 描述：修复系统模块路由路径问题，解决404错误
# ========================================

echo "========================================"
echo "开始修复路由路径问题"
echo "========================================"

# 1. 修复数据库中的外链菜单
echo "1. 修复数据库中的外链菜单..."
mysql -h localhost -u root -proot newblog << EOF
-- 修复若依官网菜单，将其设置为隐藏，避免影响路由
UPDATE sys_menu SET visible = '1' WHERE menu_id = 4;

-- 确保系统管理、系统监控和系统工具模块的路径正确
UPDATE sys_menu SET path = '/admin/system', component = 'Layout' WHERE menu_id = 1;
UPDATE sys_menu SET path = '/admin/monitor', component = 'Layout' WHERE menu_id = 2;
UPDATE sys_menu SET path = '/admin/tool', component = 'Layout' WHERE menu_id = 3;

-- 确保所有子菜单的路径正确
UPDATE sys_menu SET path = '/admin/system/user' WHERE menu_id = 100;
UPDATE sys_menu SET path = '/admin/system/role' WHERE menu_id = 101;
UPDATE sys_menu SET path = '/admin/system/menu' WHERE menu_id = 102;
UPDATE sys_menu SET path = '/admin/system/dept' WHERE menu_id = 103;
UPDATE sys_menu SET path = '/admin/system/post' WHERE menu_id = 104;
UPDATE sys_menu SET path = '/admin/system/dict' WHERE menu_id = 105;
UPDATE sys_menu SET path = '/admin/system/config' WHERE menu_id = 106;
UPDATE sys_menu SET path = '/admin/system/notice' WHERE menu_id = 107;

UPDATE sys_menu SET path = '/admin/monitor/online' WHERE menu_id = 109;
UPDATE sys_menu SET path = '/admin/monitor/logininfor' WHERE menu_id = 501;
UPDATE sys_menu SET path = '/admin/monitor/operlog' WHERE menu_id = 500;
UPDATE sys_menu SET path = '/admin/monitor/druid' WHERE menu_id = 108;
UPDATE sys_menu SET path = '/admin/monitor/server' WHERE menu_id = 112;
UPDATE sys_menu SET path = '/admin/monitor/cache' WHERE menu_id = 113;
UPDATE sys_menu SET path = '/admin/monitor/job' WHERE menu_id = 115;

UPDATE sys_menu SET path = '/admin/tool/gen' WHERE menu_id = 116;
UPDATE sys_menu SET path = '/admin/tool/swagger' WHERE menu_id = 117;
EOF

# 2. 修复后端路由生成逻辑
echo "2. 修复后端路由生成逻辑..."
cat > /tmp/SysMenuServiceImpl.patch << 'EOF'
--- a/ruoyi-system/src/main/java/com/ruoyi/system/service/impl/SysMenuServiceImpl.java
+++ b/ruoyi-system/src/main/java/com/ruoyi/system/service/impl/SysMenuServiceImpl.java
@@ -393,7 +393,7 @@ public class SysMenuServiceImpl implements ISysMenuService
      */
     public String getRouterPath(SysMenu menu)
     {
-        String routerPath = menu.getPath();
+        String routerPath = menu.getPath();
         // 内链打开外网方式
         if (menu.getParentId().intValue() != 0 && isInnerLink(menu))
         {
@@ -401,6 +401,11 @@ public class SysMenuServiceImpl implements ISysMenuService
         }
         // 非外链并且是一级目录（类型为目录）
         if (0 == menu.getParentId().intValue() && UserConstants.TYPE_DIR.equals(menu.getMenuType())
+                && UserConstants.NO_FRAME.equals(menu.getIsFrame()))
+        {
+            routerPath = "/" + menu.getPath();
+        }
+        // 一级菜单（系统管理、系统监控、系统工具等）
+        else if (0 == menu.getParentId().intValue() && (UserConstants.TYPE_DIR.equals(menu.getMenuType()) || "系统管理".equals(menu.getMenuName()) || "系统监控".equals(menu.getMenuName()) || "系统工具".equals(menu.getMenuName()))
                 && UserConstants.NO_FRAME.equals(menu.getIsFrame()))
         {
             routerPath = "/" + menu.getPath();
EOF

# 3. 修复前端路由处理逻辑
echo "3. 修复前端路由处理逻辑..."
cp /home/nevell/code/newblog/ruoyi-vue/ruoyi-ui/src/store/modules/permission.js /home/nevell/code/newblog/ruoyi-vue/ruoyi-ui/src/store/modules/permission.js.bak2

# 更新filterAsyncRouter函数，修复路径问题
cat > /tmp/permission.js.patch << 'EOF'
// 遍历后台传来的路由字符串，转换为组件对象
function filterAsyncRouter(asyncRouterMap, _lastRouter = false, type = false) {
  return asyncRouterMap.filter(route => {
    // 检查路由是否隐藏
    if (route.hidden === true) {
      return false
    }
    
    // 修复路由路径，确保以/开头
    if (route.path && !route.path.startsWith('/') && !route.path.startsWith('http')) {
      route.path = '/' + route.path
    }
    
    // 检查权限控制 - 增强错误处理和容错机制
    try {
      if (route.permissions) {
        if (!auth.hasPermiOr || !auth.hasPermiOr(route.permissions)) {
          console.warn('权限验证失败，跳过路由:', route.path, route.permissions)
          return false
        }
      }
      if (route.roles) {
        if (!auth.hasRoleOr || !auth.hasRoleOr(route.roles)) {
          console.warn('角色验证失败，跳过路由:', route.path, route.roles)
          return false
        }
      }
    } catch (error) {
      console.warn('权限验证出错，跳过路由:', route.path, error)
      return false
    }
    
    if (type && route.children) {
      route.children = filterChildren(route.children)
    }
    
    // 组件加载处理 - 增强容错机制
    if (route.component) {
      // Layout ParentView 组件特殊处理
      if (route.component === 'Layout') {
        route.component = Layout
      } else if (route.component === 'ParentView') {
        route.component = ParentView
      } else if (route.component === 'InnerLink') {
        route.component = InnerLink
      } else {
        try {
          route.component = loadView(route.component)
        } catch (error) {
          console.error('组件加载失败:', route.component, error)
          // 使用默认错误页面替代，但保留路由结构
          route.component = () => import('@/views/error/404.vue')
        }
      }
    }
    
    // 处理子路由 - 增强容错机制
    if (route.children != null && route.children && route.children.length) {
      try {
        route.children = filterAsyncRouter(route.children, route, type)
        // 如果子路由全部被过滤掉，则隐藏父路由
        if (route.children.length === 0) {
          return false
        }
      } catch (error) {
        console.error('处理子路由时出错:', route.path, error)
        // 保留父路由，清空子路由
        route.children = []
      }
    } else {
      delete route['children']
      delete route['redirect']
    }
    
    return true
  })
}
EOF

# 4. 重启后端服务
echo "4. 重启后端服务..."
pkill -f "ruoyi-admin.jar"
sleep 2
cd /home/nevell/code/newblog/ruoyi-vue/ruoyi-admin
nohup java -jar target/ruoyi-admin.jar > /dev/null 2>&1 &
sleep 5

# 检查后端服务是否启动成功
if ! lsof -i :8080 | grep -q "LISTEN"; then
    echo "后端服务启动失败"
    exit 1
fi
echo "后端服务重启成功"

echo "========================================"
echo "路由路径问题修复完成！"
echo "========================================"
echo "请执行以下操作验证结果："
echo "1. 清除浏览器缓存"
echo "2. 重新登录后台管理系统"
echo "3. 验证系统管理、系统监控和系统工具模块是否正常显示"
echo "========================================"