# 宠物问诊管理后台系统

## 项目简介

这是一个功能完整的宠物问诊管理后台系统，包含以下核心功能：

- **紧急症状库管理**：兽医专家定期修订的宠物紧急症状信息
- **药品分类管理**：宠物药品的分类和信息管理
- **审核规则库**：动态配置审核规则，支持敏感词列表
- **智能设备监控**：连接智能喂食器/饮水机，监测宠物日常数据
- **AI诊断**：接入AI模型，分析问诊记录，生成诊断建议
- **保险理赔**：对接保险公司API，实现问诊费用直接理赔

## 技术栈

### 后端
- **JDK 8**
- **Spring Boot 2.7.18**
- **H2 Database**（嵌入式数据库）
- **Maven**
- **Swagger-OpenAPI**（SpringDoc）
- **Lombok**

### 前端
- **Vue 3**
- **Element Plus**
- **Vue Router**
- **Axios**
- **Vite**

## 项目结构

```
websysjava62/
├── backend/                    # 后端项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/petclinic/
│   │   │   │       ├── entity/        # 实体类
│   │   │   │       ├── repository/    # 数据访问层
│   │   │   │       ├── service/       # 业务逻辑层
│   │   │   │       ├── controller/    # 控制层
│   │   │   │       ├── config/        # 配置类
│   │   │   │       └── common/        # 通用类
│   │   │   └── resources/
│   │   │       └── application.properties
│   └── pom.xml
└── frontend/                   # 前端项目
    ├── src/
    │   ├── views/              # 页面组件
    │   ├── router/             # 路由配置
    │   ├── App.vue
    │   ├── main.js
    │   └── style.css
    ├── index.html
    ├── vite.config.js
    └── package.json
```

## 快速开始

### 后端启动

1. 进入后端目录：
```bash
cd backend
```

2. 使用Maven编译项目：
```bash
mvn clean install
```

3. 运行Spring Boot应用：
```bash
mvn spring-boot:run
```

4. 访问地址：
- **后端API**：http://localhost:8080
- **Swagger文档**：http://localhost:8080/swagger-ui.html
- **H2控制台**：http://localhost:8080/h2-console

### 前端启动

1. 进入前端目录：
```bash
cd frontend
```

2. 安装依赖：
```bash
npm install
```

3. 启动开发服务器：
```bash
npm run dev
```

4. 访问前端页面：http://localhost:3000

## 数据库说明

项目使用H2嵌入式数据库，系统启动时会自动初始化测试数据，包括：
- 紧急症状库数据
- 药品分类和药品数据
- 审核规则数据
- 智能设备数据
- 问诊记录数据

## 功能模块说明

### 1. 紧急症状库管理
- 维护宠物常见紧急症状信息
- 支持按严重程度、宠物类型分类
- 兽医专家可定期修订内容

### 2. 药品管理
- 药品分类管理
- 药品基本信息维护
- 支持按分类、名称搜索

### 3. 审核规则管理
- 敏感词列表动态配置
- 内容审核规则管理
- 支持启用/禁用规则

### 4. 智能设备监控
- 智能喂食器/饮水机管理
- 宠物进食/饮水数据监控
- 环境温湿度监测

### 5. 问诊记录管理
- 宠物问诊信息记录
- 诊断结果和治疗方案维护
- AI诊断建议生成

### 6. AI诊断建议
- 基于问诊记录自动分析
- 生成可能疾病列表
- 提供治疗建议和置信度
- 支持兽医专家审核

### 7. 保险理赔
- 理赔申请管理
- 理赔金额自动计算
- 理赔审核流程

## API文档

启动后端服务后，访问Swagger文档查看完整API列表：
- http://localhost:8080/swagger-ui.html

## 开发说明

### 注意事项
1. JDK版本要求1.8+
2. Maven版本要求3.6+
3. Node.js版本要求14+

### 系统默认账户
- 系统启动时自动生成测试数据
- 前端当前使用管理员角色（无登录校验）

## 版本历史

- v1.0.0 - 初始版本，完成所有核心功能

## 联系方式

如有问题，请联系项目维护团队。
