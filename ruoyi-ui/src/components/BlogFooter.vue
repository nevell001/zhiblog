<template>
  <footer v-if="shouldShowFooter" class="blog-footer">
    <div class="footer-content">
      <div
        v-if="blogSettings.footer_enabled === 'true' || blogSettings.footer_enabled === true"
        class="footer-info"
      >
        <div class="footer-section">
          <h4>关于博客</h4>
          <p>{{ blogSettings.blog_desc || '一个基于RuoYi-Vue的博客系统' }}</p>
          <div class="footer-stats">
            <span>文章: {{ totalArticles }} 篇</span>
            <span>分类: {{ categoryCount }} 个</span>
            <span>标签: {{ tagCount }} 个</span>
          </div>
        </div>

        <div class="footer-section">
          <h4>快速链接</h4>
          <ul class="footer-links">
            <li>
              <router-link to="/blog">首页</router-link>
            </li>
            <li>
              <router-link to="/blog/about">关于</router-link>
            </li>
            <li>
              <router-link to="/blog/category">分类</router-link>
            </li>
            <li>
              <router-link to="/blog/tag">标签</router-link>
            </li>
            <li>
              <router-link to="/blog/archive">归档</router-link>
            </li>
            <li>
              <a :href="rssUrl" target="_blank" title="RSS订阅">RSS订阅</a>
            </li>
          </ul>
        </div>

        <div class="footer-section">
          <h4>联系方式</h4>
          <div class="footer-contact">
            <p v-if="blogSettings.blog_email">
              <i class="el-icon-message"></i>
              {{ blogSettings.blog_email }}
            </p>
            <p v-if="blogSettings.blog_beian">
              <i class="el-icon-document"></i>
              {{ blogSettings.blog_beian }}
            </p>
          </div>
        </div>
      </div>

      <div v-if="shouldShowCopyright" class="footer-copyright">
        <div class="copyright-content">
          <p>
            <span v-if="blogSettings.blog_copyright">{{ blogSettings.blog_copyright }}</span>
            <span v-else>
              © {{ currentYear }} {{ blogSettings.blog_author || '博客作者' }}. All rights reserved.
            </span>
          </p>
          <p class="tech-info">
            Powered by
            <a href="#" target="_blank">RuoYi-Vue</a>
            &
            <a href="#" target="_blank">Element Plus</a>
          </p>
        </div>
      </div>
    </div>
  </footer>
</template>

<script setup>
import { computed } from 'vue'

// 定义props
const props = defineProps({
  blogSettings: {
    type: Object,
    default: () => ({})
  },
  totalArticles: {
    type: Number,
    default: 0
  },
  categoryCount: {
    type: Number,
    default: 0
  },
  tagCount: {
    type: Number,
    default: 0
  }
})

// 计算RSS URL
const rssUrl = computed(() => {
  // 根据当前环境构建RSS URL
  // 在浏览器中，需要使用localhost而不是Docker内部地址
  let baseUrl = 'http://localhost:8080'

  // 如果当前在localhost:3000，使用localhost:8080
  if (window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1') {
    baseUrl = 'http://localhost:8080'
  } else {
    // 否则使用当前域名，但端口改为8080
    baseUrl = `${window.location.protocol}//${window.location.hostname}:8080`
  }

  return `${baseUrl}/blog/rss`
})

// 计算当前年份
const currentYear = computed(() => {
  return new Date().getFullYear()
})

// 是否显示底部
const shouldShowFooter = computed(() => {
  return props.blogSettings.footer_enabled === 'true' || props.blogSettings.footer_enabled === true
})

// 是否显示版权
const shouldShowCopyright = computed(() => {
  return (
    props.blogSettings.copyright_enabled === 'true' || props.blogSettings.copyright_enabled === true
  )
})
</script>

<style scoped>
.blog-footer {
  background: linear-gradient(135deg, #2c3e50 0%, #34495e 100%);
  color: #ecf0f1;
  margin-top: 60px;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.1);
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px 20px;
}

.footer-info {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 40px;
  margin-bottom: 40px;
  padding-bottom: 30px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.footer-section h4 {
  margin: 0 0 20px 0;
  color: #3498db;
  font-size: 1.2rem;
  font-weight: 600;
  position: relative;
  padding-bottom: 10px;
}

.footer-section h4::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 30px;
  height: 2px;
  background: #3498db;
}

.footer-section p {
  margin: 0 0 15px 0;
  line-height: 1.6;
  color: #bdc3c7;
}

.footer-stats {
  display: flex;
  gap: 20px;
  margin-top: 15px;
}

.footer-stats span {
  background: rgba(52, 152, 219, 0.2);
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 0.9rem;
  color: #3498db;
}

.footer-links {
  list-style: none;
  padding: 0;
  margin: 0;
}

.footer-links li {
  margin-bottom: 10px;
}

.footer-links a {
  color: #bdc3c7;
  text-decoration: none;
  transition: all 0.3s ease;
  display: inline-block;
}

.footer-links a:hover {
  color: #3498db;
  transform: translateX(5px);
}

.footer-links a .el-icon {
  margin-right: 4px;
  font-size: 0.9em;
  vertical-align: middle;
}

.footer-contact p {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 10px;
}

.footer-contact i {
  color: #3498db;
  font-size: 1.1rem;
}

.footer-copyright {
  text-align: center;
  padding-top: 20px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.copyright-content p {
  margin: 8px 0;
  font-size: 0.9rem;
  color: #95a5a6;
}

.tech-info {
  font-size: 0.85rem !important;
}

.tech-info a {
  color: #3498db;
  text-decoration: none;
  transition: color 0.3s ease;
}

.tech-info a:hover {
  color: #2980b9;
  text-decoration: underline;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .footer-content {
    padding: 30px 15px 15px;
  }

  .footer-info {
    grid-template-columns: 1fr;
    gap: 30px;
    margin-bottom: 30px;
    padding-bottom: 20px;
  }

  .footer-section h4 {
    font-size: 1.1rem;
  }

  .footer-stats {
    flex-wrap: wrap;
    gap: 10px;
  }

  .footer-stats span {
    font-size: 0.8rem;
    padding: 3px 6px;
  }
}

@media (max-width: 480px) {
  .footer-content {
    padding: 20px 10px 10px;
  }

  .footer-info {
    gap: 20px;
  }

  .footer-section {
    text-align: center;
  }

  .footer-section h4::after {
    left: 50%;
    transform: translateX(-50%);
  }

  .footer-stats {
    justify-content: center;
  }

  .footer-contact p {
    justify-content: center;
  }

  .copyright-content {
    font-size: 0.85rem;
  }
}

/* 深色主题适配 */
[data-theme='dark'] .blog-footer {
  background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 100%);
  box-shadow: 0 -2px 10px rgba(255, 255, 255, 0.1);
}

[data-theme='dark'] .footer-section h4 {
  color: #64b5f6;
}

[data-theme='dark'] .footer-section h4::after {
  background: #64b5f6;
}

[data-theme='dark'] .footer-section p {
  color: #9e9e9e;
}

[data-theme='dark'] .footer-stats span {
  background: rgba(100, 181, 246, 0.2);
  color: #64b5f6;
}

[data-theme='dark'] .footer-links a {
  color: #9e9e9e;
}

[data-theme='dark'] .footer-links a:hover {
  color: #64b5f6;
}

[data-theme='dark'] .footer-contact i {
  color: #64b5f6;
}

[data-theme='dark'] .footer-copyright {
  border-top-color: rgba(255, 255, 255, 0.1);
}

[data-theme='dark'] .copyright-content p {
  color: #757575;
}

[data-theme='dark'] .tech-info a {
  color: #64b5f6;
}

[data-theme='dark'] .tech-info a:hover {
  color: #42a5f5;
}
</style>
