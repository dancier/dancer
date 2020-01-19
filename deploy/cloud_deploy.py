#!/usr/bin/python3

import time
import os

from hcloud import Client, APIException
from hcloud.server_types.domain import ServerType
from hcloud.images.domain import Image

client = Client(token="RkGceGqcTcUe3OpMFJZKGAY46kcdZ1G2rssFcRcUEj1QieBJY7vzzZz1whO0tyEe")

ssh_key = client.ssh_keys.get_by_name("dancier")

def get_data_center():
    return client.datacenters.get_by_id(2)


def get_floating_ip():
    return client.floating_ips.get_by_name("dancer")


def execute_command(command):
    stream = os.popen(command)
    stream.read()


def bootstrap_server(ip):
    print("Remove possible existing host key")
    execute_command("ssh-keygen -R " + ip)
    print("Copy bootstrap.sh")
    execute_command("scp -i /home/runner/dancier -oStrictHostKeyChecking=no  ./bootstrap.sh root@" + ip + ":/root/")
    print("Invoke bootstrap")
    execute_command("ssh -i /home/runner/dancier -oStrictHostKeyChecking=no root@" + ip + " /root/bootstrap.sh")
    print("Enable floating IP")
    execute_command("ssh -i /home/runner/dancier -oStrictHostKeyChecking=no root@" + ip + " sudo ip addr add 116.202.177.122 dev eth0")


def create_server(name):
    response = client.servers.create(name=name, server_type=ServerType(name="cx11"), datacenter=get_data_center(),
                                     image=Image(name="ubuntu-18.04"), ssh_keys=[ssh_key])
    server = response.server
    main_ip = str(response.server.data_model.public_net.ipv4.ip)
    print(server)
    print(
        "You can login: " + "ssh -oStrictHostKeyChecking=no root@" + main_ip)
    try:
        client.floating_ips.unassign(get_floating_ip())
    except APIException:
        print("No need to unassign")
    print("Waiting...")
    time.sleep(20)
    print("Now assign the ip new")
    client.floating_ips.assign(get_floating_ip(), server)
    time.sleep(60)
    print("Bootstrapping...")
    bootstrap_server(main_ip)


# ip addr add 116.202.177.122/32 dev eth0

def get_servers():
    bound_servers = client.servers.get_all()
    servers = []
    for server in bound_servers:
        servers.append(server)
    return servers


def find_new_server_name(base_name="dancer"):
    all_servers = get_servers()
    all_server_names = set()
    for server in all_servers:
        all_server_names.add(server.data_model.name)
    print(all_server_names)
    i = 1
    found = False
    found_name = ""
    while not found:
        possible_new_name = base_name + "-" + str(i)
        if possible_new_name not in all_server_names:
            found_name = possible_new_name
            found = True
        else:
            i = i + 1
    return found_name


def create_next_server():
    new_server_name = find_new_server_name()
    print("Creating new server with name: " + new_server_name)
    create_server(new_server_name)
