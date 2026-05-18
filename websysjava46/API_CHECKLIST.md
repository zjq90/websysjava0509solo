# 📋 API调用检查清单

## 一、前端代理配置验证

### vue.config.js 代理规则
```javascript
devServer: {
  port: 8081,
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true,
      pathRewrite: {
        '^/api': ''  // 移除/api前缀
      }
    }
  }
}
```

**验证要点：**
- ✅ 所有API请求以 `/api` 开头会被代理到 `http://localhost:8080`
- ✅ 开发环境请求：`/api/products` → `http://localhost:8080/api/products`
- ✅ 生产环境请求直接使用 `http://localhost:8080/api/...`

---

## 二、商品管理API检查清单

| 功能 | 前端调用 | 后端端点 | 状态 |
|------|---------|----------|------|
| 分页查询 | `GET /api/products?page=0&size=10` | `GET /api/products` | ✅ |
| 单个查询 | `GET /api/products/{id}` | `GET /api/products/{id}` | ✅ |
| 新增商品 | `POST /api/products` | `POST /api/products` | ✅ |
| 更新商品 | `PUT /api/products/{id}` | `PUT /api/products/{id}` | ✅ |
| 删除商品 | `DELETE /api/products/{id}` | `DELETE /api/products/{id}` | ✅ |
| 上下架 | `PUT /api/products/{id}/status?status=1` | `PUT /api/products/{id}/status` | ✅ |
| 库存预警 | `GET /api/products/warning` | `GET /api/products/warning` | ✅ |
| 下载模板 | `GET /api/products/template` | `GET /api/products/template` | ✅ |
| 批量导入 | `POST /api/products/import` | `POST /api/products/import` | ✅ |

**关键修复：**
- el-upload action 使用动态计算的 `uploadUrl`
- 开发环境：`/api/products/import` → 通过代理转发
- 生产环境：`http://localhost:8080/api/products/import` → 直接请求

---

## 三、订单管理API检查清单

| 功能 | 前端调用 | 后端端点 | 状态 |
|------|---------|----------|------|
| 分页查询 | `GET /api/orders?page=0&size=10` | `GET /api/orders` | ✅ |
| 单个查询 | `GET /api/orders/{id}` | `GET /api/orders/{id}` | ✅ |
| 按编号查询 | `GET /api/orders/no/{orderNo}` | `GET /api/orders/no/{orderNo}` | ✅ |
| 创建订单 | `POST /api/orders` | `POST /api/orders` | ✅ |
| 接单审核 | `PUT /api/orders/{id}/accept` | `PUT /api/orders/{id}/accept` | ✅ |
| 更新状态 | `PUT /api/orders/{id}/status?status=2` | `PUT /api/orders/{id}/status` | ✅ |
| 订单明细 | `GET /api/orders/{id}/items` | `GET /api/orders/{id}/items` | ✅ |
| 创建异常 | `POST /api/orders/{id}/abnormal` | `POST /api/orders/{id}/abnormal` | ✅ |
| 处理异常 | `PUT /api/orders/abnormal/{id}/handle` | `PUT /api/orders/abnormal/{id}/handle` | ✅ |
| 异常列表 | `GET /api/orders/abnormal?status=0` | `GET /api/orders/abnormal` | ✅ |

---

## 四、客户管理API检查清单

| 功能 | 前端调用 | 后端端点 | 状态 |
|------|---------|----------|------|
| 分页查询 | `GET /api/customers?page=0&size=10` | `GET /api/customers` | ✅ |
| 单个查询 | `GET /api/customers/{id}` | `GET /api/customers/{id}` | ✅ |
| 新增客户 | `POST /api/customers` | `POST /api/customers` | ✅ |
| 更新客户 | `PUT /api/customers/{id}` | `PUT /api/customers/{id}` | ✅ |
| 删除客户 | `DELETE /api/customers/{id}` | `DELETE /api/customers/{id}` | ✅ |
| 添加标签 | `POST /api/customers/{id}/tags?tag=VIP` | `POST /api/customers/{id}/tags` | ✅ |
| 移除标签 | `DELETE /api/customers/{id}/tags?tag=VIP` | `DELETE /api/customers/{id}/tags` | ✅ |

---

## 五、数据分析API检查清单

| 功能 | 前端调用 | 后端端点 | 状态 |
|------|---------|----------|------|
| 销售报表 | `GET /api/analytics/sales-report?type=day` | `GET /api/analytics/sales-report` | ✅ |
| 销售趋势 | `GET /api/analytics/sales-trend?type=day` | `GET /api/analytics/sales-trend` | ✅ |
| 热销商品 | `GET /api/analytics/hot-products?limit=10` | `GET /api/analytics/hot-products` | ✅ |
| 分类统计 | `GET /api/analytics/category-stats` | `GET /api/analytics/category-stats` | ✅ |
| 用户行为 | `GET /api/analytics/user-behavior` | `GET /api/analytics/user-behavior` | ✅ |

---

## 六、内容管理API检查清单

| 功能 | 前端调用 | 后端端点 | 状态 |
|------|---------|----------|------|
| 公告列表 | `GET /api/announcements?page=0&size=10` | `GET /api/announcements` | ✅ |
| 公告详情 | `GET /api/announcements/{id}` | `GET /api/announcements/{id}` | ✅ |
| 发布公告 | `POST /api/announcements` | `POST /api/announcements` | ✅ |
| 更新公告 | `PUT /api/announcements/{id}` | `PUT /api/announcements/{id}` | ✅ |
| 删除公告 | `DELETE /api/announcements/{id}` | `DELETE /api/announcements/{id}` | ✅ |
| 置顶切换 | `PUT /api/announcements/{id}/top` | `PUT /api/announcements/{id}/top` | ✅ |
| 文章列表 | `GET /api/articles?page=0&size=10` | `GET /api/articles` | ✅ |
| 文章详情 | `GET /api/articles/{id}` | `GET /api/articles/{id}` | ✅ |
| 发布文章 | `POST /api/articles` | `POST /api/articles` | ✅ |
| 更新文章 | `PUT /api/articles/{id}` | `PUT /api/articles/{id}` | ✅ |
| 删除文章 | `DELETE /api/articles/{id}` | `DELETE /api/articles/{id}` | ✅ |
| 增加浏览 | `PUT /api/articles/{id}/view` | `PUT /api/articles/{id}/view` | ✅ |

---

## 七、系统API检查清单

| 功能 | 前端调用 | 后端端点 | 状态 |
|------|---------|----------|------|
| 健康检查 | `GET /api/system/health` | `GET /api/system/health` | ✅ |

---

## 八、常见问题排查指南

### 1. 404 Not Found
**可能原因：**
- 请求URL不正确
- 后端服务未启动
- 代理配置不生效

**排查步骤：**
1. 检查浏览器Network面板，确认请求URL
2. 确认后端服务已启动（访问 `http://localhost:8080/swagger-ui/index.html`）
3. 重启前端开发服务器

### 2. CORS跨域错误
**已配置：**
- 后端 `CorsConfig.java` 已启用跨域
- 前端开发服务器已配置代理

**排查步骤：**
1. 确认后端 `@CrossOrigin` 注解已添加
2. 确认前端请求使用相对路径 `/api/...`

### 3. Swagger页面空白
**已修复：**
- 添加了 `WebMvcConfig.java` 静态资源映射
- 正确URL：`http://localhost:8080/swagger-ui/index.html`

### 4. 文件上传失败
**检查点：**
- 文件大小是否超过限制（后端配置10MB）
- 文件格式是否正确（.xlsx, .xls）
- 后端MultipartFile配置是否正确

---

## 九、测试步骤建议

### 第一步：启动后端服务
1. 启动Spring Boot应用
2. 验证Swagger：`http://localhost:8080/swagger-ui/index.html`
3. 验证H2控制台：`http://localhost:8080/h2-console`

### 第二步：启动前端服务
1. `npm install`（首次）
2. `npm run serve`
3. 访问：`http://localhost:8081`

### 第三步：功能测试
1. ✅ 商品管理 - 查看列表
2. ✅ 商品管理 - 下载Excel模板
3. ✅ 商品管理 - 批量导入（使用刚下载的模板）
4. ✅ 商品管理 - 上下架切换
5. ✅ 订单管理 - 查看列表
6. ✅ 订单管理 - 接单/更新状态
7. ✅ 数据看板 - 图表渲染

---

**所有API端点已对齐 ✅**