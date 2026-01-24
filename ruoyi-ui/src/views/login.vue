<template>
  <div class="login">
    <el-form
      ref="loginRef"
      :model="loginForm"
      :rules="loginRules"
      class="login-form"
    >
      <h3 class="title">
        {{ title }}
      </h3>
      <el-form-item prop="username">
        <el-input
          v-model="loginForm.username"
          type="text"
          size="large"
          auto-complete="off"
          placeholder="账号"
        >
          <template #prefix>
            <svg-icon
              icon-class="user"
              class="el-input__icon input-icon"
            />
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          v-model="loginForm.password"
          :type="showPassword ? 'text' : 'password'"
          size="large"
          auto-complete="off"
          placeholder="密码"
          @keyup.enter="handleLogin"
        >
          <template #prefix>
            <svg-icon
              icon-class="password"
              class="el-input__icon input-icon"
            />
          </template>
          <template #suffix>
            <span
              class="password-eye"
              style="cursor: pointer; color: #909399"
              @click="showPassword = !showPassword"
            >
              <svg-icon :icon-class="showPassword ? 'eye-open' : 'eye'" />
            </span>
          </template>
        </el-input>
      </el-form-item>

      <!-- 验证码 -->
      <el-form-item
        v-if="captchaEnabled"
        prop="code"
      >
        <el-input
          v-model="loginForm.code"
          size="large"
          auto-complete="off"
          placeholder="验证码"
          style="width: 63%"
          clearable
          @keyup.enter="handleLogin"
        >
          <template #prefix>
            <svg-icon
              icon-class="validCode"
              class="el-input__icon input-icon"
            />
          </template>
        </el-input>
        <div class="login-code">
          <img
            :src="codeUrl"
            class="login-code-img"
            :style="{
              opacity: codeLoading ? 0.5 : 1,
              cursor: codeLoading ? 'not-allowed' : 'pointer'
            }"
            :title="codeLoading ? '验证码加载中...' : '点击刷新验证码'"
            @click="getCode"
          />
        </div>
      </el-form-item>

      <el-checkbox
        v-model="loginForm.rememberMe"
        style="margin: 0px 0px 25px 0px"
      >
        记住密码
      </el-checkbox>
      <el-form-item style="width: 100%">
        <el-button
          :loading="loading"
          size="large"
          type="primary"
          style="
            width: 100%;
            font-size: 16px;
            padding: 12px 0;
            font-weight: bold;
            box-shadow: 0 2px 12px 0 rgba(24, 144, 255, 0.3);
          "
          @click.prevent="handleLogin"
        >
          <span v-if="!loading">登 录</span>
          <span v-else>登 录 中...</span>
        </el-button>
        <div
          v-if="register"
          style="float: right"
        >
          <router-link
            class="link-type"
            :to="'/register'"
          >
            立即注册
          </router-link>
        </div>
      </el-form-item>
    </el-form>
    <!--  底部  -->
    <div class="el-login-footer">
      <span>Copyright © 2026 博客管理系统 All Rights Reserved.</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, getCurrentInstance, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getCodeImg } from '@/api/login'
import Cookies from 'js-cookie'
import { encrypt, decrypt } from '@/utils/jsencrypt'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

const title = import.meta.env?.VITE_APP_TITLE || '博客管理系统'
const userStore = useUserStore()
const route = useRoute()
const router = useRouter()
const { proxy } = getCurrentInstance()

const loginForm = ref({
  username: 'admin',
  password: 'admin123',
  rememberMe: false,
  code: '',
  uuid: '',
  captchaType: '',
  pointJson: '',
  token: ''
})

const loginRules = {
  username: [
    { required: true, trigger: 'blur', message: '请输入您的账号', whitespace: true },
    { min: 2, max: 20, trigger: 'blur', message: '账号长度在 2 到 20 个字符' }
  ],
  password: [
    { required: true, trigger: 'blur', message: '请输入您的密码', whitespace: true },
    { min: 5, max: 20, trigger: 'blur', message: '密码长度在 5 到 20 个字符' }
  ],
  code: [
    { required: true, trigger: 'blur', message: '请输入验证码' },
    { min: 1, max: 6, trigger: 'blur', message: '验证码长度为 1-6 个字符' }
  ]
}

const codeUrl = ref('')
const loading = ref(false)
// 验证码开关
const captchaEnabled = ref(true)
// 验证码加载状态
const codeLoading = ref(false)
// 注册开关
const register = ref(false)
const redirect = ref(undefined)
// 显示密码开关
const showPassword = ref(false)

// 设置 watch 监听器，Vue 3 会自动清理
watch(
  route,
  newRoute => {
    redirect.value = newRoute.query && newRoute.query.redirect
  },
  { immediate: true }
)

function handleLogin() {
  proxy.$refs.loginRef.validate(valid => {
    if (valid) {
      loading.value = true
      // 勾选了需要记住密码设置在 cookie 中设置记住用户名和密码
      if (loginForm.value.rememberMe) {
        Cookies.set('username', loginForm.value.username, { expires: 30 })
        Cookies.set('password', encrypt(loginForm.value.password), { expires: 30 })
        Cookies.set('rememberMe', loginForm.value.rememberMe, { expires: 30 })
      } else {
        // 否则移除
        Cookies.remove('username')
        Cookies.remove('password')
        Cookies.remove('rememberMe')
      }
      // 调用action的登录方法
      userStore
        .login(loginForm.value)
        .then(() => {
          const query = route.query
          const otherQueryParams = Object.keys(query).reduce((acc, cur) => {
            if (cur !== 'redirect') {
              acc[cur] = query[cur]
            }
            return acc
          }, {})
          // 使用 router.push 而不是 window.location.href，避免页面刷新导致 store 数据重置
          let targetPath
          if (redirect.value && redirect.value !== '/login') {
            targetPath = redirect.value
          } else {
            // 默认跳转到管理后台首页
            targetPath = '/admin/dashboard'
          }
          const queryString =
            Object.keys(otherQueryParams).length > 0
              ? '?' + new URLSearchParams(otherQueryParams).toString()
              : ''
          router.push(targetPath + queryString)
        })
        .catch(error => {
          loading.value = false
          // 显示详细的错误信息
          ElMessage.error(error.message || '登录失败，请检查账号密码或验证码是否正确')
          // 重新获取验证码
          if (captchaEnabled.value) {
            getCode()
          }
        })
    }
  })
}

function getCode() {
  codeLoading.value = true
  getCodeImg()
    .then(res => {
      captchaEnabled.value = res.captchaEnabled === undefined ? true : res.captchaEnabled
      if (captchaEnabled.value) {
        // 后端返回的是 jpg 格式，使用 image/jpeg
        codeUrl.value = 'data:image/jpeg;base64,' + res.img
        loginForm.value.uuid = res.uuid
        // 清空验证码输入框
        loginForm.value.code = ''
      }
    })
    .catch(error => {
      console.error('获取验证码失败:', error)
      ElMessage.error('获取验证码失败，请点击图片重试')
    })
    .finally(() => {
      codeLoading.value = false
    })
}

function getCookie() {
  const username = Cookies.get('username')
  const password = Cookies.get('password')
  const rememberMe = Cookies.get('rememberMe')
  loginForm.value = {
    username: username === undefined ? loginForm.value.username : username,
    password: password === undefined ? loginForm.value.password : decrypt(password),
    rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
  }
}

getCode()
getCookie()
</script>

<style lang="scss" scoped>
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-image: url('../assets/images/login-background.jpg');
  background-size: cover;
  padding: 20px;
}
.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: #707070;
}

.login-form {
  border-radius: 6px;
  background: #ffffff;
  width: 100%;
  max-width: 400px;
  padding: 25px 25px 5px 25px;
  z-index: 1;
  .el-input {
    height: 40px;
    input {
      height: 40px;
    }
  }
  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 0px;
  }
}

// 响应式设计，确保在移动设备上也能正常显示
@media screen and (max-width: 768px) {
  .login-form {
    padding: 20px 20px 5px 20px;
  }

  .title {
    font-size: 18px;
    margin-bottom: 20px;
  }

  .el-button {
    font-size: 14px !important;
    padding: 10px 0 !important;
  }
}

@media screen and (max-width: 480px) {
  .login {
    padding: 15px;
  }

  .login-form {
    padding: 15px 15px 5px 15px;
  }

  .el-input {
    height: 36px !important;
    input {
      height: 36px !important;
    }
  }
}
.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}
.login-code {
  width: 33%;
  height: 40px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}
.login-code-img {
  height: 40px;
  padding-left: 12px;
}
</style>
