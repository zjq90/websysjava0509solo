@echo off
echo ========================================
echo    智慧医院预约挂号系统 - 前端启动
echo ========================================
echo.

cd /d "%~dp0frontend"

echo [1/3] 检查Node.js环境...
where node >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] 未检测到Node.js，请先安装Node.js (建议v16+)
    echo 下载地址: https://nodejs.org/
    pause
    exit /b 1
)
echo [OK] Node.js已就绪

echo.
echo [2/3] 安装依赖...
if not exist "node_modules" (
    call npm install
    if %errorlevel% neq 0 (
        echo [ERROR] 依赖安装失败
        pause
        exit /b 1
    )
) else (
    echo [OK] 依赖已存在，跳过安装
)

echo.
echo [3/3] 启动开发服务器...
echo.
echo 启动后请在HBuilderX或浏览器中访问
echo.
echo 注意: 开发模式下请确保后端服务已启动
echo 后端地址: http://localhost:8080
echo.

call npm run dev:h5

pause
