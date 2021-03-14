#!/bin/bash

source ./version.conf
docker run --rm --name dancer -v $(pwd)/config.yml:/config.yml -p 8080:8080 --net=host dancier/dancer:${VERSION}
