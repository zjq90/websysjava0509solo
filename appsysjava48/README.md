# 心理咨询系统

一个完整的心理咨询管理系统，包含后端SpringBoot API和前端uniapp移动端应用。

## 技术栈

### 后端
- **SpringBoot 2.7.18** - 核心框架
- **Spring Data JPA** - 数据持久化
- **H2 Database** - 嵌入式数据库
- **Redis** - 缓存支持
- **SpringFox Swagger 3.0** - API文档
- **Lombok** - 简化代码

### 前端
- **uniapp (Vue 2)** - 跨端开发框架
- **SCSS** - 样式预处理

## 项目结构

```
appsysjava48/
├── backend/                    # 后端项目
│   ├── src/main/java/com/psyconsult/
│   │   ├── PsyConsultApplication.java    # 启动类
│   │   ├── config/                       # 配置类
│   │   │   ├── SwaggerConfig.java
│   │   │   ├── RedisConfig.java
│   │   │   └── DataInitializer.java      # 数据初始化
│   │   ├── entity/                       # 实体类
│   │   │   ├── User.java
│   │   │   ├── Counselor.java
│   │   │   ├── Schedule.java
│   │   │   ├── Appointment.java
│   │   │   ├── ConsultationRecord.java
│   │   │   ├── CrisisAlert.java
│   │   │   ├── CaseTag.java
│   │   │   ├── Course.java
│   │   │   ├── Supervision.java
│   │   │   └── EmergencyContact.java
│   │   ├── repository/                   # 数据访问层
│   │   ├── service/                      # 业务逻辑层
│   │   │   └── SensitiveWordService.java # 敏感词检测
│   │   └── controller/                   # 控制器层
│   │       ├── ScheduleController.java
│   │       ├── ConsultationRecordController.java
│   │       ├── CrisisAlertController.java
│   │       ├── AppointmentController.java
│   │       └── CounselorController.java
│   └── src/main/resources/
│       └── application.yml               # 配置文件
│
└── frontend/                     # 前端uniapp项目
    ├── pages/                          # 页面目录
    │   ├── index/index.vue              # 首页
    │   ├── schedule/schedule.vue        # 日程管理
    │   ├── records/records.vue          # 咨询记录列表
    │   ├── record-detail/record-detail.vue  # 记录详情/编辑
    │   ├── crisis/crisis.vue            # 危机预警
    │   ├── counselors/counselors.vue    # 咨询师列表
    │   ├── appointment/appointment.vue  # 预约咨询
    │   ├── courses/courses.vue          # 专业课程
    │   ├── supervision/supervision.vue  # 督导预约
    │   └── my/my.vue                    # 个人中心
    ├── utils/
    │   └── request.js                    # 请求工具
    └── pages.json                         # 页面配置
```

## 核心功能

### 📅 日程管理
- 可视化排班表
- 支持设置可咨询时段
- 冲突检测：避免同一时段被多人预约
- 支持暂停接单

### 📝 咨询记录与案例管理
- 结构化记录咨询过程（用户主诉、干预措施、后续建议）
- 案例标签分类（抑郁、焦虑、恐慌等）
- 情绪评分记录
- 风险等级评估

### 🚨 危机预警与干预
- **敏感词监测**：自动检测"自杀"、"想死"、"自残"等关键词
- 自动触发系统预警，通知督导
- 支持紧急联系人设置（需用户授权）
- 预警状态管理（待处理/处理中/已解决）

### 📚 专业发展支持
- 接入教育课程（认知行为疗法培训等）
- 督导预约系统：与资深咨询师一对一案例讨论
- 课程分类管理

### 👴 适老化设计
- **长辈模式**：字体放大、界面简化
- 响应式布局，适配手机、平板
- 简洁直观的操作流程

## 数据库设计

### 核心表结构
- **sys_user** - 用户表
- **counselor** - 咨询师表（包含资质、专长、是否可督导）
- **schedule** - 排班表（日期、时间段、最大预约数）
- **appointment** - 预约表（用户、咨询师、时间、主诉）
- **consultation_record** - 咨询记录表（结构化记录、标签、风险等级）
- **crisis_alert** - 危机预警表（触发词、内容、状态、处理结果）
- **case_tag** - 案例标签表
- **course** - 课程表
- **supervision** - 督导预约表
- **emergency_contact** - 紧急联系人表

## API文档

启动后端后访问：
- **Swagger UI**: http://localhost:8080/swagger-ui/
- **H2 Console**: http://localhost:8080/h2-console

### 主要API接口

| 模块 | 接口 | 说明 |
|------|------|------|
| 咨询师 | GET /api/counselor | 获取咨询师列表 |
| 咨询师 | GET /api/counselor/senior | 获取资深咨询师（督导） |
| 日程 | GET /api/schedule/counselor/{id}/date/{date} | 获取指定日期排班 |
| 日程 | POST /api/schedule | 创建排班 |
| 预约 | GET /api/appointment/counselor/{id} | 获取咨询师预约 |
| 预约 | POST /api/appointment | 创建预约 |
| 咨询记录 | GET /api/record/counselor/{id} | 获取咨询师的咨询记录 |
| 咨询记录 | POST /api/record | 创建咨询记录（自动检测敏感词） |
| 危机预警 | GET /api/crisis | 获取所有预警 |
| 危机预警 | PUT /api/crisis/{id}/handle | 处理预警 |

## 快速启动

### 后端启动
```bash
cd backend
# 使用Maven编译运行
mvn spring-boot:run
```

或直接运行主类 `com.psyconsult.PsyConsultApplication`

### 前端运行
```bash
cd frontend
npm install
npm run dev:h5
```

## 测试数据

系统启动时会自动初始化测试数据，包括：
- 示例用户（张小明、李小红）
- 示例咨询师（王医生、李医生等）
- 常用案例标签（抑郁、焦虑、恐慌等）
- 专业课程（CBT入门、抑郁症干预等）
- 一周的排班数据

## 敏感词检测

系统内置危机敏感词检测，包含以下关键词：
- 自杀、想死、不想活、结束生命、活不下去
- 自残、自伤、自杀倾向、自我伤害
- 跳楼、割腕、喝农药、上吊、烧炭

当咨询记录中包含这些关键词时，系统会自动创建危机预警。

## 特色亮点

1. **模块化设计**：清晰的代码分层，易于维护和扩展
2. **危机预警机制**：自动敏感词检测，保障咨询安全
3. **适老化支持**：长辈模式，字体放大、界面简化
4. **响应式设计**：适配手机、平板多种设备
5. **完善的API文档**：Swagger在线文档，便于接口对接
6. **数据初始化**：内置测试数据，快速上手体验

## 开发说明

### 后端开发
- 修改 `application.yml` 配置数据库和Redis连接
- 新增实体类需添加 `@Entity` 和 `@Table` 注解
- Repository继承 `JpaRepository` 获得基础CRUD功能

### 前端开发
- 页面统一放在 `pages/` 目录下
- 在 `pages.json` 中注册新页面
- 使用 `request.js` 统一处理API请求

## License

MIT
