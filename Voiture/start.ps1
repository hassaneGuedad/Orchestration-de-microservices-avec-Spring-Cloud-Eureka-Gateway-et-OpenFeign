# Script de démarrage du microservice SERVICE-VOITURE (PowerShell)

Write-Host "======================================" -ForegroundColor Cyan
Write-Host "  SERVICE-VOITURE - Microservice" -ForegroundColor Cyan
Write-Host "======================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "[INFO] Compilation du projet..." -ForegroundColor Yellow
& .\mvnw.cmd clean compile

if ($LASTEXITCODE -ne 0) {
    Write-Host "[ERREUR] La compilation a echoue!" -ForegroundColor Red
    Read-Host "Appuyez sur Entree pour continuer"
    exit 1
}

Write-Host ""
Write-Host "[INFO] Demarrage du service..." -ForegroundColor Green
Write-Host "[INFO] Port: 8089" -ForegroundColor Green
Write-Host "[INFO] Nom du service: SERVICE-VOITURE" -ForegroundColor Green
Write-Host ""
Write-Host "IMPORTANT: Assurez-vous que les services suivants sont demarres:" -ForegroundColor Yellow
Write-Host "  1. Eureka Server (port 8761)" -ForegroundColor Yellow
Write-Host "  2. SERVICE-CLIENT" -ForegroundColor Yellow
Write-Host ""
Write-Host "Appuyez sur Ctrl+C pour arreter le service" -ForegroundColor Cyan
Write-Host ""

& .\mvnw.cmd spring-boot:run

