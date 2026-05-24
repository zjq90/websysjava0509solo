@echo off
echo Starting BikeShare Frontend...
echo.

cd frontend

rem Check if Node.js is available
where node >nul 2>&1
if %errorlevel% neq 0 (
    echo Node.js is not installed or not in PATH.
    echo Please install Node.js 16+ from https://nodejs.org/
    pause
    exit /b 1
)

rem Check if node_modules exists
if not exist "node_modules" (
    echo Installing dependencies...
    call npm install
    if %errorlevel% neq 0 (
        echo npm install failed.
        pause
        exit /b 1
    )
)

echo Starting development server...
call npm run dev
