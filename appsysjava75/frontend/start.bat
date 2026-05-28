@echo off
chcp 65001
echo ========================================
echo 大学生社团管理系统 - 前端启动脚本
echo ========================================
echo.

echo [1/3] 检查Node.js环境...
node -v
if %errorlevel% neq 0 (
    echo 错误: 未检测到Node.js环境，请先安装Node.js 16+
    pause
    exit /b 1
)
echo Node.js环境检查通过!
echo.

echo [2/3] 安装依赖...
if not exist "node_modules" (
    echo 正在安装依赖，首次运行可能需要几分钟...
    npm install
) else (
    echo 依赖已安装!
)
echo.

echo [3/3] 启动H5开发服务器...
echo 应用地址: http://localhost:8081
echo.
echo 按 Ctrl+C 停止应用
echo ========================================
echo.

npm run dev:h5

pause
