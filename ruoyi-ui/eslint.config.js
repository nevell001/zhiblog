import js from '@eslint/js'
import vue from 'eslint-plugin-vue'
import globals from 'globals'

export default [
  // 忽略文件
  {
    ignores: [
      'node_modules/**',
      'dist/**',
      'build/**',
      '*.config.js',
      '.vite/**',
      '.DS_Store',
      '*.lock',
      'coverage/**',
      'pnpm-lock.yaml',
      'package-lock.json',
      'yarn.lock',
      '*.min.js'
    ]
  },

  // 基础 JavaScript 配置
  js.configs.recommended,

  // Vue 3 配置
  ...vue.configs['flat/recommended'],

  // 自定义规则
  {
    languageOptions: {
      ecmaVersion: 'latest',
      sourceType: 'module',
      globals: {
        ...globals.browser,
        ...globals.node,
        ...globals.es2021,
        // Vue 组合式 API（由 unplugin-auto-import 自动导入）
        ref: 'readonly',
        reactive: 'readonly',
        computed: 'readonly',
        watch: 'readonly',
        watchEffect: 'readonly',
        onMounted: 'readonly',
        onUnmounted: 'readonly',
        onBeforeMount: 'readonly',
        onBeforeUnmount: 'readonly',
        nextTick: 'readonly',
        toRefs: 'readonly',
        toRef: 'readonly',
        shallowRef: 'readonly',
        triggerRef: 'readonly',
        customRef: 'readonly',
        getCurrentInstance: 'readonly',
        onActivated: 'readonly',
        onDeactivated: 'readonly',
        onErrorCaptured: 'readonly',
        onRenderTracked: 'readonly',
        onRenderTriggered: 'readonly',
        provide: 'readonly',
        inject: 'readonly',
        defineComponent: 'readonly',
        defineProps: 'readonly',
        defineEmits: 'readonly',
        defineExpose: 'readonly',
        withDefaults: 'readonly',
        resolveComponent: 'readonly',
        // Vue Router（由 unplugin-auto-import 自动导入）
        useRoute: 'readonly',
        useRouter: 'readonly',
        onBeforeRouteUpdate: 'readonly',
        onBeforeRouteLeave: 'readonly',
        // Pinia（由 unplugin-auto-import 自动导入）
        defineStore: 'readonly',
        storeToRefs: 'readonly',
        createPinia: 'readonly',
        toRaw: 'readonly',
        // Element Plus 组件（由 unplugin-auto-import 自动导入）
        ElMessage: 'readonly',
        ElMessageBox: 'readonly',
        ElNotification: 'readonly',
        ElLoading: 'readonly',
        ElDialog: 'readonly',
        ElForm: 'readonly',
        ElFormItem: 'readonly',
        ElInput: 'readonly',
        ElButton: 'readonly',
        ElTable: 'readonly',
        ElTableColumn: 'readonly',
        ElPagination: 'readonly',
        ElSelect: 'readonly',
        ElOption: 'readonly',
        ElCascader: 'readonly',
        ElCascaderPanel: 'readonly',
        ElCheckbox: 'readonly',
        ElCheckboxGroup: 'readonly',
        ElRadio: 'readonly',
        ElRadioGroup: 'readonly',
        ElSwitch: 'readonly',
        ElSlider: 'readonly',
        ElTimePicker: 'readonly',
        ElDatePicker: 'readonly',
        ElRate: 'readonly',
        ElColorPicker: 'readonly',
        ElTransfer: 'readonly',
        ElFormDialog: 'readonly',
        ElUpload: 'readonly',
        ElProgress: 'readonly',
        ElTree: 'readonly',
        ElPaginationDialog: 'readonly',
        ElTag: 'readonly',
        ElIcon: 'readonly',
        ElRow: 'readonly',
        ElCol: 'readonly',
        ElContainer: 'readonly',
        ElHeader: 'readonly',
        ElAside: 'readonly',
        ElMain: 'readonly',
        ElFooter: 'readonly',
        ElMenu: 'readonly',
        ElMenuItem: 'readonly',
        ElMenuItemGroup: 'readonly',
        ElSubMenu: 'readonly',
        ElBreadcrumb: 'readonly',
        ElBreadcrumbItem: 'readonly',
        ElPageHeader: 'readonly',
        ElDropdown: 'readonly',
        ElDropdownItem: 'readonly',
        ElSteps: 'readonly',
        ElStep: 'readonly',
        ElCard: 'readonly',
        ElCollapse: 'readonly',
        ElCollapseItem: 'readonly',
        ElTabs: 'readonly',
        ElTabPane: 'readonly',
        ElTimeline: 'readonly',
        ElTimelineItem: 'readonly',
        ElDivider: 'readonly',
        ElCalendar: 'readonly',
        ElImage: 'readonly',
        ElBacktop: 'readonly',
        ElDrawer: 'readonly',
        ElPopconfirm: 'readonly',
        ElPopover: 'readonly',
        ElTooltip: 'readonly',
        ElBadge: 'readonly',
        ElAvatar: 'readonly',
        ElSkeleton: 'readonly',
        ElSkeletonItem: 'readonly',
        ElEmpty: 'readonly',
        ElDescriptions: 'readonly',
        ElDescriptionsItem: 'readonly',
        ElResult: 'readonly',
        ElStatistic: 'readonly',
        ElAlert: 'readonly',
        ElMenuScrollbar: 'readonly',
        ElScrollbar: 'readonly',
        ElCarousel: 'readonly',
        ElCarouselItem: 'readonly',
        ElCollapseTransition: 'readonly',
        ElDialogDraggable: 'readonly'
      }
    },
    rules: {
      // Vue 规则
      'vue/multi-word-component-names': 'off',
      'vue/no-v-html': 'warn',
      'vue/require-default-prop': 'off',
      'vue/require-explicit-emits': 'off',
      'vue/no-mutating-props': 'off',
      'vue/no-side-effects-in-computed-properties': 'off',
      'vue/html-self-closing': [
        'error',
        {
          html: {
            void: 'always',
            normal: 'never',
            component: 'always'
          },
          svg: 'always',
          math: 'always'
        }
      ],

      // 通用规则
      'no-console': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
      'no-debugger': process.env.NODE_ENV === 'production' ? 'error' : 'off',
      'no-unused-vars': [
        'warn',
        {
          argsIgnorePattern: '^_',
          varsIgnorePattern: '^_'
        }
      ],

      // 代码风格
      indent: ['error', 2, { SwitchCase: 1 }],
      quotes: ['error', 'single', { avoidEscape: true }],
      semi: ['error', 'never'],
      'comma-dangle': ['error', 'never'],
      'object-curly-spacing': ['error', 'always'],
      'array-bracket-spacing': ['error', 'never'],
      'space-before-function-paren': [
        'error',
        {
          anonymous: 'always',
          named: 'never',
          asyncArrow: 'always'
        }
      ],
      'keyword-spacing': ['error', { before: true, after: true }],
      'comma-spacing': ['error', { before: false, after: true }],
      'key-spacing': ['error', { beforeColon: false, afterColon: true }],
      'no-multiple-empty-lines': ['error', { max: 1, maxEOF: 1 }],
      'eol-last': ['error', 'always'],

      // 最佳实践
      eqeqeq: ['error', 'always'],
      'no-var': 'error',
      'prefer-const': 'error',
      'no-alert': 'warn',
      'no-eval': 'error',
      'no-implied-eval': 'error',
      'no-new-func': 'error',
      'no-new-object': 'error',
      'no-return-await': 'warn'
    }
  }
]