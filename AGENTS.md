# AGENTS.md

ZhiBlog — a blog system on the RuoYi-Vue 3.9.1 platform. Spring Boot 3.3.0 backend (Java 17, `jakarta.*` namespace, Spring Security 6) + Vue 3 + TypeScript 5.9 frontend. Requires MySQL 8.4 (not 5.x) and Redis 6.2+. Current version: v1.4.0.

## Commands

### Backend (root of repo)

```bash
mvn clean install -DskipTests      # compile all modules
cd zhi-admin && mvn spring-boot:run
mvn test -Dtest=ClassName          # single test
mvn test -Dtest=ClassName#method   # single method
mvn test -pl zhi-system            # one module
mvn checkstyle:check               # style gate (runs on validate; checkstyle.xml)
```

### Frontend (zhi-ui)

```bash
npm install
npm run dev              # port 3000
npm run build:prod
npm run test             # Vitest
npm run test:coverage
npm run lint:check       # ESLint
npm run format:check     # Prettier
```

### Docker / DB

```bash
docker compose -f docker-compose.dev.yml up -d
docker compose -f docker-compose.prod.yml up -d
mysql -u root -p zhiblog < sql/00_init_database.sql
```

## Architecture

Multi-module Maven. Standard pattern: Controller → Service → Mapper (MyBatis), RuoYi conventions (`BaseController`, `@PreAuthorize` with `blog:entity:action` strings, `AjaxResult`, `TableDataInfo`).

- **zhi-admin** — bootstrap. Entry: `com.zhi.RuoYiApplication` (port 8080). `@ComponentScan(basePackages = {"com.zhi", "com.zhi.system.controller"})`, excludes `DataSourceAutoConfiguration`/`RedisAutoConfiguration` (manually configured).
- **zhi-system** — business logic. Holds the 9 admin blog controllers (`Blog*Controller` under `com.zhi.system.controller`) and MyBatis mappers/XML. Mapper XML: `zhi-system/src/main/resources/mapper/system/`.
- **zhi-framework** — Spring Security, JWT filter, interceptors, AOP aspects, Redis/Druid config.
- **zhi-common** — utils, constants, base domain objects, XSS/filters.
- **zhi-quartz / zhi-generator** — scheduled jobs / CRUD codegen.
- **zhi-ui** — Vue 3 + TS frontend. `views/blog/` (public), `views/admin/`, `api/blog/`, `api/system/`, `components/`, `router/`, `stores/`, `utils/`, `types/`.

Front-end public blog endpoints (`BlogFrontController`, `BlogArticleController`, etc.) live in `zhi-admin/src/main/java/com/zhi/web/controller/blog/` — distinct from the admin `Blog*Controller` classes in zhi-system.

## Auth

Unified login via `UnifiedAuthController` (`zhi-admin/src/main/java/com/zhi/web/controller/auth/`):
- `POST /auth/login` — the only login endpoint for both admin and blog users (legacy `/login` and `/blog/auth/login` are gone). Logout is Spring Security's `POST /logout`.
- `GET /auth/user/info` — current user with roles.
- `BlogAuthController` (`/blog/auth/*`) — registration + email code + password reset only.
- JWT in `Authorization` header. Blog settings control features via `blog_setting` table: `comment_review`, `view_count_enabled`, `like_enabled`, `share_enabled`, `search_enabled`, `sidebar_enabled`, `footer_enabled`, `copyright_enabled`.
- Dev: set `EMAIL_DEV_PRINT_CODE=true` to print verification codes to console instead of sending mail.
- Email codes: `verifyCode` is brute-force protected via Redis fail-counter per `email:codeType` (`max-verify-attempts`, default 5; locked for `verify-lock-minutes`). `BlogFrontController` POST `/comment` and `/article/view/*` are IP rate-limited via `@RateLimiter` (60s / 10 req / IP); `UnifiedAuthController` `POST /auth/login` is IP rate-limited (60s / 20 req / IP); every `/common/upload*` endpoint is IP rate-limited (60s / 20 req / IP).
- Public stats: `GET /blog/stats/overview` (`@Anonymous`) exposes published article/category/tag/comment counts and total views for the public 关于 page. The full admin overview `GET /system-stats/overview` requires `statistics:overview:list` (`StatisticsController`) — never call it from public pages.
- Guestbook (留言板): public `GET /blog/message/list|count` + `POST /blog/message` (`@Anonymous`, IP rate-limited 60s / 5 req, length-validated, nickname/content only; the public list query deliberately omits `email`/`ip`/`user_agent`). The POST also enforces an input time window: a graphic captcha (`code`+`uuid`, same switch as login — reuse `ICaptchaService.validateAndGetAgeSeconds`) younger than 3s is rejected with 「提交过快」, and each visitor (login `userId`, else IP) gets one message per 60s via Redis `blog:message:cooldown:<visitorKey>` (checked **before** the captcha, so a rejection does not burn the single-use code; the frontend shows a countdown on the button). Moderation reuses the `comment_review` setting. Admin endpoints live in `BlogMessageController` (`/system/message`, perms `blog:message:list|query|edit|reply|remove|export`) — replies set status to published.
- Custom pages (自定义页面): public `GET /blog/page/list` + `GET /blog/page/{slug}` (`@Anonymous`, published only; slug detail increments `view_count`). Admin CRUD in `BlogPageController` (`/system/page`, perms `blog:page:*`). `slug` is unique and restricted to `[A-Za-z0-9_-]{1,100}`; `show_in_nav='1'` pages appear in the blog navigation.
- Friend-link apply: public `POST /system/friendLink/apply` requires a graphic captcha (`code` + `uuid`, same switch as login: env `CAPTCHA_ENABLED` overrides `sys.account.captchaEnabled`) and is rejected with 「本站暂未开放友链申请」 when the `friend_link_apply_enabled` setting is off. Validate captcha through `ICaptchaService` (`CaptchaServiceImpl`, Redis key `captcha_codes:<uuid>`, single use) — reuse it for other anonymous writes rather than duplicating the logic.
- PV/UV detail: `blog_visit_log` stores one row per content view (`BlogVisitLogServiceImpl.recordVisit`, called from article view in `BlogFrontController` and page view in `BlogFrontPageController`) with `is_unique='1'` for a visitor's first view of that target that day (Redis `blog:visit:day:<date>:<type>:<id>:<visitorKey>`, 48h TTL). Admin API: `/statistics/visit/list|summary|export|clean|{ids}` (perms `statistics:visit:*`). Retention is 90 days, cleaned daily at 01:10 by `DailyStatsSyncTask`. Note the intentional 口径 difference: `blog_article.view_count` stays 24h-deduped while daily PV counts every visit and UV counts per-day unique visitors.

## Version Management (single source of truth)

Version is defined once in root `pom.xml` (`<version>` and `<app.version>`). When bumping, **must update** root `pom.xml` lines 9/24 AND the parent `<version>` in all 6 child poms (`zhi-common`, `zhi-system`, `zhi-framework`, `zhi-quartz`, `zhi-generator`, `zhi-admin`). Maven resource filtering replaces `@app.version@` in `application.yml`; `GET /system/version` serves it. See `docs/VERSION_MANAGEMENT.md`.

## Config / Env Gotchas

- Env vars come from `.env` (copy `.env.example`). Security-critical: `R_TOKEN_SECRET` (JWT, ≥64 chars), `DRUID_PASSWORD`, `REDIS_PASSWORD`, `DB_PASSWORD`.
- `SecurityConfigValidator` runs at startup: in prod it **blocks startup** if those are missing/weak; in dev it only warns. Set `SECURITY_VALIDATION_ENABLED=false` to bypass (dev only).
- Spring Security 6: use `requestMatchers()` / `authorizeHttpRequests()` / `SecurityFilterChain` — NOT Spring Boot 2 APIs.
- `spring.profiles.active` switches dev/prod; `captchaEnabled` controls captcha (disabled in dev). Env `CAPTCHA_ENABLED` overrides the DB switch.
- Feature switches share ONE rule (every read goes through these helpers — no hand-rolled `equals("true")` checks): only `false` / `"false"` / `"0"` / `0` means OFF; anything else (including a missing row) means ON. Frontend: `useBlogSettingsStore().isFeatureEnabled(key)` — never hand-roll the check in a page. Backend: `BlogSwitchUtils.isOn/isOff` (zhi-common). Server-side enforcement matters: `comment_enabled` rejects `POST /blog/comment`, `like_enabled` rejects the like toggles, `search_enabled` short-circuits `/blog/article/search`, `view_count_enabled` skips counting + visit logging, `comment_review` decides the moderation status (comments and guestbook), `email_notify_enabled` gates comment notification mails, `referer_enabled` drives `RefererPolicy`. `BlogLayout.vue` loads the public settings by itself so footer/nav/copyright switches work on every page (pages that never fetch settings would otherwise fall back to store defaults).
- Security config validation reads Druid credentials as `spring.datasource.druid.stat-view-servlet.login-password` **with an env fallback** (`DRUID_PASSWORD`). Reading the property only made dev warn spuriously and **blocked production startup** whenever only `.env`'s `DRUID_PASSWORD` was set. The Druid console itself is intentionally not configured (the misplaced `statViewServlet`/`filter` block under `druid.master` and the never-loaded `application-druid.yml` were removed in v1.4.0) — DB monitoring goes through Actuator/Prometheus/Grafana.
- `/error` must stay `permitAll` in `SecurityConfig`: filters that call `sendError()` (e.g. `RefererFilter` returning 403) trigger an ERROR dispatch, and if `/error` requires authentication RuoYi's unauthorized handler rewrites the response to `HTTP 200 + code 401`, masking the real status.
- Blog settings cache: `GET /common/blog/setting` caches the aggregated map in Redis at `blog:settings:all` (`CacheConstants.BLOG_SETTINGS_ALL`, 600s TTL). **Every** path that writes `blog_setting` must evict it (`@BlogCacheEvict(value = CacheConstants.BLOG_SETTINGS_ALL)` on the controller method) — missing eviction makes admin toggles appear to have no effect on the frontend, which is why `BlogSettingController`'s 5 write methods all carry the annotation.
- Uploads go to `./uploadPath/` (project root, Docker mount point).
- Vite dev proxies: `/dev-api/*` (strip prefix), `^/blog/api/` → `/blog`, `/profile/` → uploads, `/manage/*` → Actuator. Auto-detects Docker via `DOCKER=true` env var.

## Frontend Conventions

- **100% TypeScript.** All new code must be TS with types. Vite plugins in `vite/plugins/*.js` intentionally stay JavaScript — do NOT migrate them.
- **Auto-import**: `ref`, `computed`, `watch`, `onMounted`, `useRoute`, `useRouter`, Pinia APIs, etc. are auto-imported. Do NOT manually import them. Types in `src/auto-imports.d.ts`.
- Access Vite env vars with optional chaining: `import.meta.env?.VAR || 'default'` (can be undefined).
- API modules in `zhi-ui/src/api/` use the `request` wrapper; each has corresponding types in `src/types/api.d.ts`.
- HMR is enabled. If dev oddities persist, restart with `npm run dev -- --force`. Inside Docker (`DOCKER=true`) the dev server watches with `usePolling` because host-side edits on bind mounts (macOS/Windows) often deliver no inotify events — without it Vite keeps serving a stale module transform and edits look like they had no effect.

## Testing

- **Backend (JaCoCo)**: 60% line + 60% branch minimums enforced via `jacoco:check` in root `pom.xml` (BUNDLE rule). `zhi-admin` and `zhi-generator` set `jacoco.skip=true` (still run tests, no coverage gate). Report at `target/jacoco/index.html`.
- **Frontend (Vitest)**: `npm run test` runs `*.test.ts`/`*.spec.ts` files. Coverage thresholds enforced in `vitest.config.ts` (lines/statements ≥70, functions ≥75, branches ≥55), scoped to `src/stores|utils|api|components` `.ts` files. Tests live alongside source (e.g., `utils/validate.test.ts`).
- Quality: `mvn checkstyle:check`, `npm run lint:check`, `npm run format:check`.

## Git

Conventional commits: `feat:`, `fix:`, `docs:`, `style:`, `refactor:`, `test:`, `chore:` — often with a scope (e.g., `fix(auth):`, `feat(blog):`).

## Useful Docs

- `docs/VERSION_MANAGEMENT.md` — full version bump workflow
- `docs/SECURITY_CONFIG.md` — security config validation details
- `docs/图片压缩功能使用指南.md` — image compression feature guide (three upload endpoints: `/common/upload/compressed|avatar|thumbnail`)
