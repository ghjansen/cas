#!/bin/bash
if type -p java; then
    _java=java
elif [[ -n "$JAVA_HOME" ]] && [[ -x "$JAVA_HOME/bin/java" ]];  then
    _java="$JAVA_HOME/bin/java"
else
    echo "No Java found. Please, install Java 1.5 or later in your machine to run CAS."
    read -n1 -r -p "Press any key to continue..."
fi
if [[ "$_java" ]]; then
    version=$("$_java" -version 2>&1 | awk -F '"' '/version/ {print $2}')
    if [[ "$version" > "1.5" ]]; then
    	DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
        cd "${DIR}"
        java -jar cas-*.jar
    else         
        echo "Java version is not compatible with CAS. Please install Java 1.5 or later, or update your Java version and try again."
        read -n1 -r -p "Press any key to continue..."
    fi
fi