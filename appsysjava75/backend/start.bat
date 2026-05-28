@echo off
chcp 65001
echo ========================================
echo 大学生社团管理系统 - 后端启动脚本
echo ========================================
echo.

echo [1/3] 检查Java环境...
java -version
if %errorlevel% neq 0 (
    echo 错误: 未检测到Java环境，请先安装JDK 17+
    pause
    exit /b 1
)
echo Java环境检查通过!
echo.

echo [2/3] 检查Maven Wrapper...
if not exist ".mvn\wrapper\maven-wrapper.jar" (
    echo 正在下载Maven Wrapper...
    call mvnw.cmd --version
) else (
    echo Maven Wrapper已存在!
)
echo.

echo [3/3] 启动Spring Boot应用...
echo 应用地址: http://localhost:8080/api
echo Swagger文档: http://localhost:8080/api/swagger-ui.html
echo H2控制台: http://localhost:8080/api/h2-console
echo.
echo 按 Ctrl+C 停止应用
echo ========================================
echo.

call mvnw.cmd spring-boot:run

pause
