@echo off
setlocal
cd /d "%~dp0"
title GestioEPIs - NO TANQUIS aquesta finestra mentre hi treballis

if not exist "jre\bin\java.exe" (
    echo No s'ha trobat el Java inclos a la carpeta "jre".
    echo Torna a generar aquesta carpeta portable amb el script de build.
    pause
    exit /b 1
)

if not exist "app\app.jar" (
    echo No s'ha trobat "app\app.jar".
    echo Torna a generar aquesta carpeta portable amb el script de build.
    pause
    exit /b 1
)

echo Iniciant el GestioEPIs...
echo El navegador s'obrira automaticament d'aqui uns segons.
echo.
echo Per ATURAR l'aplicacio: fes clic al boto "Tancar aplicacio" dins de la
echo propia web, o simplement tanca aquesta finestra.
echo.

"%~dp0jre\bin\java.exe" -Dspring.profiles.active=desktop -jar "%~dp0app\app.jar"

echo.
echo El servidor s'ha aturat.
pause
endlocal
