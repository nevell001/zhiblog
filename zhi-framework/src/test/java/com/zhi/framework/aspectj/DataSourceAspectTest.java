package com.zhi.framework.aspectj;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.zhi.common.annotation.DataSource;
import com.zhi.common.enums.DataSourceType;
import com.zhi.framework.datasource.DynamicDataSourceContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

/**
 * 多数据源切换切面单元测试。
 *
 * <p>路由只在方法执行期间生效、执行结束必须清干净，否则线程复用后会把下一次查询打到从库上。
 * 优先级口径是"方法注解盖类注解"。</p>
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DataSourceAspectTest
{
    @DataSource(DataSourceType.SLAVE)
    static class SlaveAnnotated
    {
        @DataSource(DataSourceType.MASTER)
        public void overriddenByMethod()
        {
        }

        public void inheritsClassLevel()
        {
        }
    }

    static class Unannotated
    {
        public void plain()
        {
        }
    }

    @Mock
    private ProceedingJoinPoint point;

    @Mock
    private MethodSignature signature;

    private DataSourceAspect aspect;

    @BeforeEach
    void setUp()
    {
        aspect = new DataSourceAspect();
        when(point.getSignature()).thenReturn(signature);
        DynamicDataSourceContextHolder.clearDataSourceType();
    }

    @AfterEach
    void tearDown()
    {
        DynamicDataSourceContextHolder.clearDataSourceType();
    }

    /** 让切面在方法体内可见的取值成为返回值，从而断言"执行期间"的上下文 */
    private void target(Class<?> declaringType, String method) throws Throwable
    {
        Method target = declaringType.getDeclaredMethod(method);
        when(signature.getMethod()).thenReturn(target);
        when(signature.getDeclaringType()).thenReturn(declaringType);
        when(point.proceed()).thenAnswer(invocation -> DynamicDataSourceContextHolder.getDataSourceType());
    }

    @Test
    void methodLevelAnnotationWinsOverTheClassLevelOne() throws Throwable
    {
        target(SlaveAnnotated.class, "overriddenByMethod");

        assertThat(aspect.around(point)).isEqualTo("MASTER");
    }

    @Test
    void classLevelAnnotationAppliesToAnnotatedMethodsOnlyViaTypeLookup() throws Throwable
    {
        target(SlaveAnnotated.class, "inheritsClassLevel");

        assertThat(aspect.around(point)).isEqualTo("SLAVE");
    }

    @Test
    void contextIsClearedAfterTheCallEvenForUnannotatedTargets() throws Throwable
    {
        target(Unannotated.class, "plain");
        when(point.proceed()).thenReturn("ok");

        assertThat(aspect.around(point)).isEqualTo("ok");
        assertThat(DynamicDataSourceContextHolder.getDataSourceType()).isNull();
    }

    @Test
    void contextIsClearedWhenTheTargetThrows() throws Throwable
    {
        target(SlaveAnnotated.class, "inheritsClassLevel");
        when(point.proceed()).thenThrow(new IllegalStateException("sql failed"));

        assertThatThrownBy(() -> aspect.around(point)).isInstanceOf(IllegalStateException.class);
        assertThat(DynamicDataSourceContextHolder.getDataSourceType()).isNull();
    }

    @Test
    void holderIsThreadLocalSoOneRequestCannotLeakIntoAnother() throws Exception
    {
        DynamicDataSourceContextHolder.setDataSourceType("SLAVE");

        boolean[] sawOwnCopy = new boolean[1];
        Thread other = new Thread(() -> sawOwnCopy[0] = DynamicDataSourceContextHolder.getDataSourceType() == null);
        other.start();
        other.join(2000L);

        assertThat(sawOwnCopy[0]).isTrue();
        assertThat(DynamicDataSourceContextHolder.getDataSourceType()).isEqualTo("SLAVE");

        DynamicDataSourceContextHolder.clearDataSourceType();
        assertThat(DynamicDataSourceContextHolder.getDataSourceType()).isNull();
    }
}
