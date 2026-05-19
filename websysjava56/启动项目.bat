@echo off
chcp 65001 >nul
echo ========================================
echo    文物收藏数据中台 - 一键启动工具
echo ========================================
echo.
echo 请选择要启动的服务:
echo   1. 启动后端服务
echo   2. 启动前端服务
echo   3. 启动全部服务
echo   4. 退出
echo.
set /p choice=请输入选项 (1-4): 

if "%choice%"=="1" goto start_backend
if "%choice%"=="2" goto start_frontend
if "%choice%"=="3" goto start_all
if "%choice%"=="4" goto end

echo 无效的选项，请重新运行
pause
goto end

:start_backend
echo.
echo 正在启动后端服务...
cd backend
start "后端服务" cmd /k "run.bat"
cd ..
echo 后端服务正在启动中...
echo 请等待启动完成后访问: http://localhost:8080/api
pause
goto end

:start_frontend
echo.
echo 正在启动前端服务...
cd frontend
start "前端服务" cmd /k "run.bat"
cd ..
echo 前端服务正在启动中...
echo 启动完成后访问: http://localhost:8081
pause
goto end

:start_all
echo.
echo 正在启动全部服务...
echo.

echo [1/2] 启动后端服务...
cd backend
start "后端服务" cmd /k "run.bat"
cd ..
timeout /t 3 /nobreak >nul

echo.
echo [2/2] 启动前端服务...
cd frontend
start "前端服务" cmd /k "run.bat"
cd ..

echo.
echo ========================================
echo 全部服务启动中!
echo ========================================
echo 后端地址: http://localhost:8080/api
echo 前端地址: http://localhost:8081
echo Swagger: http://localhost:8081/api/swagger-ui.html
echo H2控制台: http://localhost:8080/api/h2-console
echo.
echo 默认账号: admin / admin123
echo ========================================
pause

:end
