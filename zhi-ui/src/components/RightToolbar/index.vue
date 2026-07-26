<template>
  <div class="top-right-btn" :style="style">
    <el-row>
      <el-tooltip
        v-if="search"
        class="item"
        effect="dark"
        :content="showSearch ? '隐藏搜索' : '显示搜索'"
        placement="top"
      >
        <el-button circle icon="Search" @click="toggleSearch()" />
      </el-tooltip>
      <el-tooltip class="item" effect="dark" content="刷新" placement="top">
        <el-button circle icon="Refresh" @click="refresh()" />
      </el-tooltip>
      <el-tooltip
        v-if="Object.keys(columns).length > 0"
        class="item"
        effect="dark"
        content="显隐列"
        placement="top"
      >
        <el-button v-if="showColumnsType === 'transfer'" circle icon="Menu" @click="showColumn()" />
        <el-dropdown
          v-if="showColumnsType === 'checkbox'"
          trigger="click"
          :hide-on-click="false"
          style="padding-left: 12px"
        >
          <el-button circle icon="Menu" />
          <template #dropdown>
            <el-dropdown-menu>
              <!-- 全选/反选 按钮 -->
              <el-dropdown-item>
                <el-checkbox
                  v-model="isChecked"
                  :indeterminate="isIndeterminate"
                  @change="toggleCheckAll"
                >
                  列展示
                </el-checkbox>
              </el-dropdown-item>
              <div class="check-line"></div>
              <template v-for="(item, key) in columns" :key="item.key">
                <el-dropdown-item>
                  <el-checkbox
                    v-model="item.visible"
                    :label="item.label"
                    @change="checkboxChange($event, key)"
                  />
                </el-dropdown-item>
              </template>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-tooltip>
    </el-row>
    <el-dialog v-model="open" :title="title" append-to-body>
      <el-transfer
        v-model="value"
        :titles="['显示', '隐藏']"
        :data="transferData"
        @change="dataChange"
      />
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import type { CSSProperties, PropType } from 'vue'

interface ColumnItem {
  key?: string | number
  label?: string
  visible?: boolean
}

const props = defineProps({
  /* 是否显示检索条件 */
  showSearch: {
    type: Boolean,
    default: true
  },
  /* 显隐列信息（数组格式、对象格式） */
  columns: {
    type: [Array, Object] as PropType<ColumnItem[] | Record<string, ColumnItem>>,
    default: () => ({})
  },
  /* 是否显示检索图标 */
  search: {
    type: Boolean,
    default: true
  },
  /* 显隐列类型（transfer穿梭框、checkbox复选框） */
  showColumnsType: {
    type: String,
    default: 'checkbox'
  },
  /* 右外边距 */
  gutter: {
    type: Number,
    default: 10
  }
})

const emits = defineEmits(['update:showSearch', 'queryTable'])
const columns = computed(() => props.columns as ColumnItem[] | Record<string, ColumnItem>)

// 显隐数据
const value = ref<number[]>([])
// 弹出层标题
const title = ref('显示/隐藏')
// 是否显示弹出层
const open = ref(false)

const style = computed(() => {
  const ret: CSSProperties = {}
  if (props.gutter) {
    ret.marginRight = `${props.gutter / 2}px`
  }
  return ret
})

// 是否全选/半选 状态
const isChecked = computed({
  get: () =>
    Array.isArray(columns.value)
      ? columns.value.every(col => col.visible)
      : Object.values(columns.value).every(col => col.visible),
  set: () => {}
})
const isIndeterminate = computed(() =>
  Array.isArray(columns.value)
    ? columns.value.some(col => col.visible) && !isChecked.value
    : Object.values(columns.value).some(col => col.visible) && !isChecked.value
)
const transferData = computed(() =>
  Array.isArray(columns.value)
    ? columns.value.map((item, index) => ({ key: index, label: item.label }))
    : Object.keys(columns.value).map((key, index) => ({
        key: index,
        label: (columns.value as Record<string, ColumnItem>)[key].label
      }))
)

// 搜索
function toggleSearch() {
  emits('update:showSearch', !props.showSearch)
}

// 刷新
function refresh() {
  emits('queryTable')
}

// 右侧列表元素变化
function dataChange(data: Array<string | number>) {
  if (Array.isArray(columns.value)) {
    for (const item in columns.value) {
      const key = columns.value[item].key
      columns.value[item].visible = !data.includes(key || item)
    }
  } else {
    Object.keys(columns.value).forEach((key, index) => {
      ;(columns.value as Record<string, ColumnItem>)[key].visible = !data.includes(index)
    })
  }
}

// 打开显隐列dialog
function showColumn() {
  open.value = true
}

if (props.showColumnsType === 'transfer') {
  // transfer穿梭显隐列初始默认隐藏列
  if (Array.isArray(columns.value)) {
    for (const item in columns.value) {
      if (columns.value[item].visible === false) {
        value.value.push(parseInt(item))
      }
    }
  } else {
    Object.keys(columns.value).forEach((key, index) => {
      if ((columns.value as Record<string, ColumnItem>)[key].visible === false) {
        value.value.push(index)
      }
    })
  }
}

// 单勾选
function checkboxChange(event: boolean, key: string | number) {
  if (Array.isArray(columns.value)) {
    const column = columns.value.find(item => item.key === key)
    if (column) {
      column.visible = event
    }
  } else {
    ;(columns.value as Record<string, ColumnItem>)[key].visible = event
  }
}

// 切换全选/反选
function toggleCheckAll() {
  const newValue = !isChecked.value
  if (Array.isArray(columns.value)) {
    columns.value.forEach(col => (col.visible = newValue))
  } else {
    Object.values(columns.value).forEach(col => (col.visible = newValue))
  }
}
</script>

<style lang="scss" scoped>
:deep(.el-transfer__button) {
  border-radius: 50%;
  display: block;
  margin-left: 0px;
}
:deep(.el-transfer__button:first-child) {
  margin-bottom: 10px;
}
:deep(.el-dropdown-menu__item) {
  line-height: 30px;
  padding: 0 17px;
}
.check-line {
  width: 90%;
  height: 1px;
  background-color: #ccc;
  margin: 3px auto;
}
</style>
