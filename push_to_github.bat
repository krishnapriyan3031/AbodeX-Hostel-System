@echo off
setlocal
cd /d "%~dp0"

set "PATH=C:\Program Files\Git\cmd;%PATH%"

echo ===============================================================
echo Pushing AbodeX Spring Boot Project to GitHub...
echo Target: https://github.com/krishnapriyan3031/AbodeX-Hostel-System.git
echo ===============================================================

git push -u origin main

if %errorlevel% equ 0 (
    echo.
    echo ===============================================================
    echo [SUCCESS] Project pushed successfully to your GitHub repository!
    echo Check it live at: https://github.com/krishnapriyan3031/AbodeX-Hostel-System
    echo ===============================================================
) else (
    echo.
    echo [NOTE] If prompted by GitHub, click 'Sign in with your browser' or enter your GitHub Personal Access Token (PAT).
)
pause
