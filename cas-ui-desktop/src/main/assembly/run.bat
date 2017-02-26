@echo off
REM CAS - Cellular Automata Simulator
REM Copyright (C) 2016  Guilherme Humberto Jansen
REM 
REM This program is free software: you can redistribute it and/or modify
REM it under the terms of the GNU Affero General Public License as published
REM by the Free Software Foundation, either version 3 of the License, or
REM (at your option) any later version.
REM 
REM This program is distributed in the hope that it will be useful,
REM but WITHOUT ANY WARRANTY; without even the implied warranty of
REM MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
REM GNU Affero General Public License for more details.
REM 
REM You should have received a copy of the GNU Affero General Public License
REM along with this program.  If not, see <http://www.gnu.org/licenses/>.
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