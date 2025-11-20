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
import { ElMessage, ElMessageBox, ElButton, ElCard, ElTabs, ElTabPane, ElForm, ElFormItem, ElInput, ElInputNumber, ElSwitch, ElColorPicker, ElRadioGroup, ElRadio, ElDatePicker, ElUpload } from 'element-plus';
import { listSetting, getSetting, updateSetting } from "@/api/admin/blog/setting";
import { reloadBlogSettings } from '@/utils/blogSettings';

const { proxy } = getCurrentInstance();

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
    // 获取所有设置项
    const response = await listSetting({});
    const settingList = response.rows || response.data || [];
    
    // 将设置项转换为Map格式
    const settings = {};
    settingList.forEach(setting => {
      if (setting.settingKey) {
        settings[setting.settingKey] = setting.settingValue;
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
    
    // 合并默认设置和从服务器获取的设置
    const mergedSettings = { ...defaultSettings, ...settings };
    
    // 保存设置到响应式数据
    settingsMap.value = mergedSettings;
    // 保存原始设置用于重置
    originalSettings.value = { ...mergedSettings };
    
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
    originalSettings.value = { ...defaultSettings };
    
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
        modifiedSettings.push({
          key,
          value: settingsMap.value[key]
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
    
    // 更新原始设置
    originalSettings.value = { ...settingsMap.value };
    
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
 * 初始化获取设置
 */
onMounted(() => {
  getAllSettings();
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