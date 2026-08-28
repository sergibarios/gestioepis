# Genera una carpeta portable (dist\GestioEPIs) amb l'app + un Java propi,
# perque el company pugui obrir-la fent doble clic sense instal·lar res
# ni haver d'executar cap .exe (aixo evita els bloquejos de l'antivirus
# corporatiu, que desconfia dels executables nous i sense signar).
#
# Requisits per EXECUTAR aquest script (nomes a l'ordinador de qui compila,
# no a l'ordinador de desti):
#   - JDK 21+ (el projecte fa servir Java 25) amb "java" i "jlink" al PATH.
#   - No cal Gradle instal·lat: es fa servir el Gradle Wrapper (gradlew.bat).
#
# Us: obre PowerShell en aquesta carpeta i executa:
#     .\scripts\build-portable.ps1

$ErrorActionPreference = "Stop"

$root = Split-Path -Parent $PSScriptRoot
Set-Location $root

$distName = "GestioEPIs"
$distRoot = Join-Path $root "dist\$distName"

Write-Host "== 1/4 Compilant l'aplicacio (gradlew bootJar) ==" -ForegroundColor Cyan
& "$root\gradlew.bat" -q clean bootJar
if ($LASTEXITCODE -ne 0) { throw "Ha fallat 'gradlew bootJar'." }

$jar = Get-ChildItem (Join-Path $root "build\libs") -Filter "GestioEPIs-*.jar" |
    Where-Object { $_.Name -notlike "*plain*" } |
    Select-Object -First 1
if (-not $jar) { throw "No s'ha trobat cap .jar generat a build\libs\." }

Write-Host "== 2/4 Preparant la carpeta de sortida ==" -ForegroundColor Cyan
if (Test-Path $distRoot) { Remove-Item $distRoot -Recurse -Force }
New-Item -ItemType Directory -Path (Join-Path $distRoot "app") -Force | Out-Null
Copy-Item $jar.FullName (Join-Path $distRoot "app\app.jar")

Write-Host "== 3/4 Generant un Java portable (jlink) ==" -ForegroundColor Cyan
$jlinkCmd = Get-Command jlink -ErrorAction SilentlyContinue
if (-not $jlinkCmd) { throw "No es troba 'jlink' al PATH. Cal un JDK (no nomes un JRE) 21+ instal·lat i afegit al PATH." }

# jlink necessita --module-path per resoldre ALL-MODULE-PATH; es dedueix
# la carpeta arrel del JDK a partir de la ubicacio de jlink.exe (bin\..).
$jdkHome = Split-Path -Parent (Split-Path -Parent $jlinkCmd.Source)
$jmods = Join-Path $jdkHome "jmods"
if (-not (Test-Path $jmods)) { throw "No es troba la carpeta 'jmods' a '$jdkHome'. Cal un JDK complet (no nomes un JRE)." }

& jlink --module-path $jmods --add-modules ALL-MODULE-PATH --strip-debug --no-header-files --no-man-pages --compress=zip-6 --output (Join-Path $distRoot "jre")
if ($LASTEXITCODE -ne 0) { throw "Ha fallat 'jlink'." }

Write-Host "== 4/4 Copiant els llancadors ==" -ForegroundColor Cyan
Copy-Item (Join-Path $root "scripts\portable-launcher\*") $distRoot -Recurse -Force

Write-Host ""
Write-Host "Fet! Carpeta portable creada a:" -ForegroundColor Green
Write-Host "  $distRoot"
Write-Host ""
Write-Host "Seguent pas: comprimeix aquesta carpeta en un .zip i comparteix-la"
Write-Host "amb el company. Nomes cal que la descomprimeixi i faci doble clic"
Write-Host "a Iniciar-GestioEPIs.bat -- no necessita tenir Java instal·lat."
