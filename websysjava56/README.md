# 文物收藏数据中台

## 项目简介

文物收藏数据中台是一个集数据采集、治理、分析和服务于一体的综合性平台，为文物管理、收藏、研究提供全方位的数据支持。

## 技术栈

### 后端
- **JDK 1.8** - Java开发环境
- **Spring Boot 2.7.18** - 应用框架
- **H2 Database** - 内存数据库
- **Maven** - 项目构建工具
- **Swagger/OpenAPI 3.0** - API文档
- **Jsoup** - 网页爬虫
- **Spring Security + OAuth2** - 安全认证

### 前端
- **Vue 2.6** - 前端框架
- **Element UI** - UI组件库
- **Vue Router** - 路由管理
- **Vuex** - 状态管理
- **Axios** - HTTP客户端
- **ECharts** - 数据可视化

## 功能模块

### 1. 数据采集
- **文物信息爬取**：支持博物馆官网、拍卖行数据爬取，遵守robots协议
- **用户行为日志**：记录点击、浏览、收藏、购买等用户行为
- **物联网设备数据**：温湿度、光照、震动等环境监测数据，支持数据完整性校验

### 2. 数据治理
- **数据清洗**：去重、纠错、标准化处理
- **主数据管理**：统一文物编号规则，与国家文物编码标准对齐
- **数据源追踪**：可视化展示数据来源和流转路径

### 3. 数据分析
- **数据融合**：融合博物馆结构化数据与用户上传非结构化数据
- **价值评估模型**：基于历史交易数据，引入专家经验规则（如明代青花瓷价格下限）
- **用户偏好预测**：基于行为日志的推荐算法
- **风险预警模型**：支持动态调整阈值的环境风险预警

### 4. 数据服务
- **API开放平台**：OAuth2.0授权的第三方API接口
- **数据订阅**：按主题推送更新，支持数据脱敏
- **沙箱环境**：完整的API测试环境

## 项目结构

```
websysjava56/
├── backend/                    # 后端项目
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/heritage/
│   │       │       ├── HeritageApplication.java    # 启动类
│   │       │       ├── config/                     # 配置类
│   │       │       ├── controller/                 # 控制器
│   │       │       ├── entity/                     # 实体类
│   │       │       ├── repository/                 # 数据访问层
│   │       │       ├── service/                    # 业务逻辑层
│   │       │       └── common/                     # 公共类
│   │       └── resources/
│   │           └── application.properties          # 配置文件
│   └── pom.xml                                     # Maven配置
│
└── frontend/                   # 前端项目
    ├── src/
    │   ├── main.js             # 入口文件
    │   ├── App.vue             # 根组件
    │   ├── router/             # 路由配置
    │   ├── store/              # Vuex状态
    │   └── views/              # 页面组件
    ├── public/
    │   └── index.html
    └── package.json            # NPM配置
```

## 快速开始

### 环境要求
- JDK 1.8+
- Maven 3.6+
- Node.js 14+

### 后端启动

**方式一：使用启动脚本（推荐）**
```bash
cd backend
run.bat
```

**方式二：手动启动**
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

**访问地址**
- 应用地址: http://localhost:8080/api
- Swagger文档: http://localhost:8080/api/swagger-ui.html
- H2控制台: http://localhost:8080/api/h2-console

### 前端启动

**方式一：使用启动脚本（推荐）**
```bash
cd frontend
run.bat
```

**方式二：手动启动**
```bash
cd frontend
npm install
npm run serve
```

**访问地址**: http://localhost:8081

## 🔧 已修复的问题

### 1. Maven依赖错误修复
**问题**: `Could not find artifact org.springdoc:springdoc-openapi-ui:pom:3.0.0`

**原因**: 
- Spring Boot 2.7.x 与 springdoc-openapi-ui 3.0.0 不兼容
- 3.0.0 版本是专门为 Spring Boot 3 设计的

**解决方案**:
1. ✅ 将 springdoc-openapi-ui 版本降级为 `1.6.15`（与 Spring Boot 2.7.x 兼容）
2. ✅ 移除了不兼容的 OAuth2 依赖
3. ✅ 简化 Security 配置，使用 Basic Auth 认证
4. ✅ 添加了 Maven Wrapper 支持
5. ✅ 创建了一键启动脚本

---

### 2. 导入错误修复
**问题**: `程序包org.h2.mvstore不存在`

**原因**:
- HeritageController.java 中错误地导入了 H2 数据库内部类 `org.h2.mvstore.Page`
- 应该导入 Spring Data 的 `org.springframework.data.domain.Page`
- 同时还错误导入了 `SpringDataWebProperties.Pageable`

**修复内容** (HeritageController.java):
| 错误导入 | 正确导入 |
|---------|---------|
| `org.h2.mvstore.Page` ❌ | `org.springframework.data.domain.Page` ✅ |
| `SpringDataWebProperties.Pageable` ❌ | `org.springframework.data.domain.Pageable` ✅ |
| 第12行多余分号 ❌ | 已移除 ✅ |

---

### 3. JPA实体映射错误修复
**问题**: 
```
No property 'dynasty' found for type 'Transaction'
```

**原因**:
- `TransactionRepository` 中错误定义了 `findByDynastyAndDeletedFalse()` 方法
- `Transaction` 实体类中并没有 `dynasty` 属性，该属性在 `Heritage` 实体中
- `ValueEvaluationService` 中调用了 `findByHeritageId(..., null)` 传入null参数

**修复内容**:
1. ✅ 从 `TransactionRepository` 移除了错误的 `findByDynastyAndDeletedFalse()` 方法
2. ✅ 修复了 `ValueEvaluationService` 中的null参数调用，改用 `countByHeritageCode()`
3. ✅ 创建了所有缺失的Repository：
   - `IotDeviceDataRepository`
   - `RiskAlertRepository`
   - `DataSourceTraceRepository`
   - `DataSubscriptionRepository`

---

**默认登录账号**:
- 用户名: `admin`
- 密码: `admin123`

## 核心API接口

### 文物管理
- `GET /api/heritages` - 获取文物列表
- `POST /api/heritages` - 创建文物记录
- `GET /api/heritages/{id}` - 获取单个文物
- `PUT /api/heritages/{id}` - 更新文物信息
- `DELETE /api/heritages/{id}` - 删除文物
- `POST /api/heritages/{id}/convert-master` - 转换为主数据
- `GET /api/heritages/{id}/evaluate-value` - 文物价值评估

### 数据分析
- `GET /api/analysis/recommendations/user/{userId}` - 用户推荐
- `GET /api/analysis/recommendations/similar/{heritageId}` - 相似文物推荐
- `GET /api/analysis/preferences/{userId}` - 用户偏好画像
- `POST /api/analysis/risk/check` - 环境风险检测
- `POST /api/analysis/risk/thresholds` - 更新阈值

### 测试数据
- `POST /api/heritages/generate-test-data` - 生成文物测试数据
- `POST /api/analysis/generate-behavior-logs` - 生成行为日志
- `POST /api/analysis/generate-iot-data` - 生成物联网数据

## 数据库配置

项目使用H2内存数据库，配置如下：
- JDBC URL: `jdbc:h2:mem:heritagedb;DB_CLOSE_DELAY=-1`
- 用户名: `sa`
- 密码: (空)

## 数据模型

### 核心实体
- **Heritage** - 文物信息
- **User** - 用户信息
- **Transaction** - 交易记录
- **UserBehaviorLog** - 用户行为日志
- **IotDeviceData** - 物联网设备数据
- **DataSourceTrace** - 数据源追踪
- **DataSubscription** - 数据订阅
- **RiskAlert** - 风险预警

## 特色功能

### 1. 文物价值评估
- 基于历史交易数据的智能估价
- 引入专家经验规则（如明代青花瓷价格下限保护）
- 综合考虑朝代、类别、材质、保存状况等因素

### 2. 智能推荐系统
- 基于用户行为的协同过滤推荐
- 基于文物属性的相似性推荐
- 用户偏好画像可视化展示

### 3. 环境风险预警
- 实时监测温湿度、震动等环境指标
- 可动态调整的预警阈值
- 多级预警机制（警告、严重）

### 4. 数据溯源追踪
- 完整的数据来源记录
- 可视化的数据流转路径
- 支持数据血缘关系查询

## 开发说明

### 添加新的文物分类
1. 在 `MasterDataService` 中添加分类代码映射
2. 更新前端分类选择组件
3. 重新编译部署

### 调整评估模型参数
1. 修改 `ValueEvaluationService` 中的权重参数
2. 调整专家规则阈值
3. 测试验证效果

## 许可证

Apache License 2.0

## 联系方式

如有问题或建议，请联系开发团队。
