# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

ZhiBlog (知博) is a full-stack blog system built on Spring Boot 3.3.0 + Vue 3 + Element Plus + TypeScript. The backend is a multi-module Maven project based on RuoYi-Vue 3.9.1, upgraded to Jakarta EE and Java 17.

**Current Version**: v1.3.4 (defined in `pom.xml`)

**Brand Identity**:
- Brand name: "ZhiBlog - 知博"
- Theme name: "默 Blog" (mo-blog) - refers to the visual theme style only
- Internal package structure uses `com.ruoyi.*` (framework naming convention)
- Never display "若依" or "墨 Blog" (incorrect character) in user-facing code

## Common Commands

### Backend (Java/Maven)

```bash
# From project root
mvn clean install -DskipTests           # Build all modules
cd ruoyi-admin && mvn spring-boot:run   # Run backend server (port 8080)

# Testing
mvn test -Dtest=TestClassName            # Run single test
mvn test -pl ruoyi-system               # Test specific module
mvn test jacoco:report                   # Coverage report
mvn checkstyle:check                    # Code style check

# Check parent version consistency across modules
for m in ruoyi-common ruoyi-system ruoyi-framework ruoyi-quartz ruoyi-generator ruoyi-admin; do
  grep -A 5 "<parent>" $m/pom.xml | grep -E "(groupId|artifactId|version)"
done
```

### Frontend (Vue 3/Vite + TypeScript)

```bash
cd ruoyi-ui
npm run dev              # Dev server (port 3000)
npm run build:prod      # Production build
npm run test             # Run Vitest tests
npm run test:coverage    # Coverage report
npm run lint             # Fix ESLint issues
npm run format           # Format with Prettier
```

**Important**: If HMR becomes stale, restart with `npm run dev -- --force`.

### Docker Deployment

```bash
docker compose -f docker-compose.dev.yml up -d --build    # Development
docker compose -f docker-compose.prod.yml up -d --build   # Production
```

No default `docker-compose.yml` exists - must specify dev or prod.

### Database

```bash
mysql -u root -p
CREATE DATABASE zhiblog CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
mysql -u root -p zhiblog < sql/00_init_database.sql
```

## Architecture

### Multi-Module Backend Structure

- **ruoyi-admin**: Main entry point (`com.ruoyi.RuoYiApplication`), contains controllers for web/admin endpoints
- **ruoyi-framework**: Core framework (Security config, JWT filter, MyBatis, Redis config, AOP aspects)
- **ruoyi-system**: Business logic layer - includes both system admin and blog features
- **ruoyi-common**: Shared utilities (file upload, image compression, XSS filter, cache, exceptions)
- **ruoyi-quartz**: Scheduled task management
- **ruoyi-generator**: Code generation for CRUD modules

### Frontend Structure

- `views/blog/`: Public-facing blog pages
- `views/admin/`: Admin management (dynamically loaded by permissions)
- `api/blog/` & `api/system/`: Typed API calls
- `stores/`: Pinia state management (TypeScript)
- 100% TypeScript migration completed (230+ files)

### Key Architectural Patterns

**Backend Request Flow**: Controller → Service → Mapper (MyBatis)

**Blog Controllers** (in ruoyi-system):
- `BlogArticleController` - Article CRUD, search, status
- `BlogCategoryController`, `BlogTagController`, `BlogCommentController`
- `BlogFriendLinkController`, `BlogSettingController`
- `UnifiedAuthController` - Unified login for admin + blog users (JWT)

**Blog Feature Toggles** (stored in `blog_setting` table):
- `comment_review`, `view_count_enabled`, `like_enabled`, `share_enabled`
- `search_enabled`, `sidebar_enabled`, `footer_enabled`, `copyright_enabled`

### Version Management

Version defined in `pom.xml` (`<version>` and `<app.version>`). When updating:
1. Update root `pom.xml` `<version>` and `<app.version>`
2. Update all 6 sub-module parent `<version>` references
3. Frontend version in `package.json` is independent (currently 4.1.0)

See `docs/VERSION_MANAGEMENT.md` for detailed instructions.

## Important Configuration Details

### Spring Boot 3.x Migration

- **Java 17 required** (was Java 8)
- **jakarta.* namespace** (not javax.*) - ensure dependencies support Jakarta EE
- **Spring Security 6.x APIs**: `antMatchers()` → `requestMatchers()`, `authorizeRequests()` → `authorizeHttpRequests()`
- No `WebSecurityConfigurerAdapter` - uses `SecurityFilterChain` bean

### Security Configuration

Located in `ruoyi-framework/config/SecurityConfig.java`:
- Blog frontend (`/blog/**`, `/common/blog/**`) allows anonymous access
- Admin backend requires JWT authentication
- Swagger, Druid, Actuator open in non-prod environments

### Security Validation (v1.2.8+)

Automatic validation at startup via `SecurityConfigValidator`:

**Required Configs** (use `openssl rand -base64 64` for secrets):
- `R_TOKEN_SECRET`: Must be ≥64 characters
- `DRUID_PASSWORD`: Must be non-empty
- `REDIS_PASSWORD`: Must be non-empty
- `DB_PASSWORD`: Must be set and not a weak password

**Environment Behavior**:
- **Dev**: Shows warnings for missing configs, allows startup
- **Prod**: Blocks startup if any required security config is missing

Disable validation in dev only: `export SECURITY_VALIDATION_ENABLED=false`

### Environment Variables

Configure via `.env` file (copy from `.env.example`):

```bash
# Database
DB_HOST=localhost
DB_USERNAME=zhiblog_app
DB_PASSWORD={your_password}
DB_NAME=zhiblog

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_PASSWORD={your_password}

# JWT
R_TOKEN_SECRET={your_secret_key_64_chars}

# Email (for verification codes)
MAIL_HOST
MAIL_PORT
MAIL_USERNAME
MAIL_PASSWORD

# Captcha
CAPTCHA_ENABLED
```

### Image Compression

Three upload endpoints with intelligent compression:
- `/common/upload/compressed` - General compression
- `/common/upload/avatar` - Avatar compression (200x200)
- `/common/upload/thumbnail` - Thumbnail compression (400x400)

Configurable in `application.yml` via `ImageCompressConfig`.

## Key Files to Reference

- `AGENTS.md` - Comprehensive development guide
- `docs/VERSION_MANAGEMENT.md` - Version update procedures
- `docs/SECURITY_CONFIG.md` - Security configuration details
