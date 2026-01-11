<template>
  <div class="app-container">
    <el-form
      v-show="showSearch"
      ref="queryRef"
      :model="queryParams"
      :inline="true"
      label-width="68px"
    >
      <el-form-item label="分类名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入分类名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="排序" prop="sort">
        <el-input
          v-model="queryParams.sort"
          placeholder="排序值"
          clearable
          style="width: 120px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:category:add']"
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
          v-hasPermi="['system:category:edit']"
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
          v-hasPermi="['system:category:remove']"
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
          v-hasPermi="['system:category:export']"
          type="info"
          plain
          icon="Download"
          @click="handleExport"
        >
          导出
        </el-button>
      </el-col>
      <right-toolbar v-model:show-search="showSearch" @query-table="getList" />
    </el-row>

    <el-table v-loading="loading" :data="categoryList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="分类ID" align="center" prop="id" />
      <el-table-column label="分类名称" align="center" prop="name" :show-overflow-tooltip="true" />
      <el-table-column label="排序" align="center" prop="sort" width="100" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button
            v-hasPermi="['system:category:edit']"
            link
            type="primary"
            icon="Edit"
            @click="handleUpdate(scope.row)"
          >
            修改
          </el-button>
          <el-button
            v-hasPermi="['system:category:remove']"
            link
            type="primary"
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

    <el-dialog v-model="open" :title="title" width="500px" append-to-body>
      <el-form ref="categoryRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input v-model="form.sort" placeholder="请输入排序值（数字越小越靠前）" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Category">
import { ref, reactive, toRefs, getCurrentInstance } from 'vue'
import {
  listCategory,
  getCategory,
  delCategory,
  addCategory,
  updateCategory
} from '@/api/blog/category'

const { proxy } = getCurrentInstance()

const categoryList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref('')

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: null,
    sort: null
  },
  rules: {
    name: [
      { required: true, message: '分类名称不能为空', trigger: 'blur' },
      { min: 1, max: 20, message: '分类名称长度必须介于 1 和 20 之间', trigger: 'blur' }
    ],
    sort: [{ pattern: /^\d+$/, message: '排序必须为非负整数', trigger: 'blur' }]
  }
})

const { queryParams, form, rules } = toRefs(data)

function getList() {
  loading.value = true
  listCategory(queryParams.value)
    .then(response => {
      categoryList.value = response.rows
      total.value = response.total
      loading.value = false
    })
    .catch(() => {
      loading.value = false
    })
}

function cancel() {
  open.value = false
  reset()
}

function reset() {
  form.value = {
    id: null,
    name: null,
    sort: 0
  }
  proxy.resetForm('categoryRef')
}

function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

function resetQuery() {
  proxy.resetForm('queryRef')
  handleQuery()
}

function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id)
  single.value = selection.length !== 1
  multiple.value = !selection.length
}

function handleAdd() {
  reset()
  open.value = true
  title.value = '添加分类'
}

function handleUpdate(row) {
  reset()
  const id = row.id || ids.value
  getCategory(id).then(response => {
    form.value = response.data
    open.value = true
    title.value = '修改分类'
  })
}

function submitForm() {
  proxy.$refs['categoryRef'].validate(valid => {
    if (valid) {
      if (form.value.sort !== null && form.value.sort !== undefined && form.value.sort !== '') {
        form.value.sort = Number(form.value.sort)
      }
      if (form.value.id !== null) {
        updateCategory(form.value).then(() => {
          proxy.$modal.msgSuccess('修改成功')
          open.value = false
          getList()
        })
      } else {
        addCategory(form.value).then(() => {
          proxy.$modal.msgSuccess('新增成功')
          open.value = false
          getList()
        })
      }
    }
  })
}

function handleDelete(row) {
  const delIds = row.id || ids.value
  proxy.$modal
    .confirm('是否确认删除分类编号为"' + delIds + '"的数据项？')
    .then(function () {
      return delCategory(delIds)
    })
    .then(() => {
      getList()
      proxy.$modal.msgSuccess('删除成功')
    })
    .catch(() => {})
}

function handleExport() {
  proxy.download(
    'system/category/export',
    {
      ...queryParams.value
    },
    `category_${new Date().getTime()}.xlsx`
  )
}

getList()
</script>
