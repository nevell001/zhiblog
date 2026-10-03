package com.zhi.framework.aspectj;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.zhi.common.annotation.DataScope;
import com.zhi.common.core.domain.entity.SysRole;
import com.zhi.common.core.domain.entity.SysUser;
import com.zhi.common.core.domain.model.LoginUser;
import com.zhi.framework.security.context.PermissionContextHolder;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 数据权限切面单元测试。
 *
 * <p>这段代码直接拼 SQL 片段到查询参数里，是越权读数据的最后防线：五种数据范围必须各自拼出正确条件，
 * 停用角色与不匹配权限字符的角色一律不产生可见范围（退化成"查不到任何数据"），
 * 超管例外，并且进入方法前必须清掉调用方塞进来的 {@code params.dataScope}（防注入）。</p>
 */
class DataScopeAspectTest
{
    private static final String PERM = "system:user:list";

    /** 只用于提供注解元数据 */
    static class Fixture
    {
        @DataScope(deptAlias = "d", userAlias = "u", permission = PERM)
        public void annotated()
        {
        }

        /** 注解不写 permission，靠 @PreAuthorize 那侧写进请求上下文的权限字符兜底 */
        @DataScope(deptAlias = "d", userAlias = "u")
        public void withoutPermission()
        {
        }
    }

    private JoinPoint joinPoint;

    private SysUser query;

    @BeforeEach
    void setUp()
    {
        query = new SysUser();
        joinPoint = Mockito.mock(JoinPoint.class);
        Mockito.when(joinPoint.getArgs()).thenReturn(new Object[] { query });
        // 切面读注解兜底权限时一定经过请求上下文（RequestContextHolder.currentRequestAttributes 会直接抛错）
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));
    }

    @AfterEach
    void clearContext()
    {
        SecurityContextHolder.clearContext();
        RequestContextHolder.resetRequestAttributes();
    }

    private static DataScope annotationOf(String method) throws Exception
    {
        Method target = Fixture.class.getDeclaredMethod(method);
        return target.getAnnotation(DataScope.class);
    }

    private static SysRole role(long roleId, String dataScope, String... permissions)
    {
        return roleWithStatus(roleId, dataScope, "0", permissions);
    }

    private static SysRole roleWithStatus(long roleId, String dataScope, String status, String... permissions)
    {
        SysRole sysRole = new SysRole();
        sysRole.setRoleId(roleId);
        sysRole.setDataScope(dataScope);
        sysRole.setStatus(status);
        Set<String> perms = new HashSet<>(Arrays.asList(permissions));
        sysRole.setPermissions(perms);
        return sysRole;
    }

    private static SysUser userWith(long userId, long deptId, SysRole... roles)
    {
        SysUser user = new SysUser();
        user.setUserId(userId);
        user.setDeptId(deptId);
        List<SysRole> roleList = new ArrayList<>(Arrays.asList(roles));
        user.setRoles(roleList);
        return user;
    }

    /** 拼出来的条件写在查询对象（切点第一个参数）的 params 里，不是写在身份用户上 */
    private String scopeFor(SysUser identity, String deptAlias, String userAlias, String permission)
    {
        DataScopeAspect.dataScopeFilter(joinPoint, identity, deptAlias, userAlias, permission);
        return (String) query.getParams().get("dataScope");
    }

    @Test
    void allDataScopeRemovesTheFilterEntirely() throws Exception
    {
        SysUser user = userWith(2L, 103L, role(3L, "1", PERM));

        DataScopeAspect.dataScopeFilter(joinPoint, user, "d", "u", PERM);

        assertThat(query.getParams()).doesNotContainKey("dataScope");
    }

    @Test
    void customScopeFiltersByTheRoleDepartmentSubquery() throws Exception
    {
        SysUser user = userWith(2L, 103L, role(102L, "2", PERM));

        DataScopeAspect.dataScopeFilter(joinPoint, user, "d", "u", PERM);

        assertThat((String) query.getParams().get("dataScope"))
                .isEqualTo(" AND (d.dept_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = 102 ) )");
    }

    @Test
    void severalCustomRolesAreCollapsedIntoOneInList() throws Exception
    {
        SysUser user = userWith(2L, 103L, role(102L, "2", PERM), role(103L, "2", PERM));

        DataScopeAspect.dataScopeFilter(joinPoint, user, "d", "u", PERM);

        assertThat((String) query.getParams().get("dataScope")).contains("role_id in (102,103)");
    }

    @Test
    void departmentScopeUsesTheUsersOwnDepartment() throws Exception
    {
        SysUser user = userWith(2L, 103L, role(3L, "3", PERM));

        DataScopeAspect.dataScopeFilter(joinPoint, user, "d", "u", PERM);

        assertThat((String) query.getParams().get("dataScope")).isEqualTo(" AND (d.dept_id = 103 )");
    }

    @Test
    void departmentAndChildScopeWalksTheAncestorsColumn() throws Exception
    {
        SysUser user = userWith(2L, 103L, role(3L, "4", PERM));

        DataScopeAspect.dataScopeFilter(joinPoint, user, "d", "u", PERM);

        assertThat((String) query.getParams().get("dataScope"))
                .isEqualTo(" AND (d.dept_id IN ( SELECT dept_id FROM sys_dept WHERE dept_id = 103 or find_in_set( 103 , ancestors ) ))");
    }

    @Test
    void selfScopeFallsBackToAnImpossibleConditionWithoutAUserAlias() throws Exception
    {
        SysUser user = userWith(2L, 103L, role(3L, "5", PERM));

        assertThat(scopeFor(user, "d", "u", PERM)).isEqualTo(" AND (u.user_id = 2 )");
        assertThat(scopeFor(user, "d", null, PERM)).isEqualTo(" AND (d.dept_id = 0 )");
    }

    @Test
    void disabledRoleOrUnmatchedPermissionGrantsNothing() throws Exception
    {
        // 停用角色、权限字符对不上的角色，都拿不到任何可见范围，只能退化成"查不到数据"
        SysUser disabled = userWith(2L, 103L, roleWithStatus(3L, "3", "1", PERM));
        assertThat(scopeFor(disabled, "d", "u", PERM)).isEqualTo(" AND (d.dept_id = 0 )");

        SysUser unrelated = userWith(2L, 103L, role(3L, "3", "system:post:list"));
        assertThat(scopeFor(unrelated, "d", "u", PERM)).isEqualTo(" AND (d.dept_id = 0 )");
    }

    @Test
    void superAdminIsNeverConstrained() throws Throwable
    {
        SysUser admin = userWith(1L, 103L, role(3L, "5", PERM));
        LoginUser loginUser = new LoginUser(1L, 103L, admin, Collections.emptySet());
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(loginUser, null));

        new DataScopeAspect().doBefore(joinPoint, annotationOf("annotated"));

        // 入口先清空，超管分支不再写入，所以留在空串上
        assertThat(query.getParams()).containsEntry("dataScope", "");
    }

    @Test
    void callerSuppliedDataScopeIsWipedBeforeTheFilterRuns() throws Throwable
    {
        // 前端把 params[dataScope] 当成普通查询参数提交时，绝不能原样进 SQL
        query.getParams().put("dataScope", " OR 1=1");
        SysUser writer = userWith(2L, 103L, role(3L, "3", PERM));
        LoginUser loginUser = new LoginUser(2L, 103L, writer, Collections.emptySet());
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(loginUser, null));

        new DataScopeAspect().doBefore(joinPoint, annotationOf("annotated"));

        assertThat((String) query.getParams().get("dataScope")).isEqualTo(" AND (d.dept_id = 103 )");
    }

    @Test
    void permissionFallsBackToTheRequestContextWhenTheAnnotationOmitsIt() throws Throwable
    {
        SysUser writer = userWith(2L, 103L, role(3L, "3", PERM));
        LoginUser loginUser = new LoginUser(2L, 103L, writer, Collections.emptySet());
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(loginUser, null));
        PermissionContextHolder.setContext(PERM);

        new DataScopeAspect().doBefore(joinPoint, annotationOf("withoutPermission"));

        assertThat((String) query.getParams().get("dataScope")).isEqualTo(" AND (d.dept_id = 103 )");
    }

    @Test
    void missingRequestContextPermissionLeavesTheCallerUnrestrictedByAnyRole() throws Throwable
    {
        // 注解没写 permission、请求上下文也没有 → 没有任何角色能匹配上，直接查不到数据
        SysUser writer = userWith(2L, 103L, role(3L, "3", PERM));
        LoginUser loginUser = new LoginUser(2L, 103L, writer, Collections.emptySet());
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(loginUser, null));

        new DataScopeAspect().doBefore(joinPoint, annotationOf("withoutPermission"));

        assertThat((String) query.getParams().get("dataScope")).isEqualTo(" AND (d.dept_id = 0 )");
    }
}
