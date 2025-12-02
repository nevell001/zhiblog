<template>
  <div class="blog-nav">
    <router-link 
      v-for="menu in frontendMenus" 
      :key="menu.path"
      :to="menu.path" 
      class="nav-item"
    >
      <i :class="getMenuIcon(menu.icon)"></i>
      <span>{{ menu.name }}</span>
    </router-link>
    
    <div class="nav-item admin-link" @click="goToAdmin">
      <i class="el-icon-setting"></i>
      <span>后台管理</span>
    </div>
    
    <div class="nav-actions">
      <el-button
        link
        @click="toggleTheme"
        class="theme-toggle"
        title="切换主题"
      >
        <i :class="isDarkTheme ? 'el-icon-sunny' : 'el-icon-moon'"></i>
      </el-button>
      <el-button
        link
        @click="scrollToTop"
        class="scroll-top"
        title="回到顶部"
      >
        <i class="el-icon-top"></i>
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { getFilteredMenus } from '@/config/menu'

// 路由实例
const router = useRouter()

// 主题状态
const isDarkTheme = ref(false)
let scrollHandler = null

// 获取前台菜单
const frontendMenus = computed(() => {
  return getFilteredMenus('frontend', 'guest')
})

// 获取菜单图标
const getMenuIcon = (icon) => {
  const iconMap = {
    home: 'el-icon-house',
    category: 'el-icon-folder-opened',
    tag: 'el-icon-price-tag',
    archive: 'el-icon-date',
    info: 'el-icon-info'
  }
  return iconMap[icon] || 'el-icon-document'
}

// 切换主题
const toggleTheme = () => {
  isDarkTheme.value = !isDarkTheme.value
  document.documentElement.setAttribute('data-theme', isDarkTheme.value ? 'dark' : 'light')
  localStorage.setItem('blog-theme', isDarkTheme.value ? 'dark' : 'light')
}

// 回到顶部
const scrollToTop = () => {
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

// 跳转到后台管理
const goToAdmin = () => {
  window.location.href = '/login?redirect=/admin'
}

// 显示/隐藏回到顶部按钮
const handleScroll = () => {
  const scrollTop = window.pageYOffset || document.documentElement.scrollTop
  const scrollTopBtn = document.querySelector('.scroll-top')
  if (scrollTopBtn) {
    scrollTopBtn.style.opacity = scrollTop > 300 ? '1' : '0'
  }
}

// 初始化主题
onMounted(() => {
  const savedTheme = localStorage.getItem('blog-theme') || 'light'
  isDarkTheme.value = savedTheme === 'dark'
  document.documentElement.setAttribute('data-theme', savedTheme)

  // 添加滚动监听
  scrollHandler = handleScroll
  window.addEventListener('scroll', scrollHandler)
})

onUnmounted(() => {
  if (scrollHandler) {
    window.removeEventListener('scroll', scrollHandler)
  }
})
</script>

<style scoped>
.blog-nav {
  position: fixed;
  top: 20px;
  right: 20px;
  display: flex;
  align-items: center;
  gap: 15px;
  z-index: 1000;
}

.nav-item {
  padding: 10px 18px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 25px;
  text-decoration: none;
  color: #333;
  font-size: 0.9rem;
  font-weight: 500;
  transition: all 0.3s ease;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  gap: 6px;
}

.nav-item:hover {
  background: #409eff;
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 4px 20px rgba(64, 158, 255, 0.3);
}

.admin-link:hover {
  background: #f56c6c;
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 4px 20px rgba(245, 108, 108, 0.3);
}

.nav-item i {
  font-size: 1rem;
}

.nav-actions {
  display: flex;
  gap: 8px;
}

.theme-toggle,
.scroll-top {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.95);
  border: 1px solid rgba(255, 255, 255, 0.2);
  color: #666;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.theme-toggle:hover,
.scroll-top:hover {
  background: #409eff;
  color: white;
  transform: scale(1.1);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

.scroll-top {
  opacity: 0;
  transition: all 0.3s ease;
}

/* 深色主题 */
[data-theme="dark"] .blog-nav .nav-item {
  background: rgba(30, 30, 30, 0.95);
  color: #e0e0e0;
  border-color: rgba(255, 255, 255, 0.1);
}

[data-theme="dark"] .blog-nav .nav-item:hover {
  background: #409eff;
  color: white;
}

[data-theme="dark"] .theme-toggle,
[data-theme="dark"] .scroll-top {
  background: rgba(30, 30, 30, 0.95);
  color: #e0e0e0;
  border-color: rgba(255, 255, 255, 0.1);
}

@media (max-width: 768px) {
  .blog-nav {
    top: 15px;
    right: 15px;
    gap: 10px;
  }

  .nav-item {
    padding: 8px 14px;
    font-size: 0.8rem;
  }

  .nav-item span {
    display: none;
  }

  .theme-toggle,
  .scroll-top {
    width: 35px;
    height: 35px;
  }
}

@media (max-width: 480px) {
  .blog-nav {
    top: 10px;
    right: 10px;
  }

  .nav-actions {
    gap: 5px;
  }

  .nav-item {
    padding: 6px 10px;
  }
}
</style>