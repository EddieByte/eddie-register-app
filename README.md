# Eddie Register App

A small Java web application used as the workload for the
[cicd-eks-pipeline](https://github.com/EddieByte/cicd-eks-pipeline) DevOps platform.
The app is intentionally simple: the focus of the project is the pipeline and
platform that build, scan, deploy and monitor it.

## What it does

- **Home page** (`home.jsp`) with a call-to-action to create an account
- **Registration form** (`register.jsp`) for name, mobile number, email and password
- **`RegisterServlet`** (mapped to `/register`) handles the form POST and forwards to
  the thank-you page
- **Thank-you page** (`thankyou.jsp`) and **About page** (`about.jsp`)

Form data is not stored anywhere. There is no database.

## Tech stack

| Layer | Technology |
|---|---|
| Language | Java 21 (compiled), running on JDK 17 in the container |
| Web | JSP + Java Servlet (javax.servlet) |
| Packaging | Maven multi-module build → `webapp.war` |
| Runtime | Apache Tomcat 9 (`tomcat:9.0-jdk17` image), deployed as the root app |

## Project layout

```
eddie-register-app/
├── pom.xml          Parent POM (Java 21, plugin versions)
├── webapp/          The web application: JSPs, CSS, RegisterServlet → webapp.war
├── server/          Supporting Maven module (no application code yet)
├── Dockerfile       Copies webapp.war into Tomcat as ROOT.war
└── Jenkinsfile      CI pipeline (see below)
```

## Build and run locally

```bash
mvn clean package                         # produces webapp/target/webapp.war
docker build -t eddie-register-app .
docker run --rm -p 8080:8080 eddie-register-app
```

Open http://localhost:8080.

## CI/CD pipeline

Every push triggers the Jenkins pipeline in `Jenkinsfile`:

1. **Build** with Maven (`mvn clean package`)
2. **SonarQube analysis**: the pipeline stops if the quality gate fails
3. **Docker build**
4. **Trivy scan** for HIGH/CRITICAL vulnerabilities. This is *report-only*: findings
   are printed in the build log but don't stop the build.
5. **Push** the image to Docker Hub as `eddiebyte/eddie-register-app:<version>-<build>`
6. **Update the manifest repo**: the new image tag is committed to
   [eddie-register-app-manifests](https://github.com/EddieByte/eddie-register-app-manifests)
   (`overlays/dev`), and ArgoCD deploys it to EKS
7. **Slack**: the build result is posted to `#ci-builds`

Build output (`target/`) is not committed. The pipeline always builds from source.
