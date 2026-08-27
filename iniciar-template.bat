@echo off
cd /d %~dp0
powershell -NoProfile -Command "Get-CimInstance Win32_Process | Where-Object { $_.CommandLine -like '*spring.profiles.active=desktop*' -and $_.CommandLine -like '*app.jar*' } | ForEach-Object { Stop-Process -Id $_.ProcessId -Force -ErrorAction SilentlyContinue }" >nul 2>&1
start "" "runtime\bin\javaw.exe" -Dspring.profiles.active=desktop -jar app.jar
exit
