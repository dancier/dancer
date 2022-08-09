# fluentd/Dockerfile

FROM fluent/fluentd:v1.15.0-1.0
USER root
RUN ["gem", "install", "elasticsearch", "--version", "7.6"]
RUN ["gem", "install", "fluent-plugin-elasticsearch", "--no-document", "--version", "5.2.3"]
USER fluent

