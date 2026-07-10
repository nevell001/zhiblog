<template>
  <div class="blog-nav">
    <!-- 汉堡菜单按钮（仅移动端显示） -->
    <div v-if="isMobile" class="hamburger-menu" @click="toggleMobileMenu">
      <div class="hamburger-icon" :class="{ active: isMobileMenuOpen }">
        <span></span>
        <span></span>
        <span></span>
      </div>
    </div>

    <!-- 导航菜单 -->
    <div class="nav-menu" :class="{ 'mobile-open': isMobileMenuOpen }">
      <router-link
        v-for="menu in frontendMenus"
        :key="menu.path"
        :to="menu.path"
        class="nav-item"
        @click="closeMobileMenu"
      >
        <component :is="getMenuIcon(menu.icon)" :size="16" style="vertical-align: middle" />
        <span>{{ menu.name }}</span>
      </router-link>
    </div>

    <!-- 右侧操作按钮 -->
    <div class="nav-actions">
      <!-- 用户信息（已登录） -->
      <el-dropdown v-if="userStore.token" trigger="click" @command="handleUserCommand">
        <div class="user-info">
          <el-avatar :size="36" :src="userStore.avatar">
            <el-icon><UserFilled /></el-icon>
          </el-avatar>
          <span class="username">{{ userStore.name }}</span>
          <el-icon class="dropdown-icon">
            <ArrowDown />
          </el-icon>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <!-- 管理员显示管理后台入口 -->
            <el-dropdown-item v-if="userStore.userType === '00'" @click="goToAdmin">
              <el-icon><Setting /></el-icon>
              管理后台
            </el-dropdown-item>
            <el-dropdown-item command="profile">
              <el-icon><User /></el-icon>
              个人中心
            </el-dropdown-item>
            <el-dropdown-item divided command="logout">
              <el-icon><SwitchButton /></el-icon>
              退出登录
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>

      <!-- 登录按钮（未登录） -->
      <el-button v-else size="small" type="primary" @click="goToLogin">登录</el-button>

      <el-button link class="theme-toggle" title="切换主题" @click="toggleTheme">
        <el-icon :size="18">
          <Sunny v-if="isDarkTheme" />
          <Moon v-else />
        </el-icon>
      </el-button>
      <el-button ref="scrollTopBtn" link class="scroll-top" title="回到顶部" @click="scrollToTop">
        <el-icon :size="18">
          <Top />
        </el-icon>
      </el-button>
    </div>

    <!-- 遮罩层（移动端菜单打开时显示） -->
    <div v-if="isMobileMenuOpen" class="overlay" @click="closeMobileMenu"></div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getFilteredMenus } from '@/config/menu'
import { useUserStore } from '@/stores/user'
import { useDevice } from '@/composables/useDevice'
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
  Document,
  UserFilled,
  ArrowDown,
  User,
  SwitchButton
} from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()
const { isMobile } = useDevice()
const scrollTopBtn = ref<HTMLElement | null>(null)

// 主题状态
const isDarkTheme = ref(false)

// 移动端菜单状态
const isMobileMenuOpen = ref(false)

// 获取前台菜单
const frontendMenus = computed(() => {
  return getFilteredMenus('guest', 'frontend')
})

// 获取菜单图标
const getMenuIcon = (icon: string) => {
  const iconMap: Record<string, any> = {
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
  document.documentElement.classList.toggle('dark', isDarkTheme.value)
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

// 跳转到统一登录页
const goToLogin = () => {
  router.push('/login')
}

// 处理用户下拉菜单命令
const handleUserCommand = async (command: string) => {
  switch (command) {
    case 'profile':
      window.location.href = '/user/profile'
      break
    case 'logout':
      try {
        await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        await userStore.logOut()
        ElMessage.success('已退出登录')
        router.push('/blog')
      } catch (error) {
        // 用户取消操作
      }
      break
  }
}

// 切换移动端菜单
const toggleMobileMenu = () => {
  isMobileMenuOpen.value = !isMobileMenuOpen.value
}

// 关闭移动端菜单
const closeMobileMenu = () => {
  isMobileMenuOpen.value = false
}

// 显示/隐藏回到顶部按钮
const handleScroll = () => {
  const scrollTop = window.pageYOffset || document.documentElement.scrollTop
  if (scrollTopBtn.value) {
    scrollTopBtn.value.style.opacity = scrollTop > 300 ? '1' : '0'
  }
}

// 初始化主题
onMounted(() => {
  const savedTheme = localStorage.getItem('blog-theme') || 'light'
  isDarkTheme.value = savedTheme === 'dark'
  document.documentElement.classList.toggle('dark', isDarkTheme.value)

  // 添加滚动监听
  window.addEventListener('scroll', handleScroll)
})

onUnmounted(() => {
  // 移除滚动监听
  window.removeEventListener('scroll', handleScroll)
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
  background: var(--tech-bg-light);
  border-radius: 50%;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.2);
  transition: all 0.3s ease;
  z-index: 1002;
}

.hamburger-menu:hover {
  background: var(--tech-primary);
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
  background: var(--tech-text);
  border-radius: 2px;
  transition: all 0.3s ease;
}

.hamburger-menu:hover .hamburger-icon span {
  background: var(--tech-bg);
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
  background: var(--tech-bg-light);
  border-radius: 25px;
  text-decoration: none;
  color: var(--tech-text);
  font-size: 0.9rem;
  font-weight: 500;
  transition: all 0.3s ease;
  border: 1px solid var(--tech-bg);
  display: flex;
  align-items: center;
  gap: 6px;
  white-space: nowrap;
}

.nav-item:hover {
  background: var(--tech-primary);
  color: var(--tech-bg);
  transform: translateY(-2px);
  box-shadow: 0 4px 20px rgba(0, 212, 255, 0.3);
}

.nav-item i {
  font-size: 1rem;
}

.nav-actions {
  display: flex;
  gap: 8px;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 6px 14px;
  background: var(--tech-bg-light);
  border-radius: 25px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid var(--tech-bg);
}

.user-info:hover {
  background: var(--tech-primary);
  color: var(--tech-bg);
  transform: translateY(-2px);
}

.user-info .username {
  font-size: 0.9rem;
  font-weight: 500;
  max-width: 120px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.theme-toggle,
.scroll-top {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: var(--tech-bg-light);
  border: 1px solid var(--tech-bg);
  color: var(--tech-text);
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.theme-toggle:hover,
.scroll-top:hover {
  background: var(--tech-primary);
  color: var(--tech-bg);
  transform: scale(1.1);
  box-shadow: 0 4px 12px rgba(0, 212, 255, 0.3);
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
  background: rgba(0, 25, 47, 0.7);
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
  .hamburger-menu {
    display: block;
  }

  .nav-menu {
    position: fixed;
    top: 0;
    right: -100%;
    width: 280px;
    height: 100vh;
    background: var(--tech-bg);
    flex-direction: column;
    align-items: flex-start;
    padding: 80px 20px 20px;
    gap: 12px;
    box-shadow: -4px 0 20px rgba(0, 0, 0, 0.3);
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
  }
}
</style>
