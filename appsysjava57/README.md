# 宠物医院问诊系统

## 项目概述

本项目是一个完整的宠物医院问诊系统，包含后端SpringBoot API和前端UniApp应用。

## 功能特性

### 用户端功能
- **宠物档案管理**：多宠物管理、疫苗记录、到期提醒、病历历史
- **在线问诊**：分步引导、AI紧急症状识别、24小时医院推荐、图文/视频问诊
- **预约挂号**：科室选择、医生排班查看、预约改签/取消、双重提醒
- **药品购买**：处方药/非处方药分类、处方验证、用药提醒、多设备同步

### 设计特色
- 响应式设计：适配手机、平板
- 适老化设计：长辈模式（字体放大、界面简化、按钮增大）
- 美观界面：统一绿色主题，现代化UI设计

## 技术栈

### 后端
- SpringBoot 2.7.x
- Spring Data JPA
- H2内存数据库
- Redis缓存
- Swagger OpenAPI 3.0
- WebSocket

### 前端
- UniApp (Vue3)
- 支持H5、小程序、App多端运行

## 项目结构

```
appsysjava57/
├── backend/                    # 后端SpringBoot项目
│   ├── src/main/java/com/pethospital/
│   │   ├── config/            # 配置类
│   │   ├── controller/        # API接口层
│   │   ├── service/           # 业务逻辑层
│   │   ├── repository/        # 数据访问层
│   │   ├── entity/            # 实体类
│   │   └── PetHospitalApplication.java
│   └── pom.xml
├── frontend/                   # 前端UniApp项目
│   ├── pages/                  # 页面文件
│   │   ├── index/index.vue    # 首页
│   │   ├── pet/list.vue       # 宠物列表
│   │   ├── consultation/index.vue  # 在线问诊
│   │   ├── appointment/index.vue   # 预约挂号
│   │   ├── medicine/index.vue      # 药品购买
│   │   └── mine/index.vue     # 个人中心
│   ├── pages.json              # 页面路由配置
│   ├── manifest.json           # 应用配置
│   └── App.vue                 # 入口文件
└── README.md
```

## 已修复的问题

### 后端问题
1. **类型转换错误**：Doctor实体类的`consultationFee`字段从`Double`改为`BigDecimal`
2. **DataInitializer初始化数据**：修复了`BigDecimal.valueOf()`的参数类型问题

### 前端问题
1. **pages.json配置**：移除了不存在的页面引用（pet/detail、consultation/symptom、medicine/reminder）
2. **tabBar图标**：移除了不存在的图片引用，使用纯文字tabBar
3. **App.vue样式**：移除了不存在的CSS文件引用
4. **首页banner**：移除了不存在的图片引用，使用渐变色背景

## 运行说明

### 后端启动

#### 前置要求
- JDK 11+
- Maven 3.6+
- Redis（可选，项目会自动处理连接失败的情况）

#### 启动步骤
1. 进入后端目录：
   ```bash
   cd backend
   ```

2. 编译项目：
   ```bash
   mvn clean compile
   ```

3. 启动应用：
   ```bash
   mvn spring-boot:run
   ```

4. 访问地址：
   - API接口：http://localhost:8080/api
   - Swagger文档：http://localhost:8080/api/swagger-ui.html
   - H2数据库控制台：http://localhost:8080/api/h2-console

#### H2数据库登录信息
- JDBC URL: `jdbc:h2:mem:pethospital`
- 用户名: `sa`
- 密码: (空)

### 前端启动

#### 前置要求
- HBuilderX 3.0+

#### 启动步骤
1. 使用HBuilderX打开`frontend`目录

2. 运行到浏览器：
   - 点击菜单：运行 → 运行到浏览器 → Chrome（或其他浏览器）

3. 运行到小程序：
   - 点击菜单：运行 → 运行到小程序模拟器 → 微信开发者工具

4. 运行到手机：
   - 点击菜单：运行 → 运行到手机或模拟器 → 选择对应平台

## 测试数据

系统启动时会自动生成以下测试数据：

### 用户
- 用户名：testuser
- 密码：123456
- 姓名：张三
- 手机号：13800138000

### 宠物
- 金毛豆豆（狂犬疫苗20天后到期）
- 英短咪咪（狂犬疫苗20天后到期）

### 医生（5位）
- 王医生（内科主任医师）
- 李医生（外科副主任医师）
- 张医生（皮肤科主治医师）
- 刘医生（牙科主任医师）
- 陈医生（眼科副主任医师）

### 药品（8种）
- 体内驱虫片、体外驱虫滴剂、综合营养膏等

### 医院
- 宠物急救中心（24小时急诊）
- 爱宠医院（24小时急诊）
- 阳光宠物诊所

## API接口说明

主要API接口：

### 宠物管理
- `GET /api/pets/list` - 获取宠物列表
- `POST /api/pets/add` - 添加宠物
- `PUT /api/pets/update` - 更新宠物信息
- `DELETE /api/pets/delete/{id}` - 删除宠物
- `GET /api/pets/{id}/vaccine-records` - 获取疫苗记录

### 在线问诊
- `POST /api/consultations/check-emergency` - 检查紧急症状
- `GET /api/consultations/hospitals/24h` - 获取24小时医院列表
- `POST /api/consultations/create` - 创建问诊
- `GET /api/consultations/list` - 获取问诊记录

### 预约挂号
- `GET /api/appointments/doctors` - 获取医生列表
- `GET /api/appointments/doctors/{doctorId}/schedules` - 获取医生排班
- `POST /api/appointments/book` - 预约挂号
- `GET /api/appointments/list` - 获取预约列表
- `PUT /api/appointments/cancel/{id}` - 取消预约

### 药品购买
- `GET /api/medicines/categories` - 获取药品分类
- `GET /api/medicines/list` - 获取药品列表
- `GET /api/medicines/prescriptions` - 获取处方列表
- `POST /api/medicines/order` - 创建订单

完整的API文档请访问Swagger UI。

## 注意事项

1. **Redis连接**：如果本地没有安装Redis，项目仍能正常启动，只是缓存功能会受影响
2. **H2数据库**：内存数据库，重启后数据会重置
3. **前端图片**：项目中使用emoji代替图片资源，确保可以正常运行
4. **长辈模式**：在"我的"页面可以切换长辈模式，字体和按钮会放大

## 开发说明

### 后端开发
- 新增实体类请放在`com.pethospital.entity`包
- 新增Repository请放在`com.pethospital.repository`包
- 新增Service请放在`com.pethospital.service`包
- 新增Controller请放在`com.pethospital.controller`包

### 前端开发
- 新增页面请在`pages`目录创建
- 在`pages.json`中配置页面路由
- 公共样式请在`App.vue`中定义

## 许可证

本项目仅供学习和开发使用。
