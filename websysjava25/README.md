# 检验检查管理系统 (LIMS)

## 项目简介

本系统是一个完整的检验检查管理平台，实现检验、检查科室与临床科室的信息互通，提高医技科室工作效率和报告准确性。

## 技术栈

### 后端
- **JDK 8** - Java开发环境
- **Spring Boot 2.7.x** - 应用框架
- **H2 Database** - 内存数据库
- **Maven** - 项目构建工具
- **Swagger/OpenAPI 3** - API文档工具
- **Spring Data JPA** - ORM框架

### 前端
- **Vue 2.x** - 前端框架
- **Vue Router** - 路由管理
- **Element UI** - UI组件库
- **Axios** - HTTP客户端

## 功能模块

### 1. 申请管理
- 接收临床科室发送的检验/检查申请
- 任务确认与分配
- 申请状态跟踪

### 2. 技师工作站
- 技师接收任务
- 记录操作过程
- 录入检验/检查结果
- 结果异常标识

### 3. 报告审核与发布
- 支持多级审核（一级审核、二级审核）
- 审核通过后的报告自动反馈至申请科室
- 患者自助查询报告

### 4. 设备接口
- 与各类检验、检查设备对接
- 自动采集设备结果
- 减少人工录入错误

### 5. 基础数据管理
- 科室管理（临床科室、检验科室、检查科室）
- 用户管理（管理员、医生、技师、审核人员、患者）
- 患者管理
- 检验检查项目管理
- 设备管理

## 项目结构

```
websysjava25/
├── src/
│   └── main/
│       ├── java/com/lims/
│       │   ├── entity/          # 实体类
│       │   ├── repository/      # 数据访问层
│       │   ├── service/         # 业务逻辑层
│       │   ├── controller/      # 控制器层
│       │   ├── config/          # 配置类
│       │   └── LaboratoryManagementApplication.java  # 启动类
│       └── resources/
│           └── application.yml  # 配置文件
├── frontend/                     # 前端项目
│   ├── src/
│   │   ├── views/               # 页面组件
│   │   ├── router/              # 路由配置
│   │   ├── App.vue              # 主组件
│   │   └── main.js              # 入口文件
│   ├── public/
│   │   └── index.html           # HTML模板
│   └── package.json             # 前端依赖配置
├── pom.xml                       # Maven配置文件
└── README.md                     # 项目说明文档
```

## 快速开始

### 后端启动

1. **环境要求**
   - JDK 8+
   - Maven 3.6+

2. **编译运行**
   ```bash
   # 进入项目目录
   cd websysjava25

   # 编译项目
   mvn clean compile

   # 运行项目
   mvn spring-boot:run
   ```

3. **访问地址**
   - 应用主页: http://localhost:8080/api/
   - Swagger文档: http://localhost:8080/api/swagger-ui.html
   - H2数据库控制台: http://localhost:8080/api/h2-console
     - JDBC URL: `jdbc:h2:mem:limsdb`
     - 用户名: `sa`
     - 密码: （空）

### 前端启动

1. **环境要求**
   - Node.js 14+
   - npm 6+

2. **安装依赖并运行**
   ```bash
   # 进入前端目录
   cd frontend

   # 安装依赖
   npm install

   # 启动开发服务器
   npm run serve
   ```

3. **访问地址**
   - 前端地址: http://localhost:8081

## 数据库表设计

### 主要表结构

| 表名 | 说明 |
|------|------|
| department | 科室表 |
| sys_user | 用户表 |
| patient | 患者表 |
| test_item | 检验检查项目表 |
| device | 设备表 |
| test_application | 检验检查申请表 |
| test_result | 检验检查结果表 |
| test_report | 检验检查报告表 |

### 状态字典

**申请状态:**
- PENDING: 待确认
- CONFIRMED: 已确认
- ASSIGNED: 已分配
- PROCESSING: 处理中
- REPORTED: 已出报告
- PUBLISHED: 已发布

**报告状态:**
- DRAFT: 草稿
- FIRST_AUDIT: 待一级审核
- SECOND_AUDIT: 待二级审核
- PASSED: 审核通过
- REJECTED: 已驳回
- PUBLISHED: 已发布

## 测试数据

系统启动时会自动初始化以下测试数据：

- **科室**: 4个科室（检验科、影像科、内科、外科）
- **用户**: 5个用户（管理员、医生、技师、审核人员、患者）
- **患者**: 3个测试患者
- **项目**: 4个检验检查项目（血常规、肝功能、胸部CT、尿常规）
- **设备**: 3个设备（生化分析仪、血细胞分析仪、CT扫描仪）
- **申请**: 4个检验检查申请（包含不同状态）
- **结果**: 2个检验检查结果
- **报告**: 1个已发布检验报告

## API接口说明

### 主要接口

| 模块 | 接口路径 | 说明 |
|------|----------|------|
| 科室 | GET /api/departments | 查询所有科室 |
| 用户 | GET /api/users | 查询所有用户 |
| 患者 | GET /api/patients | 查询所有患者 |
| 项目 | GET /api/items | 查询所有项目 |
| 申请 | GET /api/applications | 查询所有申请 |
| 结果 | GET /api/results | 查询所有结果 |
| 报告 | GET /api/reports | 查询所有报告 |
| 测试 | GET /api/test/status | 检查系统状态 |

完整API文档请访问: http://localhost:8080/api/swagger-ui.html

## 功能演示流程

1. **申请创建**
   - 临床医生创建检验检查申请

2. **任务分配**
   - 科室管理员确认申请并分配给技师

3. **结果录入**
   - 技师开始处理任务，录入检验检查结果

4. **报告生成**
   - 系统生成检验检查报告草稿

5. **报告审核**
   - 一级审核：审核人员审核报告
   - 二级审核：上级医生复核报告

6. **报告发布**
   - 审核通过后报告正式发布，患者可查看

## 注意事项

1. 本系统使用H2内存数据库，重启后数据会重置
2. 如需持久化数据，请修改application.yml中的数据库配置
3. 前端页面使用Vue + Element UI，需先配置好Node.js环境
4. 建议使用Chrome或Edge浏览器访问系统

## 技术支持

如有问题请查看Swagger文档或联系开发团队。
