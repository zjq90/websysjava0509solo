# 文物收藏管理后台系统

基于 Spring Boot + Vue 构建的文物收藏管理全栈系统，集成区块链、物联网、数字分身、VR博物馆等先进技术。

## 技术栈

### 后端
- **JDK 8** - Java开发环境
- **Spring Boot 2.7.18** - 后端框架
- **H2 Database** - 嵌入式数据库
- **Maven** - 项目构建工具
- **Swagger OpenAPI** - API文档
- **Spring Data JPA** - 数据持久层

### 前端
- **Vue 3** - 前端框架
- **Vite** - 构建工具
- **Element Plus** - UI组件库
- **ECharts** - 图表库
- **Axios** - HTTP客户端

## 功能模块

### 1. 文物管理
- 文物信息CRUD操作
- 文物分类管理
- 朝代管理
- 文物状态管理

### 2. 区块链节点管理
- 区块链节点添加/移除
- 支持跨可用区部署
- 主节点/备份节点/同步节点管理
- 节点状态监控

### 3. 物联网设备管理
- 温湿度传感器管理
- NFC近场通信绑定文物
- 实时温湿度监控
- 设备告警管理

### 4. 审核规则库
- 文物入库审核规则
- 文物出库审核规则
- 修复审核规则
- 展览审核规则
- 多级审核配置

### 5. 文物数字分身
- 文物3D模型管理
- 支持多种模型格式
- 虚拟修复演示
- 精度等级管理

### 6. VR虚拟博物馆
- VR场景管理
- 360度全景浏览
- 多展厅支持
- 支持VR设备

### 7. 文物识别模型
- AI模型部署管理
- 文物类别自动识别
- 多种框架支持（PyTorch/ONNX）
- 模型准确率监控

## 项目结构

```
websysjava55/
├── backend/                    # 后端项目
│   ├── pom.xml                # Maven配置
│   └── src/main/
│       ├── java/com/culturalrelic/
│       │   ├── CulturalRelicApplication.java    # 启动类
│       │   ├── common/                            # 公共类
│       │   ├── config/                           # 配置类
│       │   ├── controller/                       # 控制器
│       │   ├── entity/                          # 实体类
│       │   ├── repository/                      # 数据访问层
│       │   └── service/                        # 业务逻辑层
│       └── resources/
│           └── application.properties           # 配置文件
└── frontend/                    # 前端项目
    ├── package.json            # 依赖配置
    ├── vite.config.js          # Vite配置
    ├── index.html              # 入口HTML
    └── src/
        ├── main.js             # 入口JS
        ├── App.vue             # 主组件
        ├── router/             # 路由配置
        ├── api/               # API封装
        ├── assets/styles/     # 样式文件
        └── views/             # 页面组件
```

## 快速开始

### 后端启动

1. 进入后端目录：
```bash
cd backend
```

2. 使用Maven编译运行：
```bash
mvn clean install
mvn spring-boot:run
```

3. 访问地址：
- 后端API：http://localhost:8080/api
- Swagger文档：http://localhost:8080/api/swagger-ui.html
- H2控制台：http://localhost:8080/api/h2-console

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

4. 访问地址：http://localhost:3000

## 数据库配置

系统使用H2嵌入式数据库，配置如下：

```properties
spring.datasource.url=jdbc:h2:file:./data/cultural_relic_db
spring.datasource.username=admin
spring.datasource.password=admin123
```

## 测试数据

系统启动时会自动初始化测试数据，包括：
- 8个文物样本
- 5个区块链节点
- 6个物联网设备
- 4个审核规则
- 4个数字分身
- 4个VR场景
- 3个识别模型

## API文档

启动后端后，访问 Swagger UI 查看完整API文档：

http://localhost:8080/api/swagger-ui.html

## 主要功能截图

### 数据概览
- 统计卡片展示各项指标
- 文物类别分布饼图
- 朝代分布柱状图
- 区块链节点状态
- 设备实时监控

### 文物管理
- 文物列表展示
- 新增/编辑/删除文物
- 搜索和筛选功能

## 开发说明

### 后端开发
- 实体类继承 `BaseEntity` 获得基础字段
- Repository层继承 `JpaRepository` 和 `JpaSpecificationExecutor`
- Controller统一返回 `Result<T>` 格式

### 前端开发
- 使用 Vue 3 Composition API
- 页面组件位于 `src/views/` 目录
- API调用统一封装在 `src/api/index.js`

## 系统特点

1. **模块化设计** - 清晰的包结构，各模块独立
2. **完整的CRUD** - 所有模块支持基本的增删改查
3. **美观的UI** - Element Plus组件库，响应式布局
4. **API文档** - 集成Swagger，接口自动生成文档
5. **测试数据** - 自动初始化，方便功能测试
6. **跨域支持** - 已配置CORS跨域支持

## 许可证

MIT License
