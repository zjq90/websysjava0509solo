# 宠物问诊系统 - Pet Clinic

一个完整的宠物健康管理与问诊平台，包含Spring Boot后端和Vue3前端。

## 项目概述

### 技术栈

**后端:**
- Spring Boot 2.7.18
- Spring Data JPA
- H2 内存数据库
- Swagger/OpenAPI 3.0 接口文档
- Lombok

**前端:**
- Vue 3.4 + Vite 5
- Vue Router 4
- Pinia 状态管理
- Element Plus UI 组件库
- Axios HTTP 客户端

## 项目结构

```
appsysjava58/
├── backend/                    # Spring Boot 后端
│   ├── src/main/java/com/petclinic/
│   │   ├── config/            # 配置类
│   │   ├── controller/        # 控制器
│   │   ├── dto/               # 数据传输对象
│   │   ├── entity/            # 实体类
│   │   ├── repository/        # 数据访问层
│   │   └── PetClinicApplication.java
│   └── pom.xml
│
├── frontend-vue/              # Vue3 前端（推荐）
│   ├── src/
│   │   ├── views/            # 页面组件
│   │   ├── router/           # 路由配置
│   │   ├── store/            # 状态管理
│   │   ├── api/              # API 请求
│   │   ├── App.vue
│   │   └── main.js
│   ├── index.html
│   ├── vite.config.js
│   └── package.json
│
├── frontend/                   # UniApp 前端（需HBuilderX）
│   ├── src/
│   │   ├── pages/            # 页面
│   │   ├── App.vue
│   │   └── main.js
│   ├── pages.json
│   ├── manifest.json
│   └── vite.config.js
│
└── README.md
```

## 核心功能

### 用户端功能

#### 1. 健康管理 🩺
- 驱虫/体检提醒
- 倒计时显示
- 已完成绿色标记
- 历史记录查询

#### 2. 饮食建议 🍽️
- 根据品种/体重生成个性化建议
- 引用权威营养学文献
- 每日卡路里、蛋白质、脂肪配比建议
- 喂食频率指导

#### 3. 运动监测 🏃
- 步数统计、距离、卡路里消耗
- 每日目标达成追踪
- 智能项圈数据接入支持
- 活动时长统计

#### 4. 宠物圈 📱
- 经验分享（图文）
- 内容审核（敏感词过滤）
- 专家问答（付费咨询）
- 帖子浏览、点赞、评论

#### 5. 附近医院 🏥
- 周边宠物医院列表
- 用户评分系统（满分5分）
- 详细医院信息（地址、电话、营业时间）
- 24小时急诊医院标识
- 一键呼叫导航

### 特殊功能

#### 响应式设计
- 适配手机、平板、桌面多种设备
- 自适应布局，流畅体验

#### 长辈模式 👓
- 字体放大30%，更易阅读
- 界面简化，只显示核心功能
- 语音输入支持
- 一键呼叫快捷功能

## 快速开始

### 后端启动

1. **环境要求:** JDK 11+、Maven 3.6+

2. **进入后端目录:**
```bash
cd backend
```

3. **编译运行:**
```bash
mvn spring-boot:run
```

4. **访问地址:**
   - 应用接口: http://localhost:8080/api
   - Swagger文档: http://localhost:8080/api/swagger-ui.html
   - H2控制台: http://localhost:8080/api/h2-console

### 前端启动（Vue3版本，推荐）

1. **环境要求:** Node.js 16+

2. **进入前端目录:**
```bash
cd frontend-vue
```

3. **安装依赖:**
```bash
npm install
```

4. **启动开发服务:**
```bash
npm run dev
```

5. **访问地址:** http://localhost:3000

### 前端启动（UniApp版本）

1. **下载HBuilderX:** https://www.dcloud.io/hbuilderx.html

2. **打开项目:** 选择 `frontend` 目录

3. **运行到浏览器:** 运行 → 运行到浏览器 → Chrome

## API 接口示例

### 健康管理
- `GET /api/health/cards/{petId}` - 获取宠物健康卡片
- `POST /api/health/deworming` - 添加驱虫记录
- `GET /api/health/deworming/history/{petId}` - 获取驱虫历史

### 饮食建议
- `POST /api/diet/generate/{petId}` - 生成饮食建议
- `GET /api/diet/history/{petId}` - 获取建议历史

### 运动监测
- `GET /api/exercise/stats/{petId}` - 获取今日统计
- `POST /api/exercise/sync` - 同步第三方设备数据
- `GET /api/exercise/devices` - 获取支持设备列表

### 宠物圈
- `POST /api/circle/post` - 发布帖子
- `GET /api/circle/posts` - 获取已审核帖子列表
- `POST /api/circle/like/{postId}` - 点赞帖子

### 医院查询
- `GET /api/hospital/nearby` - 获取附近医院
- `GET /api/hospital/{id}` - 获取医院详情
- `POST /api/hospital/rating` - 提交评分

## 数据库设计

### 核心数据表
- `users` - 用户表
- `pets` - 宠物表
- `deworming_records` - 驱虫记录表
- `checkup_records` - 体检记录表
- `diet_suggestions` - 饮食建议表
- `exercise_data` - 运动数据表
- `posts` - 帖子表
- `pet_hospitals` - 宠物医院表
- `hospital_ratings` - 医院评分表
- `expert_questions` - 专家问答表

## 测试数据

项目启动时自动初始化测试数据，包括：
- 3个测试用户（普通用户、专家、管理员）
- 2只测试宠物（金毛犬、英短猫）
- 驱虫/体检历史记录
- 饮食建议示例
- 7天运动数据
- 3家测试宠物医院
- 示例帖子和评分

## 开发说明

### 后端开发规范
- RESTful API 设计
- 统一返回格式: `{ code: 200, message: "成功", data: {} }`
- 全局异常处理
- Swagger 接口文档自动生成

### 前端开发规范
- Vue 3 Composition API
- 组件化开发
- 响应式布局设计
- 长辈模式样式适配

## 注意事项

1. **H2数据库:** 内存数据库，重启后数据重置。如需持久化，可修改 `application.properties` 配置
2. **跨域配置:** 后端已配置CORS支持前端本地开发
3. **端口说明:** 后端默认8080，前端默认3000，如需修改请分别配置

## License

Apache License 2.0
