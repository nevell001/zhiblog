package com.zhi.framework.aspectj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.zhi.common.annotation.Log;
import com.zhi.common.core.domain.AjaxResult;
import com.zhi.common.core.domain.entity.SysDept;
import com.zhi.common.core.domain.entity.SysUser;
import com.zhi.common.core.domain.model.LoginUser;
import com.zhi.common.enums.BusinessStatus;
import com.zhi.common.enums.BusinessType;
import com.zhi.common.enums.OperatorType;
import com.zhi.common.exception.ServiceException;
import com.zhi.common.filter.PropertyPreExcludeFilter;
import com.zhi.framework.testsupport.SpringContextStub;
import com.zhi.system.domain.SysOperLog;
import com.zhi.system.service.ISysOperLogService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * 操作日志切面单元测试。
 *
 * <p>这个切面把方法入参和返回值序列化后写进 sys_oper_log，是唯一必须保证"密码绝不落库"的地方：
 * 敏感字段名由 {@link LogAspect#EXCLUDE_PROPERTIES} + 注解 excludeParamNames 一起决定。
 * 断言都打在异步落库的那条 {@code SysOperLog} 上（AsyncFactory 的真实出口），而不是打在日志文本上。</p>
 */
class LogAspectTest
{
    private static final String SECRET = "P@ssw0rd-should-never-hit-the-db";

    /** 只用于提供注解元数据 */
    static class Fixture
    {
        @Log(title = "用户管理", businessType = BusinessType.INSERT)
        public void add()
        {
        }

        @Log(title = "敏感操作", excludeParamNames = { "token" })
        public void withExcludedParam()
        {
        }

        @Log(title = "静默接口", isSaveRequestData = false, isSaveResponseData = false)
        public void silent()
        {
        }

        @Log(title = "导出", businessType = BusinessType.EXPORT)
        public void export()
        {
        }
    }

    /** 被记录的方法入参 */
    public static class Payload
    {
        private String nickname = "张三";

        private String password = SECRET;

        private String token = "bearer-should-be-dropped";

        public String getNickname()
        {
            return nickname;
        }

        public String getPassword()
        {
            return password;
        }

        public String getToken()
        {
            return token;
        }
    }

    private final LogAspect aspect = new LogAspect();

    private final AtomicReference<SysOperLog> recorded = new AtomicReference<>();

    private MockHttpServletRequest request;

    private JoinPoint joinPoint;

    @BeforeEach
    void setUp()
    {
        SpringContextStub.install();
        ISysOperLogService operLogService = SpringContextStub
                .stub(ISysOperLogService.class, Mockito.mock(ISysOperLogService.class));
        Mockito.doAnswer(invocation ->
        {
            recorded.set(invocation.getArgument(0));
            return null;
        }).when(operLogService).insertOperlog(Mockito.any());

        request = new MockHttpServletRequest("POST", "/system/user");
        request.setRemoteAddr("127.0.0.1");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        loginAs("zhangsan", "研发部门");
        joinPoint = mockJoinPoint(new Payload());
    }

    @AfterEach
    void clearContext()
    {
        SecurityContextHolder.clearContext();
        RequestContextHolder.resetRequestAttributes();
    }

    private static void loginAs(String userName, String deptName)
    {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(2L);
        sysUser.setUserName(userName);
        SysDept dept = new SysDept();
        dept.setDeptName(deptName);
        sysUser.setDept(dept);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                new LoginUser(2L, 103L, sysUser, Collections.emptySet()), null));
    }

    private static Log annotationOf(String method) throws Exception
    {
        return Fixture.class.getDeclaredMethod(method).getAnnotation(Log.class);
    }

    private static JoinPoint mockJoinPoint(Object... args)
    {
        JoinPoint point = Mockito.mock(JoinPoint.class);
        Mockito.when(point.getArgs()).thenReturn(args);
        Mockito.when(point.getTarget()).thenReturn(new Fixture());
        MethodSignature signature = Mockito.mock(MethodSignature.class);
        Mockito.when(signature.getName()).thenReturn("add");
        Mockito.when(point.getSignature()).thenReturn(signature);
        return point;
    }

    /** 切面自己会吞掉异常，只能等异步任务真的把记录交给 ISysOperLogService 才算跑通 */
    private SysOperLog awaitRecorded() throws InterruptedException
    {
        long deadline = System.currentTimeMillis() + 3000L;
        while (recorded.get() == null)
        {
            if (System.currentTimeMillis() > deadline)
            {
                throw new AssertionError("操作日志未在超时前落库");
            }
            Thread.sleep(20L);
        }
        return recorded.get();
    }

    @Test
    void successfulRequestIsStoredWithIdentityAndRequestMetadata() throws Exception
    {
        aspect.doBefore(joinPoint, annotationOf("add"));
        aspect.doAfterReturning(joinPoint, annotationOf("add"), AjaxResult.success("操作成功"));

        SysOperLog operLog = awaitRecorded();
        assertThat(operLog.getTitle()).isEqualTo("用户管理");
        assertThat(operLog.getBusinessType()).isEqualTo(BusinessType.INSERT.ordinal());
        assertThat(operLog.getOperatorType()).isEqualTo(OperatorType.MANAGE.ordinal());
        assertThat(operLog.getMethod()).endsWith("Fixture.add()");
        assertThat(operLog.getRequestMethod()).isEqualTo("POST");
        assertThat(operLog.getOperUrl()).isEqualTo("/system/user");
        assertThat(operLog.getOperIp()).isEqualTo("127.0.0.1");
        assertThat(operLog.getOperLocation()).isEqualTo("内网IP");
        assertThat(operLog.getOperName()).isEqualTo("zhangsan");
        assertThat(operLog.getDeptName()).isEqualTo("研发部门");
        assertThat(operLog.getStatus()).isEqualTo(BusinessStatus.SUCCESS.ordinal());
        assertThat(operLog.getJsonResult()).contains("操作成功");
        assertThat(operLog.getCostTime()).isNotNegative();
    }

    @Test
    void passwordIsNeverWrittenToTheOperationLog() throws Exception
    {
        aspect.doBefore(joinPoint, annotationOf("add"));
        aspect.doAfterReturning(joinPoint, annotationOf("add"), AjaxResult.success());

        SysOperLog operLog = awaitRecorded();
        assertThat(operLog.getOperParam()).contains("张三");
        assertThat(operLog.getOperParam()).doesNotContain(SECRET).doesNotContain("password");
    }

    @Test
    void annotationExcludeParamNamesAreMaskedOnTopOfTheBuiltInOnes() throws Exception
    {
        JoinPoint point = mockJoinPoint(new Payload());
        Log log = annotationOf("withExcludedParam");
        aspect.doBefore(point, log);
        aspect.doAfterReturning(point, log, AjaxResult.success());

        assertThat(awaitRecorded().getOperParam()).doesNotContain("bearer-should-be-dropped");
    }

    @Test
    void requestAndResponsePayloadsCanBeSwitchedOffPerEndpoint() throws Exception
    {
        JoinPoint point = mockJoinPoint(new Payload());
        Log log = annotationOf("silent");
        aspect.doBefore(point, log);
        aspect.doAfterReturning(point, log, AjaxResult.success("不该出现"));

        SysOperLog operLog = awaitRecorded();
        assertThat(operLog.getOperParam()).isNull();
        assertThat(operLog.getJsonResult()).isNull();
    }

    @Test
    void failingOperationIsStoredWithTheErrorMessage() throws Exception
    {
        aspect.doBefore(joinPoint, annotationOf("add"));
        aspect.doAfterThrowing(joinPoint, annotationOf("add"), new ServiceException("密码长度不符合要求"));

        SysOperLog operLog = awaitRecorded();
        assertThat(operLog.getStatus()).isEqualTo(BusinessStatus.FAIL.ordinal());
        assertThat(operLog.getErrorMsg()).isEqualTo("密码长度不符合要求");
    }

    @Test
    void anonymousRequestRecordsNothingInsteadOfThrowingOutOfTheController() throws Exception
    {
        SecurityContextHolder.clearContext();

        aspect.doBefore(joinPoint, annotationOf("add"));
        assertThatCode(() -> aspect.doAfterReturning(joinPoint, annotationOf("add"), AjaxResult.success()))
                .doesNotThrowAnyException();

        Thread.sleep(120L);
        assertThat(recorded.get()).isNull();
    }

    @Test
    void queryParametersGoThroughTheSameMaskingAsPostBodies() throws Exception
    {
        request = new MockHttpServletRequest("GET", "/system/user/list");
        request.setRemoteAddr("127.0.0.1");
        request.setParameter("password", SECRET);
        request.setParameter("keyword", "张三");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        aspect.doBefore(joinPoint, annotationOf("export"));
        aspect.doAfterReturning(joinPoint, annotationOf("export"), AjaxResult.success());

        SysOperLog operLog = awaitRecorded();
        assertThat(operLog.getRequestMethod()).isEqualTo("GET");
        assertThat(operLog.getOperParam()).contains("张三");
        assertThat(operLog.getOperParam()).doesNotContain(SECRET);
    }

    @Test
    void oversizedPayloadsAreTruncatedToTheColumnLimit() throws Exception
    {
        Map<String, Object> huge = new HashMap<>();
        huge.put("content", "x".repeat(5000));
        JoinPoint point = mockJoinPoint(huge);
        Log log = annotationOf("add");

        aspect.doBefore(point, log);
        aspect.doAfterReturning(point, log, huge);

        SysOperLog operLog = awaitRecorded();
        assertThat(operLog.getOperParam()).hasSizeLessThanOrEqualTo(2000);
        assertThat(operLog.getJsonResult()).hasSizeLessThanOrEqualTo(2000);
    }

    @Test
    void servletAndUploadArgumentsAreFilteredOutOfThePayload()
    {
        MultipartFile file = new MockMultipartFile("file", "a.png", "image/png", new byte[] { 1 });
        assertThat(aspect.isFilterObject(file)).isTrue();
        assertThat(aspect.isFilterObject(new MultipartFile[] { file })).isTrue();
        List<MultipartFile> fileList = new ArrayList<>();
        fileList.add(file);
        assertThat(aspect.isFilterObject(fileList)).isTrue();
        Map<String, MultipartFile> fileMap = new HashMap<>();
        fileMap.put("file", file);
        assertThat(aspect.isFilterObject(fileMap)).isTrue();

        assertThat(aspect.isFilterObject(request)).isTrue();
        assertThat(aspect.isFilterObject(new BeanPropertyBindingResult(new Payload(), "payload"))).isTrue();

        // 普通值不进过滤器，才轮得到序列化逻辑
        assertThat(aspect.isFilterObject(new Payload())).isFalse();
        assertThat(aspect.isFilterObject(Arrays.asList("only", "strings"))).isFalse();
        assertThat(aspect.isFilterObject(Collections.singletonMap("name", "张三"))).isFalse();
        assertThat(aspect.isFilterObject(new String[] { "a" })).isFalse();
    }

    @Test
    void excludeFilterMergesBuiltInSensitiveNamesWithAnnotationOnes()
    {
        PropertyPreExcludeFilter filter = aspect.excludePropertyPreFilter(new String[] { "token" });

        assertThat(filter.getExcludes()).contains(
                LogAspect.EXCLUDE_PROPERTIES[0], "oldPassword", "newPassword", "confirmPassword", "token");
        assertThat(filter.getExcludes()).doesNotContain("nickname");
    }
}
