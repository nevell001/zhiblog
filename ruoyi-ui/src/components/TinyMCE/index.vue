<template>
  <div class="tinymce-container">
    <textarea :id="id" style="visibility: hidden"></textarea>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, watch, nextTick } from 'vue'
import { loadTinymce } from '@/utils/tinymce'

const props = defineProps({
  modelValue: {
    type: String,
    default: ''
  },
  height: {
    type: [Number, String],
    default: 400
  },
  minHeight: {
    type: [Number, String],
    default: 200
  }
})

const emit = defineEmits(['update:modelValue'])

const id = ref('tinymce-' + Date.now() + Math.random().toString(36).substr(2))
const editorRef = ref<any>(null)
const inited = ref(false)

// 设置 watch 监听器，返回的清理函数会自动处理
watch(
  () => props.modelValue,
  newValue => {
    if (inited.value && editorRef.value && newValue !== editorRef.value.getContent()) {
      editorRef.value.setContent(newValue || '')
    }
  }
)

watch(
  () => props.height,
  newHeight => {
    if (editorRef.value) {
      editorRef.value.getContainer().style.height = `${newHeight}px`
    }
  }
)

const initEditor = async () => {
  // 动态加载 tinymce
  const tinymce = await loadTinymce()

  // 只有当tinymce成功加载时才初始化编辑器
  if (tinymce && typeof tinymce.init === 'function') {
    tinymce.init({
      selector: `#${id.value}`,
      height: Number(props.height),
      min_height: Number(props.minHeight),
      language: 'en',
      license_key: 'gpl',
      menubar: 'file edit view insert format tools table help',
      plugins: 'lists link image',
      toolbar: 'undo redo | bold italic underline | bullist numlist | link image',
      toolbar_mode: 'sliding',
      content_style: `
        body {
          font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
          font-size: 14px;
          line-height: 1.6;
        }
        img { max-width: 100%; height: auto; }
        table { border-collapse: collapse; width: 100%; }
        table td, table th { border: 1px solid #ddd; padding: 8px; }
        table th { background-color: #f5f5f5; }
        blockquote { border-left: 4px solid #ddd; margin-left: 0; padding-left: 16px; color: #666; }
      `,
      branding: false,
      promotion: false,
      resize: true,
      autoresize_bottom_margin: 50,
      convert_urls: false,
      end_container_on_empty_block: true,
      powerpaste_word_import: 'clean',
      code_dialog_height: 450,
      code_dialog_width: 1000,
      advlist_bullet_styles: 'square',
      advlist_number_styles: 'default',
      default_link_target: '_blank',
      link_title: false,
      nonbreaking_force_tab: true,
      paste_data_images: true,
      images_upload_handler: (blobInfo, progress) =>
        new Promise((resolve, reject) => {
          // 图片上传处理
          const xhr = new XMLHttpRequest()
          xhr.withCredentials = false
          xhr.open('POST', '/dev-api/common/upload')

          if (xhr.upload) {
            xhr.upload.onprogress = e => {
              progress((e.loaded / e.total) * 100)
            }
          }

          xhr.onload = () => {
            if (xhr.status === 200) {
              const json = JSON.parse(xhr.responseText)
              if (json.code === 200) {
                resolve(json.fileName)
              } else {
                reject('上传失败: ' + json.msg)
              }
            } else {
              reject('上传失败')
            }
          }

          xhr.onerror = () => {
            reject('上传失败')
          }

          const formData = new FormData()
          formData.append('file', blobInfo.blob(), blobInfo.filename())
          xhr.send(formData)
        }),
      setup: editor => {
        // 确保editor存在且具有on方法
        if (editor && typeof editor.on === 'function') {
          editorRef.value = editor

          editor.on('init', () => {
            inited.value = true
            editor.setContent(props.modelValue)
          })

          editor.on('input change undo redo', () => {
            const content = editor.getContent()
            emit('update:modelValue', content)
          })

          editor.on('blur', () => {
            const content = editor.getContent()
            emit('update:modelValue', content)
          })
        } else {
          console.error('TinyMCE editor initialization failed: editor is not a valid object')
        }
      }
    })
  } else {
    console.error('TinyMCE failed to load or is not a valid object')
  }
}

const destroyEditor = () => {
  if (editorRef.value && !editorRef.value.destroyed) {
    editorRef.value.destroy()
    editorRef.value = null
    inited.value = false
  }
}

onMounted(() => {
  nextTick(async () => {
    await initEditor()
  })
})

onUnmounted(() => {
  destroyEditor()
})

defineExpose({
  getContent: () => (editorRef.value ? editorRef.value.getContent() : ''),
  setContent: content => {
    if (editorRef.value) {
      editorRef.value.setContent(content)
    }
  },
  focus: () => {
    if (editorRef.value) {
      editorRef.value.focus()
    }
  }
})
</script>

<style scoped>
.tinymce-container {
  position: relative;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
}

.tinymce-container:hover {
  border-color: #c0c4cc;
}

.tinymce-container:focus-within {
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}
</style>
