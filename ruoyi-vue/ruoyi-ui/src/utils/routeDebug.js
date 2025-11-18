import router from '@/router'

/**
 * 调试路由注册情况
 * 打印所有已注册的路由，特别关注动态路由
 */
export function debugRoutes() {
  console.log('===== 开始调试路由注册情况 =====')
  const allRoutes = router.getRoutes()
  console.log(`总共注册了 ${allRoutes.length} 个路由`)
  
  // 过滤出blog相关路由
  const blogRoutes = allRoutes.filter(route => 
    route.path.includes('/blog/')
  )
  
  console.log('\n===== 博客相关路由 =====')
  blogRoutes.forEach(route => {
    console.log(`路径: ${route.path}, 名称: ${route.name || '无名称'}`)
    
    // 检查是否有子路由
    if (route.children && route.children.length > 0) {
      route.children.forEach(child => {
        console.log(`  子路由: ${child.path}, 名称: ${child.name || '无名称'}`)
      })
    }
  })
  
  // 特别检查动态路由
  console.log('\n===== 动态路由（包含:）=====')
  const dynamicRoutes = allRoutes.filter(route => route.path.includes(':'))
  dynamicRoutes.forEach(route => {
    console.log(`动态路由: ${route.path}, 名称: ${route.name || '无名称'}`)
  })
  
  console.log('\n===== 路由调试结束 =====')
}