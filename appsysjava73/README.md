# 大学生社团管理系统

## 项目简介

本项目是一个完整的大学生社团管理系统，包含后端API服务和前端UniApp移动端应用，支持社团浏览与发现、招新管理、成员管理、内部管理和数据统计等功能。

## 技术栈

### 后端
- **框架**: Spring Boot 2.7.18
- **数据库**: H2 内存数据库
- **缓存**: Redis
- **API文档**: Swagger-OpenAPI (springdoc)
- **认证**: JWT
- **ORM**: Spring Data JPA
- **构建工具**: Maven

### 前端
- **框架**: UniApp (Vue.js)
- **状态管理**: Vuex
- **样式**: SCSS
- **适配**: 手机、平板响应式设计

## 项目结构

```
appsysjava73/
├── backend/                    # 后端项目
│   ├── src/main/java/com/club/
│   │   ├── common/            # 通用类（Result、ResultCode等）
│   │   ├── config/            # 配置类（Swagger、Redis、CORS等）
│   │   ├── controller/        # 控制器层
│   │   ├── dto/               # 数据传输对象
│   │   ├── entity/            # 实体类
│   │   │   └── enums/         # 枚举类
│   │   ├── exception/         # 异常处理
│   │   ├── interceptor/       # 拦截器
│   │   ├── repository/        # 数据访问层
│   │   ├── service/           # 业务逻辑层
│   │   └── utils/             # 工具类
│   ├── src/main/resources/
│   │   └── application.yml    # 应用配置
│   └── pom.xml                # Maven配置
└── frontend/                   # 前端项目
    ├── pages/                  # 页面
    ├── api/                    # API接口
    ├── store/                  # Vuex状态管理
    ├── utils/                  # 工具类
    ├── common/                 # 通用样式
    ├── pages.json              # 页面路由配置
    └── manifest.json           # 应用配置
```

## 功能模块

### 1. 社团浏览与发现
- 按分类筛选（学术科技、文化艺术、体育竞技、公益实践、创新创业等）
- 社团主页展示：logo、介绍、负责人、成员数量、活动相册、获奖荣誉、联系方式
- 关注/取消关注社团，接收活动通知
- 跨校社团浏览关注（多校版本）

### 2. 社团招新管理
- 开启/关闭招新通道，设置名额、要求、截止时间
- 在线提交入团申请，上传个人简介，选择意向部门
- 在线审核申请（通过/驳回），发送审核留言
- 实时展示招新进度和剩余名额

### 3. 社团成员管理
- 按部门分组管理成员
- 设置成员权限：普通成员、部门负责人、副社长、社长
- 添加/移除成员，转让社长权限
- 查看成员活动参与记录，标记活跃/不活跃成员
- 批量导出成员名单

### 4. 社团内部管理
- 社团资料网盘存储（章程、活动策划、过往资料）
- 社团大事记功能，记录发展重要节点
- 历任社长信息管理
- 专属讨论区：发布帖子、交流讨论、点赞评论

### 5. 数据统计
- 成员增长趋势图表
- 活动参与率统计
- 部门成员分布
- 月度/年度活动报告生成

## 快速开始

### 环境要求

- **JDK**: 1.8 或更高版本
- **Maven**: 3.6 或更高版本
- **Redis**: 5.0 或更高版本（可选，默认本地localhost:6379）
- **Node.js**: 12 或更高版本（前端开发）
- **HBuilderX**: 最新版本（UniApp开发运行）

### 后端启动

#### 1. 启动Redis（可选）
如果Redis未运行，应用会启动成功但缓存功能不可用。

#### 2. 编译并启动后端
```bash
cd backend

# Windows
mvnw.cmd clean install
mvnw.cmd spring-boot:run

# 或使用已安装的Maven
mvn clean install
mvn spring-boot:run
```

#### 3. 访问服务
- **应用地址**: http://localhost:8080
- **Swagger文档**: http://localhost:8080/swagger-ui.html
- **H2数据库控制台**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:clubdb`
  - 用户名: `admin`
  - 密码: `admin123`

### 常见问题 - Maven下载失败

如果遇到SSL证书问题导致Maven依赖下载失败，请尝试以下方法：

1. **使用国内Maven镜像**，在`~/.m2/settings.xml`中添加：
```xml
<mirrors>
    <mirror>
        <id>aliyunmaven</id>
        <mirrorOf>*</mirrorOf>
        <name>阿里云公共仓库</name>
        <url>https://maven.aliyun.com/repository/public</url>
    </mirror>
</mirrors>
```

2. **升级JDK版本**到11或更高版本（包含更新的根证书）

3. **导入Maven Central证书**到JDK信任库

### 前端启动

#### 1. 使用HBuilderX打开项目
1. 打开HBuilderX
2. 文件 → 打开目录 → 选择 `frontend` 文件夹

#### 2. 运行项目
- **浏览器运行**: 运行 → 运行到浏览器 → Chrome
- **真机运行**: 运行 → 运行到手机或模拟器 → 选择设备
- **小程序运行**: 运行 → 运行到小程序模拟器 → 微信开发者工具

#### 3. 配置说明
前端API请求地址默认为 `http://localhost:8080`，如需修改请编辑 `frontend/utils/request.js`。

## 测试账号

系统启动时会自动初始化测试数据，可使用以下账号登录：

### 管理员/社长账号
- **用户名**: `admin`
- **密码**: `123456`

### 普通用户账号
- **用户名**: `student001`
- **密码**: `123456`

## API接口说明

### 认证接口
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/register` - 用户注册
- `GET /api/auth/me` - 获取当前用户信息

### 社团接口
- `GET /api/clubs` - 获取社团列表
- `GET /api/clubs/{id}` - 获取社团详情
- `GET /api/clubs/categories` - 获取社团分类
- `GET /api/clubs/hot` - 获取热门社团
- `GET /api/clubs/my-managed` - 获取我管理的社团

### 数据统计接口
- `GET /api/clubs/{clubId}/statistics` - 获取基本统计数据
- `GET /api/clubs/{clubId}/statistics/member-growth` - 获取成员增长趋势
- `GET /api/clubs/{clubId}/statistics/activity-participation` - 获取活动参与率
- `GET /api/clubs/{clubId}/statistics/department-distribution` - 获取部门分布
- `GET /api/clubs/{clubId}/statistics/monthly-report` - 生成月度报告

更多接口请查看Swagger文档。

## 数据库设计

### 核心实体
- **User**: 用户表
- **School**: 学校表
- **Club**: 社团表
- **ClubMember**: 社团成员表
- **ClubDepartment**: 社团部门表
- **ClubActivity**: 社团活动表
- **ClubRecruit**: 招新信息表
- **ClubRecruitApply**: 招新申请表
- **ClubPost**: 社团帖子表
- **ClubFile**: 社团文件表
- **ClubMilestone**: 社团大事记表
- **ClubNotification**: 通知表

所有实体均包含软删除字段（deleted）和通用字段（id, createTime, updateTime）。

## 开发规范

### 后端
- 采用分层架构：Controller → Service → Repository
- 使用DTO进行数据传输，不直接暴露Entity
- 统一异常处理，返回标准Result格式
- 使用JWT进行身份认证
- 重要操作记录详细日志

### 前端
- 采用响应式设计，适配手机和平板
- 使用Vuex管理全局状态
- 统一API请求封装，自动处理Token和错误
- 组件化开发，复用通用组件

## 许可证

MIT License
