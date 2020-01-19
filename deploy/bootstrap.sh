#!/bin/bash 

echo "update"
sudo apt -y update;
echo "dist upgrade"
sudo apt -y dist-upgrade;

echo "get docker"
curl -fsSL https://get.docker.com -o get-docker.sh
echo "install docker"
sh get-docker.sh

echo "run"
docker pull dancier/dancer:1.0
docker run --name dancer  -d -p 8080:8080 dancier/dancer:1.0
