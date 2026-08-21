# Harness CI/CD Demo - Spring Boot Maven Web Application

A deliberately scan-friendly demo application for a Harness CI/CD pipeline. It is designed to demonstrate **Build → Unit Test → Coverage → Gitleaks → OWASP Dependency-Check → SonarQube → Docker Build → Trivy → Deploy → Verify**.

## Application

- Spring Boot 3.x / Java 17 / Maven
- Persistent file-backed H2 database under `./data/`
- REST API at `/api/config`
- Static web UI at `/`
- Spring Boot Actuator at `/actuator/health`, `/actuator/info`, `/actuator/metrics`, `/actuator/prometheus`
- Port `8080` by default (`PORT` env var supported)
- The UI heading and subtitle can be changed and persist across restarts/redeployments when the same persistent volume is mounted at `/app/data`.

## Run locally

```bash
mvn clean verify
mvn spring-boot:run
```

Open `http://localhost:8080`.

Build a Docker image:

```bash
mvn clean package -DskipTests
 docker build -t harness-ci-demo:1.0.0 .
 docker run --rm -p 8080:8080 -v harness-demo-data:/app/data harness-ci-demo:1.0.0
```

## Test and coverage

`mvn clean verify` runs unit tests and creates a JaCoCo report at `target/site/jacoco/index.html`. The build enforces a **68% minimum line coverage** so the demo naturally lands around the requested ~70% range.

There are 12 enabled tests and 2 disabled tests that intentionally fail. The disabled tests are there to demonstrate the failure path in Harness without breaking the default build.

To temporarily demonstrate a red test stage, remove `@Disabled` from either intentional failure test.

## Deliberate security findings for CI/CD demo stages

The repo contains **non-production demo-only** findings to give scanning stages something visible to report:

- **Gitleaks:** `src/test/resources/demo-secrets.properties` contains example AWS-looking credentials. They are public example strings, not real credentials.
- **OWASP Dependency-Check:** `commons-fileupload:1.3.1` is intentionally outdated and expected to generate CVE findings.
- **SonarQube:** the repository has intentionally simple code-quality/security smells, including a dynamically concatenated SQL statement in `AppConfigRepository` and a hardcoded/unsafe demo pattern.
- **Trivy:** the Dockerfile uses a deliberately older base-image tag to provide a realistic container scanning stage. Depending on the current image metadata, CVE results can change over time.

These findings are intentionally seeded for training/demo use. Do not copy the insecure patterns into production.

## Suggested Harness stages

1. **Maven Build** - `mvn clean package -DskipTests`
2. **Unit Test + Coverage** - `mvn clean verify`
3. **Gitleaks** - scan repository filesystem/history
4. **OWASP Dependency-Check** - scan Maven dependency tree / `pom.xml`
5. **SonarQube** - run analysis and publish quality gate
6. **Docker Build & Push** - `docker build ...` then push to registry
7. **Trivy Image Scan** - scan the built image
8. **Deploy** - deploy container or Kubernetes workload
9. **Verification** - curl `/actuator/health` and check `/`

## Redeployment demo

Change the heading in `src/main/resources/static/index.html`, for example the browser page title or instructional text, commit the change, and run the pipeline again. After deployment, open `/` and verify the new code is live.

For **data persistence**, change the heading through the UI. The value is stored in H2 under `./data/`; mount that directory as a persistent volume in Docker/Kubernetes so the value survives container replacement.
