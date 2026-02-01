package com.ruoyi.common.utils.uuid;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * IdUtils ID生成器工具类测试
 *
 * @author ruoyi
 */
public class IdUtilsTest {

    @Test
    public void testRandomUUID() {
        // 测试随机UUID生成
        String uuid = IdUtils.randomUUID();
        assertNotNull(uuid);
        assertFalse(uuid.isEmpty());
        assertEquals(36, uuid.length()); // 标准UUID格式：8-4-4-4-12
        assertTrue(uuid.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"));
    }

    @Test
    public void testSimpleUUID() {
        // 测试简化UUID生成（无横线）
        String uuid = IdUtils.simpleUUID();
        assertNotNull(uuid);
        assertFalse(uuid.isEmpty());
        assertEquals(32, uuid.length()); // 无横线的UUID格式
        assertFalse(uuid.contains("-"));
        assertTrue(uuid.matches("[0-9a-f]{32}"));
    }

    @Test
    public void testFastUUID() {
        // 测试快速UUID生成
        String uuid = IdUtils.fastUUID();
        assertNotNull(uuid);
        assertFalse(uuid.isEmpty());
        assertEquals(36, uuid.length());
        assertTrue(uuid.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"));
    }

    @Test
    public void testFastSimpleUUID() {
        // 测试快速简化UUID生成
        String uuid = IdUtils.fastSimpleUUID();
        assertNotNull(uuid);
        assertFalse(uuid.isEmpty());
        assertEquals(32, uuid.length());
        assertFalse(uuid.contains("-"));
        assertTrue(uuid.matches("[0-9a-f]{32}"));
    }

    @Test
    public void testRandomUUIDUniqueness() {
        // 测试UUID唯一性
        Set<String> uuids = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            String uuid = IdUtils.randomUUID();
            assertTrue(uuids.add(uuid), "UUID should be unique");
        }
        assertEquals(100, uuids.size());
    }

    @Test
    public void testSimpleUUIDUniqueness() {
        // 测试简化UUID唯一性
        Set<String> uuids = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            String uuid = IdUtils.simpleUUID();
            assertTrue(uuids.add(uuid), "Simple UUID should be unique");
        }
        assertEquals(100, uuids.size());
    }

    @Test
    public void testFastUUIDUniqueness() {
        // 测试快速UUID唯一性
        Set<String> uuids = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            String uuid = IdUtils.fastUUID();
            assertTrue(uuids.add(uuid), "Fast UUID should be unique");
        }
        assertEquals(100, uuids.size());
    }

    @Test
    public void testFastSimpleUUIDUniqueness() {
        // 测试快速简化UUID唯一性
        Set<String> uuids = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            String uuid = IdUtils.fastSimpleUUID();
            assertTrue(uuids.add(uuid), "Fast simple UUID should be unique");
        }
        assertEquals(100, uuids.size());
    }

    @Test
    public void testRandomUUIDFormat() {
        // 测试随机UUID格式
        String uuid = IdUtils.randomUUID();
        String[] parts = uuid.split("-");
        assertEquals(5, parts.length);
        assertEquals(8, parts[0].length());
        assertEquals(4, parts[1].length());
        assertEquals(4, parts[2].length());
        assertEquals(4, parts[3].length());
        assertEquals(12, parts[4].length());
    }

    @Test
    public void testConsistency() {
        // 测试一致性（每次调用应该返回不同的值）
        String uuid1 = IdUtils.randomUUID();
        String uuid2 = IdUtils.randomUUID();
        assertNotEquals(uuid1, uuid2);
    }

    @Test
    public void testFastVsRegular() {
        // 测试快速UUID和普通UUID格式相同
        String fast = IdUtils.fastUUID();
        String regular = IdUtils.randomUUID();
        assertEquals(fast.length(), regular.length());
        assertEquals(fast.chars().filter(ch -> ch == '-').count(), 
                    regular.chars().filter(ch -> ch == '-').count());
    }

    @Test
    public void testSimpleVsRegular() {
        // 测试简化UUID和普通UUID
        String simple = IdUtils.simpleUUID();
        String regular = IdUtils.randomUUID();
        assertEquals(32, simple.length());
        assertEquals(36, regular.length());
        assertFalse(simple.contains("-"));
        assertTrue(regular.contains("-"));
    }

    @Test
    public void testFastSimpleVsSimple() {
        // 测试快速简化UUID和简化UUID
        String fastSimple = IdUtils.fastSimpleUUID();
        String simple = IdUtils.simpleUUID();
        assertEquals(fastSimple.length(), simple.length());
        assertEquals(32, fastSimple.length());
        assertFalse(fastSimple.contains("-"));
        assertFalse(simple.contains("-"));
    }

    @Test
    public void testUUIDPerformance() {
        // 测试UUID生成性能（快速方法应该更快）
        long startTime = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            IdUtils.randomUUID();
        }
        long regularTime = System.nanoTime() - startTime;

        startTime = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            IdUtils.fastUUID();
        }
        long fastTime = System.nanoTime() - startTime;

        // 快速方法应该更快或至少不会慢太多
        assertTrue(fastTime <= regularTime * 2, "Fast UUID should be faster");
    }

    @Test
    public void testAllMethodsReturnValidUUID() {
        // 测试所有方法返回有效的UUID
        String random = IdUtils.randomUUID();
        String simple = IdUtils.simpleUUID();
        String fast = IdUtils.fastUUID();
        String fastSimple = IdUtils.fastSimpleUUID();

        assertNotNull(random);
        assertNotNull(simple);
        assertNotNull(fast);
        assertNotNull(fastSimple);

        assertFalse(random.isEmpty());
        assertFalse(simple.isEmpty());
        assertFalse(fast.isEmpty());
        assertFalse(fastSimple.isEmpty());
    }

    @Test
    public void testUUIDCase() {
        // 测试UUID是小写的
        String uuid = IdUtils.randomUUID();
        assertEquals(uuid, uuid.toLowerCase());
    }
}