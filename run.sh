#!/bin/bash

jarname=$(find $PWD -type f -name "*.jar")

if [-n "$jarname"]; then
	echo "Found at: $jarname"
else
	echo "Not found"
fi
echo "Running..."

eval "mvn spring-boot:run"

echo "Finished!"
