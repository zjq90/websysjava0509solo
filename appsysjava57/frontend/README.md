# 宠物医院 - 前端项目

## 项目说明

这是一个基于uni-app + Vue3开发的宠物医院问诊系统前端项目。

## 项目结构

```
frontend/
├── .hbuilderx/              # HBuilderX配置目录
│   └── launch.json          # 运行配置
├── pages/                   # 页面目录
│   ├── index/               # 首页
│   ├── pet/                 # 宠物管理页
│   ├── consultation/        # 在线问诊页
│   ├── appointment/         # 预约挂号页
│   ├── medicine/            # 药品购买页
│   └── mine/                # 个人中心页
├── static/                  # 静态资源目录
│   ├── css/                 # 样式文件
│   └── images/              # 图片资源
├── App.vue                  # 应用入口组件
├── main.js                  # 应用入口文件
├── manifest.json            # 应用配置文件
├── pages.json               # 页面路由配置
├── uni.scss                 # 全局样式变量
├── package.json             # 项目依赖配置
├── vite.config.js           # Vite配置文件
├── index.html               # H5入口页面
└── .project                 # HBuilderX项目识别文件
```

## HBuilderX运行步骤

### 方法一：直接运行（推荐，使用HBuilderX内置编译器）

1. **打开项目**
   - 启动HBuilderX
   - 点击菜单：文件 → 打开目录
   - 选择本 `frontend` 目录

2. **确认项目识别**
   - 项目图标应该显示为「uni-app」图标
   - 如果不是，请检查 `.project` 文件是否存在

3. **运行到浏览器**
   - 点击菜单：运行 → 运行到浏览器 → Chrome
   - 或点击工具栏的「运行」按钮，选择「运行到浏览器」

4. **运行到微信小程序**
   - 确保已安装微信开发者工具
   - 点击菜单：运行 → 运行到小程序模拟器 → 微信开发者工具
   - 首次运行需要配置微信开发者工具路径

### 方法二：CLI方式（需要Node.js环境）

```bash
# 安装依赖
npm install

# 运行到H5
npm run dev:h5

# 运行到微信小程序
npm run dev:mp-weixin
```

## 常见问题解决

### 问题1：HBuilderX无法识别为uni-app项目

**原因**：缺少必要的配置文件

**解决**：
- 确认 `.project` 文件存在于项目根目录
- 确认文件内容包含 `io.dcloud.uniapp.nature`
- 重启HBuilderX后重新打开项目

### 问题2：运行报错「找不到main.js」

**原因**：入口文件缺失

**解决**：
- 确认 `main.js` 文件存在于项目根目录
- 检查文件编码为UTF-8

### 问题3：tabBar图标不显示

**说明**：本项目使用纯文字tabBar，无需图标资源，因此不会报错。

### 问题4：页面样式不生效

**解决**：
- 检查 `<style>` 标签是否有 `scoped` 属性
- 确保CSS语法正确，rpx单位使用正确

### 问题5：运行到浏览器白屏

**可能原因**：
1. Vue语法错误
2. 页面路径配置错误
3. App.vue配置错误

**排查步骤**：
1. 查看HBuilderX控制台的错误信息
2. 检查 `pages.json` 中的页面路径是否正确
3. 确认所有页面文件都存在

## 注意事项

1. **HBuilderX版本**：建议使用最新版HBuilderX（3.6.0+）
2. **微信开发者工具**：运行小程序前需确保已安装并开启安全端口
3. **后端API**：前端默认请求 `http://localhost:8080/api`，请确保后端已启动
4. **长辈模式**：在「我的」页面可切换长辈模式，字体和按钮会放大

## 技术栈

- 框架：uni-app + Vue3
- 样式：SCSS
- 构建工具：Vite
- 支持平台：H5、微信小程序、App

## 页面列表

| 页面路径 | 页面名称 | 说明 |
|---------|---------|------|
| pages/index/index | 首页 | 展示banner、快捷入口、提醒、推荐医生 |
| pages/pet/list | 宠物列表 | 管理宠物档案，添加/删除宠物 |
| pages/consultation/index | 在线问诊 | 症状选择、AI诊断、24小时医院推荐 |
| pages/appointment/index | 预约挂号 | 科室选择、医生排班、预约确认 |
| pages/medicine/index | 药品购买 | 药品分类、处方管理、下单购买 |
| pages/mine/index | 个人中心 | 用户信息、长辈模式开关、设置 |
