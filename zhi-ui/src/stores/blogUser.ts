import { defineStore } from 'pinia'
import { ElMessage } from '@/plugins/element-plus-service'
import {
  blogLogin,
  blogRegister,
  blogLogout,
  getBlogUserInfo,
  sendRegisterCode
} from '@/api/blog/auth'
import defaultAvatar from '@/assets/images/profile.jpg'

const BLOG_TOKEN_KEY = 'Blog-Token'

/**
 * 获取博客用户Token
 */
export function getBlogToken(): string {
  return localStorage.getItem(BLOG_TOKEN_KEY) || ''
}

/**
 * 设置博客用户Token
 */
export function setBlogToken(token: string): void {
  localStorage.setItem(BLOG_TOKEN_KEY, token)
}

/**
 * 移除博客用户Token
 */
export function removeBlogToken(): void {
  localStorage.removeItem(BLOG_TOKEN_KEY)
}

interface BlogUserInfo {
  username: string
  password: string
  code?: string
  uuid?: string
}

interface BlogRegisterInfo {
  username: string
  password: string
  confirmPassword: string
  email: string
  emailCode: string
  code?: string
  uuid?: string
}

interface BlogUserState {
  token: string
  name: string
  avatar: string
  email: string
  userId: number
  isLoggedIn: boolean
}

export const useBlogUserStore = defineStore('blogUser', {
  state: (): BlogUserState => ({
    token: getBlogToken(),
    name: '',
    avatar: '',
    email: '',
    userId: 0,
    isLoggedIn: false
  }),

  getters: {
    /** 判断是否已登录 */
    isLogin: state => !!state.token && !!state.isLoggedIn
  },

  actions: {
    /**
     * 博客用户登录
     */
    async login(userInfo: BlogUserInfo): Promise<void> {
      const username = userInfo.username.trim()
      const password = userInfo.password
      const code = userInfo.code
      const uuid = userInfo.uuid

      try {
        const res = await blogLogin({ username, password, code, uuid })
        const token = res.data || res.token || res
        setBlogToken(token)
        this.token = token
        this.isLoggedIn = true
        await this.getUserInfo()
      } catch (error) {
        console.error('博客用户登录失败:', error)
        throw error
      }
    },

    /**
     * 博客用户注册
     */
    async register(registerInfo: BlogRegisterInfo): Promise<void> {
      try {
        await blogRegister(registerInfo)
        // 注册成功后跳转到登录页面（不自动登录，因为图形验证码已被使用）
        ElMessage.success('注册成功，请登录')
        // 不需要手动跳转，由组件处理跳转
      } catch (error) {
        console.error('博客用户注册失败:', error)
        throw error
      }
    },

    /**
     * 获取博客用户信息
     */
    async getUserInfo(): Promise<void> {
      try {
        const res = await getBlogUserInfo()
        // 后端返回的是 LoginUser 对象，用户信息在 user 字段中
        const loginUser = res.data || res
        const user = loginUser.user || loginUser

        const avatar =
          user.avatar == '' || user.avatar == null
            ? defaultAvatar
            : (import.meta.env?.VITE_APP_BASE_API || '/dev-api') + user.avatar

        this.name = user.userName || user.username || ''
        this.avatar = avatar
        this.email = user.email || ''
        this.userId = user.userId || 0
        this.isLoggedIn = true
      } catch (error) {
        console.error('获取博客用户信息失败:', error)
        throw error
      }
    },

    /**
     * 退出登录
     */
    async logOut(): Promise<void> {
      try {
        await blogLogout()
      } catch (error) {
        console.error('退出登录失败:', error)
      } finally {
        this.token = ''
        this.name = ''
        this.avatar = ''
        this.email = ''
        this.userId = 0
        this.isLoggedIn = false
        removeBlogToken()
      }
    },

    /**
     * 发送注册验证码
     */
    async sendRegisterCode(email: string): Promise<void> {
      try {
        await sendRegisterCode(email)
      } catch (error) {
        console.error('发送注册验证码失败:', error)
        throw error
      }
    }
  }
})
