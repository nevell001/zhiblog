package com.zhi.framework.config;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import com.zhi.common.core.domain.entity.SysUser;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Redis 的 FastJson 序列化器单元测试。
 *
 * <p>登录会话、博客设置缓存都按这个字节格式落 Redis：类型信息必须写进去（否则读回来是 Map），
 * 空值必须编成 0 字节而不是 {@code "null"} 文本，且反序列化只能命中白名单内的类型。</p>
 */
class FastJson2JsonRedisSerializerTest
{
    private final FastJson2JsonRedisSerializer<SysUser> serializer =
            new FastJson2JsonRedisSerializer<>(SysUser.class);

    @Test
    void roundTripsTheDeclaredTypeThroughTheAutoTypeWhitelist()
    {
        SysUser user = new SysUser();
        user.setUserId(42L);
        user.setUserName("zhangsan");
        user.setStatus("0");

        byte[] bytes = serializer.serialize(user);
        String json = new String(bytes, StandardCharsets.UTF_8);
        assertThat(json).contains("@type").contains(SysUser.class.getName());

        SysUser restored = serializer.deserialize(bytes);
        assertThat(restored).isNotNull().isInstanceOf(SysUser.class);
        assertThat(restored.getUserId()).isEqualTo(42L);
        assertThat(restored.getUserName()).isEqualTo("zhangsan");
    }

    @Test
    void nullValueBecomesAnEmptyPayloadNotTheLiteralNull()
    {
        assertThat(serializer.serialize(null)).isEmpty();
    }

    @Test
    void emptyAndNullPayloadsDeserializeBackToNull()
    {
        assertThat(serializer.deserialize(null)).isNull();
        assertThat(serializer.deserialize(new byte[0])).isNull();
    }

    @Test
    void utf8ContentSurvivesTheWireFormat()
    {
        SysUser user = new SysUser();
        user.setNickName("张三·博客");

        assertThat(serializer.deserialize(serializer.serialize(user)).getNickName()).isEqualTo("张三·博客");
    }
}
