# Local Kubernetes (Rancher Desktop) setup

This project runs locally in Kubernetes (Rancher Desktop) with Keycloak outside the cluster (Docker Compose).

## Prerequisites
- Rancher Desktop with **Kubernetes enabled** and **dockerd** mode
- `kubectl`
- `docker` / `docker compose`

---

## K8S (without Helm)

### 1. Start Keycloak (Docker Compose)

From the project root (or the folder with `docker-compose.yml`):

```bash
docker compose up -d keycloak keycloak-db
```

Check it is up:

```bash
curl -f http://localhost:18080/health/ready
```

Open Keycloak Admin Console:
- http://localhost:18080
- Admin credentials (from compose): admin / admin

> Note: Keycloak must be reachable from Kubernetes pods. Use a host IP (e.g. `http://<HOST_IP>:18080/realms/my-bank`) in K8S ConfigMaps (not `host.docker.internal`).

### 2. Create a user in Keycloak

In Keycloak:
- Select realm: my-bank
- Go to Users → Create user
- Set username (login), first/last name, email (optional)
- Create the user
- Add realm roles — `ACCOUNTS`, `CASH`, `TRANSFER`
- Go to Credentials → set password (disable “Temporary” if needed)
- Copy the user ID (UUID) from the user page — you will insert it into the accounts database.

### 3. Create Kubernetes namespace

```bash
kubectl apply -f k8s/namespace.yaml
```

### 4. Deploy databases (StatefulSets)

Apply all DB manifests:

```bash
kubectl apply -f k8s/databases/
```

Wait until all DB pods are running:

```
kubectl -n dev get pods
```

### 5. Seed users in accounts-db

Open psql inside the Postgres pod:

```bash
kubectl -n dev exec -it accounts-db-0 -- psql -U admin -d accounts
```

Insert the Keycloak user (replace UUID and user fields):

```bash
INSERT INTO accounts.customers (customer_id, login, first_name, last_name, birth_date)
VALUES ('<KEYCLOAK_USER_UUID>', '<login>', '<first_name>', '<last_name>', '<YYYY-MM-DD>');
```

Insert additional test users (no Keycloak required):

```bash
INSERT INTO accounts.customers (customer_id, login, first_name, last_name, birth_date)
VALUES
  (gen_random_uuid(), 'testuser1', 'Test', 'User1', '1990-02-02'),
  (gen_random_uuid(), 'testuser2', 'Test', 'User2', '1991-03-03');
```

Create primary wallets for all customers who don’t have one yet:

```bash
INSERT INTO accounts.wallets (wallet_id, customer_id, balance, is_primary)
SELECT gen_random_uuid(), c.customer_id, 0, true
FROM accounts.customers c
WHERE NOT EXISTS (
    SELECT 1
    FROM accounts.wallets w
    WHERE w.customer_id = c.customer_id
      AND w.is_primary = true
);
```

### 6. Deploy all microservices

```
kubectl apply -f k8s/services/
```

### 7. Open application in browser

Front controller is exposed via NodePort:

```bash
http://localhost:30080
```

Login using the user created in Keycloak.