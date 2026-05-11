@echo off
echo ========================================
echo    智慧医院预约挂号系统 - 后端启动
echo ========================================
echo.

cd /d "%~dp0backend"

echo [1/3] 检查Maven环境...
where mvn >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] 未检测到Maven，请先安装Maven并配置环境变量
    echo 下载地址: https://maven.apache.org/download.cgi
    pause
    exit /b 1
)
echo [OK] Maven已就绪

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
echo [3/3] 启动SpringBoot应用...
echo.
echo 服务启动后请访问:
echo   - Swagger文档: http://localhost:8080/swagger-ui.html
echo   - H2控制台:   http://localhost:8080/h2-console
echo   - API基础路径: http://localhost:8080/api
echo.
echo 测试账号:
echo   - 管理员: admin / 123456
echo   - 普通用户: user1 / 123456
echo.

call mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xms256m -Xmx512m"

pause
