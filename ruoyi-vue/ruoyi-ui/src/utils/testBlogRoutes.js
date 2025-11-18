import router from '@/router'

/**
 * 直接测试博客相关动态路由的匹配情况
 */
export function testBlogRoutes() {
  console.log('===== 开始测试博客动态路由匹配 =====')
  
  // 测试的路径列表
  const testPaths = [
    '/blog/tag/1',
    '/blog/tag/6',
    '/blog/tag/10',
    '/blog/category/1',
    '/blog/category/7',
    '/blog/article/1',
    '/blog/article/100'
  ]
  
  testPaths.forEach(path => {
    try {
      // 使用Vue Router v4的正确API
      const matched = router.resolve(path)
      const isMatched = matched.matched.length > 0
      const matchedRoute = isMatched ? matched.matched[matched.matched.length - 1].path : '无匹配'
      
      console.log(`路径: ${path} - 匹配结果: ${isMatched ? '成功' : '失败'}`)
      if (isMatched) {
        console.log(`  匹配到的路由: ${matchedRoute}`)
        console.log(`  路由名称: ${matched.name || '无名称'}`)
      }
    } catch (error) {
      console.error(`路径: ${path} - 匹配错误:`, error)
    }
  })
  
  console.log('\n===== 测试完成 =====')
}