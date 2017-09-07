XMX=512M
JAVA_OPTS="-Xmx$(XMX) -XX:+AlwaysPreTouch -XX:+UnlockExperimentalVMOptions -XX:NativeMemoryTracking=summary -Djava.net.preferIPv4Stack=true -XX:+DisableExplicitGC"
DOCKER_MEMORY=1024M
INFINISPAN_PARAMETERS=-c clustered.xml -Djboss.default.jgroups.stack=tcp

_CONTAINER_SHA1=$(shell docker ps --filter="ancestor=infinispan-measurements" --format={{.ID}})

build-docker:
	docker build -t infinispan-measurements ./docker
.PHONY: build-docker

run-docker:
	docker run -m $(DOCKER_MEMORY) --memory-swappiness=0 --memory-swap $(DOCKER_MEMORY) -e JAVA_OPTS=$(JAVA_OPTS) infinispan-measurements $(INFINISPAN_PARAMETERS)
.PHONY: run-docker

run-tests:
	./gradlew
.PHONY: run-tests

show-stats:
	docker exec -it $(_CONTAINER_SHA1) measure_infinispan.sh
.PHONY: show-stats


