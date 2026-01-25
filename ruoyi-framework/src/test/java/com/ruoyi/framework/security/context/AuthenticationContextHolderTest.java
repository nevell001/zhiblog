package com.ruoyi.framework.security.context;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 身份验证上下文测试类
 * 
 * @author ruoyi
 */
public class AuthenticationContextHolderTest
{
    @AfterEach
    public void tearDown()
    {
        // 每个测试后清理上下文
        AuthenticationContextHolder.clearContext();
    }

    @Test
    public void testSetContext()
    {
        // 测试设置上下文
        Authentication auth = createMockAuthentication();
        AuthenticationContextHolder.setContext(auth);
        assertNotNull(AuthenticationContextHolder.getContext());
    }

    @Test
    public void testGetContext()
    {
        // 测试获取上下文
        Authentication auth = createMockAuthentication();
        AuthenticationContextHolder.setContext(auth);
        Authentication retrievedAuth = AuthenticationContextHolder.getContext();
        assertEquals(auth, retrievedAuth);
    }

    @Test
    public void testClearContext()
    {
        // 测试清空上下文
        Authentication auth = createMockAuthentication();
        AuthenticationContextHolder.setContext(auth);
        AuthenticationContextHolder.clearContext();
        assertNull(AuthenticationContextHolder.getContext());
    }

    @Test
    public void testGetContextWhenNotSet()
    {
        // 测试未设置上下文时获取
        assertNull(AuthenticationContextHolder.getContext());
    }

    @Test
    public void testMultipleSetAndClear()
    {
        // 测试多次设置和清空
        Authentication auth1 = createMockAuthentication("user1");
        AuthenticationContextHolder.setContext(auth1);
        assertEquals(auth1, AuthenticationContextHolder.getContext());

        Authentication auth2 = createMockAuthentication("user2");
        AuthenticationContextHolder.setContext(auth2);
        assertEquals(auth2, AuthenticationContextHolder.getContext());

        AuthenticationContextHolder.clearContext();
        assertNull(AuthenticationContextHolder.getContext());
    }

    @Test
    public void testSetContextWithNull()
    {
        // 测试设置 null 上下文
        AuthenticationContextHolder.setContext(null);
        assertNull(AuthenticationContextHolder.getContext());
    }

    /**
     * 创建模拟的 Authentication 对象
     */
    private Authentication createMockAuthentication()
    {
        return createMockAuthentication("testuser");
    }

    /**
     * 创建模拟的 Authentication 对象
     */
    private Authentication createMockAuthentication(String username)
    {
        UserDetails userDetails = User.builder()
                .username(username)
                .password("password")
                .authorities(new SimpleGrantedAuthority("ROLE_USER"))
                .build();
        
        return new Authentication() {
            private static final long serialVersionUID = 1L;

            @Override
            public String getName()
            {
                return username;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException
            {
            }

            @Override
            public boolean isAuthenticated()
            {
                return true;
            }

            @Override
            public Object getPrincipal()
            {
                return userDetails;
            }

            @Override
            public Object getCredentials()
            {
                return "password";
            }

            @Override
            public Object getDetails()
            {
                return null;
            }

            @Override
            public java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> getAuthorities()
            {
                return userDetails.getAuthorities();
            }
        };
    }
}