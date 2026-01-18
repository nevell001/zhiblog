<template>
  <div class="component-upload-image">
    <el-upload
      ref="imageUpload"
      multiple
      :disabled="disabled"
      :action="uploadImgUrl"
      list-type="picture-card"
      :on-success="handleUploadSuccess"
      :before-upload="handleBeforeUpload"
      :data="data"
      :limit="limit"
      :on-error="handleUploadError"
      :on-exceed="handleExceed"
      :before-remove="handleDelete"
      :show-file-list="true"
      :headers="headers"
      :file-list="fileList"
      :on-preview="handlePictureCardPreview"
      :class="{ hide: fileList.length >= limit }"
    >
      <el-icon class="avatar-uploader-icon">
        <plus />
      </el-icon>
    </el-upload>
    <!-- 上传提示 -->
    <div v-if="showTip && !disabled" class="el-upload__tip">
      请上传
      <template v-if="fileSize">
        大小不超过
        <b style="color: #f56c6c">{{ fileSize }}MB</b>
      </template>
      <template v-if="fileType">
        格式为
        <b style="color: #f56c6c">{{ fileType.join('/') }}</b>
      </template>
      的文件
    </div>

    <el-dialog v-model="dialogVisible" title="预览" width="800px" append-to-body>
      <img :src="dialogImageUrl" style="display: block; max-width: 100%; margin: 0 auto" />
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch, getCurrentInstance, onMounted, onUnmounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { getToken } from '@/utils/auth'
import { isExternal } from '@/utils/validate'
import Sortable from 'sortablejs'

const props = defineProps({
  modelValue: [String, Object, Array],
  // 上传接口地址
  action: {
    type: String,
    default: '/common/upload'
  },
  // 上传携带的参数
  data: {
    type: Object
  },
  // 图片数量限制
  limit: {
    type: Number,
    default: 5
  },
  // 大小限制(MB)
  fileSize: {
    type: Number,
    default: 5
  },
  // 文件类型, 例如['png', 'jpg', 'jpeg']
  fileType: {
    type: Array,
    default: () => ['png', 'jpg', 'jpeg']
  },
  // 是否显示提示
  isShowTip: {
    type: Boolean,
    default: true
  },
  // 禁用组件（仅查看图片）
  disabled: {
    type: Boolean,
    default: false
  },
  // 拖动排序
  drag: {
    type: Boolean,
    default: true
  }
})

const { proxy } = getCurrentInstance()
const emit = defineEmits(['update:modelValue'])
const number = ref(0)
const uploadList = ref([])
const dialogImageUrl = ref('')
const dialogVisible = ref(false)
const baseUrl = import.meta.env.VITE_APP_BASE_API
const uploadImgUrl = ref(import.meta.env.VITE_APP_BASE_API + props.action) // 上传的图片服务器地址
const headers = ref({ Authorization: 'Bearer ' + getToken() })
const fileList = ref([])
const showTip = computed(() => props.isShowTip && (props.fileType || props.fileSize))

// 设置 watch 监听器，Vue 3 会自动清理
watch(
  () => props.modelValue,
  val => {
    if (val) {
      // 首先将值转为数组
      const list = Array.isArray(val) ? val : props.modelValue.split(',')
      // 然后将数组转为对象数组
      fileList.value = list.map(item => {
        if (typeof item === 'string') {
          if (item.indexOf(baseUrl) === -1 && !isExternal(item)) {
            item = { name: baseUrl + item, url: baseUrl + item }
          } else {
            item = { name: item, url: item }
          }
        }
        return item
      })
    } else {
      fileList.value = []
      return []
    }
  },
  { deep: true, immediate: true }
)

// 上传前loading加载
function handleBeforeUpload(file) {
  let isImg = false
  if (props.fileType.length) {
    let fileExtension = ''
    if (file.name.lastIndexOf('.') > -1) {
      fileExtension = file.name.slice(file.name.lastIndexOf('.') + 1)
    }
    isImg = props.fileType.some(type => {
      if (file.type.indexOf(type) > -1) return true
      if (fileExtension && fileExtension.indexOf(type) > -1) return true
      return false
    })
  } else {
    isImg = file.type.indexOf('image') > -1
  }
  if (!isImg) {
    ;(proxy as any).$modal.msgError(
      `文件格式不正确，请上传${props.fileType.join('/')}图片格式文件!`
    )
    return false
  }
  if (file.name.includes(',')) {
    ;(proxy as any).$modal.msgError('文件名不正确，不能包含英文逗号!')
    return false
  }
  if (props.fileSize) {
    const isLt = file.size / 1024 / 1024 < props.fileSize
    if (!isLt) {
      ;(proxy as any).$modal.msgError(`上传头像图片大小不能超过 ${props.fileSize} MB!`)
      return false
    }
  }
  ;(proxy as any).$modal.loading('正在上传图片，请稍候...')
  number.value++
}

// 文件个数超出
function handleExceed() {
  ;(proxy as any).$modal.msgError(`上传文件数量不能超过 ${props.limit} 个!`)
}

// 上传成功回调
function handleUploadSuccess(res: any, file: any) {
  if (res.code === 200) {
    // 保存完整的URL用于显示（包含baseUrl），文件名使用原始路径
    const fullUrl = res.url || res.fileName
    // 如果URL不包含baseUrl，添加baseUrl
    const displayUrl = fullUrl.indexOf(baseUrl) === 0 ? fullUrl : baseUrl + fullUrl
    uploadList.value.push({
      name: res.fileName || res.url,
      url: displayUrl
    })
    uploadedSuccessfully()
  } else {
    number.value--
    ;(proxy as any).$modal.closeLoading()
    ;(proxy as any).$modal.msgError(res.msg)
    ;(proxy.$refs.imageUpload as any).handleRemove(file)
    uploadedSuccessfully()
  }
}

// 删除图片
function handleDelete(file) {
  const findex = fileList.value.map(f => f.name).indexOf(file.name)
  if (findex > -1 && uploadList.value.length === number.value) {
    fileList.value.splice(findex, 1)
    emit('update:modelValue', listToString(fileList.value, ','))
    return false
  }
}

// 上传结束处理
function uploadedSuccessfully() {
  if (number.value > 0 && uploadList.value.length === number.value) {
    fileList.value = fileList.value.filter(f => f.url !== undefined).concat(uploadList.value)
    uploadList.value = []
    number.value = 0
    emit('update:modelValue', listToString(fileList.value, ','))
    ;(proxy as any).$modal.closeLoading()
  }
}

// 上传失败
function handleUploadError() {
  ;(proxy as any).$modal.msgError('上传图片失败')
  ;(proxy as any).$modal.closeLoading()
}

// 预览
function handlePictureCardPreview(file) {
  dialogImageUrl.value = file.url
  dialogVisible.value = true
}

// 对象转成指定字符串分隔
function listToString(list, separator) {
  let strs = ''
  separator = separator || ','
  for (const i in list) {
    if (undefined !== list[i].url && list[i].url.indexOf('blob:') !== 0) {
      let url = list[i].url
      // 如果URL包含baseUrl，移除它，保存相对路径到数据库
      if (url.indexOf(baseUrl) === 0) {
        url = url.substring(baseUrl.length)
      }
      strs += url + separator
    }
  }
  return strs !== '' ? strs.substr(0, strs.length - 1) : ''
}

// 初始化拖拽排序
onMounted(() => {
  if (props.drag && !props.disabled) {
    nextTick(() => {
      const element = proxy.$refs.imageUpload?.$el?.querySelector('.el-upload-list')
      Sortable.create(element, {
        onEnd: evt => {
          const movedItem = fileList.value.splice(evt.oldIndex, 1)[0]
          fileList.value.splice(evt.newIndex, 0, movedItem)
          emit('update:modelValue', listToString(fileList.value, ','))
        }
      })
    })
  }
})
</script>

<style scoped lang="scss">
// .el-upload--picture-card 控制加号部分
:deep(.hide .el-upload--picture-card) {
  display: none;
}

:deep(.el-upload.el-upload--picture-card.is-disabled) {
  display: none !important;
}
</style>
