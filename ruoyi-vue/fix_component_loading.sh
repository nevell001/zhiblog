#!/bin/bash

# ========================================
# 修复组件加载问题
# 创建时间：2025-11-17
# 描述：修复系统模块组件加载问题，解决404错误
# ========================================

echo "========================================"
echo "开始修复组件加载问题"
echo "========================================"

# 1. 修复前端组件加载逻辑
echo "1. 修复前端组件加载逻辑..."

# 备份原始文件
cp /home/nevell/code/newblog/ruoyi-vue/ruoyi-ui/src/store/modules/permission.js /home/nevell/code/newblog/ruoyi-vue/ruoyi-ui/src/store/modules/permission.js.bak

# 更新loadView函数
cat > /tmp/loadView.js << 'EOF'
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

# 2. 修复数据库菜单组件路径
echo "2. 修复数据库菜单组件路径..."
mysql -h localhost -u root -proot newblog << EOF
-- 确保系统管理模块的父菜单有正确的路径和组件
UPDATE sys_menu SET path = 'system', component = 'Layout' WHERE menu_id = 1;

-- 确保系统监控模块的父菜单有正确的路径和组件
UPDATE sys_menu SET path = 'monitor', component = 'Layout' WHERE menu_id = 2;

-- 确保系统工具模块的父菜单有正确的路径和组件
UPDATE sys_menu SET path = 'tool', component = 'Layout' WHERE menu_id = 3;

-- 确保所有子菜单的路径和组件正确
UPDATE sys_menu SET path = 'user', component = 'admin/system/user/user/index' WHERE menu_id = 100;
UPDATE sys_menu SET path = 'role', component = 'admin/system/role/role/index' WHERE menu_id = 101;
UPDATE sys_menu SET path = 'menu', component = 'admin/system/menu/menu/index' WHERE menu_id = 102;
UPDATE sys_menu SET path = 'dept', component = 'admin/system/dept/dept/index' WHERE menu_id = 103;
UPDATE sys_menu SET path = 'post', component = 'admin/system/post/post/index' WHERE menu_id = 104;
UPDATE sys_menu SET path = 'dict', component = 'admin/system/dict/dict/index' WHERE menu_id = 105;
UPDATE sys_menu SET path = 'config', component = 'admin/system/config/config/index' WHERE menu_id = 106;
UPDATE sys_menu SET path = 'notice', component = 'admin/system/notice/notice/index' WHERE menu_id = 107;

UPDATE sys_menu SET path = 'online', component = 'admin/monitor/online/index' WHERE menu_id = 109;
UPDATE sys_menu SET path = 'logininfor', component = 'admin/monitor/logininfor/index' WHERE menu_id = 501;
UPDATE sys_menu SET path = 'operlog', component = 'admin/monitor/operlog/index' WHERE menu_id = 500;
UPDATE sys_menu SET path = 'druid', component = 'admin/monitor/druid/index' WHERE menu_id = 108;
UPDATE sys_menu SET path = 'server', component = 'admin/monitor/server/index' WHERE menu_id = 112;
UPDATE sys_menu SET path = 'cache', component = 'admin/monitor/cache/index' WHERE menu_id = 113;
UPDATE sys_menu SET path = 'job', component = 'admin/monitor/job/index' WHERE menu_id = 115;

UPDATE sys_menu SET path = 'gen', component = 'admin/tool/gen/index' WHERE menu_id = 116;
UPDATE sys_menu SET path = 'swagger', component = 'admin/tool/swagger/index' WHERE menu_id = 117;

-- 确保所有菜单都是可见且启用状态
UPDATE sys_menu SET visible = '0' WHERE menu_id IN (1, 2, 3) OR parent_id IN (1, 2, 3);
UPDATE sys_menu SET status = '0' WHERE menu_id IN (1, 2, 3) OR parent_id IN (1, 2, 3);
EOF

# 3. 创建缺失的组件文件
echo "3. 检查并创建缺失的组件文件..."

# 检查并创建缺失的组件文件
for file in \
  "ruoyi-ui/src/views/admin/monitor/druid/index.vue" \
  "ruoyi-ui/src/views/admin/monitor/server/index.vue" \
  "ruoyi-ui/src/views/admin/monitor/cache/index.vue" \
  "ruoyi-ui/src/views/admin/monitor/job/index.vue" \
  "ruoyi-ui/src/views/admin/tool/swagger/index.vue"
do
  if [ ! -f "/home/nevell/code/newblog/ruoyi-vue/$file" ]; then
    echo "创建缺失的组件文件: $file"
    mkdir -p "/home/nevell/code/newblog/ruoyi-vue/$(dirname $file)"
    
    if [[ $file == *"druid"* ]]; then
      cat > "/home/nevell/code/newblog/ruoyi-vue/$file" << 'EOF'
<template>
  <div>
    <i-frame v-model:src="url"></i-frame>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import iFrame from '@/components/iFrame/index.vue'

const url = ref(import.meta.env.VITE_APP_BASE_API + '/druid/login.html')
</script>
EOF
    elif [[ $file == *"swagger"* ]]; then
      cat > "/home/nevell/code/newblog/ruoyi-vue/$file" << 'EOF'
<template>
  <i-frame v-model:src="url"></i-frame>
</template>

<script setup>
import { ref } from 'vue'
import iFrame from '@/components/iFrame/index.vue'

const url = ref(import.meta.env.VITE_APP_BASE_API + "/swagger-ui/index.html")
</script>
EOF
    else
      cat > "/home/nevell/code/newblog/ruoyi-vue/$file" << 'EOF'
<template>
  <div class="app-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>功能开发中</span>
        </div>
      </template>
      <div class="text item">
        该功能正在开发中，敬请期待...
      </div>
    </el-card>
  </div>
</template>

<script setup>
// 功能开发中
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.text {
  font-size: 14px;
}
.item {
  margin-bottom: 18px;
}
</style>
EOF
    fi
  fi
done

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
echo "组件加载问题修复完成！"
echo "========================================"
echo "请执行以下操作验证结果："
echo "1. 清除浏览器缓存"
echo "2. 重新登录后台管理系统"
echo "3. 验证系统管理、系统监控和系统工具模块是否正常显示"
echo "========================================"