# 历史记录查询筛选功能 - Bug修复说明

## 问题描述
在历史记录查询菜单中，选择多种组合条件后点击查询，数据列表没有变化，筛选功能无法使用。

## 问题根因分析

### 1. 前端函数名不匹配（主要原因）

**文件：`frontend/src/views/Records.vue`**

| 位置 | 模板调用 | 实际函数名 | 状态 |
|------|----------|------------|------|
| 查询按钮 | `queryRecords` | `queryRecordsData` | ❌ 不匹配 |
| 分页size-change | `queryRecords` | `queryRecordsData` | ❌ 不匹配 |
| 分页current-change | `queryRecords` | `queryRecordsData` | ❌ 不匹配 |
| 删除按钮 | `deleteRecord` | `deleteRecordData` | ❌ 不匹配 |

**文件：`frontend/src/views/Cameras.vue`**

| 位置 | 模板调用 | 实际函数名 | 状态 |
|------|----------|------------|------|
| 删除按钮 | `deleteCamera` | `deleteCameraData` | ❌ 不匹配 |

### 2. 日期反序列化问题

前端传递的日期格式可能是多种格式（ISO格式、带空格的格式等），后端`LocalDateTime`反序列化可能失败。

### 3. Blob响应处理问题

导出CSV/Excel时，后端返回Blob类型数据，但前端响应拦截器没有正确处理Blob类型。

## 修复内容

### 修复1：修正Records.vue中的函数调用

- ✅ 查询按钮 `@click="queryRecords"` → `@click="queryRecordsData"`
- ✅ 分页 `@size-change="queryRecords"` → `@size-change="queryRecordsData"`
- ✅ 分页 `@current-change="queryRecords"` → `@current-change="queryRecordsData"`
- ✅ 删除按钮 `@click="deleteRecord(row)"` → `@click="deleteRecordData(row)"`

### 修复2：修正Cameras.vue中的函数调用

- ✅ 删除按钮 `@click="deleteCamera(row)"` → `@click="deleteCameraData(row)"`

### 修复3：添加Jackson日期反序列化配置

**新建文件：`backend/src/main/java/com/plate/config/JacksonConfig.java`**

- 支持多种日期格式自动解析：
  - ISO_DATE_TIME (yyyy-MM-dd'T'HH:mm:ss)
  - yyyy-MM-dd HH:mm:ss
  - yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
- 配置时间默认值
- 忽略未知属性

### 修复4：修复Blob响应处理

**修改文件：`frontend/src/utils/request.js`**

- ✅ 添加Blob类型判断：`response.config.responseType === 'blob'` 时直接返回数据
- ✅ 延长超时时间：10s → 30s（导出大文件需要更长时间）

## 验证步骤

### 后端验证
1. 启动Spring Boot应用
2. 访问Swagger文档：http://localhost:8080/swagger-ui.html
3. 测试`POST /api/records/query`接口，传入不同筛选条件

### 前端验证
1. 启动前端开发服务器
2. 进入"历史记录查询"页面
3. 测试以下筛选组合：
   - 车牌号模糊查询
   - 选择特定摄像头
   - 选择异常类型
   - 选择时间范围
   - 多条件组合查询
4. 验证分页功能
5. 验证删除功能
6. 验证导出CSV/Excel功能

## 代码变更清单

| 文件 | 变更类型 | 说明 |
|------|----------|------|
| `frontend/src/views/Records.vue` | 修改 | 修正4处函数调用 |
| `frontend/src/views/Cameras.vue` | 修改 | 修正1处函数调用 |
| `frontend/src/utils/request.js` | 修改 | 添加Blob处理、延长超时 |
| `backend/src/main/java/com/plate/config/JacksonConfig.java` | 新增 | 日期反序列化配置 |
