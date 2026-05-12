@echo off
echo ====================================
echo 医院住院管理系统 - 后端启动脚本
echo ====================================
echo.

echo [1/2] 检查Java环境...
java -version
if %errorlevel% neq 0 (
    echo 错误: 未找到Java环境，请安装JDK 1.8或更高版本
    pause
    exit /b 1
)
echo Java环境检查通过
echo.

echo [2/2] 检查Maven环境...
mvn -version
if %errorlevel% neq 0 (
    echo 错误: 未找到Maven环境，请安装Maven 3.6或更高版本
    echo 或者配置Maven环境变量
    pause
    exit /b 1
)
echo Maven环境检查通过
echo.

echo ====================================
echo 正在启动Spring Boot后端服务...
echo 后端地址: http://localhost:8080
echo Swagger文档: http://localhost:8080/swagger-ui.html
echo H2控制台: http://localhost:8080/h2-console
echo ====================================
echo.

mvn spring-boot:run

pause
