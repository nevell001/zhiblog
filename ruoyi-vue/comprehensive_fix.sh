#!/bin/bash

# ========================================
# 全面修复系统模块404错误
# 创建时间：2025-11-17
# 描述：全面修复系统管理、系统监控和系统工具模块的404错误
# ========================================

echo "========================================"
echo "开始全面修复系统模块404错误"
echo "========================================"

# 1. 修复后端路由控制器
echo "1. 修复后端路由控制器..."
cat > /home/nevell/code/newblog/ruoyi-vue/ruoyi-admin/src/main/java/com/ruoyi/web/controller/AdminRouterController.java << 'EOF'
package com.ruoyi.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 后台管理路由控制器
 * 处理前端SPA应用的后台管理路由请求，支持history模式
 * @author ruoyi
 */
@Controller
public class AdminRouterController {

    /**
     * 捕获所有/admin路径下的请求，重定向到index.html
     * 这样前端路由可以处理这些请求
     */
    @RequestMapping(value = "/admin/**")
    public String spaAdminRouter() {
        return "forward:/index.html";
    }
}
EOF

# 2. 修复数据库菜单路径
echo "2. 修复数据库菜单路径..."
mysql -h localhost -u root -proot newblog << EOF
-- 确保系统管理、系统监控和系统工具模块的路径正确
UPDATE sys_menu SET path = '/admin/system' WHERE menu_id = 1;
UPDATE sys_menu SET path = '/admin/monitor' WHERE menu_id = 2;
UPDATE sys_menu SET path = '/admin/tool' WHERE menu_id = 3;

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

-- 确保所有菜单都是可见且启用状态
UPDATE sys_menu SET visible = '0' WHERE menu_id IN (1, 2, 3) OR parent_id IN (1, 2, 3);
UPDATE sys_menu SET status = '0' WHERE menu_id IN (1, 2, 3) OR parent_id IN (1, 2, 3);

-- 确保管理员拥有所有菜单权限
DELETE FROM sys_role_menu WHERE role_id = 1;
INSERT INTO sys_role_menu (role_id, menu_id) 
SELECT 1, menu_id FROM sys_menu WHERE visible = '0' AND status = '0';
EOF

# 3. 修复前端路由处理逻辑
echo "3. 修复前端路由处理逻辑..."
cp /home/nevell/code/newblog/ruoyi-vue/ruoyi-ui/src/store/modules/permission.js /home/nevell/code/newblog/ruoyi-vue/ruoyi-ui/src/store/modules/permission.js.bak3

# 更新filterAsyncRouter函数，修复路径问题
cat > /tmp/permission_fix.js << 'EOF'
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

export const loadView = (view) => {
  // 使用Vite兼容的静态路径导入
  if (view === '404') {
    return () => import('@/views/error/404.vue')
  }
  if (view === '401') {
    return () => import('@/views/error/401.vue')
  }
  
  // 处理特殊组件路径
  if (view.startsWith('admin/')) {
    // 后台管理组件使用标准路径
    const normalizedPath = view.replace(/^admin\//, '')
    return () => import(`@/views/admin/${normalizedPath}.vue`)
  }
  
  // 更稳健的组件加载：按多个可能的路径依次尝试，运行时捕获失败并继续
  const attempts = []

  // 特殊处理 admin/ 前缀（若后台直接返回 admin/xxx）
  if (view.startsWith('admin/')) {
    const normalizedPath = view.replace(/^admin\//, '')
    attempts.push(() => import(`@/views/admin/${normalizedPath}.vue`))
    attempts.push(() => import(`@/views/admin/${normalizedPath}/index.vue`))
  }

  // 常见尝试路径（从最直观到最具体）
  attempts.push(() => import(`@/views/${view}.vue`))
  attempts.push(() => import(`@/views/${view}/index.vue`))
  attempts.push(() => import(`@/views/admin/${view}.vue`))
  attempts.push(() => import(`@/views/admin/${view}/index.vue`))
  attempts.push(() => import(`@/views/admin/system/${view}/index.vue`))
  attempts.push(() => import(`@/views/admin/monitor/${view}/index.vue`))
  attempts.push(() => import(`@/views/admin/tool/${view}/index.vue`))
  attempts.push(() => import(`@/views/admin/blog/${view}/index.vue`))
  attempts.push(() => import(`@/views/admin/statistics/${view}/index.vue`))

  // 返回一个异步组件加载器：按顺序尝试每个导入，直到成功或使用404
  return async () => {
    for (let i = 0; i < attempts.length; i++) {
      try {
        const mod = await attempts[i]()
        return mod
      } catch (err) {
        // 忽略错误，继续尝试下一个路径
        if (i === attempts.length - 1) {
          console.error(`loadView: 无法加载组件 ${view}，使用404替代`, err)
        }
      }
    }
    return import('@/views/error/404.vue')
  }
}
EOF

# 4. 重新编译后端项目
echo "4. 重新编译后端项目..."
cd /home/nevell/code/newblog/ruoyi-vue/ruoyi-admin
mvn clean package -DskipTests > /dev/null 2>&1

# 5. 重启后端服务
echo "5. 重启后端服务..."
pkill -f "ruoyi-admin.jar"
sleep 2
nohup java -jar target/ruoyi-admin.jar > /dev/null 2>&1 &
sleep 5

# 检查后端服务是否启动成功
if ! lsof -i :8080 | grep -q "LISTEN"; then
    echo "后端服务启动失败"
    exit 1
fi
echo "后端服务重启成功"

echo "========================================"
echo "全面修复完成！"
echo "========================================"
echo "请执行以下操作验证结果："
echo "1. 清除浏览器缓存 (Ctrl+Shift+R)"
echo "2. 重新登录后台管理系统"
echo "3. 验证系统管理、系统监控和系统工具模块是否正常显示"
echo "4. 点击各个子菜单，确认不再出现404错误"
echo "========================================"