@echo off
echo ========================================
echo    裁判管理系统 - 后端启动脚本
echo ========================================
echo.

echo 正在检查 Maven 环境...
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [错误] 未找到 Maven，请先安装 Maven 并配置环境变量
    pause
    exit /b 1
)
echo [OK] Maven 环境正常
echo.

echo 正在检查 Java 环境...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [错误] 未找到 Java，请先安装 JDK 8 并配置环境变量
    pause
    exit /b 1
)
echo [OK] Java 环境正常
echo.

echo 正在启动后端服务...
echo.
echo 服务启动后可访问：
echo   - Swagger API文档: http://localhost:8080/api/swagger-ui.html
echo   - H2 数据库控制台: http://localhost:8080/api/h2-console
echo.
echo 按 Ctrl+C 停止服务
echo ========================================
echo.

mvn spring-boot:run

pause
