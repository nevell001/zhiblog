// 测试博客文章API的脚本
// 请在浏览器开发者工具控制台中运行

const BASE_URL = 'http://localhost:8080';

// 测试获取文章列表
async function testArticleList() {
    console.log('=== 测试获取文章列表 ===');
    try {
        const response = await fetch(`${BASE_URL}/system/article/list`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        });

        const result = await response.json();
        console.log('文章列表响应:', result);

        if (result.code === 200 && result.rows && result.rows.length > 0) {
            console.log('找到文章，第一个文章:', result.rows[0]);
            return result.rows[0].id;
        } else {
            console.log('未找到文章或响应异常');
            return null;
        }
    } catch (error) {
        console.error('获取文章列表失败:', error);
        return null;
    }
}

// 测试获取单个文章详情
async function testArticleDetail(articleId) {
    console.log(`=== 测试获取文章详情 (ID: ${articleId}) ===`);
    try {
        const response = await fetch(`${BASE_URL}/system/article/${articleId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        });

        const result = await response.json();
        console.log('文章详情响应:', result);

        if (result.code === 200) {
            console.log('文章数据详情:', result.data);
            console.log('标题:', result.data?.title);
            console.log('内容长度:', result.data?.content?.length);
            console.log('作者:', result.data?.authorName);
            console.log('状态:', result.data?.status);
        } else {
            console.log('获取文章详情失败:', result.msg);
        }
    } catch (error) {
        console.error('获取文章详情失败:', error);
    }
}

// 运行测试
async function runTests() {
    const articleId = await testArticleList();
    if (articleId) {
        await testArticleDetail(articleId);
    }
}

// 如果需要带token的测试
async function testWithToken() {
    console.log('=== 带Token的测试 ===');
    // 先获取token
    try {
        const loginResponse = await fetch(`${BASE_URL}/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                username: 'admin',
                password: 'admin123'
            })
        });

        const loginResult = await loginResponse.json();
        console.log('登录响应:', loginResult);

        if (loginResult.code === 200 && loginResult.token) {
            const token = loginResult.token;
            console.log('获取到Token:', token);

            // 使用token获取文章列表
            const listResponse = await fetch(`${BASE_URL}/system/article/list`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                }
            });

            const listResult = await listResponse.json();
            console.log('带Token的文章列表:', listResult);

            if (listResult.code === 200 && listResult.rows && listResult.rows.length > 0) {
                const firstArticle = listResult.rows[0];
                console.log('测试获取文章详情...');

                const detailResponse = await fetch(`${BASE_URL}/system/article/${firstArticle.id}`, {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json',
                    }
                });

                const detailResult = await detailResponse.json();
                console.log('带Token的文章详情:', detailResult);
            }
        }
    } catch (error) {
        console.error('带Token测试失败:', error);
    }
}

// 自动运行测试
console.log('开始API调试测试...');
testWithToken();
// runTests(); // 也可以运行不带token的测试