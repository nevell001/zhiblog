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
          <template v-if="userStore.token && userStore.name">
            <!-- 站内通知铃铛 -->
            <el-popover
              placement="bottom-end"
              :width="340"
              trigger="click"
              :teleported="false"
              @show="handleNotifShow"
            >
              <template #reference>
                <div class="notif-trigger">
                  <el-badge :value="unreadCount" :hidden="unreadCount <= 0" :max="99">
                    <el-icon :size="19"><Bell /></el-icon>
                  </el-badge>
                </div>
              </template>
              <div class="notif-panel">
                <div class="notif-panel-head">
                  <span class="notif-panel-title">通知</span>
                  <el-button
                    v-if="notifications.length > 0"
                    link
                    type="primary"
                    size="small"
                    @click="markAllNotifications"
                  >
                    全部已读
                  </el-button>
                </div>
                <div v-if="notificationLoading" class="notif-empty">加载中…</div>
                <div v-else-if="notifications.length === 0" class="notif-empty">暂无新通知</div>
                <div v-else class="notif-list">
                  <div
                    v-for="n in notifications"
                    :key="n.id"
                    class="notif-item"
                    :class="{ 'is-unread': n.isRead !== 1 }"
                    @click="handleNotifClick(n)"
                  >
                    <div class="notif-item-title">
                      <span v-if="n.isRead !== 1" class="notif-dot"></span>
                      {{ n.title || notifTypeText(n.type) }}
                    </div>
                    <div class="notif-item-content">{{ n.content }}</div>
                    <div class="notif-item-time">{{ formatNotifTime(n.createTime) }}</div>
                  </div>
                </div>
                <div class="notif-panel-foot">
                  <router-link to="/user/profile" class="notif-more">查看全部通知</router-link>
                </div>
              </div>
            </el-popover>

            <el-dropdown trigger="click" @command="handleUserCommand">
              <div class="user-info">
                <div class="user-avatar-wrapper">
                  <img v-if="userStore.avatar" :src="userStore.avatar" class="user-avatar" />
                  <div v-else class="avatar-placeholder">
                    <el-icon :size="16"><UserFilled /></el-icon>
                  </div>
                </div>
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
                  <el-dropdown-item command="bookmark">
                    <el-icon><CollectionTag /></el-icon>
                    我的收藏
                  </el-dropdown-item>
                  <el-dropdown-item divided command="logout">
                    <el-icon><SwitchButton /></el-icon>
                    退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <el-button v-else size="small" type="primary" @click="$router.push('/login')">
            登录
          </el-button>
          <button class="theme-btn" :title="themeModeTooltip" @click="toggleTheme">
            <el-icon :size="18">
              <Monitor v-if="themeMode === 'system'" />
              <Sunny v-else-if="isDark" />
              <Moon v-else />
            </el-icon>
          </button>
          <button
            class="hamburger-btn"
            aria-label="展开菜单"
            :class="{ 'is-open': mobileMenuOpen }"
            @click="mobileMenuOpen = !mobileMenuOpen"
          >
            <el-icon :size="20"><Menu /></el-icon>
          </button>
        </div>
      </div>
      <!-- 移动端下拉菜单 -->
      <div v-show="mobileMenuOpen" class="mobile-nav-menu">
        <router-link
          v-for="m in menus"
          :key="m.path"
          :to="m.path"
          class="mobile-nav-link"
          @click="mobileMenuOpen = false"
        >
          {{ m.name }}
        </router-link>
        <template v-if="userStore.token && userStore.name">
          <router-link to="/blog/bookmarks" class="mobile-nav-link" @click="mobileMenuOpen = false">
            我的收藏
          </router-link>
          <router-link to="/user/profile" class="mobile-nav-link" @click="mobileMenuOpen = false">
            个人中心
          </router-link>
        </template>
        <router-link v-else to="/login" class="mobile-nav-link" @click="mobileMenuOpen = false">
          登录
        </router-link>
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
          <div v-if="friendLinks.length > 0 && isFriendLinkEnabled" class="footer-col">
            <h4>友情链接</h4>
            <a
              v-for="link in friendLinks"
              :key="link.id"
              :href="formatUrl(link.url)"
              target="_blank"
              rel="noopener noreferrer"
              :title="link.description || link.name"
            >
              {{ link.name }}
            </a>
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
import { computed, onBeforeUnmount, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from '@/plugins/element-plus-service'
import { useUserStore } from '@/stores/user'
import { useSettingsStore } from '@/stores/settings'
import { useBlogSettingsStore } from '@/stores/blogSettings'
import {
  Setting,
  UserFilled,
  ArrowDown,
  User,
  CollectionTag,
  SwitchButton,
  Monitor,
  Sunny,
  Moon,
  Bell,
  Menu
} from '@element-plus/icons-vue'
import { getFrontFriendLinkList } from '@/api/blog/friendLink'
import {
  getNotificationList,
  getUnreadCount,
  markAllAsRead,
  markAsRead,
  type BlogNotification
} from '@/api/blog/notification'
import { getApiBaseUrl } from '@/utils/index'

const router = useRouter()
const userStore = useUserStore()
const settingsStore = useSettingsStore()
const blogSettingsStore = useBlogSettingsStore()

const blogSettings = computed(() => blogSettingsStore.blogSettings)
const isDark = computed(() => settingsStore.isDark)
const themeMode = computed(() => settingsStore.themeMode)

// 主题模式提示文本
const themeModeTooltip = computed(() => {
  switch (settingsStore.themeMode) {
    case 'system':
      return '跟随系统 (点击切换)'
    case 'dark':
      return '深色模式 (点击切换)'
    case 'light':
      return '浅色模式 (点击切换)'
    default:
      return '主题模式'
  }
})

interface FriendLink {
  id: number
  name: string
  url: string
  logo?: string
  description?: string
}
const friendLinks = ref<FriendLink[]>([])

// 移动端菜单展开状态
const mobileMenuOpen = ref(false)

// 站内通知
const unreadCount = ref(0)
const notifications = ref<BlogNotification[]>([])
const notificationLoading = ref(false)
let notifTimer: ReturnType<typeof setInterval> | null = null

const notifTypeText = (type?: string) => {
  switch (type) {
    case 'comment':
      return '收到新评论'
    case 'reply':
      return '收到回复'
    case 'audit':
      return '评论已通过审核'
    case 'reject':
      return '评论未通过审核'
    default:
      return '新通知'
  }
}

const formatNotifTime = (time?: string) => {
  if (!time) return ''
  const date = new Date(time)
  if (Number.isNaN(date.getTime())) return time
  return date.toLocaleString('zh-CN', {
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const fetchUnreadCount = async () => {
  if (!userStore.token) {
    unreadCount.value = 0
    return
  }
  try {
    const response = await getUnreadCount()
    const count = Number((response && (response as any).data) ?? 0)
    unreadCount.value = Number.isFinite(count) && count > 0 ? count : 0
  } catch {
    unreadCount.value = 0
  }
}

const fetchNotifications = async () => {
  if (!userStore.token) {
    notifications.value = []
    return
  }
  notificationLoading.value = true
  try {
    const response = await getNotificationList({ pageNum: 1, pageSize: 10 })
    const rows = (response && (response as any).rows) || []
    notifications.value = Array.isArray(rows) ? rows : []
  } catch {
    // 静默失败：保持空列表
    notifications.value = []
  } finally {
    notificationLoading.value = false
  }
}

const handleNotifShow = () => {
  fetchNotifications()
  fetchUnreadCount()
}

const handleNotifClick = async (n: BlogNotification) => {
  if (n.isRead !== 1) {
    try {
      await markAsRead([n.id])
      n.isRead = 1
      unreadCount.value = Math.max(0, unreadCount.value - 1)
    } catch {
      // 标记失败不阻断跳转
    }
  }
  if (n.articleId) {
    router.push(`/blog/article/${n.articleId}`)
  }
}

const markAllNotifications = async () => {
  try {
    await markAllAsRead()
    notifications.value.forEach(n => {
      n.isRead = 1
    })
    unreadCount.value = 0
  } catch {
    // 忽略
  }
}

// 友链全局开关（默认开启）
const isFriendLinkEnabled = computed(() => {
  const v = (blogSettings.value as any).friend_link_enabled
  return v === undefined || v === null || v === 'true' || v === true
})

const menus = [
  { name: '首页', path: '/blog' },
  { name: '分类', path: '/blog/category' },
  { name: '标签', path: '/blog/tag' },
  { name: '归档', path: '/blog/archive' },
  { name: '关于', path: '/blog/about' }
]

const currentYear = computed(() => new Date().getFullYear())

const rssUrl = computed(() => {
  const baseUrl = getApiBaseUrl()
  return `${baseUrl}/blog/rss`
})

const formatUrl = (url: string) => {
  if (!url) return ''
  if (!url.startsWith('http://') && !url.startsWith('https://')) return 'https://' + url
  return url
}

const toggleTheme = () => {
  settingsStore.toggleTheme()
}

// 路由变化时收起移动端菜单
watch(
  () => router.currentRoute.value.path,
  () => {
    mobileMenuOpen.value = false
  }
)

const goToAdmin = () => {
  router.push('/admin/blog/article')
}

const handleUserCommand = async (command: string) => {
  if (command === 'profile') {
    window.location.href = '/user/profile'
  } else if (command === 'bookmark') {
    router.push('/blog/bookmarks')
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
  // 加载友情链接
  fetchFriendLinks()

  // 登录用户：拉取未读通知并定时刷新
  if (userStore.token) {
    fetchUnreadCount()
    notifTimer = setInterval(fetchUnreadCount, 30000)
  }
})

onBeforeUnmount(() => {
  if (notifTimer) {
    clearInterval(notifTimer)
    notifTimer = null
  }
})

function fetchFriendLinks() {
  getFrontFriendLinkList()
    .then(response => {
      const list = (response && (response.data || response)) as FriendLink[] | undefined
      friendLinks.value = Array.isArray(list) ? list : []
    })
    .catch(() => {
      friendLinks.value = []
    })
}
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
.user-avatar-wrapper {
  flex-shrink: 0;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  overflow: hidden;
}
.user-avatar {
  width: 30px;
  height: 30px;
  object-fit: cover;
  display: block;
}
.avatar-placeholder {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background: var(--mo-n200);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--mo-n500);
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

/* 通知铃铛 */
.notif-trigger {
  display: flex;
  align-items: center;
  padding: 6px;
  margin-right: 4px;
  border-radius: 8px;
  cursor: pointer;
  color: var(--mo-p600);
  transition: background 0.2s;
}
.notif-trigger:hover {
  background: var(--mo-p50);
}
.notif-panel {
  width: 100%;
}
.notif-panel-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
  padding-bottom: 8px;
  border-bottom: 1px solid var(--el-border-color-lighter, #ebeef5);
}
.notif-panel-title {
  font-size: 14px;
  font-weight: 600;
  color: var(--mo-n800);
}
.notif-list {
  max-height: 320px;
  overflow-y: auto;
}
.notif-item {
  padding: 8px 4px;
  border-radius: 6px;
  cursor: pointer;
  transition: background 0.15s;
}
.notif-item:hover {
  background: var(--mo-p50);
}
.notif-item + .notif-item {
  border-top: 1px dashed var(--el-border-color-lighter, #f0f0f0);
}
.notif-item-title {
  display: flex;
  align-items: center;
  font-size: 13px;
  font-weight: 600;
  color: var(--mo-n800);
}
.notif-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: var(--el-color-danger, #f56c6c);
  margin-right: 6px;
  flex-shrink: 0;
}
.notif-item-content {
  margin-top: 4px;
  font-size: 12px;
  color: var(--mo-n500);
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}
.notif-item-time {
  margin-top: 4px;
  font-size: 11px;
  color: var(--mo-n400);
}
.notif-empty {
  padding: 18px 0;
  text-align: center;
  font-size: 13px;
  color: var(--mo-n400);
}
.notif-panel-foot {
  margin-top: 8px;
  padding-top: 8px;
  border-top: 1px solid var(--el-border-color-lighter, #ebeef5);
  text-align: center;
}
.notif-more {
  font-size: 13px;
  color: var(--mo-p600);
  text-decoration: none;
}

/* 汉堡按钮（桌面端隐藏） */
.hamburger-btn {
  display: none;
  background: none;
  border: none;
  cursor: pointer;
  color: var(--mo-p600);
  padding: 6px;
  border-radius: 8px;
  transition: background 0.2s;
}
.hamburger-btn:hover,
.hamburger-btn.is-open {
  background: var(--mo-p50);
}

/* 移动端菜单（桌面端隐藏） */
.mobile-nav-menu {
  display: none;
}

/* 响应式 */
@media (max-width: 768px) {
  .nav-center {
    display: none;
  }
  .hamburger-btn {
    display: inline-flex;
    align-items: center;
    margin-left: 4px;
  }
  .mobile-nav-menu {
    display: flex;
    flex-direction: column;
    gap: 2px;
    padding: 10px 16px 14px;
    background: var(--mo-n50);
    border-top: 1px solid rgba(0, 0, 0, 0.06);
  }
  .mobile-nav-link {
    padding: 10px 8px;
    border-radius: 8px;
    font-size: 15px;
    color: var(--mo-n800);
    text-decoration: none;
    transition: background 0.15s;
  }
  .mobile-nav-link:hover {
    background: var(--mo-p50);
    color: var(--mo-p600);
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
