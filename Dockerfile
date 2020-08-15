FROM maven:3.6-jdk-8-alpine AS MAVEN_TOOL_CHAIN
COPY pom.xml /tmp/
COPY src/ /tmp/src/
COPY sql /tmp/sql/
WORKDIR /tmp/
RUN mvn clean install site

FROM tomcat:8
COPY --from=MAVEN_TOOL_CHAIN /tmp/target/Training-Work*.war /usr/local/tomcat/webapps/ROOT.war
CMD ["catalina.sh", "run"]
