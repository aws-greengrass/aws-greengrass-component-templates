#!/bin/bash

languages_directories='java python'
current_directory=`pwd`
for directory in $languages_directories; do
   echo "Building $directory templates"
   cd $current_directory/templates/$directory
   ./build.sh
done
