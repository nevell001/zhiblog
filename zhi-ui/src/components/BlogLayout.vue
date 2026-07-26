<template>
  <div class="blog-layout">
    <!-- 统一顶部导航 -->
    <nav class="blog-top-nav">
      <div class="nav-inner">
        <router-link to="/blog" class="nav-left">
          <div class="brand-logo">
            {{ blogSettings.blog_name?.charAt(0) || '博' }}
          </div>
          <span class="brand-name">{{ blogSettings.blog_name || '我的博客' }}</span>
        </router-link>
        <div class="nav-center">
          <router-link v-for="menu in menus" :key="menu.path" :to="menu.path" class="nav-link">
            {{ menu.name }}
          </router-link>
        </div>
        <div class="nav-right">
          <el-dropdown
            v-if="userStore.token && userStore.name"
            trigger="click"
            @command="handleUserCommand"
          >
            <div class="user-info">
              <el-avatar :size="30" :src="userStore.avatar">
                <el-icon><UserFilled /></el-icon>
              </el-avatar>
              <span class="username">{{ userStore.name }}</span>
              <el-icon class="dropdown-icon">
                <ArrowDown />
              </el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
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
          <el-button v-else size="small" type="primary" @click="$router.push('/login')">
            登录
          </el-button>
          <button class="theme-btn" :title="isDark ? '切换亮色' : '切换深色'" @click="toggleTheme">
            <svg
              v-if="isDark"
              width="18"
              height="18"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              stroke-width="2"
            >
              <circle cx="12" cy="12" r="5" />
              <line x1="12" y1="1" x2="12" y2="3" />
              <line x1="12" y1="21" x2="12" y2="23" />
              <line x1="4.22" y1="4.22" x2="5.64" y2="5.64" />
              <line x1="18.36" y1="18.36" x2="19.78" y2="19.78" />
              <line x1="1" y1="12" x2="3" y2="12" />
              <line x1="21" y1="12" x2="23" y2="12" />
              <line x1="4.22" y1="19.78" x2="5.64" y2="18.36" />
              <line x1="18.36" y1="5.64" x2="19.78" y2="4.22" />
            </svg>
            <svg
              v-else
              width="18"
              height="18"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              stroke-width="2"
            >
              <path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z" />
            </svg>
          </button>
        </div>
      </div>
    </nav>

    <!-- 子页面内容 -->
    <slot></slot>

    <!-- 统一底部 -->
    <footer class="blog-site-footer">
      <div class="footer-wave">
        <svg viewBox="0 0 1440 60" preserveAspectRatio="none">
          <path
            fill="currentColor"
            d="M0,30L80,25C160,20,320,15,480,20C640,25,800,35,960,30C1120,25,1280,15,1360,10L1440,5L1440,60L1360,60C1280,60,1120,60,960,60C800,60,640,60,480,60C320,60,160,60,80,60L0,60Z"
          />
        </svg>
      </div>
      <div class="footer-inner">
        <div class="footer-brand">
          <div class="footer-logo">
            {{ blogSettings.blog_name?.charAt(0) || '博' }}
          </div>
          <div class="brand-text">
            <span class="brand-name">{{ blogSettings.blog_name || '我的博客' }}</span>
            <p class="brand-desc">
              {{ blogSettings.blog_desc || '分享技术与生活的点滴' }}
            </p>
          </div>
        </div>
        <div class="footer-links">
          <div class="footer-col">
            <h4>快速导航</h4>
            <router-link to="/blog">首页</router-link>
            <router-link to="/blog/category">分类</router-link>
            <router-link to="/blog/tag">标签</router-link>
            <router-link to="/blog/archive">归档</router-link>
            <router-link to="/blog/about">关于</router-link>
          </div>
          <div class="footer-col">
            <h4>社交平台</h4>
            <a
              v-if="blogSettings.github_url"
              :href="formatUrl(blogSettings.github_url)"
              target="_blank"
            >
              GitHub
            </a>
            <a
              v-if="blogSettings.weibo_url"
              :href="formatUrl(blogSettings.weibo_url)"
              target="_blank"
            >
              微博
            </a>
            <a :href="rssUrl" target="_blank">RSS订阅</a>
          </div>
        </div>
      </div>
      <div class="footer-bottom">
        <p>
          © {{ currentYear }}
          {{ blogSettings.blog_author || blogSettings.blog_name || '我的博客' }} · 保留所有权利
        </p>
        <p class="tech-info">Powered by ZhiBlog - 知博 & Element Plus</p>
      </div>
    </footer>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from '@/plugins/element-plus-service'
import { useUserStore } from '@/stores/user'
import { useBlogSettingsStore } from '@/stores/blogSettings'
import { Setting, UserFilled, ArrowDown, User, SwitchButton } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()
const blogSettingsStore = useBlogSettingsStore()

const blogSettings = computed(() => blogSettingsStore.blogSettings)
const isDark = ref(false)

const menus = [
  { name: '首页', path: '/blog' },
  { name: '分类', path: '/blog/category' },
  { name: '标签', path: '/blog/tag' },
  { name: '归档', path: '/blog/archive' },
  { name: '关于', path: '/blog/about' }
]

const currentYear = computed(() => new Date().getFullYear())

const rssUrl = computed(() => {
  const baseUrl =
    window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1'
      ? 'http://localhost:8080'
      : `${window.location.protocol}//${window.location.hostname}:8080`
  return `${baseUrl}/blog/rss`
})

const formatUrl = (url: string) => {
  if (!url) return ''
  if (!url.startsWith('http://') && !url.startsWith('https://')) return 'https://' + url
  return url
}

const toggleTheme = () => {
  isDark.value = !isDark.value
  document.documentElement.classList.toggle('dark', isDark.value)
  localStorage.setItem('blog-theme', isDark.value ? 'dark' : 'light')
}

const goToAdmin = () => {
  router.push('/admin')
}

const handleUserCommand = async (command: string) => {
  if (command === 'profile') {
    window.location.href = '/user/profile'
  } else if (command === 'logout') {
    try {
      await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      await userStore.logOut()
      ElMessage.success('已退出登录')
      router.push('/blog')
    } catch {
      // User cancelled
    }
  }
}

onMounted(() => {
  const saved = localStorage.getItem('blog-theme') || 'light'
  isDark.value = saved === 'dark'
  document.documentElement.classList.toggle('dark', isDark.value)
})
</script>

<style scoped>
.blog-layout {
  min-height: 100vh;
  background: var(--mo-n50);
  color: var(--mo-n800);
  font-family: Inter, 'PingFang SC', 'Microsoft YaHei', system-ui, sans-serif;
}

/* 导航栏 */
.blog-top-nav {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 100;
  border-bottom: 1px solid rgba(0, 0, 0, 0.06);
  background: rgba(255, 255, 255, 0.92);
  backdrop-filter: blur(12px);
}
.nav-inner {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.nav-left {
  display: flex;
  align-items: center;
  gap: 10px;
  text-decoration: none;
}
.brand-logo {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--mo-p600), var(--mo-p800));
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 14px;
}
.brand-name {
  font-weight: 700;
  font-size: 18px;
  color: var(--mo-n900);
  letter-spacing: 0;
}
.nav-center {
  display: flex;
  gap: 6px;
}
.nav-link {
  padding: 8px 16px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  color: var(--mo-n600);
  text-decoration: none;
  transition: all 0.2s;
}
.nav-link:hover,
.nav-link.router-link-active {
  background: var(--mo-p50);
  color: var(--mo-p700);
}
.nav-right {
  display: flex;
  align-items: center;
  gap: 10px;
}
.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 12px;
  border-radius: 20px;
  transition: background 0.2s;
}
.user-info:hover {
  background: var(--mo-n100);
}
.username {
  font-size: 14px;
  font-weight: 500;
  max-width: 80px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  color: var(--mo-n800);
}
.dropdown-icon {
  color: var(--mo-n400);
}
.theme-btn {
  background: none;
  border: none;
  cursor: pointer;
  color: var(--mo-p600);
  padding: 6px;
  border-radius: 8px;
  transition: background 0.2s;
}
.theme-btn:hover {
  background: var(--mo-p50);
}

/* 底部 */
.blog-site-footer {
  position: relative;
  border-top: 1px solid var(--mo-n200);
  background: var(--mo-n100);
  color: var(--mo-n600);
  padding: 0 24px 24px;
}
.footer-wave {
  position: relative;
  margin-bottom: -2px;
  color: var(--mo-n50);
}
.footer-wave svg {
  width: 100%;
  height: 52px;
  display: block;
}
.footer-inner {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 40px;
  padding-bottom: 32px;
  border-bottom: 1px solid var(--mo-n200);
}
.footer-brand {
  display: flex;
  align-items: flex-start;
  gap: 12px;
}
.footer-logo {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: var(--mo-p100);
  color: var(--mo-p600);
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 16px;
  flex-shrink: 0;
}
.brand-text {
  display: flex;
  flex-direction: column;
}
.brand-desc {
  font-size: 13px;
  color: var(--mo-n500);
  margin-top: 4px;
}
.footer-links {
  display: flex;
  gap: 60px;
}
.footer-col h4 {
  font-size: 13px;
  font-weight: 700;
  color: var(--mo-n800);
  letter-spacing: 0;
  margin-bottom: 16px;
}
.footer-col a {
  display: block;
  font-size: 14px;
  color: var(--mo-n600);
  text-decoration: none;
  margin-bottom: 10px;
  transition: color 0.2s;
}
.footer-col a:hover {
  color: var(--mo-p600);
}
.footer-bottom {
  max-width: 1200px;
  margin: 0 auto;
  padding-top: 24px;
  text-align: center;
  font-size: 13px;
  color: var(--mo-n500);
}
.tech-info {
  margin-top: 6px;
  font-size: 12px;
}

/* 深色模式 */
html.dark .blog-layout {
  background: var(--mo-n900);
  color: var(--mo-n200);
}

html.dark .blog-top-nav {
  border-bottom-color: rgba(68, 64, 60, 0.72);
  background: rgba(28, 25, 23, 0.92);
}

html.dark .brand-name,
html.dark .username,
html.dark .footer-col h4 {
  color: var(--mo-n100);
}

html.dark .nav-link,
html.dark .footer-col a,
html.dark .blog-site-footer,
html.dark .brand-desc,
html.dark .footer-bottom {
  color: var(--mo-n300);
}

html.dark .nav-link:hover,
html.dark .nav-link.router-link-active,
html.dark .theme-btn,
html.dark .footer-col a:hover {
  color: var(--mo-p300);
}

html.dark .nav-link:hover,
html.dark .nav-link.router-link-active,
html.dark .user-info:hover,
html.dark .theme-btn:hover {
  background: rgba(79, 70, 229, 0.18);
}

html.dark .blog-site-footer {
  border-top-color: var(--mo-n700);
  background: var(--mo-n800);
}

html.dark .footer-wave {
  color: var(--mo-n900);
}

html.dark .footer-inner {
  border-bottom-color: var(--mo-n700);
}

html.dark .footer-logo {
  background: rgba(79, 70, 229, 0.18);
  color: var(--mo-p300);
}

html.dark .dropdown-icon {
  color: var(--mo-n400);
}

/* 响应式 */
@media (max-width: 768px) {
  .nav-center {
    display: none;
  }
  .footer-inner {
    flex-direction: column;
    gap: 24px;
  }
  .footer-links {
    gap: 32px;
  }
  .footer-wave svg {
    height: 40px;
  }
}
</style>
