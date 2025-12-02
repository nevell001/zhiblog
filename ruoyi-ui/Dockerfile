FROM node:22

WORKDIR /app

# 复制package文件
COPY package*.json ./

# 安装依赖
RUN npm install

# 复制源代码
COPY . .

EXPOSE 3000

# 启动开发服务器
CMD ["npm", "run", "dev"]