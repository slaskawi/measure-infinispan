#!/usr/bin/env bash

BIND=$(hostname -i)
BIND_OPTS="-Djboss.bind.address.management=0.0.0.0 -Djgroups.join_timeout=1000 -Djgroups.bind_addr=$BIND -Djboss.bind.address=$BIND"

exec /opt/jboss/infinispan-server/bin/standalone.sh $BIND_OPTS
