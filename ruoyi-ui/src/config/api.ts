// 统一API接口配置
// 这里定义前后端统一的API接口地址

interface ApiConfig {
  baseURL: string
  blog: BlogApi
  admin: AdminApi
  common: CommonApi
}

interface BlogApi {
  article: ArticleApi
  category: CategoryApi
  tag: TagApi
  comment: CommentApi
  friendLink: FriendLinkApi
  setting: SettingApi
}

interface ArticleApi {
  list: string
  detail: string
  create: string
  update: string
  delete: string
  search: string
}

interface CategoryApi {
  list: string
  detail: string
  create: string
  update: string
  delete: string
}

interface TagApi {
  list: string
  detail: string
  create: string
  update: string
  delete: string
  cloud: string
}

interface CommentApi {
  list: string
  create: string
  delete: string
  reply: string
}

interface FriendLinkApi {
  list: string
  create: string
  update: string
  delete: string
}

interface SettingApi {
  get: string
  update: string
}

interface AdminApi {
  system: SystemApi
  monitor: MonitorApi
  tool: ToolApi
}

interface SystemApi {
  user: UserApi
  role: RoleApi
  menu: MenuApi
  dept: DeptApi
  post: PostApi
  dict: DictApi
  config: ConfigApi
  notice: NoticeApi
}

interface UserApi {
  list: string
  detail: string
  create: string
  update: string
  delete: string
  resetPwd: string
  changeStatus: string
}

interface RoleApi {
  list: string
  detail: string
  create: string
  update: string
  delete: string
  changeStatus: string
}

interface MenuApi {
  list: string
  detail: string
  create: string
  update: string
  delete: string
  tree: string
}

interface DeptApi {
  list: string
  detail: string
  create: string
  update: string
  delete: string
  tree: string
}

interface PostApi {
  list: string
  detail: string
  create: string
  update: string
  delete: string
}

interface DictApi {
  list: string
  detail: string
  create: string
  update: string
  delete: string
  dataList: string
  dataDetail: string
  dataCreate: string
  dataUpdate: string
  dataDelete: string
}

interface ConfigApi {
  list: string
  detail: string
  create: string
  update: string
  delete: string
  refreshCache: string
}

interface NoticeApi {
  list: string
  detail: string
  create: string
  update: string
  delete: string
}

interface MonitorApi {
  online: OnlineApi
  logininfor: LogininforApi
  operlog: OperlogApi
  job: JobApi
  cache: CacheApi
}

interface OnlineApi {
  list: string
  forceLogout: string
}

interface LogininforApi {
  list: string
  delete: string
  clean: string
}

interface OperlogApi {
  list: string
  delete: string
  clean: string
  detail: string
}

interface JobApi {
  list: string
  detail: string
  create: string
  update: string
  delete: string
  changeStatus: string
  run: string
  logList: string
  logDetail: string
}

interface CacheApi {
  getNames: string
  getKeys: string
  getValue: string
  clearCache: string
  clearCacheKey: string
}

interface ToolApi {
  gen: GenApi
}

interface GenApi {
  list: string
  detail: string
  create: string
  update: string
  delete: string
  preview: string
  download: string
  genCode: string
  sync: string
}

interface CommonApi {
  upload: string
  user: CommonUserApi
  auth: AuthApi
}

interface CommonUserApi {
  profile: string
  updatePwd: string
  updateAvatar: string
}

interface AuthApi {
  login: string
  logout: string
  register: string
  refresh: string
  getInfo: string
  getRouters: string
}

export const apiConfig: ApiConfig = {
  // 基础URL
  baseURL: process.env.NODE_ENV === 'production' ? '/prod-api' : '/dev-api',

  // 博客前台API
  blog: {
    // 文章相关
    article: {
      list: '/blog/article/list',
      detail: '/blog/article/',
      create: '/blog/article',
      update: '/blog/article',
      delete: '/blog/article/',
      search: '/blog/article/search'
    },

    // 分类相关
    category: {
      list: '/blog/category/list',
      detail: '/blog/category/',
      create: '/blog/category',
      update: '/blog/category',
      delete: '/blog/category/'
    },

    // 标签相关
    tag: {
      list: '/blog/tag/list',
      detail: '/blog/tag/',
      create: '/blog/tag',
      update: '/blog/tag',
      delete: '/blog/tag/',
      cloud: '/blog/tag/cloud'
    },

    // 评论相关
    comment: {
      list: '/blog/comment/list',
      create: '/blog/comment',
      delete: '/blog/comment/',
      reply: '/blog/comment/reply'
    },

    // 友链相关
    friendLink: {
      list: '/blog/friendLink/list',
      create: '/blog/friendLink',
      update: '/blog/friendLink',
      delete: '/blog/friendLink/'
    },

    // 博客设置
    setting: {
      get: '/blog/setting',
      update: '/blog/setting'
    }
  },

  // 后台管理API
  admin: {
    // 系统管理
    system: {
      // 用户管理
      user: {
        list: '/system/user/list',
        detail: '/system/user/',
        create: '/system/user',
        update: '/system/user',
        delete: '/system/user/',
        resetPwd: '/system/user/resetPwd',
        changeStatus: '/system/user/changeStatus'
      },

      // 角色管理
      role: {
        list: '/system/role/list',
        detail: '/system/role/',
        create: '/system/role',
        update: '/system/role',
        delete: '/system/role/',
        changeStatus: '/system/role/changeStatus'
      },

      // 菜单管理
      menu: {
        list: '/system/menu/list',
        detail: '/system/menu/',
        create: '/system/menu',
        update: '/system/menu',
        delete: '/system/menu/',
        tree: '/system/menu/treeselect'
      },

      // 部门管理
      dept: {
        list: '/system/dept/list',
        detail: '/system/dept/',
        create: '/system/dept',
        update: '/system/dept',
        delete: '/system/dept/',
        tree: '/system/dept/treeselect'
      },

      // 岗位管理
      post: {
        list: '/system/post/list',
        detail: '/system/post/',
        create: '/system/post',
        update: '/system/post',
        delete: '/system/post/'
      },

      // 字典管理
      dict: {
        list: '/system/dict/type/list',
        detail: '/system/dict/type/',
        create: '/system/dict/type',
        update: '/system/dict/type',
        delete: '/system/dict/type/',
        dataList: '/system/dict/data/list',
        dataDetail: '/system/dict/data/',
        dataCreate: '/system/dict/data',
        dataUpdate: '/system/dict/data',
        dataDelete: '/system/dict/data/'
      },

      // 参数设置
      config: {
        list: '/system/config/list',
        detail: '/system/config/',
        create: '/system/config',
        update: '/system/config',
        delete: '/system/config/',
        refreshCache: '/system/config/refreshCache'
      },

      // 通知公告
      notice: {
        list: '/system/notice/list',
        detail: '/system/notice/',
        create: '/system/notice',
        update: '/system/notice',
        delete: '/system/notice/'
      }
    },

    // 系统监控
    monitor: {
      // 在线用户
      online: {
        list: '/monitor/online/list',
        forceLogout: '/monitor/online/'
      },

      // 登录日志
      logininfor: {
        list: '/monitor/logininfor/list',
        delete: '/monitor/logininfor/',
        clean: '/monitor/logininfor/clean'
      },

      // 操作日志
      operlog: {
        list: '/monitor/operlog/list',
        delete: '/monitor/operlog/',
        clean: '/monitor/operlog/clean',
        detail: '/monitor/operlog/'
      },

      // 定时任务
      job: {
        list: '/monitor/job/list',
        detail: '/monitor/job/',
        create: '/monitor/job',
        update: '/monitor/job',
        delete: '/monitor/job/',
        changeStatus: '/monitor/job/changeStatus',
        run: '/monitor/job/run',
        logList: '/monitor/job/log/list',
        logDetail: '/monitor/job/log/'
      },

      // 缓存监控
      cache: {
        getNames: '/monitor/cache/getNames',
        getKeys: '/monitor/cache/getKeys',
        getValue: '/monitor/cache/getValue',
        clearCache: '/monitor/cache/clearCache',
        clearCacheKey: '/monitor/cache/clearCacheKey'
      }
    },

    // 系统工具
    tool: {
      // 代码生成
      gen: {
        list: '/tool/gen/list',
        detail: '/tool/gen/',
        create: '/tool/gen',
        update: '/tool/gen',
        delete: '/tool/gen/',
        preview: '/tool/gen/preview/',
        download: '/tool/gen/download/',
        genCode: '/tool/gen/genCode/',
        sync: '/tool/gen/sync/'
      }
    }
  },

  // 通用API
  common: {
    // 文件上传
    upload: '/common/upload',

    // 用户相关
    user: {
      profile: '/system/user/profile',
      updatePwd: '/system/user/profile/updatePwd',
      updateAvatar: '/system/user/profile/avatar'
    },

    // 权限相关
    auth: {
      login: '/auth/login',
      logout: '/auth/logout',
      register: '/auth/register',
      refresh: '/auth/refresh',
      getInfo: '/getInfo',
      getRouters: '/getRouters'
    }
  }
}

// API工具函数
export const apiUtils = {
  // 构建完整URL
  buildURL: (endpoint: string): string => {
    return apiConfig.baseURL + endpoint
  },

  // 构建带参数的URL
  buildURLWithParams: (endpoint: string, params: Record<string, any>): string => {
    let url = apiConfig.baseURL + endpoint
    if (params) {
      const queryString = Object.keys(params)
        .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
        .join('&')
      url += '?' + queryString
    }
    return url
  },

  // 获取API配置
  getApi: (module: string, submodule?: string, action?: string): string | null => {
    const config = apiConfig as any
    if (!config[module]) return null
    if (submodule && !config[module][submodule]) return null
    if (action && submodule && config[module][submodule][action]) {
      return apiConfig.baseURL + config[module][submodule][action]
    }
    if (submodule && !action) {
      return apiConfig.baseURL + config[module][submodule]
    }
    return apiConfig.baseURL + config[module]
  }
}

export default apiConfig
