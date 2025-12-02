<template>
  <div class="tag-category-selector">
    <!-- 分类选择 -->
    <el-form-item label="分类" prop="categoryId" v-if="showCategory">
      <el-select 
        v-model="selectedCategory" 
        placeholder="请选择分类" 
        clearable
        filterable
        @change="handleCategoryChange"
        style="width: 100%;"
      >
        <el-option
          v-for="category in categoryOptions"
          :key="category.id"
          :label="category.name"
          :value="category.id"
        >
          <div style="display: flex; justify-content: space-between; align-items: center;">
            <span>{{ category.name }}</span>
            <el-tag size="small" type="info" v-if="category.articleCount">
              {{ category.articleCount }}篇
            </el-tag>
          </div>
        </el-option>
      </el-select>
    </el-form-item>

    <!-- 标签选择 -->
    <el-form-item label="标签" prop="tagIds" v-if="showTags">
      <div class="tag-selector-container">
        <!-- 标签选择器 -->
        <el-select 
          v-model="selectedTags" 
          multiple 
          placeholder="请选择标签" 
          clearable
          filterable
          collapse-tags
          collapse-tags-tooltip
          @change="handleTagChange"
          style="width: 100%; margin-bottom: 10px;"
        >
          <el-option
            v-for="tag in tagOptions"
            :key="tag.id || tag.tagId"
            :label="tag.name || tag.tagName"
            :value="tag.id || tag.tagId"
          >
            <div style="display: flex; align-items: center; justify-content: space-between;">
              <div style="display: flex; align-items: center;">
                <el-tag 
                  :style="{ 
                    backgroundColor: tag.color || '#409EFF', 
                    color: 'white', 
                    border: 'none',
                    marginRight: '8px'
                  }" 
                  size="small"
                >
                  <i v-if="tag.icon" :class="tag.icon" style="margin-right: 4px;"></i>
                  {{ tag.name || tag.tagName }}
                </el-tag>
              </div>
              <el-tag size="small" type="info" v-if="tag.articleCount">
                {{ tag.articleCount }}篇
              </el-tag>
            </div>
          </el-option>
        </el-select>

        <!-- 已选标签展示 -->
        <div class="selected-tags" v-if="selectedTagsDisplay.length > 0">
          <div class="selected-tags-label">已选标签：</div>
          <div class="selected-tags-list">
            <el-tag
              v-for="tag in selectedTagsDisplay"
              :key="tag.id || tag.tagId"
              :style="{ 
                backgroundColor: tag.color || '#409EFF', 
                color: 'white', 
                border: 'none',
                margin: '2px 4px 2px 0'
              }"
              closable
              @close="removeTag(tag.id || tag.tagId)"
            >
              <i v-if="tag.icon" :class="tag.icon" style="margin-right: 4px;"></i>
              {{ tag.name || tag.tagName }}
            </el-tag>
          </div>
        </div>

        <!-- 快速添加标签 -->
        <div class="quick-add-tag" v-if="allowQuickAdd">
          <el-input
            v-model="newTagName"
            placeholder="输入新标签名称"
            size="small"
            style="width: 200px; margin-right: 8px;"
            @keyup.enter="addNewTag"
          />
          <el-button 
            type="primary" 
            size="small" 
            @click="addNewTag"
            :disabled="!newTagName.trim()"
          >
            添加标签
          </el-button>
        </div>
      </div>
    </el-form-item>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { listCategory } from "@/api/blog/category"
import { listTag, addTag } from "@/api/blog/tag"
import { ElMessage } from 'element-plus'

// Props
const props = defineProps({
  modelValue: {
    type: Object,
    default: () => ({
      categoryId: null,
      tagIds: []
    })
  },
  showCategory: {
    type: Boolean,
    default: true
  },
  showTags: {
    type: Boolean,
    default: true
  },
  allowQuickAdd: {
    type: Boolean,
    default: true
  }
})

// Emits
const emit = defineEmits(['update:modelValue', 'change'])

// 响应式数据
const categoryOptions = ref([])
const tagOptions = ref([])
const selectedCategory = ref(null)
const selectedTags = ref([])
const newTagName = ref('')

// 计算属性
const selectedTagsDisplay = computed(() => {
  return tagOptions.value.filter(tag => 
    selectedTags.value.includes(tag.id || tag.tagId)
  )
})

// 监听器
watch(() => props.modelValue, (newVal) => {
  if (newVal) {
    selectedCategory.value = newVal.categoryId
    selectedTags.value = newVal.tagIds || []
  }
}, { immediate: true, deep: true })

watch([selectedCategory, selectedTags], () => {
  const value = {
    categoryId: selectedCategory.value,
    tagIds: [...selectedTags.value]
  }
  emit('update:modelValue', value)
  emit('change', value)
}, { deep: true })

// 方法
const handleCategoryChange = (value) => {
  selectedCategory.value = value
}

const handleTagChange = (value) => {
  selectedTags.value = value
}

const removeTag = (tagId) => {
  const index = selectedTags.value.indexOf(tagId)
  if (index > -1) {
    selectedTags.value.splice(index, 1)
  }
}

const addNewTag = async () => {
  if (!newTagName.value.trim()) {
    ElMessage.warning('请输入标签名称')
    return
  }

  // 检查标签是否已存在
  const existingTag = tagOptions.value.find(tag => 
    (tag.name || tag.tagName) === newTagName.value.trim()
  )
  
  if (existingTag) {
    ElMessage.warning('标签已存在')
    // 如果标签存在但未选中，则选中它
    const tagId = existingTag.id || existingTag.tagId
    if (!selectedTags.value.includes(tagId)) {
      selectedTags.value.push(tagId)
    }
    newTagName.value = ''
    return
  }

  try {
    // 创建新标签
    const newTag = {
      name: newTagName.value.trim(),
      color: getRandomColor(),
      description: `自动创建的标签：${newTagName.value.trim()}`
    }

    const response = await addTag(newTag)
    
    if (response.code === 200) {
      ElMessage.success('标签创建成功')
      
      // 重新加载标签列表
      await getTagList()
      
      // 自动选中新创建的标签
      const createdTag = tagOptions.value.find(tag => 
        (tag.name || tag.tagName) === newTagName.value.trim()
      )
      
      if (createdTag) {
        const tagId = createdTag.id || createdTag.tagId
        if (!selectedTags.value.includes(tagId)) {
          selectedTags.value.push(tagId)
        }
      }
      
      newTagName.value = ''
    } else {
      ElMessage.error(response.msg || '创建标签失败')
    }
  } catch (error) {
    console.error('创建标签失败:', error)
    ElMessage.error('创建标签失败')
  }
}

const getRandomColor = () => {
  const colors = [
    '#FF6B6B', '#4ECDC4', '#45B7D1', '#96CEB4', 
    '#FECA57', '#FF9FF3', '#54A0FF', '#5F27CD',
    '#00D2D3', '#FF9F43', '#10AC84', '#EE5A24'
  ]
  return colors[Math.floor(Math.random() * colors.length)]
}

const getCategoryList = async () => {
  try {
    const response = await listCategory()
    categoryOptions.value = response.rows || response.data || []
  } catch (error) {
    console.error('获取分类列表失败:', error)
  }
}

const getTagList = async () => {
  try {
    const response = await listTag()
    tagOptions.value = response.rows || response.data || []
  } catch (error) {
    console.error('获取标签列表失败:', error)
  }
}

// 生命周期
onMounted(() => {
  if (props.showCategory) {
    getCategoryList()
  }
  if (props.showTags) {
    getTagList()
  }
})

// 暴露方法给父组件
defineExpose({
  refreshCategories: getCategoryList,
  refreshTags: getTagList,
  clearSelection: () => {
    selectedCategory.value = null
    selectedTags.value = []
  }
})
</script>

<style scoped>
.tag-category-selector {
  width: 100%;
}

.tag-selector-container {
  width: 100%;
}

.selected-tags {
  margin-top: 10px;
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 4px;
  border: 1px solid #e4e7ed;
}

.selected-tags-label {
  font-size: 12px;
  color: #606266;
  margin-bottom: 8px;
  font-weight: 500;
}

.selected-tags-list {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
}

.quick-add-tag {
  margin-top: 10px;
  padding: 10px;
  background-color: #fafbfc;
  border-radius: 4px;
  border: 1px dashed #d9d9d9;
  display: flex;
  align-items: center;
}

.quick-add-tag:hover {
  border-color: #409eff;
  background-color: #f0f9ff;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .quick-add-tag {
    flex-direction: column;
    align-items: stretch;
  }
  
  .quick-add-tag .el-input {
    width: 100% !important;
    margin-right: 0 !important;
    margin-bottom: 8px;
  }
}
</style>