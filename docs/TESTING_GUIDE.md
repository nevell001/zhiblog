# ZhiBlog 测试指南

## 目录
- [概述](#概述)
- [前端测试](#前端测试)
- [后端测试](#后端测试)
- [测试覆盖率](#测试覆盖率)
- [最佳实践](#最佳实践)

## 概述

ZhiBlog 项目使用以下测试框架：

**前端 (Vue 3)**:
- Vitest: 测试运行器
- Vue Test Utils: Vue 组件测试
- jsdom: 浏览器环境模拟

**后端 (Java 17)**:
- JUnit 5: 测试框架
- Mockito: Mock 框架
- Spring Boot Test: 集成测试
- H2: 内存数据库

## 前端测试

### 运行测试

```bash
cd ruoyi-ui

# 运行所有测试
npm run test

# 运行测试并生成覆盖率报告
npm run test:coverage

# 运行测试 UI
npm run test:ui
```

### 测试文件结构

```
ruoyi-ui/src/
├── utils/
│   ├── validate.ts          # 源文件
│   └── validate.test.ts     # 测试文件
├── composables/
│   ├── useArticleList.ts    # 源文件
│   └── useArticleList.test.ts # 测试文件
└── tests/
    └── setup.ts            # 测试设置
```

### 测试示例

#### 单元测试示例

```typescript
// validate.test.ts
import { describe, it, expect } from 'vitest'
import { isEmail } from '@/utils/validate'

describe('isEmail', () => {
  it('应该验证有效的邮箱地址', () => {
    expect(isEmail('test@example.com')).toBe(true)
  })

  it('应该拒绝无效的邮箱地址', () => {
    expect(isEmail('invalid')).toBe(false)
  })
})
```

#### 组件测试示例

```typescript
// ArticleCard.test.ts
import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import ArticleCard from '@/views/blog/components/ArticleCard.vue'

describe('ArticleCard', () => {
  it('应该正确渲染文章卡片', () => {
    const wrapper = mount(ArticleCard, {
      props: {
        article: {
          id: 1,
          title: '测试文章',
          summary: '文章摘要'
        }
      }
    })

    expect(wrapper.text()).toContain('测试文章')
    expect(wrapper.text()).toContain('文章摘要')
  })
})
```

## 后端测试

### 运行测试

```bash
# 运行所有测试
mvn test

# 运行指定模块的测试
mvn test -pl ruoyi-system

# 运行单个测试类
mvn test -Dtest=BlogArticleServiceTest

# 运行单个测试方法
mvn test -Dtest=BlogArticleServiceTest#testInsertArticle

# 生成测试覆盖率报告
mvn test jacoco:report
```

### 测试文件结构

```
ruoyi-system/src/test/java/com/ruoyi/system/
├── controller/
│   └── BlogArticleControllerTest.java
├── service/
│   └── impl/
│       └── BlogArticleServiceImplTest.java
└── mapper/
    └── BlogArticleMapperTest.java
```

### 测试示例

#### Service 层单元测试

```java
@ExtendWith(MockitoExtension.class)
class BlogArticleServiceImplTest {

    @Mock
    private BlogArticleMapper blogArticleMapper;

    @InjectMocks
    private BlogArticleServiceImpl blogArticleService;

    @Test
    void testSelectBlogArticleById() {
        // 准备测试数据
        BlogArticle article = new BlogArticle();
        article.setId(1L);
        article.setTitle("测试文章");

        // Mock 行为
        when(blogArticleMapper.selectBlogArticleById(1L))
            .thenReturn(article);

        // 执行测试
        BlogArticle result = blogArticleService.selectBlogArticleById(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals("测试文章", result.getTitle());
    }
}
```

#### Controller 层集成测试

```java
@SpringBootTest
@AutoConfigureMockMvc
class BlogArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testListArticles() throws Exception {
        mockMvc.perform(get("/blog/article/list"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200));
    }
}
```

## 测试覆盖率

### 前端覆盖率

目标覆盖率：
- 行覆盖率: ≥ 70%
- 分支覆盖率: ≥ 70%
- 函数覆盖率: ≥ 70%

查看报告：
```bash
npm run test:coverage
# 报告生成在 coverage/index.html
```

### 后端覆盖率

目标覆盖率：
- 行覆盖率: ≥ 60%
- 分支覆盖率: ≥ 60%

查看报告：
```bash
mvn test jacoco:report
# 报告生成在 target/site/jacoco/index.html
```

## 最佳实践

### 1. 测试命名规范

**测试类命名**:
- 后端: `XxxTest.java` 或 `XxxTests.java`
- 前端: `xxx.test.ts` 或 `xxx.spec.ts`

**测试方法命名**:
- 使用描述性名称
- 格式: `test{方法名}_{场景}`
- 示例: `testInsertArticle_Success`, `testInsertArticle_DuplicateTitle`

### 2. AAA 模式

测试方法应遵循 AAA (Arrange-Act-Assert) 模式：

```java
@Test
void testUpdateArticleStatus() {
    // Arrange (准备)
    Long articleId = 1L;
    Integer newStatus = 1;

    when(blogArticleMapper.selectBlogArticleById(articleId))
        .thenReturn(testArticle);

    // Act (执行)
    Integer result = blogArticleService.updateArticleStatus(
        Collections.singletonList(articleId),
        newStatus
    );

    // Assert (断言)
    assertNotNull(result);
    assertEquals(1, result);
}
```

### 3. 测试隔离

- 每个测试应该独立运行
- 使用 `@BeforeEach` 初始化测试数据
- 避免测试之间的依赖关系

### 4. Mock 使用

- 只 Mock 外部依赖
- 不要 Mock 被测试的类
- 使用 `@Mock` 和 `@InjectMocks`

### 5. 测试数据

- 使用固定、可预测的测试数据
- 避免使用随机数据（除非测试随机性本身）
- 测试边界条件和异常情况

### 6. 断言

- 使用具体的断言
- 验证重要的状态和行为
- 一个测试关注一个行为

### 7. 集成测试注意事项

- 使用 H2 内存数据库
- 测试真实场景
- 注意事务回滚

## 常见问题

### Q: 测试失败如何调试？

**前端**:
```bash
# 使用 watch 模式
npm run test -- --watch

# 使用 UI 模式
npm run test:ui
```

**后端**:
```bash
# 打印详细日志
mvn test -X

# 单独运行失败的测试
mvn test -Dtest=ClassName#methodName
```

### Q: 如何测试异步代码？

**前端**:
```typescript
it('应该处理异步操作', async () => {
  const result = await fetchData()
  expect(result).toBeDefined()
})
```

**后端**:
```java
@Test
void testAsyncOperation() throws Exception {
  // 使用 CompletableFuture.get()
  CompletableFuture<Result> future = service.asyncMethod();
  Result result = future.get(5, TimeUnit.SECONDS);

  assertNotNull(result);
}
```

### Q: 如何 Mock 静态方法？

使用 Mockito 的 inline mock maker：

```java
try (MockedStatic<StaticClass> mocked = mockStatic(StaticClass.class)) {
    mocked.when(() -> StaticClass.staticMethod()).thenReturn(value);
    // 测试代码
}
```

## CI/CD 集成

### GitHub Actions 示例

```yaml
name: Test

on: [push, pull_request]

jobs:
  frontend-test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Setup Node.js
        uses: actions/setup-node@v3
        with:
          node-version: '18'
      - name: Install dependencies
        run: cd ruoyi-ui && npm install
      - name: Run tests
        run: cd ruoyi-ui && npm run test:coverage

  backend-test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
      - name: Run tests
        run: mvn test
      - name: Generate coverage report
        run: mvn jacoco:report
```

## 相关文档

- [Vitest 文档](https://vitest.dev/)
- [Vue Test Utils 文档](https://test-utils.vuejs.org/)
- [JUnit 5 用户指南](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito 文档](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)

---

**版本**: v4.0.1
**最后更新**: 2026-01-10
