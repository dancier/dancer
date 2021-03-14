#!/bin/bash

source ./version.conf
docker login
docker push dancier/dancer:${VERSION}