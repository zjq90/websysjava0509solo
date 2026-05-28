@echo off
chcp 65001
echo ========================================
echo 大学生社团管理系统 - 一键启动脚本
echo ========================================
echo.

echo 正在启动后端服务...
start "后端服务" cmd /k "cd backend && call start.bat"

echo 等待5秒后启动前端服务...
timeout /t 5 /nobreak

echo 正在启动前端服务...
start "前端服务" cmd /k "cd frontend && call start.bat"

echo.
echo ========================================
echo 启动完成!
echo 后端地址: http://localhost:8080/api
echo 前端地址: http://localhost:8081
echo Swagger文档: http://localhost:8080/api/swagger-ui.html
echo ========================================
echo.
pause
