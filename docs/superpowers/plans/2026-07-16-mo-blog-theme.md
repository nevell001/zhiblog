# 墨 Blog Theme Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build a switchable `mo-blog` visual theme from `docs/design/` without removing the existing default theme.

**Architecture:** Theme choice lives in the existing settings Pinia store and is applied to `document.documentElement` as `theme-mo-blog`. CSS tokens and component overrides are centralized under `ruoyi-ui/src/assets/styles/themes/`, while page-level rules remain scoped to `.theme-mo-blog` so the default theme is untouched.

**Tech Stack:** Vue 3, TypeScript, Pinia, Element Plus, SCSS, Vitest, Vite.

## Global Constraints

- Theme key is exactly `mo-blog`.
- Default theme remains available and must not receive scoped `mo-blog` overrides.
- Root class for the new theme is exactly `theme-mo-blog`.
- Persist selected theme in `localStorage` key `app-theme`.
- Do not introduce a new UI component library.
- Do not rewrite backend APIs, routes, permissions, or data models.
- Do not add external font dependencies; use the design font stack with system fallback.
- All `mo-blog` visual rules must be scoped below `.theme-mo-blog`.
- Keep existing blog/admin workflows and feature toggles.

---

## File Structure

- Create `ruoyi-ui/src/assets/styles/themes/mo-blog.scss`: design tokens, Element Plus overrides, blog page mappings, admin page mappings, responsive rules.
- Create `ruoyi-ui/src/assets/styles/themes/index.scss`: theme stylesheet aggregator.
- Modify `ruoyi-ui/src/assets/styles/index.scss`: import the theme aggregator once.
- Modify `ruoyi-ui/src/utils/theme.ts`: add `AppTheme`, `APP_THEME_STORAGE_KEY`, `normalizeAppTheme`, `applyAppTheme`, and `getStoredAppTheme`.
- Modify `ruoyi-ui/src/utils/theme.test.ts`: test theme normalization, storage, and root class behavior.
- Modify `ruoyi-ui/src/stores/settings.ts`: add `appTheme` state and `setAppTheme` action.
- Modify `ruoyi-ui/src/stores/settings.test.ts`: test default app theme and persistence.
- Modify `ruoyi-ui/src/layout/components/Settings/index.vue`: add a theme selector entry for `default` and `mo-blog`.

## Task 1: Theme State And Root Class

**Files:**
- Modify: `ruoyi-ui/src/utils/theme.ts`
- Modify: `ruoyi-ui/src/utils/theme.test.ts`
- Modify: `ruoyi-ui/src/stores/settings.ts`
- Modify: `ruoyi-ui/src/stores/settings.test.ts`

**Interfaces:**
- Produces: `type AppTheme = 'default' | 'mo-blog'`
- Produces: `APP_THEME_STORAGE_KEY = 'app-theme'`
- Produces: `normalizeAppTheme(value: unknown): AppTheme`
- Produces: `getStoredAppTheme(storage?: Storage): AppTheme`
- Produces: `applyAppTheme(theme: AppTheme, root?: HTMLElement, storage?: Storage): void`
- Produces: `settingsStore.appTheme: AppTheme`
- Produces: `settingsStore.setAppTheme(theme: unknown): void`

- [ ] **Step 1: Write failing theme utility tests**

Add tests to `ruoyi-ui/src/utils/theme.test.ts`:

```ts
describe('app theme helpers', () => {
  it('应该把未知主题归一化为 default', () => {
    expect(normalizeAppTheme('mo-blog')).toBe('mo-blog')
    expect(normalizeAppTheme('default')).toBe('default')
    expect(normalizeAppTheme('unknown')).toBe('default')
    expect(normalizeAppTheme(undefined)).toBe('default')
  })

  it('应该在根节点挂载 mo-blog class 并持久化', () => {
    const root = document.createElement('html')
    const storage = window.localStorage
    storage.clear()

    applyAppTheme('mo-blog', root, storage)

    expect(root.classList.contains('theme-mo-blog')).toBe(true)
    expect(storage.getItem(APP_THEME_STORAGE_KEY)).toBe('mo-blog')
  })

  it('应该切回 default 并移除 mo-blog class', () => {
    const root = document.createElement('html')
    root.classList.add('theme-mo-blog')
    const storage = window.localStorage

    applyAppTheme('default', root, storage)

    expect(root.classList.contains('theme-mo-blog')).toBe(false)
    expect(storage.getItem(APP_THEME_STORAGE_KEY)).toBe('default')
  })
})
```

- [ ] **Step 2: Verify the tests fail**

Run: `cd ruoyi-ui && npm run test -- src/utils/theme.test.ts --run`

Expected: FAIL because `normalizeAppTheme`, `applyAppTheme`, and `APP_THEME_STORAGE_KEY` are not exported yet.

- [ ] **Step 3: Implement theme helpers**

In `ruoyi-ui/src/utils/theme.ts`, export the interfaces listed above. `applyAppTheme` removes `theme-mo-blog` before applying the next theme, then writes the normalized value to storage.

- [ ] **Step 4: Add store tests**

Add tests to `ruoyi-ui/src/stores/settings.test.ts`:

```ts
it('应该初始化应用主题为持久化值', () => {
  localStorage.setItem('app-theme', 'mo-blog')
  const store = useSettingsStore()
  expect(store.appTheme).toBe('mo-blog')
})

it('应该设置应用主题并同步根节点 class', () => {
  const store = useSettingsStore()
  store.setAppTheme('mo-blog')
  expect(store.appTheme).toBe('mo-blog')
  expect(document.documentElement.classList.contains('theme-mo-blog')).toBe(true)
})
```

- [ ] **Step 5: Verify the store tests fail**

Run: `cd ruoyi-ui && npm run test -- src/stores/settings.test.ts --run`

Expected: FAIL because `appTheme` and `setAppTheme` are not implemented yet.

- [ ] **Step 6: Implement store state**

Add `appTheme` to the settings state, initialize it from `getStoredAppTheme()`, call `applyAppTheme()` during initialization, and add `setAppTheme(theme: unknown)`.

- [ ] **Step 7: Verify Task 1 passes**

Run:

```bash
cd ruoyi-ui
npm run test -- src/utils/theme.test.ts src/stores/settings.test.ts --run
```

Expected: PASS.

## Task 2: Theme Styles And Selector

**Files:**
- Create: `ruoyi-ui/src/assets/styles/themes/mo-blog.scss`
- Create: `ruoyi-ui/src/assets/styles/themes/index.scss`
- Modify: `ruoyi-ui/src/assets/styles/index.scss`
- Modify: `ruoyi-ui/src/layout/components/Settings/index.vue`

**Interfaces:**
- Consumes: `settingsStore.appTheme`
- Consumes: `settingsStore.setAppTheme(theme: unknown): void`
- Produces: scoped CSS rules below `.theme-mo-blog`

- [ ] **Step 1: Add selector test coverage through existing store tests**

No new component test is required because `Settings/index.vue` has no current component test harness. Keep behavior covered at store level and validate the UI via TypeScript/build.

- [ ] **Step 2: Create theme stylesheet**

Create `mo-blog.scss` with tokens from `docs/design/prototypes/shared.css`, Element Plus variable overrides, blog layout mappings, article typography, auth/profile mappings, and admin table/sidebar mappings. Every selector starts with `.theme-mo-blog`.

- [ ] **Step 3: Wire stylesheet entry**

Create `themes/index.scss` with `@use './mo-blog.scss';` and add `@use './themes/index.scss';` to the global `index.scss`.

- [ ] **Step 4: Add settings selector**

In `Settings/index.vue`, add an `el-select` labeled `界面主题` with options `默认主题` and `墨 Blog`; bind it to `settingsStore.appTheme` and call `settingsStore.setAppTheme`.

- [ ] **Step 5: Verify full frontend quality gate**

Run:

```bash
cd ruoyi-ui
npx vue-tsc --noEmit --pretty false
npx eslint src --quiet
npm run format:check
npm run test -- --run
npm run build:prod
```

Expected: all commands pass.

## Self-Review

- Spec coverage: Task 1 covers theme identity, root class, persistence, and default fallback. Task 2 covers centralized tokens, Element Plus, blog/admin/auth/profile/editor styling, and the user-facing selector.
- Placeholder scan: no `TBD`, `TODO`, `implement later`, or unresolved placeholder text is present.
- Type consistency: `AppTheme`, `APP_THEME_STORAGE_KEY`, `applyAppTheme`, and `setAppTheme` are named consistently across tasks.
