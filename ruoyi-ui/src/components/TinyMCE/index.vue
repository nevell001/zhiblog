<template>
  <div class="tinymce-container">
    <textarea :id="id" style="visibility: hidden"></textarea>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch, nextTick } from 'vue'
import tinymce from 'tinymce/tinymce'
import 'tinymce/themes/silver'
import 'tinymce/icons/default'
import 'tinymce/models/dom'

// 导入核心和基本插件
import 'tinymce/tinymce'
import 'tinymce/themes/silver'
import 'tinymce/icons/default'
import 'tinymce/models/dom'

// 导入基本插件
import 'tinymce/plugins/advlist'
import 'tinymce/plugins/autolink'
import 'tinymce/plugins/lists'
import 'tinymce/plugins/link'
import 'tinymce/plugins/image'
import 'tinymce/plugins/charmap'
import 'tinymce/plugins/preview'
import 'tinymce/plugins/anchor'
import 'tinymce/plugins/searchreplace'
import 'tinymce/plugins/visualblocks'
import 'tinymce/plugins/code'
import 'tinymce/plugins/fullscreen'
import 'tinymce/plugins/insertdatetime'
import 'tinymce/plugins/media'
import 'tinymce/plugins/table'
import 'tinymce/plugins/help'
import 'tinymce/plugins/wordcount'

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
const editorRef = ref(null)
const inited = ref(false)

const initEditor = () => {
  tinymce.init({
    selector: `#${id.value}`,
    height: props.height,
    min_height: props.minHeight,
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

        xhr.upload.onprogress = e => {
          progress((e.loaded / e.total) * 100)
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
    }
  })
}

const destroyEditor = () => {
  if (editorRef.value && !editorRef.value.destroyed) {
    editorRef.value.destroy()
    editorRef.value = null
    inited.value = false
  }
}

onMounted(() => {
  nextTick(() => {
    initEditor()
  })
})

onUnmounted(() => {
  destroyEditor()
})

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
