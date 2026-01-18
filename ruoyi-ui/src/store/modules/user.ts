import { defineStore } from 'pinia'
import router from '@/router'
import { ElMessageBox } from 'element-plus'
import { login, logout, getInfo } from '@/api/login'
import { getToken, setToken, removeToken } from '@/utils/auth'
import { isHttp, isEmpty } from '@/utils/validate'
import defAva from '@/assets/images/profile.jpg'

interface UserInfo {
  username: string
  password: string
  code?: string
  uuid?: string
}

interface UserState {
  token: string
  id: string
  name: string
  nickName: string
  avatar: string
  roles: string[]
  permissions: string[]
}

const useUserStore = defineStore('user', {
  state: (): UserState => ({
    token: getToken() || '',
    id: '',
    name: '',
    nickName: '',
    avatar: '',
    roles: [],
    permissions: []
  }),
  actions: {
    // 登录
    login(userInfo: UserInfo): Promise<void> {
      const username = userInfo.username.trim()
      const password = userInfo.password
      const code = userInfo.code
      const uuid = userInfo.uuid
      return new Promise((resolve, reject) => {
        login(username, password, code, uuid)
          .then(res => {
            setToken(res.token)
            this.token = res.token
            resolve()
          })
          .catch(error => {
            reject(error)
          })
      })
    },
    // 获取用户信息
    getInfo(): Promise<any> {
      return new Promise((resolve, reject) => {
        getInfo()
          .then(res => {
            const user = res.user
            let avatar = user.avatar || ''
            if (!isHttp(avatar)) {
              avatar = isEmpty(avatar)
                ? defAva
                : (import.meta.env.VITE_APP_BASE_API as string) + avatar
            }
            if (res.roles && res.roles.length > 0) {
              // 验证返回的roles是否是一个非空数组
              this.roles = res.roles
              this.permissions = res.permissions
            } else {
              this.roles = ['ROLE_DEFAULT']
            }
            this.id = user.userId
            this.name = user.userName
            this.nickName = user.nickName
            this.avatar = avatar
            /* 初始密码提示 */
            if (res.isDefaultModifyPwd) {
              ElMessageBox.confirm('您的密码还是初始密码，请修改密码！', '安全提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
              })
                .then(() => {
                  router.push({ name: 'Profile', params: { activeTab: 'resetPwd' } })
                })
                .catch(() => {})
            }
            /* 过期密码提示 */
            if (!res.isDefaultModifyPwd && res.isPasswordExpired) {
              ElMessageBox.confirm('您的密码已过期，请尽快修改密码！', '安全提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
              })
                .then(() => {
                  router.push({ name: 'Profile', params: { activeTab: 'resetPwd' } })
                })
                .catch(() => {})
            }
            resolve(res)
          })
          .catch(error => {
            reject(error)
          })
      })
    },
    // 退出系统
    logOut(): Promise<void> {
      return new Promise((resolve, _reject) => {
        // 先清除本地token和状态，防止API返回401时触发无限循环
        this.token = ''
        this.roles = []
        this.permissions = []
        removeToken()

        // 调用logout API通知后端注销token（即使失败也继续执行）
        logout()
          .then(() => {})
          .catch(error => {
            console.error('Logout API调用失败:', error)
            // API调用失败也不影响退出流程
          })
          .finally(() => {
            // 延迟重定向，确保状态已更新
            setTimeout(() => {
              // 直接修改location，避免路由守卫的复杂逻辑
              window.location.replace('/login')
            }, 100)
            resolve()
          })
      })
    }
  }
})

export default useUserStore
