package com.ruoyi.system.domain.vo;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * RouterVo 测试类
 * 
 * @author ruoyi
 */
class RouterVoTest {

    @Test
    void testDefaultConstructor() {
        RouterVo routerVo = new RouterVo();
        assertNull(routerVo.getName());
        assertNull(routerVo.getPath());
        assertFalse(routerVo.getHidden());
        assertNull(routerVo.getRedirect());
        assertNull(routerVo.getComponent());
        assertNull(routerVo.getQuery());
        assertNull(routerVo.getAlwaysShow());
        assertNull(routerVo.getMeta());
        assertNull(routerVo.getChildren());
    }

    @Test
    void testSetName() {
        RouterVo routerVo = new RouterVo();
        routerVo.setName("测试路由");
        assertEquals("测试路由", routerVo.getName());
    }

    @Test
    void testSetPath() {
        RouterVo routerVo = new RouterVo();
        routerVo.setPath("/test/path");
        assertEquals("/test/path", routerVo.getPath());
    }

    @Test
    void testSetHidden() {
        RouterVo routerVo = new RouterVo();
        routerVo.setHidden(true);
        assertTrue(routerVo.getHidden());
        routerVo.setHidden(false);
        assertFalse(routerVo.getHidden());
    }

    @Test
    void testSetRedirect() {
        RouterVo routerVo = new RouterVo();
        routerVo.setRedirect("/redirect/path");
        assertEquals("/redirect/path", routerVo.getRedirect());
    }

    @Test
    void testSetComponent() {
        RouterVo routerVo = new RouterVo();
        routerVo.setComponent("TestComponent");
        assertEquals("TestComponent", routerVo.getComponent());
    }

    @Test
    void testSetQuery() {
        RouterVo routerVo = new RouterVo();
        routerVo.setQuery("id=1&name=test");
        assertEquals("id=1&name=test", routerVo.getQuery());
    }

    @Test
    void testSetAlwaysShow() {
        RouterVo routerVo = new RouterVo();
        routerVo.setAlwaysShow(true);
        assertTrue(routerVo.getAlwaysShow());
        routerVo.setAlwaysShow(false);
        assertFalse(routerVo.getAlwaysShow());
        routerVo.setAlwaysShow(null);
        assertNull(routerVo.getAlwaysShow());
    }

    @Test
    void testSetMeta() {
        RouterVo routerVo = new RouterVo();
        MetaVo metaVo = new MetaVo("测试标题", "test-icon");
        routerVo.setMeta(metaVo);
        assertEquals(metaVo, routerVo.getMeta());
    }

    @Test
    void testSetMetaNull() {
        RouterVo routerVo = new RouterVo();
        routerVo.setMeta(null);
        assertNull(routerVo.getMeta());
    }

    @Test
    void testSetChildren() {
        RouterVo routerVo = new RouterVo();
        List<RouterVo> children = new ArrayList<>();
        RouterVo child1 = new RouterVo();
        child1.setName("子路由1");
        RouterVo child2 = new RouterVo();
        child2.setName("子路由2");
        children.add(child1);
        children.add(child2);
        routerVo.setChildren(children);
        assertEquals(2, routerVo.getChildren().size());
        assertEquals("子路由1", routerVo.getChildren().get(0).getName());
        assertEquals("子路由2", routerVo.getChildren().get(1).getName());
    }

    @Test
    void testSetChildrenNull() {
        RouterVo routerVo = new RouterVo();
        routerVo.setChildren(null);
        assertNull(routerVo.getChildren());
    }

    @Test
    void testSetEmptyChildren() {
        RouterVo routerVo = new RouterVo();
        routerVo.setChildren(new ArrayList<>());
        assertNotNull(routerVo.getChildren());
        assertTrue(routerVo.getChildren().isEmpty());
    }

    @Test
    void testChainedSetters() {
        RouterVo routerVo = new RouterVo();
        routerVo.setName("测试路由");
        routerVo.setPath("/test/path");
        routerVo.setHidden(true);
        routerVo.setRedirect("/redirect");
        routerVo.setComponent("TestComponent");
        routerVo.setQuery("id=1");
        routerVo.setAlwaysShow(true);

        assertEquals("测试路由", routerVo.getName());
        assertEquals("/test/path", routerVo.getPath());
        assertTrue(routerVo.getHidden());
        assertEquals("/redirect", routerVo.getRedirect());
        assertEquals("TestComponent", routerVo.getComponent());
        assertEquals("id=1", routerVo.getQuery());
        assertTrue(routerVo.getAlwaysShow());
    }

    @Test
    void testNestedRouterStructure() {
        RouterVo parent = new RouterVo();
        parent.setName("父路由");
        parent.setPath("/parent");

        List<RouterVo> children = new ArrayList<>();
        RouterVo child1 = new RouterVo();
        child1.setName("子路由1");
        child1.setPath("/parent/child1");
        RouterVo child2 = new RouterVo();
        child2.setName("子路由2");
        child2.setPath("/parent/child2");

        children.add(child1);
        children.add(child2);
        parent.setChildren(children);

        assertEquals(2, parent.getChildren().size());
        assertEquals("子路由1", parent.getChildren().get(0).getName());
        assertEquals("/parent/child1", parent.getChildren().get(0).getPath());
        assertEquals("子路由2", parent.getChildren().get(1).getName());
        assertEquals("/parent/child2", parent.getChildren().get(1).getPath());
    }

    @Test
    void testRouterWithMeta() {
        RouterVo routerVo = new RouterVo();
        routerVo.setName("带Meta的路由");
        routerVo.setPath("/meta-route");

        MetaVo metaVo = new MetaVo("路由标题", "route-icon", true, "http://example.com");
        routerVo.setMeta(metaVo);

        assertEquals("路由标题", routerVo.getMeta().getTitle());
        assertEquals("route-icon", routerVo.getMeta().getIcon());
        assertTrue(routerVo.getMeta().isNoCache());
        assertEquals("http://example.com", routerVo.getMeta().getLink());
    }
}