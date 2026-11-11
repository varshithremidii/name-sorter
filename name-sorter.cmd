@echo off
rem Runs the Name Sorter:  name-sorter .\unsorted-names-list.txt
rem Build it first with:   mvn package

set "JAR=%~dp0target\name-sorter.jar"

if not exist "%JAR%" (
    echo name-sorter has not been built yet. Run "mvn package" in %~dp0 first. 1>&2
    exit /b 1
)

java -jar "%JAR%" %*
