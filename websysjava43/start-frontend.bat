@echo off
echo ========================================
echo 教学辅助系统 - 前端启动脚本
echo ========================================
echo.

cd /d "%~dp0frontend"

echo [1/3] 检查Node.js环境...
node -version
if %errorlevel% neq 0 (
    echo 错误: 未找到Node.js，请先安装Node.js
    pause
    exit /b 1
)

echo.
echo [2/3] 检查并安装依赖...
if not exist "node_modules" (
    echo 正在安装依赖...
    npm install
    if %errorlevel% neq 0 (
        echo 错误: 依赖安装失败
        pause
        exit /b 1
    )
) else (
    echo 依赖已存在，跳过安装
)

echo.
echo [3/3] 启动前端开发服务器...
echo.
echo 前端服务启动后访问: http://localhost:3000
echo.
echo 测试账号:
echo   - 教师: teacher / 123456
echo   - 学生: student1 / 123456
echo.
echo 按 Ctrl+C 停止服务
echo ========================================
echo.

npm run serve

pause
