#!/bin/bash

# target a local cloudfoundry/uaa instance
uaac target http://localhost:8080/uaa

# login with the canned admin user
uaac token client get admin -s adminsecret

# load up a client with some scopes:
uaac client add client1  \
  --name client1 \
  --scope resource.read,resource.write \
  --autoapprove true  \
  -s client1 \
  --authorized_grant_types authorization_code,refresh_token,client_credentials \
  --authorities uaa.resource \
  --redirect_uri http://localhost:8888/**,http://localhost:57987/**

# set up the users
uaac user add writer -p writer --emails writer@example.com
uaac group add resource.write
uaac member add resource.write writer

uaac user add reader -p reader --emails reader@example.com
uaac group add resource.read
uaac member add resource.read reader

# log in via the client as a specific user
# uaac token authcode get -c client1 -s client1 --port 57987 --no-cf
