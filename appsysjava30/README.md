# 医疗挂号系统

一个完整的医疗挂号预约系统，包含后端API和前端App。

## 项目结构

```
appsysjava30/
├── backend/                    # 后端SpringBoot项目
│   ├── pom.xml                 # Maven依赖配置
│   └── src/main/
│       ├── java/com/medical/registration/
│       │   ├── aop/            # AOP切面（日志审计）
│       │   ├── common/         # 公共组件（返回结果、异常处理）
│       │   ├── controller/     # 控制器层
│       │   ├── dto/            # 数据传输对象
│       │   ├── entity/         # 实体类（JPA）
│       │   ├── init/           # 数据初始化
│       │   ├── repository/     # 数据访问层
│       │   ├── security/       # 安全配置（JWT、权限）
│       │   ├── service/        # 业务逻辑层
│       │   └── util/           # 工具类
│       └── resources/
│           └── application.yml # 应用配置
└── frontend/                   # 前端uniapp项目
    ├── pages/                  # 页面
    │   ├── login/              # 登录页
    │   ├── register/           # 注册页
    │   ├── index/              # 首页
    │   ├── registration/       # 挂号预约页
    │   ├── record/             # 预约记录列表页
    │   ├── detail/             # 挂号详情页
    │   ├── profile/            # 个人中心页
    │   └── elderly/            # 长辈模式页
    ├── utils/                  # 工具类
    │   ├── api.js              # API接口封装
    │   ├── request.js          # HTTP请求封装
    │   └── date.js             # 日期工具
    ├── App.vue                 # 根组件
    ├── main.js                 # 入口文件
    ├── pages.json              # 页面配置
    └── manifest.json           # 应用配置
```

## 技术栈

### 后端
- **框架**: SpringBoot 2.7.x
- **数据库**: H2（内存数据库）
- **ORM**: Spring Data JPA
- **安全**: Spring Security + JWT
- **API文档**: Swagger OpenAPI 3.0
- **加密**: 国密SM4算法
- **其他**: Hutool工具库、ZXing二维码生成

### 前端
- **框架**: uniapp（Vue2）
- **兼容**: H5、微信小程序、App

## 功能特性

### 1. 挂号记录与状态追踪
- 预约记录列表，按时间倒序展示
- 状态包括：预约成功、已取消、已就诊、退费中
- 高亮显示即将就诊的预约（今日待就诊）
- 电子挂号单，含二维码供现场核验

### 2. 取消与改约机制
- 就诊前一日17:00前可自助取消
- 不支持直接改期，需先取消再重新挂号
- 退费原路返回，微信/支付宝1-3个工作日到账

### 3. 安全特性
- 敏感信息（身份证、手机号）采用国密SM4算法加密存储
- JWT Token认证机制
- 权限管理，防止越权操作
- 关键操作日志审计，保留不少于6个月

### 4. 用户体验
- 响应式设计，适配手机、平板、PC
- 网络异常、支付失败等场景友好提示
- 长辈模式：字体放大、界面简化、语音输入

## 数据库设计

### 核心表
| 表名 | 说明 |
|------|------|
| t_user | 用户表 |
| t_role | 角色表 |
| t_user_role | 用户角色关联表 |
| t_department | 科室表 |
| t_doctor | 医生表 |
| t_schedule | 号源表 |
| t_registration | 挂号记录表 |
| t_payment | 支付记录表 |
| t_operation_log | 操作日志表 |

## 启动说明

### 后端启动

#### 方式一：IDE启动
1. 使用IDEA或Eclipse导入`backend`目录作为Maven项目
2. 等待依赖下载完成
3. 运行 `RegistrationApplication.java` 主类
4. 访问 http://localhost:8080/api/swagger-ui.html 查看API文档

#### 方式二：命令行启动
```bash
cd backend
mvn spring-boot:run
```

#### 方式三：打包启动
```bash
cd backend
mvn clean package
java -jar target/registration-system-1.0.0.jar
```

### 前端启动

#### 方式一：HBuilderX启动
1. 打开HBuilderX
2. 导入`frontend`目录
3. 选择运行 → 运行到浏览器 → Chrome（H5）
4. 或运行 → 运行到小程序模拟器 → 微信开发者工具

#### 方式二：命令行启动（需安装@dcloudio/uni-cli）
```bash
cd frontend
npm install
npm run dev:h5
```

## 测试账号

系统初始化时自动创建以下测试账号：

| 用户名 | 密码 | 说明 |
|--------|------|------|
| zhangshan | 123456 | 测试用户张三 |
| lisi | 123456 | 测试用户李四 |

## API接口

### 认证相关
- `POST /api/auth/login` - 登录
- `POST /api/auth/register` - 注册
- `GET /api/auth/me` - 获取当前用户信息
- `PUT /api/auth/me` - 更新用户信息

### 号源相关
- `GET /api/schedules/departments` - 获取科室列表
- `GET /api/schedules/departments/{deptCode}/doctors` - 获取科室医生
- `GET /api/schedules/departments/{deptCode}/date/{date}` - 按科室日期查号源
- `GET /api/schedules/doctors/{doctorId}` - 按医生查号源

### 挂号相关
- `POST /api/registrations` - 创建预约
- `POST /api/registrations/pay` - 支付挂号费
- `POST /api/registrations/{no}/cancel` - 取消预约
- `GET /api/registrations` - 挂号记录列表
- `GET /api/registrations/{no}` - 挂号详情
- `GET /api/registrations/today` - 今日待就诊

### 测试辅助
- `GET /api/test/token` - 获取测试Token
- `GET /api/test/health` - 健康检查
- `POST /api/test/refund/{no}` - 模拟退费完成
- `POST /api/test/visit-complete/{no}` - 模拟就诊完成

## 功能测试指南

### 测试流程示例

1. **登录**
   - 使用测试账号 zhangshan / 123456 登录
   - 验证返回Token和用户信息

2. **预约挂号**
   - 选择科室 → 选择医生 → 选择日期和时间段
   - 填写症状描述 → 提交预约
   - 选择支付方式 → 完成支付
   - 验证挂号单生成和二维码

3. **查看预约记录**
   - 查看"我的预约"列表
   - 验证状态标签（预约成功、今日待就诊等）
   - 点击查看详情

4. **取消预约**
   - 选择一条可取消的预约
   - 点击取消按钮并确认
   - 验证状态变为"已取消"
   - 如已支付，状态变为"退费中"

5. **测试辅助功能**
   - 使用详情页的"模拟退费完成"验证退费流程
   - 使用"模拟就诊完成"验证已就诊状态

6. **长辈模式**
   - 进入长辈模式页面
   - 验证字体放大、界面简化
   - 测试语音输入功能

## 安全说明

1. 所有敏感字段（手机号、身份证号）使用国密SM4算法加密存储
2. API接口需携带JWT Token才能访问（登录、注册、测试接口除外）
3. 用户只能操作自己的数据，系统会校验用户ID匹配
4. 所有关键操作（登录、挂号、支付、取消）都会记录操作日志
5. 密码使用BCrypt加密存储

## 注意事项

1. H2为内存数据库，重启后数据会重置
2. 如需持久化数据，可修改application.yml切换到MySQL
3. 生产环境请修改JWT密钥和SM4密钥
4. 支付功能为模拟实现，实际项目需对接真实支付接口
