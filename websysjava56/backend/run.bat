@echo off
echo ========================================
echo 文物收藏数据中台 - 后端启动脚本
echo ========================================
echo.

echo [1/3] 检查Java环境...
java -version
if %errorlevel% neq 0 (
    echo 错误: 未找到Java环境，请安装JDK 1.8或更高版本
    pause
    exit /b 1
)
echo Java环境检查通过
echo.

echo [2/3] 检查Maven...
where mvn >nul 2>&1
if %errorlevel% neq 0 (
    echo 未找到系统Maven，尝试使用Maven Wrapper...
    if exist "mvnw.cmd" (
        set MVN_CMD=mvnw.cmd
    ) else (
        echo 错误: 未找到Maven，请安装Maven或配置环境变量
        echo 下载地址: https://maven.apache.org/download.cgi
        pause
        exit /b 1
    )
) else (
    set MVN_CMD=mvn
)
echo Maven检查完成
echo.

echo [3/3] 启动Spring Boot应用...
echo 应用将在 http://localhost:8080/api 启动
echo Swagger文档: http://localhost:8080/api/swagger-ui.html
echo H2控制台: http://localhost:8080/api/h2-console
echo.
echo 按 Ctrl+C 停止应用
echo ========================================
echo.

%MVN_CMD% spring-boot:run

if %errorlevel% neq 0 (
    echo.
    echo 应用启动失败，请检查错误信息
    pause
)
