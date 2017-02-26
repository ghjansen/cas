#!/bin/bash
# CAS - Cellular Automata Simulator
# Copyright (C) 2016  Guilherme Humberto Jansen
# 
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License as published
# by the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
# 
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU Affero General Public License for more details.
# 
# You should have received a copy of the GNU Affero General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
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