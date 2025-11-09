<template>
  <div class="about-container">
    <!-- 博客导航 -->
    <BlogNav />

    <!-- 关于页面内容 -->
    <div class="about-content">
      <div class="about-header">
        <h1 class="about-title">关于我们</h1>
        <p class="about-subtitle">了解我们的博客平台</p>
      </div>

      <div class="about-body">
        <el-row :gutter="30">
          <el-col :xs="24" :lg="16">
            <div class="about-info">
              <h2>博客简介</h2>
              <p>
                这是一个基于RuoYi-Vue框架开发的现代化博客系统，集成了前后端分离架构，
                提供了完整的博客管理功能。系统采用最新的技术栈，具有良好的扩展性和维护性。
              </p>
              
              <h2>技术栈</h2>
              <el-row :gutter="20">
                <el-col :span="12">
                  <h3>前端技术</h3>
                  <ul>
                    <li>Vue 3 + Composition API</li>
                    <li>Vue Router 4</li>
                    <li>Element Plus</li>
                    <li>Vite</li>
                    <li>Axios</li>
                  </ul>
                </el-col>
                <el-col :span="12">
                  <h3>后端技术</h3>
                  <ul>
                    <li>Spring Boot 2.5</li>
                    <li>Spring Security</li>
                    <li>MyBatis Plus</li>
                    <li>MySQL 8.0</li>
                    <li>Redis</li>
                  </ul>
                </el-col>
              </el-row>

              <h2>功能特性</h2>
              <el-row :gutter="20">
                <el-col :span="8">
                  <div class="feature-card">
                    <i class="el-icon-document feature-icon"></i>
                    <h3>文章管理</h3>
                    <p>支持文章的发布、编辑、分类和标签管理</p>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="feature-card">
                    <i class="el-icon-chat-dot-round feature-icon"></i>
                    <h3>评论系统</h3>
                    <p>完整的评论功能，支持回复和审核</p>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="feature-card">
                    <i class="el-icon-user feature-icon"></i>
                    <h3>用户管理</h3>
                    <p>多角色权限管理，支持用户注册和登录</p>
                  </div>
                </el-col>
              </el-row>
            </div>
          </el-col>
          
          <el-col :xs="24" :lg="8">
            <div class="about-sidebar">
              <div class="contact-card">
                <h3>联系我们</h3>
                <p><i class="el-icon-message"></i> Email: contact@blog.com</p>
                <p><i class="el-icon-phone"></i> 电话: 123-456-7890</p>
                <p><i class="el-icon-location"></i> 地址: 北京市朝阳区</p>
              </div>
              
              <div class="stat-card">
                <h3>博客统计</h3>
                <div class="stat-item">
                  <span class="stat-label">文章总数</span>
                  <span class="stat-value">{{ stats.articleCount || 0 }}</span>
                </div>
                <div class="stat-item">
                  <span class="stat-label">分类数量</span>
                  <span class="stat-value">{{ stats.categoryCount || 0 }}</span>
                </div>
                <div class="stat-item">
                  <span class="stat-label">标签数量</span>
                  <span class="stat-value">{{ stats.tagCount || 0 }}</span>
                </div>
                <div class="stat-item">
                  <span class="stat-label">评论数量</span>
                  <span class="stat-value">{{ stats.commentCount || 0 }}</span>
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import BlogNav from '@/components/BlogNav.vue'

const stats = ref({})

onMounted(() => {
  loadStats()
})

const loadStats = () => {
  // 这里可以调用API获取统计数据
  stats.value = {
    articleCount: 0,
    categoryCount: 0,
    tagCount: 0,
    commentCount: 0
  }
}
</script>

<style scoped>
.about-container {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.about-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
}

.about-header {
  text-align: center;
  margin-bottom: 50px;
}

.about-title {
  font-size: 36px;
  color: #333;
  margin-bottom: 10px;
}

.about-subtitle {
  font-size: 18px;
  color: #666;
}

.about-body {
  background: white;
  border-radius: 8px;
  padding: 40px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.about-info h2 {
  color: #333;
  margin-bottom: 20px;
  border-left: 4px solid #409eff;
  padding-left: 10px;
}

.about-info h3 {
  color: #666;
  margin: 20px 0 10px;
}

.about-info ul {
  padding-left: 20px;
}

.about-info li {
  margin-bottom: 8px;
  color: #666;
}

.feature-card {
  text-align: center;
  padding: 20px;
  border: 1px solid #e6e6e6;
  border-radius: 8px;
  margin-bottom: 20px;
}

.feature-icon {
  font-size: 36px;
  color: #409eff;
  margin-bottom: 15px;
}

.feature-card h3 {
  margin: 10px 0;
  color: #333;
}

.feature-card p {
  color: #666;
  font-size: 14px;
}

.about-sidebar {
  position: sticky;
  top: 20px;
}

.contact-card,
.stat-card {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
}

.contact-card h3,
.stat-card h3 {
  margin-bottom: 15px;
  color: #333;
}

.contact-card p {
  margin-bottom: 10px;
  color: #666;
}

.contact-card i {
  margin-right: 10px;
  color: #409eff;
}

.stat-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #e6e6e6;
}

.stat-item:last-child {
  border-bottom: none;
}

.stat-label {
  color: #666;
}

.stat-value {
  font-weight: bold;
  color: #409eff;
}

@media (max-width: 992px) {
  .about-content {
    padding: 20px 10px;
  }
  
  .about-body {
    padding: 20px;
  }
}
</style>