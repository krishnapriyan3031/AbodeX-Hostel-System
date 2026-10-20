@echo off
setlocal
cd /d "%~dp0"

set "JAVA_HOME=%LOCALAPPDATA%\Programs\IntelliJ IDEA 2026.1.3\jbr"
set "PATH=%JAVA_HOME%\bin;%LOCALAPPDATA%\Programs\IntelliJ IDEA 2026.1.3\plugins\maven\lib\maven3\bin;C:\Program Files\Git\cmd;%PATH%"

echo ===================================================
echo Starting AbodeX - Spring Boot Hostel Room Allocation
echo ===================================================

call mvn.cmd spring-boot:run
pause
