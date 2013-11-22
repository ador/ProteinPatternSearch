#!/bin/bash

## sample usage : ./0_rowFiltering.sh config/0_rowfiltering.properties

thisDir=$(dirname $0) || false

expected="/"
conf=$1
if [ $expected = ${conf:0:1} ]; then
        propfile=$1
else
        propfile=$(pwd)/$1
fi

echo "Using settings from:  $propfile"

pushd $thisDir/..

java -cp java/build/libs/java.jar protka.main.FilterRows $propfile

popd
