<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="文章标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入文章标题"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="分类" prop="categoryId">
        <el-select v-model="queryParams.categoryId" placeholder="请选择分类" filterable clearable>
          <el-option
            v-for="item in categoryOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="作者" prop="author">
        <el-input
          v-model="queryParams.author"
          placeholder="请输入作者"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否置顶 0否 1是" prop="isTop">
        <el-input
          v-model="queryParams.isTop"
          placeholder="请输入是否置顶 0否 1是"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否推荐 0否 1是" prop="isRecommend">
        <el-input
          v-model="queryParams.isRecommend"
          placeholder="请输入是否推荐 0否 1是"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="浏览量" prop="viewCount">
        <el-input
          v-model="queryParams.viewCount"
          placeholder="请输入浏览量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="点赞数" prop="likeCount">
        <el-input
          v-model="queryParams.likeCount"
          placeholder="请输入点赞数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="评论数" prop="commentCount">
        <el-input
          v-model="queryParams.commentCount"
          placeholder="请输入评论数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:article:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:article:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:article:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:article:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="articleList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键ID" align="center" prop="id" />
      <el-table-column label="文章标题" align="center" prop="title" />
      <el-table-column label="摘要" align="center" prop="summary" />
      <el-table-column label="封面图片" align="center" prop="coverUrl" />
      <el-table-column label="分类" align="center" prop="categoryId">
        <template slot-scope="scope">
          <span>{{ getCategoryName(scope.row.categoryId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="作者" align="center" prop="author" />
      <el-table-column label="是否置顶" align="center" prop="isTop">
        <template slot-scope="scope">
          <span>{{ scope.row.isTop === 1 ? '是' : '否' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="是否推荐" align="center" prop="isRecommend">
        <template slot-scope="scope">
          <span>{{ scope.row.isRecommend === 1 ? '是' : '否' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态 0草稿 1发布" align="center" prop="status" />
      <el-table-column label="浏览量" align="center" prop="viewCount" />
      <el-table-column label="点赞数" align="center" prop="likeCount" />
      <el-table-column label="评论数" align="center" prop="commentCount" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:article:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:article:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改博客文章对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="文章标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入文章标题" />
        </el-form-item>
        <el-form-item label="摘要" prop="summary">
          <el-input v-model="form.summary" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="文章内容" prop="content">
          <editor v-model="form.content" :min-height="400"/>
        </el-form-item>
        <el-form-item label="封面图片" prop="coverUrl">
          <file-upload
            v-model="form.coverUrl"
            :limit="1"
            :file-size="2"
            :file-type="['jpg','jpeg','png']"
            :is-show-tip="true"
            action="/common/upload"
            @on-success="handleCoverUploadSuccess"
          />
          <div v-if="form.coverUrl" style="margin-top:10px;">
            <img :src="$store.state.settings.apiBaseUrl + form.coverUrl" alt="封面图片" style="max-width: 200px; max-height: 120px; border:1px solid #eee;" />
            <el-link type="danger" @click="form.coverUrl=''">删除</el-link>
          </div>
        </el-form-item>
        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="form.categoryId" placeholder="请选择分类" filterable>
            <el-option
              v-for="item in categoryOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="作者" prop="author">
          <el-input v-model="form.author" placeholder="请输入作者" />
        </el-form-item>
        <el-form-item label="是否置顶" prop="isTop">
          <el-select v-model="form.isTop" placeholder="请选择">
            <el-option label="否" :value="0" />
            <el-option label="是" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="是否推荐" prop="isRecommend">
          <el-select v-model="form.isRecommend" placeholder="请选择">
            <el-option label="否" :value="0" />
            <el-option label="是" :value="1" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listArticle, getArticle, delArticle, addArticle, updateArticle } from "@/api/system/article"
import Editor from '@/components/Editor'
import { listCategory } from "@/api/system/category"
import FileUpload from '@/components/FileUpload'

export default {
  name: "Article",
  components: { Editor, FileUpload },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 博客文章表格数据
      articleList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: null,
        summary: null,
        content: null,
        coverUrl: null,
        categoryId: null,
        author: null,
        isTop: null,
        isRecommend: null,
        status: null,
        viewCount: null,
        likeCount: null,
        commentCount: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        title: [
          { required: true, message: "文章标题不能为空", trigger: "blur" }
        ],
        content: [
          { required: true, message: "文章内容不能为空", trigger: "blur" },
          { min: 10, message: "内容不能少于10个字", trigger: "blur" }
        ],
      },
      categoryOptions: [],
    }
  },
  created() {
    this.getList()
    this.loadCategoryOptions()
  },
  methods: {
    /** 查询博客文章列表 */
    getList() {
      this.loading = true
      listArticle(this.queryParams).then(response => {
        this.articleList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        title: null,
        summary: null,
        content: null,
        coverUrl: null,
        categoryId: null,
        author: null,
        isTop: null,
        isRecommend: null,
        status: null,
        viewCount: null,
        likeCount: null,
        commentCount: null,
        createTime: null,
        updateTime: null,
        delFlag: null
      }
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加博客文章"
      this.form.author = this.$store.state.user.nickName
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getArticle(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改博客文章"
        this.form.author = this.$store.state.user.nickName
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateArticle(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addArticle(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除博客文章编号为"' + ids + '"的数据项？').then(function() {
        return delArticle(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/article/export', {
        ...this.queryParams
      }, `article_${new Date().getTime()}.xlsx`)
    },
    loadCategoryOptions() {
      listCategory().then(res => {
        this.categoryOptions = res.rows || []
      })
    },
    getCategoryName(id) {
      const item = this.categoryOptions.find(c => c.id === id)
      return item ? item.name : ''
    },
    handleCoverUploadSuccess(res, file) {
      if(res.code === 200) {
        this.form.coverUrl = res.fileName;
        this.$message.success('封面图片上传成功');
      } else {
        this.$message.error(res.msg || '封面图片上传失败');
      }
    },
  }
}
</script>
