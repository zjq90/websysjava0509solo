# 种子销售订单管理系统

一个完整的销售订单闭环流程管理系统，包含后端API和移动端APP。

## 技术栈

### 后端
- Spring Boot 3.2.0
- H2 内存数据库
- Swagger/OpenAPI 3.0
- JPA + Hibernate
- AES-256 数据加密

### 前端
- UniApp (Vue 3)
- 移动端适配（iOS/Android/H5）

## 核心功能

### 销售订单闭环流程
1. **现场下单**：业务员为客户创建订单，支持选品、定价、生成合同
2. **价格策略**：根据客户等级自动匹配价格（普通/VIP/SVIP/钻石）
3. **电子合同**：客户扫码签署合同
4. **ERP同步**：订单签署后同步至ERP系统
5. **仓库备货**：触发仓库备货流程
6. **物流跟踪**：集成快递API，实时显示发货进度
7. **订单完成**：签收后完成订单

### 客户管理
- 客户信息CRUD
- 客户等级管理（普通/VIP/SVIP/钻石/临时）
- 历史订单查询
- 信用额度管理
- 回访记录
- **临时客户快速注册**：支持展会/下乡推广时快速下单

### 产品管理
- 产品信息CRUD
- 库存管理
- 质量参数（发芽率、纯度、水分）
- 多规格支持

## 数据验证规则

- **批次编号**：8位数字+字母组合，全局唯一
- **保质期**：不得早于当前日期+6个月
- **发芽率**：数值范围0-100%，精度保留1位小数
- **客户手机号**：中国大陆手机号格式（1开头，11位）

## 数据安全

敏感信息（客户联系方式等）采用AES-256加密存储。

## 项目结构

```
appsysjava16/
├── backend/                     # 后端项目
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/appsys/
│       │   ├── AppSysApplication.java      # 启动类
│       │   ├── config/                     # 配置类
│       │   │   ├── SwaggerConfig.java
│       │   │   ├── WebConfig.java
│       │   │   └── DataInitializer.java    # 测试数据初始化
│       │   ├── controller/                 # 控制器
│       │   │   ├── ProductController.java
│       │   │   ├── CustomerController.java
│       │   │   ├── SalesOrderController.java
│       │   │   ├── LogisticsController.java
│       │   │   └── TestController.java     # 测试接口
│       │   ├── service/                    # 业务层
│       │   ├── repository/                 # 数据访问层
│       │   ├── entity/                     # 实体类
│       │   ├── validation/                 # 自定义验证
│       │   ├── util/                       # 工具类
│       │   ├── exception/                  # 异常处理
│       │   └── dto/                        # 数据传输对象
│       └── resources/
│           └── application.yml
│
└── frontend/                    # 前端项目（UniApp）
    ├── package.json
    ├── manifest.json
    ├── pages.json
    ├── App.vue
    ├── main.js
    ├── api/                       # API接口封装
    │   ├── product.js
    │   ├── customer.js
    │   ├── order.js
    │   └── logistics.js
    ├── utils/
    │   └── request.js             # 请求封装
    └── pages/
        ├── home/
        │   └── index.vue          # 首页
        ├── product/
        │   ├── list.vue           # 产品列表
        │   └── detail.vue         # 产品详情
        ├── customer/
        │   ├── list.vue           # 客户列表
        │   ├── add.vue            # 客户添加
        │   └── detail.vue         # 客户详情
        └── order/
            ├── list.vue           # 订单列表
            ├── create.vue         # 订单创建（三步流程）
            ├── detail.vue         # 订单详情
            ├── sign.vue           # 合同签署
            └── logistics.vue      # 物流跟踪
```

## 快速开始

### 1. 启动后端

**环境要求：**
- JDK 17+
- Maven 3.6+

**启动步骤：**

```bash
cd backend

# 编译项目
mvn clean compile

# 启动应用
mvn spring-boot:run
```

或者使用IDE（IntelliJ IDEA）：
1. 打开后端项目
2. 等待Maven依赖下载完成
3. 运行 `AppSysApplication.java` 主类

**访问地址：**
- API服务：http://localhost:8080
- Swagger文档：http://localhost:8080/swagger-ui.html
- H2控制台：http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:appsys`
  - User Name: `sa`
  - Password: (留空)

### 2. 启动前端

**环境要求：**
- Node.js 16+
- HBuilderX 或 VS Code + UniApp插件

**启动步骤（HBuilderX）：**
1. 打开HBuilderX
2. 导入前端项目（`frontend`目录）
3. 运行到浏览器或模拟器

**启动步骤（命令行）：**

```bash
cd frontend

# 安装依赖（如果package.json有配置）
npm install

# 运行到H5
npm run dev:h5
```

## 系统测试

### 1. Swagger API 测试

启动后端后，访问 http://localhost:8080/swagger-ui.html

主要测试接口：

**产品管理：**
- `GET /api/products` - 获取产品列表
- `GET /api/products/{id}` - 获取产品详情
- `POST /api/products` - 新增产品（测试验证规则）
- `PUT /api/products/{id}` - 更新产品
- `DELETE /api/products/{id}` - 删除产品

**客户管理：**
- `GET /api/customers` - 获取客户列表
- `POST /api/customers` - 新增客户
- `POST /api/customers/temporary` - 快速创建临时客户
- `GET /api/customers/{id}` - 获取客户详情
- `GET /api/customers/{id}/orders` - 获取客户历史订单

**订单管理：**
- `GET /api/orders` - 获取订单列表
- `POST /api/orders` - 创建订单
- `GET /api/orders/{id}` - 获取订单详情
- `POST /api/orders/{id}/confirm` - 确认订单
- `POST /api/orders/{id}/sign` - 签署合同
- `POST /api/orders/{id}/sync-erp` - 同步ERP
- `POST /api/orders/{id}/stock-prepare` - 仓库备货
- `POST /api/orders/{id}/ship` - 发货
- `POST /api/orders/{id}/deliver` - 签收
- `POST /api/orders/{id}/complete` - 完成订单

**物流跟踪：**
- `GET /api/logistics/order/{orderId}` - 获取订单物流信息
- `POST /api/logistics/{orderId}/update` - 更新物流信息

**测试接口：**
- `GET /api/test/demo-data` - 获取所有演示数据
- `POST /api/test/create-test-order?customerId=1&productId=1&quantity=10` - 创建测试订单
- `POST /api/test/full-order-flow/{orderId}` - 自动执行完整订单流程
- `GET /api/test/validation-test` - 获取验证测试数据
- `GET /api/test/price-strategy` - 查看价格策略

### 2. 数据验证测试

通过Swagger测试以下验证规则：

**批次编号验证：**
- ✅ 有效：`ABC12345`（8位数字字母）
- ❌ 无效：`AB123`（太短）
- ❌ 无效：`AB@12345`（含特殊字符）

**手机号验证：**
- ✅ 有效：`13812345678`
- ❌ 无效：`23812345678`（非1开头）
- ❌ 无效：`1381234567`（不足11位）

**保质期验证：**
- ✅ 有效：当前日期 + 7个月以上
- ❌ 无效：当前日期 + 3个月（不足6个月）

**发芽率验证：**
- ✅ 有效：`0.0` ~ `100.0`
- ❌ 无效：`-1.0`（负数）
- ❌ 无效：`100.1`（超过100）

### 3. 价格策略测试

客户等级折扣：
- 临时客户：无折扣（100%）
- 普通客户：95折
- VIP客户：90折
- SVIP客户：85折
- 钻石客户：80折

测试步骤：
1. 创建不同等级的客户
2. 为各客户创建相同产品的订单
3. 查看订单金额，验证价格计算是否正确

### 4. 订单完整流程测试

**方式一：使用测试接口一键完成**
```
POST /api/test/full-order-flow/{orderId}
```

**方式二：手动逐步测试**
1. 创建订单 → 状态：PENDING_CONFIRMATION
2. 确认订单 → 状态：PENDING_SIGNATURE
3. 签署合同 → 状态：SIGNED
4. 同步ERP → 状态：STOCK_PREPARING
5. 发货 → 状态：SHIPPED（创建物流记录）
6. 签收 → 状态：DELIVERED
7. 完成 → 状态：COMPLETED

### 5. 前端功能测试

1. **首页测试**
   - 查看订单统计数据
   - 查看最近订单列表
   - 测试快捷操作入口

2. **产品管理**
   - 产品列表搜索和筛选
   - 产品详情查看
   - 验证批次号、发芽率等数据显示

3. **客户管理**
   - 客户列表搜索
   - 正式/临时客户筛选
   - 快速添加临时客户
   - 添加新客户（测试手机号验证）
   - 客户详情：历史订单、回访记录
   - 添加回访记录

4. **订单管理**
   - 订单列表（按状态筛选）
   - 三步创建订单流程
     - 步骤1：选择客户
     - 步骤2：选择商品（验证客户折扣价格）
     - 步骤3：确认订单
   - 订单详情查看
   - 订单状态流转操作

5. **合同签署**
   - 合同内容预览
   - 签署功能测试

6. **物流跟踪**
   - 物流状态展示
   - 物流详情时间线
   - 刷新物流功能

## 初始化测试数据

系统启动时会自动初始化以下测试数据：

**产品：**
- 5种种子产品（玉米、小麦、水稻、大豆、棉花）
- 各产品包含：批次编号、保质期、发芽率、纯度、水分等参数
- 不同的基准价格和库存数量

**客户：**
- 5个不同等级的客户（临时、普通、VIP、SVIP、钻石）
- 包含手机号、地址、信用额度等信息
- 累计消费金额各不相同

**订单：**
- 多个不同状态的示例订单
- 包含订单明细和金额

**物流：**
- 已发货订单的物流跟踪记录

**回访记录：**
- 客户的回访历史记录

## 订单状态流转

```
PENDING_CONFIRMATION (待确认)
    ↓ 确认订单
PENDING_SIGNATURE (待签署)
    ↓ 签署合同
SIGNED (已签署)
    ↓ ERP同步
STOCK_PREPARING (备货中)
    ↓ 发货
SHIPPED (已发货) → 创建物流记录
    ↓ 签收
DELIVERED (已签收)
    ↓ 完成
COMPLETED (已完成)
```

## API接口列表

### 产品接口
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/products | 获取所有产品 |
| GET | /api/products/{id} | 获取产品详情 |
| GET | /api/products/search?keyword= | 搜索产品 |
| POST | /api/products | 新增产品 |
| PUT | /api/products/{id} | 更新产品 |
| DELETE | /api/products/{id} | 删除产品 |

### 客户接口
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/customers | 获取所有客户 |
| GET | /api/customers/{id} | 获取客户详情 |
| GET | /api/customers/search?keyword= | 搜索客户 |
| GET | /api/customers/regular | 获取正式客户 |
| GET | /api/customers/temporary | 获取临时客户 |
| POST | /api/customers | 新增客户 |
| POST | /api/customers/temporary | 快速创建临时客户 |
| GET | /api/customers/{id}/orders | 获取客户订单 |
| POST | /api/customers/{id}/visits | 添加回访记录 |

### 订单接口
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/orders | 获取所有订单 |
| GET | /api/orders/{id} | 获取订单详情 |
| GET | /api/orders/search?keyword= | 搜索订单 |
| GET | /api/orders/status/{status} | 按状态获取订单 |
| POST | /api/orders | 创建订单 |
| POST | /api/orders/{id}/confirm | 确认订单 |
| POST | /api/orders/{id}/sign | 签署合同 |
| POST | /api/orders/{id}/sync-erp | 同步ERP |
| POST | /api/orders/{id}/stock-prepare | 备货 |
| POST | /api/orders/{id}/ship | 发货 |
| POST | /api/orders/{id}/deliver | 签收 |
| POST | /api/orders/{id}/complete | 完成订单 |
| GET | /api/orders/statistics | 订单统计 |

### 物流接口
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/logistics/order/{orderId} | 获取订单物流 |
| POST | /api/logistics/{orderId}/update | 更新物流 |

## 常见问题

### 1. 后端启动失败
- 检查JDK版本是否为17+
- 检查Maven依赖是否下载完整
- 查看控制台错误信息

### 2. 前端无法连接后端
- 确认后端已启动（访问 http://localhost:8080 测试）
- 检查 `frontend/utils/request.js` 中的baseURL配置
- 检查浏览器控制台是否有CORS错误

### 3. H2控制台无法登录
- 确认JDBC URL为 `jdbc:h2:mem:appsys`
- User Name为 `sa`，Password留空

### 4. 数据验证不生效
- 确认请求使用 `@Valid` 注解
- 检查请求体格式是否正确
- 查看返回的错误信息详情

## 开发说明

### 添加新产品类型
1. 在 `ProductCategory` 枚举中添加新类型
2. 前端产品列表页的分类筛选同步更新

### 添加新客户等级
1. 在 `CustomerLevel` 枚举中添加新等级
2. 设置对应的折扣率
3. 前端客户等级标签样式同步更新

### 自定义订单状态
1. 在 `OrderStatus` 枚举中添加新状态
2. 更新 `SalesOrderService` 中的状态流转逻辑
3. 前端状态文本和颜色同步更新

## 许可证

MIT License
