# Cloud Foundry Full Working Demo

Spring Boot (Java 17, Maven) + Angular 20.3.8 (Node 24.11.0, npm 7.14.0) with Cloud Foundry deployment.

## Quick Start (Cloud Foundry US10)

1) Log in
```bash
cf login -a https://api.cf.us10.hana.ondemand.com
# select your Org (trial) and Space (dev)
cf target
```

2) Deploy backend
```bash
cd backend
mvn package -DskipTests
cf push
# note printed URL e.g. https://demo-backend.cfapps.us10.hana.ondemand.com
```

3) Build & deploy frontend
```bash
cd ../frontend
npm i -g npm@7.14.0
npm install
npx ng build --configuration production
cf push
# e.g. https://demo-frontend.cfapps.us10.hana.ondemand.com
```

4) Map /api to backend on the frontend host
```bash
cf map-route demo-backend cfapps.us10.hana.ondemand.com --hostname demo-frontend --path api
```

5) Test
- Frontend: https://demo-frontend.cfapps.us10.hana.ondemand.com
- Frontend->Backend: https://demo-frontend.cfapps.us10.hana.ondemand.com/api/hello
- Metrics: https://demo-backend.cfapps.us10.hana.ondemand.com/actuator/prometheus

## Troubleshooting
- See apps: `cf apps`
- Logs: `cf logs <app> --recent`
- Routes: `cf routes`
- Unmap route: `cf unmap-route demo-backend cfapps.us10.hana.ondemand.com --hostname demo-frontend --path api`

## Optional: GitHub Actions deploy
Set repo secrets: CF_API_DOMAIN, CF_USERNAME, CF_PASSWORD, CF_ORG, CF_SPACE.
On push, the workflow builds and deploys both apps.
