<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="标签名称" prop="tagName">
        <el-input
          v-model="queryParams.tagName"
          placeholder="请输入标签名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="最小文章数" prop="articleCount">
        <el-input
          v-model="queryParams.articleCount"
          placeholder="最小文章数"
          clearable
          @keyup.enter="handleQuery"
          style="width: 120px;"
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
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['system:tag:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:tag:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:tag:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="DataAnalysis"
          @click="handleStatistics"
          v-hasPermi="['system:tag:list']"
        >统计</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['system:tag:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Upload"
          @click="handleImport"
          v-hasPermi="['system:tag:import']"
        >导入</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="tagList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="标签ID" align="center" prop="tagId" />
      <el-table-column label="标签名称" align="center" prop="tagName" :show-overflow-tooltip="true">
        <template #default="scope">
          <div style="display: flex; align-items: center;">
            <span 
              class="tag-color-badge"
              :style="{ 
                backgroundColor: normalizeColor(scope.row.color), 
                color: 'white'
              }"
            >
              <i v-if="scope.row.icon" :class="scope.row.icon"></i>
            </span>
            {{ scope.row.tagName }}
          </div>
        </template>
      </el-table-column>
      <el-table-column label="标签描述" align="center" prop="description" :show-overflow-tooltip="true" />
      <el-table-column label="关联文章数" align="center" prop="articleCount">
        <template #default="scope">
          <el-tag type="success">{{ scope.row.articleCount || 0 }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button
            link
            type="primary"
            icon="Edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:tag:edit']"
          >修改</el-button>
          <el-button
            link
            type="primary"
            icon="Delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:tag:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改博客标签对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form ref="tagRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="标签名称" prop="tagName">
          <el-input v-model="form.tagName" placeholder="请输入标签名称" />
        </el-form-item>
        <el-form-item label="标签描述" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入标签描述" :rows="3" />
        </el-form-item>
        <el-form-item label="标签颜色" prop="color">
          <el-color-picker v-model="form.color" show-alpha :predefine="predefineColors" />
          <el-input v-model="form.color" placeholder="请输入颜色值" style="width: 200px; margin-left: 10px;" />
        </el-form-item>
        <el-form-item label="标签图标" prop="icon">
          <el-input v-model="form.icon" placeholder="请输入图标类名（如：el-icon-star）" />
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

<script setup name="Tag">
import { ref, reactive, toRefs, getCurrentInstance } from 'vue'
import { listTag, getTag, delTag, addTag, updateTag } from "@/api/blog/tag";

const { proxy } = getCurrentInstance();

const tagList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");

// 确保颜色值格式正确
const normalizeColor = (color) => {
  if (!color) return '#409EFF'; // 默认颜色
  
  // 确保颜色值以#开头
  if (color.startsWith('#')) {
    return color;
  }
  
  // 如果颜色值不以#开头，添加#
  return '#' + color;
};

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    tagName: null,
    articleCount: null
  },
  rules: {
    tagName: [
      { required: true, message: "标签名称不能为空", trigger: "blur" },
      { min: 1, max: 20, message: "标签名称长度必须介于 1 和 20 之间", trigger: "blur" }
    ],
    description: [
      { max: 255, message: "标签描述长度不能超过 255 个字符", trigger: "blur" }
    ],
    color: [
      { pattern: /^(#([0-9a-fA-F]{3}|[0-9a-fA-F]{6}|[0-9a-fA-F]{8})|rgb\(\s*\d{1,3}\s*,\s*\d{1,3}\s*,\s*\d{1,3}\s*\)|rgba\(\s*\d{1,3}\s*,\s*\d{1,3}\s*,\s*\d{1,3}\s*,\s*(0|1|0\.\d+)\s*\)|hsl\(\s*\d{1,3}\s*,\s*\d{1,3}%\s*,\s*\d{1,3}%\s*\)|hsla\(\s*\d{1,3}\s*,\s*\d{1,3}%\s*,\s*\d{1,3}%\s*,\s*(0|1|0\.\d+)\s*\))$/, message: "颜色格式不正确", trigger: "blur" }
    ]
  },
  predefineColors: [
    '#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399',
    '#ff4500', '#ff8c00', '#ffd700', '#90ee90', '#00ced1',
    '#1e90ff', '#c71585', 'rgba(255, 69, 0, 0.68)',
    'rgb(255, 120, 0)', 'hsv(51, 100, 98)', 'hsva(120, 40, 94, 0.5)',
    'hsl(181, 100%, 37%)', 'hsla(209, 100%, 56%, 0.73)'
  ]
});

const { queryParams, form, rules, predefineColors } = toRefs(data);

/** 查询博客标签列表 */
function getList() {
  loading.value = true;
  listTag(queryParams.value).then(response => {
    console.log('标签列表数据:', response.rows); // 调试信息
    // 检查每个标签的颜色数据
    response.rows.forEach((tag, index) => {
      console.log(`标签${index}:`, tag.tagName, '颜色:', tag.color, '图标:', tag.icon);
    });
    tagList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  }).catch(error => {
    console.error('获取标签列表失败:', error);
    loading.value = false;
  });
}

// 取消按钮
function cancel() {
  open.value = false;
  reset();
}

// 表单重置
function reset() {
  form.value = {
    tagId: null,
    tagName: null,
    description: null,
    color: '#409EFF',
    icon: null
  };
  proxy.resetForm("tagRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  queryParams.value.articleCount = null;
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.tagId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加博客标签";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const tagId = row.tagId || ids.value;
  getTag(tagId).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改博客标签";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["tagRef"].validate(valid => {
    if (valid) {
      if (form.value.tagId != null) {
        updateTag(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addTag(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const tagIds = row.tagId || ids.value;
  proxy.$modal.confirm('是否确认删除博客标签编号为"' + tagIds + '"的数据项？').then(function() {
    return delTag(tagIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(error => {
    // 显示后端返回的错误信息（如"标签已关联文章"）
    if (error && error.response && error.response.data && error.response.data.msg) {
      proxy.$modal.msgError(error.response.data.msg);
    }
  });
}

/** 统计按钮操作 */
function handleStatistics() {
  // 计算统计信息
  const totalTags = tagList.value.length;
  const totalArticles = tagList.value.reduce((sum, tag) => sum + (tag.articleCount || 0), 0);
  const usedTags = tagList.value.filter(tag => tag.articleCount > 0).length;

  proxy.$modal.info({
    title: '标签统计信息',
    content: `
      <div style="line-height: 2;">
        <p>总标签数：${totalTags}</p>
        <p>已使用标签：${usedTags}</p>
        <p>未使用标签：${totalTags - usedTags}</p>
        <p>总关联文章数：${totalArticles}</p>
      </div>
    `,
    dangerouslyUseHTMLString: true
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('system/tag/export', {
    ...queryParams.value
  }, `tag_${new Date().getTime()}.xlsx`)
}

/** 导入按钮操作 */
function handleImport() {
  proxy.$modal.msgInfo("标签导入功能正在开发中，如需导入请联系管理员手动处理。");
}

getList();
</script>

<style scoped>
.tag-color-badge {
  display: inline-block;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  margin-right: 8px;
  vertical-align: middle;
}
</style>