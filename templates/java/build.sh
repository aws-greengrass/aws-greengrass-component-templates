#!/bin/bash

maven_templates=$(ls -d */ | sed 's/\//\n/g')
current_directory=`pwd`
for template in $maven_templates; do
   cd $current_directory/$template
   echo "Building $template template"
   mvn clean install
done