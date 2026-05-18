@echo off
echo ========================================
echo    文物收藏管理系统 - 启动脚本
echo ========================================
echo.

echo [1/3] 检查Java环境...
java -version
if %errorlevel% neq 0 (
    echo 错误: 未找到Java环境，请先安装JDK 8或更高版本
    pause
    exit /b 1
)
echo Java环境检查通过
echo.

echo [2/3] 检查Maven环境...
mvn -version
if %errorlevel% neq 0 (
    echo 错误: 未找到Maven环境，请先安装Maven
    pause
    exit /b 1
)
echo Maven环境检查通过
echo.

echo [3/3] 启动后端服务...
cd backend
echo 正在编译并启动Spring Boot应用...
echo 应用将运行在: http://localhost:8080/api
echo Swagger文档: http://localhost:8080/api/swagger-ui.html
echo H2控制台: http://localhost:8080/api/h2-console
echo.
echo 提示: 按 Ctrl+C 停止服务
echo ========================================
mvn spring-boot:run

cd ..
pause
