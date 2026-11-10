# Java 21 runtime: must match maven.compiler.target (21) in pom.xml, or the servlet
# fails to load with UnsupportedClassVersionError when the form is submitted.
FROM tomcat:9.0-jdk21

# Install the latest Ubuntu security patches on top of the base image. The official
# Tomcat image is only rebuilt periodically, so this picks up fixes released since
# (e.g. Trivy flagged apt CVE-2026-109518, fixed in apt 3.2.1ubuntu0.1).
RUN apt-get update \
 && apt-get upgrade -y --no-install-recommends \
 && apt-get clean \
 && rm -rf /var/lib/apt/lists/*

RUN rm -rf /usr/local/tomcat/webapps/ROOT

COPY webapp/target/webapp.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]