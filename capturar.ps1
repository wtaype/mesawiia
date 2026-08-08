# capturar.ps1 — Script de 1-Clic para capturar la pantalla HD de tu celular
Param(
    [string]$Nombre = "captura"
)

$destDir = "c:\mipro\mesawii\recursos-mesawii\capturas"
if (-not (Test-Path $destDir)) {
    New-Item -ItemType Directory -Path $destDir -Force | Out-Null
}

$timestamp = Get-Date -Format "yyyy-MM-dd_HH-mm-ss"
$fileName = "${Nombre}_${timestamp}.png"
$destPath = Join-Path $destDir $fileName

Write-Host "Capturando pantalla del celular..." -ForegroundColor Cyan

try {
    adb shell screencap -p /sdcard/temp_screen.png
    adb pull /sdcard/temp_screen.png $destPath
    adb shell rm /sdcard/temp_screen.png

    if (Test-Path $destPath) {
        Write-Host "EXITO TOTAL! Captura guardada en:" -ForegroundColor Green
        Write-Host "Ubicacion: $destPath" -ForegroundColor Yellow
        exit 0
    }
} catch {
    Write-Host "Error durante la captura." -ForegroundColor Red
}

Write-Host "No se detecto dispositivo por USB o ADB." -ForegroundColor Red
Write-Host "Asegurate de activar Depuracion USB en tu celular." -ForegroundColor Yellow
