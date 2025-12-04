FROM node:22

ARG TZ=Asia/Shanghai
ENV TZ=${TZ}

WORKDIR /app

COPY package*.json ./

RUN npm config set registry https://registry.npmmirror.com \
    && npm install --legacy-peer-deps

COPY . .

EXPOSE 3000

CMD ["npm", "run", "dev"]
