<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="文章标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入文章标题"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="分类" prop="categoryId">
        <el-select v-model="queryParams.categoryId" placeholder="请选择分类" clearable>
          <el-option
            v-for="category in categoryOptions"
            :key="category.id"
            :label="category.name"
            :value="category.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="草稿" value="0" />
          <el-option label="发布" value="1" />
        </el-select>
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
          v-hasPermi="['system:article:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:article:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:article:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="articleList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="文章ID" align="center" prop="id" />
      <el-table-column label="文章标题" align="center" prop="title" :show-overflow-tooltip="true" />
      <el-table-column label="分类" align="center" prop="categoryName" />
      <el-table-column label="作者" align="center" prop="author" />
      <el-table-column label="状态" align="center" prop="status">
        <template #default="scope">
          <el-tag :type="scope.row.status === '1' ? 'success' : 'info'">
            {{ scope.row.status === '1' ? '发布' : '草稿' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="浏览量" align="center" prop="viewCount" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button
            type="text"
            icon="Edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:article:edit']"
          >修改</el-button>
          <el-button
            type="text"
            icon="Delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:article:remove']"
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

    <!-- 添加或修改博客文章对话框 -->
    <el-dialog :title="title" v-model="open" width="780px" append-to-body>
      <el-form ref="articleRef" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="文章标题" prop="title">
              <el-input v-model="form.title" placeholder="请输入文章标题" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分类" prop="categoryId">
              <el-select v-model="form.categoryId" placeholder="请选择分类">
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
        <el-form-item label="摘要" prop="summary">
          <el-input v-model="form.summary" type="textarea" placeholder="请输入摘要" />
        </el-form-item>
        <el-form-item label="文章内容" prop="content">
          <editor v-model="form.content" :min-height="192"/>
        </el-form-item>
        <el-form-item label="封面图片" prop="coverUrl">
          <image-upload v-model="form.coverUrl"/>
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="是否置顶" prop="isTop">
              <el-radio-group v-model="form.isTop">
                <el-radio :label="1">是</el-radio>
                <el-radio :label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否推荐" prop="isRecommend">
              <el-radio-group v-model="form.isRecommend">
                <el-radio :label="1">是</el-radio>
                <el-radio :label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">发布</el-radio>
            <el-radio :label="0">草稿</el-radio>
          </el-radio-group>
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

<script setup name="Article">
import { ref, reactive, toRefs, getCurrentInstance } from 'vue'
import { listArticle, getArticle, delArticle, addArticle, updateArticle } from "@/api/blog/article";
import { listCategory } from "@/api/blog/category";

const { proxy } = getCurrentInstance();
const { sys_normal_dict } = proxy.useDict("sys_normal_dict");

const articleList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const categoryOptions = ref([]);

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
      { required: true, message: "文章标题不能为空", trigger: "blur" }
    ],
    content: [
      { required: true, message: "文章内容不能为空", trigger: "blur" }
    ]
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询博客文章列表 */
function getList() {
  loading.value = true;
  listArticle(queryParams.value).then(response => {
    articleList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

/** 查询分类列表 */
function getCategoryList() {
  listCategory().then(response => {
    categoryOptions.value = response.rows;
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
    id: null,
    title: null,
    summary: null,
    content: null,
    coverUrl: null,
    categoryId: null,
    isTop: 0,
    isRecommend: 0,
    status: 1
  };
  proxy.resetForm("articleRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加博客文章";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const id = row.id || ids.value;
  getArticle(id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改博客文章";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["articleRef"].validate(valid => {
    if (valid) {
      try {
        // 创建一个新的对象用于提交
        const apiData = {};
        
        // 复制所有字段
        Object.keys(form.value).forEach(key => {
          if (form.value[key] !== null && form.value[key] !== undefined) {
            apiData[key] = form.value[key];
          }
        });
        
        // 特殊处理content字段
        if (form.value.content) {
          let contentStr = form.value.content;
          if (typeof contentStr !== 'string') {
            contentStr = JSON.stringify(contentStr);
          }
          
          // 将HTML内容转换为纯文本
          const tempDiv = document.createElement('div');
          tempDiv.innerHTML = contentStr;
          
          // 保留原始HTML内容
          apiData.content = contentStr;
        } else {
          apiData.content = '';
        }
        
        console.log("提交的表单数据:", apiData);
        
        // 使用现有的API函数而不是直接使用axios
        if (form.value.id != null) {
          updateArticle(apiData).then(response => {
            proxy.$modal.msgSuccess("修改成功");
            open.value = false;
            getList();
          }).catch(error => {
            console.error("更新文章失败:", error);
            proxy.$modal.msgError("更新失败: " + (error.message || "未知错误"));
          });
        } else {
          addArticle(apiData).then(response => {
            proxy.$modal.msgSuccess("新增成功");
            open.value = false;
            getList();
          }).catch(error => {
            console.error("添加文章失败:", error);
            proxy.$modal.msgError("添加失败: " + (error.message || "未知错误"));
          });
        }
      } catch (e) {
        console.error("提交表单时出错:", e);
        proxy.$modal.msgError("提交失败: " + e.message);
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const articleIds = row.id || ids.value;
  proxy.$modal.confirm('是否确认删除博客文章编号为"' + articleIds + '"的数据项？').then(function() {
    return delArticle(articleIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

getList();
getCategoryList();
</script>
