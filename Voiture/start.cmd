@echo off
REM Script de démarrage du microservice SERVICE-VOITURE

echo ======================================
echo   SERVICE-VOITURE - Microservice
echo ======================================
echo.

echo [INFO] Compilation du projet...
call mvnw.cmd clean compile
if %ERRORLEVEL% NEQ 0 (
    echo [ERREUR] La compilation a echoue!
    pause
    exit /b 1
)

echo.
echo [INFO] Demarrage du service...
echo [INFO] Port: 8089
echo [INFO] Nom du service: SERVICE-VOITURE
echo.
echo IMPORTANT: Assurez-vous que les services suivants sont demarres:
echo   1. Eureka Server (port 8761)
echo   2. SERVICE-CLIENT
echo.
echo Appuyez sur Ctrl+C pour arreter le service
echo.

call mvnw.cmd spring-boot:run

