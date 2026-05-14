# 影楼Web平台管理系统

## 项目简介

这是一个功能完整的影楼管理系统，包含订单管理、客户管理、套餐管理、云相册管理、修图任务分配、产品设计排版等功能模块。

## 技术栈

### 后端
- JDK 8
- Spring Boot 2.7.18
- H2 嵌入式数据库
- Spring Data JPA
- SpringDoc OpenAPI (Swagger)
- Maven

### 前端
- Vue 3
- Element Plus
- Vue Router
- Axios
- ECharts
- Vite

## 功能特性

### 1. 订单创建与电子合同
- 支持套餐选择与加购项配置
- 自动计算订单金额
- 在线签署电子合同
- 支持微信/短信发送通知
- 客户可远程确认订单

### 2. 订单状态追踪
- 可视化进度条展示
- 6个阶段：定金 → 拍摄 → 选片 → 修图 → 产品制作 → 交付
- 实时更新各环节负责人与完成时间

### 3. 多渠道订单同步
- 支持美团、抖音团购、小程序订单同步
- 自动抓取线上订单与客户信息
- 避免重复录入

### 4. 云相册与在线选片
- 拍摄后自动上传原片至加密云相册
- 客户通过链接或App在线浏览
- 支持标记"喜欢""待修""删除"
- 系统自动统计选片数量并计算费用

### 5. 修图任务分配
- 根据修图师专长自动派单
- 支持外修人员远程接单
- 修图进度实时同步
- 客户可在线确认初修稿与精修稿

### 6. 产品设计与排版
- 提供多种相册、相框、海报排版模板
- 支持自定义设计
- 智能排版功能自动识别人物位置

## 项目结构

```
websysjava38/
├── src/
│   └── main/
│       ├── java/com/photostudio/
│       │   ├── PhotoStudioApplication.java    # 启动类
│       │   ├── config/                          # 配置类
│       │   │   ├── SwaggerConfig.java          # Swagger配置
│       │   │   └── DataInitializer.java        # 数据初始化
│       │   ├── common/                          # 通用类
│       │   │   └── Result.java                  # 统一响应结果
│       │   ├── entity/                          # 实体类
│       │   │   ├── Customer.java
│       │   │   ├── Order.java
│       │   │   ├── Package.java
│       │   │   ├── AddOnItem.java
│       │   │   ├── Album.java
│       │   │   ├── Photo.java
│       │   │   └── Employee.java
│       │   ├── repository/                      # 数据访问层
│       │   ├── service/                         # 业务逻辑层
│       │   └── controller/                      # 控制层
│       └── resources/
│           └── application.yml                  # 配置文件
├── frontend/                                     # 前端项目
│   ├── src/
│   │   ├── views/                               # 页面组件
│   │   │   ├── Layout.vue
│   │   │   ├── Dashboard.vue
│   │   │   ├── Orders.vue
│   │   │   ├── Customers.vue
│   │   │   ├── Packages.vue
│   │   │   ├── Albums.vue
│   │   │   └── Employees.vue
│   │   ├── router/                              # 路由配置
│   │   ├── utils/                               # 工具类
│   │   ├── App.vue
│   │   └── main.js
│   ├── index.html
│   ├── package.json
│   └── vite.config.js
├── pom.xml
└── README.md
```

## 快速开始

### 后端启动

1. 确保已安装 JDK 8 和 Maven

2. 在项目根目录执行：
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

3. 后端服务启动后，访问以下地址：
   - 后端API: http://localhost:8080/api/
   - Swagger文档: http://localhost:8080/api/swagger-ui.html
   - H2数据库控制台: http://localhost:8080/api/h2-console
     - JDBC URL: jdbc:h2:file:./data/photostudio
     - 用户名: sa
     - 密码: （空）

### 前端启动

1. 确保已安装 Node.js

2. 进入前端目录：
   ```bash
   cd frontend
   ```

3. 安装依赖：
   ```bash
   npm install
   ```

4. 启动开发服务器：
   ```bash
   npm run dev
   ```

5. 浏览器访问: http://localhost:3000

## 数据库设计

### 主要数据表

1. **customer** - 客户信息表
   - 姓名、手机号、邮箱、性别、年龄、地址、来源、VIP等级、累计消费等

2. **orders** - 订单表
   - 订单编号、客户ID、套餐ID、加购项JSON、总金额、定金、状态、当前阶段
   - 拍摄日期、拍摄地点、摄影师、化妆师、修图师、订单来源、合同签署状态

3. **package** - 套餐表
   - 套餐名称、类型、价格、描述、照片数量、精修数量、相册数量、相框数量等

4. **add_on_item** - 加购项表
   - 名称、类别、价格、描述、单位、状态

5. **album** - 云相册表
   - 相册编号、名称、订单ID、客户ID、照片总数、已选数量、加密状态、访问密码等

6. **photo** - 照片表
   - 相册ID、文件名、原图URL、缩略图URL、精修图URL、选中状态、标记类型、修图状态

7. **employee** - 员工表
   - 姓名、职位、专长、等级、评分、当前任务数、完成任务数、是否外包等

## API接口

### 订单管理
- `GET /api/orders` - 查询所有订单
- `GET /api/orders/{id}` - 根据ID查询订单
- `GET /api/orders/page` - 分页查询订单
- `POST /api/orders` - 创建订单
- `PUT /api/orders` - 更新订单
- `DELETE /api/orders/{id}` - 删除订单
- `PUT /api/orders/{id}/stage/{stage}` - 更新订单阶段
- `PUT /api/orders/{id}/assign` - 分配工作人员
- `GET /api/orders/statistics` - 获取订单统计数据

### 客户管理
- `GET /api/customers` - 查询所有客户
- `GET /api/customers/{id}` - 根据ID查询客户
- `POST /api/customers` - 新增客户
- `PUT /api/customers` - 更新客户
- `DELETE /api/customers/{id}` - 删除客户

### 套餐管理
- `GET /api/packages` - 查询所有套餐
- `GET /api/packages/{id}` - 根据ID查询套餐
- `GET /api/packages/available` - 查询所有上架套餐
- `POST /api/packages` - 新增套餐
- `PUT /api/packages` - 更新套餐
- `DELETE /api/packages/{id}` - 删除套餐

### 加购项管理
- `GET /api/addons` - 查询所有加购项
- `POST /api/addons` - 新增加购项
- `PUT /api/addons` - 更新加购项
- `DELETE /api/addons/{id}` - 删除加购项

### 相册管理
- `GET /api/albums` - 查询所有相册
- `GET /api/albums/{id}` - 根据ID查询相册
- `POST /api/albums` - 新增相册
- `PUT /api/albums` - 更新相册
- `DELETE /api/albums/{id}` - 删除相册
- `GET /api/albums/{id}/photos` - 获取相册照片列表
- `POST /api/albums/{id}/photos` - 添加照片到相册
- `PUT /api/albums/photos/{photoId}/mark` - 更新照片标记
- `PUT /api/albums/photos/{photoId}/retouch` - 更新修图状态

### 员工管理
- `GET /api/employees` - 查询所有员工
- `GET /api/employees/{id}` - 根据ID查询员工
- `GET /api/employees/position/{position}` - 根据职位查询员工
- `POST /api/employees` - 新增员工
- `PUT /api/employees` - 更新员工
- `DELETE /api/employees/{id}` - 删除员工

## 测试数据

系统启动时会自动初始化以下测试数据：

### 套餐数据
- 轻奢婚纱套餐 (5999元)
- 豪华婚纱套餐 (9999元)
- 个人写真套餐 (1999元)
- 全家福套餐 (2999元)
- 儿童写真套餐 (1599元)

### 加购项数据
- 加拍夜景 (500元)
- 精修照片 (50元/张)
- 外景拍摄 (1000元)
- 12寸水晶相册 (800元)
- 36寸相框 (600元)

### 员工数据
- 李明（摄影师，高级）
- 王芳（化妆师，高级）
- 张伟（修图师，中级）
- 陈静（设计师，中级）
- 刘洋（摄影师，中级）

### 客户数据
- 张三（美团，VIP1）
- 李四（抖音）
- 王五（门店，VIP2）
- 赵六（小程序，VIP1）
- 孙七（老客户推荐）

## 开发说明

### 后端开发
- 使用 Spring Data JPA 进行数据访问
- 统一响应结果封装在 `Result` 类中
- Swagger 文档自动生成，所有接口都有详细注解
- 数据库使用 H2 嵌入式数据库，数据持久化到文件

### 前端开发
- 使用 Vue 3 Composition API
- Element Plus 作为 UI 组件库
- Axios 封装 API 请求
- Vue Router 进行路由管理
- ECharts 进行数据可视化展示

## 部署建议

### 生产环境部署
1. 后端：打包为 Jar 包，使用 Java -jar 命令运行
2. 前端：执行 npm run build，将 dist 目录部署到 Nginx
3. 数据库：建议更换为 MySQL 或 PostgreSQL
4. 配置跨域：在 Nginx 中配置反向代理

### 注意事项
- 生产环境请修改数据库密码
- 关闭 H2 控制台
- 配置文件敏感信息建议使用环境变量
- 定期备份数据库文件

## 版本历史

- v1.0.0 (2024-01-01)
  - 初始版本发布
  - 完成订单、客户、套餐、相册、员工管理功能
  - 完成前后端基础架构

## 联系方式

如有问题或建议，请联系开发团队。

## 许可证

MIT License
