#!/bin/bash

languages_directories='Java Python'
current_directory=`pwd`
for directory in $languages_directories; do
   echo "Building $directory templates"
   cd $current_directory/templates/$directory
   ./build.sh
done