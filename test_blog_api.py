#!/usr/bin/env python3
import requests
import json
import base64
import io
from PIL import Image
import time

# 配置
BASE_URL = "http://localhost:8080"
USERNAME = "admin"
PASSWORD = "admin123"

def decode_captcha_simple(img_data):
    """简化版本，直接返回默认验证码"""
    # 在开发环境中，可以尝试使用常见的验证码答案
    # 或者直接查看验证码图片
    with open('/tmp/captcha.png', 'wb') as f:
        img_data_str = img_data.split(',')[1] if ',' in img_data else img_data
        f.write(base64.b64decode(img_data_str))
    print("验证码图片已保存到 /tmp/captcha.png，请查看")
    return input("请输入验证码: ").strip()

def test_blog_apis():
    """测试博客API"""
    session = requests.Session()
    
    # 1. 获取验证码
    print("1. 获取验证码...")
    captcha_resp = session.get(f"{BASE_URL}/captchaImage")
    if captcha_resp.status_code != 200:
        print(f"获取验证码失败: {captcha_resp.status_code}")
        return False
    
    captcha_data = captcha_resp.json()
    print(f"验证码获取成功，状态: {captcha_data.get('code')}")
    
    # 2. 解码验证码（这里用固定值测试，实际应该输入正确的验证码）
    captcha_code = "1234"  # 尝试常见验证码
    uuid = captcha_data.get('uuid')
    
    # 3. 尝试登录
    print("3. 尝试登录系统...")
    login_data = {
        "username": USERNAME,
        "password": PASSWORD,
        "code": captcha_code,
        "uuid": uuid
    }
    
    login_resp = session.post(f"{BASE_URL}/login", json=login_data)
    if login_resp.status_code != 200:
        print(f"登录请求失败: {login_resp.status_code}")
        return False
    
    login_result = login_resp.json()
    
    # 如果验证码失败，尝试手动输入
    if login_result.get('code') != 200:
        print("自动验证码失败，请手动输入验证码...")
        captcha_code = decode_captcha_simple(captcha_data['img'])
        login_data['code'] = captcha_code
        
        login_resp = session.post(f"{BASE_URL}/login", json=login_data)
        login_result = login_resp.json()
        
        if login_result.get('code') != 200:
            print(f"登录失败: {login_result}")
            return False
    
    print("✅ 登录成功!")
    
    # 4. 测试博客管理API
    print("\n4. 测试博客管理API...")
    
    # 获取token
    token = login_result.get('token')
    if token:
        session.headers.update({'Authorization': f'Bearer {token}'})
    
    api_tests = [
        ("文章列表", "/system/article/list", "GET"),
        ("分类列表", "/system/category/list", "GET"),
        ("标签列表", "/system/tag/list", "GET"),
        ("评论列表", "/system/comment/list", "GET"),
        ("友链列表", "/system/friendLink/list", "GET"),
    ]
    
    success_count = 0
    for api_name, api_path, method in api_tests:
        try:
            if method == "GET":
                api_resp = session.get(f"{BASE_URL}{api_path}")
            
            print(f"\n测试 {api_name}:")
            print(f"  状态码: {api_resp.status_code}")
            
            if api_resp.status_code == 200:
                result = api_resp.json()
                if result.get('code') == 200:
                    print(f"  ✅ {api_name} API 成功")
                    if 'rows' in result:
                        print(f"  📊 返回数据条数: {len(result['rows'])}")
                    elif 'data' in result:
                        print(f"  📊 返回数据: {type(result['data']).__name__}")
                    success_count += 1
                else:
                    print(f"  ❌ {api_name} API 返回错误: {result}")
            else:
                print(f"  ❌ {api_name} API HTTP错误: {api_resp.status_code}")
                
        except Exception as e:
            print(f"  ❌ {api_name} API 请求异常: {e}")
    
    print(f"\n=== 测试结果 ===")
    print(f"成功: {success_count}/{len(api_tests)}")
    
    # 5. 测试文章详情API
    print("\n5. 测试文章详情API...")
    try:
        article_resp = session.get(f"{BASE_URL}/system/article/1")
        if article_resp.status_code == 200:
            article_result = article_resp.json()
            if article_result.get('code') == 200:
                print("✅ 文章详情API成功")
                article_data = article_result.get('data', {})
                print(f"  文章标题: {article_data.get('article', {}).get('title', 'N/A')}")
            else:
                print(f"❌ 文章详情API返回错误: {article_result}")
        else:
            print(f"❌ 文章详情API HTTP错误: {article_resp.status_code}")
    except Exception as e:
        print(f"❌ 文章详情API请求异常: {e}")
    
    return success_count == len(api_tests)

if __name__ == "__main__":
    print("=== 博客管理API测试 ===")
    success = test_blog_apis()
    if success:
        print("\n🎉 所有API测试通过!")
    else:
        print("\n⚠️ 部分API测试失败，请检查配置")