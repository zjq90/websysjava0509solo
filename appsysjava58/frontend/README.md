# 前端项目启动说明

## 方案一：使用HBuilderX（推荐）

由于UniApp依赖特殊，**强烈建议使用官方HBuilderX**运行项目：

1. 下载HBuilderX: https://www.dcloud.io/hbuilderx.html
2. 打开HBuilderX
3. 文件 → 打开目录 → 选择 `frontend` 文件夹
4. 运行 → 运行到浏览器 → Chrome

## 方案二：使用Vue3纯前端版本（无需UniApp依赖）

已为您准备了纯Vue3版本的前端项目，位置：
`frontend-vue` 目录

启动方式：
```bash
cd frontend-vue
npm install
npm run dev
```

## 当前UniApp项目的依赖配置

```json
{
  "name": "pet-clinic-uniapp",
  "version": "1.0.0",
  "scripts": {
    "dev:h5": "uni -p h5",
    "build:h5": "uni build -p h5"
  },
  "dependencies": {
    "@dcloudio/uni-app": "3.0.0-4020420240930001",
    "@dcloudio/uni-components": "3.0.0-4020420240930001",
    "@dcloudio/uni-h5": "3.0.0-4020420240930001",
    "vue": "3.3.13"
  },
  "devDependencies": {
    "@dcloudio/types": "^3.4.8",
    "@dcloudio/uni-cli-shared": "3.0.0-4020420240930001",
    "@dcloudio/vite-plugin-uni": "3.0.0-4020420240930001",
    "vite": "5.0.10"
  }
}
```

## 注意事项

1. **UniApp的npm包仅发布在DCloud的npm源**，不在官方npm源上
2. 使用HBuilderX会自动处理依赖问题
3. 推荐使用纯Vue3版本（frontend-vue）进行开发测试
