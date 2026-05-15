@echo off
echo ========================================
echo    裁判管理系统 - 前端启动脚本
echo ========================================
echo.

echo 正在检查 Node.js 环境...
node -v >nul 2>&1
if %errorlevel% neq 0 (
    echo [错误] 未找到 Node.js，请先安装 Node.js 14+
    pause
    exit /b 1
)
echo [OK] Node.js 环境正常
echo.

cd /d "%~dp0frontend"

if not exist "node_modules" (
    echo 正在安装前端依赖...
    echo.
    call npm install
    if %errorlevel% neq 0 (
        echo [错误] 依赖安装失败
        pause
        exit /b 1
    )
    echo [OK] 依赖安装完成
    echo.
)

echo 正在启动前端开发服务器...
echo.
echo 服务启动后可访问: http://localhost:3000
echo.
echo 按 Ctrl+C 停止服务
echo ========================================
echo.

call npm run dev

pause
