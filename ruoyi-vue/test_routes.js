// 测试后端返回的路由数据
import axios from 'axios';

// 配置axios
const instance = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 10000
});

// 获取路由数据
instance.get('/getRouters')
  .then(response => {
    console.log('后端返回的路由数据:', response.data);
    if (response.data && response.data.code === 200) {
      console.log('路由数据:', JSON.stringify(response.data.data, null, 2));
    } else {
      console.error('获取路由数据失败:', response.data);
    }
  })
  .catch(error => {
    console.error('请求失败:', error);
  });