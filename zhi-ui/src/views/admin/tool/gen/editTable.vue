<template>
  <div class="app-container">
    <el-card shadow="never">
      <el-tabs v-model="activeName" type="border-card">
        <!-- 基本信息 -->
        <el-tab-pane label="基本信息" name="basic">
          <el-form ref="basicFormRef" :model="info" :rules="basicRules" label-width="120px">
            <el-row>
              <el-col :span="12">
                <el-form-item label="表名称" prop="tableName">
                  <el-input v-model="info.tableName" disabled />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="表描述" prop="tableComment">
                  <el-input v-model="info.tableComment" placeholder="请输入表描述" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="实体类名称" prop="className">
                  <el-input v-model="info.className" placeholder="请输入实体类名称" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="备注" prop="remark">
                  <el-input
                    v-model="info.remark"
                    type="textarea"
                    :rows="2"
                    placeholder="请输入备注"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-tab-pane>

        <!-- 字段信息 -->
        <el-tab-pane label="字段信息" name="column">
          <el-table :data="columns" :max-height="tableHeight" style="width: 100%">
            <el-table-column label="序号" type="index" width="60" align="center" />
            <el-table-column label="字段列名" prop="columnName" width="160" />
            <el-table-column label="字段描述" width="180">
              <template #default="scope">
                <el-input v-model="scope.row.columnComment" placeholder="请输入字段描述" />
              </template>
            </el-table-column>
            <el-table-column label="物理类型" prop="columnType" width="140" />
            <el-table-column label="Java类型" width="140">
              <template #default="scope">
                <el-select v-model="scope.row.javaType" placeholder="请选择">
                  <el-option
                    v-for="item in javaTypeOptions"
                    :key="item"
                    :label="item"
                    :value="item"
                  />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="java属性" width="160">
              <template #default="scope">
                <el-input v-model="scope.row.javaField" placeholder="请输入java属性" />
              </template>
            </el-table-column>
            <el-table-column label="插入" width="70" align="center">
              <template #default="scope">
                <el-checkbox
                  v-model="scope.row.isInsert"
                  true-value="1"
                  false-value="0"
                  :disabled="scope.row.isPk === '1'"
                />
              </template>
            </el-table-column>
            <el-table-column label="编辑" width="70" align="center">
              <template #default="scope">
                <el-checkbox
                  v-model="scope.row.isEdit"
                  true-value="1"
                  false-value="0"
                  :disabled="scope.row.isPk === '1'"
                />
              </template>
            </el-table-column>
            <el-table-column label="列表" width="70" align="center">
              <template #default="scope">
                <el-checkbox v-model="scope.row.isList" true-value="1" false-value="0" />
              </template>
            </el-table-column>
            <el-table-column label="查询" width="70" align="center">
              <template #default="scope">
                <el-checkbox v-model="scope.row.isQuery" true-value="1" false-value="0" />
              </template>
            </el-table-column>
            <el-table-column label="查询方式" width="140">
              <template #default="scope">
                <el-select
                  v-model="scope.row.queryType"
                  :disabled="scope.row.isQuery !== '1'"
                  placeholder="请选择"
                >
                  <el-option
                    v-for="item in queryTypeOptions"
                    :key="item"
                    :label="item"
                    :value="item"
                  />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="必填" width="70" align="center">
              <template #default="scope">
                <el-checkbox v-model="scope.row.isRequired" true-value="1" false-value="0" />
              </template>
            </el-table-column>
            <el-table-column label="显示类型" width="150">
              <template #default="scope">
                <el-select v-model="scope.row.htmlType" placeholder="请选择">
                  <el-option
                    v-for="item in htmlTypeOptions"
                    :key="item"
                    :label="item"
                    :value="item"
                  />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="字典类型" width="180">
              <template #default="scope">
                <el-input v-model="scope.row.dictType" placeholder="请输入字典类型" />
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- 生成信息 -->
        <el-tab-pane label="生成信息" name="gen">
          <el-form ref="genFormRef" :model="info" :rules="genRules" label-width="140px">
            <el-row>
              <el-col :span="12">
                <el-form-item label="生成模板" prop="tplCategory">
                  <el-select v-model="info.tplCategory" placeholder="请选择生成模板">
                    <el-option
                      v-for="item in tplCategoryOptions"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="生成包路径" prop="packageName">
                  <el-input v-model="info.packageName" placeholder="请输入生成包路径" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="生成模块名" prop="moduleName">
                  <el-input v-model="info.moduleName" placeholder="请输入生成模块名" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="生成业务名" prop="businessName">
                  <el-input v-model="info.businessName" placeholder="请输入生成业务名" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="生成功能名" prop="functionName">
                  <el-input v-model="info.functionName" placeholder="请输入生成功能名" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="作者" prop="functionAuthor">
                  <el-input v-model="info.functionAuthor" placeholder="请输入作者" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="上级菜单">
                  <el-tree-select
                    v-model="info.parentMenuId"
                    :data="menuOptions"
                    :props="{ value: 'id', label: 'label', children: 'children' }"
                    value-key="id"
                    placeholder="请选择上级菜单"
                    check-strictly
                    clearable
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="生成代码方式">
                  <el-radio-group v-model="info.genType">
                    <el-radio value="0">zip压缩包</el-radio>
                    <el-radio value="1">自定义路径</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col v-if="info.genType === '1'" :span="24">
                <el-form-item label="自定义路径" prop="genPath">
                  <el-input v-model="info.genPath" placeholder="请输入自定义路径" />
                </el-form-item>
              </el-col>

              <!-- 树表额外配置 -->
              <template v-if="info.tplCategory === 'tree'">
                <el-col :span="12">
                  <el-form-item label="树编码字段" prop="treeCode">
                    <el-select v-model="info.treeCode" placeholder="请选择树编码字段">
                      <el-option
                        v-for="item in columns"
                        :key="item.columnId"
                        :label="item.columnName"
                        :value="item.columnName"
                      />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="树父编码字段" prop="treeParentCode">
                    <el-select v-model="info.treeParentCode" placeholder="请选择树父编码字段">
                      <el-option
                        v-for="item in columns"
                        :key="item.columnId"
                        :label="item.columnName"
                        :value="item.columnName"
                      />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="树名称字段" prop="treeName">
                    <el-select v-model="info.treeName" placeholder="请选择树名称字段">
                      <el-option
                        v-for="item in columns"
                        :key="item.columnId"
                        :label="item.columnName"
                        :value="item.columnName"
                      />
                    </el-select>
                  </el-form-item>
                </el-col>
              </template>

              <!-- 主子表额外配置 -->
              <template v-if="info.tplCategory === 'sub'">
                <el-col :span="12">
                  <el-form-item label="关联子表的表名" prop="subTableName">
                    <el-select
                      v-model="info.subTableName"
                      placeholder="请选择关联子表的表名"
                      @change="handleSubTableChange"
                    >
                      <el-option
                        v-for="item in tableOptions"
                        :key="item.tableName"
                        :label="(item.tableName || '') + '：' + (item.tableComment || '')"
                        :value="item.tableName"
                      />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="子表关联的外键名" prop="subTableFkName">
                    <el-select v-model="info.subTableFkName" placeholder="请选择子表关联的外键名">
                      <el-option
                        v-for="item in subColumns"
                        :key="item.columnId"
                        :label="item.columnName"
                        :value="item.columnName"
                      />
                    </el-select>
                  </el-form-item>
                </el-col>
              </template>
            </el-row>
          </el-form>
        </el-tab-pane>
      </el-tabs>

      <div class="form-footer">
        <el-button type="primary" :loading="submitting" @click="submitForm">提 交</el-button>
        <el-button @click="close">返 回</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts" name="GenEdit">
import {
  getGenTable,
  updateGenTable,
  columnList,
  type GenTable,
  type GenTableColumn
} from '@/api/tool/gen'
import { treeselect } from '@/api/system/menu'

const route = useRoute()
const { proxy } = getCurrentInstance()

const activeName = ref('basic')
const basicFormRef = ref()
const genFormRef = ref()
const submitting = ref(false)
const tableHeight = ref(window.innerHeight - 320)

const javaTypeOptions = ['Long', 'String', 'Integer', 'Double', 'BigDecimal', 'Date']
const queryTypeOptions = ['EQ', 'NE', 'GT', 'GTE', 'LT', 'LTE', 'LIKE', 'BETWEEN']
const htmlTypeOptions = [
  'input',
  'textarea',
  'select',
  'radio',
  'checkbox',
  'datetime',
  'imageUpload',
  'fileUpload',
  'editor'
]
const tplCategoryOptions = [
  { value: 'crud', label: '单表（增删改查）' },
  { value: 'tree', label: '树表（增删改查）' },
  { value: 'sub', label: '主子表（增删改查）' }
]

const info = ref<GenTable>({
  tableId: undefined,
  tableName: '',
  tableComment: '',
  className: '',
  tplCategory: 'crud',
  packageName: '',
  moduleName: '',
  businessName: '',
  functionName: '',
  functionAuthor: '',
  genType: '0',
  genPath: '',
  remark: ''
})
const columns = ref<GenTableColumn[]>([])
const tableOptions = ref<GenTable[]>([])
const subColumns = ref<GenTableColumn[]>([])
const menuOptions = ref<any[]>([])

const basicRules = {
  tableName: [{ required: true, message: '表名称不能为空', trigger: 'blur' }],
  tableComment: [{ required: true, message: '表描述不能为空', trigger: 'blur' }],
  className: [{ required: true, message: '实体类名称不能为空', trigger: 'blur' }]
}

const genRules = {
  tplCategory: [{ required: true, message: '生成模板不能为空', trigger: 'change' }],
  packageName: [{ required: true, message: '生成包路径不能为空', trigger: 'blur' }],
  moduleName: [{ required: true, message: '生成模块名不能为空', trigger: 'blur' }],
  businessName: [{ required: true, message: '生成业务名不能为空', trigger: 'blur' }],
  functionName: [{ required: true, message: '生成功能名不能为空', trigger: 'blur' }],
  functionAuthor: [{ required: true, message: '作者不能为空', trigger: 'blur' }]
}

/** 查询表信息与字段列表 */
function getGenTableInfo() {
  const tableId = Number(route.params.tableId)
  if (!tableId) {
    ;(proxy as any).$modal.msgError('缺少表编号，无法加载生成配置')
    return
  }
  getGenTable(tableId).then((response: any) => {
    const data = response?.data || {}
    const tableInfo = data.info || {}
    info.value = {
      ...tableInfo,
      genType:
        tableInfo.genType === undefined || tableInfo.genType === null
          ? '0'
          : String(tableInfo.genType),
      parentMenuId: tableInfo.parentMenuId || undefined
    }
    columns.value = (data.rows || []).map((row: GenTableColumn) => ({ ...row }))
    tableOptions.value = data.tables || []
    if (info.value.subTableName) {
      loadSubColumns(info.value.subTableName)
    }
  })
}

/** 加载上级菜单树（失败不影响主体编辑功能） */
function loadMenuOptions() {
  treeselect()
    .then((response: any) => {
      menuOptions.value = response?.data || []
    })
    .catch(() => {
      menuOptions.value = []
    })
}

/** 切换子表时加载子表字段 */
function handleSubTableChange(tableName: string) {
  info.value.subTableFkName = ''
  loadSubColumns(tableName)
}

function loadSubColumns(tableName: string) {
  const target = tableOptions.value.find((item: GenTable) => item.tableName === tableName)
  if (!target?.tableId) {
    subColumns.value = []
    return
  }
  columnList(target.tableId).then((response: any) => {
    subColumns.value = response?.rows || response?.data || []
  })
}

/** 提交前校验树表/主子表必填项 */
function validateExtra(): boolean {
  if (info.value.tplCategory === 'tree') {
    if (!info.value.treeCode) {
      ;(proxy as any).$modal.msgError('树编码字段不能为空')
      return false
    }
    if (!info.value.treeParentCode) {
      ;(proxy as any).$modal.msgError('树父编码字段不能为空')
      return false
    }
    if (!info.value.treeName) {
      ;(proxy as any).$modal.msgError('树名称字段不能为空')
      return false
    }
  } else if (info.value.tplCategory === 'sub') {
    if (!info.value.subTableName) {
      ;(proxy as any).$modal.msgError('关联子表的表名不能为空')
      return false
    }
    if (!info.value.subTableFkName) {
      ;(proxy as any).$modal.msgError('子表关联的外键名不能为空')
      return false
    }
  }
  return true
}

/** 提交按钮 */
async function submitForm() {
  const basicValid = await basicFormRef.value?.validate().catch(() => false)
  if (!basicValid) {
    activeName.value = 'basic'
    return
  }
  const genValid = await genFormRef.value?.validate().catch(() => false)
  if (!genValid) {
    activeName.value = 'gen'
    return
  }
  if (!validateExtra()) {
    activeName.value = 'gen'
    return
  }

  submitting.value = true
  try {
    // 后端以 params 序列化 treeCode/treeParentCode/treeName/parentMenuId 等扩展配置
    const payload: GenTable = {
      ...info.value,
      columns: columns.value,
      params: {
        treeCode: info.value.treeCode,
        treeParentCode: info.value.treeParentCode,
        treeName: info.value.treeName,
        parentMenuId: info.value.parentMenuId,
        parentMenuName: info.value.parentMenuName
      }
    }
    await updateGenTable(payload)
    ;(proxy as any).$modal.msgSuccess('修改成功')
    close()
  } finally {
    submitting.value = false
  }
}

/** 返回代码生成列表 */
function close() {
  ;(proxy as any).$tab.closeOpenPage({
    path: '/admin/tool/gen',
    query: { pageNum: route.query.pageNum, t: Date.now() }
  })
}

getGenTableInfo()
loadMenuOptions()
</script>

<style scoped>
.form-footer {
  display: flex;
  justify-content: center;
  gap: 12px;
  margin-top: 20px;
}
</style>
