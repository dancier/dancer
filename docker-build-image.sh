#!/bin/bash

mvn package
source ./version.conf

docker build -t dancier/dancer:${VERSION} .
