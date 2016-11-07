@echo off
PATH %PATH%;%JAVA_HOME%\bin\
for /f tokens^=2-5^ delims^=.-_^" %%j in ('java -fullversion 2^>^&1') do set "jver=%%j%%k%%l%%m"
IF %jver% LSS 15000 (
	echo No Java found or version is not supported. Please, install or update Java to version 1.5 or later to run CAS. 
	pause
) ELSE (
	goto run
)

:run
for /f "delims=" %%i in ('dir /b /a-d %~dp0cas-*.jar') do set casfile=%%i
set caspath=%~dp0
set casabsolutepath=%caspath%%casfile%
java -jar %casabsolutepath%                             