<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="设置键" prop="settingKey">
        <el-input
          v-model="queryParams.settingKey"
          placeholder="请输入设置键"
          clearable
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
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['system:setting:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:setting:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:setting:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="settingList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="设置ID" align="center" prop="id" />
      <el-table-column label="设置键" align="center" prop="settingKey" />
      <el-table-column label="设置值" align="center" prop="settingValue" :show-overflow-tooltip="true" />
      <el-table-column label="设置描述" align="center" prop="description" :show-overflow-tooltip="true" />
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
            v-hasPermi="['system:setting:edit']"
          >修改</el-button>
          <el-button
            type="text"
            icon="Delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:setting:remove']"
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

    <!-- 添加或修改博客设置对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form ref="settingRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="设置键" prop="settingKey">
          <el-input v-model="form.settingKey" placeholder="请输入设置键" :disabled="form.id != null" />
        </el-form-item>
        <el-form-item label="设置值" prop="settingValue">
          <el-input v-model="form.settingValue" type="textarea" placeholder="请输入设置值" />
        </el-form-item>
        <el-form-item label="设置描述" prop="description">
          <el-input v-model="form.description" placeholder="请输入设置描述" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 博客基本设置卡片 -->
    <el-card class="box-card mt20">
      <template #header>
        <div class="card-header">
          <span>博客基本设置</span>
        </div>
      </template>
      <el-form ref="basicSettingRef" :model="basicSetting" :rules="basicRules" label-width="100px">
        <el-form-item label="博客名称" prop="blogName">
          <el-input v-model="basicSetting.blogName" placeholder="请输入博客名称" />
        </el-form-item>
        <el-form-item label="博客描述" prop="blogDesc">
          <el-input v-model="basicSetting.blogDesc" type="textarea" placeholder="请输入博客描述" />
        </el-form-item>
        <el-form-item label="博客作者" prop="blogAuthor">
          <el-input v-model="basicSetting.blogAuthor" placeholder="请输入博客作者" />
        </el-form-item>
        <el-form-item label="博客关键词" prop="blogKeywords">
          <el-input v-model="basicSetting.blogKeywords" placeholder="请输入博客关键词，多个关键词用逗号分隔" />
        </el-form-item>
        <el-form-item label="版权信息" prop="blogCopyright">
          <el-input v-model="basicSetting.blogCopyright" placeholder="请输入版权信息" />
        </el-form-item>
        <el-form-item label="备案信息" prop="blogBeian">
          <el-input v-model="basicSetting.blogBeian" placeholder="请输入备案信息" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="saveBasicSetting">保存设置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup name="Setting">
import { ref, reactive, toRefs, getCurrentInstance, onMounted } from 'vue'
import { listSetting, getSetting, delSetting, addSetting, updateSetting, getSettingValueByKey, updateSettingValueByKey } from "@/api/blog/setting";

const { proxy } = getCurrentInstance();

const settingList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    settingKey: null
  },
  rules: {
    settingKey: [
      { required: true, message: "设置键不能为空", trigger: "blur" }
    ],
    settingValue: [
      { required: true, message: "设置值不能为空", trigger: "blur" }
    ]
  }
});

const { queryParams, form, rules } = toRefs(data);

// 博客基本设置
const basicSetting = reactive({
  blogName: '',
  blogDesc: '',
  blogAuthor: '',
  blogKeywords: '',
  blogCopyright: '',
  blogBeian: ''
});

const basicRules = {
  blogName: [
    { required: true, message: "博客名称不能为空", trigger: "blur" }
  ],
  blogAuthor: [
    { required: true, message: "博客作者不能为空", trigger: "blur" }
  ]
};

/** 查询博客设置列表 */
function getList() {
  loading.value = true;
  listSetting(queryParams.value).then(response => {
    settingList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

// 加载博客基本设置
function loadBasicSetting() {
  getSettingValueByKey('blog_name').then(response => {
    basicSetting.blogName = response.data || '';
  });
  getSettingValueByKey('blog_desc').then(response => {
    basicSetting.blogDesc = response.data || '';
  });
  getSettingValueByKey('blog_author').then(response => {
    basicSetting.blogAuthor = response.data || '';
  });
  getSettingValueByKey('blog_keywords').then(response => {
    basicSetting.blogKeywords = response.data || '';
  });
  getSettingValueByKey('blog_copyright').then(response => {
    basicSetting.blogCopyright = response.data || '';
  });
  getSettingValueByKey('blog_beian').then(response => {
    basicSetting.blogBeian = response.data || '';
  });
}

// 保存博客基本设置
function saveBasicSetting() {
  proxy.$refs["basicSettingRef"].validate(valid => {
    if (valid) {
      const promises = [
        updateSettingValueByKey({ settingKey: 'blog_name', settingValue: basicSetting.blogName }),
        updateSettingValueByKey({ settingKey: 'blog_desc', settingValue: basicSetting.blogDesc }),
        updateSettingValueByKey({ settingKey: 'blog_author', settingValue: basicSetting.blogAuthor }),
        updateSettingValueByKey({ settingKey: 'blog_keywords', settingValue: basicSetting.blogKeywords }),
        updateSettingValueByKey({ settingKey: 'blog_copyright', settingValue: basicSetting.blogCopyright }),
        updateSettingValueByKey({ settingKey: 'blog_beian', settingValue: basicSetting.blogBeian })
      ];
      
      Promise.all(promises).then(() => {
        proxy.$modal.msgSuccess("保存成功");
        getList();
      }).catch(() => {
        proxy.$modal.msgError("保存失败");
      });
    }
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
    settingKey: null,
    settingValue: null,
    description: null
  };
  proxy.resetForm("settingRef");
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
  title.value = "添加博客设置";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const id = row.id || ids.value;
  getSetting(id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改博客设置";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["settingRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateSetting(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addSetting(form.value).then(response => {
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
  const settingIds = row.id || ids.value;
  proxy.$modal.confirm('是否确认删除博客设置编号为"' + settingIds + '"的数据项？').then(function() {
    return delSetting(settingIds);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

onMounted(() => {
  getList();
  loadBasicSetting();
});
</script>

<style scoped>
.mt20 {
  margin-top: 20px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>