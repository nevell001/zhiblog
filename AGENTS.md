# AGENTS.md

This file provides guidance to Codex (Codex.ai/code) when working with code in this repository.

## Project Overview

This is a blog system built on RuoYi-Vue 3.9.1 platform - a full-stack application with Spring Boot 3.3.0 backend and Vue 3 + TypeScript frontend (100% migrated). The project uses Java 17, requires MySQL 8.4 and Redis 6.2+, and features a complete blog with article management, comments, tags, categories, friend links, and monitoring (Prometheus + Grafana).

**Current Version**: v1.3.2 (as defined in pom.xml)

**Recent Changes**:
- v1.3.2: Fixed login state synchronization, optimized home page access, improved login UX
- v1.3.1: Optimized blog homepage and about page, fixed article editor content cache
- v1.3.0: Unified authentication controller (`UnifiedAuthController`) for both admin and blog user login
- JWT token-based authentication system with email verification code support

**Blog Feature Toggles**: The system uses blog settings (stored in `blog_setting` table) to control feature availability:
- `comment_review`: Whether comments require moderation
- `view_count_enabled`: Whether to track and display article view counts
- `like_enabled`: Like/点赞 feature toggle
- `share_enabled`: Social sharing buttons toggle
- `search_enabled`: Search functionality toggle
- `sidebar_enabled`: Sidebar widgets toggle
- `footer_enabled`: Footer display toggle
- `copyright_enabled`: Copyright notice toggle

## Common Commands

### Backend (Java/Maven)

```bash
# Compile all modules (from project root)
mvn clean install -DskipTests

# Run backend server
cd ruoyi-admin
mvn spring-boot:run

# Run single test
mvn test -Dtest=TestClassName

# Run single test method
mvn test -Dtest=TestClassName#testMethodName

# Run tests for specific module
mvn test -pl ruoyi-system

# Package without tests
mvn clean package -DskipTests

# Run tests with coverage report
mvn test jacoco:report

# Checkstyle code style check
mvn checkstyle:check

# Check all child module parent versions (for version consistency)
for module in ruoyi-common ruoyi-system ruoyi-framework ruoyi-quartz ruoyi-generator ruoyi-admin; do
    echo "=== $module ==="
    grep -A 5 "<parent>" $module/pom.xml | grep -E "(groupId|artifactId|version)"
done

# Verify version API endpoint
curl http://localhost:8080/system/version
```

### Frontend (Vue 3/Vite + TypeScript)

```bash
cd ruoyi-ui

# Install dependencies
npm install

# Development server (port 3000)
npm run dev

# Production build
npm run build:prod

# Staging build
npm run build:stage

# Preview production build locally
npm run preview

# Testing
npm run test              # Run Vitest tests
npm run test:ui           # Run tests with UI
npm run test:coverage     # Run tests with coverage report

# Code quality
npm run lint              # Fix ESLint issues
npm run lint:check        # Check ESLint issues
npm run format            # Format with Prettier
npm run format:check      # Check Prettier formatting
```

**Important Note**: HMR (Hot Module Replacement) is disabled. Code changes require manual browser refresh.

### Docker Deployment

```bash
# Development environment (with dev server)
docker compose -f docker-compose.dev.yml up -d

# Production environment (with Nginx static serving)
docker compose -f docker-compose.prod.yml up -d

# View logs
docker compose -f docker-compose.dev.yml logs -f ruoyi-admin

# Stop services
docker compose -f docker-compose.dev.yml down
```

### Database

```bash
# Create database
mysql -u root -p
CREATE DATABASE newblog CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# Initialize database with all tables and sample data
mysql -u root -p newblog < sql/init_database.sql

# Optional: Execute permissions setup
mysql -u root -p < sql/00_setup_permissions.sql

# Optional: Add performance indexes
mysql -u root -p newblog < sql/performance_indexes.sql
```

## Module Architecture

This is a multi-module Maven project. Key modules:

### ruoyi-admin
- **Purpose**: Main application entry point and startup module
- **Entry Class**: `com.ruoyi.RuoYiApplication`
- **Port**: 8080 (configurable via `server.port`)
- **Contains**: Controllers for web/admin endpoints, configuration classes

### ruoyi-framework
- **Purpose**: Core framework components and configuration
- **Key Packages**:
  - `config/` - Spring Security, resources, MyBatis, Redis, Druid configuration
  - `security/` - JWT authentication filter, login handling
  - `interceptor/` - Request interceptors
  - `aspectj/` - AOP aspects for logging and permissions
  - `manager/` - Async task management

### ruoyi-system
- **Purpose**: Business logic layer (includes both system and blog features)
- **Blog Controllers**: 8 controllers for blog functionality:
  - `BlogArticleController` - Article CRUD, search, status management
  - `BlogCategoryController` - Category management
  - `BlogTagController` - Tag management
  - `BlogCommentController` - Comments with moderation
  - `BlogFriendLinkController` - Friend links
  - `BlogSettingController` - Blog settings (key-value config)
  - `BlogStatisticsController` - Analytics and statistics
  - `BlogArticleTagController` - Article-tag relations
- **Auth Controllers**:
  - `UnifiedAuthController` - Unified login for both admin and blog users (supports JWT authentication)
- **Standard Pattern**: Controller → Service → Mapper (MyBatis)

### ruoyi-common
- **Purpose**: Shared utilities and common functionality
- **Key Areas**:
  - `utils/` - File upload, image compression, JSON, HTTP, security utilities
  - `filter/` - XSS filter, referer anti-hotlinking filter
  - `constant/` - System constants
  - `core/domain/` - Base entities and domain objects
  - `annotation/` - Custom annotations (data dict, Excel, etc.)
  - `cache/` - Redis cache configuration
  - `exception/` - Global exception handling
  - `xss/` - XSS防护实现

### ruoyi-quartz
- **Purpose**: Scheduled task management using Quartz
- **Contains**: Job scheduling, execution, logging

### ruoyi-generator
- **Purpose**: Code generation for creating new CRUD modules
- **Generates**: Domain, Mapper, Service, Controller, Vue pages

### ruoyi-ui
- **Purpose**: Vue 3 + TypeScript frontend (100% TypeScript migration completed - 230 files)
- **Key Directories**:
  - `views/blog/` - Public-facing blog pages (index, article detail, category, tag, archive, about)
  - `views/admin/` - Admin management interface (loaded dynamically based on permissions)
  - `api/blog/` - Blog API calls (TypeScript)
  - `api/system/` - System/admin API calls (TypeScript)
  - `components/` - Reusable components (ArticleTOC, TinyMCE editor, upload components, etc.)
  - `router/` - Vue Router configuration (TypeScript)
  - `stores/` - Pinia state management (TypeScript)
  - `utils/` - Frontend utilities (request wrapper, auth, etc.) (TypeScript)
  - `types/` - TypeScript type definitions
  - `vite/plugins/` - Vite plugins (JavaScript - run in Node.js environment)

## Important Configuration Details

### Spring Boot 3.x Migration Notes

This project has been upgraded from Spring Boot 2.x to 3.3.0. Key changes:

- **Java 17 required** (was Java 8)
- **jakarta.* namespace** (not javax.*) - When adding new dependencies, ensure they support Jakarta EE
- **Spring Security 6.x APIs**:
  - `antMatchers()` → `requestMatchers()`
  - `authorizeRequests()` → `authorizeHttpRequests()`
  - No more `WebSecurityConfigurerAdapter` - uses `SecurityFilterChain` bean

### Security Configuration

Located in `ruoyi-framework/config/SecurityConfig.java`:

- Blog frontend (`/blog/**`, `/common/blog/**`) allows anonymous access
- Admin backend requires authentication (JWT token-based)
- Swagger, Druid, Actuator open in non-prod environments
- Front-end routes: `/blog/*` (public), `/admin/*` (authenticated)

### Key Environment Variables

```yaml
# Database (defaults to localhost:3306/newblog)
DB_HOST
DB_USERNAME
DB_PASSWORD

# Redis (defaults to localhost:6379, no password)
REDIS_HOST
REDIS_PORT
REDIS_PASSWORD

# JWT signing secret - MUST CHANGE in production (generate: openssl rand -base64 64)
R_TOKEN_SECRET

# Druid monitor credentials (defaults to nevell/no password)
DRUID_USERNAME
DRUID_PASSWORD

# Mail service (for email verification codes)
MAIL_HOST
MAIL_PORT
MAIL_USERNAME
MAIL_PASSWORD
MAIL_STARTTLS
MAIL_SSL

# Email verification code settings
EMAIL_CODE_EXPIRE_MINUTES
EMAIL_CODE_LENGTH
EMAIL_CODE_RATE_LIMIT_SECONDS
EMAIL_CODE_IP_RATE_LIMIT
EMAIL_DEV_PRINT_CODE  # Print codes to console in dev instead of sending emails

# Captcha (disabled in dev by default)
CAPTCHA_ENABLED

# Anti-hotlinking (disabled by default)
REFERER_ENABLED
REFERER_ALLOWED_DOMAINS

# Security validation (v1.2.8+) - Controls security config validation at startup
SECURITY_VALIDATION_ENABLED  # Set to 'false' to disable in dev only (cannot be disabled in prod)
```

### Security Configuration Validation (v1.2.8+)

The system automatically validates critical security configurations at startup via `SecurityConfigValidator`:

**Validated Configurations:**
- `R_TOKEN_SECRET`: Must be set and ≥64 characters (use `openssl rand -base64 64`)
- `DRUID_PASSWORD`: Must be non-empty (use `openssl rand -base64 32`)
- `REDIS_PASSWORD`: Must be non-empty (use `openssl rand -base64 32`)
- `DB_PASSWORD`: Must be set and not a weak password (not root/password/123456, etc.)

**Environment-Specific Behavior:**
- **Development (dev)**: Shows warnings if configs missing, but allows startup
- **Production (prod)**: Blocks startup if any required security config is missing or invalid

**Disabling Validation (Development Only):**
```bash
export SECURITY_VALIDATION_ENABLED=false
```
⚠️ Cannot be disabled in production environment

**Configuration Method:**
Use `.env` file (copy from `.env.example`) to set all required environment variables. The `.env` file is excluded from git.

### Image Compression Configuration

The system includes intelligent image compression via `ImageCompressConfig`:

- Three upload endpoints: `/common/upload/compressed`, `/common/upload/avatar`, `/common/upload/thumbnail`
- Compression thresholds and quality configurable in `application.yml`
- Uses Thumbnailator library

### Database Schema

- **Blog tables** (prefix: `blog_`): article, category, tag, article_tag, comment, friend_link, setting
- **System tables** (prefix: `sys_`): user, role, menu, dept, post, config, dict_*, job, oper_log, logininfor
- MyBatis mappers in `ruoyi-system/src/main/resources/mapper/system/`

### Actuator Monitoring Configuration

Located in `application.yml` under `management.endpoints.web.exposure.include`:
- **Development**: Exposes `health,info,metrics,env,configprops,prometheus`
- **Production**: Should be limited to `health,info,metrics,prometheus` only (env and configprops can leak sensitive data like database passwords)
- Base path: `/manage/actuator`
- Health check details: `show-details: when-authorized`

### Vite Proxy Configuration

The Vite dev server proxies API calls to backend:

- `/dev-api/*` → backend (removes prefix)
- `/blog/api/*` → `/blog/*` on backend
- `/profile/*` → static file uploads
- `/manage/*` → Actuator endpoints

**Auto-detects Docker environment** via `DOCKER=true` environment variable (configured in docker-compose files).

### Frontend Build Configuration

**Code Splitting**: The Vite build includes vendor chunk splitting for optimized loading:
- `vue-vendor`: vue, vue-router, pinia
- `element-plus`: Element Plus and its icons
- `tinymce`: TinyMCE editor
- `echarts`: Charts library
- `highlight`: Syntax highlighting
- `quill`: Quill editor
- `utils`: axios, js-cookie, file-saver, fuse.js, @vueuse/core

### Version Management Architecture

**Unique to this project**: Unified version management system ensuring consistency across all modules:

- **Single Source of Truth**: Version defined once in root `pom.xml` (`<version>` and `<app.version>` properties)
- **Automatic Propagation**: Maven resource filtering replaces `@app.version@` placeholder in `application.yml` during build
- **API Endpoint**: `GET /system/version` returns current version to frontend dynamically
- **Version Update Workflow**:
  1. Update `<version>` and `<app.version>` in root `pom.xml`
  2. Update parent `<version>` in all 6 child modules (common, system, framework, quartz, generator, admin)
  3. Run `mvn clean install -DskipTests` to rebuild with new version
  4. Frontend automatically fetches new version from `/system/version` API

**Files that MUST be updated when changing version**:
- `pom.xml` (root) - lines 9 and 24
- `ruoyi-common/pom.xml` - parent version
- `ruoyi-system/pom.xml` - parent version
- `ruoyi-framework/pom.xml` - parent version
- `ruoyi-quartz/pom.xml` - parent version
- `ruoyi-generator/pom.xml` - parent version
- `ruoyi-admin/pom.xml` - parent version

**Files that are AUTOMATICALLY updated during build**:
- `ruoyi-admin/target/classes/application.yml` (Maven resource filtering)

See `docs/VERSION_MANAGEMENT.md` for the complete version management guide.

### Critical Frontend Configuration

**HMR Disabled**: The project has HMR (Hot Module Replacement) disabled in `vite.config.ts`:
```typescript
server: {
  hmr: false  // Disabled due to compatibility issues
}
```
- **Reason**: Vite HMR system causes runtime error: `Cannot read properties of undefined (reading 'on')`
- **Impact**: Code changes require manual browser refresh (not automatic)
- **Workaround**: This is a known limitation, not a bug to fix

**Vite Plugins**: Located in `vite/plugins/*.js` (5 files):
- These intentionally remain in JavaScript format
- Run in Node.js environment, not affected by TypeScript migration
- Include: auto-import, svg-icons, compression, setup-extend
- Do NOT attempt to migrate these to TypeScript

**Global Error Handling**: In `src/main.ts` (lines 1-42):
- Catches `.on()` HMR errors to prevent runtime failures
- Ignores network resource loading errors (images, fonts, CSS, JS)
- Handles unhandled promise rejections
- Uses event capturing phase for comprehensive error catching

**Environment Variable Protection**: When accessing Vite env vars, use optional chaining:
```typescript
const title = import.meta.env?.VITE_APP_TITLE || '博客管理系统'
```
This prevents crashes when `import.meta.env` is undefined.

## Key Development Patterns

### Backend Controller Pattern

Controllers follow RuoYi conventions:
- Extend `BaseController`
- Use `@PreAuthorize` annotations for permission checks (format: `prefix:entity:action`)
- Return `AjaxResult` with `success()`, `error()`, `put()` methods
- Use `TableDataInfo` for paginated list responses
- Services extend `ServiceImpl<Mapper, Domain>`

### Frontend API Pattern

API modules in `ruoyi-ui/src/api/`:
- Use `request` utility (wraps axios with token and error handling)
- Export functions that return promises
- API base URL automatically handled by proxy

### MyBatis Mapper Locations

- XML files: `src/main/resources/mapper/`
- Java interfaces: `mapper/` package in system module
- Configured in `application.yml` under `mybatis.mapperLocations`

### Permission Strings

Blog permissions follow `blog:entity:action` pattern:
- `blog:article:list`, `blog:article:add`, `blog:article:edit`, `blog:article:remove`
- Similar for category, tag, comment, friendLink, setting

### Authentication Pattern

The system uses unified authentication via `UnifiedAuthController`:
- **Single Login Endpoint**: `POST /auth/login` handles both admin and blog user authentication
- **JWT Token-Based**: Returns JWT token stored in `Authorization` header
- **User Info Endpoint**: `GET /auth/user/info` returns current user with roles
- **Role-Based Access**: Frontend determines access level based on user roles
- **Email Verification**: Supports email verification codes for blog user registration
- **Development Mode**: Set `EMAIL_DEV_PRINT_CODE=true` to print codes to console instead of sending emails

### Auto-Import Pattern

The project uses `unplugin-auto-import` for Vue composition APIs:
- Auto-imported: `ref`, `computed`, `reactive`, `watch`, `onMounted`, etc. from Vue
- Auto-imported: `useRoute`, `useRouter` from Vue Router
- Auto-imported: `useStore`, `storeToRefs` from Pinia
- Do NOT manually import these - they're available globally
- Generated types in `src/auto-imports.d.ts`

## Access URLs (Local Development)

- **Blog Frontend**: http://localhost:3000/blog
- **Admin Backend**: http://localhost:3000/admin
- **Backend API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **Druid Monitor**: http://localhost:8080/druid (nevell/[password])
- **Actuator Health**: http://localhost:8080/manage/actuator/health
- **Prometheus**: http://localhost:9090
- **Grafana**: http://localhost:3001 (admin/admin)
- **RSS Feed**: http://localhost:8080/blog/rss
- **Unified Auth API**: http://localhost:8080/auth/login (POST - unified login for admin and blog users)

**Default admin credentials**: admin / admin123

## Special Considerations

1. **Frontend Language**: Project 100% migrated to TypeScript 5.9.3 - all new code MUST use TypeScript with proper types
2. **HMR Disabled**: Hot Module Replacement is disabled - requires manual browser refresh after code changes
3. **Vite Plugins**: Keep `vite/plugins/*.js` files in JavaScript format - do not migrate to TypeScript
4. **Environment Variables**: Always use optional chaining for Vite env vars: `import.meta.env?.VAR_NAME || 'default'`
5. **Upload Directory**: `./uploadPath/` in project root (mount point for Docker)
6. **Database**: MySQL 8.4 required (not 5.x)
7. **Captcha**: Disabled in development, controlled by `captchaEnabled` in application.yml
8. **Profile-based configs**: Use `spring.profiles.active` (dev/prod) to switch environments
9. **Component Scanning**: Main app scans `com.ruoyi` and `com.ruoyi.system.controller` packages
10. **Excluded Auto-configurations**: DataSourceAutoConfiguration and RedisAutoConfiguration (manually configured)
11. **MyBatis SQL Logging**: Enabled in dev via `com.ruoyi.system.mapper: debug` in application.yml
12. **Type Definition Files**: All API modules have corresponding types in `src/types/api.d.ts`
13. **Docker Environment Detection**: Frontend automatically detects Docker via `DOCKER=true` env var for proxy configuration
14. **Version Management**: Version defined in `pom.xml` (currently 1.3.2), sync with `application.yml` ruoyi.version for consistency. See `docs/VERSION_MANAGEMENT.md` for comprehensive version update instructions
15. **Email Verification**: In development, verification codes are printed to console instead of sending emails (controlled by `EMAIL_DEV_PRINT_CODE`)
16. **ARM64 Docker Support**: Dockerfile-admin uses standard eclipse-temurin images (not Alpine) for better ARM64 compatibility
17. **Security Config Validation**: As of v1.2.8, production startup requires all security configs (R_TOKEN_SECRET, DRUID_PASSWORD, REDIS_PASSWORD, DB_PASSWORD) to be properly set. Missing or weak passwords will block production startup

## Testing Requirements

### Backend Testing

The project uses JaCoCo for test coverage with the following requirements:
- **Minimum 60% line coverage** across all packages
- **Minimum 60% branch coverage** across all packages

Run `mvn test` to verify coverage - the build will fail if thresholds are not met.

View coverage report: `target/jacoco/index.html` (after running `mvn test jacoco:report`)

### Frontend Testing

The project uses Vitest with TypeScript and component testing support. Run:
- `npm run test` - Basic test run (TypeScript files in `*.test.ts`)
- `npm run test:ui` - Interactive test UI
- `npm run test:coverage` - Coverage report (targets ≥70% coverage)
- `npm run test -- --watch` - Watch mode for development
- `npm run test -- <pattern>` - Run tests matching a pattern (e.g., `npm run test -- validate`)

**Test Files**: Located alongside source files with `.test.ts` extension (e.g., `utils/validate.test.ts`)

### Code Quality

- **Checkstyle**: `mvn checkstyle:check` (config: `checkstyle.xml`)
- **ESLint**: `npm run lint:check` (config: `eslint.config.js`) - Includes auto-import support
- **Prettier**: `npm run format:check` (config: `.prettierrc`)

**VS Code Integration**: Recommended settings in `.vscode/settings.json`:
```json
{
  "editor.formatOnSave": true,
  "editor.defaultFormatter": "esbenp.prettier-vscode",
  "editor.codeActionsOnSave": {
    "source.fixAll.eslint": "explicit"
  }
}
```

Recommended VS Code extensions: dbaeumer.vscode-eslint, esbenp.prettier-vscode, vue.volar

**Auto-Import**: The project uses `unplugin-auto-import` to automatically import Vue APIs:
- Generated types in `src/auto-imports.d.ts`
- ESLint config in `.eslintrc-auto-import.json`
- No need to manually import `ref`, `computed`, `reactive`, etc. from Vue

## Git Commit Conventions

The project follows conventional commit format:
- `feat:` - New features
- `fix:` - Bug fixes
- `docs:` - Documentation updates
- `style:` - Code formatting changes
- `refactor:` - Code refactoring
- `test:` - Test additions or modifications
- `chore:` - Build/tooling changes

Examples from recent commits:
- `feat(blog): 实现博客用户注册登录认证系统`
- `fix(auth): 修复登录认证系统问题`
- `release: 版本更新到v1.2.9`

## Troubleshooting Common Issues

### Backend Issues

**Version number display incorrect**:
- Check version in `pom.xml` (root project) - both `<version>` and `<app.version>` must match
- Ensure all child module parent versions match the root POM version
- After updating version, run `mvn clean install` to rebuild
- See `docs/VERSION_MANAGEMENT.md` for detailed instructions
- Sync with `ruoyi.version` in `application.yml` (uses `@app.version@` placeholder replaced by Maven)

**Database connection fails**:
- Verify MySQL 8.4+ is running: `mysql --version`
- Check connection string in `application.yml`
- Ensure database exists: `CREATE DATABASE newblog CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;`

**Application startup fails with security validation errors**:
- This happens in production when required security configs are missing or invalid
- Required configs: `R_TOKEN_SECRET` (≥64 chars), `DRUID_PASSWORD`, `REDIS_PASSWORD`, `DB_PASSWORD` (not weak password)
- Check startup logs for specific validation errors
- Generate strong passwords: `openssl rand -base64 64` (for JWT) or `openssl rand -base64 32` (for others)
- In development, set `SECURITY_VALIDATION_ENABLED=false` to bypass (never use in production)

**Redis connection errors**:
- Redis 6.2+ required
- Check if Redis is running: `redis-cli ping`
- In Docker, Redis service name is `redis` (not `localhost`)

**JWT token errors in production**:
- Must set `R_TOKEN_SECRET` environment variable (min 64 characters)
- Generate: `openssl rand -base64 64`
- Development will use temporary key but log warnings

**Email verification not sending**:
- Check mail service configuration in `application.yml`
- In development, codes print to console when `EMAIL_DEV_PRINT_CODE=true`
- Verify mail provider settings (Gmail requires App Password)

### Frontend Issues

**Login redirects/loops**:
- Clear browser cache and localStorage
- Check that backend is running on port 8080
- Verify JWT token is being set in localStorage

**CORS errors**:
- Ensure Vite proxy is configured correctly in `vite.config.ts`
- In Docker, check that `DOCKER=true` environment variable is set
- Verify backend CORS filter allows your origin

**Build fails with TypeScript errors**:
- Ensure all new code uses proper TypeScript types
- Check `src/types/api.d.ts` for API type definitions
- Run `npm run lint:check` to identify issues

**Build fails with "Non-resolvable parent POM"**:
- This indicates version mismatch between parent and child modules
- Check that all child module parent `<version>` tags match root `pom.xml` `<version>`
- Use: `grep -A 5 "<parent>" ruoyi-*/pom.xml | grep version` to check all versions
- See `docs/VERSION_MANAGEMENT.md` for detailed troubleshooting

**HMR not working**:
- HMR is intentionally disabled due to compatibility issues
- Manually refresh browser after code changes
- This is a known limitation, not a bug

### Docker Issues

**Container exits immediately**:
- Check logs: `docker compose -f docker-compose.dev.yml logs -f ruoyi-admin`
- Verify environment variables in docker-compose file
- Ensure MySQL container is ready before backend starts

**ARM64/M1 Mac issues**:
- Dockerfile-admin uses standard eclipse-temurin images (not Alpine)
- Images support both ARM64 and AMD64 architectures
- Pull latest images: `docker compose pull`

**Upload files not persisting**:
- Check that `./uploadPath/` directory exists and has proper permissions
- Docker volume should mount this directory
- Verify files are written to host machine, not just container
