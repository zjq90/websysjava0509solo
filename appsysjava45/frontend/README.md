# 花店APP前端项目

## 安装说明

由于UniApp特定版本可能在npm源上不存在，建议使用以下方式安装：

### 方案一：使用HBuilderX（推荐）
1. 下载安装HBuilderX：https://www.dcloud.io/hbuilderx.html
2. 打开HBuilderX，选择"文件" -> "打开目录"，选择本项目的frontend目录
3. 点击"运行" -> "运行到浏览器" -> "Chrome"

### 方案二：使用npm安装稳定版
如果要使用npm安装，请使用以下配置（已更新到package.json）：

```json
{
  "name": "flower-shop-app",
  "version": "1.0.0",
  "description": "花店APP前端",
  "main": "main.js",
  "scripts": {
    "dev": "uni",
    "build": "uni build"
  },
  "dependencies": {
    "@dcloudio/uni-app": "3.0.0-4020920250520001",
    "@dcloudio/uni-components": "3.0.0-4020920250520001",
    "@dcloudio/uni-h5": "3.0.0-4020920250520001",
    "@dcloudio/uni-mp-weixin": "3.0.0-4020920250520001",
    "vue": "^3.4.0",
    "vuex": "^4.1.0"
  },
  "devDependencies": {
    "@dcloudio/types": "^3.4.8",
    "@dcloudio/uni-automator": "3.0.0-4020920250520001",
    "@dcloudio/uni-cli-shared": "3.0.0-4020920250520001",
    "@dcloudio/vite-plugin-uni": "3.0.0-4020920250520001",
    "@vue/runtime-core": "^3.4.0",
    "vite": "5.2.8"
  }
}
```

## 项目结构

```
frontend/
├── pages/              # 页面文件
│   ├── index/          # 首页
│   ├── product/        # 商品列表、详情
│   ├── cart/           # 购物车
│   ├── order/          # 订单相关
│   ├── user/           # 用户中心、登录、注册
│   ├── coupon/         # 优惠券
│   └── activity/       # 活动
├── store/              # Vuex状态管理
├── utils/              # 工具类
├── static/             # 静态资源
├── App.vue             # 根组件
├── main.js             # 入口文件
├── manifest.json       # 应用配置
├── pages.json          # 路由配置
└── package.json        # 依赖配置
```

## 后端API地址配置

修改 `utils/request.js` 中的 `baseUrl`：

```javascript
const baseUrl = 'http://localhost:8080/api'
```

## 功能说明

1. **首页**：轮播图、分类导航、热门商品、新品推荐、秒杀活动
2. **商品**：商品列表、商品搜索、商品详情
3. **购物车**：添加商品、修改数量、删除商品、结算
4. **订单**：创建订单、订单列表、订单详情、取消订单
5. **用户**：登录、注册、个人中心、优惠券
6. **长辈模式**：字体放大、界面简化

## 注意事项

- 建议使用HBuilderX进行开发和运行
- 确保后端服务已启动
- 如遇到跨域问题，请检查后端CORS配置
