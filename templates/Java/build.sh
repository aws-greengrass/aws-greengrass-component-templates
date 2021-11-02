#!/bin/bash

maven_templates='HelloWorld'
current_directory=`pwd`
for template in $maven_templates; do
   cd $current_directory/$template
   echo "Building $template template"
    /opt/apache-maven/apache-maven-3.8.3/bin/mvn clean install
done