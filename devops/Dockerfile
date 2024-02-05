FROM openjdk:17-slim

ARG group_name
ARG project_name
ARG version
ARG port

COPY target/${project_name}-${version}.jar /application-ability-manage.jar
COPY ./agent /usr/local/agent/


WORKDIR /

EXPOSE ${port}

#CMD ["sh", "-ec", "exec java $JAVA_OPTS -jar $APP_OPTS horae-admin-api.jar"]
#CMD ["sh", "-ec", "exec java -Xms512m -Xmx1024m -jar application-ability-manage.jar"]
ENTRYPOINT [ "sh", "-c", "java -javaagent:/usr/local/agent/skywalking-agent.jar -Dskywalking.agent.service_name=application-ability-manage -Dskywalking.collector.backend_service=106.227.94.62:11800 -jar application-ability-manage.jar" ]

