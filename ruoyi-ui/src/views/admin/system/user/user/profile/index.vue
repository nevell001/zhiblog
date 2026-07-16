<template>
  <div class="profile-page">
    <div class="profile-cover"></div>

    <section class="profile-info">
      <div class="profile-avatar">
        <userAvatar />
      </div>
      <div class="profile-meta">
        <div class="name">
          {{ profileName }}
          <span class="verify">✓</span>
        </div>
        <div class="bio">
          {{ profileBio }}
        </div>
      </div>
      <div class="profile-stats">
        <div class="stat">
          <div class="num">42</div>
          <div class="lbl">文章</div>
        </div>
        <div class="stat">
          <div class="num">1.2k</div>
          <div class="lbl">粉丝</div>
        </div>
        <div class="stat">
          <div class="num">328</div>
          <div class="lbl">关注</div>
        </div>
        <div class="stat">
          <div class="num">3.4k</div>
          <div class="lbl">获赞</div>
        </div>
      </div>
      <el-button plain size="small" @click="selectedTab = 'settings'">编辑资料</el-button>
    </section>

    <div class="profile-tabs">
      <button
        type="button"
        class="tab"
        :class="{ active: selectedTab === 'articles' }"
        @click="selectedTab = 'articles'"
      >
        我的文章
      </button>
      <button
        type="button"
        class="tab"
        :class="{ active: selectedTab === 'notifications' }"
        @click="selectedTab = 'notifications'"
      >
        评论通知
        <span class="badge">5</span>
      </button>
      <button
        type="button"
        class="tab"
        :class="{ active: selectedTab === 'messages' }"
        @click="selectedTab = 'messages'"
      >
        互动消息
      </button>
      <button
        type="button"
        class="tab"
        :class="{ active: selectedTab === 'settings' }"
        @click="selectedTab = 'settings'"
      >
        账号设置
      </button>
    </div>

    <section v-if="selectedTab === 'articles'" class="profile-content">
      <div class="filter-bar">
        <div class="filter-tags">
          <span class="ftag active">全部 42</span>
          <span class="ftag">已发布 38</span>
          <span class="ftag">草稿 4</span>
        </div>
        <el-button type="primary" size="small">+ 写新文章</el-button>
      </div>

      <table class="article-table">
        <thead>
          <tr>
            <th style="width: 32px"><input type="checkbox" /></th>
            <th>标题</th>
            <th class="hide-mobile">分类</th>
            <th>状态</th>
            <th class="hide-mobile">发布时间</th>
            <th class="hide-mobile">数据</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="article in profileArticles" :key="article.title">
            <td><input type="checkbox" /></td>
            <td>
              <div class="t-title">{{ article.title }}</div>
            </td>
            <td class="hide-mobile">
              <span :class="['tag', article.categoryClass]">{{ article.category }}</span>
            </td>
            <td>
              <span :class="['tag', article.statusClass]">{{ article.status }}</span>
            </td>
            <td class="hide-mobile">{{ article.date }}</td>
            <td class="hide-mobile">
              <span class="metric">{{ article.metric }}</span>
            </td>
            <td>
              <div class="actions">
                <span class="act primary">编辑</span>
                <span class="act">查看</span>
                <span class="act danger">删除</span>
              </div>
            </td>
          </tr>
        </tbody>
      </table>

      <div class="pagination">
        <span class="pg">‹</span>
        <span class="pg active">1</span>
        <span class="pg">2</span>
        <span class="pg">3</span>
        <span class="pg">›</span>
      </div>
    </section>

    <section v-else-if="selectedTab === 'notifications'" class="profile-content">
      <div class="filter-bar">
        <div class="filter-tags">
          <span class="ftag active">全部</span>
          <span class="ftag">评论</span>
          <span class="ftag">点赞</span>
          <span class="ftag">关注</span>
        </div>
        <el-button plain size="small">全部已读</el-button>
      </div>
      <div class="notif-list">
        <div
          v-for="item in notifications"
          :key="item.text"
          class="notif-item"
          :class="{ unread: item.unread }"
        >
          <div class="n-icon" :class="item.type">{{ item.icon }}</div>
          <div class="n-body">
            <div class="n-text" v-html="item.text"></div>
            <div v-if="item.quote" class="n-text">
              <div class="quote">{{ item.quote }}</div>
            </div>
            <div class="n-time">{{ item.time }}</div>
            <div class="n-actions">
              <button v-for="action in item.actions" :key="action" type="button">
                {{ action }}
              </button>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section v-else-if="selectedTab === 'messages'" class="profile-content">
      <div class="notif-list">
        <div class="notif-item">
          <div class="n-icon follow">✚</div>
          <div class="n-body">
            <div class="n-text">
              <strong>架构师老王</strong>
              关注了你
            </div>
            <div class="n-time">3 天前</div>
            <div class="n-actions"><button type="button">回关</button></div>
          </div>
        </div>
      </div>
    </section>

    <section v-else class="profile-content settings-content">
      <div class="settings-card">
        <aside class="profile-details">
          <div class="detail-row">
            <svg-icon icon-class="user" />
            用户名称
            <span>{{ state.user.userName }}</span>
          </div>
          <div class="detail-row">
            <svg-icon icon-class="phone" />
            手机号码
            <span>{{ state.user.phonenumber }}</span>
          </div>
          <div class="detail-row">
            <svg-icon icon-class="email" />
            用户邮箱
            <span>{{ state.user.email }}</span>
          </div>
          <div class="detail-row" v-if="state.user.dept">
            <svg-icon icon-class="tree" />
            所属部门
            <span>{{ state.user.dept.deptName }} / {{ state.postGroup }}</span>
          </div>
          <div class="detail-row">
            <svg-icon icon-class="peoples" />
            所属角色
            <span>{{ state.roleGroup }}</span>
          </div>
          <div class="detail-row">
            <svg-icon icon-class="date" />
            创建日期
            <span>{{ state.user.createTime }}</span>
          </div>
        </aside>
        <el-tabs v-model="settingsTab" class="settings-tabs">
          <el-tab-pane label="基本资料" name="userinfo">
            <userInfo :user="state.user" />
          </el-tab-pane>
          <el-tab-pane label="修改密码" name="resetPwd">
            <resetPwd />
          </el-tab-pane>
        </el-tabs>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts" name="Profile">
import userAvatar from './userAvatar.vue'
import userInfo from './userInfo.vue'
import resetPwd from './resetPwd.vue'
import { getUserProfile } from '@/api/system/user'

const route = useRoute()
const selectedTab = ref('articles')
const settingsTab = ref('userinfo')
const state = reactive<Record<string, any>>({
  user: {},
  roleGroup: {},
  postGroup: {}
})
const profileName = computed(() => state.user.nickName || state.user.userName || '墨 Blog 用户')
const profileBio = computed(() => {
  const dept = state.user.dept?.deptName
  const role = state.roleGroup
  return [dept, role, state.user.email].filter(Boolean).join(' · ') || '记录思考，分享洞见'
})
const profileArticles = [
  {
    title: 'Vue 3.4 新特性详解：defineModel 与 Props 解构稳定性',
    category: '前端',
    categoryClass: 'tag-blue',
    status: '已发布',
    statusClass: 'tag-green',
    date: '2026-07-14',
    metric: '👁 1.2k · 💬 28'
  },
  {
    title: 'Spring Boot 3.2 虚拟线程实践：提升吞吐量的正确姿势',
    category: '后端',
    categoryClass: 'tag-green',
    status: '已发布',
    statusClass: 'tag-green',
    date: '2026-07-12',
    metric: '👁 856 · 💬 15'
  },
  {
    title: '从零搭建设计系统：Token、组件、文档三位一体',
    category: '设计',
    categoryClass: 'tag-pink',
    status: '已发布',
    statusClass: 'tag-green',
    date: '2026-07-10',
    metric: '👁 2.1k · 💬 42'
  },
  {
    title: 'TypeScript 类型体操进阶：条件类型的分布式特性',
    category: '前端',
    categoryClass: 'tag-blue',
    status: '草稿',
    statusClass: 'tag-amber',
    date: '—',
    metric: '—'
  }
]
const notifications = [
  {
    type: 'comment',
    icon: '💬',
    unread: true,
    text: '<strong>主公</strong> 评论了你的文章《Vue 3.4 新特性详解》',
    quote: 'defineModel 确实方便了很多，之前写组件库的时候那套 props + emit 的样板代码太痛苦了...',
    time: '2 小时前',
    actions: ['回复', '查看文章']
  },
  {
    type: 'like',
    icon: '❤',
    unread: true,
    text: '<strong>码农阿强</strong> 等 12 人赞了你的文章《从零搭建设计系统》',
    time: '5 小时前',
    actions: ['查看文章']
  },
  {
    type: 'follow',
    icon: '✚',
    unread: true,
    text: '<strong>前端小妹</strong> 关注了你',
    time: '8 小时前',
    actions: ['回关', '查看主页']
  }
]

function getUser() {
  getUserProfile().then(response => {
    state.user = response.data
    state.roleGroup = response.roleGroup
    state.postGroup = response.postGroup
  })
}

onMounted(() => {
  const activeTab = route.params && route.params.activeTab
  if (activeTab) {
    settingsTab.value = Array.isArray(activeTab) ? activeTab[0] : activeTab
    selectedTab.value = 'settings'
  }
  getUser()
})
</script>

<style scoped>
.profile-page {
  min-height: calc(100vh - 84px);
  color: #44403c;
  background: #fafaf9;
}

.profile-cover {
  height: 140px;
  background: linear-gradient(135deg, #818cf8, #4338ca);
}

.profile-info {
  position: relative;
  display: flex;
  align-items: flex-end;
  gap: 20px;
  max-width: 1200px;
  padding: 0 32px 20px;
  margin: -44px auto 0;
}

.profile-avatar {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 88px;
  height: 88px;
  overflow: hidden;
  background: #e0e7ff;
  border: 4px solid #fff;
  border-radius: 50%;
  box-shadow: 0 10px 18px rgba(15, 23, 42, 0.14);
}

.profile-avatar :deep(.user-info-head) {
  width: 88px;
  height: 88px;
}

.profile-meta {
  flex: 1;
  padding-bottom: 8px;
}

.name {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #1c1917;
  font-size: 22px;
  font-weight: 700;
}

.verify {
  color: #6366f1;
  font-size: 14px;
}

.bio {
  margin-top: 4px;
  color: #78716c;
  font-size: 13px;
}

.profile-stats {
  display: flex;
  gap: 28px;
  padding-bottom: 8px;
}

.stat {
  text-align: center;
  cursor: pointer;
}

.num {
  color: #292524;
  font-size: 20px;
  font-weight: 700;
}

.stat:hover .num {
  color: #4f46e5;
}

.lbl {
  margin-top: 2px;
  color: #a8a29e;
  font-size: 12px;
}

.profile-tabs {
  display: flex;
  max-width: 1200px;
  padding: 0 32px;
  margin: 0 auto;
  border-bottom: 1px solid #e7e5e4;
}

.profile-tabs .tab {
  padding: 14px 24px;
  color: #78716c;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  background: transparent;
  border: 0;
  border-bottom: 2px solid transparent;
  transition: all 0.15s;
}

.profile-tabs .tab:hover {
  color: #292524;
}

.profile-tabs .tab.active {
  color: #4f46e5;
  border-bottom-color: #4f46e5;
}

.badge {
  padding: 2px 7px;
  margin-left: 4px;
  color: #db2777;
  font-size: 11px;
  font-weight: 600;
  background: #fce7f3;
  border-radius: 999px;
}

.profile-content {
  max-width: 1200px;
  padding: 24px 32px;
  margin: 0 auto;
}

.filter-bar {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 20px;
}

.filter-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.ftag {
  padding: 5px 14px;
  color: #57534e;
  font-size: 13px;
  cursor: pointer;
  background: #f5f5f4;
  border-radius: 999px;
  transition: all 0.15s;
}

.ftag.active {
  color: #fff;
  font-weight: 500;
  background: #4f46e5;
}

.article-table {
  width: 100%;
  overflow: hidden;
  font-size: 13px;
  background: #fff;
  border: 1px solid #e7e5e4;
  border-collapse: collapse;
  border-radius: 12px;
}

.article-table th {
  padding: 12px 16px;
  color: #57534e;
  font-size: 12px;
  font-weight: 600;
  text-align: left;
  text-transform: uppercase;
  background: #fafaf9;
  border-bottom: 1px solid #e7e5e4;
}

.article-table td {
  padding: 14px 16px;
  color: #44403c;
  border-bottom: 1px solid #f5f5f4;
}

.article-table tr:last-child td {
  border-bottom: 0;
}

.article-table tr:hover {
  background: #fafaf9;
}

.t-title {
  color: #292524;
  font-weight: 500;
  cursor: pointer;
}

.t-title:hover {
  color: #4f46e5;
}

.tag {
  display: inline-flex;
  align-items: center;
  padding: 3px 8px;
  font-size: 12px;
  font-weight: 500;
  border-radius: 6px;
}

.tag-blue {
  color: #2563eb;
  background: #dbeafe;
}

.tag-green {
  color: #16a34a;
  background: #dcfce7;
}

.tag-pink {
  color: #db2777;
  background: #fce7f3;
}

.tag-amber {
  color: #d97706;
  background: #fef3c7;
}

.metric {
  color: #78716c;
  font-size: 12px;
}

.actions {
  display: flex;
  gap: 4px;
}

.act {
  padding: 4px 10px;
  color: #78716c;
  font-size: 12px;
  cursor: pointer;
  border-radius: 6px;
}

.act:hover {
  color: #44403c;
  background: #f5f5f4;
}

.act.primary:hover {
  color: #4f46e5;
  background: #eef2ff;
}

.act.danger:hover {
  color: #dc2626;
  background: #fee2e2;
}

.pagination {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  padding: 28px 0;
}

.pg {
  display: flex;
  align-items: center;
  justify-content: center;
  min-width: 34px;
  height: 34px;
  padding: 0 10px;
  color: #57534e;
  font-size: 13px;
  cursor: pointer;
  border: 1px solid #e7e5e4;
  border-radius: 8px;
}

.pg.active {
  color: #fff;
  background: #4f46e5;
  border-color: #4f46e5;
}

.notif-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.notif-item {
  display: flex;
  gap: 14px;
  padding: 16px 20px;
  cursor: pointer;
  background: #fff;
  border: 1px solid #e7e5e4;
  border-radius: 12px;
  transition: all 0.15s;
}

.notif-item:hover {
  border-color: #d6d3d1;
  box-shadow: 0 3px 10px rgba(15, 23, 42, 0.06);
}

.notif-item.unread {
  border-left: 3px solid #6366f1;
}

.n-icon {
  display: flex;
  flex-shrink: 0;
  align-items: center;
  justify-content: center;
  width: 42px;
  height: 42px;
  font-size: 18px;
  border-radius: 50%;
}

.n-icon.comment {
  color: #4f46e5;
  background: #eef2ff;
}

.n-icon.like {
  color: #dc2626;
  background: #fee2e2;
}

.n-icon.follow {
  color: #16a34a;
  background: #dcfce7;
}

.n-body {
  flex: 1;
}

.n-text {
  color: #44403c;
  font-size: 14px;
  line-height: 1.5;
}

.n-text :deep(strong) {
  color: #1c1917;
  font-weight: 600;
}

.quote {
  display: block;
  padding: 8px 12px;
  margin-top: 4px;
  color: #78716c;
  font-size: 13px;
  font-style: italic;
  background: #fafaf9;
  border-left: 2px solid #d6d3d1;
  border-radius: 8px;
}

.n-time {
  margin-top: 6px;
  color: #a8a29e;
  font-size: 12px;
}

.n-actions {
  display: flex;
  gap: 8px;
  margin-top: 8px;
}

.n-actions button {
  padding: 4px 12px;
  color: #57534e;
  font-size: 12px;
  background: #fff;
  border: 1px solid #d6d3d1;
  border-radius: 6px;
}

.settings-card {
  display: grid;
  grid-template-columns: 320px 1fr;
  gap: 24px;
  padding: 20px;
  background: #fff;
  border: 1px solid #e7e5e4;
  border-radius: 12px;
}

.profile-details {
  display: flex;
  flex-direction: column;
  gap: 0;
  border-right: 1px solid #e7e5e4;
}

.detail-row {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 16px 12px 0;
  color: #57534e;
  font-size: 13px;
  border-bottom: 1px solid #f5f5f4;
}

.detail-row span {
  margin-left: auto;
  color: #292524;
  text-align: right;
}

.settings-tabs {
  min-width: 0;
}

@media (max-width: 768px) {
  .profile-info {
    flex-wrap: wrap;
    padding: 0 16px 16px;
  }

  .profile-stats {
    width: 100%;
    gap: 16px;
    padding-top: 12px;
  }

  .profile-tabs {
    padding: 0 16px;
    overflow-x: auto;
  }

  .profile-tabs .tab {
    flex-shrink: 0;
    padding: 14px 16px;
  }

  .profile-content {
    padding: 16px;
  }

  .article-table {
    font-size: 12px;
  }

  .article-table th,
  .article-table td {
    padding: 8px;
  }

  .hide-mobile {
    display: none;
  }

  .settings-card {
    grid-template-columns: 1fr;
  }

  .profile-details {
    border-right: 0;
  }
}
</style>
