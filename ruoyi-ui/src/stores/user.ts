import { defineStore } from 'pinia'
import { login, logout } from '@/api/login'
import { getUserInfo } from '@/api/unifiedAuth'
import { getToken, setToken, removeToken } from '@/utils/auth'
import defaultAvatar from '@/assets/images/profile.jpg'

interface UserInfo {
  username: string
  password: string
  code?: string
  uuid?: string
}

interface UserState {
  token: string
  name: string
  avatar: string
  roles: string[]
  permissions: string[]
  userType: string
}

export const useUserStore = defineStore('user', {
  state: (): UserState => ({
    token: getToken() || '',
    name: '',
    avatar: '',
    roles: [],
    permissions: [],
    userType: ''
  }),

  actions: {
    /**
     * 登录
     */
    login(userInfo: UserInfo): Promise<void> {
      const username = userInfo.username.trim()
      const password = userInfo.password
      const code = userInfo.code
      const uuid = userInfo.uuid
      return new Promise((resolve, reject) => {
        login(username, password, code, uuid)
          .then((res: any) => {
            setToken(res.token)
            this.token = res.token
            resolve()
          })
          .catch(error => {
            reject(error)
          })
      })
    },

    /**
     * 获取用户信息
     */
    getInfo(): Promise<any> {
      return new Promise((resolve, reject) => {
        console.log('📋 开始获取用户信息...')
        // 使用统一的认证接口获取用户信息
        getUserInfo()
          .then((res: any) => {
            console.log('✅ 获取用户信息成功:', res)
            const data = res.data || res
            console.log('📊 data 对象:', data)
            console.log('📊 data.userType:', data.userType)
            console.log('📊 data.user:', data.user)
            console.log('📊 data.user.userType:', data.user?.userType)
            const user = data.user
            const avatar =
              user.avatar == '' || user.avatar == null
                ? defaultAvatar
                : (import.meta.env?.VITE_APP_BASE_API || '/dev-api') + user.avatar

            if (data.roles && data.roles.length > 0) {
              // 验证返回的roles是否是一个非空数组
              this.roles = data.roles
              this.permissions = data.permissions
              console.log('✅ 设置 roles:', this.roles)
            } else {
              this.roles = ['ROLE_DEFAULT']
              console.log('⚠️ roles 为空，使用默认值')
            }
            this.name = user.userName
            this.avatar = avatar
            this.userType = data.userType || user.userType || '00'
            // 确保 token 状态与 localStorage 同步
            const token = getToken()
            if (token && token !== this.token) {
              this.token = token
              console.log('✅ 更新 store 中的 token')
            }
            console.log(
              '✅ 用户信息设置完成: name =',
              this.name,
              ', avatar =',
              this.avatar,
              ', userType =',
              this.userType,
              ', token =',
              this.token ? '有' : '无'
            )
            resolve(res)
          })
          .catch(error => {
            console.error('❌ 获取用户信息失败:', error)
            reject(error)
          })
      })
    },

    /**
     * 退出系统
     */
    logOut(): Promise<void> {
      return new Promise((resolve, reject) => {
        logout()
          .then(() => {
            this.token = ''
            this.roles = []
            this.permissions = []
            removeToken()
            resolve()
          })
          .catch(error => {
            reject(error)
          })
      })
    }
  }
})
