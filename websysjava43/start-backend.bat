@echo off
echo ========================================
echo 教学辅助系统 - 后端启动脚本
echo ========================================
echo.

cd /d "%~dp0backend"

echo [1/3] 检查Maven环境...
mvn -version
if %errorlevel% neq 0 (
    echo 错误: 未找到Maven，请先安装Maven并配置环境变量
    pause
    exit /b 1
)

echo.
echo [2/3] 编译项目...
mvn clean compile -DskipTests
if %errorlevel% neq 0 (
    echo 错误: 编译失败
    pause
    exit /b 1
)

echo.
echo [3/3] 启动Spring Boot应用...
echo.
echo 后端服务启动后访问:
echo   - 服务地址: http://localhost:8080
echo   - Swagger文档: http://localhost:8080/swagger-ui.html
echo   - H2控制台: http://localhost:8080/h2-console
echo.
echo 测试账号:
echo   - 教师: teacher / 123456
echo   - 学生: student1 / 123456
echo.
echo 按 Ctrl+C 停止服务
echo ========================================
echo.

mvn spring-boot:run

pause
