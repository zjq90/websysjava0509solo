# 问题修复说明

## 修复的问题

### 1. 后端Hibernate HQL查询语法错误

**问题描述：**
```
org.hibernate.QueryException: No data type for node: org.hibernate.hql.internal.ast.tree.MethodNode
```

**原因：**
在Repository的@Query注解中，直接使用 `LIKE %?1%` 语法是不正确的。HQL要求使用 `CONCAT` 函数来拼接通配符。

**修复方案：**

**DiseaseRepository.java:**
```java
// 修复前
@Query("SELECT d FROM Disease d WHERE d.status = ?2 AND (d.name LIKE %?1% OR d.symptoms LIKE %?1% OR d.keywords LIKE %?1%)")

// 修复后
@Query("SELECT d FROM Disease d WHERE d.status = ?2 AND (d.name LIKE CONCAT('%', ?1, '%') OR d.symptoms LIKE CONCAT('%', ?1, '%') OR d.keywords LIKE CONCAT('%', ?1, '%'))")
```

**MedicineRepository.java 和 CaseRepository.java 也进行了同样的修复。**

---

### 2. ConsultationRepository FUNCTION函数兼容性问题

**问题描述：**
使用 `FUNCTION('DATE', ...)` 在某些Hibernate版本或数据库中可能不兼容。

**修复方案：**
移除FUNCTION函数调用，直接使用实体字段，并在Service层进行日期格式化处理。

```java
// 修复前
@Query("SELECT FUNCTION('DATE', c.consultationDate) as date, COUNT(c) as count " +
       "FROM Consultation c WHERE c.consultationDate BETWEEN ?1 AND ?2 AND c.status = 1 " +
       "GROUP BY FUNCTION('DATE', c.consultationDate) ORDER BY date")

// 修复后
@Query("SELECT c.consultationDate, COUNT(c) " +
       "FROM Consultation c WHERE c.consultationDate BETWEEN ?1 AND ?2 AND c.status = 1 " +
       "GROUP BY c.consultationDate ORDER BY c.consultationDate")
```

同时更新了StatisticsService中的日期聚合逻辑，在Java代码中进行按日期分组。

---

### 3. 前端UniApp配置不完整

**问题描述：**
缺少UniApp运行必需的核心文件，导致HBuilderX无法正常运行项目。

**修复方案：**
添加以下必需文件：

| 文件 | 作用 |
|------|------|
| `main.js` | Vue应用入口文件，创建SSR应用实例 |
| `package.json` | 项目依赖和配置，包含UniApp所需的所有依赖 |
| `uni.scss` | 全局SCSS变量文件，定义统一样式变量 |
| `index.html` | H5运行时入口HTML文件 |
| `vite.config.js` | Vite构建配置文件，包含开发服务器和代理配置 |

---

### 4. 前端TabBar图标路径问题

**问题描述：**
pages.json中配置的tabBar图标路径不存在，会导致编译警告。

**修复方案：**
移除图标配置，改用emoji文字作为tabBar显示：

```json
// 修复前
{
  "pagePath": "pages/index/index",
  "text": "首页",
  "iconPath": "static/tabbar/home.png",
  "selectedIconPath": "static/tabbar/home-active.png"
}

// 修复后
{
  "pagePath": "pages/index/index",
  "text": "🏠 首页"
}
```

---

## 项目文件结构

### 后端 (SpringBoot)
```
backend/
├── pom.xml                          # Maven依赖配置
└── src/main/java/com/pethospital/
    ├── PetHospitalApplication.java # 启动类
    ├── common/                      # 公共模块
    │   ├── Result.java
    │   ├── BusinessException.java
    │   └── GlobalExceptionHandler.java
    ├── config/                      # 配置类
    │   ├── OpenApiConfig.java
    │   ├── RedisConfig.java
    │   ├── CorsConfig.java
    │   └── DataInitializer.java
    ├── entity/                      # 实体类
    │   ├── Doctor.java
    │   ├── Disease.java
    │   ├── Medicine.java
    │   ├── Case.java
    │   ├── Consultation.java
    │   └── KnowledgeUpdate.java
    ├── repository/                  # 数据访问层
    │   ├── DoctorRepository.java
    │   ├── DiseaseRepository.java    # ✅ 已修复HQL
    │   ├── MedicineRepository.java   # ✅ 已修复HQL
    │   ├── CaseRepository.java       # ✅ 已修复HQL
    │   ├── ConsultationRepository.java # ✅ 已修复FUNCTION
    │   └── KnowledgeUpdateRepository.java
    ├── service/                     # 业务逻辑层
    │   ├── KnowledgeService.java
    │   └── StatisticsService.java  # ✅ 已修复日期聚合
    ├── controller/                  # 控制器
    │   ├── KnowledgeController.java
    │   └── StatisticsController.java
    └── dto/                        # 数据传输对象
        ├── SearchResultDTO.java
        └── StatisticsDTO.java
```

### 前端 (UniApp Vue3)
```
frontend/
├── pages.json                      # ✅ 已修复tabBar图标
├── manifest.json                   # 应用配置
├── App.vue                         # 根组件
├── main.js                       # ✅ 新增 - 应用入口
├── package.json                    # ✅ 新增 - 依赖配置
├── uni.scss                      # ✅ 新增 - 全局样式变量
├── index.html                    # ✅ 新增 - H5入口
├── vite.config.js                # ✅ 新增 - Vite配置
└── pages/
    ├── index/
    │   └── index.vue
    ├── knowledge/
    │   ├── search.vue
    │   ├── disease-list.vue
    │   ├── disease-detail.vue
    │   ├── medicine-list.vue
    │   ├── medicine-detail.vue
    │   ├── case-list.vue
    │   └── case-detail.vue
    └── statistics/
        └── index.vue
```

---

## 运行说明

### 后端运行
1. 确保安装了JDK 11+
2. 确保Redis服务已启动（可选，不影响基础功能）
3. 在backend目录执行：
   ```bash
   mvn spring-boot:run
   ```
4. 访问：
   - 后端API: http://localhost:8080
   - Swagger文档: http://localhost:8080/swagger-ui.html
   - H2控制台: http://localhost:8080/h2-console

### 前端运行
**方式一：使用HBuilderX（推荐）**
1. 打开HBuilderX
2. 文件 -> 打开目录 -> 选择frontend目录
3. 点击"运行" -> "运行到浏览器" -> 选择Chrome
4. 或点击"运行" -> "运行到内置终端"

**方式二：使用命令行（需要安装Node.js）**
```bash
cd frontend
npm install
npm run dev:h5
```

---

## 技术栈验证

### 后端
- ✅ SpringBoot 2.7.x
- ✅ Spring Data JPA (Hibernate)
- ✅ H2 内存数据库
- ✅ Redis 缓存
- ✅ SpringDoc OpenAPI (Swagger)
- ✅ EasyExcel 导出

### 前端
- ✅ UniApp (Vue 3)
- ✅ Vite 构建工具
- ✅ 响应式设计
- ✅ 长辈模式（字体放大）
- ✅ 模拟语音输入功能
