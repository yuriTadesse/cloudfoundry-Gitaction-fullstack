Perfect — here’s a **complete, production-quality `README.md`** for your full-stack project:

> Spring Boot + Angular + Prometheus + Grafana + Cloud Foundry + GitHub Actions CI/CD

It’s fully self-contained — you can copy it directly into your repository root as `README.md`.
Every command and configuration used in this chat is included and explained in clear steps.

---

```markdown
# 🚀 Full-Stack Cloud Foundry Demo  
### Spring Boot + Angular + Prometheus + Grafana + GitHub Actions CI/CD  

---

## 🧠 Overview

This project demonstrates a modern **Java 17 / Spring Boot 3** backend,  
a **Node 24 / Angular 19** frontend, **Prometheus + Grafana** monitoring,  
and **CI/CD automation** with **GitHub Actions** deploying to **SAP BTP Cloud Foundry**.

---

## 🏗️ Architecture

```

┌────────────────────┐
│ Angular Frontend   │  (TypeScript, served by Spring Boot)
└──────────┬─────────┘
│ REST /api
┌──────────▼─────────┐
│ Spring Boot Backend│  (Java 17)
│ /api/hello         │
│ /actuator/*        │
└──────────┬─────────┘
│ Metrics
┌──────────▼─────────┐
│ Prometheus Service │  (scrapes /actuator/prometheus)
└──────────┬─────────┘
│ Data source
┌──────────▼─────────┐
│ Grafana Service    │  (dashboards & visualizations)
└────────────────────┘

````

---

## 🧩 Technologies Used

| Layer | Technology | Version / Notes |
|-------|-------------|-----------------|
| Backend | Spring Boot | 3.x |
|  | Java | 17 |
|  | Maven | Build tool |
| Frontend | Angular | 19.2.15 |
|  | Node.js | 24.11.0 |
|  | npm | 7.14.0 |
| Monitoring | Prometheus | SAP BTP CF service |
|  | Grafana | SAP BTP CF service |
| Cloud Platform | Cloud Foundry | SAP BTP trial/free-tier |
| CI/CD | GitHub Actions | Automated build + deploy |

---

## ⚙️ Local Development Setup

### 1️⃣ Prerequisites
| Tool | Command to Verify |
|------|-------------------|
| Java 17 | `java -version` |
| Maven | `mvn -v` |
| Node.js 24 | `node -v` |
| npm 7+ | `npm -v` |
| Angular CLI 19 | `ng version` |

---

### 2️⃣ Clone Repository
```bash
git clone https://github.com/<your-user>/<your-repo>.git
cd cloudfoundry-full-working-demo
````

---

### 3️⃣ Run Backend Locally

```bash
cd backend
mvn clean package -DskipTests
mvn spring-boot:run
```

Backend runs on **[http://localhost:8080/api/hello](http://localhost:8080/api/hello)**

---

### 4️⃣ Run Frontend Locally

```bash
cd ../frontend
npm install
npm start
```

Frontend runs on **[http://localhost:4200](http://localhost:4200)**

If backend runs separately, configure proxy:
`proxy.conf.json`

```json
{
  "/api": {
    "target": "http://localhost:8080",
    "secure": false
  }
}
```

---

## ☁️ Deploying to Cloud Foundry

### 1️⃣ Install CF CLI

* Download: [https://github.com/cloudfoundry/cli/releases](https://github.com/cloudfoundry/cli/releases)
* Verify: `cf --version`

### 2️⃣ Log in

```bash
cf login -a https://api.cf.us10-001.hana.ondemand.com
```

### 3️⃣ Check domain and quota

```bash
cf domains
cf org <your-org>
```

### 4️⃣ Backend Manifest (`backend/manifest.yml`)

```yaml
---
applications:
  - name: demo-backend
    memory: 512M
    instances: 1
    path: target/demo-0.0.1-SNAPSHOT.jar
    buildpacks:
      - java_buildpack
    env:
      JBP_CONFIG_OPEN_JDK_JRE: '{ jre: { version: 17.+ } }'
      SPRING_PROFILES_ACTIVE: cloud
      MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE: health,info,prometheus
    services:
      - prometheus-service
      - grafana-service
```

### 5️⃣ Build & Push

```bash
cd backend
mvn clean package -DskipTests
cf push demo-backend --no-route
```

> Use `--no-route` if route quota is full.
> Use `cf map-route` later to add one.

### 6️⃣ Verify Deployment

```bash
cf apps
cf logs demo-backend --recent
```

---

## 📊 Add Prometheus + Grafana

```bash
cf create-service prometheus standard prometheus-service
cf create-service grafana standard grafana-service
cf bind-service demo-backend prometheus-service
cf bind-service prometheus-service grafana-service
cf restage demo-backend
cf service grafana-service
```

View Grafana dashboard URL printed by the last command.

---

## 🔁 Clean-Up & Maintenance

```bash
cf apps                       # List apps
cf delete demo-backend -f -r   # Delete app & routes
cf delete-orphaned-routes -f   # Remove stray routes
cf routes                      # List all routes
cf delete-space dev -f         # Reset Cloud Foundry space
```

---

## 🧰 Cloud Foundry Common Commands

```bash
cf login -a <api-endpoint>            # Login
cf target -o <org> -s <space>         # Select org/space
cf push                               # Deploy app
cf stop <app>                         # Stop app
cf start <app>                        # Start app
cf restage <app>                      # Rebuild app
cf apps                               # List apps
cf routes                             # List routes
cf domains                            # Show available domains
cf delete-orphaned-routes -f          # Clean unused routes
cf logs <app> --recent                # Tail logs
```

---

## 🔄 Continuous Integration / Continuous Deployment (GitHub Actions)

### 🔹 1. Workflow File

Create: `.github/workflows/deploy-to-cf.yml`

```yaml
name: CI/CD to Cloud Foundry with Prometheus & Grafana

on:
  push:
    branches: [ main ]

jobs:
  build-and-deploy:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout repository
        uses: actions/checkout@v4

      - name: Setup Node.js
        uses: actions/setup-node@v4
        with:
          node-version: '24.11.0'

      - name: Build Angular frontend
        working-directory: frontend
        run: |
          npm ci
          npx ng build --configuration production

      - name: Setup Java 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'

      - name: Build Spring Boot backend
        working-directory: backend
        run: |
          rm -rf src/main/resources/static/*
          mkdir -p src/main/resources/static
          cp -r ../frontend/dist/frontend/* src/main/resources/static/
          mvn clean package -DskipTests

      - name: Deploy to Cloud Foundry
        uses: citizen-of-planet-earth/cf-cli-action@v2
        with:
          cf_api: ${{ secrets.CF_API }}
          cf_username: ${{ secrets.CF_USERNAME }}
          cf_password: ${{ secrets.CF_PASSWORD }}
          cf_org: ${{ secrets.CF_ORG }}
          cf_space: ${{ secrets.CF_SPACE }}
          command: push

      - name: Create Prometheus & Grafana services
        run: |
          cf create-service prometheus standard prometheus-service || echo "Prometheus exists"
          cf create-service grafana standard grafana-service || echo "Grafana exists"
          cf bind-service demo-backend prometheus-service || echo "Already bound"
          cf bind-service prometheus-service grafana-service || echo "Already bound"
          cf restage demo-backend
          cf service grafana-service
```

---

### 🔹 2. GitHub Secrets

Add under **Settings → Secrets → Actions**:

| Secret        | Example                                     |
| ------------- | ------------------------------------------- |
| `CF_API`      | `https://api.cf.us10-001.hana.ondemand.com` |
| `CF_USERNAME` | your SAP BTP email                          |
| `CF_PASSWORD` | your SAP BTP password                       |
| `CF_ORG`      | e.g. `e34c8498trial`                        |
| `CF_SPACE`    | e.g. `demo`                                 |

---

### 🔹 3. Verify GitHub Action

After push to `main` →
Go to **GitHub → Actions tab → “CI/CD to Cloud Foundry”** → watch build & deploy logs.

At the end, you’ll see:

```
✅ Angular build done
✅ Backend JAR built
✅ Monitoring stack deployed
```

Your app + metrics are now live in Cloud Foundry!

---

## 📈 Accessing Grafana Dashboards

```bash
cf service grafana-service
```

Check the output for:

```
dashboard_url: https://grafana.cfapps.us10-001.hana.ondemand.com
```

Open that URL → import dashboards:

* JVM Micrometer (ID 4701)
* Spring Boot Statistics (ID 11378)

---

## 💡 Troubleshooting Guide

| Issue                               | Fix                                                                           |
| ----------------------------------- | ----------------------------------------------------------------------------- |
| `Routes quota exceeded`             | Delete routes → `cf delete-orphaned-routes -f`                                |
| `memory_quota_exceeded`             | Lower memory in manifest.yml or delete old apps                               |
| `Org Memory Limit: 0 MB` in cockpit | Disable + Enable Cloud Foundry again                                          |
| `BuildpackCompileFailed`            | Rebuild JAR → ensure `spring-boot-maven-plugin` with `<goal>repackage</goal>` |
| `NG0908` Angular error              | Delete `node_modules`, reinstall, rebuild frontend                            |

---

## 🧾 Project Structure

```
cloudfoundry-full-working-demo/
├── backend/
│   ├── src/
│   │   └── main/java/... (Spring Boot code)
│   ├── pom.xml
│   ├── manifest.yml
│   └── target/demo-0.0.1-SNAPSHOT.jar
├── frontend/
│   ├── src/
│   ├── angular.json
│   ├── package.json
│   └── dist/
└── .github/
    └── workflows/deploy-to-cf.yml
```

---

## 🧠 Key Concepts

| Concept             | Description                                |
| ------------------- | ------------------------------------------ |
| **Spring Boot**     | Backend serving REST APIs and static files |
| **Angular**         | SPA frontend built with TypeScript         |
| **Cloud Foundry**   | PaaS for deploying the full stack          |
| **Prometheus**      | Metrics scraping                           |
| **Grafana**         | Visualization dashboards                   |
| **GitHub Actions**  | CI/CD automation                           |
| **Manifest YAML**   | Cloud Foundry app configuration file       |
| **Service Binding** | Connects CF app ↔ monitoring services      |

---

## 📜 License

This project is provided for educational and demo purposes.
Use and adapt freely under the MIT License.

---

## 🧭 Author

**Yuri**
Full-Stack Cloud Engineer | Spring Boot | Angular | DevOps

---

```

---

✅ **Usage:**  
Save this as `README.md` in the root of your repository.  
It documents every step from local dev → CF deployment → GitHub Actions CI/CD → Prometheus + Grafana integration.  

Would you like me to also generate a **project diagram image (architecture visual)** that you can embed in the README for a professional look?
```
