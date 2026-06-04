@echo off
setlocal

cd /d "%~dp0.."

echo Running Sprint 3 service CI tests...
echo Services: merchant-service, payment-service, ledger-service

mvn -pl merchant-service,payment-service,ledger-service -am test
if errorlevel 1 exit /b %errorlevel%

echo Service CI tests completed successfully.
