FROM openjdk:8-jre-alpine

MAINTAINER $maintainer$

ENV LOG_LEVEL="INFO"
ENV PROJECT_NAME="$normalizedName$"
# install bash
RUN apk add --update bash && rm -rf /var/cache/apk/*
# copy the locally built application to the image
ADD \${PROJECT_NAME}-server/target/universal/stage /opt/application
# make executable all script needed to run application
RUN chmod +x /opt/application/bin/*
# run the server when a container based on this image is being run
ENTRYPOINT ["/opt/application/bin/startup"]

EXPOSE 8080