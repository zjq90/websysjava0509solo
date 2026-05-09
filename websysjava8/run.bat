@echo off
chcp 65001 >nul
echo ========================================
echo  智能库存管理系统 - 启动脚本
echo ========================================
echo.

REM 检查Java版本
echo [1/3] 检查Java环境...
java -version 2>&1 | findstr "1.8" >nul
if errorlevel 1 (
    echo [错误] 需要Java 8 (JDK 1.8)
    echo 当前Java版本:
    java -version
    pause
    exit /b 1
)
echo [OK] Java 8 环境正常
echo.

REM 检查是否有Maven Wrapper
if exist "mvnw.cmd" (
    echo [2/3] 找到Maven Wrapper
    echo [3/3] 启动Spring Boot应用...
    echo.
    call mvnw.cmd spring-boot:run
) else (
    echo [2/3] Maven Wrapper未找到，尝试使用Maven...
    where mvn >nul 2>&1
    if errorlevel 1 (
        echo [错误] 未找到Maven，请先安装Maven或下载Maven Wrapper
        echo.
        echo 解决方案:
        echo 1. 安装Maven: https://maven.apache.org/download.cgi
        echo 2. 或使用Trae IDE的内置Maven功能
        echo 3. 或使用IDE（IDEA/Eclipse）直接运行 InventoryApplication.java
        pause
        exit /b 1
    )
    echo [3/3] 启动Spring Boot应用...
    echo.
    mvn spring-boot:run
)
