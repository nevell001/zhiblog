<template>
  <div class="app-container">
    <el-card shadow="never" class="blog-setting-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">博客设置管理</span>
          <div class="card-extra">
            <el-button type="primary" @click="saveAllSettings" size="small" :loading="loading">
              <i class="el-icon-check"></i> 保存所有设置
            </el-button>
            <el-button type="warning" @click="resetSettings" size="small" :loading="loading">
              <i class="el-icon-refresh"></i> 重置设置
            </el-button>
          </div>
        </div>
      </template>

      <el-tabs v-model="activeTab" class="blog-setting-tabs">
        <!-- 基本设置 -->
        <el-tab-pane label="基本设置" name="basic">
          <el-form ref="basicForm" :model="settingsMap" label-width="120px">
            <el-form-item label="博客名称" prop="blog_name">
              <el-input v-model="settingsMap.blog_name" placeholder="请输入博客名称" />
            </el-form-item>
            <el-form-item label="博客描述" prop="blog_desc">
              <el-input v-model="settingsMap.blog_desc" type="textarea" :rows="3" placeholder="请输入博客描述" />
            </el-form-item>
            <el-form-item label="博客作者" prop="blog_author">
              <el-input v-model="settingsMap.blog_author" placeholder="请输入博客作者" />
            </el-form-item>
            <el-form-item label="联系邮箱" prop="blog_email">
              <el-input v-model="settingsMap.blog_email" placeholder="请输入联系邮箱" />
            </el-form-item>
            <el-form-item label="博客地址" prop="blog_url">
              <el-input v-model="settingsMap.blog_url" placeholder="请输入博客访问地址" />
            </el-form-item>
            <el-form-item label="创建时间" prop="blog_start_time">
              <el-date-picker
                v-model="settingsMap.blog_start_time"
                type="date"
                placeholder="选择博客创建时间"
                style="width: 100%"
              />
            </el-form-item>
            <el-form-item label="博主头像" prop="blog_avatar">
              <div class="avatar-upload">
                <el-upload
                  class="avatar-uploader"
                  :action="uploadAvatarUrl"
                  :headers="headers"
                  :on-success="handleAvatarUploadSuccess"
                  :before-upload="handleAvatarBeforeUpload"
                  :show-file-list="false"
                >
                  <img v-if="settingsMap.blog_avatar" :src="settingsMap.blog_avatar" class="avatar" />
                  <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                </el-upload>
                <el-input v-model="settingsMap.blog_avatar" class="avatar-input" placeholder="或直接输入头像URL" />
                <div class="upload-tip" style="font-size: 12px; color: #999; margin-top: 5px;">
                  🚀 <strong>基于Thumbnailator专业处理</strong>：智能压缩为200x200像素，质量优化
                  <br>💡 支持 JPG/PNG/GIF 格式，最大10MB，自动居中裁剪，高性能
                  <br>📦 文件存储，数据库仅存URL，性能卓越
                </div>
              </div>
            </el-form-item>
            <el-form-item label="博主签名" prop="blog_signature">
              <el-input v-model="settingsMap.blog_signature" placeholder="请输入博主签名" />
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- SEO设置 -->
        <el-tab-pane label="SEO设置" name="seo">
          <el-form ref="seoForm" :model="settingsMap" label-width="120px">
            <el-form-item label="SEO标题" prop="seo_title">
              <el-input v-model="settingsMap.seo_title" placeholder="请输入SEO标题" />
            </el-form-item>
            <el-form-item label="SEO描述" prop="seo_description">
              <el-input v-model="settingsMap.seo_description" type="textarea" :rows="3" placeholder="请输入SEO描述" />
            </el-form-item>
            <el-form-item label="关键词" prop="blog_keywords">
              <el-input v-model="settingsMap.blog_keywords" placeholder="请输入关键词，多个关键词用逗号分隔" />
            </el-form-item>
            <el-form-item label="规范URL" prop="seo_canonical_url">
              <el-input v-model="settingsMap.seo_canonical_url" placeholder="请输入规范URL" />
            </el-form-item>
            <el-form-item label="Robots规则" prop="seo_robots">
              <el-input v-model="settingsMap.seo_robots" placeholder="如：index,follow" />
            </el-form-item>
            <el-form-item label="网站图标" prop="seo_favicon">
              <el-input v-model="settingsMap.seo_favicon" placeholder="请输入网站图标路径" />
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 个性化设置 -->
        <el-tab-pane label="个性化设置" name="personalization">
          <el-form ref="personalizationForm" :model="settingsMap" label-width="120px">
            <el-form-item label="主题颜色" prop="theme_color">
              <el-color-picker v-model="settingsMap.theme_color" show-alpha />
            </el-form-item>
            <el-form-item label="头部背景色" prop="header_background">
              <el-color-picker v-model="settingsMap.header_background" show-alpha />
            </el-form-item>
            <el-form-item label="侧边栏样式" prop="sidebar_style">
              <el-radio-group v-model="settingsMap.sidebar_style">
                <el-radio label="dark">深色</el-radio>
                <el-radio label="light">浅色</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="版权信息" prop="blog_copyright">
              <el-input v-model="settingsMap.blog_copyright" type="textarea" :rows="2" placeholder="请输入版权信息" />
            </el-form-item>
            <el-form-item label="备案信息" prop="blog_beian">
              <el-input v-model="settingsMap.blog_beian" placeholder="请输入ICP备案信息" />
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 功能设置 -->
        <el-tab-pane label="功能设置" name="features">
          <el-form ref="featuresForm" :model="settingsMap" label-width="120px">
            <el-form-item label="评论功能" prop="comment_enabled">
              <el-switch v-model="settingsMap.comment_enabled" />
            </el-form-item>
            <el-form-item 
              v-if="settingsMap.comment_enabled === 'true' || settingsMap.comment_enabled === true" 
              label="评论审核" 
              prop="comment_review"
            >
              <el-switch v-model="settingsMap.comment_review" />
            </el-form-item>
            <el-form-item label="点赞功能" prop="like_enabled">
              <el-switch v-model="settingsMap.like_enabled" />
            </el-form-item>
            <el-form-item label="浏览统计" prop="view_count_enabled">
              <el-switch v-model="settingsMap.view_count_enabled" />
            </el-form-item>
            <el-form-item label="分享功能" prop="share_enabled">
              <el-switch v-model="settingsMap.share_enabled" />
            </el-form-item>
            <el-form-item label="搜索功能" prop="search_enabled">
              <el-switch v-model="settingsMap.search_enabled" />
            </el-form-item>
            <el-form-item label="显示侧边栏" prop="sidebar_enabled">
              <el-switch v-model="settingsMap.sidebar_enabled" />
            </el-form-item>
            <el-form-item label="显示底部" prop="footer_enabled">
              <el-switch v-model="settingsMap.footer_enabled" />
            </el-form-item>
            <el-form-item label="显示版权" prop="copyright_enabled">
              <el-switch v-model="settingsMap.copyright_enabled" />
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 博主信息 -->
        <el-tab-pane label="博主信息" name="author">
          <el-form ref="authorForm" :model="settingsMap" label-width="120px">
            <el-form-item label="作者职位" prop="author_title">
              <el-input v-model="settingsMap.author_title" placeholder="请输入作者职位，如：全栈开发工程师" />
            </el-form-item>
            <el-form-item label="个人简介" prop="author_bio">
              <el-input v-model="settingsMap.author_bio" type="textarea" :rows="3" placeholder="请输入个人简介" />
            </el-form-item>
            <el-form-item label="GitHub地址" prop="github_url">
              <el-input v-model="settingsMap.github_url" placeholder="请输入GitHub地址" />
            </el-form-item>
            <el-form-item label="微博地址" prop="weibo_url">
              <el-input v-model="settingsMap.weibo_url" placeholder="请输入微博地址" />
            </el-form-item>
              <el-form-item label="微信二维码" prop="wechat_qr">
              <div class="avatar-upload">
                <el-upload
                  class="avatar-uploader"
                  :action="uploadThumbnailUrl"
                  :headers="headers"
                  :on-success="handleQRCodeUploadSuccess"
                  :before-upload="handleQRCodeBeforeUpload"
                  :show-file-list="false"
                >
                  <img v-if="settingsMap.wechat_qr" :src="settingsMap.wechat_qr" class="avatar" />
                  <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                </el-upload>
                <el-input v-model="settingsMap.wechat_qr" class="avatar-input" placeholder="或直接输入二维码URL" />
                <div class="upload-tip" style="font-size: 12px; color: #999; margin-top: 5px;">
                  🚀 <strong>基于Thumbnailator专业处理</strong>：智能压缩为400x400像素，保持清晰度
                  <br>💡 适合微信二维码、支付码等，自动优化大小和清晰度
                  <br>📦 文件存储，数据库仅存URL，扫描性能更佳
                </div>
              </div>
            </el-form-item>
            <el-form-item label="位置信息" prop="author_location">
              <el-input v-model="settingsMap.author_location" placeholder="请输入位置信息，如：中国·北京" />
            </el-form-item>
            <el-form-item label="个人网站" prop="personal_website">
              <el-input v-model="settingsMap.personal_website" placeholder="请输入个人网站地址" />
            </el-form-item>
          </el-form>
        </el-tab-pane>

  
        <!-- 其他设置 -->
        <el-tab-pane label="其他设置" name="other">
          <el-form ref="otherForm" :model="settingsMap" label-width="120px">
            <el-form-item label="每页文章数" prop="page_size">
              <el-input-number v-model="settingsMap.page_size" :min="5" :max="50" :step="1" />
            </el-form-item>
            <el-form-item label="热门文章数" prop="hot_article_count">
              <el-input-number v-model="settingsMap.hot_article_count" :min="1" :max="20" :step="1" />
            </el-form-item>
            <el-form-item label="最新评论数" prop="recent_comment_count">
              <el-input-number v-model="settingsMap.recent_comment_count" :min="1" :max="20" :step="1" />
            </el-form-item>
            <el-form-item label="欢迎信息" prop="greeting_message">
              <el-input v-model="settingsMap.greeting_message" placeholder="请输入欢迎信息" />
            </el-form-item>
            <el-form-item label="关于页面内容" prop="about_content">
              <el-input v-model="settingsMap.about_content" type="textarea" :rows="5" placeholder="请输入关于页面内容" />
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>

      <!-- 图片压缩功能介绍 -->
      <el-card shadow="never" style="margin-top: 20px; background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);">
        <template #header>
          <div style="display: flex; align-items: center;">
            <span style="color: #409EFF; font-weight: bold;">🎨 图片压缩功能</span>
            <el-tag type="success" size="small" style="margin-left: 10px;">已启用</el-tag>
          </div>
        </template>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="compress-feature">
              <h4>👤 头像压缩</h4>
              <p><strong>200×200正方形</strong> | 质量90% | 自动居中裁剪</p>
              <div class="feature-example">
                <div class="example-avatar"></div>
                <span>博主头像、用户头像</span>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="compress-feature">
              <h4>🖼️ 缩略图压缩</h4>
              <p><strong>400×400最大尺寸</strong> | 质量80% | 保持宽高比</p>
              <div class="feature-example">
                <div class="example-thumbnail"></div>
                <span>二维码、图片预览</span>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="compress-feature">
              <h4>📦 智能压缩</h4>
              <p><strong>自适应压缩策略</strong> | 根据文件大小 | 高质量压缩</p>
              <div class="feature-example">
                <div class="example-smart"></div>
                <span>文章配图、通用图片</span>
              </div>
            </div>
          </el-col>
        </el-row>
        <div style="margin-top: 15px; text-align: center; font-size: 12px; color: #666;">
          基于 <strong>Thumbnailator</strong> 专业图片处理库 | 压缩率可达60-80% | 支持JPG/PNG/GIF
        </div>
      </el-card>
    </el-card>
  </div>
</template>

<script setup name="BlogSetting">
import { ref, reactive, onMounted, getCurrentInstance } from 'vue';
import { ElMessage, ElButton, ElCard, ElTabs, ElTabPane, ElForm, ElFormItem, ElInput, ElInputNumber, ElSwitch, ElColorPicker, ElRadioGroup, ElRadio, ElDatePicker, ElUpload, ElRow, ElCol } from 'element-plus';
import { listSetting, getSetting, updateSetting, updateSettingValueByKey, addSetting, getConfigByKey, delSetting } from "@/api/admin/blog/setting";
import { reloadBlogSettings } from '@/utils/blogSettings';
import { useBlogSettingsStore } from '@/stores/blogSettings';
import { getToken } from "@/utils/auth";

const { proxy } = getCurrentInstance();

// 头像上传相关
const baseUrl = import.meta.env.VITE_APP_BASE_API;
const uploadAvatarUrl = baseUrl + '/common/upload/avatar';
const uploadThumbnailUrl = baseUrl + '/common/upload/thumbnail';
const headers = ref({ Authorization: "Bearer " + getToken() });

// 初始化博客设置全局状态
const blogSettingsStore = useBlogSettingsStore();

// 响应式数据
const loading = ref(false);
const activeTab = ref('basic');
const settingsMap = ref({});
const originalSettings = ref({});


/**
 * 获取所有博客设置
 */
async function getAllSettings() {
  loading.value = true;
  try {
    console.log('🚀 开始强制刷新获取博客设置...');

    // 强制清除可能的缓存，使用多重策略
    const timestamp = new Date().getTime();
    const randomNonce = Math.random().toString(36).substring(7);
    const cacheBuster = `${timestamp}_${randomNonce}`;

    console.log(`🔄 使用缓存破坏器: ${cacheBuster}`);

    // 尝试多种查询策略来获取博客设置
    let allSettings = [];
    let queryStrategies = [];

    // 策略1: 标准查询，大页面大小
    queryStrategies.push({
      name: '标准查询(大页面)',
      params: { _t: cacheBuster, pageSize: 200, pageNum: 1 }
    });

    // 策略2: 查询包含blog_前缀的配置
    queryStrategies.push({
      name: '博客配置查询',
      params: { _t: cacheBuster, configKey: 'blog_', pageSize: 200 }
    });

    // 策略3: 查询所有可能的博客相关配置键
    const blogKeys = [
      'blog_name', 'blog_desc', 'blog_author', 'blog_email', 'blog_url', 'blog_start_time',
      'blog_avatar', 'blog_signature', 'blog_keywords', 'blog_copyright', 'blog_beian',
      'seo_title', 'seo_description', 'seo_canonical_url', 'seo_robots', 'seo_favicon',
      'theme_color', 'header_background', 'sidebar_style',
      'comment_enabled', 'comment_review', 'like_enabled', 'view_count_enabled',
      'share_enabled', 'search_enabled', 'sidebar_enabled', 'footer_enabled', 'copyright_enabled',
      'page_size', 'hot_article_count', 'recent_comment_count', 'greeting_message', 'about_content',
      'author_title', 'author_bio', 'github_url', 'weibo_url', 'wechat_qr', 'author_location', 'personal_website'
    ];

    // 策略4: 分别查询关键的博客设置
    for (const key of blogKeys) {
      queryStrategies.push({
        name: `单独查询: ${key}`,
        params: { _t: cacheBuster, configKey: key, pageSize: 1 }
      });
    }

    // 执行优化的查询策略 - 减少API调用
    let successfulQueries = 0;
    let totalFound = 0;

    // 策略1: 先尝试标准查询
    try {
      console.log(`📡 执行标准查询(大页面)`);
      const response = await listSetting(queryStrategies[0].params);
      const settingList = response.rows || response.data || [];

      if (settingList.length > 0) {
        successfulQueries++;
        totalFound += settingList.length;

        const foundKeys = settingList.map(s => s.configKey).filter(Boolean);
        console.log(`✅ 标准查询找到 ${settingList.length} 项设置:`, foundKeys);

        allSettings.push(...settingList);
      }
    } catch (error) {
      console.warn(`❌ 标准查询失败:`, error.message);
    }

    // 如果标准查询没有找到关键的博客设置，才执行单独查询
    const loadCriticalKeys = ['blog_start_time', 'blog_avatar', 'blog_signature'];
    const foundCriticalKeys = allSettings.map(s => s.configKey).filter(key => loadCriticalKeys.includes(key));

    console.log(`📊 检查关键设置: 已找到 ${foundCriticalKeys.length}/3 关键设置:`, foundCriticalKeys);

    if (foundCriticalKeys.length < 3) {
      console.log(`🔍 关键设置缺失，执行针对性查询...`);

      // 只查询缺失的关键设置，减少API调用
      const missingKeys = loadCriticalKeys.filter(key => !foundCriticalKeys.includes(key));
      const batchQuery = {
        _t: cacheBuster,
        configKey: missingKeys.join(','), // 假设API支持批量查询
        pageSize: 50
      };

      try {
        console.log(`📡 批量查询缺失的关键设置:`, missingKeys);
        const batchResponse = await listSetting(batchQuery);
        const batchSettings = batchResponse.rows || batchResponse.data || [];

        if (batchSettings.length > 0) {
          successfulQueries++;
          totalFound += batchSettings.length;

          const batchFoundKeys = batchSettings.map(s => s.configKey).filter(Boolean);
          console.log(`✅ 批量查询找到 ${batchSettings.length} 项关键设置:`, batchFoundKeys);

          // 合并并去重
          batchSettings.forEach(setting => {
            if (setting.configKey) {
              const existingIndex = allSettings.findIndex(s => s.configKey === setting.configKey);
              if (existingIndex >= 0) {
                allSettings[existingIndex] = setting;
                console.log(`🔄 更新已存在的设置: ${setting.configKey}`);
              } else {
                allSettings.push(setting);
                console.log(`➕ 添加新设置: ${setting.configKey} = ${setting.configValue}`);
              }
            }
          });
        }
      } catch (batchError) {
        console.warn(`❌ 批量查询失败，回退到单独查询:`, batchError.message);

        // 回退策略：使用并行查询来提高速度
        if (missingKeys.length > 0) {
          console.log(`📡 并行查询缺失的关键设置...`);
          const parallelQueries = missingKeys.map(async (key) => {
            try {
              console.log(`📡 并行查询: ${key}`);
              const singleResponse = await listSetting({
                _t: cacheBuster,
                configKey: key,
                pageSize: 1
              });

              const singleSettings = singleResponse.rows || singleResponse.data || [];
              if (singleSettings.length > 0) {
                console.log(`➕ 并行查询找到设置: ${key}`);
                return singleSettings[0]; // 返回找到的设置项
              }
            } catch (singleError) {
              console.warn(`❌ 并行查询 ${key} 失败:`, singleError.message);
            }
            return null;
          });

          // 等待所有并行查询完成
          const parallelResults = await Promise.all(parallelQueries);
          const validResults = parallelResults.filter(result => result !== null);

          successfulQueries += validResults.length;
          totalFound += validResults.length;
          allSettings.push(...validResults);

          console.log(`✅ 并行查询完成，找到 ${validResults.length} 项设置`);
        }
      }
    }

    console.log(`📊 查询总结: ${successfulQueries}/${queryStrategies.length} 个策略成功，总共找到 ${allSettings.length} 项设置`);

    // 如果还是没有找到博客设置，尝试强制刷新和延迟重试
    if (allSettings.length <= 10) {
      console.log('⚠️ 找到的设置数量过少，尝试强制刷新...');

      // 减少延迟时间到500毫秒，提高响应速度
      await new Promise(resolve => setTimeout(resolve, 500));

      try {
        const retryResponse = await listSetting({
          _t: new Date().getTime(),
          pageSize: 500,
          pageNum: 1
        });

        const retrySettings = retryResponse.rows || retryResponse.data || [];
        console.log(`🔄 重试查询找到 ${retrySettings.length} 项设置`);

        if (retrySettings.length > allSettings.length) {
          allSettings = retrySettings;
          console.log('✅ 重试查询找到更多设置，已更新');
        }
      } catch (retryError) {
        console.warn('重试查询也失败:', retryError.message);
      }
    }

    console.log('📋 最终合并的设置列表 (共 ' + allSettings.length + ' 项):', allSettings.map(s => ({
      key: s.configKey,
      value: s.configValue,
      type: typeof s.configValue
    })));

    // 将设置项转换为Map格式，并处理特殊类型
    const settings = {};
    allSettings.forEach(setting => {
      if (setting.configKey) {
        let value = setting.configValue;
        const originalType = typeof value;

        // 处理布尔值转换
        if (value === 'true') {
          value = true;
          console.log(`🔄 布尔转换 ${setting.configKey}: 'true' → true`);
        } else if (value === 'false') {
          value = false;
          console.log(`🔄 布尔转换 ${setting.configKey}: 'false' → false`);
        }

        // 处理日期类型转换
        if (setting.configKey === 'blog_start_time' && value && typeof value === 'string') {
          // 将YYYY-MM-DD格式的字符串转换为Date对象
          const dateValue = new Date(value);
          if (!isNaN(dateValue.getTime())) {
            value = dateValue;
            console.log(`📅 日期转换 ${setting.configKey}: '${setting.configValue}' → Date(${value.toISOString()})`);
          } else {
            console.warn(`⚠️ 无效的日期格式 ${setting.configKey}: '${value}'`);
            value = null;
          }
        }

        // 验证和处理头像数据 - 不再支持Base64格式
        if (setting.configKey === 'blog_avatar') {
          if (value && value.length > 500) {
            console.warn(`⚠️ 数据库中的头像数据过长 (${value.length} 字符)，清空并要求用户重新上传`);
            value = '';
          } else if (value && value.startsWith('data:image/')) {
            console.warn(`⚠️ 检测到Base64格式头像，不再支持，清空并要求用户重新上传`);
            value = '';
          }
        }

        settings[setting.configKey] = value;

        // 为关键字段添加详细调试信息
        if (['blog_start_time', 'blog_avatar', 'blog_signature'].includes(setting.configKey)) {
          console.log(`🔍 关键字段 ${setting.configKey}:`, {
            value: value,
            valueType: typeof value,
            originalValue: setting.configValue,
            originalType: originalType,
            isValid: value !== null && value !== undefined && value !== ''
          });
        }
      }
    });

    console.log('🗂️ 最终处理后的设置对象:', settings);

    // 设置默认值，确保所有必要的设置项都有值
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
      wechat_qr: '',
      author_title: '',
      author_bio: '',
      author_location: '',
      personal_website: ''
    };

    // 合并默认设置和从服务器获取的设置（服务器设置优先）
    const mergedSettings = { ...defaultSettings };

    // 统计从服务器获取的设置数量
    const serverSettingKeys = Object.keys(settings);
    const criticalKeys = ['blog_start_time', 'blog_avatar', 'blog_signature'];
    const retrievedCriticalKeys = serverSettingKeys.filter(key => criticalKeys.includes(key));

    console.log(`📊 设置统计: 从服务器获取 ${serverSettingKeys.length} 项设置`);
    console.log(`🔑 关键键统计: 找到 ${retrievedCriticalKeys.length}/3 项关键设置:`, retrievedCriticalKeys);
    console.log(`📋 服务器设置键:`, serverSettingKeys);

    // 用服务器获取的设置覆盖默认设置
    Object.keys(settings).forEach(key => {
      if (settings[key] !== undefined) {
        // 即使是 null 或空字符串也要保留，因为有些字段可能就是空的
        mergedSettings[key] = settings[key];

        // 为关键字段添加详细日志
        if (criticalKeys.includes(key)) {
          console.log(`✅ 应用关键设置 ${key}:`, {
            value: settings[key],
            valueType: typeof settings[key],
            isEmpty: settings[key] === '' || settings[key] === null || settings[key] === undefined
          });
        }
      }
    });

    // 验证关键字段是否正确加载
    console.log('🔍 关键字段最终验证:');
    criticalKeys.forEach(key => {
      const finalValue = mergedSettings[key];
      console.log(`  ${key}:`, {
        value: finalValue,
        type: typeof finalValue,
        isEmpty: finalValue === '' || finalValue === null || finalValue === undefined,
        fromServer: serverSettingKeys.includes(key)
      });
    });

    // 特别处理头像URL，确保格式正确
    if (mergedSettings.blog_avatar) {
      mergedSettings.blog_avatar = validateAvatarUrl(mergedSettings.blog_avatar);
    }
    if (mergedSettings.wechat_qr) {
      mergedSettings.wechat_qr = validateAvatarUrl(mergedSettings.wechat_qr);
    }

    // 保存设置到响应式数据
    settingsMap.value = mergedSettings;
    // 保存原始设置用于重置（转换为存储格式以便比较）
    originalSettings.value = {};
    Object.keys(mergedSettings).forEach(key => {
      let value = mergedSettings[key];
      let originalType = typeof value;
      if (value instanceof Date) {
        // 将日期转换为YYYY-MM-DD格式
        value = value.toISOString().split('T')[0];
        originalType = 'Date->' + typeof value;
      } else if (typeof value === 'boolean') {
        // 将布尔值转换为字符串
        value = value.toString();
        originalType = 'boolean->' + typeof value;
      }
      originalSettings.value[key] = value;

      // 对关键字段添加调试信息
      if (['blog_start_time', 'blog_avatar', 'blog_signature'].includes(key)) {
        console.log(`原始设置 ${key} (${originalType}):`, value);
      }
    });
    
  } catch (error) {
    console.error('获取设置失败:', error);
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
      blog_email: '',
      wechat_qr: ''
    };
    
    // 特别处理默认头像URL
    defaultSettings.blog_avatar = validateAvatarUrl(defaultSettings.blog_avatar);
    defaultSettings.wechat_qr = validateAvatarUrl(defaultSettings.wechat_qr);

    settingsMap.value = defaultSettings;
    // 保存原始设置用于重置（转换为存储格式以便比较）
    originalSettings.value = {};
    Object.keys(defaultSettings).forEach(key => {
      let value = defaultSettings[key];
      let originalType = typeof value;
      if (value instanceof Date) {
        // 将日期转换为YYYY-MM-DD格式
        value = value.toISOString().split('T')[0];
        originalType = 'Date->' + typeof value;
      } else if (typeof value === 'boolean') {
        // 将布尔值转换为字符串
        value = value.toString();
        originalType = 'boolean->' + typeof value;
      } else if (value === null || value === undefined) {
        // 将 null/undefined 转换为空字符串
        value = '';
        originalType = 'null/undef->string';
      }
      originalSettings.value[key] = value;

      // 对关键字段添加调试信息
      if (['blog_start_time', 'blog_avatar', 'blog_signature', 'author_title', 'author_bio', 'github_url', 'weibo_url', 'wechat_qr', 'author_location', 'personal_website'].includes(key)) {
        console.log(`错误处理-原始设置 ${key} (${originalType}):`, value);
      }
    });
    
    ElMessage.warning('获取设置失败，已使用默认设置');
  } finally {
    loading.value = false;
  }
}

/**
 * 保存所有设置
 */
async function saveAllSettings() {
  loading.value = true;
  try {
    // 获取修改过的设置项
    const modifiedSettings = [];
    for (const key in settingsMap.value) {
      let currentValue = settingsMap.value[key];
      let originalValue = originalSettings.value[key];

      // 统一比较格式：将当前值转换为存储格式
      let comparableCurrentValue = currentValue;
      if (currentValue instanceof Date) {
        comparableCurrentValue = currentValue.toISOString().split('T')[0];
      } else if (typeof currentValue === 'boolean') {
        comparableCurrentValue = currentValue.toString();
      }
      // 字符串类型保持不变，但需要处理 null/undefined
      else if (comparableCurrentValue === null || comparableCurrentValue === undefined) {
        comparableCurrentValue = '';
      }

      // 对关键字段添加详细调试信息
      if (['blog_start_time', 'blog_avatar', 'blog_signature', 'author_title', 'author_bio', 'github_url', 'weibo_url', 'wechat_qr', 'author_location', 'personal_website'].includes(key)) {
        console.log(`字段比较 ${key}:`, {
          current: currentValue,
          currentType: typeof currentValue,
          original: originalValue,
          originalType: typeof originalValue,
          comparable: comparableCurrentValue,
          hasChanged: comparableCurrentValue !== originalValue
        });
      }

      // 如果值有变化，则加入保存列表
      if (comparableCurrentValue !== originalValue) {
        let value = currentValue;

        // 将布尔值转换为字符串，以便保存到数据库
        if (typeof value === 'boolean') {
          value = value.toString();
        } else if (value === null || value === undefined) {
          value = ''; // 将 null/undefined 转换为空字符串
        }

        modifiedSettings.push({
          key,
          value: value
        });

        console.log(`检测到修改 ${key}:`, value);
      }
    }
    
    if (modifiedSettings.length === 0) {
      ElMessage.success('没有修改任何设置');
      return;
    }
    
    console.log('开始保存设置，修改项数量:', modifiedSettings.length);

    // 移除获取配置列表的步骤，直接在保存时动态检测是否存在
    // 这可以减少一次API调用，提高性能

    // 顺序处理设置以避免并发操作导致的键冲突
    const results = [];

    for (const setting of modifiedSettings) {
      console.log(`正在保存设置: ${setting.key} = ${setting.value} (${typeof setting.value})`);

      try {
        // 处理日期类型的值
        let processedValue = setting.value;
        if (setting.value instanceof Date) {
          // 将日期转换为YYYY-MM-DD格式
          processedValue = setting.value.toISOString().split('T')[0];
          console.log(`日期 ${setting.key} 转换为: ${processedValue}`);
        }

        // 验证字段长度以符合数据库约束
        if (setting.key === 'blog_avatar' && processedValue && processedValue.length > 500) {
          console.warn(`头像URL过长: ${processedValue.length} 字符，超过500字符限制`);
          // 不再支持Base64格式，只处理URL格式
          if (processedValue.startsWith('http')) {
            ElMessage.warning('头像URL过长，请使用文件上传方式或较短的URL');
            processedValue = processedValue.substring(0, 497) + '...';
          } else {
            // 清空过长的值，要求用户重新上传
            ElMessage.warning('头像数据格式不支持，请使用文件上传功能');
            processedValue = '';
          }
        }

        // 验证其他字段的长度
        if (processedValue && typeof processedValue === 'string') {
          // 大部分字符串字段限制为500字符
          if (processedValue.length > 500) {
            console.warn(`字段 ${setting.key} 长度 ${processedValue.length} 超过500字符，将被截断`);
            processedValue = processedValue.substring(0, 497) + '...';
          }
        }

        const configData = {
          configKey: setting.key,
          configValue: processedValue,
          configName: setting.key, // 后端验证需要这个字段
          configType: "N" // 非系统内置配置
        };

        let success = false;

        // 尝试保存配置（智能检测新增或更新）
        try {
          // 首先尝试更新，因为大多数情况下配置已存在
          console.log(`尝试智能保存配置 ${setting.key}`);

          // 获取最新的配置状态以决定使用更新还是新增
          const currentConfigResponse = await listSetting({ configKey: setting.key });
          console.log(`获取配置响应 ${setting.key}:`, {
            status: currentConfigResponse.status,
            rows: currentConfigResponse.rows?.length,
            data: currentConfigResponse.data?.length,
            total: currentConfigResponse.total
          });

          const currentConfigs = currentConfigResponse.rows || currentConfigResponse.data || [];
          const currentConfig = currentConfigs.find(config => config.configKey === setting.key);

          if (currentConfig) {
            // 配置存在，执行更新
            console.log(`配置 ${setting.key} 已存在，执行更新，ID: ${currentConfig.configId}, 当前值: ${currentConfig.configValue}`);
            const updateResponse = await updateSetting({
              ...configData,
              configId: currentConfig.configId
            });
            console.log(`设置 ${setting.key} 更新响应:`, updateResponse);
            // 验证更新是否真的成功
            if (updateResponse.code === 200) {
              console.log(`✅ 设置 ${setting.key} 更新成功，返回码: ${updateResponse.code}`);
              success = true;
            } else {
              console.error(`❌ 设置 ${setting.key} 更新失败，返回码: ${updateResponse.code}, 消息: ${updateResponse.msg}`);
              success = false;
            }
          } else {
            // 配置不存在，执行新增
            console.log(`配置 ${setting.key} 不存在，执行新增`);
            const addResponse = await addSetting(configData);
            console.log(`设置 ${setting.key} 新增响应:`, addResponse);
            // 验证新增是否真的成功
            if (addResponse.code === 200) {
              console.log(`✅ 设置 ${setting.key} 新增成功，返回码: ${addResponse.code}`);
              success = true;
            } else {
              console.error(`❌ 设置 ${setting.key} 新增失败，返回码: ${addResponse.code}, 消息: ${addResponse.msg}`);
              success = false;
            }
          }

        } catch (saveErr) {
          console.error(`设置 ${setting.key} 保存失败详细信息:`, {
            message: saveErr.message,
            response: saveErr.response,
            status: saveErr.response?.status,
            data: saveErr.response?.data,
            configData: configData
          });

          // 分析错误类型并采取相应策略
          const errorMessage = saveErr.message || '';
          const responseData = saveErr.response?.data || {};

          if (errorMessage.includes('参数键名已存在') || errorMessage.includes('key already exists') || responseData.msg?.includes('参数键名已存在')) {
            // 键已存在错误，重新获取配置并尝试更新
            console.log(`检测到键冲突，重新获取配置并更新 ${setting.key}`);
            try {
              const retryResponse = await listSetting({ configKey: setting.key });
              const retryConfigs = retryResponse.rows || retryResponse.data || [];
              const retryConfig = retryConfigs.find(config => config.configKey === setting.key);

              if (retryConfig) {
                await updateSetting({
                  ...configData,
                  configId: retryConfig.configId
                });
                console.log(`设置 ${setting.key} 冲突解决后更新成功`);
                success = true;
              } else {
                console.error(`配置 ${setting.key} 冲突但无法找到更新目标`);
              }
            } catch (retryErr) {
              console.error(`设置 ${setting.key} 冲突解决失败:`, retryErr);
            }
          } else {
            // 其他类型的错误
            console.error(`设置 ${setting.key} 遇到数据库错误:`, {
              errorCode: saveErr.response?.status,
              errorMessage: responseData.msg || saveErr.message,
              validationErrors: responseData.errors || responseData.errors
            });
          }
        }

        results.push({ success, key: setting.key });

        // 减少延迟以提高性能，只保留必要的最小延迟
        await new Promise(resolve => setTimeout(resolve, 10));

      } catch (err) {
        console.error(`设置 ${setting.key} 保存过程出错:`, err);
        results.push({ success: false, key: setting.key, error: err });
      }
    }
    
    // 检查结果
    const failedSettings = results.filter(result => !result.success);
    
    if (failedSettings.length > 0) {
      const failedKeys = failedSettings.map(s => s.key).join(', ');
      const conflictErrors = failedSettings.filter(s =>
        s.error && (s.error.message.includes('参数键名已存在') || s.error.message.includes('key already exists'))
      );

      if (conflictErrors.length > 0) {
        console.warn('检测到键冲突错误，可能是并发操作导致，尝试重新加载配置...');
        // 减少延迟时间，提高响应速度
        await new Promise(resolve => setTimeout(resolve, 100));
      }

      throw new Error(`以下设置保存失败: ${failedKeys}`);
    }
    
    // 重新加载博客设置，使新设置立即生效
    try {
      await reloadBlogSettings();
      console.log('重新加载博客设置完成');
    } catch (error) {
      console.warn('重新加载设置失败，但不影响保存:', error);
    }

    // 更新全局博客设置状态，通知前台页面
    try {
      // 构建最新的设置对象
      const latestSettings = {};
      Object.keys(settingsMap.value).forEach(key => {
        let value = settingsMap.value[key];
        // 转换为存储格式
        if (value instanceof Date) {
          value = value.toISOString().split('T')[0];
        } else if (typeof value === 'boolean') {
          value = value.toString();
        } else if (value === null || value === undefined) {
          value = '';
        }
        latestSettings[key] = value;
      });

      blogSettingsStore.updateBlogSettings(latestSettings);
      console.log('博客设置全局状态已更新，前台页面将收到通知');

      // 触发自定义事件，通知其他组件
      window.dispatchEvent(new CustomEvent('blogSettingsUpdated', {
        detail: latestSettings
      }));

    } catch (error) {
      console.warn('更新全局状态失败:', error);
    }

    // 保存后立即重新获取设置数据，确保显示最新值
    console.log('💾 保存成功后立即获取最新数据...');

    // 减少延迟时间，提高响应速度
    await new Promise(resolve => setTimeout(resolve, 300));

    await getAllSettings();

    // 更新原始设置（转换为存储格式以便比较）
    originalSettings.value = {};
    Object.keys(settingsMap.value).forEach(key => {
      let value = settingsMap.value[key];
      let originalType = typeof value;
      if (value instanceof Date) {
        // 将日期转换为YYYY-MM-DD格式
        value = value.toISOString().split('T')[0];
        originalType = 'Date->' + typeof value;
      } else if (typeof value === 'boolean') {
        // 将布尔值转换为字符串
        value = value.toString();
        originalType = 'boolean->' + typeof value;
      } else if (value === null || value === undefined) {
        // 将 null/undefined 转换为空字符串
        value = '';
        originalType = 'null/undef->string';
      }
      originalSettings.value[key] = value;

      // 对关键字段添加调试信息
      if (['blog_start_time', 'blog_avatar', 'blog_signature', 'author_title', 'author_bio', 'github_url', 'weibo_url', 'wechat_qr', 'author_location', 'personal_website'].includes(key)) {
        console.log(`保存后-原始设置 ${key} (${originalType}):`, value);
      }
    });

    ElMessage.success(`成功保存 ${modifiedSettings.length} 项设置`);
    
  } catch (error) {
    console.error('保存设置失败详情:', error);
    // 根据错误类型显示不同的错误信息
    if (error.message && error.message.includes('Request method')) {
      ElMessage.error('保存设置失败: API方法不支持，请检查后端配置');
    } else if (error.message && error.message.includes('Network Error')) {
      ElMessage.error('保存设置失败: 网络连接错误，请检查网络配置');
    } else {
      ElMessage.error(`保存设置失败: ${error.message || '请稍后重试'}`);
    }
  } finally {
    loading.value = false;
  }
}





/**
 * 验证头像URL格式
 * 专注于文件上传，不再支持Base64格式
 */
function validateAvatarUrl(url) {
  if (!url) return '';

  // 如果是base64格式，直接清空并提示用户使用文件上传
  if (url.startsWith('data:image/')) {
    ElMessage.warning('不再支持Base64格式头像，请使用文件上传功能');
    return '';
  }

  // 如果是相对路径，确保格式正确
  if (!url.startsWith('http') && !url.startsWith('/')) {
    url = '/' + url;
  }

  return url;
}

/**
 * 处理单个设置项的变更保存
 */
async function handleSingleSettingChange(key, value) {
  try {
    console.log(`开始保存单个设置: ${key} = ${value}`);

    // 处理日期类型的值
    let processedValue = value;
    if (value instanceof Date) {
      processedValue = value.toISOString().split('T')[0];
      console.log(`日期 ${key} 转换为: ${processedValue}`);
    }

    // 使用 updateSettingValueByKey 方法，这个方法会自动处理不存在的情况
    const response = await updateSettingValueByKey(key, processedValue);

    if (response.code === 200) {
      console.log(`✅ ${key} 保存成功`);
      ElMessage.success(`设置 ${key} 保存成功`);

      // 更新原始设置以避免重复保存
      originalSettings.value[key] = processedValue;
      console.log(`原始设置已更新: ${key} = ${processedValue}`);

      // 如果是头像设置，同时更新前台store
      if (key === 'blog_avatar' && processedValue) {
        console.log('更新前台store中的头像设置...');
        const blogSettingsStore = useBlogSettingsStore();
        if (blogSettingsStore && blogSettingsStore.updateBlogAvatar) {
          blogSettingsStore.updateBlogAvatar(processedValue);
        }
      }
    } else {
      throw new Error(response.msg || '操作失败');
    }
  } catch (error) {
    console.error(`❌ 保存设置 ${key} 失败:`, error);
    ElMessage.error(`保存设置 ${key} 失败: ${error.message}`);
    throw error;
  }
}

/**
 * 强制刷新前台头像显示
 */
function refreshFrontendAvatar() {
  const blogSettingsStore = useBlogSettingsStore();

  // 如果当前有头像设置，更新前台store
  if (settingsMap.value.blog_avatar) {
    blogSettingsStore.updateBlogAvatar(settingsMap.value.blog_avatar);
    console.log('前台头像已刷新:', settingsMap.value.blog_avatar);
  }

  // 刷新前台二维码
  if (settingsMap.value.wechat_qr) {
    blogSettingsStore.updateBlogSettings({
      wechat_qr: settingsMap.value.wechat_qr
    });
  }
}

/**
 * 初始化获取设置
 */
onMounted(() => {
  getAllSettings().then(() => {
    // 设置加载完成后，强制刷新前台头像显示
    setTimeout(() => {
      refreshFrontendAvatar();
    }, 1000);
  });
});

/**
 * 头像上传前检查
 */
function handleAvatarBeforeUpload(file) {
  // 检查文件类型
  const isImage = file.type.startsWith('image/');
  if (!isImage) {
    ElMessage.error('只能上传图片文件!');
    return false;
  }

  // 检查文件大小 (10MB)
  const isLt10M = file.size / 1024 / 1024 < 10;
  if (!isLt10M) {
    ElMessage.error('头像图片大小不能超过 10MB!');
    return false;
  }

  ElMessage.info('正在上传并压缩头像...');
  return true;
}

/**
 * 头像上传成功回调
 */
function handleAvatarUploadSuccess(response, uploadFile) {
  if (response.code === 200) {
    // 处理头像URL
    let avatarUrl = response.url;

    // 确保URL格式正确
    if (avatarUrl && !avatarUrl.startsWith('http')) {
      // 如果是相对路径，直接使用后端返回的路径
      // 开发环境会通过vite代理访问，生产环境会通过nginx代理访问
      if (!avatarUrl.startsWith('/')) {
        avatarUrl = '/' + avatarUrl;
      }
    }

    // 验证URL格式
    avatarUrl = validateAvatarUrl(avatarUrl);

    settingsMap.value.blog_avatar = avatarUrl;
    ElMessage.success('头像上传成功！已自动压缩为200x200尺寸');

    // 同时更新前台store
    const blogSettingsStore = useBlogSettingsStore();
    if (blogSettingsStore && blogSettingsStore.updateBlogAvatar) {
      blogSettingsStore.updateBlogAvatar(avatarUrl);
    }

    // 自动保存头像设置
    setTimeout(() => {
      handleSingleSettingChange('blog_avatar', avatarUrl);
    }, 500);
  } else {
    ElMessage.error('头像上传失败: ' + response.msg);
  }
}

/**
 * 二维码上传前检查
 */
function handleQRCodeBeforeUpload(file) {
  // 检查文件类型
  const isImage = file.type.startsWith('image/');
  if (!isImage) {
    ElMessage.error('只能上传图片文件!');
    return false;
  }

  // 检查文件大小 (10MB)
  const isLt10M = file.size / 1024 / 1024 < 10;
  if (!isLt10M) {
    ElMessage.error('二维码图片大小不能超过 10MB!');
    return false;
  }

  ElMessage.info('正在上传并压缩二维码...');
  return true;
}

/**
 * 二维码上传成功回调
 */
function handleQRCodeUploadSuccess(response, uploadFile) {
  if (response.code === 200) {
    // 处理二维码URL
    let qrCodeUrl = response.url;

    // 确保URL格式正确
    if (qrCodeUrl && !qrCodeUrl.startsWith('http')) {
      // 如果是相对路径，直接使用后端返回的路径
      // 开发环境会通过vite代理访问，生产环境会通过nginx代理访问
      if (!qrCodeUrl.startsWith('/')) {
        qrCodeUrl = '/' + qrCodeUrl;
      }
    }

    settingsMap.value.wechat_qr = qrCodeUrl;
    ElMessage.success('二维码上传成功！已自动压缩为400x400尺寸');

    // 自动保存二维码设置
    setTimeout(() => {
      handleSingleSettingChange('wechat_qr', qrCodeUrl);
    }, 500);
  } else {
    ElMessage.error('二维码上传失败: ' + response.msg);
  }
}


// 添加一个测试函数用于验证数据库连接
async function testDatabaseConnection() {
  console.log('=== 开始数据库连接测试 ===');

  try {
    // 1. 测试获取所有设置
    console.log('1. 测试获取所有设置...');
    const allSettingsResponse = await listSetting({});
    console.log('获取所有设置成功:', allSettingsResponse);

    // 2. 测试获取特定设置（如果存在）
    console.log('2. 测试获取特定设置...');
    const testKey = 'blog_name'; // 尝试获取一个可能存在的设置
    const specificResponse = await listSetting({ configKey: testKey });
    console.log(`获取设置 ${testKey}:`, specificResponse);

    // 3. 测试添加一个测试设置
    console.log('3. 测试添加测试设置...');
    const testConfig = {
      configKey: 'test_setting_' + Date.now(),
      configValue: 'test_value_' + Date.now(),
      configName: '测试设置',
      configType: 'N'
    };

    try {
      const addResponse = await addSetting(testConfig);
      console.log('添加测试设置成功:', addResponse);

      // 4. 测试更新刚添加的设置
      console.log('4. 测试更新测试设置...');
      const updateData = {
        ...testConfig,
        configValue: 'updated_value_' + Date.now()
      };
      const updateResponse = await updateSetting(updateData);
      console.log('更新测试设置成功:', updateResponse);

    } catch (testErr) {
      console.error('测试设置操作失败:', testErr);
    }

    console.log('=== 数据库连接测试完成 ===');

  } catch (error) {
    console.error('数据库连接测试失败:', error);
  }
}

// 将测试函数暴露到全局，方便在控制台调用
window.testDatabaseConnection = testDatabaseConnection;

/**
 * 重置设置
 */
async function resetSettings() {
  try {
    await proxy.$modal.confirm('确定要重置所有设置吗？此操作不可恢复！', '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    });

    loading.value = true;
    // 重新加载原始设置
    await getAllSettings();
    proxy.$modal.msgSuccess('设置已重置');
  } catch (error) {
    // 用户取消操作
  } finally {
    loading.value = false;
  }
}
</script>

<style scoped>
.blog-setting-card {
  margin-bottom: 20px;
}

/* 图片压缩功能样式 */
.compress-feature {
  text-align: center;
  padding: 15px;
  border-radius: 8px;
  background: white;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  transition: transform 0.2s ease;
}

.compress-feature:hover {
  transform: translateY(-2px);
}

.compress-feature h4 {
  margin: 0 0 10px 0;
  color: #409EFF;
  font-size: 16px;
}

.compress-feature p {
  margin: 0 0 15px 0;
  font-size: 12px;
  color: #666;
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
  color: #888;
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
  border: 1px dashed #d9d9d9;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #999;
  cursor: pointer;
  transition: all 0.3s;
}

.avatar-uploader-icon:hover {
  color: #409EFF;
  border-color: #409EFF;
}

.avatar-input {
  flex: 1;
}

/* 表单样式优化 */
.el-form {
  padding: 20px;
  background-color: #fafafa;
  border-radius: 8px;
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
}
</style>