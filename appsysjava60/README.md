# 宠物医院问诊系统

基于 SpringBoot + UniApp 开发的宠物医院医生端知识库和数据统计系统。

## 技术栈

### 后端
- **框架**: SpringBoot 2.7.x
- **数据库**: H2 (内存数据库)
- **缓存**: Redis
- **API文档**: Swagger / SpringDoc OpenAPI
- **ORM**: Spring Data JPA
- **导出**: EasyExcel

### 前端
- **框架**: UniApp (Vue 3)
- **UI**: 原生组件 + 自定义样式
- **适配**: 响应式设计，支持手机、平板

## 项目结构

```
appsysjava60/
├── backend/                    # 后端项目
│   ├── pom.xml
│   └── src/
│       └── main/
│           ├── java/com/pethospital/
│           │   ├── PetHospitalApplication.java     # 启动类
│           │   ├── config/                         # 配置类
│           │   │   ├── OpenApiConfig.java          # Swagger配置
│           │   │   ├── RedisConfig.java            # Redis配置
│           │   │   ├── CorsConfig.java             # 跨域配置
│           │   │   └── DataInitializer.java        # 数据初始化
│           │   ├── common/                         # 公共模块
│           │   │   ├── Result.java                 # 统一响应封装
│           │   │   ├── BusinessException.java      # 业务异常
│           │   │   └── GlobalExceptionHandler.java # 全局异常处理
│           │   ├── entity/                         # 实体类
│           │   │   ├── Doctor.java                 # 医生
│           │   │   ├── Disease.java                # 疾病
│           │   │   ├── Medicine.java               # 药品
│           │   │   ├── Case.java                   # 案例
│           │   │   ├── Consultation.java           # 接诊记录
│           │   │   └── KnowledgeUpdate.java        # 知识库更新
│           │   ├── repository/                     # 数据访问层
│           │   ├── service/                        # 业务逻辑层
│           │   │   ├── KnowledgeService.java       # 知识库服务
│           │   │   └── StatisticsService.java      # 统计服务
│           │   ├── controller/                     # 控制器
│           │   │   ├── KnowledgeController.java    # 知识库接口
│           │   │   └── StatisticsController.java   # 统计接口
│           │   └── dto/                            # 数据传输对象
│           └── resources/
│               └── application.yml                 # 配置文件
│
└── frontend/                   # 前端项目
    ├── pages.json              # 页面路由配置
    ├── manifest.json           # 应用配置
    ├── App.vue                 # 根组件
    └── pages/
        ├── index/              # 首页
        │   └── index.vue
        ├── knowledge/          # 知识库模块
        │   ├── search.vue      # 知识搜索
        │   ├── disease-list.vue    # 疾病列表
        │   ├── disease-detail.vue  # 疾病详情
        │   ├── medicine-list.vue   # 药品列表
        │   ├── medicine-detail.vue # 药品详情
        │   ├── case-list.vue       # 案例列表
        │   └── case-detail.vue     # 案例详情
        └── statistics/       # 统计模块
            └── index.vue         # 统计主页
```

## 功能特性

### 1. 知识库系统
- **智能搜索**: 支持症状描述搜索，结果按相关性排序
- **关键词高亮**: 搜索结果高亮显示匹配关键词
- **疾病百科**: 详细的疾病信息，包括症状、治疗方案、病因、预防措施
- **药品说明书**: 完整的药品信息，包括适应症、用法用量、不良反应、禁忌
- **案例分享**: 实际诊疗案例分享，支持按宠物类型筛选

### 2. 数据统计
- **接诊量统计**: 今日/本周/本月/总接诊量
- **常见疾病排行**: Top疾病统计，关联治疗方案
- **药品使用分析**: 药品使用频率统计
- **Excel导出**: 支持统计数据导出为Excel文件

### 3. 适老化设计
- **长辈模式**: 字体放大，界面简化
- **语音输入**: 支持语音输入搜索症状
- **简洁布局**: 大按钮、高对比度设计

## 快速开始

### 后端启动
1. 确保已安装 JDK 11+ 和 Maven
2. 确保 Redis 服务已启动（可选，不影响基础功能）
3. 进入 backend 目录：
   ```bash
   cd backend
   ```
4. 编译并运行：
   ```bash
   mvn spring-boot:run
   ```
5. 访问地址：
   - 后端服务: http://localhost:8080
   - Swagger文档: http://localhost:8080/swagger-ui.html
   - H2数据库控制台: http://localhost:8080/h2-console

### 前端启动
1. 确保已安装 HBuilderX 或使用 UniApp 命令行工具
2. 使用 HBuilderX 打开 frontend 目录
3. 选择运行到浏览器或手机端

## API接口

### 知识库接口
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/knowledge/search | 搜索知识库 |
| GET | /api/knowledge/disease/list | 获取疾病列表 |
| GET | /api/knowledge/disease/{id} | 获取疾病详情 |
| GET | /api/knowledge/medicine/list | 获取药品列表 |
| GET | /api/knowledge/medicine/{id} | 获取药品详情 |
| GET | /api/knowledge/case/list | 获取案例列表 |
| GET | /api/knowledge/case/{id} | 获取案例详情 |

### 统计接口
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/statistics | 获取统计数据 |
| GET | /api/statistics/export | 导出Excel |

## 测试数据

系统启动时会自动初始化测试数据，包括：
- 4位测试医生
- 8种常见宠物疾病
- 6种常用药品
- 4个典型案例
- 30天的接诊记录

## 开发说明

### 后端开发
- 新增实体类在 `entity` 包下
- 数据访问接口在 `repository` 包下
- 业务逻辑在 `service` 包下
- API接口在 `controller` 包下

### 前端开发
- 新增页面在 `pages.json` 中注册
- 全局样式在 `App.vue` 中定义
- 长辈模式通过 `elderMode` 状态控制样式

## 注意事项

1. **Redis配置**: 默认连接 localhost:6379，如无Redis可正常使用，但缓存功能不可用
2. **H2数据库**: 内存数据库，重启后数据会重置，生产环境建议改用MySQL
3. **跨域配置**: 默认允许所有来源跨域访问，生产环境请限制域名
4. **语音识别**: 前端模拟了语音输入功能，实际应用需集成语音识别SDK

## License

MIT License
