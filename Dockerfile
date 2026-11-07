# Java 21 runtime: must match maven.compiler.target (21) in pom.xml, or the servlet
# fails to load with UnsupportedClassVersionError when the form is submitted.
FROM tomcat:9.0-jdk21

RUN rm -rf /usr/local/tomcat/webapps/ROOT

COPY webapp/target/webapp.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]