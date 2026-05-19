@echo off
echo ========================================
echo 文物收藏数据中台 - 前端启动脚本
echo ========================================
echo.

echo [1/3] 检查Node.js环境...
node --version
if %errorlevel% neq 0 (
    echo 错误: 未找到Node.js环境，请安装Node.js 14.x或更高版本
    echo 下载地址: https://nodejs.org/
    pause
    exit /b 1
)
echo Node.js环境检查通过
echo.

echo [2/3] 检查依赖...
if not exist "node_modules" (
    echo 首次运行，正在安装依赖...
    call npm install
    if %errorlevel% neq 0 (
        echo 依赖安装失败
        pause
        exit /b 1
    )
) else (
    echo 依赖已存在
)
echo.

echo [3/3] 启动Vue开发服务器...
echo 前端将在 http://localhost:8081 启动
echo 请确保后端服务已在 http://localhost:8080/api 运行
echo.
echo 按 Ctrl+C 停止应用
echo ========================================
echo.

call npm run serve

if %errorlevel% neq 0 (
    echo.
    echo 应用启动失败，请检查错误信息
    pause
)
