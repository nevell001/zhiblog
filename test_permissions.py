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

def decode_captcha(img_data):
    """解码验证码"""
    try:
        # 解码base64图片数据
        img_data = img_data.split(',')[1]  # 去掉data:image/png;base64,前缀
        img_bytes = base64.b64decode(img_data)
        
        # 保存图片文件
        with open('/tmp/captcha.png', 'wb') as f:
            f.write(img_bytes)
        
        print("验证码已保存到 /tmp/captcha.png，请查看图片并输入验证码")
        return input("请输入验证码: ").strip()
    except Exception as e:
        print(f"解码验证码失败: {e}")
        return "1234"  # 默认值

def test_blog_permissions():
    """测试博客管理权限"""
    session = requests.Session()
    
    # 1. 获取验证码
    print("1. 获取验证码...")
    captcha_resp = session.get(f"{BASE_URL}/captchaImage")
    if captcha_resp.status_code != 200:
        print(f"获取验证码失败: {captcha_resp.status_code}")
        return
    
    captcha_data = captcha_resp.json()
    print(f"验证码获取成功，UUID: {captcha_data.get('uuid')}")
    
    # 2. 解码验证码
    captcha_code = decode_captcha(captcha_data['img'])
    uuid = captcha_data['uuid']
    
    # 3. 登录
    print("3. 登录系统...")
    login_data = {
        "username": USERNAME,
        "password": PASSWORD,
        "code": captcha_code,
        "uuid": uuid
    }
    
    login_resp = session.post(f"{BASE_URL}/login", json=login_data)
    if login_resp.status_code != 200:
        print(f"登录失败: {login_resp.status_code} - {login_resp.text}")
        return
    
    login_result = login_resp.json()
    if login_result.get('code') != 200:
        print(f"登录失败: {login_result}")
        return
    
    print("登录成功!")
    
    # 4. 获取用户路由信息
    print("4. 获取用户路由...")
    routes_resp = session.get(f"{BASE_URL}/getRouters")
    if routes_resp.status_code != 200:
        print(f"获取路由失败: {routes_resp.status_code}")
        return
    
    routes_data = routes_resp.json()
    if routes_data.get('code') != 200:
        print(f"获取路由失败: {routes_data}")
        return
    
    routes = routes_data.get('data', [])
    print(f"路由数量: {len(routes)}")
    
    # 查找博客管理路由
    blog_menu = None
    for route in routes:
        if route.get('name') == '博客管理' or route.get('path') == 'admin/blog':
            blog_menu = route
            break
    
    if blog_menu:
        print("✅ 博客管理菜单权限已配置")
        print(f"菜单信息: {json.dumps(blog_menu, ensure_ascii=False, indent=2)}")
        
        # 检查子菜单
        children = blog_menu.get('children', [])
        print(f"子菜单数量: {len(children)}")
        for child in children:
            print(f"  - {child.get('name')}: {child.get('path')}")
    else:
        print("❌ 未找到博客管理菜单")
        print("所有菜单:")
        for route in routes:
            print(f"  - {route.get('name')}: {route.get('path')}")
    
    # 5. 测试博客API权限
    print("\n5. 测试博客API权限...")
    
    # 测试文章列表API
    api_tests = [
        ("文章列表", "/system/article/list"),
        ("分类列表", "/system/category/list"),
        ("标签列表", "/system/tag/list"),
    ]
    
    for api_name, api_path in api_tests:
        try:
            api_resp = session.get(f"{BASE_URL}{api_path}")
            if api_resp.status_code == 200:
                result = api_resp.json()
                if result.get('code') == 200:
                    print(f"✅ {api_name} API 权限正常")
                else:
                    print(f"❌ {api_name} API 返回错误: {result}")
            else:
                print(f"❌ {api_name} API HTTP错误: {api_resp.status_code}")
        except Exception as e:
            print(f"❌ {api_name} API 请求异常: {e}")

if __name__ == "__main__":
    test_blog_permissions()