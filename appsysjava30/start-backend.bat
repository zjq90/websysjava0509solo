@echo off
echo ========================================
echo   医疗挂号系统 - 后端启动脚本
echo ========================================
echo.

cd /d "%~dp0backend"

echo [1/3] 检查Maven环境...
where mvn >nul 2>nul
if %errorlevel% neq 0 (
    echo [ERROR] 未找到Maven，请先安装Maven并配置环境变量
    echo 下载地址: https://maven.apache.org/download.cgi
    pause
    exit /b 1
)
echo [OK] Maven环境正常
echo.

echo [2/3] 编译项目...
call mvn clean compile -DskipTests
if %errorlevel% neq 0 (
    echo [ERROR] 编译失败
    pause
    exit /b 1
)
echo [OK] 编译成功
echo.

echo [3/3] 启动应用...
echo.
echo ========================================
echo   服务启动中，请稍候...
echo   API文档: http://localhost:8080/api/swagger-ui.html
echo   H2控制台: http://localhost:8080/api/h2-console
echo ========================================
echo.

call mvn spring-boot:run

pause
