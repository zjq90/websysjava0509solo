# 智慧医院预约挂号系统

## 项目简介

基于SpringBoot + UniApp开发的智慧医院预约挂号系统，支持在线预约挂号、多条件筛选、在线支付、医保结算等功能。

## 技术栈

### 后端
- Spring Boot 2.7.18
- Spring Data JPA
- Spring Security + JWT
- H2 嵌入式数据库
- SpringDoc OpenAPI (Swagger)
- 国密SM4算法 (Bouncy Castle)
- Hutool 工具库

### 前端
- UniApp (Vue3)
- Vuex 4.1.0
- Sass
- Vite

## 项目结构

```
appsysjava29/
├── backend/                    # 后端项目
│   ├── src/main/java/com/hospital/appointment/
│   │   ├── AppointmentApplication.java    # 主启动类
│   │   ├── annotation/        # 自定义注解 (操作日志)
│   │   ├── aspect/            # AOP切面 (日志审计)
│   │   ├── common/            # 通用类 (Result、异常处理)
│   │   ├── config/            # 配置类 (Security、Swagger)
│   │   ├── controller/        # 控制器层
│   │   ├── entity/            # 实体类
│   │   ├── repository/        # 数据访问层
│   │   ├── scheduler/         # 定时任务 (号源释放)
│   │   ├── security/          # 安全模块 (JWT、SM4加密)
│   │   └── service/           # 业务逻辑层
│   └── src/main/resources/
│       ├── application.yml    # 应用配置
│       └── db/
│           ├── schema.sql     # 数据库表结构
│           └── data.sql       # 测试数据
├── frontend/                  # 前端项目
│   ├── src/
│   │   ├── App.vue            # 应用入口
│   │   ├── main.js            # 主入口
│   │   ├── pages/             # 页面
│   │   │   ├── index/         # 首页
│   │   │   ├── login/         # 登录
│   │   │   ├── register/      # 注册
│   │   │   ├── doctor/        # 医生列表、详情
│   │   │   ├── appointment/   # 预约、支付、列表
│   │   │   ├── patient/       # 就诊人管理
│   │   │   └── user/          # 个人中心
│   │   ├── store/             # Vuex状态管理
│   │   └── utils/             # 工具类 (请求、API封装)
│   ├── package.json
│   ├── pages.json
│   └── manifest.json
├── start-backend.bat          # 后端启动脚本 (Windows)
└── start-frontend.bat         # 前端启动脚本 (Windows)
```

## 核心功能

### 预约挂号流程
- ✅ 号源动态同步
- ✅ 分时段预约 (15分钟粒度)
- ✅ 挂号锁号机制 (锁定10分钟，超时自动释放)
- ✅ 多条件筛选 (职称、时间、号源类型)

### 在线支付与医保结算
- ✅ 支持微信支付、支付宝、银联云闪付
- ✅ 医保脱卡结算 (60%报销模拟)
- ✅ 支付失败自动退款并释放号源

### 安全功能
- ✅ 国密SM4算法加密敏感信息 (身份证、手机号、支付数据)
- ✅ JWT身份认证
- ✅ 权限管理 (用户/医生/管理员角色)
- ✅ AOP操作日志审计 (保留不少于6个月)

### UI特性
- ✅ 响应式设计 (适配手机、平板、PC端)
- ✅ 容错提示 (网络异常、支付失败等场景)
- ✅ 适老化设计 (长辈模式：字体放大、界面简化、语音输入)

## 快速开始

### 环境要求
- JDK 1.8+
- Maven 3.6+
- Node.js 16+

### 启动后端

**方式一：使用启动脚本**
```bash
# Windows
start-backend.bat
```

**方式二：手动启动**
```bash
cd backend
mvn clean compile -DskipTests
mvn spring-boot:run
```

启动成功后访问：
- Swagger文档: http://localhost:8080/swagger-ui.html
- H2控制台: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:hospital`
  - Username: `sa`
  - Password: (空)

### 启动前端

**方式一：使用启动脚本**
```bash
# Windows
start-frontend.bat
```

**方式二：手动启动**
```bash
cd frontend
npm install
npm run dev:h5
```

### 测试账号

| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin | 123456 | 管理员 |
| user1 | 123456 | 普通用户 |
| user2 | 123456 | 普通用户 |

## 数据库表结构

### 核心表
1. **sys_user** - 用户表
2. **sys_role** - 角色表
3. **department** - 科室表
4. **doctor** - 医生表
5. **schedule** - 排班表
6. **schedule_slot** - 号源时段表 (15分钟粒度)
7. **appointment** - 预约表
8. **payment** - 支付记录表
9. **patient** - 就诊人表
10. **operation_log** - 操作日志表

## API接口

### 认证接口
- `POST /api/auth/login` - 登录
- `POST /api/auth/register` - 注册
- `GET /api/auth/me` - 获取当前用户信息
- `PUT /api/auth/me` - 更新用户信息
- `POST /api/auth/elder-mode` - 切换长辈模式

### 医生/科室接口
- `GET /api/departments` - 获取科室列表
- `GET /api/doctors` - 获取医生列表 (支持筛选)
- `GET /api/doctors/{id}` - 获取医生详情

### 排班号源接口
- `GET /api/schedules` - 获取排班列表
- `GET /api/schedules/dates` - 获取可预约日期
- `GET /api/slots/{scheduleId}` - 获取号源时段
- `POST /api/slots/{id}/lock` - 锁定号源
- `POST /api/slots/{id}/unlock` - 释放号源

### 预约接口
- `POST /api/appointments` - 创建预约
- `GET /api/appointments` - 获取预约列表
- `GET /api/appointments/{id}` - 获取预约详情
- `POST /api/appointments/{id}/cancel` - 取消预约
- `POST /api/appointments/{id}/pay` - 支付预约

### 就诊人接口
- `GET /api/patients` - 获取就诊人列表
- `POST /api/patients` - 添加就诊人
- `PUT /api/patients/{id}` - 更新就诊人
- `DELETE /api/patients/{id}` - 删除就诊人
- `POST /api/patients/{id}/default` - 设为默认

## 测试功能

### 后端测试
使用Swagger UI进行接口测试：
1. 访问 http://localhost:8080/swagger-ui.html
2. 调用 `/api/auth/login` 获取Token
3. 点击右上角 "Authorize" 按钮，输入 `Bearer {token}`
4. 即可测试所有需要认证的接口

### 前端测试
1. 启动前后端服务
2. 使用测试账号登录 (user1 / 123456)
3. 测试流程：
   - 首页 → 预约挂号 → 选择医生 → 选择时段 → 确认预约 → 支付
   - 我的预约 → 查看/取消预约
   - 就诊人管理 → 添加/编辑/删除就诊人
   - 个人中心 → 切换长辈模式

## 定时任务

- **号源释放任务**: 每分钟执行一次，自动释放超过10分钟未支付的锁定号源

## 注意事项

1. 首次启动后端会自动初始化数据库和测试数据
2. 长辈模式状态保存在本地存储和数据库
3. 敏感信息在数据库中使用国密SM4加密存储
4. 操作日志自动记录关键操作，可用于审计
