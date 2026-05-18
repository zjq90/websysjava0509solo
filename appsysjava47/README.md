# 心理咨询App系统

基于SpringBoot + H2 + Redis + Swagger-OpenAPI 后端 和 UniApp 前端的心理咨询应用系统。

## 🌟 功能特性

### 咨询者端

#### 🔐 匿名注册与认证
- 支持用户名/手机号/邮箱注册
- 可选匿名模式，保护用户隐私
- 实名认证功能（需用户授权）
- 隐私设置：昵称、头像自定义
- 咨询记录默认加密存储

#### 🧠 智能匹配咨询师
- 心理测评问卷（PHQ-9抑郁量表、GAD-7焦虑量表）
- 系统根据测评结果推荐匹配度高的咨询师
- 支持按领域、语言、价格筛选
- 咨询师评分排序功能

#### 💬 多样化咨询方式
- **即时文字聊天**：支持发送图片/表情
- **语音/视频通话**：需预约时段，支持虚拟背景模糊（保护隐私）
- **紧急求助通道**：一键呼叫24小时危机干预热线及附近医疗机构

#### 📊 咨询记录管理
- 自动生成咨询日志（需用户授权）
- 按时间、主题分类查看
- 情绪趋势分析：通过NLP技术提取关键词，生成情绪变化曲线图

#### 📚 心理健康资源库
- 冥想音频、心理科普文章
- 自助练习工具（如呼吸训练）
- 社区互动：匿名分享心得，需审核内容避免传播负面信息

### 🎨 适老化设计
- 长辈模式：字体放大、界面简化
- 支持语音输入
- 操作流程简化

## 🏗️ 技术架构

### 后端技术栈
- **框架**：SpringBoot 2.7.x
- **数据库**：H2（内存数据库，开发环境）
- **缓存**：Redis
- **API文档**：Swagger-OpenAPI
- **安全认证**：Spring Security + JWT
- **ORM**：Spring Data JPA

### 前端技术栈
- **框架**：UniApp（Vue3）
- **跨平台**：支持H5、小程序、App

## 📁 项目结构

```
appsysjava47/
├── backend/                          # 后端项目
│   ├── src/
│   │   └── main/
│   │       ├── java/com/psyconsult/
│   │       │   ├── PsyConsultApplication.java      # 启动类
│   │       │   ├── config/                          # 配置类
│   │       │   │   ├── OpenApiConfig.java           # Swagger配置
│   │       │   │   ├── RedisConfig.java             # Redis配置
│   │       │   │   ├── SecurityConfig.java          # 安全配置
│   │       │   │   └── DataInitializer.java         # 数据初始化
│   │       │   ├── entity/                          # 实体类
│   │       │   │   ├── User.java
│   │       │   │   ├── Counselor.java
│   │       │   │   ├── Assessment.java
│   │       │   │   ├── Consultation.java
│   │       │   │   ├── ChatMessage.java
│   │       │   │   ├── Appointment.java
│   │       │   │   ├── EmergencyContact.java
│   │       │   │   ├── Resource.java
│   │       │   │   ├── CommunityPost.java
│   │       │   │   └── EmotionRecord.java
│   │       │   ├── repository/                      # 数据访问层
│   │       │   ├── service/                         # 业务逻辑层
│   │       │   │   ├── AuthService.java
│   │       │   │   ├── CounselorService.java
│   │       │   │   ├── AssessmentService.java
│   │       │   │   ├── ConsultationService.java
│   │       │   │   └── CustomUserDetailsService.java
│   │       │   ├── controller/                      # 控制器层
│   │       │   │   ├── AuthController.java
│   │       │   │   ├── CounselorController.java
│   │       │   │   ├── AssessmentController.java
│   │       │   │   ├── ConsultationController.java
│   │       │   │   ├── EmergencyController.java
│   │       │   │   └── ResourceController.java
│   │       │   ├── dto/                             # 数据传输对象
│   │       │   │   ├── ApiResponse.java
│   │       │   │   ├── LoginRequest.java
│   │       │   │   └── RegisterRequest.java
│   │       │   ├── security/                        # 安全相关
│   │       │   │   ├── JwtTokenProvider.java
│   │       │   │   └── JwtAuthenticationFilter.java
│   │       │   └── exception/                       # 异常处理
│   │       │       └── GlobalExceptionHandler.java
│   │       └── resources/
│   │           └── application.yml                   # 配置文件
│   └── pom.xml                                       # Maven配置
└── frontend/                                         # 前端项目
    ├── manifest.json                                 # UniApp配置
    ├── pages.json                                    # 页面路由配置
    ├── App.vue                                       # 应用入口
    ├── main.js                                       # 主文件
    └── pages/                                        # 页面目录
        ├── index/
        │   └── index.vue                             # 首页
        ├── login/
        │   └── login.vue                             # 登录页
        ├── register/
        │   └── register.vue                          # 注册页
        ├── counselors/
        │   ├── list.vue                              # 咨询师列表
        │   └── detail.vue                            # 咨询师详情
        ├── assessment/
        │   ├── phq9.vue                              # 抑郁测评
        │   └── gad7.vue                              # 焦虑测评
        ├── chat/
        │   └── chat.vue                              # 聊天页面
        ├── consult/
        │   └── list.vue                              # 咨询记录
        ├── resources/
        │   └── list.vue                              # 资源列表
        ├── emergency/
        │   └── emergency.vue                         # 紧急求助
        └── user/
            ├── profile.vue                           # 个人中心
            └── settings.vue                          # 设置页面
```

## 🚀 快速开始

### 环境要求
- JDK 11+
- Maven 3.6+
- Redis（可选，用于缓存）
- Node.js（前端开发）
- HBuilderX（UniApp开发工具）

### 后端启动

1. **进入后端目录**
   ```bash
   cd backend
   ```

2. **编译项目**
   ```bash
   mvn clean install
   ```

3. **启动应用**
   ```bash
   mvn spring-boot:run
   ```

4. **访问地址**
   - 应用地址：http://localhost:8080
   - H2控制台：http://localhost:8080/h2-console
     - JDBC URL：`jdbc:h2:mem:psyconsultdb`
     - 用户名：`sa`
     - 密码：（空）
   - Swagger文档：http://localhost:8080/swagger-ui.html

### 前端启动

1. **使用HBuilderX打开项目**
   - 打开HBuilderX
   - 文件 → 打开目录 → 选择 `frontend` 文件夹

2. **运行到浏览器**
   - 点击工具栏「运行」→「运行到浏览器」→ 选择Chrome

3. **运行到小程序**
   - 配置微信开发者工具路径
   - 点击「运行」→「运行到小程序模拟器」→「微信开发者工具」

## 📋 测试数据

系统启动时会自动创建以下测试数据：

### 测试用户
- **用户名**：`user1`
- **密码**：`123456`
- **特性**：已实名认证，昵称"小明"

- **用户名**：`user2`
- **密码**：`123456`
- **特性**：匿名模式，已启用长辈模式

### 测试咨询师
| 姓名 | 专长 | 经验 | 评分 |
|------|------|------|------|
| 张医生 | 抑郁、焦虑、情绪管理 | 15年 | ⭐ 4.8 |
| 李咨询师 | 职场压力、人际关系、个人成长 | 8年 | ⭐ 4.6 |
| 王教授 | 亲子关系、青少年心理、学业压力 | 22年 | ⭐ 4.9 |

### 紧急热线
- 全国心理援助热线：400-161-9995
- 北京心理危机研究中心：010-82951332
- 希望24热线：400-161-9995

## 📚 API接口说明

### 认证接口
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/auth/register | 用户注册 |
| POST | /api/auth/login | 用户登录 |

### 咨询师接口
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/counselors | 获取咨询师列表 |
| GET | /api/counselors/recommended | 获取推荐咨询师 |
| GET | /api/counselors/{id} | 获取咨询师详情 |
| GET | /api/counselors/filter | 筛选咨询师 |

### 测评接口
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/assessments | 提交测评 |
| GET | /api/assessments | 获取我的测评 |
| GET | /api/assessments/{id} | 获取测评详情 |

### 咨询接口
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/consultations | 创建咨询 |
| GET | /api/consultations | 获取我的咨询 |
| GET | /api/consultations/{id} | 获取咨询详情 |
| POST | /api/consultations/{id}/messages | 发送消息 |
| GET | /api/consultations/{id}/messages | 获取聊天记录 |
| POST | /api/consultations/{id}/end | 结束咨询 |

## 🔧 配置说明

### 后端配置 (application.yml)

```yaml
server:
  port: 8080

spring:
  # Redis配置
  data:
    redis:
      host: localhost
      port: 6379
      database: 0

  # H2数据库配置
  datasource:
    url: jdbc:h2:mem:psyconsultdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    username: sa
    password:

jwt:
  secret: PsyConsultSecretKeyForJWTTokenGeneration2024VeryLongSecretKey
  expiration: 86400000  # 24小时
```

### 前端配置 (main.js)

```javascript
app.config.globalProperties.$api = 'http://localhost:8080/api'
```

## 🎯 核心功能演示

### 1. 用户注册流程
1. 打开首页，点击"立即登录"
2. 选择"注册账号"
3. 填写用户名、密码、可选手机号/邮箱
4. 选择是否开启"匿名模式"
5. 完成注册，自动登录

### 2. 心理测评流程
1. 登录后，在首页选择"抑郁测评"或"焦虑测评"
2. 回答问卷题目（PHQ-9共9题，GAD-7共7题）
3. 提交测评，查看测评结果和建议
4. 系统自动推荐匹配的咨询师

### 3. 咨询流程
1. 在咨询师列表选择心仪的咨询师
2. 查看咨询师详情和专长
3. 选择咨询方式（文字/语音/视频）
4. 预约或立即开始咨询
5. 咨询结束后可查看咨询记录和情绪分析

### 4. 紧急求助
1. 在首页点击"紧急求助"
2. 浏览可用的心理援助热线
3. 一键拨打危机干预热线
4. 使用自助呼吸练习缓解情绪

## 🧪 测试指南

### 后端测试
```bash
# 运行单元测试
mvn test

# 生成测试报告
mvn surefire-report:report
```

### 接口测试
使用Swagger UI进行接口测试：
1. 访问 http://localhost:8080/swagger-ui.html
2. 调用 `/api/auth/login` 登录获取token
3. 点击「Authorize」按钮，输入 `Bearer {token}`
4. 测试其他需要认证的接口

### 前端测试
1. 使用不同设备测试响应式布局
2. 测试长辈模式开关效果
3. 验证各页面跳转和数据加载
4. 测试离线状态下的用户体验

## 🔐 安全特性

- **密码加密**：使用BCrypt加密存储
- **JWT认证**：无状态token认证
- **匿名模式**：隐藏真实用户信息
- **咨询加密**：敏感聊天记录加密存储
- **隐私设置**：用户可控制个人信息可见范围

## 📈 性能优化

- **Redis缓存**：热门咨询师、资源列表缓存
- **数据库索引**：常用查询字段建立索引
- **分页查询**：列表数据支持分页加载
- **图片压缩**：上传图片自动压缩处理

## 🤝 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📄 许可证

本项目仅供学习和研究使用，请勿用于商业用途。

---

## 📞 技术支持

如遇到问题，请：
1. 查看项目Issues
2. 查阅API文档
3. 检查Redis和H2连接状态

---

**祝愿每一位用户都能获得专业的心理支持，守护心理健康！** 🌻
