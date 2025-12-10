<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="文章标题" prop="articleTitle">
        <el-input
          v-model="queryParams.articleTitle"
          placeholder="请输入文章标题"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="评论内容" prop="content">
        <el-input
          v-model="queryParams.content"
          placeholder="请输入评论内容"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="评论状态" clearable>
          <el-option label="待审核" value="0" />
          <el-option label="已审核" value="1" />
          <el-option label="已删除" value="2" />
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
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['blog:comment:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Check"
          :disabled="multiple"
          @click="handleAudit"
          v-hasPermi="['blog:comment:audit']"
        >审核通过</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Close"
          :disabled="multiple"
          @click="handleReject"
          v-hasPermi="['blog:comment:reject']"
        >审核拒绝</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['blog:comment:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="commentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="评论ID" align="center" prop="id" />
      <el-table-column label="文章标题" align="center" prop="articleTitle" :show-overflow-tooltip="true" />
      <el-table-column label="评论内容" align="center" prop="content" :show-overflow-tooltip="true" />
      <el-table-column label="评论人" align="center" prop="authorName" />
      <el-table-column label="邮箱" align="center" prop="email" />
      <el-table-column label="IP地址" align="center" prop="ipAddress" />
      <el-table-column label="状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :options="comment_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-tooltip content="审核通过" placement="top" v-if="scope.row.status === '0'">
            <el-button link type="success" icon="Check" @click="handleAudit(scope.row)" v-hasPermi="['blog:comment:audit']"></el-button>
          </el-tooltip>
          <el-tooltip content="审核拒绝" placement="top" v-if="scope.row.status === '0'">
            <el-button link type="warning" icon="Close" @click="handleReject(scope.row)" v-hasPermi="['blog:comment:reject']"></el-button>
          </el-tooltip>
          <el-tooltip content="删除" placement="top">
            <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['blog:comment:remove']"></el-button>
          </el-tooltip>
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
  </div>
</template>

<script setup name="BlogComment">
import { listComment, delComment, auditComment, rejectComment } from "@/api/admin/blog/comment";

const { proxy } = getCurrentInstance();
const { comment_status } = proxy.useDict("comment_status");

const commentList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    articleTitle: undefined,
    content: undefined,
    status: undefined
  }
});

const { queryParams } = toRefs(data);

/** 查询评论列表 */
function getList() {
  loading.value = true;
  listComment(queryParams.value).then(response => {
    commentList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
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

/** 多选框选中数据 */
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 审核通过按钮操作 */
function handleAudit(row) {
  const commentIds = row.id || ids.value;
  proxy.$modal.confirm('是否确认审核通过评论编号为"' + commentIds + '"的数据项？').then(function() {
    return auditComment(commentIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("审核通过成功");
  }).catch(() => {});
}

/** 审核拒绝按钮操作 */
function handleReject(row) {
  const commentIds = row.id || ids.value;
  proxy.$modal.confirm('是否确认审核拒绝评论编号为"' + commentIds + '"的数据项？').then(function() {
    return rejectComment(commentIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("审核拒绝成功");
  }).catch(() => {});
}

/** 删除按钮操作 */
function handleDelete(row) {
  const commentIds = row.id || ids.value;
  proxy.$modal.confirm('是否确认删除评论编号为"' + commentIds + '"的数据项？').then(function() {
    return delComment(commentIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('blog/comment/export', {
    ...queryParams.value
  }, `comment_${new Date().getTime()}.xlsx`)
}

getList();
</script>