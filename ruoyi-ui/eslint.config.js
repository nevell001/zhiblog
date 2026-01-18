import js from '@eslint/js'
import vue from 'eslint-plugin-vue'
import globals from 'globals'
import tseslint from '@typescript-eslint/eslint-plugin'
import tsParser from '@typescript-eslint/parser'

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
    files: ['**/*.vue', '**/*.ts', '**/*.tsx', '**/*.js'],
    rules: {
      // 禁用未使用变量检查
      'no-unused-vars': 'off',
      '@typescript-eslint/no-unused-vars': 'off',
      'no-useless-escape': 'off',
      // 禁用 alert 检查（Editor 组件中使用 prompt 是合理的）
      'no-alert': 'off',
      // 禁用 v-html 检查（文章详情页中使用 v-html 渲染 Markdown 是合理的）
      'vue/no-v-html': 'off'
    }
  },

  // Vue 文件配置
  {
    files: ['**/*.vue'],
    languageOptions: {
      parser: vue.parser,
      parserOptions: {
        ecmaVersion: 'latest',
        sourceType: 'module',
        ecmaFeatures: {
          jsx: true
        },
        parser: {
          ts: tsParser,
          '<template>': false
        }
      },
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
      'vue/no-v-html': 'off',
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

      // 忽略 TypeScript 关键字错误
      'no-reserved-keys': 'off',

      // 通用规则
      'no-console': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
      'no-debugger': process.env.NODE_ENV === 'production' ? 'error' : 'off',
      'no-unused-vars': 'off',
      'no-alert': 'off',
      'no-useless-escape': 'off',

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
  },

  // 忽略 TypeScript 关键字错误的覆盖规则
  {
    files: ['src/components/SvgIcon/index.vue'],
    rules: {
      'no-reserved-keys': 'off'
    }
  },

  // Vue 文件中的 TypeScript 配置
  {
    files: ['**/*.vue'],
    rules: {
      // 允许 Vue 文件中使用 TypeScript 语法
      'no-undef': 'off',
      'no-unused-vars': 'off',
      'no-reserved-keys': 'off'
    }
  },

  // TypeScript 文件配置
  {
    files: ['**/*.ts', '**/*.tsx'],
    languageOptions: {
      parser: tsParser,
      parserOptions: {
        ecmaVersion: 'latest',
        sourceType: 'module'
      },
      globals: {
        ...globals.node,
        ...globals.es2021
      }
    },
    plugins: {
      '@typescript-eslint': tseslint
    },
    rules: {
      '@typescript-eslint/no-unused-vars': 'off',
      '@typescript-eslint/no-explicit-any': 'off',
      '@typescript-eslint/ban-ts-comment': 'warn'
    }
  },

  // JavaScript 文件配置
  {
    files: ['**/*.js'],
    rules: {
      '@typescript-eslint/no-explicit-any': 'off',
      '@typescript-eslint/no-unused-vars': 'off'
    }
  },

  // 配置文件支持 Node.js 全局变量
  {
    files: ['vite.config.ts', 'vitest.config.ts', 'vitest.config.ts'],
    languageOptions: {
      globals: {
        ...globals.node,
        process: 'readonly',
        __dirname: 'readonly',
        __filename: 'readonly'
      }
    }
  },

  // vite 插件文件支持 Node.js 全局变量
  {
    files: ['vite/plugins/*.js'],
    languageOptions: {
      globals: {
        ...globals.node,
        process: 'readonly'
      }
    }
  }
]