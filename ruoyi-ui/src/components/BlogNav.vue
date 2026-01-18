<template>
  <div class="blog-nav">
    <!-- 汉堡菜单按钮（仅移动端显示） -->
    <div
      v-if="isMobile"
      class="hamburger-menu"
      @click="toggleMobileMenu"
    >
      <div
        class="hamburger-icon"
        :class="{ active: isMobileMenuOpen }"
      >
        <span></span>
        <span></span>
        <span></span>
      </div>
    </div>

    <!-- 导航菜单 -->
    <div
      class="nav-menu"
      :class="{ 'mobile-open': isMobileMenuOpen }"
    >
      <router-link
        v-for="menu in frontendMenus"
        :key="menu.path"
        :to="menu.path"
        class="nav-item"
        @click="closeMobileMenu"
      >
        <component
          :is="getMenuIcon(menu.icon)"
          :size="16"
          style="vertical-align: middle"
        />
        <span>{{ menu.name }}</span>
      </router-link>

      <div
        class="nav-item admin-link"
        @click="goToAdmin"
      >
        <el-icon :size="16">
          <Setting />
        </el-icon>
        <span>后台管理</span>
      </div>
    </div>

    <!-- 右侧操作按钮 -->
    <div class="nav-actions">
      <el-button
        link
        class="theme-toggle"
        title="切换主题"
        @click="toggleTheme"
      >
        <el-icon :size="18">
          <Sunny v-if="isDarkTheme" />
          <Moon v-else />
        </el-icon>
      </el-button>
      <el-button
        link
        class="scroll-top"
        title="回到顶部"
        @click="scrollToTop"
      >
        <el-icon :size="18">
          <Top />
        </el-icon>
      </el-button>
    </div>

    <!-- 遮罩层（移动端菜单打开时显示） -->
    <div
      v-if="isMobileMenuOpen"
      class="overlay"
      @click="closeMobileMenu"
    ></div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { getFilteredMenus } from '@/config/menu'
import {
  Setting,
  Sunny,
  Moon,
  Top,
  House,
  FolderOpened,
  PriceTag,
  Calendar,
  InfoFilled,
  Document
} from '@element-plus/icons-vue'

// 主题状态
const isDarkTheme = ref(false)
let scrollHandler = null

// 移动端菜单状态
const isMobile = ref(false)
const isMobileMenuOpen = ref(false)

// 获取前台菜单
const frontendMenus = computed(() => {
  return getFilteredMenus('guest', 'frontend')
})

// 获取菜单图标
const getMenuIcon = icon => {
  const iconMap = {
    home: House,
    category: FolderOpened,
    tag: PriceTag,
    archive: Calendar,
    info: InfoFilled
  }
  return iconMap[icon] || Document
}

// 切换主题
const toggleTheme = () => {
  isDarkTheme.value = !isDarkTheme.value
  if (isDarkTheme.value) {
    document.documentElement.classList.add('dark')
  } else {
    document.documentElement.classList.remove('dark')
  }
  localStorage.setItem('blog-theme', isDarkTheme.value ? 'dark' : 'light')
}

// 回到顶部
const scrollToTop = () => {
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

// 跳转到后台管理
const goToAdmin = () => {
  closeMobileMenu()
  window.location.href = '/login?redirect=/admin'
}

// 切换移动端菜单
const toggleMobileMenu = () => {
  isMobileMenuOpen.value = !isMobileMenuOpen.value
}

// 关闭移动端菜单
const closeMobileMenu = () => {
  isMobileMenuOpen.value = false
}

// 检测是否为移动端
const checkMobile = () => {
  isMobile.value = window.innerWidth <= 768
}

// 显示/隐藏回到顶部按钮
const handleScroll = () => {
  const scrollTop = window.pageYOffset || document.documentElement.scrollTop
  const scrollTopBtn = document.querySelector('.scroll-top') as HTMLElement
  if (scrollTopBtn) {
    scrollTopBtn.style.opacity = scrollTop > 300 ? '1' : '0'
  }
}

// 初始化主题
onMounted(() => {
  const savedTheme = localStorage.getItem('blog-theme') || 'light'
  isDarkTheme.value = savedTheme === 'dark'
  if (savedTheme === 'dark') {
    document.documentElement.classList.add('dark')
  } else {
    document.documentElement.classList.remove('dark')
  }

  // 添加滚动监听
  window.addEventListener('scroll', handleScroll)

  // 检测移动端
  checkMobile()
  window.addEventListener('resize', checkMobile)
})

onUnmounted(() => {
  // 移除滚动监听
  window.removeEventListener('scroll', handleScroll)
  window.removeEventListener('resize', checkMobile)
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

/* 汉堡菜单按钮 */
.hamburger-menu {
  display: none;
  cursor: pointer;
  padding: 8px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 50%;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  z-index: 1002;
}

.hamburger-menu:hover {
  background: #409eff;
  transform: scale(1.1);
}

.hamburger-icon {
  width: 24px;
  height: 18px;
  position: relative;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  transition: all 0.3s ease;
}

.hamburger-icon span {
  width: 100%;
  height: 2px;
  background: #333;
  border-radius: 2px;
  transition: all 0.3s ease;
}

.hamburger-menu:hover .hamburger-icon span {
  background: white;
}

.hamburger-icon.active span:nth-child(1) {
  transform: rotate(45deg) translate(6px, 6px);
}

.hamburger-icon.active span:nth-child(2) {
  opacity: 0;
}

.hamburger-icon.active span:nth-child(3) {
  transform: rotate(-45deg) translate(6px, -6px);
}

/* 导航菜单容器 */
.nav-menu {
  display: flex;
  align-items: center;
  gap: 15px;
  transition: all 0.3s ease;
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
  white-space: nowrap;
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

/* 遮罩层 */
.overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  z-index: 1000;
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

/* 深色主题 */
[data-theme='dark'] .blog-nav .nav-item {
  background: rgba(30, 30, 30, 0.95);
  color: #e0e0e0;
  border-color: rgba(255, 255, 255, 0.1);
}

[data-theme='dark'] .blog-nav .nav-item:hover {
  background: #409eff;
  color: white;
}

[data-theme='dark'] .theme-toggle,
[data-theme='dark'] .scroll-top,
[data-theme='dark'] .hamburger-menu {
  background: rgba(30, 30, 30, 0.95);
  color: #e0e0e0;
  border-color: rgba(255, 255, 255, 0.1);
}

[data-theme='dark'] .hamburger-icon span {
  background: #e0e0e0;
}

[data-theme='dark'] .hamburger-menu:hover .hamburger-icon span {
  background: white;
}

/* 平板响应式 */
@media (max-width: 1024px) {
  .blog-nav {
    top: 15px;
    right: 15px;
    gap: 12px;
  }

  .nav-item {
    padding: 8px 14px;
    font-size: 0.85rem;
  }

  .theme-toggle,
  .scroll-top {
    width: 36px;
    height: 36px;
  }
}

/* 移动端响应式 */
@media (max-width: 768px) {
  .blog-nav {
    top: 15px;
    right: 15px;
    gap: 10px;
  }

  /* 显示汉堡菜单 */
  .hamburger-menu {
    display: block;
  }

  /* 隐藏导航菜单，直到点击汉堡菜单 */
  .nav-menu {
    position: fixed;
    top: 0;
    right: -100%;
    width: 280px;
    height: 100vh;
    background: rgba(255, 255, 255, 0.98);
    flex-direction: column;
    align-items: flex-start;
    padding: 80px 20px 20px;
    gap: 12px;
    box-shadow: -4px 0 20px rgba(0, 0, 0, 0.15);
    transition: right 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    z-index: 1001;
  }

  .nav-menu.mobile-open {
    right: 0;
  }

  .nav-item {
    width: 100%;
    padding: 12px 16px;
    border-radius: 12px;
    font-size: 0.95rem;
    justify-content: flex-start;
  }

  .nav-item span {
    display: inline;
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

  .hamburger-menu {
    padding: 6px;
  }

  .hamburger-icon {
    width: 20px;
    height: 16px;
  }

  .nav-menu {
    width: 260px;
    padding-top: 70px;
  }

  .nav-actions {
    gap: 5px;
  }

  .theme-toggle,
  .scroll-top {
    width: 32px;
    height: 32px;
  }
}
</style>
