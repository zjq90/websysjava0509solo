# 影楼管理系统

## 项目简介

这是一个完整的影楼Web平台管理系统，包含销售业绩看板、客户转化漏斗、员工绩效排行、财务报表、订单管理、报销管理等功能模块。

## 技术栈

### 后端
- JDK 8
- Spring Boot 2.7.18
- H2 内存数据库
- Maven
- Swagger-OpenAPI (SpringDoc)

### 前端
- Vue 2.x
- Vue Router
- Element UI
- ECharts
- Axios

## 项目结构

```
websysjava39/
├── src/
│   └── main/
│       ├── java/com/photostudio/
│       │   ├── config/          # 配置类
│       │   ├── controller/      # 控制器
│       │   ├── dto/             # 数据传输对象
│       │   ├── entity/          # 实体类
│       │   ├── exception/       # 异常处理
│       │   ├── repository/      # 数据访问层
│       │   └── service/         # 业务逻辑层
│       └── resources/
│           └── application.yml  # 配置文件
├── frontend/                     # 前端项目
│   ├── src/
│   │   ├── views/               # 页面组件
│   │   ├── router/              # 路由
│   │   ├── App.vue
│   │   └── main.js
│   ├── public/
│   ├── package.json
│   └── vue.config.js
├── pom.xml                      # Maven配置
└── README.md
```

## 功能模块

### 1. 销售业绩看板
- 实时展示总营收、订单量、客单价、转化率等核心指标
- 支持按时间、门店、销售员、套餐类型多维度筛选
- 营收趋势图和套餐销量分布图表

### 2. 客户转化漏斗
- 分析"咨询→定单→拍摄→交付"各环节流失率，定位优化点
- 统计老客户复购率与转介绍率，评估客户忠诚度
- 漏斗图展示各环节转化情况

### 3. 员工绩效排行
- 摄影师、化妆师、选片师按接单量、客户评分、成单金额实时排行
- 修图师按修图数量、返修率进行质量考核
- 可视化图表展示排名情况

### 4. 收支记录
- 生成每日/每周/每月财务报表，支持按门店、销售员维度统计
- 收支趋势图和支出构成饼图

### 5. 成本与利润分析
- 计算每笔订单的成本（人工、服装、耗材）与净利润
- 识别高利润套餐

### 6. 报销管理
- 员工报销流程线上化，支持上传凭证、审批留痕
- 支持按状态、员工筛选报销记录

## 数据库设计

主要数据表：
- `ps_store` - 门店表
- `ps_employee` - 员工表
- `ps_package` - 套餐表
- `ps_customer` - 客户表
- `ps_order` - 订单表
- `ps_finance_record` - 财务记录表
- `ps_reimbursement` - 报销表
- `ps_photo_edit_record` - 修图记录表

## 快速开始

### 后端启动

1. 确保已安装 JDK 8 和 Maven

2. 在项目根目录执行：
```bash
mvn clean install
mvn spring-boot:run
```

3. 后端服务启动后，访问以下地址：
   - API文档: http://localhost:8080/swagger-ui.html
   - H2控制台: http://localhost:8080/h2-console
     - JDBC URL: `jdbc:h2:mem:photostudio`
     - 用户名: `sa`
     - 密码: (空)

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
npm run serve
```

5. 访问前端页面: http://localhost:8081

### 初始化测试数据

启动后端服务后，点击页面右上角的"初始化测试数据"按钮，系统将自动生成演示数据。

## API接口说明

### 看板相关
- `GET /api/dashboard/sales` - 获取销售业绩数据
- `GET /api/dashboard/conversion-funnel` - 获取转化漏斗数据
- `GET /api/dashboard/employee-performance` - 获取员工绩效数据
- `GET /api/dashboard/finance-report` - 获取财务报表数据

### 基础CRUD接口
- `/api/orders/**` - 订单管理
- `/api/stores/**` - 门店管理
- `/api/employees/**` - 员工管理
- `/api/reimbursements/**` - 报销管理

### 数据初始化
- `POST /api/init/all` - 初始化所有测试数据

## 开发说明

### 后端开发
- 采用Spring Data JPA进行数据访问
- 全局异常处理统一返回格式
- Swagger自动生成API文档

### 前端开发
- 使用Vue Router进行路由管理
- Element UI组件库构建界面
- ECharts实现数据可视化

## 注意事项

1. H2数据库为内存数据库，服务重启后数据会丢失
2. 生产环境建议更换为MySQL等持久化数据库
3. 测试数据仅供演示使用

## License

MIT
