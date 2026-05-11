# 医疗预约APP系统

一个功能完整的医疗预约挂号系统，包含用户身份管理、实名认证、就诊人管理、智能导诊、科室导航、医生管理、预约挂号、数据加密、权限控制、日志审计等功能。

## 项目架构

### 后端技术栈
- **框架**: Spring Boot 2.7.18
- **数据库**: H2 内存数据库
- **ORM**: Spring Data JPA
- **安全**: Spring Security + JWT
- **接口文档**: Swagger 2.9.2
- **加密**: 国密风格SM4算法
- **日志**: SLF4J + Logback

### 前端技术栈
- **框架**: UniApp (Vue3)
- **适配**: H5 + 微信小程序 + APP
- **HTTP**: 统一请求拦截器
- **状态**: LocalStorage本地存储

## 目录结构

```
appsysjava28/
├── backend/                          # 后端项目
│   ├── pom.xml                       # Maven配置
│   └── src/main/
│       ├── java/com/medical/appointment/
│       │   ├── MedicalAppointmentApplication.java    # 主入口
│       │   ├── common/               # 公共模块
│       │   ├── config/               # 配置类
│       │   ├── controller/           # 控制器
│       │   ├── entity/               # 实体类
│       │   ├── repository/           # 数据访问层
│       │   ├── security/             # 安全模块
│       │   └── service/              # 业务服务层
│       └── resources/
│           └── application.yml       # 应用配置
│
└── frontend/                         # 前端项目
    ├── pages.json                    # 页面路由配置
    ├── manifest.json                 # 应用配置
    ├── package.json                  # 依赖配置
    ├── main.js                       # 入口文件
    ├── App.vue                       # 根组件
    ├── api/                          # API接口封装
    ├── pages/                        # 页面文件
    ├── utils/                        # 工具类
    └── static/                       # 静态资源
```

## 功能模块

### 1. 用户身份管理
- 手机号验证码登录/注册
- 微信一键授权登录（模拟）
- JWT无状态认证

### 2. 实名认证
- 对接国家政务服务平台API（模拟）
- 身份证+人脸识别核验
- 认证状态管理

### 3. 就诊人管理
- 添加多位家庭成员
- 独立实名认证
- 主账号代为挂号
- 默认就诊人设置

### 4. 智能导诊与科室导航
- AI症状自查（关键词匹配）
- 科室树结构展示
- 医生详情页（职称、专长、评价）
- 排班号源展示

### 5. 预约挂号
- 创建预约
- 支付流程（模拟）
- 取消预约
- 评价功能

### 6. 数据安全
- 敏感信息国密风格加密
- 身份证/手机号脱敏显示
- 权限访问控制

### 7. 日志审计
- 登录日志
- 预约操作日志
- 保留不少于6个月

### 8. 适老化设计
- 长辈模式（字体放大）
- 语音输入症状（模拟）
- 界面简化

## 数据库设计

### 主要表
- `user` - 用户表
- `patient` - 就诊人表
- `department` - 科室表
- `doctor` - 医生表
- `doctor_schedule` - 医生排班表
- `appointment` - 预约表
- `review` - 评价表
- `audit_log` - 审计日志表
- `sms_code` - 短信验证码表
- `symptom_knowledge` - 症状知识库

## 启动方式

### 后端启动

#### 方式一：使用Maven命令（需安装Maven）
```bash
cd backend
mvn spring-boot:run
```

#### 方式二：使用IDE
1. 用IntelliJ IDEA或Eclipse导入项目
2. 等待Maven依赖下载完成
3. 运行 `MedicalAppointmentApplication.java`

### 前端启动

#### 方式一：使用HBuilderX（推荐）
1. 下载安装HBuilderX
2. 打开 `frontend` 目录
3. 选择"运行" → "运行到浏览器" → "Chrome"

#### 方式二：使用命令行
```bash
cd frontend
npm install
npm run serve
```

## 访问地址

- **后端API**: http://localhost:8080/api
- **Swagger文档**: http://localhost:8080/api/swagger-ui.html
- **H2控制台**: http://localhost:8080/api/h2-console
  - JDBC URL: `jdbc:h2:file:./data/medicaldb`
  - 用户: `sa`
  - 密码: (空)

## 测试指南

### 1. 启动后端服务
启动后，系统会自动初始化测试数据：
- 10个科室（内科、外科、妇产科等）
- 13名医生
- 各医生排班数据
- 症状知识库

### 2. 测试登录流程
1. 打开Swagger文档: http://localhost:8080/api/swagger-ui.html
2. 调用 `POST /api/auth/sms-code` 发送验证码
   - Request Body: `{"phone": "13800138000"}`
   - 返回结果中会包含验证码（测试环境）

3. 调用 `POST /api/auth/login/sms` 登录
   - Request Body: `{"phone": "13800138000", "code": "返回的验证码"}`
   - 返回JWT token

4. 在Swagger页面右上角点击"Authorize"，输入: `Bearer {token}`

### 3. 测试API接口

#### 用户相关
- `GET /api/user/profile` - 获取用户信息
- `POST /api/user/elderly-mode` - 切换长辈模式

#### 就诊人管理
- `GET /api/patient/list` - 获取就诊人列表
- `POST /api/patient/add` - 添加就诊人
- `PUT /api/patient/{id}` - 更新就诊人
- `DELETE /api/patient/{id}` - 删除就诊人
- `POST /api/patient/{id}/default` - 设为默认

#### 科室导航
- `GET /api/department/tree` - 获取科室树
- `GET /api/department/{id}/doctors` - 获取科室医生列表
- `GET /api/department/search?keyword=内科` - 搜索科室

#### 医生管理
- `GET /api/doctor/list` - 获取医生列表
- `GET /api/doctor/{id}` - 获取医生详情
- `GET /api/doctor/search?keyword=张` - 搜索医生

#### 预约挂号
- `POST /api/appointment/create` - 创建预约
- `GET /api/appointment/list?status=PAID` - 获取预约列表
- `GET /api/appointment/{id}` - 获取预约详情
- `POST /api/appointment/{id}/pay` - 支付预约
- `POST /api/appointment/{id}/cancel` - 取消预约

#### 智能导诊
- `POST /api/diagnosis` - 症状诊断
  - Request Body: `{"symptoms": "头痛 发热"}`

#### 审计日志
- `GET /api/audit/logs` - 获取操作日志

### 4. 前端测试
1. 启动后端服务
2. 用HBuilderX打开前端项目
3. 运行到H5浏览器
4. 按以下流程测试：
   - 登录页：输入手机号13800138000 → 点击获取验证码 → 输入验证码（后端控制台会打印）→ 登录
   - 首页：查看快捷入口、热门医生、我的预约
   - 科室导航：展开科室树、搜索科室、查看医生
   - 医生详情：选择排班、选择就诊人、预约挂号
   - 预约管理：查看预约、支付、取消
   - 就诊人管理：添加、编辑、删除、设为默认
   - 智能导诊：输入症状、查看推荐科室和医生
   - 个人中心：切换长辈模式、查看统计、退出登录

### 5. 测试数据
系统启动时自动创建：

#### 科室
- 内科 → 心血管内科、呼吸内科、消化内科、神经内科
- 外科 → 普外科、骨科、神经外科
- 妇产科
- 儿科
- 眼科
- 耳鼻喉科
- 口腔科
- 皮肤科

#### 医生示例
- 张明 - 主任医师 - 心血管内科
- 李华 - 副主任医师 - 呼吸内科
- 王芳 - 主治医师 - 妇产科
- 等13位医生

#### 排班
- 每位医生未来7天有排班
- 上午、下午、晚上三个时段
- 每个时段10-20个号源

## API响应格式

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {}
}
```

- code: 状态码（200成功，400参数错误，401未认证，500服务器错误）
- message: 提示信息
- data: 业务数据

## 安全特性

1. **JWT认证**: 无状态Token认证
2. **密码加密**: BCrypt加密存储
3. **敏感数据加密**: 国密SM4风格加密身份证号等
4. **脱敏显示**: 手机号、身份证号部分隐藏
5. **权限控制**: 基于用户ID验证越权操作
6. **CORS配置**: 允许跨域请求
7. **审计日志**: 关键操作自动记录

## 配置说明

### 后端配置 (application.yml)
```yaml
server:
  port: 8080
  servlet:
    context-path: /api

jwt:
  secret: MedicalAppointmentSystemSecretKeyForJwtToken2024
  expiration: 86400000  # 24小时

app:
  encryption:
    sm4-key: 0123456789ABCDEFFEDCBA9876543210
  audit:
    retention-days: 180  # 日志保留180天
```

### 前端配置 (utils/request.js)
```javascript
const BASE_URL = 'http://localhost:8080/api'
```

## 注意事项

1. 本项目使用H2内存数据库，重启后数据会重置
2. 所有第三方服务（短信、微信、实名认证）均为模拟实现
3. 支付功能为模拟，真实支付需要对接微信/支付宝
4. 前端tabbar图标需要用户自行添加到static/tabbar目录

## 技术亮点

1. **清晰的分层架构**: Entity/Repository/Service/Controller四层结构
2. **完整的测试数据**: 启动时自动初始化科室、医生、排班
3. **国密风格加密**: 敏感数据使用SM4算法保护
4. **完整的日志审计**: 关键操作自动记录，便于追溯
5. **适老化设计**: 长辈模式一键切换，字体放大
6. **响应式设计**: 适配手机、平板、PC端
7. **完善的错误处理**: 网络异常、支付失败等场景友好提示
