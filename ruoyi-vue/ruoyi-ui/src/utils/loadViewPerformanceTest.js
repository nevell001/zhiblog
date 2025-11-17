/**
 * loadView性能测试工具
 * 用于比较重构前后的组件加载性能
 */

import { loadView } from '../store/modules/permission';

export class LoadViewPerformanceTest {
  constructor() {
    this.testResults = [];
  }

  /**
   * 测试单个视图的加载时间
   * @param {string} viewPath - 视图路径
   * @param {number} iterations - 测试次数
   */
  async testSingleView(viewPath, iterations = 5) {
    console.log(`\n开始测试视图: ${viewPath} (${iterations}次)`);
    
    let totalTime = 0;
    let successCount = 0;
    let errorCount = 0;
    
    for (let i = 0; i < iterations; i++) {
      const startTime = performance.now();
      try {
        // 获取异步加载器函数
        const loader = loadView(viewPath);
        // 执行加载（实际项目中可能会有缓存，这里主要测量路径解析和首次尝试时间）
        // 注意：为避免实际组件加载，我们只测量loadView函数本身的执行时间
        // 不实际执行loader()，因为那会加载整个组件
        const endTime = performance.now();
        const duration = endTime - startTime;
        
        console.log(`  迭代 ${i + 1}: ${duration.toFixed(2)}ms`);
        totalTime += duration;
        successCount++;
      } catch (error) {
        console.error(`  迭代 ${i + 1}: 错误`, error);
        errorCount++;
      }
    }
    
    const averageTime = totalTime / successCount;
    const result = {
      viewPath,
      iterations,
      totalTime,
      averageTime,
      successCount,
      errorCount
    };
    
    console.log(`测试结果 - ${viewPath}:`);
    console.log(`  平均时间: ${averageTime.toFixed(2)}ms`);
    console.log(`  成功率: ${(successCount / iterations * 100).toFixed(1)}%`);
    
    this.testResults.push(result);
    return result;
  }

  /**
   * 测试多个视图
   * @param {string[]} viewPaths - 视图路径数组
   */
  async testMultipleViews(viewPaths) {
    console.log('\n开始批量测试多个视图...');
    
    const results = [];
    for (const viewPath of viewPaths) {
      const result = await this.testSingleView(viewPath);
      results.push(result);
    }
    
    this.generateSummary();
    return results;
  }

  /**
   * 生成测试摘要
   */
  generateSummary() {
    console.log('\n===== 性能测试摘要 =====');
    
    if (this.testResults.length === 0) {
      console.log('暂无测试结果');
      return;
    }
    
    let totalAverageTime = 0;
    let totalTests = 0;
    let totalSuccess = 0;
    
    for (const result of this.testResults) {
      console.log(`${result.viewPath}: ${result.averageTime.toFixed(2)}ms (${result.successCount}/${result.iterations})`);
      totalAverageTime += result.averageTime;
      totalTests += result.iterations;
      totalSuccess += result.successCount;
    }
    
    const overallAverage = totalAverageTime / this.testResults.length;
    const overallSuccessRate = (totalSuccess / totalTests) * 100;
    
    console.log('\n总体性能:');
    console.log(`  平均路径解析时间: ${overallAverage.toFixed(2)}ms`);
    console.log(`  总体成功率: ${overallSuccessRate.toFixed(1)}%`);
    console.log(`  注意：此测试仅测量路径解析时间，不包括实际组件加载`);
    console.log('====================');
  }
}

// 测试用例示例
const testViews = [
  'system/user',        // 标准后台管理组件
  'admin/system/role',  // 带admin前缀的后台组件
  'blog/article',       // 前台博客组件
  'dashboard',          // 根级组件
  '404',                // 特殊组件
  'system/dict/data'    // 多层级组件
];

// 执行测试的函数
export async function runLoadViewPerformanceTest() {
  const tester = new LoadViewPerformanceTest();
  await tester.testMultipleViews(testViews);
}

// 如果直接运行此文件（在开发环境中），自动执行测试
if (process.env.NODE_ENV === 'development' && typeof window !== 'undefined') {
  // 延迟执行，确保应用加载完成
  setTimeout(() => {
    runLoadViewPerformanceTest().catch(console.error);
  }, 2000);
}