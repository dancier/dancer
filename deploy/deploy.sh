#!/bin/bash

# shellcheck disable=SC1073
export CLOUD_API_TOKEN=$(cat "./deploy/cloud-api-token")
export PRIVATE_KEY_FILE="./deploy/dancier.key"

IP=$(./deploy/create_next_server.py)

echo "This is the created ip: " $IP

echo "Remove possible existing host key"
ssh-keygen -R "$IP"

# wait until we can use ssh
n=0
until [[ $n -ge 5 ]]
do
   echo "try ssh"
   time ssh -i $PRIVATE_KEY_FILE -oStrictHostKeyChecking=no  root@"${IP}" echo "hallo world" && break  # substitute your command here
   echo "not successful. waiting..."
   n=$n+1
   sleep 5
done

echo "Copy init-instance script"
scp -i $PRIVATE_KEY_FILE -oStrictHostKeyChecking=no  ./deploy/init-instance.sh root@"${IP}":/root/

echo "Invoke bootstrap"
ssh -i $PRIVATE_KEY_FILE -oStrictHostKeyChecking=no root@"${IP}" /root/init-instance.sh

echo "Enable floating IP"
ssh -i $PRIVATE_KEY_FILE -oStrictHostKeyChecking=no root@"${IP}" sudo ip addr add 116.202.177.122 dev eth0
