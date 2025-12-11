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
                  action=""
                  :on-change="handleAvatarChange"
                  :show-file-list="false"
                  :auto-upload="false"
                >
                  <img v-if="settingsMap.blog_avatar" :src="settingsMap.blog_avatar" class="avatar" />
                  <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                </el-upload>
                <el-input v-model="settingsMap.blog_avatar" class="avatar-input" placeholder="或直接输入头像URL" />
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
            <el-form-item label="知乎地址" prop="zhihu_url">
              <el-input v-model="settingsMap.zhihu_url" placeholder="请输入知乎地址" />
            </el-form-item>
            <el-form-item label="微信二维码" prop="wechat_qr">
              <div class="avatar-upload">
                <el-upload
                  class="avatar-uploader"
                  action=""
                  :on-change="handleWechatQRChange"
                  :show-file-list="false"
                  :auto-upload="false"
                >
                  <img v-if="settingsMap.wechat_qr" :src="settingsMap.wechat_qr" class="avatar" />
                  <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                </el-upload>
                <el-input v-model="settingsMap.wechat_qr" class="avatar-input" placeholder="或直接输入二维码URL" />
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

        <!-- 技能管理 -->
        <el-tab-pane label="技能管理" name="skills">
          <div class="skills-container">
            <div class="skills-header">
              <h3>技能列表</h3>
              <el-button type="primary" size="small" @click="addSkill">
                <i class="el-icon-plus"></i> 添加技能
              </el-button>
            </div>
            <div class="skills-list">
              <div v-for="(skill, index) in skillsList" :key="index" class="skill-item">
                <el-row :gutter="20">
                  <el-col :span="6">
                    <el-input v-model="skill.name" placeholder="技能名称" />
                  </el-col>
                  <el-col :span="4">
                    <el-input-number v-model="skill.level" :min="0" :max="100" placeholder="熟练度" />
                  </el-col>
                  <el-col :span="4">
                    <el-color-picker v-model="skill.color" show-alpha />
                  </el-col>
                  <el-col :span="8">
                    <el-input v-model="skill.description" placeholder="技能描述" />
                  </el-col>
                  <el-col :span="2">
                    <el-button type="danger" size="small" @click="removeSkill(index)">
                      <i class="el-icon-delete"></i>
                    </el-button>
                  </el-col>
                </el-row>
              </div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 经历管理 -->
        <el-tab-pane label="经历管理" name="experience">
          <div class="experience-container">
            <div class="experience-header">
              <h3>成长历程</h3>
              <el-button type="primary" size="small" @click="addExperience">
                <i class="el-icon-plus"></i> 添加经历
              </el-button>
            </div>
            <div class="experience-list">
              <div v-for="(exp, index) in experienceList" :key="index" class="experience-item">
                <el-row :gutter="20">
                  <el-col :span="6">
                    <el-input v-model="exp.date" placeholder="时间段" />
                  </el-col>
                  <el-col :span="6">
                    <el-input v-model="exp.title" placeholder="职位/学历" />
                  </el-col>
                  <el-col :span="8">
                    <el-input v-model="exp.description" placeholder="描述" />
                  </el-col>
                  <el-col :span="4">
                    <el-button type="danger" size="small" @click="removeExperience(index)">
                      <i class="el-icon-delete"></i> 删除
                    </el-button>
                  </el-col>
                </el-row>
              </div>
            </div>
          </div>
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
    </el-card>
  </div>
</template>

<script setup name="BlogSetting">
import { ref, reactive, onMounted, getCurrentInstance } from 'vue';
import { ElMessage, ElButton, ElCard, ElTabs, ElTabPane, ElForm, ElFormItem, ElInput, ElInputNumber, ElSwitch, ElColorPicker, ElRadioGroup, ElRadio, ElDatePicker, ElUpload, ElRow, ElCol } from 'element-plus';
import { listSetting, getSetting, updateSetting, updateSettingValueByKey } from "@/api/admin/blog/setting";
import { reloadBlogSettings } from '@/utils/blogSettings';

const { proxy } = getCurrentInstance();

// 响应式数据
const loading = ref(false);
const activeTab = ref('basic');
const settingsMap = ref({});
const originalSettings = ref({});

// 技能列表数据
const skillsList = ref([]);

// 经历列表数据
const experienceList = ref([]);

/**
 * 获取所有博客设置
 */
async function getAllSettings() {
  loading.value = true;
  try {
    // 获取所有设置项
    const response = await listSetting({});
    const settingList = response.rows || response.data || [];
    
    // 将设置项转换为Map格式，并处理布尔值
    const settings = {};
    settingList.forEach(setting => {
      if (setting.settingKey) {
        let value = setting.settingValue;

        // 处理布尔值转换
        if (value === 'true') {
          value = true;
        } else if (value === 'false') {
          value = false;
        }

        settings[setting.settingKey] = value;
      }
    });

    // 设置默认值，确保所有必要的设置项都有值
    const defaultSettings = {
      blog_name: '我的博客',
      blog_desc: '欢迎来到我的博客',
      blog_author: '博主',
      blog_email: '',
      blog_url: '',
      blog_start_time: new Date(),
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
      about_content: ''
    };

    // 合并默认设置和从服务器获取的设置（服务器设置优先）
    const mergedSettings = { ...defaultSettings };

    // 用服务器获取的设置覆盖默认设置
    Object.keys(settings).forEach(key => {
      if (settings[key] !== undefined && settings[key] !== null) {
        mergedSettings[key] = settings[key];
      }
    });

    // 保存设置到响应式数据
    settingsMap.value = mergedSettings;
    // 保存原始设置用于重置（转换为字符串以便比较）
    originalSettings.value = {};
    Object.keys(mergedSettings).forEach(key => {
      let value = mergedSettings[key];
      if (typeof value === 'boolean') {
        value = value.toString();
      }
      originalSettings.value[key] = value;
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
      blog_start_time: new Date(),
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
      about_content: ''
    };
    
    settingsMap.value = defaultSettings;
    // 保存原始设置用于重置（转换为字符串以便比较）
    originalSettings.value = {};
    Object.keys(defaultSettings).forEach(key => {
      let value = defaultSettings[key];
      if (typeof value === 'boolean') {
        value = value.toString();
      }
      originalSettings.value[key] = value;
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
      if (JSON.stringify(settingsMap.value[key]) !== JSON.stringify(originalSettings.value[key])) {
        let value = settingsMap.value[key];

        // 将布尔值转换为字符串，以便保存到数据库
        if (typeof value === 'boolean') {
          value = value.toString();
        }

        modifiedSettings.push({
          key,
          value: value
        });
      }
    }
    
    if (modifiedSettings.length === 0) {
      ElMessage.success('没有修改任何设置');
      return;
    }
    
    console.log('开始保存设置，修改项数量:', modifiedSettings.length);
    
    // 批量更新设置
    const promises = modifiedSettings.map(async (setting) => {
      console.log(`正在保存设置: ${setting.key} = ${setting.value}`);
      try {
        await updateSettingValueByKey(setting.key, setting.value);
        console.log(`设置 ${setting.key} 保存成功`);
        return { success: true, key: setting.key };
      } catch (err) {
        console.error(`设置 ${setting.key} 保存失败:`, err);
        return { success: false, key: setting.key, error: err };
      }
    });
    
    const results = await Promise.all(promises);
    
    // 检查结果
    const failedSettings = results.filter(result => !result.success);
    
    if (failedSettings.length > 0) {
      const failedKeys = failedSettings.map(s => s.key).join(', ');
      throw new Error(`以下设置保存失败: ${failedKeys}`);
    }
    
    // 重新加载博客设置，使新设置立即生效
    try {
      await reloadBlogSettings();
    } catch (error) {
      console.warn('重新加载设置失败，但不影响保存:', error);
    }
    
    // 更新原始设置（转换为字符串以便比较）
    originalSettings.value = {};
    Object.keys(settingsMap.value).forEach(key => {
      let value = settingsMap.value[key];
      if (typeof value === 'boolean') {
        value = value.toString();
      }
      originalSettings.value[key] = value;
    });

    // 保存技能和经历数据
    saveSkillsData();
    saveExperienceData();

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
 * 处理头像变更
 */
function handleAvatarChange(file) {
  // 这里只是预览，实际上传需要调用上传接口
  // 在真实环境中，这里应该上传图片到服务器并获取URL
  // 现在只是简单地读取本地文件用于预览
  const reader = new FileReader();
  reader.readAsDataURL(file.raw);
  reader.onload = (e) => {
    settingsMap.value.blog_avatar = e.target.result;
  };
}

/**
 * 处理头像变更
 */
function handleAvatarChange(file) {
  // 这里只是预览，实际上传需要调用上传接口
  // 在真实环境中，这里应该上传图片到服务器并获取URL
  // 现在只是简单地读取本地文件用于预览
  const reader = new FileReader();
  reader.readAsDataURL(file.raw);
  reader.onload = (e) => {
    settingsMap.value.blog_avatar = e.target.result;
  };
}

/**
 * 处理微信二维码变更
 */
function handleWechatQRChange(file) {
  const reader = new FileReader();
  reader.readAsDataURL(file.raw);
  reader.onload = (e) => {
    settingsMap.value.wechat_qr = e.target.result;
  };
}

/**
 * 添加技能
 */
function addSkill() {
  skillsList.value.push({
    name: '',
    level: 0,
    color: '#409EFF',
    description: ''
  });
}

/**
 * 删除技能
 */
function removeSkill(index) {
  skillsList.value.splice(index, 1);
}

/**
 * 添加经历
 */
function addExperience() {
  experienceList.value.push({
    date: '',
    title: '',
    description: ''
  });
}

/**
 * 删除经历
 */
function removeExperience(index) {
  experienceList.value.splice(index, 1);
}

/**
 * 加载技能数据
 */
function loadSkillsData() {
  try {
    const skillsData = localStorage.getItem('blog_skills');
    if (skillsData) {
      skillsList.value = JSON.parse(skillsData);
    } else {
      // 默认技能数据
      skillsList.value = [
        { name: 'Vue.js', level: 90, color: '#4FC08D', description: '熟练掌握 Vue 3 全家桶，包括 Vue Router、Pinia、Element Plus 等' },
        { name: 'React', level: 75, color: '#61DAFB', description: '了解 React 生态，能够进行 React 项目开发' },
        { name: 'Node.js', level: 85, color: '#339933', description: '熟悉 Node.js 后端开发，Express、Koa 框架' },
        { name: 'Java', level: 88, color: '#ED8B00', description: '精通 Java 开发，Spring Boot、Spring Cloud 微服务架构' }
      ];
    }
  } catch (error) {
    console.error('加载技能数据失败:', error);
  }
}

/**
 * 加载经历数据
 */
function loadExperienceData() {
  try {
    const experienceData = localStorage.getItem('blog_experience');
    if (experienceData) {
      experienceList.value = JSON.parse(experienceData);
    } else {
      // 默认经历数据
      experienceList.value = [
        { date: '2024 - 至今', title: '高级全栈工程师', description: '负责公司核心产品架构设计和团队技术管理，推动技术革新和性能优化' },
        { date: '2022 - 2024', title: '全栈开发工程师', description: '参与多个大型 Web 项目开发，精通前后端技术栈，独立完成项目交付' },
        { date: '2020 - 2022', title: '前端开发工程师', description: '专注于前端开发，精通 Vue、React 等现代前端框架，推动前端工程化建设' }
      ];
    }
  } catch (error) {
    console.error('加载经历数据失败:', error);
  }
}

/**
 * 保存技能数据
 */
function saveSkillsData() {
  try {
    localStorage.setItem('blog_skills', JSON.stringify(skillsList.value));
  } catch (error) {
    console.error('保存技能数据失败:', error);
  }
}

/**
 * 保存经历数据
 */
function saveExperienceData() {
  try {
    localStorage.setItem('blog_experience', JSON.stringify(experienceList.value));
  } catch (error) {
    console.error('保存经历数据失败:', error);
  }
}

/**
 * 初始化获取设置
 */
onMounted(() => {
  getAllSettings();
  loadSkillsData();
  loadExperienceData();
});

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
    loadSkillsData();
    loadExperienceData();
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
el-form {
  padding: 20px;
  background-color: #fafafa;
  border-radius: 8px;
}

el-form-item {
  margin-bottom: 20px;
}

/* 技能和经历管理样式 */
.skills-container,
.experience-container {
  padding: 20px;
}

.skills-header,
.experience-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.skills-header h3,
.experience-header h3 {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.skills-list,
.experience-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.skill-item,
.experience-item {
  padding: 15px;
  background: #f9f9f9;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.skill-item:hover,
.experience-item:hover {
  background: #f0f0f0;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
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

  .skills-header,
  .experience-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .skill-item .el-row,
  .experience-item .el-row {
    margin: 0 !important;
  }
}
</style>