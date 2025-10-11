<template>
  <div>
    <el-upload
      :action="uploadUrl"
      :before-upload="handleBeforeUpload"
      :on-success="handleUploadSuccess"
      :on-error="handleUploadError"
      name="file"
      :show-file-list="false"
      :headers="headers"
      class="editor-img-uploader"
      v-if="type == 'url'"
    >
      <i ref="uploadRef" class="editor-img-uploader"></i>
    </el-upload>
  </div>
  <div class="editor">
    <quill-editor
      ref="quillEditorRef"
      v-model:content="content"
      contentType="html"
      @textChange="(e) => $emit('update:modelValue', content)"
      :options="options"
      :style="styles"
    />
  </div>
</template>

<script setup>
import axios from 'axios'
import { QuillEditor } from "@vueup/vue-quill"
import "@vueup/vue-quill/dist/vue-quill.snow.css"
import { getToken } from "@/utils/auth"

const { proxy } = getCurrentInstance()

const quillEditorRef = ref()
const uploadUrl = ref(import.meta.env.VITE_APP_BASE_API + "/common/upload") // 上传的图片服务器地址
const headers = ref({
  Authorization: "Bearer " + getToken()
})

const props = defineProps({
  /* 编辑器的内容 */
  modelValue: {
    type: String,
  },
  /* 高度 */
  height: {
    type: Number,
    default: null,
  },
  /* 最小高度 */
  minHeight: {
    type: Number,
    default: null,
  },
  /* 只读 */
  readOnly: {
    type: Boolean,
    default: false,
  },
  /* 上传文件大小限制(MB) */
  fileSize: {
    type: Number,
    default: 5,
  },
  /* 类型（base64格式、url格式） */
  type: {
    type: String,
    default: "url",
  }
})

const options = ref({
  theme: "snow",
  bounds: document.body,
  debug: "warn",
  modules: {
    // 工具栏配置
    toolbar: {
      container: [
        [{ 'font': [] }, { 'size': ['small', false, 'large', 'huge'] }],  // 字体和大小
        [{ 'header': [1, 2, 3, 4, 5, 6, false] }],                       // 标题
        ['bold', 'italic', 'underline', 'strike'],                       // 加粗 斜体 下划线 删除线
        [{ 'color': [] }, { 'background': [] }],                         // 字体颜色、背景色
        [{ 'script': 'sub'}, { 'script': 'super' }],                      // 上标/下标
        [{ 'list': 'ordered'}, { 'list': 'bullet' }],                     // 有序、无序列表
        [{ 'indent': '-1'}, { 'indent': '+1' }],                         // 缩进
        [{ 'direction': 'rtl' }],                                         // 文本方向
        [{ 'align': [] }],                                                // 对齐方式
        ['blockquote', 'code-block'],                                    // 引用、代码块
        ['link', 'image', 'video'],                                       // 链接、图片、视频
        [{ 'table': true }],                                              // 表格
        ['clean']                                                        // 清除格式
      ],
      handlers: {
        'image': function() {
          // 图片上传处理
          proxy.$refs.uploadRef.click()
        },
        'video': function() {
          // 视频上传处理
          const url = prompt('请输入视频URL:')
          if (url) {
            const quill = quillEditorRef.value.getQuill()
            const range = quill.getSelection()
            quill.insertEmbed(range.index, 'video', url)
          }
        }
      }
    },
    syntax: false,
    table: true    // 启用表格功能
  },
  placeholder: "请输入内容...",
  readOnly: props.readOnly
})

const styles = computed(() => {
  let style = {}
  if (props.minHeight) {
    style.minHeight = `${props.minHeight}px`
  }
  if (props.height) {
    style.height = `${props.height}px`
  }
  return style
})

const content = ref("")
watch(() => props.modelValue, (v) => {
  if (v !== content.value) {
    content.value = v == undefined ? "<p></p>" : v
  }
}, { immediate: true })

// 如果设置了上传地址则自定义图片上传事件
onMounted(() => {
  if (props.type == 'url') {
    let quill = quillEditorRef.value.getQuill()
    let toolbar = quill.getModule("toolbar")
    toolbar.addHandler("image", (value) => {
      if (value) {
        proxy.$refs.uploadRef.click()
      } else {
        quill.format("image", false)
      }
    })
    quill.root.addEventListener('paste', handlePasteCapture, true)
  }
})

// 上传前校检格式和大小
function handleBeforeUpload(file) {
  // 支持的文件类型
  const imageTypes = ["image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp", "image/svg+xml"]
  const videoTypes = ["video/mp4", "video/webm", "video/ogg"]
  
  const isImage = imageTypes.includes(file.type)
  const isVideo = videoTypes.includes(file.type)
  
  // 检验文件格式
  if (!isImage && !isVideo) {
    proxy.$modal.msgError(`文件格式不支持! 支持格式: JPG, PNG, GIF, WebP, SVG, MP4, WebM, OGG`)
    return false
  }
  
  // 校检文件大小
  if (props.fileSize) {
    const isLt = file.size / 1024 / 1024 < props.fileSize
    if (!isLt) {
      proxy.$modal.msgError(`上传文件大小不能超过 ${props.fileSize} MB!`)
      return false
    }
  }
  
  return true
}

// 上传成功处理
function handleUploadSuccess(res, file) {
  // 如果上传成功
  if (res.code == 200) {
    // 获取富文本实例
    let quill = toRaw(quillEditorRef.value).getQuill()
    // 获取光标位置
    let length = quill.selection.savedRange.index
    
    // 判断文件类型
    const fileType = file.type
    if (fileType.startsWith('image/')) {
      // 插入图片
      quill.insertEmbed(length, "image", import.meta.env.VITE_APP_BASE_API + res.fileName)
    } else if (fileType.startsWith('video/')) {
      // 插入视频
      quill.insertEmbed(length, "video", import.meta.env.VITE_APP_BASE_API + res.fileName)
    }
    
    // 调整光标到最后
    quill.setSelection(length + 1)
  } else {
    proxy.$modal.msgError("文件插入失败")
  }
}

// 上传失败处理
function handleUploadError() {
  proxy.$modal.msgError("图片插入失败")
}

// 复制粘贴图片处理
function handlePasteCapture(e) {
  const clipboard = e.clipboardData || window.clipboardData
  if (clipboard && clipboard.items) {
    for (let i = 0; i < clipboard.items.length; i++) {
      const item = clipboard.items[i]
      if (item.type.indexOf('image') !== -1) {
        e.preventDefault()
        const file = item.getAsFile()
        insertImage(file)
      }
    }
  }
}

function insertImage(file) {
  const formData = new FormData()
  formData.append("file", file)
  axios.post(uploadUrl.value, formData, { headers: { "Content-Type": "multipart/form-data", Authorization: headers.value.Authorization } }).then(res => {
    handleUploadSuccess(res.data)
  })
}
</script>

<style scoped>
.editor-img-uploader {
  display: none;
}
.editor {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
}
.editor:hover {
  border-color: #c0c4cc;
}
.editor:focus-within {
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}
.ql-toolbar {
  border-bottom: 1px solid #dcdfe6 !important;
  background-color: #f8f9fa !important;
}
.ql-container {
  border: none !important;
  font-size: 14px;
  line-height: 1.6;
}
.quill-img {
  display: none;
}
</style>

<style>
/* 全局样式 */
.ql-snow .ql-tooltip[data-mode="link"]::before {
  content: "请输入链接地址:";
}
.ql-snow .ql-tooltip.ql-editing a.ql-action::after {
  border-right: 0px;
  content: "保存";
  padding-right: 0px;
}
.ql-snow .ql-tooltip[data-mode="video"]::before {
  content: "请输入视频地址:";
}

/* 字体大小选择器 */
.ql-snow .ql-picker.ql-size .ql-picker-label::before,
.ql-snow .ql-picker.ql-size .ql-picker-item::before {
  content: "14px";
}
.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="small"]::before,
.ql-snow .ql-picker.ql-size .ql-picker-item[data-value="small"]::before {
  content: "10px";
}
.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="large"]::before,
.ql-snow .ql-picker.ql-size .ql-picker-item[data-value="large"]::before {
  content: "18px";
}
.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="huge"]::before,
.ql-snow .ql-picker.ql-size .ql-picker-item[data-value="huge"]::before {
  content: "32px";
}

/* 标题选择器 */
.ql-snow .ql-picker.ql-header .ql-picker-label::before,
.ql-snow .ql-picker.ql-header .ql-picker-item::before {
  content: "文本";
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="1"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="1"]::before {
  content: "标题1";
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="2"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="2"]::before {
  content: "标题2";
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="3"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="3"]::before {
  content: "标题3";
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="4"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="4"]::before {
  content: "标题4";
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="5"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="5"]::before {
  content: "标题5";
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="6"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="6"]::before {
  content: "标题6";
}

/* 字体选择器 */
.ql-snow .ql-picker.ql-font .ql-picker-label::before,
.ql-snow .ql-picker.ql-font .ql-picker-item::before {
  content: "标准字体";
}
.ql-snow .ql-picker.ql-font .ql-picker-label[data-value="serif"]::before,
.ql-snow .ql-picker.ql-font .ql-picker-item[data-value="serif"]::before {
  content: "衬线字体";
}
.ql-snow .ql-picker.ql-font .ql-picker-label[data-value="monospace"]::before,
.ql-snow .ql-picker.ql-font .ql-picker-item[data-value="monospace"]::before {
  content: "等宽字体";
}

/* 代码块样式 */
.ql-syntax {
  background-color: #f6f8fa !important;
  border-radius: 4px;
  padding: 12px !important;
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace !important;
  font-size: 13px;
  line-height: 1.45;
}

/* 表格样式 */
.ql-editor table {
  border-collapse: collapse;
  width: 100%;
  margin: 10px 0;
}
.ql-editor table td, .ql-editor table th {
  border: 1px solid #ddd;
  padding: 8px;
}
.ql-editor table th {
  background-color: #f5f5f5;
  font-weight: bold;
}

/* 引用样式 */
.ql-editor blockquote {
  border-left: 4px solid #ddd;
  margin: 16px 0;
  padding-left: 16px;
  color: #666;
  font-style: italic;
}

/* 图片样式 */
.ql-editor img {
  max-width: 100%;
  height: auto;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

/* 视频样式 */
.ql-editor iframe {
  max-width: 100%;
  border-radius: 4px;
}

/* 工具栏按钮悬停效果 */
.ql-snow .ql-toolbar .ql-formats button:hover {
  background-color: #e8f4ff !important;
  border-radius: 3px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .ql-toolbar .ql-formats {
    margin-bottom: 5px;
  }
  .ql-toolbar .ql-formats button {
    margin: 2px;
  }
}
</style>
