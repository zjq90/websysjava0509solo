@echo off
echo ====================================
echo 医院住院管理系统 - 前端启动脚本
echo ====================================
echo.

echo [1/2] 检查Node.js环境...
node -v
if %errorlevel% neq 0 (
    echo 错误: 未找到Node.js环境，请安装Node.js 14或更高版本
    pause
    exit /b 1
)
echo Node.js环境检查通过
echo.

echo [2/2] 检查npm环境...
npm -v
if %errorlevel% neq 0 (
    echo 错误: npm环境异常
    pause
    exit /b 1
)
echo npm环境检查通过
echo.

cd frontend

if not exist "node_modules" (
    echo ====================================
    echo 首次运行，正在安装依赖...
    echo ====================================
    call npm install
    if %errorlevel% neq 0 (
        echo 错误: 依赖安装失败
        pause
        exit /b 1
    )
    echo 依赖安装完成
    echo.
)

echo ====================================
echo 正在启动Vue前端开发服务器...
echo 前端地址: http://localhost:8081
echo ====================================
echo.

call npm run serve

pause
