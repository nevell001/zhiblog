<template>
  <div class="app-container">
    <el-card
      shadow="never"
      class="blog-setting-card"
    >
      <template #header>
        <div class="card-header">
          <span class="card-title">{{ tabTitle }}</span>
          <div class="card-extra">
            <el-button
              type="primary"
              size="small"
              :loading="loading"
              @click="saveAllSettings"
            >
              <i class="el-icon-check"></i>
              保存所有设置
            </el-button>
            <el-button
              type="warning"
              size="small"
              :loading="loading"
              @click="resetSettings"
            >
              <i class="el-icon-refresh"></i>
              重置设置
            </el-button>
          </div>
        </div>
      </template>

      <el-tabs
        v-model="activeTab"
        class="blog-setting-tabs"
      >
        <!-- 站点信息 -->
        <el-tab-pane
          label="站点信息"
          name="basic"
        >
          <el-form
            ref="basicForm"
            :model="settingsMap"
            label-width="120px"
          >
            <el-alert
              title="站点信息将在博客首页、关于页面等位置显示"
              type="info"
              :closable="false"
              style="margin-bottom: 20px"
            />
            <el-form-item
              label="博客名称"
              prop="blog_name"
            >
              <el-input
                v-model="settingsMap.blog_name"
                placeholder="请输入博客名称"
                maxlength="50"
                show-word-limit
              />
            </el-form-item>
            <el-form-item
              label="博客描述"
              prop="blog_desc"
            >
              <el-input
                v-model="settingsMap.blog_desc"
                type="textarea"
                :rows="3"
                placeholder="请输入博客描述"
                maxlength="200"
                show-word-limit
              />
            </el-form-item>
            <el-form-item
              label="博客作者"
              prop="blog_author"
            >
              <el-input
                v-model="settingsMap.blog_author"
                placeholder="请输入博客作者"
                maxlength="50"
                show-word-limit
              />
            </el-form-item>
            <el-form-item
              label="版权信息"
              prop="blog_copyright"
            >
              <el-input
                v-model="settingsMap.blog_copyright"
                placeholder="请输入版权信息，如：© 2025 My Blog"
                maxlength="200"
                show-word-limit
              />
            </el-form-item>
            <el-form-item
              label="备案信息"
              prop="blog_beian"
            >
              <el-input
                v-model="settingsMap.blog_beian"
                placeholder="请输入ICP备案信息，如：京ICP备12345678号"
                maxlength="200"
                show-word-limit
              />
            </el-form-item>
            <!-- 博客头像设置已移除，直接使用账号头像 -->
            <!--
            <el-form-item
              label="博主头像"
              prop="blog_avatar"
            >
              <div class="avatar-upload">
                <el-upload
                  class="avatar-uploader"
                  :action="uploadAvatarUrl"
                  :headers="headers"
                  :on-success="handleAvatarUploadSuccess"
                  :before-upload="handleAvatarBeforeUpload"
                  :show-file-list="false"
                >
                  <img
                    v-if="settingsMap.blog_avatar"
                    :src="processedAvatarUrl"
                    class="avatar"
                  />
                  <i
                    v-else
                    class="el-icon-plus avatar-uploader-icon"
                  ></i>
                </el-upload>
                <el-input
                  v-model="settingsMap.blog_avatar"
                  class="avatar-input"
                  placeholder="或直接输入头像URL"
                />
                <div
                  class="upload-tip"
                  style="
                    font-size: 12px;
                    color: var(--el-text-color-placeholder, #999);
                    margin-top: 5px;
                  "
                >
                  🚀
                  <strong>基于Thumbnailator专业处理</strong>
                  ：智能压缩为200x200像素，质量优化
                  <br />
                  💡 支持 JPG/PNG/GIF 格式，最大10MB，自动居中裁剪，高性能
                  <br />
                  📦 文件存储，数据库仅存URL，性能卓越
                </div>
              </div>
            </el-form-item>
            -->
          </el-form>
        </el-tab-pane>

        <!-- 功能设置 -->
        <el-tab-pane
          label="功能设置"
          name="features"
        >
          <el-form
            ref="featuresForm"
            :model="settingsMap"
            label-width="120px"
          >
            <!-- 互动功能 -->
            <el-divider content-position="left">
              <span style="font-weight: 600; color: var(--el-color-primary, #409eff)">
                互动功能
              </span>
            </el-divider>
            <el-form-item
              label="评论功能"
              prop="comment_enabled"
            >
              <el-switch v-model="settingsMap.comment_enabled" />
              <span style="margin-left: 10px; color: var(--el-text-color-secondary, #909399); font-size: 12px">
                允许访客在文章下方发表评论
              </span>
            </el-form-item>
            <el-form-item
              v-if="settingsMap.comment_enabled === 'true' || settingsMap.comment_enabled === true"
              label="评论审核"
              prop="comment_review"
            >
              <el-switch v-model="settingsMap.comment_review" />
              <span style="margin-left: 10px; color: var(--el-text-color-secondary, #909399); font-size: 12px">
                新评论需要管理员审核后才能显示
              </span>
            </el-form-item>
            <el-form-item
              label="点赞功能"
              prop="like_enabled"
            >
              <el-switch v-model="settingsMap.like_enabled" />
              <span style="margin-left: 10px; color: var(--el-text-color-secondary, #909399); font-size: 12px">
                允许访客为文章点赞
              </span>
            </el-form-item>

            <!-- 内容功能 -->
            <el-divider content-position="left">
              <span style="font-weight: 600; color: var(--el-color-primary, #409eff)">
                内容功能
              </span>
            </el-divider>
            <el-form-item
              label="浏览统计"
              prop="view_count_enabled"
            >
              <el-switch v-model="settingsMap.view_count_enabled" />
              <span style="margin-left: 10px; color: var(--el-text-color-secondary, #909399); font-size: 12px">
                统计文章浏览次数
              </span>
            </el-form-item>
            <el-form-item
              label="分享功能"
              prop="share_enabled"
            >
              <el-switch v-model="settingsMap.share_enabled" />
              <span style="margin-left: 10px; color: var(--el-text-color-secondary, #909399); font-size: 12px">
                允许访客分享文章到社交媒体
              </span>
            </el-form-item>
            <el-form-item
              label="搜索功能"
              prop="search_enabled"
            >
              <el-switch v-model="settingsMap.search_enabled" />
              <span style="margin-left: 10px; color: var(--el-text-color-secondary, #909399); font-size: 12px">
                启用文章搜索功能
              </span>
            </el-form-item>

            <!-- 界面设置 -->
            <el-divider content-position="left">
              <span style="font-weight: 600; color: var(--el-color-primary, #409eff)">
                界面设置
              </span>
            </el-divider>
            <el-form-item
              label="显示侧边栏"
              prop="sidebar_enabled"
            >
              <el-switch v-model="settingsMap.sidebar_enabled" />
              <span style="margin-left: 10px; color: var(--el-text-color-secondary, #909399); font-size: 12px">
                在博客首页显示侧边栏
              </span>
            </el-form-item>
            <el-form-item
              label="显示底部"
              prop="footer_enabled"
            >
              <el-switch v-model="settingsMap.footer_enabled" />
              <span style="margin-left: 10px; color: var(--el-text-color-secondary, #909399); font-size: 12px">
                在博客页面底部显示页脚信息
              </span>
            </el-form-item>
            <el-form-item
              label="显示版权"
              prop="copyright_enabled"
            >
              <el-switch v-model="settingsMap.copyright_enabled" />
              <span style="margin-left: 10px; color: var(--el-text-color-secondary, #909399); font-size: 12px">
                在底部显示版权信息
              </span>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 个人信息 -->
        <el-tab-pane
          label="个人信息"
          name="author"
        >
          <el-form
            ref="authorForm"
            :model="settingsMap"
            label-width="120px"
          >
            <el-alert
              title="个人信息将在关于页面和博客侧边栏显示"
              type="info"
              :closable="false"
              style="margin-bottom: 20px"
            />
            <!-- 基本信息 -->
            <el-divider content-position="left">
              <span style="font-weight: 600; color: var(--el-color-primary, #409eff)">
                基本信息
              </span>
            </el-divider>
            <el-form-item
              label="作者职位"
              prop="author_title"
            >
              <el-input
                v-model="settingsMap.author_title"
                placeholder="请输入作者职位，如：全栈开发工程师"
                maxlength="50"
                show-word-limit
              />
            </el-form-item>
            <el-form-item
              label="个人简介"
              prop="author_bio"
            >
              <el-input
                v-model="settingsMap.author_bio"
                type="textarea"
                :rows="3"
                placeholder="请输入个人简介"
                maxlength="500"
                show-word-limit
              />
            </el-form-item>
            <el-form-item
              label="位置信息"
              prop="author_location"
            >
              <el-input
                v-model="settingsMap.author_location"
                placeholder="请输入位置信息，如：中国·北京"
                maxlength="100"
                show-word-limit
              />
            </el-form-item>

            <!-- 联系方式 -->
            <el-divider content-position="left">
              <span style="font-weight: 600; color: var(--el-color-primary, #409eff)">
                联系方式
              </span>
            </el-divider>
            <el-form-item
              label="联系邮箱"
              prop="blog_email"
            >
              <el-input
                v-model="settingsMap.blog_email"
                placeholder="请输入联系邮箱"
                maxlength="100"
                show-word-limit
              />
            </el-form-item>

            <!-- 社交媒体 -->
            <el-divider content-position="left">
              <span style="font-weight: 600; color: var(--el-color-primary, #409eff)">
                社交媒体
              </span>
            </el-divider>
            <el-form-item
              label="GitHub地址"
              prop="github_url"
            >
              <el-input
                v-model="settingsMap.github_url"
                placeholder="请输入GitHub地址"
                maxlength="200"
                show-word-limit
              >
                <template #prepend>
                  <el-icon><LinkIcon /></el-icon>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item
              label="微博地址"
              prop="weibo_url"
            >
              <el-input
                v-model="settingsMap.weibo_url"
                placeholder="请输入微博地址"
                maxlength="200"
                show-word-limit
              >
                <template #prepend>
                  <el-icon><LinkIcon /></el-icon>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item
              label="个人网站"
              prop="personal_website"
            >
              <el-input
                v-model="settingsMap.personal_website"
                placeholder="请输入个人网站地址"
                maxlength="200"
                show-word-limit
              >
                <template #prepend>
                  <el-icon><LinkIcon /></el-icon>
                </template>
              </el-input>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 关于页面 -->
        <el-tab-pane
          label="关于页面"
          name="other"
        >
          <el-form
            ref="otherForm"
            :model="settingsMap"
            label-width="120px"
          >
            <el-alert
              title="关于页面内容将在博客的关于页面显示，支持富文本编辑"
              type="info"
              :closable="false"
              style="margin-bottom: 20px"
            />
            <el-form-item
              label="关于页面内容"
              prop="about_content"
            >
              <editor
                v-model="settingsMap.about_content"
                :min-height="400"
              />
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>

      <!-- 图片压缩功能介绍 -->
      <el-card
        shadow="never"
        style="margin-top: 20px; background: var(--el-bg-color-overlay, #ffffff)"
      >
        <template #header>
          <div style="display: flex; align-items: center">
            <span style="color: var(--el-color-primary, #409eff); font-weight: bold">
              🎨 图片压缩功能
            </span>
            <el-tag
              type="success"
              size="small"
              style="margin-left: 10px"
            >
              已启用
            </el-tag>
          </div>
        </template>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="compress-feature">
              <h4>👤 头像压缩</h4>
              <p>
                <strong>200×200正方形</strong>
                | 质量90% | 自动居中裁剪
              </p>
              <div class="feature-example">
                <div class="example-avatar"></div>
                <span>博主头像、用户头像</span>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="compress-feature">
              <h4>🖼️ 缩略图压缩</h4>
              <p>
                <strong>400×400最大尺寸</strong>
                | 质量80% | 保持宽高比
              </p>
              <div class="feature-example">
                <div class="example-thumbnail"></div>
                <span>二维码、图片预览</span>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="compress-feature">
              <h4>📦 智能压缩</h4>
              <p>
                <strong>自适应压缩策略</strong>
                | 根据文件大小 | 高质量压缩
              </p>
              <div class="feature-example">
                <div class="example-smart"></div>
                <span>文章配图、通用图片</span>
              </div>
            </div>
          </el-col>
        </el-row>
        <div
          style="
            margin-top: 15px;
            text-align: center;
            font-size: 12px;
            color: var(--el-text-color-regular, #666);
          "
        >
          基于
          <strong>Thumbnailator</strong>
          专业图片处理库 | 压缩率可达60-80% | 支持JPG/PNG/GIF
        </div>
      </el-card>
    </el-card>
  </div>
</template>

<script setup name="BlogSetting">
import { ref, reactive, computed, onMounted, getCurrentInstance, watch } from 'vue'
import { useSettingsStore } from '@/stores/settings'
import {
  ElMessage,
  ElButton,
  ElCard,
  ElTabs,
  ElTabPane,
  ElForm,
  ElFormItem,
  ElInput,
  ElInputNumber,
  ElSwitch,
  ElColorPicker,
  ElRadioGroup,
  ElRadio,
  ElDatePicker,
  ElUpload,
  ElRow,
  ElCol,
  ElAlert,
  ElDivider
} from 'element-plus'
import { Link as LinkIcon } from '@element-plus/icons-vue'
import {
  listSetting,
  getSetting,
  updateSetting,
  updateSettingValueByKey,
  addSetting,
  getConfigByKey,
  delSetting
} from '@/api/admin/blog/setting'
import { clearBlogCache } from '@/api/blog/setting'
import { useBlogSettingsStore } from '@/stores/blogSettings'
import { processAvatarUrl } from '@/api/blog/avatar'
import { getToken } from '@/utils/auth'

const { proxy } = getCurrentInstance()

// 头像上传相关
const baseApi = import.meta.env?.VITE_APP_BASE_API || '/dev-api'
const baseUrl = baseApi
const uploadAvatarUrl = baseUrl + '/common/upload/avatar'
const uploadThumbnailUrl = baseUrl + '/common/upload/thumbnail'
const headers = ref({ Authorization: 'Bearer ' + getToken() })

// 初始化博客设置全局状态
const blogSettingsStore = useBlogSettingsStore()
const settingsStore = useSettingsStore()

// 响应式数据
const loading = ref(false)
const activeTab = ref('basic')
const settingsMap = ref({})
const originalSettings = ref({})

// 计算属性：根据当前标签页返回对应的标题
const tabTitle = computed(() => {
  const titleMap = {
    basic: '站点信息',
    features: '功能设置',
    author: '个人信息',
    other: '关于页面'
  }
  const title = titleMap[activeTab.value] || '博客设置管理'
  console.log('🏷️ tabTitle计算:', { activeTab: activeTab.value, title })
  return title
})

// 监听activeTab变化，用于调试
watch(activeTab, (newVal, oldVal) => {
  console.log('🔄 activeTab变化:', { oldVal, newVal, tabTitle: tabTitle.value })
})

// 监听tabTitle变化，确保标题正确更新
watch(tabTitle, (newVal) => {
  console.log('📝 tabTitle更新:', newVal)
  // 更新页面标题
  settingsStore.setTitle(newVal)
})

// 计算属性：处理头像URL，确保与前端首页显示一致
const processedAvatarUrl = computed(() => {
  const avatarUrl = settingsMap.value.blog_avatar
  const processedUrl = processAvatarUrl(avatarUrl)
  console.log('🖼️ 后台头像URL处理:', {
    original: avatarUrl,
    processed: processedUrl
  })
  return processedUrl
})

// 微信二维码处理已移除

/**
 * 获取所有博客设置
 */
async function getAllSettings() {
  loading.value = true
  try {
    console.log('🚀 开始强制刷新获取博客设置...')

    // 强制清除可能的缓存，使用多重策略
    const timestamp = new Date().getTime()
    const randomNonce = Math.random().toString(36).substring(7)
    const cacheBuster = `${timestamp}_${randomNonce}`

    console.log(`🔄 使用缓存破坏器: ${cacheBuster}`)

    // 尝试多种查询策略来获取博客设置
    let allSettings = []
    const queryStrategies = []

    // 策略1: 标准查询，大页面大小
    queryStrategies.push({
      name: '标准查询(大页面)',
      params: { _t: cacheBuster, pageSize: 200, pageNum: 1 }
    })

    // 策略2: 查询包含blog_前缀的配置
    queryStrategies.push({
      name: '博客配置查询',
      params: { _t: cacheBuster, configKey: 'blog_', pageSize: 200 }
    })

    // 策略3: 查询所有可能的博客相关配置键
    const blogKeys = [
      'blog_name',
      'blog_desc',
      'blog_author',
      'blog_email',
      'blog_url',
      'blog_start_time',
      'blog_avatar',
      'blog_signature',
      'blog_keywords',
      'blog_copyright',
      'blog_beian',
      'seo_title',
      'seo_description',
      'seo_canonical_url',
      'seo_robots',
      'seo_favicon',
      'theme_color',
      'header_background',
      'sidebar_style',
      'comment_enabled',
      'comment_review',
      'like_enabled',
      'view_count_enabled',
      'share_enabled',
      'search_enabled',
      'sidebar_enabled',
      'footer_enabled',
      'copyright_enabled',
      'page_size',
      'hot_article_count',
      'recent_comment_count',
      'greeting_message',
      'about_content',
      'author_title',
      'author_bio',
      'github_url',
      'weibo_url',
      'wechat_qr',
      'author_location',
      'personal_website'
    ]

    // 策略4: 分别查询关键的博客设置
    for (const key of blogKeys) {
      queryStrategies.push({
        name: `单独查询: ${key}`,
        params: { _t: cacheBuster, configKey: key, pageSize: 1 }
      })
    }

    // 执行优化的查询策略 - 减少API调用
    let successfulQueries = 0
    let totalFound = 0

    // 策略1: 先尝试标准查询
    try {
      console.log('📡 执行标准查询(大页面)')
      const response = await listSetting(queryStrategies[0].params)
      const settingList = response.rows || response.data || []

      if (settingList.length > 0) {
        successfulQueries++
        totalFound += settingList.length

        const foundKeys = settingList.map(s => s.configKey).filter(Boolean)
        console.log(`✅ 标准查询找到 ${settingList.length} 项设置:`, foundKeys)

        allSettings.push(...settingList)
      }
    } catch (error) {
      console.warn('❌ 标准查询失败:', error.message)
    }

    // 如果标准查询没有找到关键的博客设置，才执行单独查询
    const loadCriticalKeys = ['blog_start_time', 'blog_avatar', 'blog_signature']
    const foundCriticalKeys = allSettings
      .map(s => s.configKey)
      .filter(key => loadCriticalKeys.includes(key))

    console.log(
      `📊 检查关键设置: 已找到 ${foundCriticalKeys.length}/3 关键设置:`,
      foundCriticalKeys
    )

    if (foundCriticalKeys.length < 3) {
      console.log('🔍 关键设置缺失，执行针对性查询...')

      // 只查询缺失的关键设置，减少API调用
      const missingKeys = loadCriticalKeys.filter(key => !foundCriticalKeys.includes(key))
      const batchQuery = {
        _t: cacheBuster,
        configKey: missingKeys.join(','), // 假设API支持批量查询
        pageSize: 50
      }

      try {
        console.log('📡 批量查询缺失的关键设置:', missingKeys)
        const batchResponse = await listSetting(batchQuery)
        const batchSettings = batchResponse.rows || batchResponse.data || []

        if (batchSettings.length > 0) {
          successfulQueries++
          totalFound += batchSettings.length

          const batchFoundKeys = batchSettings.map(s => s.configKey).filter(Boolean)
          console.log(`✅ 批量查询找到 ${batchSettings.length} 项关键设置:`, batchFoundKeys)

          // 合并并去重
          batchSettings.forEach(setting => {
            if (setting.configKey) {
              const existingIndex = allSettings.findIndex(s => s.configKey === setting.configKey)
              if (existingIndex >= 0) {
                allSettings[existingIndex] = setting
                console.log(`🔄 更新已存在的设置: ${setting.configKey}`)
              } else {
                allSettings.push(setting)
                console.log(`➕ 添加新设置: ${setting.configKey} = ${setting.configValue}`)
              }
            }
          })
        }
      } catch (batchError) {
        console.warn('❌ 批量查询失败，回退到单独查询:', batchError.message)

        // 回退策略：使用并行查询来提高速度
        if (missingKeys.length > 0) {
          console.log('📡 并行查询缺失的关键设置...')
          const parallelQueries = missingKeys.map(async key => {
            try {
              console.log(`📡 并行查询: ${key}`)
              const singleResponse = await listSetting({
                _t: cacheBuster,
                configKey: key,
                pageSize: 1
              })

              const singleSettings = singleResponse.rows || singleResponse.data || []
              if (singleSettings.length > 0) {
                console.log(`➕ 并行查询找到设置: ${key}`)
                return singleSettings[0] // 返回找到的设置项
              }
            } catch (singleError) {
              console.warn(`❌ 并行查询 ${key} 失败:`, singleError.message)
            }
            return null
          })

          // 等待所有并行查询完成
          const parallelResults = await Promise.all(parallelQueries)
          const validResults = parallelResults.filter(result => result !== null)

          successfulQueries += validResults.length
          totalFound += validResults.length
          allSettings.push(...validResults)

          console.log(`✅ 并行查询完成，找到 ${validResults.length} 项设置`)
        }
      }
    }

    console.log(
      `📊 查询总结: ${successfulQueries}/${queryStrategies.length} 个策略成功，总共找到 ${allSettings.length} 项设置`
    )

    // 如果还是没有找到博客设置，尝试强制刷新和延迟重试
    if (allSettings.length <= 10) {
      console.log('⚠️ 找到的设置数量过少，尝试强制刷新...')

      // 减少延迟时间到500毫秒，提高响应速度
      await new Promise(resolve => setTimeout(resolve, 500))

      try {
        const retryResponse = await listSetting({
          _t: new Date().getTime(),
          pageSize: 500,
          pageNum: 1
        })

        const retrySettings = retryResponse.rows || retryResponse.data || []
        console.log(`🔄 重试查询找到 ${retrySettings.length} 项设置`)

        if (retrySettings.length > allSettings.length) {
          allSettings = retrySettings
          console.log('✅ 重试查询找到更多设置，已更新')
        }
      } catch (retryError) {
        console.warn('重试查询也失败:', retryError.message)
      }
    }

    console.log(
      '📋 最终合并的设置列表 (共 ' + allSettings.length + ' 项):',
      allSettings.map(s => ({
        key: s.configKey,
        value: s.configValue,
        type: typeof s.configValue
      }))
    )

    // 将设置项转换为Map格式，并处理特殊类型
    const settings = {}
    allSettings.forEach(setting => {
      if (setting.configKey) {
        let value = setting.configValue
        const originalType = typeof value

        // 处理布尔值转换
        if (value === 'true') {
          value = true
          console.log(`🔄 布尔转换 ${setting.configKey}: 'true' → true`)
        } else if (value === 'false') {
          value = false
          console.log(`🔄 布尔转换 ${setting.configKey}: 'false' → false`)
        }

        // 处理日期类型转换
        if (setting.configKey === 'blog_start_time' && value && typeof value === 'string') {
          // 将YYYY-MM-DD格式的字符串转换为Date对象
          const dateValue = new Date(value)
          if (!isNaN(dateValue.getTime())) {
            value = dateValue
            console.log(
              `📅 日期转换 ${setting.configKey}: '${setting.configValue}' → Date(${value.toISOString()})`
            )
          } else {
            console.warn(`⚠️ 无效的日期格式 ${setting.configKey}: '${value}'`)
            value = null
          }
        }

        // 验证和处理头像数据 - 不再支持Base64格式
        if (setting.configKey === 'blog_avatar') {
          if (value && value.length > 500) {
            console.warn(`⚠️ 数据库中的头像数据过长 (${value.length} 字符)，清空并要求用户重新上传`)
            value = ''
          } else if (value && value.startsWith('data:image/')) {
            console.warn('⚠️ 检测到Base64格式头像，不再支持，清空并要求用户重新上传')
            value = ''
          }
        }

        settings[setting.configKey] = value

        // 为关键字段添加详细调试信息
        if (
          [
            'blog_name',
            'blog_desc',
            'blog_author',
            'blog_email',
            'blog_url',
            'blog_start_time',
            'blog_avatar',
            'blog_signature'
          ].includes(setting.configKey)
        ) {
          console.log(`🔍 关键字段 ${setting.configKey}:`, {
            value: value,
            valueType: typeof value,
            originalValue: setting.configValue,
            originalType: originalType,
            isValid: value !== null && value !== undefined && value !== ''
          })
        }
      }
    })

    console.log('🗂️ 最终处理后的设置对象:', settings)

    // 设置默认值，确保所有必要的设置项都有值，并确保数值类型正确
    const defaultSettings = {
      blog_name: '我的博客',
      blog_desc: '欢迎来到我的博客',
      blog_author: '博主',
      blog_email: '',
      blog_url: '',
      blog_start_time: null, // 不设置默认值，让用户自行选择
      blog_avatar: '',
      blog_signature: '',
      seo_title: '',
      seo_description: '',
      blog_keywords: '',
      seo_canonical_url: '',
      seo_robots: 'index,follow',
      seo_favicon: '',
      theme_color: '#409EFF',
      header_background: '#304156',
      sidebar_style: 'dark',
      blog_copyright: '',
      blog_beian: '',
      comment_enabled: true,
      comment_review: true,
      like_enabled: true,
      view_count_enabled: true,
      share_enabled: true,
      search_enabled: true,
      sidebar_enabled: true,
      footer_enabled: true,
      copyright_enabled: true,
      page_size: 10, // 确保数值类型
      hot_article_count: 5, // 确保数值类型
      recent_comment_count: 5, // 确保数值类型
      greeting_message: '欢迎来到我的博客！',
      about_content: '',
      // 社交链接
      github_url: '',
      weibo_url: '',
      wechat_qr: '',
      author_title: '',
      author_bio: '',
      author_location: '',
      personal_website: ''
    }

    // 合并默认设置和从服务器获取的设置（服务器设置优先）
    const mergedSettings = { ...defaultSettings }

    // 统计从服务器获取的设置数量
    const serverSettingKeys = Object.keys(settings)
    const criticalKeys = ['blog_start_time', 'blog_avatar', 'blog_signature']
    const retrievedCriticalKeys = serverSettingKeys.filter(key => criticalKeys.includes(key))

    console.log(`📊 设置统计: 从服务器获取 ${serverSettingKeys.length} 项设置`)
    console.log(
      `🔑 关键键统计: 找到 ${retrievedCriticalKeys.length}/3 项关键设置:`,
      retrievedCriticalKeys
    )
    console.log('📋 服务器设置键:', serverSettingKeys)

    // 用服务器获取的设置覆盖默认设置
    Object.keys(settings).forEach(key => {
      if (settings[key] !== undefined) {
        // 即使是 null 或空字符串也要保留，因为有些字段可能就是空的
        mergedSettings[key] = settings[key]

        // 对数值类型字段进行类型转换
        if (['page_size', 'hot_article_count', 'recent_comment_count'].includes(key)) {
          mergedSettings[key] = Number(settings[key]) || defaultSettings[key]
        }

        // 为关键字段添加详细日志
        if (criticalKeys.includes(key)) {
          console.log(`✅ 应用关键设置 ${key}:`, {
            value: settings[key],
            valueType: typeof settings[key],
            isEmpty: settings[key] === '' || settings[key] === null || settings[key] === undefined
          })
        }
      }
    })

    // 验证关键字段是否正确加载
    console.log('🔍 关键字段最终验证:')
    criticalKeys.forEach(key => {
      const finalValue = mergedSettings[key]
      console.log(`  ${key}:`, {
        value: finalValue,
        type: typeof finalValue,
        isEmpty: finalValue === '' || finalValue === null || finalValue === undefined,
        fromServer: serverSettingKeys.includes(key)
      })
    })

    // 特别处理头像URL，确保格式正确
    if (mergedSettings.blog_avatar) {
      mergedSettings.blog_avatar = validateAvatarUrl(mergedSettings.blog_avatar)
    }
    if (mergedSettings.wechat_qr) {
      mergedSettings.wechat_qr = validateAvatarUrl(mergedSettings.wechat_qr)
    }

    // 保存设置到响应式数据
    settingsMap.value = mergedSettings
    // 保存原始设置用于重置（转换为存储格式以便比较）
    originalSettings.value = {}
    Object.keys(mergedSettings).forEach(key => {
      let value = mergedSettings[key]
      let originalType = typeof value
      if (value instanceof Date) {
        // 将日期转换为YYYY-MM-DD格式
        value = value.toISOString().split('T')[0]
        originalType = 'Date->' + typeof value
      } else if (typeof value === 'boolean') {
        // 将布尔值转换为字符串
        value = value.toString()
        originalType = 'boolean->' + typeof value
      } else if (typeof value === 'number') {
        // 将数字转换为字符串
        value = value.toString()
        originalType = 'number->' + typeof value
      }
      originalSettings.value[key] = value

      // 对关键字段添加调试信息
      if (
        [
          'blog_name',
          'blog_desc',
          'blog_author',
          'blog_email',
          'blog_url',
          'blog_start_time',
          'blog_avatar',
          'blog_signature'
        ].includes(key)
      ) {
        console.log(`原始设置 ${key} (${originalType}):`, value)
      }
    })
  } catch (error) {
    console.error('获取设置失败:', error)
    // 如果获取失败，使用默认设置
    const defaultSettings = {
      blog_name: '我的博客',
      blog_desc: '欢迎来到我的博客',
      blog_author: '博主',
      blog_email: '',
      blog_url: '',
      blog_start_time: null, // 不设置默认值，让用户自行选择
      blog_avatar: '',
      blog_signature: '',
      seo_title: '',
      seo_description: '',
      blog_keywords: '',
      seo_canonical_url: '',
      seo_robots: 'index,follow',
      seo_favicon: '',
      theme_color: '#409EFF',
      header_background: '#304156',
      sidebar_style: 'dark',
      blog_copyright: '',
      blog_beian: '',
      comment_enabled: true,
      comment_review: true,
      like_enabled: true,
      view_count_enabled: true,
      share_enabled: true,
      search_enabled: true,
      sidebar_enabled: true,
      footer_enabled: true,
      copyright_enabled: true,
      page_size: 10,
      hot_article_count: 5,
      recent_comment_count: 5,
      greeting_message: '欢迎来到我的博客！',
      about_content: '',
      // 社交链接
      github_url: '',
      weibo_url: '',
      wechat_qr: ''
    }

    // 特别处理默认头像URL
    defaultSettings.blog_avatar = validateAvatarUrl(defaultSettings.blog_avatar)
    defaultSettings.wechat_qr = validateAvatarUrl(defaultSettings.wechat_qr)

    settingsMap.value = defaultSettings
    // 保存原始设置用于重置（转换为存储格式以便比较）
    originalSettings.value = {}
    Object.keys(defaultSettings).forEach(key => {
      let value = defaultSettings[key]
      let originalType = typeof value
      if (value instanceof Date) {
        // 将日期转换为YYYY-MM-DD格式
        value = value.toISOString().split('T')[0]
        originalType = 'Date->' + typeof value
      } else if (typeof value === 'boolean') {
        // 将布尔值转换为字符串
        value = value.toString()
        originalType = 'boolean->' + typeof value
      } else if (typeof value === 'number') {
        // 将数字转换为字符串
        value = value.toString()
        originalType = 'number->' + typeof value
      } else if (value === null || value === undefined) {
        // 将 null/undefined 转换为空字符串
        value = ''
        originalType = 'null/undef->string'
      }
      originalSettings.value[key] = value

      // 对关键字段添加调试信息
      if (
        [
          'blog_start_time',
          'blog_avatar',
          'blog_signature',
          'author_title',
          'author_bio',
          'github_url',
          'weibo_url',
          'wechat_qr',
          'author_location',
          'personal_website'
        ].includes(key)
      ) {
        console.log(`错误处理-原始设置 ${key} (${originalType}):`, value)
      }
    })

    ElMessage.warning('获取设置失败，已使用默认设置')
  } finally {
    loading.value = false
  }
}

/**
 * 保存所有设置
 */
async function saveAllSettings() {
  loading.value = true
  try {
    // 获取修改过的设置项
    const modifiedSettings = []
    for (const key in settingsMap.value) {
      const currentValue = settingsMap.value[key]
      const originalValue = originalSettings.value[key]

      // 统一比较格式：将当前值转换为存储格式
      let comparableCurrentValue = currentValue
      if (currentValue instanceof Date) {
        comparableCurrentValue = currentValue.toISOString().split('T')[0]
      } else if (typeof currentValue === 'boolean') {
        comparableCurrentValue = currentValue.toString()
      } else if (typeof currentValue === 'number') {
        comparableCurrentValue = currentValue.toString()
      }
      // 字符串类型保持不变，但需要处理 null/undefined
      else if (comparableCurrentValue === null || comparableCurrentValue === undefined) {
        comparableCurrentValue = ''
      }

      // 对关键字段添加详细调试信息
      if (
        [
          'blog_name',
          'blog_desc',
          'blog_author',
          'blog_email',
          'blog_url',
          'blog_start_time',
          'blog_avatar',
          'blog_signature'
        ].includes(key)
      ) {
        console.log(`字段比较 ${key}:`, {
          current: currentValue,
          currentType: typeof currentValue,
          original: originalValue,
          originalType: typeof originalValue,
          comparable: comparableCurrentValue,
          hasChanged: comparableCurrentValue !== originalValue
        })
      }

      // 如果值有变化，则加入保存列表
      if (comparableCurrentValue !== originalValue) {
        let value = currentValue

        // 将布尔值和数字转换为字符串，以便保存到数据库
        if (typeof value === 'boolean') {
          value = value.toString()
        } else if (typeof value === 'number') {
          value = value.toString()
        } else if (value === null || value === undefined) {
          value = '' // 将 null/undefined 转换为空字符串
        }

        modifiedSettings.push({
          key,
          value: value
        })

        console.log(`检测到修改 ${key}:`, value)
      }
    }

    if (modifiedSettings.length === 0) {
      ElMessage.success('没有修改任何设置')
      return
    }

    console.log('开始保存设置，修改项数量:', modifiedSettings.length)

    // 移除获取配置列表的步骤，直接在保存时动态检测是否存在
    // 这可以减少一次API调用，提高性能

    // 使用简化的保存逻辑，直接使用 updateSettingValueByKey 方法
    const results = []

    for (const setting of modifiedSettings) {
      console.log(`正在保存设置: ${setting.key} = ${setting.value} (${typeof setting.value})`)

      try {
        // 处理日期类型的值
        let processedValue = setting.value
        if (setting.value instanceof Date) {
          // 将日期转换为YYYY-MM-DD格式
          processedValue = setting.value.toISOString().split('T')[0]
          console.log(`日期 ${setting.key} 转换为: ${processedValue}`)
        }

        // 验证字段长度以符合数据库约束（现已更新为1000字符）
        if (setting.key === 'blog_avatar' && processedValue && processedValue.length > 1000) {
          console.warn(`头像URL过长: ${processedValue.length} 字符，超过1000字符限制`)
          // 不再支持Base64格式，只处理URL格式
          if (processedValue.startsWith('http')) {
            ElMessage.warning('头像URL过长，请使用文件上传方式或较短的URL')
            processedValue = processedValue.substring(0, 997) + '...'
          } else {
            // 清空过长的值，要求用户重新上传
            ElMessage.warning('头像数据格式不支持，请使用文件上传功能')
            processedValue = ''
          }
        }

        // 验证其他字段的长度
        if (processedValue && typeof processedValue === 'string') {
          // 大部分字符串字段限制为1000字符
          if (processedValue.length > 1000) {
            console.warn(`字段 ${setting.key} 长度 ${processedValue.length} 超过1000字符，将被截断`)
            processedValue = processedValue.substring(0, 997) + '...'
          }
        }

        // 使用 updateSettingValueByKey 方法，它会自动处理不存在的情况
        console.log(`使用 updateSettingValueByKey 保存 ${setting.key}`)
        const response = await updateSettingValueByKey(setting.key, processedValue)

        console.log(`设置 ${setting.key} 保存响应:`, response)

        const success = response.code === 200
        if (success) {
          console.log(`✅ 设置 ${setting.key} 保存成功`)
        } else {
          console.error(
            `❌ 设置 ${setting.key} 保存失败，返回码: ${response.code}, 消息: ${response.msg}`
          )
        }

        results.push({ success, key: setting.key })

        // 减少延迟以提高性能
        await new Promise(resolve => setTimeout(resolve, 10))
      } catch (err) {
        console.error(`设置 ${setting.key} 保存过程出错:`, err)
        results.push({ success: false, key: setting.key, error: err })
      }
    }

    // 检查结果
    const failedSettings = results.filter(result => !result.success)

    if (failedSettings.length > 0) {
      const failedKeys = failedSettings.map(s => s.key).join(', ')
      const conflictErrors = failedSettings.filter(
        s =>
          s.error &&
          (s.error.message.includes('参数键名已存在') ||
            s.error.message.includes('key already exists'))
      )

      if (conflictErrors.length > 0) {
        console.warn('检测到键冲突错误，可能是并发操作导致，尝试重新加载配置...')
        // 减少延迟时间，提高响应速度
        await new Promise(resolve => setTimeout(resolve, 100))
      }

      throw new Error(`以下设置保存失败: ${failedKeys}`)
    }

    // 注释掉复杂的重新加载逻辑，避免可能的认证问题
    // try {
    //   await reloadBlogSettings();
    //   console.log('重新加载博客设置完成');
    // } catch (error) {
    //   console.warn('重新加载设置失败，但不影响保存:', error);
    // }

    // 清除前台API缓存，确保前台能获取到最新设置
    // 使用异步方式，避免阻塞保存流程
    setTimeout(async () => {
      try {
        console.log('清除前台API缓存...')
        // 确保 clearBlogCache 函数存在且可调用
        if (typeof clearBlogCache === 'function') {
          await clearBlogCache()
          console.log('前台API缓存清除完成')
        } else {
          console.warn('clearBlogCache 函数未定义，跳过缓存清除')
        }
      } catch (error) {
        console.warn('清除前台API缓存失败，但不影响保存:', error)
      }
    }, 100)

    // 更新全局博客设置状态，通知前台页面
    try {
      // 构建最新的设置对象
      const latestSettings = {}
      Object.keys(settingsMap.value).forEach(key => {
        let value = settingsMap.value[key]
        // 转换为存储格式
        if (value instanceof Date) {
          value = value.toISOString().split('T')[0]
        } else if (typeof value === 'boolean') {
          value = value.toString()
        } else if (typeof value === 'number') {
          value = value.toString()
        } else if (value === null || value === undefined) {
          value = ''
        }
        latestSettings[key] = value
      })

      blogSettingsStore.updateBlogSettings(latestSettings)
      console.log('博客设置全局状态已更新，前台页面将收到通知')

      // 触发自定义事件，通知其他组件
      window.dispatchEvent(
        new CustomEvent('blogSettingsUpdated', {
          detail: latestSettings
        })
      )
    } catch (error) {
      console.warn('更新全局状态失败:', error)
    }

    // 保存后立即重新获取设置数据，确保显示最新值
    console.log('💾 保存成功后立即获取最新数据...')

    // 减少延迟时间，提高响应速度
    await new Promise(resolve => setTimeout(resolve, 300))

    await getAllSettings()

    // 更新原始设置（转换为存储格式以便比较）
    originalSettings.value = {}
    Object.keys(settingsMap.value).forEach(key => {
      let value = settingsMap.value[key]
      let originalType = typeof value
      if (value instanceof Date) {
        // 将日期转换为YYYY-MM-DD格式
        value = value.toISOString().split('T')[0]
        originalType = 'Date->' + typeof value
      } else if (typeof value === 'boolean') {
        // 将布尔值转换为字符串
        value = value.toString()
        originalType = 'boolean->' + typeof value
      } else if (typeof value === 'number') {
        // 将数字转换为字符串
        value = value.toString()
        originalType = 'number->' + typeof value
      } else if (value === null || value === undefined) {
        // 将 null/undefined 转换为空字符串
        value = ''
        originalType = 'null/undef->string'
      }
      originalSettings.value[key] = value

      // 对关键字段添加调试信息
      if (
        [
          'blog_name',
          'blog_desc',
          'blog_author',
          'blog_email',
          'blog_url',
          'blog_start_time',
          'blog_avatar',
          'blog_signature'
        ].includes(key)
      ) {
        console.log(`保存后-原始设置 ${key} (${originalType}):`, value)
      }
    })

    ElMessage.success(`成功保存 ${modifiedSettings.length} 项设置`)
  } catch (error) {
    console.error('保存设置失败详情:', error)
    // 根据错误类型显示不同的错误信息
    if (error.message && error.message.includes('Request method')) {
      ElMessage.error('保存设置失败: API方法不支持，请检查后端配置')
    } else if (error.message && error.message.includes('Network Error')) {
      ElMessage.error('保存设置失败: 网络连接错误，请检查网络配置')
    } else {
      ElMessage.error(`保存设置失败: ${error.message || '请稍后重试'}`)
    }
  } finally {
    loading.value = false
  }
}

/**
 * 验证头像URL格式
 * 专注于文件上传，不再支持Base64格式
 */
function validateAvatarUrl(url) {
  if (!url) return ''

  // 如果是base64格式，直接清空并提示用户使用文件上传
  if (url.startsWith('data:image/')) {
    ElMessage.warning('不再支持Base64格式头像，请使用文件上传功能')
    return ''
  }

  // 如果是相对路径，确保格式正确
  if (!url.startsWith('http') && !url.startsWith('/')) {
    url = '/' + url
  }

  return url
}

/**
 * 处理单个设置项的变更保存
 */
async function handleSingleSettingChange(key, value) {
  try {
    console.log(`开始保存单个设置: ${key} = ${value}`)

    // 处理日期类型的值
    let processedValue = value
    if (value instanceof Date) {
      processedValue = value.toISOString().split('T')[0]
      console.log(`日期 ${key} 转换为: ${processedValue}`)
    }

    // 使用 updateSettingValueByKey 方法，这个方法会自动处理不存在的情况
    const response = await updateSettingValueByKey(key, processedValue)

    if (response.code === 200) {
      console.log(`✅ ${key} 保存成功`)
      ElMessage.success(`设置 ${key} 保存成功`)

      // 更新原始设置以避免重复保存
      originalSettings.value[key] = processedValue
      console.log(`原始设置已更新: ${key} = ${processedValue}`)

      // 博客头像已移除，不再需要同步到前台store
    } else {
      throw new Error(response.msg || '操作失败')
    }
  } catch (error) {
    console.error(`❌ 保存设置 ${key} 失败:`, error)
    ElMessage.error(`保存设置 ${key} 失败: ${error.message}`)
    throw error
  }
}

/**
 * 强制刷新前台头像显示
 */
function refreshFrontendAvatar() {
  const blogSettingsStore = useBlogSettingsStore()

  // 博客头像已移除，不再需要刷新前台头像
  // 刷新前台二维码
  if (settingsMap.value.wechat_qr) {
    blogSettingsStore.updateBlogSettings({
      wechat_qr: settingsMap.value.wechat_qr
    })
  }
}

/**
 * 初始化获取设置
 */
onMounted(() => {
  // 设置正确的页面标题
  settingsStore.setTitle('博客设置')
  
  getAllSettings().then(() => {
    // 头像已移除，不再需要刷新前台头像
  })
})

/**
 * 头像上传前检查
 */
function handleAvatarBeforeUpload(file) {
  // 检查文件类型
  const isImage = file.type.startsWith('image/')
  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }

  // 检查文件大小 (10MB)
  const isLt10M = file.size / 1024 / 1024 < 10
  if (!isLt10M) {
    ElMessage.error('头像图片大小不能超过 10MB!')
    return false
  }

  ElMessage.info('正在上传并压缩头像...')
  return true
}

/**
 * 头像上传成功回调（已废弃，不再使用）
 */
/*
function handleAvatarUploadSuccess(response, uploadFile) {
  // 博客头像功能已移除，直接使用账号头像
}
*/

// 微信二维码上传功能已移除

// 添加一个测试函数用于验证数据库连接
async function testDatabaseConnection() {
  console.log('=== 开始数据库连接测试 ===')

  try {
    // 1. 测试获取所有设置
    console.log('1. 测试获取所有设置...')
    const allSettingsResponse = await listSetting({})
    console.log('获取所有设置成功:', allSettingsResponse)

    // 2. 测试获取特定设置（如果存在）
    console.log('2. 测试获取特定设置...')
    const testKey = 'blog_name' // 尝试获取一个可能存在的设置
    const specificResponse = await listSetting({ configKey: testKey })
    console.log(`获取设置 ${testKey}:`, specificResponse)

    // 3. 测试添加一个测试设置
    console.log('3. 测试添加测试设置...')
    const testConfig = {
      configKey: 'test_setting_' + Date.now(),
      configValue: 'test_value_' + Date.now(),
      configName: '测试设置',
      configType: 'N'
    }

    try {
      const addResponse = await addSetting(testConfig)
      console.log('添加测试设置成功:', addResponse)

      // 4. 测试更新刚添加的设置
      console.log('4. 测试更新测试设置...')
      const updateData = {
        ...testConfig,
        configValue: 'updated_value_' + Date.now()
      }
      const updateResponse = await updateSetting(updateData)
      console.log('更新测试设置成功:', updateResponse)
    } catch (testErr) {
      console.error('测试设置操作失败:', testErr)
    }

    console.log('=== 数据库连接测试完成 ===')
  } catch (error) {
    console.error('数据库连接测试失败:', error)
  }
}

// 将测试函数暴露到全局，方便在控制台调用
window.testDatabaseConnection = testDatabaseConnection

/**
 * 重置设置
 */
async function resetSettings() {
  try {
    await proxy.$modal.confirm('确定要重置所有设置吗？此操作不可恢复！', '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    loading.value = true
    // 重新加载原始设置
    await getAllSettings()
    proxy.$modal.msgSuccess('设置已重置')
  } catch (error) {
    // 用户取消操作
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
/* 主容器背景修复 - 支持暗色主题自动切换 */
.app-container {
  background-color: var(--el-bg-color-page);
  min-height: calc(100vh - 84px);
}

/* 暗色主题下的背景色 */
html.dark .app-container {
  background-color: var(--el-bg-color-page, #141414);
}

.blog-setting-card {
  margin-bottom: 20px;
  background-color: var(--el-bg-color-overlay);
  border: 1px solid var(--el-border-color-light);
}

/* 暗色主题下的卡片样式 */
html.dark .blog-setting-card {
  background-color: var(--el-bg-color-overlay, #1d1e1f);
  border: 1px solid var(--el-border-color-light, #434343);
}

/* 确保所有卡片组件在暗色主题下正确显示 */
:deep(.el-card) {
  background-color: var(--el-bg-color-overlay);
  border: 1px solid var(--el-border-color-light);
}

:deep(.el-card__header) {
  background-color: var(--el-bg-color-overlay);
  border-bottom: 1px solid var(--el-border-color-light);
}

:deep(.el-card__body) {
  background-color: var(--el-bg-color-overlay);
}

/* 卡片标题样式 */
.card-title {
  font-size: 16px;
  font-weight: bold;
  color: var(--el-text-color-primary, #303133);
}

/* 标签页样式优化 */
.blog-setting-tabs {
  margin-top: 16px;
}

:deep(.el-tabs__header) {
  background-color: var(--el-bg-color-page, #f5f7fa);
  border-radius: 8px 8px 0 0;
  padding: 0 16px;
  margin: 0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

:deep(.el-tabs__nav-wrap) {
  background-color: transparent;
  padding: 8px 0;
}

:deep(.el-tabs__nav-wrap::after) {
  display: none;
}

:deep(.el-tabs__item) {
  color: var(--el-text-color-regular, #606266);
  background-color: transparent;
  border: none;
  padding: 0 20px;
  height: 40px;
  line-height: 40px;
  font-size: 14px;
  font-weight: 500;
  border-radius: 6px;
  margin-right: 4px;
  transition: all 0.3s ease;
  position: relative;
}

:deep(.el-tabs__item:hover) {
  color: var(--el-color-primary, #409eff);
  background-color: var(--el-bg-color-overlay, #ffffff);
}

:deep(.el-tabs__item.is-active) {
  color: var(--el-color-primary, #409eff);
  background-color: var(--el-bg-color-overlay, #ffffff);
  font-weight: 600;
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.15);
}

:deep(.el-tabs__active-bar) {
  display: none;
}

:deep(.el-tabs__content) {
  background-color: var(--el-bg-color-overlay, #ffffff);
  padding: 24px;
  border-radius: 0 0 8px 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

/* 图片压缩功能样式 */
.compress-feature {
  text-align: center;
  padding: 15px;
  border-radius: 8px;
  background: var(--el-bg-color-overlay, white);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s ease;
}

.compress-feature:hover {
  transform: translateY(-2px);
}

.compress-feature h4 {
  margin: 0 0 10px 0;
  color: var(--el-color-primary, #409eff);
  font-size: 16px;
}

.compress-feature p {
  margin: 0 0 15px 0;
  font-size: 12px;
  color: var(--el-text-color-regular, #666);
  line-height: 1.4;
}

.feature-example {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.example-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.example-thumbnail {
  width: 35px;
  height: 35px;
  border-radius: 4px;
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.example-smart {
  width: 35px;
  height: 35px;
  border-radius: 4px;
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.feature-example span {
  font-size: 11px;
  color: var(--el-text-color-placeholder, #888);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-title {
  font-size: 16px;
  font-weight: bold;
}

.card-extra {
  display: flex;
  gap: 10px;
}

.blog-setting-tabs {
  margin-top: 20px;
}

/* 头像上传样式 */
.avatar-upload {
  display: flex;
  align-items: center;
  gap: 15px;
}

.avatar-uploader {
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
  cursor: pointer;
  transition: all 0.3s;
}

.avatar:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.avatar-uploader-icon {
  width: 100px;
  height: 100px;
  border: 1px dashed var(--el-border-color-light, #d9d9d9);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--el-text-color-placeholder, #999);
  cursor: pointer;
  transition: all 0.3s;
}

.avatar-uploader-icon:hover {
  color: var(--el-color-primary, #409eff);
  border-color: var(--el-color-primary, #409eff);
}

.avatar-input {
  flex: 1;
}

/* 表单样式优化 */
.el-form {
  padding: 0;
  background-color: transparent;
  border-radius: 0;
}

/* 暗色主题下的表单背景 */
html.dark .el-form {
  background-color: transparent;
}

/* 表单项标签和输入框样式修复 */
:deep(.el-form-item__label) {
  color: var(--el-text-color-primary, #303133);
  font-weight: 700;
}

:deep(.el-input__wrapper) {
  background-color: var(--el-bg-color-overlay, #ffffff);
  border: 1px solid var(--el-border-color, #dcdfe6);
}

:deep(.el-input__inner) {
  background-color: var(--el-bg-color-overlay, #ffffff);
  color: var(--el-text-color-primary, #303133);
}

:deep(.el-textarea__inner) {
  background-color: var(--el-bg-color-overlay, #ffffff);
  color: var(--el-text-color-primary, #303133);
  border: 1px solid var(--el-border-color, #dcdfe6);
}

:deep(.el-upload) {
  border: 1px dashed var(--el-border-color-light, #d9d9d9);
  border-radius: 6px;
  background-color: var(--el-bg-color-overlay, #ffffff);
}

.el-form-item {
  margin-bottom: 20px;
}

/* 响应式优化 */
@media (max-width: 768px) {
  .card-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .avatar-upload {
    flex-direction: column;
    align-items: flex-start;
  }

  .avatar-input {
    width: 100%;
  }

  .blog-setting-tabs {
    margin-top: 12px;
  }

  :deep(.el-tabs__header) {
    padding: 0 8px;
  }

  :deep(.el-tabs__item) {
    padding: 0 12px;
    font-size: 13px;
    height: 36px;
    line-height: 36px;
  }

  :deep(.el-tabs__content) {
    padding: 16px;
  }

  :deep(.el-form-item__label) {
    font-size: 13px;
    font-weight: 600;
  }
}

@media (max-width: 480px) {
  :deep(.el-tabs__item) {
    padding: 0 8px;
    font-size: 12px;
  }

  :deep(.el-tabs__content) {
    padding: 12px;
  }

  :deep(.el-form-item) {
    margin-bottom: 16px;
  }
}
</style>
