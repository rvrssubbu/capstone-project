Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

$root = Split-Path -Parent $PSScriptRoot
Set-Location $root

$services = @(
  "merchant-service",
  "payment-service",
  "ledger-service"
)

Write-Host "Running Sprint 3 service CI tests..."
Write-Host "Services: $($services -join ', ')"

mvn -pl ($services -join ",") -am test

Write-Host "Service CI tests completed successfully."
