<template>
  <div class="about-container">
    <!-- 博客导航 -->
    <BlogNav />

    <!-- 博主介绍头部 -->
    <div class="hero-section">
      <div class="hero-content">
        <div
          v-animate="'fade-in-up'"
          class="hero-avatar"
        >
          <img
            :src="blogSettings.blog_avatar || '/default-avatar.jpg'"
            :alt="blogSettings.blog_author"
          />
          <div class="avatar-decoration"></div>
        </div>
        <div
          v-animate="'fade-in-up'"
          class="hero-info"
        >
          <h1 class="hero-title">
            {{ blogSettings.blog_author || 'Nevell' }}
          </h1>
          <p class="hero-subtitle">
            {{ blogSettings.author_title || '全栈开发工程师' }}
          </p>
          <p class="hero-description">
            {{
              blogSettings.blog_desc ||
                '热爱技术，热爱生活，专注于Web开发和用户体验设计，分享技术心得与生活感悟。'
            }}
          </p>

          <!-- 社交链接 -->
          <div
            v-animate="'fade-in-up'"
            class="hero-social"
          >
            <a
              :href="blogSettings.github_url || '#'"
              class="social-link"
              title="GitHub"
              target="_blank"
              rel="noopener"
            >
              <i class="el-icon-s-promotion"></i>
            </a>
            <a
              :href="blogSettings.email ? `mailto:${blogSettings.email}` : '#'"
              class="social-link"
              title="邮箱"
            >
              <i class="el-icon-message"></i>
            </a>
            <a
              href="#"
              class="social-link"
              title="微信"
              @click.prevent="showWechatQR = true"
            >
              <i class="el-icon-chat-dot-round"></i>
            </a>
            <a
              :href="blogSettings.weibo_url || '#'"
              class="social-link"
              title="微博"
              target="_blank"
              rel="noopener"
            >
              <i class="el-icon-star-off"></i>
            </a>
            <a
              :href="blogSettings.zhihu_url || '#'"
              class="social-link"
              title="知乎"
              target="_blank"
              rel="noopener"
            >
              <i class="el-icon-reading"></i>
            </a>
          </div>
        </div>
      </div>
    </div>

    <!-- 统计数据 -->
    <div
      v-animate="'fade-in-up'"
      class="stats-section"
    >
      <div class="stats-container">
        <div class="stat-card">
          <div class="stat-icon">
            <i class="el-icon-document"></i>
          </div>
          <div class="stat-content">
            <div class="stat-number">
              {{ stats.articleCount || 0 }}
            </div>
            <div class="stat-label">
              篇文章
            </div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">
            <i class="el-icon-folder-opened"></i>
          </div>
          <div class="stat-content">
            <div class="stat-number">
              {{ stats.categoryCount || 0 }}
            </div>
            <div class="stat-label">
              个分类
            </div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">
            <i class="el-icon-price-tag"></i>
          </div>
          <div class="stat-content">
            <div class="stat-number">
              {{ stats.tagCount || 0 }}
            </div>
            <div class="stat-label">
              个标签
            </div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">
            <i class="el-icon-chat-dot-round"></i>
          </div>
          <div class="stat-content">
            <div class="stat-number">
              {{ stats.commentCount || 0 }}
            </div>
            <div class="stat-label">
              条评论
            </div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">
            <i class="el-icon-view"></i>
          </div>
          <div class="stat-content">
            <div class="stat-number">
              {{ formatNumber(stats.totalViews || 0) }}
            </div>
            <div class="stat-label">
              总访问
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 技能专长 -->
    <div
      v-animate="'fade-in-up'"
      class="skills-section"
    >
      <h2 class="section-title">
        技能专长
      </h2>
      <div class="skills-container">
        <div
          v-for="(skill, index) in skills"
          :key="index"
          v-animate="'fade-in-up'"
          class="skill-item"
        >
          <div class="skill-header">
            <span class="skill-name">{{ skill.name }}</span>
            <span class="skill-level">{{ skill.level }}%</span>
          </div>
          <div class="skill-bar">
            <div
              class="skill-progress"
              :style="{ width: skill.level + '%', backgroundColor: skill.color }"
            ></div>
          </div>
          <div class="skill-description">
            {{ skill.description }}
          </div>
        </div>
      </div>
    </div>

    <!-- 工作经历 -->
    <div
      v-animate="'fade-in-up'"
      class="timeline-section"
    >
      <h2 class="section-title">
        成长历程
      </h2>
      <div class="timeline">
        <div
          v-for="(item, index) in timeline"
          :key="index"
          v-animate="'fade-in-up'"
          class="timeline-item"
        >
          <div class="timeline-marker">
            <div class="timeline-dot"></div>
          </div>
          <div class="timeline-content">
            <div class="timeline-date">
              {{ item.date }}
            </div>
            <div class="timeline-title">
              {{ item.title }}
            </div>
            <div class="timeline-description">
              {{ item.description }}
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 联系方式 -->
    <div
      v-animate="'fade-in-up'"
      class="contact-section"
    >
      <h2 class="section-title">
        联系我
      </h2>
      <div class="contact-container">
        <div class="contact-info">
          <div class="contact-item">
            <div class="contact-icon">
              <i class="el-icon-message"></i>
            </div>
            <div class="contact-details">
              <div class="contact-label">
                邮箱
              </div>
              <div class="contact-value">
                {{ blogSettings.email || 'contact@example.com' }}
              </div>
            </div>
          </div>
          <div class="contact-item">
            <div class="contact-icon">
              <i class="el-icon-location"></i>
            </div>
            <div class="contact-details">
              <div class="contact-label">
                位置
              </div>
              <div class="contact-value">
                {{ blogSettings.author_location || '中国·北京' }}
              </div>
            </div>
          </div>
          <div class="contact-item">
            <div class="contact-icon">
              <i class="el-icon-link"></i>
            </div>
            <div class="contact-details">
              <div class="contact-label">
                GitHub
              </div>
              <div class="contact-value">
                {{ blogSettings.github_url ? 'github.com/nevell' : 'github.com/username' }}
              </div>
            </div>
          </div>
        </div>

        <div class="contact-form">
          <el-form
            ref="contactFormRef"
            :model="contactForm"
            :rules="contactRules"
            label-width="0"
          >
            <el-form-item prop="name">
              <el-input
                v-model="contactForm.name"
                placeholder="您的姓名"
                size="large"
              >
                <template #prefix>
                  <i class="el-icon-user"></i>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="email">
              <el-input
                v-model="contactForm.email"
                placeholder="您的邮箱"
                size="large"
              >
                <template #prefix>
                  <i class="el-icon-message"></i>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="subject">
              <el-input
                v-model="contactForm.subject"
                placeholder="主题"
                size="large"
              >
                <template #prefix>
                  <i class="el-icon-edit"></i>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="message">
              <el-input
                v-model="contactForm.message"
                type="textarea"
                :rows="4"
                placeholder="留言内容"
                resize="none"
              />
            </el-form-item>
            <el-form-item>
              <el-button
                type="primary"
                size="large"
                :loading="submitting"
                class="submit-btn"
                @click="submitContact"
              >
                <i class="el-icon-s-promotion"></i>
                发送消息
              </el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>

    <!-- 微信二维码对话框 -->
    <el-dialog
      v-model="showWechatQR"
      title="微信二维码"
      width="300px"
      center
    >
      <div class="qr-code-container">
        <img
          v-if="blogSettings.wechat_qr"
          :src="blogSettings.wechat_qr"
          alt="微信二维码"
        />
        <div
          v-else
          class="qr-placeholder"
        >
          暂无微信二维码
        </div>
        <p>扫码添加微信</p>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import BlogNav from '@/components/BlogNav.vue'
import { getBlogSettingsAnonymous } from '@/api/blog/setting'
import { getSkillsList, getExperienceList } from '@/api/blog/author'

// 响应式数据
interface BlogSettings {
  email?: string
  weibo_url?: string
  github_url?: string
  wechat_qr?: string
  [key: string]: any
}

const blogSettings = ref<BlogSettings>({})
const stats = ref({})
const showWechatQR = ref(false)
const submitting = ref(false)
const contactFormRef = ref()

// 联系表单
const contactForm = reactive({
  name: '',
  email: '',
  subject: '',
  message: ''
})

// 表单验证规则
const contactRules = {
  name: [
    { required: true, message: '请输入您的姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '姓名长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入您的邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  subject: [
    { required: true, message: '请输入主题', trigger: 'blur' },
    { min: 5, max: 50, message: '主题长度在 5 到 50 个字符', trigger: 'blur' }
  ],
  message: [
    { required: true, message: '请输入留言内容', trigger: 'blur' },
    { min: 10, max: 500, message: '留言内容长度在 10 到 500 个字符', trigger: 'blur' }
  ]
}

// 技能数据
const skills = ref([])

// 成长历程数据
const timeline = ref([])

// 加载技能数据
const loadSkillsData = async () => {
  try {
    const response = await getSkillsList()
    if (response.data && response.data.length > 0) {
      skills.value = response.data
    } else {
      // 使用默认数据
      skills.value = [
        {
          name: 'Vue.js',
          level: 90,
          color: '#4FC08D',
          description: '熟练掌握 Vue 3 全家桶，包括 Vue Router、Pinia、Element Plus 等'
        },
        {
          name: 'React',
          level: 75,
          color: '#61DAFB',
          description: '了解 React 生态，能够进行 React 项目开发'
        },
        {
          name: 'Node.js',
          level: 85,
          color: '#339933',
          description: '熟悉 Node.js 后端开发，Express、Koa 框架'
        },
        {
          name: 'Java',
          level: 88,
          color: '#ED8B00',
          description: '精通 Java 开发，Spring Boot、Spring Cloud 微服务架构'
        },
        {
          name: 'Python',
          level: 70,
          color: '#3776AB',
          description: '掌握 Python 基础，能够进行脚本编写和数据分析'
        },
        {
          name: 'MySQL',
          level: 85,
          color: '#4479A1',
          description: '熟练使用 MySQL 数据库设计、优化和集群部署'
        },
        {
          name: 'Redis',
          level: 80,
          color: '#DC382D',
          description: '熟悉 Redis 缓存应用、数据结构和集群'
        },
        {
          name: 'Docker',
          level: 82,
          color: '#2496ED',
          description: '掌握 Docker 容器化部署和 Docker Compose 编排'
        }
      ]
    }
  } catch (error) {
    console.error('加载技能数据失败:', error)
    // 使用默认数据
    skills.value = [
      {
        name: 'Vue.js',
        level: 90,
        color: '#4FC08D',
        description: '熟练掌握 Vue 3 全家桶，包括 Vue Router、Pinia、Element Plus 等'
      },
      {
        name: 'React',
        level: 75,
        color: '#61DAFB',
        description: '了解 React 生态，能够进行 React 项目开发'
      },
      {
        name: 'Node.js',
        level: 85,
        color: '#339933',
        description: '熟悉 Node.js 后端开发，Express、Koa 框架'
      },
      {
        name: 'Java',
        level: 88,
        color: '#ED8B00',
        description: '精通 Java 开发，Spring Boot、Spring Cloud 微服务架构'
      }
    ]
  }
}

// 加载经历数据
const loadExperienceData = async () => {
  try {
    const response = await getExperienceList()
    if (response.data && response.data.length > 0) {
      timeline.value = response.data
    } else {
      // 使用默认数据
      timeline.value = [
        {
          date: '2024 - 至今',
          title: '高级全栈工程师',
          description: '负责公司核心产品架构设计和团队技术管理，推动技术革新和性能优化'
        },
        {
          date: '2022 - 2024',
          title: '全栈开发工程师',
          description: '参与多个大型 Web 项目开发，精通前后端技术栈，独立完成项目交付'
        },
        {
          date: '2020 - 2022',
          title: '前端开发工程师',
          description: '专注于前端开发，精通 Vue、React 等现代前端框架，推动前端工程化建设'
        },
        {
          date: '2019 - 2020',
          title: '初级开发工程师',
          description: '职业生涯起步，学习全栈开发技术，积累项目经验'
        },
        {
          date: '2015 - 2019',
          title: '计算机科学学位',
          description: '获得计算机科学学士学位，系统学习软件开发基础理论知识'
        }
      ]
    }
  } catch (error) {
    console.error('加载经历数据失败:', error)
    // 使用默认数据
    timeline.value = [
      {
        date: '2024 - 至今',
        title: '高级全栈工程师',
        description: '负责公司核心产品架构设计和团队技术管理，推动技术革新和性能优化'
      },
      {
        date: '2022 - 2024',
        title: '全栈开发工程师',
        description: '参与多个大型 Web 项目开发，精通前后端技术栈，独立完成项目交付'
      },
      {
        date: '2020 - 2022',
        title: '前端开发工程师',
        description: '专注于前端开发，精通 Vue、React 等现代前端框架，推动前端工程化建设'
      }
    ]
  }
}

// 加载博客设置
const loadBlogSettings = async () => {
  try {
    const response = await getBlogSettingsAnonymous()
    blogSettings.value = response || {}
  } catch (error) {
    console.error('加载博客设置失败:', error)
    // 使用默认设置
    blogSettings.value = {
      blog_author: 'Nevell',
      author_title: '全栈开发工程师',
      blog_desc: '热爱技术，热爱生活，专注于Web开发和用户体验设计，分享技术心得与生活感悟。',
      email: 'contact@example.com',
      github_url: 'https://github.com',
      weibo_url: 'https://weibo.com',
      wechat_qr: ''
    }
  }
}

// 加载统计数据
const loadStats = () => {
  // 模拟数据，实际应该从API获取
  stats.value = {
    articleCount: 42,
    categoryCount: 8,
    tagCount: 25,
    commentCount: 168,
    totalViews: 15230
  }
}

// 格式化数字
const formatNumber = num => {
  if (num >= 10000) {
    return (num / 10000).toFixed(1) + 'W'
  } else if (num >= 1000) {
    return (num / 1000).toFixed(1) + 'K'
  }
  return num.toString()
}

// 提交联系表单
const submitContact = async () => {
  try {
    await contactFormRef.value.validate()
    submitting.value = true

    // 这里应该调用API发送邮件
    await new Promise(resolve => setTimeout(resolve, 1000)) // 模拟API调用

    ElMessage.success('消息已发送，我会尽快回复您！')
    // 重置表单
    Object.keys(contactForm).forEach(key => {
      contactForm[key] = ''
    })
  } catch (error) {
    console.error('提交失败:', error)
    if (error !== 'validation_failed') {
      ElMessage.error('发送失败，请稍后重试')
    }
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  loadBlogSettings()
  loadStats()
  loadSkillsData()
  loadExperienceData()
})
</script>

<style scoped>
.about-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  position: relative;
}

/* 博主介绍头部 */
.hero-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 120px 0 80px;
  position: relative;
  overflow: hidden;
}

.hero-section::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background:
    radial-gradient(circle at 30% 20%, rgba(255, 255, 255, 0.1) 3px, transparent 4px),
    radial-gradient(circle at 70% 60%, rgba(255, 255, 255, 0.08) 2px, transparent 3px),
    radial-gradient(circle at 40% 80%, rgba(255, 255, 255, 0.06) 4px, transparent 5px);
  background-size:
    150px 150px,
    100px 100px,
    200px 200px;
  animation: floatStars 25s linear infinite;
}

@keyframes floatStars {
  0% {
    transform: translate(0, 0);
  }
  100% {
    transform: translate(-50px, -50px);
  }
}

.hero-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  display: flex;
  align-items: center;
  gap: 60px;
  position: relative;
  z-index: 1;
}

.hero-avatar {
  position: relative;
  flex-shrink: 0;
}

.hero-avatar img {
  width: 200px;
  height: 200px;
  border-radius: 50%;
  border: 6px solid rgba(255, 255, 255, 0.9);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.2);
  transition: transform 0.3s ease;
}

.hero-avatar:hover img {
  transform: scale(1.05);
}

.avatar-decoration {
  position: absolute;
  top: -10px;
  left: -10px;
  right: -10px;
  bottom: -10px;
  border: 3px solid rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  animation: rotate 20s linear infinite;
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.hero-info {
  flex: 1;
}

.hero-title {
  font-size: 3.5rem;
  font-weight: 700;
  margin-bottom: 15px;
  background: linear-gradient(45deg, #fff, #f0f8ff);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.hero-subtitle {
  font-size: 1.5rem;
  margin-bottom: 20px;
  opacity: 0.9;
  font-weight: 500;
}

.hero-description {
  font-size: 1.1rem;
  line-height: 1.8;
  margin-bottom: 30px;
  opacity: 0.85;
  max-width: 600px;
}

.hero-social {
  display: flex;
  gap: 15px;
}

.social-link {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 50px;
  height: 50px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 50%;
  color: white;
  font-size: 1.3rem;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.social-link:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateY(-5px);
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
}

.social-link::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.3), transparent);
  transition: left 0.5s ease;
}

.social-link:hover::before {
  left: 100%;
}

/* 统计数据 */
.stats-section {
  padding: 80px 20px;
  background: white;
}

.stats-container {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  gap: 30px;
}

.stat-card {
  flex: 1;
  background: white;
  border-radius: 20px;
  padding: 30px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.08);
  display: flex;
  align-items: center;
  gap: 20px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1px solid rgba(0, 0, 0, 0.05);
  backdrop-filter: blur(10px);
}

.stat-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
}

.stat-icon {
  width: 60px;
  height: 60px;
  background: linear-gradient(135deg, #409eff, #337ecc);
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  color: white;
}

.stat-number {
  font-size: 2.5rem;
  font-weight: 700;
  color: #333;
  line-height: 1;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 0.95rem;
  color: #666;
}

/* 技能专长 */
.skills-section {
  padding: 80px 20px;
  background: #f8f9fa;
}

.section-title {
  text-align: center;
  font-size: 2.5rem;
  color: #333;
  margin-bottom: 50px;
  position: relative;
}

.section-title::after {
  content: '';
  position: absolute;
  bottom: -10px;
  left: 50%;
  transform: translateX(-50%);
  width: 60px;
  height: 4px;
  background: linear-gradient(90deg, #409eff, #337ecc);
  border-radius: 2px;
}

.skills-container {
  max-width: 1000px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(450px, 1fr));
  gap: 30px;
}

.skill-item {
  background: white;
  border-radius: 16px;
  padding: 25px;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

.skill-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.12);
}

.skill-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.skill-name {
  font-size: 1.1rem;
  font-weight: 600;
  color: #333;
}

.skill-level {
  font-size: 1rem;
  font-weight: 600;
  color: #666;
}

.skill-bar {
  height: 8px;
  background: #f0f0f0;
  border-radius: 4px;
  overflow: hidden;
  margin-bottom: 15px;
}

.skill-progress {
  height: 100%;
  border-radius: 4px;
  transition: width 1s ease-in-out;
  background: linear-gradient(90deg, #409eff, #337ecc);
}

.skill-description {
  color: #666;
  font-size: 0.9rem;
  line-height: 1.6;
}

/* 成长历程 */
.timeline-section {
  padding: 80px 20px;
  background: white;
}

.timeline {
  max-width: 1000px;
  margin: 0 auto;
  position: relative;
  padding: 20px 0;
}

.timeline::before {
  content: '';
  position: absolute;
  left: 50%;
  top: 0;
  bottom: 0;
  width: 3px;
  background: linear-gradient(180deg, #409eff, #337ecc);
  transform: translateX(-50%);
}

.timeline-item {
  display: flex;
  margin-bottom: 50px;
  position: relative;
}

.timeline-item:nth-child(odd) {
  flex-direction: row-reverse;
}

.timeline-marker {
  position: absolute;
  left: 50%;
  top: 0;
  transform: translateX(-50%);
  z-index: 2;
}

.timeline-dot {
  width: 20px;
  height: 20px;
  background: white;
  border: 4px solid #409eff;
  border-radius: 50%;
  transition: all 0.3s ease;
}

.timeline-item:hover .timeline-dot {
  transform: translateX(-50%) scale(1.3);
  box-shadow: 0 0 20px rgba(64, 158, 255, 0.5);
}

.timeline-content {
  width: 45%;
  background: white;
  padding: 25px;
  border-radius: 16px;
  box-shadow: 0 5px 25px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  border: 1px solid rgba(0, 0, 0, 0.05);
}

.timeline-content:hover {
  transform: scale(1.02);
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.12);
}

.timeline-date {
  font-size: 0.9rem;
  color: #409eff;
  font-weight: 600;
  margin-bottom: 10px;
}

.timeline-title {
  font-size: 1.3rem;
  font-weight: 700;
  color: #333;
  margin-bottom: 15px;
}

.timeline-description {
  color: #666;
  line-height: 1.8;
  font-size: 0.95rem;
}

/* 联系方式 */
.contact-section {
  padding: 80px 20px 100px;
  background: #f8f9fa;
}

.contact-container {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 60px;
}

.contact-info {
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.contact-item {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 25px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

.contact-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.12);
}

.contact-icon {
  width: 60px;
  height: 60px;
  background: linear-gradient(135deg, #409eff, #337ecc);
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  color: white;
  flex-shrink: 0;
}

.contact-label {
  font-size: 0.9rem;
  color: #666;
  margin-bottom: 5px;
}

.contact-value {
  font-size: 1.1rem;
  font-weight: 600;
  color: #333;
}

.contact-form {
  background: white;
  padding: 40px;
  border-radius: 20px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.08);
}

.submit-btn {
  width: 100%;
  height: 50px;
  font-size: 1.1rem;
  font-weight: 600;
  border-radius: 10px;
  background: linear-gradient(135deg, #409eff, #337ecc);
  transition: all 0.3s ease;
}

.submit-btn:hover {
  background: linear-gradient(135deg, #337ecc, #2575fc);
  transform: translateY(-2px);
  box-shadow: 0 10px 30px rgba(64, 158, 255, 0.3);
}

/* 二维码对话框 */
.qr-code-container {
  text-align: center;
  padding: 20px;
}

.qr-code-container img {
  width: 200px;
  height: 200px;
  border-radius: 10px;
  margin-bottom: 15px;
}

.qr-placeholder {
  width: 200px;
  height: 200px;
  border-radius: 10px;
  margin-bottom: 15px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f5f5;
  color: #999;
  font-size: 0.95rem;
  border: 2px dashed #ddd;
}

.qr-code-container p {
  color: #666;
  font-size: 0.95rem;
  margin: 0;
}

/* 响应式设计 */
@media (max-width: 992px) {
  .hero-content {
    flex-direction: column;
    text-align: center;
    gap: 40px;
  }

  .hero-title {
    font-size: 2.8rem;
  }

  .stats-container {
    flex-wrap: wrap;
  }

  .stat-card {
    min-width: calc(50% - 15px);
  }

  .skills-container {
    grid-template-columns: 1fr;
  }

  .timeline::before {
    left: 30px;
  }

  .timeline-item {
    flex-direction: row !important;
    margin-left: 60px;
  }

  .timeline-content {
    width: calc(100% - 60px);
  }

  .timeline-marker {
    left: 30px;
  }

  .contact-container {
    grid-template-columns: 1fr;
    gap: 40px;
  }
}

@media (max-width: 768px) {
  .hero-section {
    padding: 80px 0 60px;
  }

  .hero-avatar img {
    width: 150px;
    height: 150px;
  }

  .hero-title {
    font-size: 2.2rem;
  }

  .hero-subtitle {
    font-size: 1.2rem;
  }

  .stats-container {
    flex-direction: column;
  }

  .stat-card {
    min-width: 100%;
  }

  .section-title {
    font-size: 2rem;
  }

  .timeline-item {
    margin-left: 40px;
  }

  .contact-form {
    padding: 30px 20px;
  }
}

@media (max-width: 480px) {
  .hero-title {
    font-size: 1.8rem;
  }

  .hero-description {
    font-size: 1rem;
  }

  .social-link {
    width: 40px;
    height: 40px;
    font-size: 1.1rem;
  }

  .stat-number {
    font-size: 2rem;
  }

  .contact-item {
    padding: 20px;
  }

  .contact-icon {
    width: 50px;
    height: 50px;
    font-size: 1.3rem;
  }
}
</style>
