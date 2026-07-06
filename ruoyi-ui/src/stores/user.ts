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
    async login(userInfo: UserInfo): Promise<void> {
      const username = userInfo.username.trim()
      const { password, code, uuid } = userInfo
      const res = await login(username, password, code, uuid)
      setToken(res.token)
      this.token = res.token
    },

    /**
     * 获取用户信息
     */
    async getInfo(): Promise<any> {
      const res = await getUserInfo()
      const data = res.data || res
      const user = data.user
      const avatar =
        user.avatar == '' || user.avatar == null
          ? defaultAvatar
          : (import.meta.env?.VITE_APP_BASE_API || '/dev-api') + user.avatar

      if (data.roles && data.roles.length > 0) {
        this.roles = data.roles
        this.permissions = data.permissions
      } else {
        this.roles = ['ROLE_DEFAULT']
      }
      this.name = user.userName
      this.avatar = avatar
      this.userType = data.userType || user.userType || '00'

      const token = getToken()
      if (token && token !== this.token) {
        this.token = token
      }
      return res
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
