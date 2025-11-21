#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import requests
import json
import time
import random
import threading
from concurrent.futures import ThreadPoolExecutor, as_completed
import statistics

class BlogSystemTest:
    def __init__(self, base_url="http://localhost:8080"):
        self.base_url = base_url
        self.session = requests.Session()
        self.token = None
        self.results = {}
        
    def login(self, username="admin", password="admin123"):
        """登录获取token"""
        try:
            # 禁用验证码（如果启用了的话）
            response = self.session.post(
                f"{self.base_url}/login",
                json={"username": username, "password": password},
                headers={"Content-Type": "application/json"}
            )
            
            if response.status_code == 200:
                data = response.json()
                if data.get('code') == 200:
                    self.token = data.get('token')
                    self.session.headers.update({
                        'Authorization': f'Bearer {self.token}'
                    })
                    return True
            print(f"登录失败: {response.text}")
            return False
        except Exception as e:
            print(f"登录异常: {e}")
            return False
    
    def test_api_list(self):
        """测试API接口列表"""
        apis = [
            ("文章列表", "/system/article/list"),
            ("分类列表", "/system/category/list"), 
            ("标签列表", "/system/tag/list"),
            ("评论列表", "/system/comment/list"),
            ("友链列表", "/system/friendLink/list"),
            ("博客设置", "/system/setting/list"),
        ]
        
        results = {}
        for name, path in apis:
            try:
                start_time = time.time()
                response = self.session.get(f"{self.base_url}{path}")
                end_time = time.time()
                
                results[name] = {
                    'status_code': response.status_code,
                    'response_time': round((end_time - start_time) * 1000, 2),  # ms
                    'success': response.status_code == 200 and response.json().get('code') == 200
                }
            except Exception as e:
                results[name] = {
                    'status_code': 0,
                    'response_time': 0,
                    'success': False,
                    'error': str(e)
                }
        
        return results
    
    def create_test_article(self):
        """创建测试文章"""
        test_data = {
            "title": f"性能测试文章_{int(time.time())}_{random.randint(1000, 9999)}",
            "summary": "这是一篇用于性能测试的文章",
            "content": f"# 性能测试文章\n\n创建时间: {time.strftime('%Y-%m-%d %H:%M:%S')}\n\n测试内容...",
            "categoryId": random.randint(1, 5),
            "status": 1,
            "isTop": 0,
            "isRecommend": random.choice([0, 1]),
            "tagIds": [random.randint(1, 10)]
        }
        
        try:
            start_time = time.time()
            response = self.session.post(
                f"{self.base_url}/system/article",
                json=test_data,
                headers={"Content-Type": "application/json"}
            )
            end_time = time.time()
            
            return {
                'success': response.status_code == 200 and response.json().get('code') == 200,
                'response_time': round((end_time - start_time) * 1000, 2),
                'article_id': response.json().get('data', {}).get('articleId') if response.status_code == 200 else None
            }
        except Exception as e:
            return {
                'success': False,
                'response_time': 0,
                'error': str(e)
            }
    
    def concurrent_test(self, func, num_threads=10, requests_per_thread=5):
        """并发测试"""
        results = []
        with ThreadPoolExecutor(max_workers=num_threads) as executor:
            futures = []
            for _ in range(num_threads * requests_per_thread):
                futures.append(executor.submit(func))
            
            for future in as_completed(futures):
                try:
                    result = future.result()
                    results.append(result)
                except Exception as e:
                    results.append({'success': False, 'error': str(e)})
        
        return results
    
    def performance_test(self):
        """性能测试"""
        print("🚀 开始博客系统性能测试")
        print("=" * 50)
        
        # 1. 登录测试
        print("1. 登录测试...")
        if not self.login():
            print("❌ 登录失败，测试终止")
            return
        print("✅ 登录成功")
        
        # 2. 基础API测试
        print("\n2. 基础API接口测试...")
        api_results = self.test_api_list()
        for name, result in api_results.items():
            status = "✅" if result['success'] else "❌"
            print(f"{status} {name}: {result['response_time']}ms")
        
        # 3. 单次创建文章测试
        print("\n3. 单次创建文章测试...")
        create_result = self.create_test_article()
        if create_result['success']:
            print(f"✅ 创建成功: {create_result['response_time']}ms")
        else:
            print(f"❌ 创建失败: {create_result.get('error', 'Unknown error')}")
        
        # 4. 并发测试 - 创建文章
        print("\n4. 并发创建文章测试 (10线程 x 5次 = 50个请求)...")
        concurrent_results = self.concurrent_test(self.create_test_article, 10, 5)
        
        success_count = sum(1 for r in concurrent_results if r['success'])
        response_times = [r['response_time'] for r in concurrent_results if r['success']]
        
        if response_times:
            print(f"成功率: {success_count}/{len(concurrent_results)} ({success_count/len(concurrent_results)*100:.1f}%)")
            print(f"平均响应时间: {statistics.mean(response_times):.2f}ms")
            print(f"最快响应时间: {min(response_times)}ms")
            print(f"最慢响应时间: {max(response_times)}ms")
            if len(response_times) > 1:
                print(f"响应时间中位数: {statistics.median(response_times):.2f}ms")
                print(f"响应时间标准差: {statistics.stdev(response_times):.2f}ms")
        else:
            print("❌ 并发测试全部失败")
        
        # 5. 高并发读取测试
        print("\n5. 并发读取测试 (20线程 x 10次 = 200个请求)...")
        read_results = self.concurrent_test(
            lambda: self.test_api_list()['文章列表'], 
            20, 10
        )
        
        read_success = sum(1 for r in read_results if r['success'])
        read_times = [r['response_time'] for r in read_results if r['success']]
        
        if read_times:
            print(f"读取成功率: {read_success}/{len(read_results)} ({read_success/len(read_results)*100:.1f}%)")
            print(f"平均读取时间: {statistics.mean(read_times):.2f}ms")
            print(f"最快读取时间: {min(read_times)}ms")
            print(f"最慢读取时间: {max(read_times)}ms")
        
        print("\n" + "=" * 50)
        print("🎯 性能测试完成")
        
        # 保存测试结果
        self.results = {
            'login': 'success',
            'api_tests': api_results,
            'create_test': create_result,
            'concurrent_create': {
                'total': len(concurrent_results),
                'success': success_count,
                'avg_response_time': statistics.mean(response_times) if response_times else 0,
                'min_response_time': min(response_times) if response_times else 0,
                'max_response_time': max(response_times) if response_times else 0
            },
            'concurrent_read': {
                'total': len(read_results),
                'success': read_success,
                'avg_response_time': statistics.mean(read_times) if read_times else 0,
                'min_response_time': min(read_times) if read_times else 0,
                'max_response_time': max(read_times) if read_times else 0
            }
        }
    
    def save_results(self, filename="performance_test_results.json"):
        """保存测试结果"""
        try:
            with open(filename, 'w', encoding='utf-8') as f:
                json.dump(self.results, f, ensure_ascii=False, indent=2)
            print(f"📊 测试结果已保存到: {filename}")
        except Exception as e:
            print(f"❌ 保存测试结果失败: {e}")

if __name__ == "__main__":
    tester = BlogSystemTest()
    tester.performance_test()
    tester.save_results()