import { defineStore } from 'pinia'
import { login, logout, getInfo } from '@/api/login'
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
}

export const useUserStore = defineStore('user', {
  state: (): UserState => ({
    token: getToken() || '',
    name: '',
    avatar: '',
    roles: [],
    permissions: []
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
        getInfo()
          .then((res: any) => {
            const user = res.user
            const avatar =
              user.avatar == '' || user.avatar == null
                ? defaultAvatar
                : process.env.VUE_APP_BASE_API + user.avatar

            if (res.roles && res.roles.length > 0) {
              // 验证返回的roles是否是一个非空数组
              this.roles = res.roles
              this.permissions = res.permissions
            } else {
              this.roles = ['ROLE_DEFAULT']
            }
            this.name = user.userName
            this.avatar = avatar
            resolve(res)
          })
          .catch(error => {
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
