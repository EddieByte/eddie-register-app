# Eddie Register App

A small Java web application used as the workload for the
[cicd-eks-pipeline](https://github.com/EddieByte/cicd-eks-pipeline) DevOps platform.
The app is intentionally simple: the focus of the project is the pipeline and
platform that build, scan, deploy and monitor it.

## What it does

- **Register** (`/register`): creates a real account in PostgreSQL and logs you straight in
- **Log in / Log out** (`/login`, logout button in the nav bar)
- **My account** (`/account`): your profile (mobile number partly masked) and a **Delete account**
  option that asks for your password first
- **Community** (`/community`): member count and recent sign-ups, shown only as "Jane D."
- **Health check** (`/health`): `200` when the database is reachable, `503` when it isn't
- Home and About pages (these work even when the database is down)

### Security built in

- Passwords stored only as salted **PBKDF2-HMAC-SHA256** hashes (310,000 iterations)
- **CSRF tokens** on every form; logout and delete are POST-only
- New session id on login (prevents session fixation); 30-minute idle timeout
- Generic "invalid email or password" message, equal timing for unknown emails, and a
  **lockout after 5 failed logins** for 15 minutes
- Server-side validation of every field; all user input **HTML-escaped** on output (no XSS)
- Parameterised SQL everywhere (no SQL injection)

> Demo app served over plain HTTP: don't reuse a real password.

## Tech stack

| Layer | Technology |
|---|---|
| Language | Java 21 (build and runtime) |
| Web | JSP + Java Servlet (javax.servlet) |
| Database | PostgreSQL (Amazon RDS in AWS), via the PostgreSQL JDBC driver |
| Packaging | Maven multi-module build → `webapp.war` |
| Runtime | Apache Tomcat 9 (`tomcat:9.0-jdk21` image), deployed as the root app |

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

The app reads its database settings from environment variables:

| Variable | Example |
|---|---|
| `DB_URL` | `jdbc:postgresql://localhost:5432/registerapp` |
| `DB_USER` | `app` |
| `DB_PASSWORD` | `secret` |

The `users` table is created automatically on first use. Without `DB_URL`, the home and
about pages still work and account pages show a "database not connected" message.

```bash
mvn clean package                         # builds webapp/target/webapp.war and runs unit tests
docker build -t eddie-register-app .
docker run --rm -p 8080:8080 \
  -e DB_URL=jdbc:postgresql://host.docker.internal:5432/registerapp \
  -e DB_USER=app -e DB_PASSWORD=secret eddie-register-app
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
