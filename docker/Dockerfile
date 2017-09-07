FROM jboss/infinispan-server:9.1.0.Final-2

USER root
ADD ./measure_infinispan.sh /usr/bin
ADD ./bootstrap.sh /usr/bin
RUN chmod +x /usr/bin/bootstrap.sh /usr/bin/measure_infinispan.sh
RUN yum install -y dstat

ENTRYPOINT ["/usr/bin/bootstrap.sh"]
