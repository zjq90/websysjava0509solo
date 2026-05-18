# HBuilderX 运行指南

## 🔧 已修复的问题

### 1. Sass Loader 配置错误
**问题原因**：`vue.config.js` 中使用了新版本 sass-loader 的 `additionalData` 选项，但 HBuilderX 内置的 sass-loader 是旧版本，使用 `prependData`。

**修复方法**：已移除 `css.loaderOptions.scss` 配置，让 HBuilderX 使用默认配置。

### 2. 依赖版本冲突
**问题原因**：`package.json` 中手动指定了 `sass` 和 `sass-loader` 版本，与 HBuilderX 内置版本冲突。

**修复方法**：已移除这两个依赖，使用 HBuilderX 内置的编译工具。

### 3. 多余的 babel 配置
**问题原因**：项目根目录下有自定义 `.babelrc`，可能与 HBuilderX 内置配置冲突。

**修复方法**：已删除 `.babelrc`，使用 HBuilderX 默认的 babel 配置。

---

## 🚀 正确运行步骤

### 步骤 1：清理旧的依赖（非常重要！）

**方法 A：手动删除（推荐）**
1. 关闭 HBuilderX
2. 删除 `frontend` 目录下的 `node_modules` 文件夹
3. 删除 `package-lock.json` 或 `yarn.lock` 文件

**方法 B：使用命令（如果有命令行环境）**
```bash
cd frontend
rm -rf node_modules
rm -f package-lock.json
```

### 步骤 2：使用 HBuilderX 打开项目

1. 启动 **HBuilderX**
2. 菜单：文件 → 打开目录
3. 选择 `frontend` 文件夹（**注意：不是整个 appsysjava48 文件夹！**）

### 步骤 3：运行到 H5（推荐先测试）

1. 在 HBuilderX 左侧项目管理器中，右键点击 `frontend` 项目
2. 选择：运行 → 运行到浏览器 → Chrome（或其他浏览器）
3. 等待编译完成，会自动打开浏览器

### 步骤 4：运行到手机模拟器（可选）

1. 确保 Android Studio 或模拟器已启动
2. HBuilderX 菜单：运行 → 运行到手机或模拟器 → 运行到 Android App 基座
3. 首次运行需要下载基座，等待即可

---

## ❌ 常见错误及解决方法

### 错误 1：Sass Loader 相关错误
```
ValidationError: Invalid options object. Sass Loader has been initialized using an options object that does not match the API schema.
- options has an unknown property 'additionalData'
```

**解决方法**：
1. 关闭 HBuilderX
2. 删除 `frontend/node_modules`
3. 重新打开项目，HBuilderX 会自动重新安装依赖

### 错误 2：找不到模块 '@dcloudio/xxx'
**解决方法**：
1. 确保 HBuilderX 已安装 uni-app 插件
2. 菜单：工具 → 插件安装 → 检查是否安装了 `uni-app` 插件
3. 如果没有，安装后重启 HBuilderX

### 错误 3：页面空白或控制台报错
**解决方法**：
1. 按 F12 打开浏览器开发者工具
2. 查看 Console 标签页的具体错误
3. 常见原因：API 请求失败（后端服务未启动）

---

## 📦 项目结构说明

```
frontend/
├── pages/                    # 页面目录
│   ├── index/index.vue       # 首页
│   ├── schedule/schedule.vue # 日程管理
│   ├── records/records.vue   # 咨询记录
│   ├── crisis/crisis.vue     # 危机预警
│   ├── courses/courses.vue   # 专业课程
│   ├── supervision/supervision.vue # 督导预约
│   ├── counselors/counselors.vue # 咨询师列表
│   ├── appointment/appointment.vue # 预约咨询
│   └── my/my.vue             # 个人中心
├── utils/
│   └── request.js            # API 请求封装
├── App.vue                   # 应用入口
├── main.js                   # 主文件
├── manifest.json             # 应用配置
├── pages.json                # 路由配置
├── vue.config.js             # Vue 配置（已修复！）
└── package.json              # 依赖配置（已修复！）
```

---

## 🔌 后端连接配置

### 开发环境
- 前端默认请求地址：`http://localhost:8080/api`
- 请确保后端 Spring Boot 项目已启动在 8080 端口

### 修改 API 地址
如果后端端口不是 8080，请修改：
- 文件：`frontend/utils/request.js`
- 修改第 1 行：`const BASE_URL = 'http://你的IP:端口/api'`

---

## 💡 快速验证方案

如果 HBuilderX 运行仍然有问题，可以使用 **纯 HTML 版本**（无需任何依赖）：

1. 打开目录：`frontend-simple/`
2. **直接双击** `index.html` 在浏览器中打开
3. 无需编译，开箱即用！

这个简化版本包含了所有完整功能：
- ✅ 首页 + 快速入口
- ✅ 日程管理
- ✅ 咨询记录（含敏感词自动检测）
- ✅ 危机预警
- ✅ 专业课程
- ✅ 督导预约
- ✅ 长辈模式切换

---

## 📱 功能测试清单

启动后请测试以下功能：

### 1. 首页功能
- [ ] 切换「长辈模式」，字体和按钮应该变大
- [ ] 点击快速入口能跳转到对应页面
- [ ] 推荐咨询师列表正常显示

### 2. 咨询记录功能
- [ ] 能创建新的咨询记录
- [ ] 输入包含「自杀」「想死」等关键词时，能自动触发危机预警
- [ ] 预警能在危机预警页面看到

### 3. 日程管理
- [ ] 能查看排班列表
- [ ] 能点击时段进行预约

### 4. 督导预约
- [ ] 能选择督导
- [ ] 能填写预约信息并提交

---

## 🆘 获取帮助

如果仍然遇到问题：

1. 查看 HBuilderX 控制台的错误日志
2. 查看浏览器开发者工具的 Console 信息
3. 确认使用的是最新版 HBuilderX（建议 3.99+）
4. 先尝试简化版本 `frontend-simple/index.html` 验证功能

---

**祝使用愉快！** 🎉
