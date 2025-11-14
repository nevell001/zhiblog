import { defineConfig, loadEnv } from 'vite'
import path from 'path'
import createVitePlugins from './vite/plugins'

// 判断是否在 Docker 容器内运行
// 通过环境变量 DOCKER 来控制（docker-compose.yml 中设置）
const inDocker = process.env.DOCKER === 'true'

// 如果在容器内 → 用容器间网络访问后端
// 如果在本机开发 → 用 localhost 访问后端
const baseUrl = inDocker ? 'http://ruoyi-admin:8080' : 'http://localhost:8080'

// https://vitejs.dev/config/
export default defineConfig(({ mode, command }) => {
  const env = loadEnv(mode, process.cwd())
  const { VITE_APP_ENV } = env

  return {
    // 部署生产环境和开发环境下的URL
    base: VITE_APP_ENV === 'production' ? '/' : '/',
    plugins: createVitePlugins(env, command === 'build'),
    resolve: {
      // 设置路径别名
      alias: {
        '~': path.resolve(__dirname, './'),
        '@': path.resolve(__dirname, './src')
      },
      extensions: ['.mjs', '.js', '.ts', '.jsx', '.tsx', '.json', '.vue']
    },
    build: {
      sourcemap: command === 'build' ? false : 'inline',
      outDir: 'dist',
      assetsDir: 'assets',
      chunkSizeWarningLimit: 2000,
      rollupOptions: {
        output: {
          chunkFileNames: 'static/js/[name]-[hash].js',
          entryFileNames: 'static/js/[name]-[hash].js',
          assetFileNames: 'static/[ext]/[name]-[hash].[ext]'
        }
      }
    },
    server: {
      host: '0.0.0.0', // 绑定到所有网络接口
      port: 3000,
      open: false, // 不自动打开浏览器，在容器中会导致错误
      proxy: {
        // 接口代理 - RuoYi 默认 API 前缀
      '/dev-api': {
        target: baseUrl,
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/dev-api/, '')
      },
      // 代理系统管理接口
      '/system': {
        target: baseUrl,
        changeOrigin: true
      },
      // 代理博客前台接口
      '/blog': {
        target: baseUrl,
        changeOrigin: true
      }
      },
      // 解决 SPA 应用 history 模式下刷新404问题
      historyApiFallback: true
    },
    css: {
      postcss: {
        plugins: [
          {
            postcssPlugin: 'internal:charset-removal',
            AtRule: {
              charset: (atRule) => {
                if (atRule.name === 'charset') {
                  atRule.remove()
                }
              }
            }
          }
        ]
      }
    }
  }
})