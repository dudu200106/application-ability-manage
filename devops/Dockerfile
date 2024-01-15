FROM openjdk:17-slim

ARG group_name
ARG project_name
ARG version
ARG port

COPY ../target/${project_name}-${version}.jar /application-ability-manage.jar

WORKDIR /

EXPOSE ${port}

#CMD ["sh", "-ec", "exec java $JAVA_OPTS -jar $APP_OPTS horae-admin-api.jar"]
CMD ["sh", "-ec", "exec java -Xms512m -Xmx1024m -jar application-ability-manage.jar"]
