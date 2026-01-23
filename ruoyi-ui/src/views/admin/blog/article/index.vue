<template>
  <div class="app-container">
    <el-form
      v-show="showSearch"
      ref="queryRef"
      :model="queryParams"
      :inline="true"
      label-width="68px"
    >
      <el-form-item
        label="文章标题"
        prop="title"
      >
        <el-input
          v-model="queryParams.title"
          placeholder="请输入文章标题"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item
        label="分类"
        prop="categoryId"
      >
        <el-select
          v-model="queryParams.categoryId"
          placeholder="请选择分类"
          clearable
          class="category-select"
        >
          <el-option
            v-for="category in categoryOptions"
            :key="category.id"
            :label="category.name"
            :value="category.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        label="状态"
        prop="status"
      >
        <el-select
          v-model="queryParams.status"
          placeholder="请选择状态"
          clearable
          class="status-select"
        >
          <el-option
            label="草稿"
            value="0"
          />
          <el-option
            label="发布"
            value="1"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="">
        <el-button
          type="primary"
          icon="Search"
          @click="handleQuery"
        >
          搜索
        </el-button>
        <el-button
          icon="Refresh"
          @click="resetQuery"
        >
          重置
        </el-button>
      </el-form-item>
    </el-form>

    <el-row
      :gutter="10"
      class="mb8"
    >
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
        >
          新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
        >
          修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
        >
          删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['blog:article:edit']"
          type="warning"
          plain
          icon="Top"
          :disabled="multiple"
          @click="handleBatchTop"
        >
          批量置顶
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['blog:article:edit']"
          type="warning"
          plain
          icon="Star"
          :disabled="multiple"
          @click="handleBatchRecommend"
        >
          批量推荐
        </el-button>
      </el-col>
      <right-toolbar
        v-model:show-search="showSearch"
        @query-table="getList"
      />
    </el-row>

    <el-table
      v-loading="loading"
      :data="articleList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column
        type="selection"
        width="55"
        align="center"
      />
      <el-table-column
        label="文章ID"
        align="center"
        prop="id"
        width="60"
      />
      <el-table-column
        label="文章标题"
        align="left"
        prop="title"
        width="200"
        :show-overflow-tooltip="true"
      >
        <template #default="scope">
          <div class="article-title-wrapper">
            <el-tag
              v-if="scope.row.isTop === 1 || scope.row.isTop === '1'"
              type="danger"
              size="small"
              class="mr-1 article-top-tag"
            >
              置顶
            </el-tag>
            <el-tag
              v-if="scope.row.isRecommend === 1 || scope.row.isRecommend === '1'"
              type="warning"
              size="small"
              class="mr-1 article-recommend-tag"
            >
              推荐
            </el-tag>
            <span class="article-title-text">{{ scope.row.title }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        label="分类"
        align="center"
        prop="categoryName"
        width="100"
      >
        <template #default="scope">
          <el-tag
            v-if="scope.row.categoryName"
            type="info"
            size="small"
            class="category-tag"
          >
            {{ scope.row.categoryName }}
          </el-tag>
          <span
            v-else
            class="text-muted"
          >未分类</span>
        </template>
      </el-table-column>
      <el-table-column
        label="作者"
        align="center"
        prop="authorName"
        width="80"
      />

      <el-table-column
        label="标签"
        align="center"
        prop="tags"
        min-width="100"
      >
        <template #default="scope">
          <el-tag
            v-for="(tag, index) in formatTagList(scope.row.tags)"
            :key="index"
            size="small"
            class="mr-1"
          >
            {{ tag }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column
        label="状态"
        align="center"
        prop="status"
        width="80"
      >
        <template #default="scope">
          <el-tag
            :type="scope.row.status === '1' || scope.row.status === 1 ? 'success' : 'warning'"
            effect="dark"
            class="article-status-tag"
          >
            {{ scope.row.status === '1' || scope.row.status === 1 ? '已发布' : '草稿' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="浏览量"
        align="center"
        prop="viewCount"
        width="70"
      />
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        width="100"
      >
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        width="280"
        class-name="small-padding fixed-width"
      >
        <template #default="scope">
          <el-button
            v-hasPermi="['blog:article:edit']"
            link
            type="primary"
            icon="Edit"
            @click="handleUpdate(scope.row)"
          >
            修改
          </el-button>
          <el-button
            v-hasPermi="['blog:article:edit']"
            link
            type="danger"
            icon="Top"
            @click="handleTop(scope.row)"
          >
            {{ scope.row.isTop === 1 || scope.row.isTop === '1' ? '取消置顶' : '置顶' }}
          </el-button>
          <el-button
            v-hasPermi="['blog:article:edit']"
            link
            type="warning"
            icon="Star"
            @click="handleRecommend(scope.row)"
          >
            {{ scope.row.isRecommend === 1 || scope.row.isRecommend === '1' ? '取消推荐' : '推荐' }}
          </el-button>
          <el-button
            v-hasPermi="['blog:article:edit']"
            link
            type="info"
            icon="Refresh"
            @click="handleStatusChange(scope.row)"
          >
            {{ scope.row.status === 1 || scope.row.status === '1' ? '转为草稿' : '发布' }}
          </el-button>
          <el-button
            v-hasPermi="['blog:article:remove']"
            link
            type="danger"
            icon="Delete"
            @click="handleDelete(scope.row)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      :total="total"
      @pagination="getList"
    />

    <!-- 添加或修改博客文章对话框 -->
    <el-dialog
      v-model="open"
      :title="title"
      width="800px"
      append-to-body
      @close="cancel"
    >
      <el-form
        ref="articleRef"
        :model="form"
        :rules="rules"
        label-width="80px"
      >
        <el-row>
          <el-col :span="12">
            <el-form-item
              label="文章标题"
              prop="title"
            >
              <el-input
                v-model="form.title"
                placeholder="请输入文章标题"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="分类"
              prop="categoryId"
            >
              <el-select
                v-model="form.categoryId"
                placeholder="请选择分类"
              >
                <el-option
                  v-for="category in categoryOptions"
                  :key="category.id"
                  :label="category.name"
                  :value="category.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item
          label="摘要"
          prop="summary"
        >
          <el-input
            v-model="form.summary"
            type="textarea"
            placeholder="请输入摘要"
          />
        </el-form-item>
        <el-form-item
          label="文章内容"
          prop="content"
        >
          <editor
            :key="form.id || 'new'"
            v-model="form.content"
            :min-height="192"
          />
        </el-form-item>
        <el-form-item
          label="封面图片"
          prop="coverUrl"
        >
          <image-upload
            v-model="form.coverUrl"
            action="/common/upload/article-cover"
          />
          <div
            v-if="form.coverUrl"
            style="margin-top: 10px"
          >
            <img
              :src="getCoverUrl(form.coverUrl)"
              style="
                max-width: 200px;
                max-height: 150px;
                border: 1px solid #e0e0e0;
                border-radius: 4px;
              "
            />
            <div style="margin-top: 8px">
              <el-button
                type="danger"
                size="small"
                @click="form.coverUrl = ''"
              >
                删除封面
              </el-button>
            </div>
          </div>
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item
              label="是否置顶"
              prop="isTop"
            >
              <el-radio-group v-model="form.isTop">
                <el-radio :label="1">
                  是
                </el-radio>
                <el-radio :label="0">
                  否
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="是否推荐"
              prop="isRecommend"
            >
              <el-radio-group v-model="form.isRecommend">
                <el-radio :label="1">
                  是
                </el-radio>
                <el-radio :label="0">
                  否
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item
          label="状态"
          prop="status"
        >
          <el-radio-group v-model="form.status">
            <el-radio :label="1">
              发布
            </el-radio>
            <el-radio :label="0">
              草稿
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item
          label="标签分类"
          prop="tagIds"
        >
          <TagCategorySelector
            v-model:selected-tags="form.tagIds"
            v-model:selected-category="form.categoryId"
            :show-category="false"
            placeholder="选择或创建标签"
          />
        </el-form-item>
        <el-form-item
          v-if="form.id"
          label="作者"
          prop="authorName"
        >
          <el-input
            v-model="form.authorName"
            placeholder="请输入作者"
            readonly
          />
        </el-form-item>
        <el-form-item
          v-else
          label="作者"
          prop="authorName"
        >
          <el-input
            v-model="form.authorName"
            placeholder="自动填充为当前用户"
            readonly
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button
            type="primary"
            @click="submitForm"
          >
            确 定
          </el-button>
          <el-button @click="cancel">
            取 消
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Article">
import { ref, reactive, toRefs, getCurrentInstance, onMounted, nextTick } from 'vue'
import { useUserStore } from '@/stores/user'
import {
  listArticle,
  getArticle,
  delArticle,
  addArticle,
  updateArticle,
  updateArticleTopStatus,
  updateArticleRecommendStatus
} from '@/api/admin/blog/article'
import { listCategory } from '@/api/admin/blog/category'
import { listTag } from '@/api/admin/blog/tag'
import ImageUpload from '@/components/ImageUpload'
import TagCategorySelector from '@/components/TagCategorySelector.vue'
import { parseTime } from '@/utils/ruoyi'
import { ElMessage, ElMessageBox } from 'element-plus'

const { proxy } = getCurrentInstance()
const userStore = useUserStore()

const articleList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref('')
const categoryOptions = ref([])
const tagOptions = ref([])

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    title: null,
    categoryId: null,
    status: null
  },
  rules: {
    title: [
      { required: true, message: '文章标题不能为空', trigger: 'blur' },
      { min: 2, max: 200, message: '标题长度应为2-200个字符', trigger: 'blur' }
    ],
    content: [
      { required: true, message: '文章内容不能为空', trigger: 'blur' },
      { min: 10, message: '内容至少需要10个字符', trigger: 'blur' }
    ],
    summary: [{ max: 500, message: '摘要长度不能超过500个字符', trigger: 'blur' }],
    categoryId: [{ required: true, message: '请选择文章分类', trigger: 'change' }]
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询博客文章列表 */
async function getList() {
  loading.value = true
  try {
    const response = await listArticle(queryParams.value)
    articleList.value = response.rows || []
    total.value = response.total || 0

    // 如果搜索结果为空，显示提示信息
    if (articleList.value.length === 0) {
      ElMessage.info('未找到符合条件的文章')
    }
  } catch (error) {
    console.error('获取文章列表失败:', error)
    ElMessage.error('获取文章列表失败: ' + (error.message || '未知错误'))
    // 错误时也设置空数组，确保表格不会显示错误数据
    articleList.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

/** 查询分类列表 */
async function getCategoryList() {
  try {
    const response = await listCategory()
    categoryOptions.value = response.rows || []
  } catch (error) {
    console.error('获取分类列表失败:', error)
    ElMessage.error('获取分类列表失败: ' + (error.message || '未知错误'))
    categoryOptions.value = []
  }
}

/** 查询标签列表 */
async function getTagList() {
  try {
    const response = await listTag()
    tagOptions.value = response.rows || []
  } catch (error) {
    console.error('获取标签列表失败:', error)
    ElMessage.error('获取标签列表失败: ' + (error.message || '未知错误'))
    tagOptions.value = []
  }
}

// 取消按钮
function cancel() {
  reset()
  open.value = false
}

// 表单重置
function reset() {
  // 强制清空表单对象，使用 nextTick 确保响应式更新
  form.value = {
    id: null,
    title: '',
    summary: '',
    content: null, // 使用 null 而不是空字符串，确保编辑器组件能正确响应
    coverUrl: '',
    categoryId: null,
    authorId: userStore.userId || null,
    authorName: userStore.name || 'admin',
    status: 0, // 默认草稿状态
    isTop: 0,
    isRecommend: 0,
    tagIds: [],
    viewCount: 0,
    likeCount: 0,
    commentCount: 0,
    createTime: null,
    updateTime: null,
    delFlag: 0
  }

  // 清空表单验证状态
  if (articleRef.value) {
    articleRef.value.clearValidate()
  }

  // 使用 nextTick 确保 DOM 更新后再设置内容为空
  nextTick(() => {
    form.value.content = ''
  })
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  // 直接重置查询参数
  queryParams.value = {
    pageNum: 1,
    pageSize: 10,
    title: null,
    categoryId: null,
    status: null
  }
  getList()
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id)
  single.value = selection.length !== 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = '添加博客文章'

  // 使用 nextTick 确保 DOM 更新后再强制清空编辑器内容
  nextTick(() => {
    form.value.content = ''
  })
}

/** 修改按钮操作 */
async function handleUpdate(row) {
  reset()
  const id = row.id || ids.value
  try {
    loading.value = true
    const response = await getArticle(id)

    if (response.data) {
      // 深拷贝并确保数据类型正确
      const articleData = JSON.parse(JSON.stringify(response.data))

      // 确保数值字段是正确的类型
      if (articleData.categoryId !== null && articleData.categoryId !== undefined) {
        articleData.categoryId = Number(articleData.categoryId)
      }
      if (articleData.status !== null && articleData.status !== undefined) {
        articleData.status = Number(articleData.status)
      }
      if (articleData.isTop !== null && articleData.isTop !== undefined) {
        articleData.isTop = Number(articleData.isTop)
      }
      if (articleData.isRecommend !== null && articleData.isRecommend !== undefined) {
        articleData.isRecommend = Number(articleData.isRecommend)
      }

      // 确保tagIds是数组类型
      if (articleData.tagIds && !Array.isArray(articleData.tagIds)) {
        articleData.tagIds = [articleData.tagIds]
      } else if (!articleData.tagIds) {
        articleData.tagIds = []
      }

      // 确保字符串字段不为null
      articleData.title = articleData.title || ''
      articleData.summary = articleData.summary || ''
      articleData.content = articleData.content || ''
      articleData.coverUrl = articleData.coverUrl || ''
      articleData.authorName = articleData.authorName || ''

      form.value = articleData
    } else {
      form.value = {}
    }

    open.value = true
    title.value = '修改博客文章'
  } catch (error) {
    console.error('获取文章详情失败:', error)
    ElMessage.error('获取文章详情失败: ' + (error.message || '未知错误'))
  } finally {
    loading.value = false
  }
}

/** 格式化标签列表 */
function formatTagList(tags) {
  try {
    // 处理null或undefined情况
    if (!tags) return []

    // 如果是字符串，尝试解析JSON
    if (typeof tags === 'string') {
      try {
        const parsedTags = JSON.parse(tags)
        if (Array.isArray(parsedTags)) {
          return parsedTags.map(tag => {
            // 处理对象类型的标签
            if (typeof tag === 'object' && tag !== null) {
              return tag.name || tag.title || String(tag)
            }
            return String(tag)
          })
        }
        // 如果解析结果不是数组，返回字符串本身的数组
        return [tags]
      } catch (e) {
        // 如果JSON解析失败，检查是否为逗号分隔的字符串
        if (tags.includes(',')) {
          return tags.split(',').map(tag => tag.trim())
        }
        // 否则返回原字符串作为单个标签
        return [tags]
      }
    }

    // 如果已经是数组
    if (Array.isArray(tags)) {
      return tags.map(tag => {
        // 处理对象类型的标签
        if (typeof tag === 'object' && tag !== null) {
          return tag.name || tag.title || String(tag)
        }
        return String(tag)
      })
    }

    // 其他类型转为字符串数组
    return [String(tags)]
  } catch (error) {
    console.error('格式化标签列表失败:', error)
    return []
  }
}

// 提交按钮
const articleRef = ref()
const submitForm = async () => {
  if (!articleRef.value) return

  articleRef.value.validate(async valid => {
    if (valid) {
      loading.value = true
      try {
        // 创建一个完整的数据对象，包含所有必需的字段
        const apiData = { ...form.value }

        // 确保所有必需的字段都有值
        apiData.title = apiData.title?.trim() || ''
        apiData.summary = apiData.summary?.trim() || ''
        apiData.content = apiData.content || ''
        apiData.coverUrl = apiData.coverUrl || ''

        // 设置默认值，确保数据类型正确
        apiData.authorId = userStore.userId || 1 // 使用当前用户ID
        apiData.author = userStore.name || 'admin'
        apiData.isTop = apiData.isTop ? 1 : 0
        apiData.isRecommend = apiData.isRecommend ? 1 : 0
        apiData.status = apiData.status ? 1 : 0

        // 确保所有数值字段都是Number类型
        apiData.authorId = Number(apiData.authorId)
        apiData.isTop = Number(apiData.isTop)
        apiData.isRecommend = Number(apiData.isRecommend)
        apiData.status = Number(apiData.status)

        // 确保分类ID类型正确
        if (apiData.categoryId !== null && apiData.categoryId !== undefined) {
          apiData.categoryId = Number(apiData.categoryId)
        }

        // 处理标签数据，确保格式正确
        if (apiData.tagIds && Array.isArray(apiData.tagIds)) {
          // 保留标签ID数组格式
        } else if (typeof apiData.tagIds === 'string') {
          // 如果是字符串，尝试分割为数组
          apiData.tagIds = apiData.tagIds
            .split(',')
            .map(id => Number(id.trim()))
            .filter(id => !isNaN(id))
        } else {
          apiData.tagIds = []
        }

        // 移除可能引起JSON解析问题的字段
        if (apiData.id === null || apiData.id === '') {
          delete apiData.id // 新增时不需要id
        }
        delete apiData.createTime
        delete apiData.updateTime
        delete apiData.delFlag

        // 使用现有的API函数
        if (form.value.id !== null) {
          await updateArticle(apiData)
          ElMessage.success('修改成功')
        } else {
          await addArticle(apiData)
          ElMessage.success('新增成功')
        }

        // 重置表单，清空所有字段
        reset()
        open.value = false
        await getList()
      } catch (error) {
        console.error('操作失败:', error)
        // 显示友好的错误提示
        const errorMsg = error.response?.data?.msg || error.message || '操作失败，请重试'
        ElMessage.error(errorMsg)

        // 如果是401错误，处理登录过期
        if (error.response?.status === 401) {
          setTimeout(() => {
            window.location.href = '/index'
          }, 1500)
        }
      } finally {
        loading.value = false
      }
    }
  })
}

/** 删除按钮操作 */
async function handleDelete(row) {
  const articleIds = row.id || ids.value

  try {
    await ElMessageBox.confirm(
      '是否确认删除博客文章编号为"' + articleIds + '"的数据项？',
      '删除确认',
      {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    loading.value = true
    await delArticle(articleIds)
    ElMessage.success('删除成功')
    await getList()
  } catch (error) {
    if (error !== 'cancel' && error !== 'close') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败: ' + (error.message || '未知错误'))
    }
  } finally {
    loading.value = false
  }
}

/** 状态切换按钮操作 */
async function handleStatusChange(row) {
  try {
    const newStatus = row.status === '1' || row.status === 1 ? 0 : 1
    const statusText = newStatus === 1 ? '发布' : '草稿'

    // 安全处理标题显示，防止title为null或undefined
    const articleTitle = row.title || '(无标题文章)'

    await ElMessageBox.confirm(
      `是否确认将文章《${articleTitle}》的状态切换为${statusText}？`,
      '状态切换确认',
      {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'info'
      }
    )

    loading.value = true
    // 使用updateArticle接口进行状态更新，传递必要的字段以满足后端验证
    await updateArticle({
      id: row.id,
      status: Number(newStatus), // 确保状态值是数字类型
      title: row.title || '(无标题文章)',
      // 传递其他必要字段以防止后端清空这些字段
      categoryId: row.categoryId,
      content: row.content,
      summary: row.summary,
      coverUrl: row.coverUrl,
      authorId: row.authorId,
      authorName: row.authorName,
      isTop: row.isTop,
      isRecommend: row.isRecommend,
      tagIds: row.tagIds
    })
    ElMessage.success('状态切换成功')
    await getList()
  } catch (error) {
    if (error !== 'cancel' && error !== 'close') {
      console.error('状态切换失败:', error)
      // 显示更友好的错误提示，优先使用后端返回的错误信息
      const errorMsg = error.response?.data?.msg || error.message || '状态切换失败'
      ElMessage.error(errorMsg)
    }
  } finally {
    loading.value = false
  }
}

/** 置顶/取消置顶 */
async function handleTop(row) {
  try {
    const newIsTop = row.isTop === 1 || row.isTop === '1' ? 0 : 1
    const actionText = newIsTop === 1 ? '置顶' : '取消置顶'
    const articleTitle = row.title || '(无标题文章)'

    await ElMessageBox.confirm(`是否确认将文章《${articleTitle}》${actionText}？`, '置顶确认', {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'info'
    })

    loading.value = true
    await updateArticle({
      id: row.id,
      isTop: Number(newIsTop),
      title: row.title || '(无标题文章)',
      categoryId: row.categoryId,
      content: row.content,
      summary: row.summary,
      coverUrl: row.coverUrl,
      authorId: row.authorId,
      authorName: row.authorName,
      status: row.status,
      isRecommend: row.isRecommend,
      tagIds: row.tagIds
    })
    ElMessage.success(`${actionText}成功`)
    await getList()
  } catch (error) {
    if (error !== 'cancel' && error !== 'close') {
      console.error('置顶操作失败:', error)
      const errorMsg = error.response?.data?.msg || error.message || '置顶操作失败'
      ElMessage.error(errorMsg)
    }
  } finally {
    loading.value = false
  }
}

/** 推荐/取消推荐 */
async function handleRecommend(row) {
  try {
    const newIsRecommend = row.isRecommend === 1 || row.isRecommend === '1' ? 0 : 1
    const actionText = newIsRecommend === 1 ? '推荐' : '取消推荐'
    const articleTitle = row.title || '(无标题文章)'

    await ElMessageBox.confirm(`是否确认将文章《${articleTitle}》${actionText}？`, '推荐确认', {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'info'
    })

    loading.value = true
    await updateArticle({
      id: row.id,
      isRecommend: Number(newIsRecommend),
      title: row.title || '(无标题文章)',
      categoryId: row.categoryId,
      content: row.content,
      summary: row.summary,
      coverUrl: row.coverUrl,
      authorId: row.authorId,
      authorName: row.authorName,
      status: row.status,
      isTop: row.isTop,
      tagIds: row.tagIds
    })
    ElMessage.success(`${actionText}成功`)
    await getList()
  } catch (error) {
    if (error !== 'cancel' && error !== 'close') {
      console.error('推荐操作失败:', error)
      const errorMsg = error.response?.data?.msg || error.message || '推荐操作失败'
      ElMessage.error(errorMsg)
    }
  } finally {
    loading.value = false
  }
}

/** 批量置顶 */
async function handleBatchTop() {
  if (ids.value.length === 0) {
    ElMessage.warning('请选择要置顶的文章')
    return
  }

  try {
    await ElMessageBox.confirm(
      `是否确认将选中的 ${ids.value.length} 篇文章置顶？`,
      '批量置顶确认',
      {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'info'
      }
    )

    loading.value = true
    // 使用批量更新接口
    await updateArticleTopStatus({
      ids: ids.value,
      isTop: 1
    })
    ElMessage.success('批量置顶成功')
    await getList()
    // 清空选择
    ids.value = []
  } catch (error) {
    if (error !== 'cancel' && error !== 'close') {
      console.error('批量置顶失败:', error)
      const errorMsg = error.response?.data?.msg || error.message || '批量置顶失败'
      ElMessage.error(errorMsg)
    }
  } finally {
    loading.value = false
  }
}

/** 批量推荐 */
async function handleBatchRecommend() {
  if (ids.value.length === 0) {
    ElMessage.warning('请选择要推荐的文章')
    return
  }

  try {
    await ElMessageBox.confirm(
      `是否确认将选中的 ${ids.value.length} 篇文章推荐？`,
      '批量推荐确认',
      {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'info'
      }
    )

    loading.value = true
    // 使用批量更新接口
    await updateArticleRecommendStatus({
      ids: ids.value,
      isRecommend: 1
    })
    ElMessage.success('批量推荐成功')
    await getList()
    // 清空选择
    ids.value = []
  } catch (error) {
    if (error !== 'cancel' && error !== 'close') {
      console.error('批量推荐失败:', error)
      const errorMsg = error.response?.data?.msg || error.message || '批量推荐失败'
      ElMessage.error(errorMsg)
    }
  } finally {
    loading.value = false
  }
}

/** 获取封面图片URL */
function getCoverUrl(coverUrl) {
  if (!coverUrl) return ''
  // 检查是否为完整URL
  if (coverUrl.startsWith('http://') || coverUrl.startsWith('https://')) {
    return coverUrl
  }
  // 如果是相对路径，添加基础URL
  const baseUrl = import.meta.env.VITE_APP_BASE_API || ''
  return baseUrl + coverUrl
}

// 页面加载时初始化数据
onMounted(async () => {
  // 并行加载所有初始化数据
  try {
    await Promise.all([getList(), getCategoryList(), getTagList()])
  } catch (error) {
    console.error('初始化数据加载失败:', error)
    // 单个请求失败不会阻止其他请求继续执行，这里只是记录错误
  }
})
</script>

<style scoped>
/* 文章状态标签样式优化 */
.article-status-tag {
  font-size: 13px;
  padding: 4px 10px;
  border-radius: 4px;
  font-weight: 500;
}

/* 分类标签样式优化 */
.category-tag {
  font-size: 12px;
  padding: 3px 8px;
  border-radius: 3px;
  font-weight: 500;
}

/* 置顶标签样式优化 */
.article-top-tag {
  font-size: 11px;
  padding: 2px 6px;
  border-radius: 3px;
  font-weight: 600;
}

/* 推荐标签样式优化 */
.article-recommend-tag {
  font-size: 11px;
  padding: 2px 6px;
  border-radius: 3px;
  font-weight: 600;
}

/* 未分类文本样式 */
.text-muted {
  color: var(--el-text-color-placeholder, #999);
  font-size: 12px;
}

/* 标签样式优化 */
.mr-1 {
  margin-right: 4px;
}

.ml-1 {
  margin-left: 4px;
}

.ml-2 {
  margin-left: 8px;
}

/* 文章标题包装器样式 */
.article-title-wrapper {
  display: flex;
  align-items: center;
  gap: 6px;
  width: 100%;
  overflow: hidden;
}

.article-title-text {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  font-size: 14px;
  line-height: 1.4;
}

/* 响应式优化 */
@media (max-width: 1400px) {
  .article-title-text {
    font-size: 13px;
  }
}

@media (max-width: 1200px) {
  .article-title-text {
    font-size: 12px;
  }
}

@media (max-width: 992px) {
  .article-title-wrapper {
    flex-direction: column;
    align-items: flex-start;
    gap: 4px;
  }

  .article-title-text {
    font-size: 12px;
  }
}

/* 分类下拉选择框样式优化 */
.category-select {
  width: 150px;
}

:deep(.category-select .el-input__wrapper) {
  height: 32px;
  font-size: 13px;
}

:deep(.category-select .el-select__placeholder) {
  font-size: 13px;
}

:deep(.category-select .el-input__inner) {
  font-size: 13px;
}

/* 状态下拉选择框样式优化 */
.status-select {
  width: 120px;
}

:deep(.status-select .el-input__wrapper) {
  height: 32px;
  font-size: 13px;
}

:deep(.status-select .el-select__placeholder) {
  font-size: 13px;
}

:deep(.status-select .el-input__inner) {
  font-size: 13px;
}

/* 下拉选项样式优化 */
:deep(.el-select-dropdown__item) {
  font-size: 13px;
  padding: 8px 12px;
  line-height: 1.4;
}

/* 响应式样式 */
@media (max-width: 768px) {
  .category-select {
    width: 120px;
  }

  .status-select {
    width: 100px;
  }

  :deep(.category-select .el-input__wrapper),
  :deep(.status-select .el-input__wrapper) {
    height: 30px;
    font-size: 12px;
  }

  :deep(.category-select .el-select__placeholder),
  :deep(.status-select .el-select__placeholder) {
    font-size: 12px;
  }

  :deep(.category-select .el-input__inner),
  :deep(.status-select .el-input__inner) {
    font-size: 12px;
  }

  :deep(.el-select-dropdown__item) {
    font-size: 12px;
    padding: 6px 10px;
  }

  :deep(.el-tag) {
    font-size: 12px;
    padding: 3px 8px;
  }

  .article-status-tag {
    font-size: 12px;
    padding: 3px 8px;
  }

  .category-tag {
    font-size: 11px;
    padding: 2px 6px;
  }

  .article-top-tag,
  .article-recommend-tag {
    font-size: 10px;
    padding: 1px 4px;
  }

  .text-muted {
    font-size: 11px;
  }

  .ml-1,
  .ml-2 {
    margin-left: 2px;
  }
}

@media (max-width: 480px) {
  .category-select {
    width: 100px;
  }

  .status-select {
    width: 90px;
  }

  :deep(.category-select .el-input__wrapper),
  :deep(.status-select .el-input__wrapper) {
    height: 28px;
    font-size: 11px;
  }

  :deep(.category-select .el-select__placeholder),
  :deep(.status-select .el-select__placeholder) {
    font-size: 11px;
  }

  :deep(.category-select .el-input__inner),
  :deep(.status-select .el-input__inner) {
    font-size: 11px;
  }

  :deep(.el-select-dropdown__item) {
    font-size: 11px;
    padding: 5px 8px;
  }

  :deep(.el-tag) {
    font-size: 11px;
    padding: 2px 6px;
  }

  .article-status-tag {
    font-size: 11px;
    padding: 2px 6px;
  }

  .category-tag {
    font-size: 10px;
    padding: 2px 5px;
  }

  .article-top-tag,
  .article-recommend-tag {
    font-size: 9px;
    padding: 1px 3px;
  }

  .text-muted {
    font-size: 10px;
  }

  .ml-1,
  .ml-2 {
    margin-left: 1px;
  }
}
</style>
