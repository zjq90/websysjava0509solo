# 花店商家端管理系统

基于Spring Boot + Vue + MySQL的花店商家后台管理系统，实现了商品管理、订单处理、客户管理、数据分析、内容管理等核心功能。

## 技术栈

### 后端
- **JDK 8+**
- **Spring Boot 2.7.x**
- **MySQL 8.0**
- **MyBatis Plus**
- **Maven**
- **Swagger/OpenAPI**
- **POI（Excel导入导出）**

### 前端
- **Vue 2.6**
- **Vue Router**
- **Element UI**
- **ECharts**
- **Axios**

## 功能模块

### 1. 商品管理
- ✅ 商品的增删改查
- ✅ 批量导入商品（Excel模板下载）
- ✅ 库存预警（低于阈值自动提醒）
- ✅ 上下架管理
- ✅ 商品分类管理

### 2. 订单处理
- ✅ 订单列表查询
- ✅ 订单审核（接单/拒绝）
- ✅ 制作进度跟踪（备货中→制作中→已发货→已完成）
- ✅ 异常订单处理（退款、补发、协商赔偿）
- ✅ 订单明细查看

### 3. 客户管理
- ✅ 客户信息管理
- ✅ 用户标签管理
- ✅ 批量打标签
- ✅ 精准营销（向特定标签用户推送优惠）

### 4. 数据分析
- ✅ 销售报表（按日/周/月统计）
- ✅ 热销商品排行
- ✅ 销售趋势图表
- ✅ 商品分类销售占比
- ✅ 用户行为分析（浏览量、转化率、复购率可视化）

### 5. 内容管理
- ✅ 公告发布管理（置顶、发布/撤回）
- ✅ 文章管理（养护知识、花语文化等SEO优化内容）
- ✅ 营销活动管理（优惠券、折扣、满减、拼团）

## 项目结构

```
websysjava46/
├── backend/                 # 后端项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/flower/
│   │   │   │   ├── controller/    # 控制器层
│   │   │   │   ├── service/       # 业务逻辑层
│   │   │   │   ├── mapper/        # 数据访问层
│   │   │   │   ├── entity/        # 实体类
│   │   │   │   ├── dto/           # 数据传输对象
│   │   │   │   └── config/        # 配置类
│   │   │   └── resources/
│   │   │       ├── application.yml # 配置文件
│   │   │       └── mapper/         # MyBatis映射文件
│   └── pom.xml
└── frontend/                # 前端项目
    ├── src/
    │   ├── views/           # 页面组件
    │   ├── router/          # 路由配置
    │   ├── App.vue
    │   └── main.js
    ├── public/
    └── package.json
```

## 快速开始

### 后端启动

1. **创建数据库**
```sql
CREATE DATABASE flower_shop CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. **修改数据库配置**
编辑 `backend/src/main/resources/application.yml`
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/flower_shop?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
```

3. **启动项目**
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

4. **访问接口文档**
```
http://localhost:8080/swagger-ui/index.html
```

### 前端启动

1. **安装依赖**
```bash
cd frontend
npm install
```

2. **启动开发服务器**
```bash
npm run serve
```

3. **访问系统**
```
http://localhost:8081
```

## 数据库设计

### 核心数据表
- `product` - 商品表
- `order` - 订单表
- `order_item` - 订单明细表
- `customer` - 客户表
- `announcement` - 公告表
- `article` - 文章表
- `marketing_campaign` - 营销活动表

## 测试数据

系统启动时会自动初始化测试数据，包括：
- 8个测试商品（玫瑰、康乃馨、百合、向日葵等）
- 10个测试订单
- 8个测试客户
- 4篇文章
- 4条公告

## 特色功能

### 1. Excel批量导入
- 提供模板下载
- 支持批量导入商品信息
- 自动解析Excel文件

### 2. 库存预警
- 可设置最低库存阈值
- 库存不足时在首页预警提醒

### 3. ECharts数据可视化
- 销售趋势折线图
- 分类占比饼图
- 热销商品排行柱状图

### 4. 订单状态流转
完整的订单生命周期管理：
```
待审核 → 备货中 → 制作中 → 已发货 → 已完成
                    ↓
                  异常订单 → 退款/补发/赔偿
```

## API接口预览

| 模块 | 接口 | 说明 |
|------|------|------|
| 商品 | GET /api/products | 商品列表 |
| 商品 | POST /api/products/import | 批量导入 |
| 订单 | GET /api/orders/{id} | 订单详情 |
| 订单 | PUT /api/orders/{id}/status | 更新状态 |
| 客户 | GET /api/customers | 客户列表 |
| 数据 | GET /api/analytics/sales-report | 销售报表 |

## 开发说明

### 后端开发
- 统一使用 `Result<T>` 作为返回结果
- 使用MyBatis Plus简化CRUD操作
- 代码注释详细，便于二次开发

### 前端开发
- 使用Element UI组件库，界面美观简洁
- 响应式布局，适配不同屏幕
- 统一的表单验证和提示风格

## 浏览器支持

- Chrome (推荐)
- Firefox
- Safari
- Edge
- IE 10+

## 许可证

MIT License
