<template>
  <footer v-if="shouldShowFooter" class="blog-footer">
    <div class="footer-content">
      <div v-if="blogSettingsStore.isFeatureEnabled('footer_enabled')" class="footer-info">
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

<script setup lang="ts">
import { computed } from 'vue'
import { useBlogSettingsStore } from '@/stores/blogSettings'

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

const blogSettingsStore = useBlogSettingsStore()

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
  return blogSettingsStore.isFeatureEnabled('footer_enabled')
})

// 是否显示版权
const shouldShowCopyright = computed(() => {
  return blogSettingsStore.isFeatureEnabled('copyright_enabled')
})
</script>

<style scoped>
.blog-footer {
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 50%, #cbd5e1 100%);
  color: #334155;
  margin-top: 60px;
  box-shadow: 0 -4px 20px rgba(0, 0, 0, 0.08);
  border-top: 3px solid rgba(99, 102, 241, 0.15);
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
  border-bottom: 2px solid rgba(99, 102, 241, 0.15);
}

.footer-section h4 {
  margin: 0 0 20px 0;
  color: #4f46e5;
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
  background: linear-gradient(90deg, #6366f1, #8b5cf6);
  border-radius: 2px;
}

.footer-section p {
  margin: 0 0 15px 0;
  line-height: 1.6;
  color: #64748b;
}

.footer-stats {
  display: flex;
  gap: 20px;
  margin-top: 15px;
}

.footer-stats span {
  background: linear-gradient(135deg, rgba(99, 102, 241, 0.08), rgba(139, 92, 246, 0.08));
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 0.9rem;
  color: #4f46e5;
  border: 1px solid rgba(99, 102, 241, 0.15);
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
  color: #64748b;
  text-decoration: none;
  transition: all 0.3s ease;
  display: inline-block;
}

.footer-links a:hover {
  color: #4f46e5;
  transform: translateX(5px);
  background: rgba(99, 102, 241, 0.06);
  padding: 2px 6px;
  border-radius: 4px;
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
  color: #4f46e5;
  font-size: 1.1rem;
}

.footer-copyright {
  text-align: center;
  padding-top: 20px;
  border-top: 2px solid rgba(99, 102, 241, 0.15);
  background: linear-gradient(to bottom, transparent, rgba(99, 102, 241, 0.03));
}

.copyright-content p {
  margin: 8px 0;
  font-size: 0.9rem;
  color: #475569;
  font-weight: 500;
}

.tech-info {
  font-size: 0.85rem !important;
}

.tech-info a {
  color: #4f46e5;
  text-decoration: none;
  transition: all 0.3s ease;
  background: rgba(99, 102, 241, 0.06);
  padding: 2px 6px;
  border-radius: 4px;
}

.tech-info a:hover {
  color: #7c3aed;
  background: rgba(124, 58, 237, 0.08);
  text-decoration: none;
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
html.dark .blog-footer {
  background: linear-gradient(135deg, #1e1e2e 0%, #2d2d3f 100%);
  color: #e2e8f0;
  box-shadow: 0 -4px 20px rgba(0, 0, 0, 0.3);
  border-top-color: rgba(139, 92, 246, 0.2);
}

html.dark .footer-section h4 {
  color: #a78bfa;
}

html.dark .footer-section h4::after {
  background: linear-gradient(90deg, #a78bfa, #c4b5fd);
}

html.dark .footer-section p {
  color: #a1a1aa;
}

html.dark .footer-stats span {
  background: linear-gradient(135deg, rgba(139, 92, 246, 0.15), rgba(167, 139, 250, 0.15));
  color: #a78bfa;
  border-color: rgba(139, 92, 246, 0.3);
}

html.dark .footer-links a {
  color: #a1a1aa;
}

html.dark .footer-links a:hover {
  color: #a78bfa;
  background: rgba(139, 92, 246, 0.1);
}

html.dark .footer-contact i {
  color: #a78bfa;
}

html.dark .footer-copyright {
  border-top-color: rgba(139, 92, 246, 0.2);
}

html.dark .copyright-content p {
  color: #71717a;
}

html.dark .tech-info a {
  color: #a78bfa;
  background: rgba(139, 92, 246, 0.1);
}

html.dark .tech-info a:hover {
  color: #c4b5fd;
  background: rgba(196, 181, 253, 0.15);
}
</style>
