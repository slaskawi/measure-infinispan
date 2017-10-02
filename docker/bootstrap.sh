#!/usr/bin/env bash

BIND=$(hostname -i)
BIND_OPTS="-Djboss.bind.address.management=0.0.0.0 -Djgroups.join_timeout=1000 -Djgroups.bind_addr=$BIND -Djboss.bind.address=$BIND"

/opt/jboss/infinispan-server/bin/add-user.sh -u admin -p admin
/opt/jboss/infinispan-server/bin/add-user.sh -a -u admin -p admin

exec /opt/jboss/infinispan-server/bin/standalone.sh $BIND_OPTS
