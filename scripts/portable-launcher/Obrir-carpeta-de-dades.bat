@echo off
if not exist "%USERPROFILE%\GestioEPIs" mkdir "%USERPROFILE%\GestioEPIs"
start "" explorer "%USERPROFILE%\GestioEPIs"
