@echo off
echo Starting BikeShare Backend...
echo.

rem Check if Maven is available
where mvn >nul 2>&1
if %errorlevel% neq 0 (
    echo Maven is not installed or not in PATH.
    echo Please install Maven and add it to your system PATH.
    echo.
    echo Alternatively, you can use an IDE like IntelliJ IDEA to run the project.
    echo Run the main class: com.bikeshare.BikeShareAdminApplication
    pause
    exit /b 1
)

rem Run Spring Boot application
mvn spring-boot:run

if %errorlevel% neq 0 (
    echo.
    echo Build failed. Please check the error messages above.
    pause
)
