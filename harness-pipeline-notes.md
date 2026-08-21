# Harness Pipeline Demo Notes

Suggested stage names: Build, Unit Tests, Gitleaks, OWASP Dependency-Check, SonarQube, Docker Build/Push, Trivy, Deploy, Smoke Test.

Smoke test command after deployment:
```bash
curl -fsS http://<host>:8080/actuator/health
curl -fsS http://<host>:8080/api/config
```
