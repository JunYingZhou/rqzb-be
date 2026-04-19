# rqzb-be

Spring Cloud multi-module backend with MyBatis-Plus, MySQL, Lombok, and Sa-Token.

## Modules

- `rqzb-common`: shared DTOs and utilities
- `rqzb-auth-service`: auth service, default port `9001`
- `rqzb-system-service`: system service, default port `9002`
- `rqzb-renqing-service`: renqing and gift business service, default port `9003`

## Requirements

- JDK 17
- Maven 3.6+
- MySQL 8+

## Run

The default local datasource is `localhost:3306/rqzb` with user `root`.
Initialize the database before starting services:

```bash
mysql -uroot -p < sql/rqzb_schema.sql
mysql -uroot -p < sql/rqzb_seed.sql
mysql -uroot -p < sql/rqzb_business_schema.sql
```

Default development account:

```text
username: admin
password: admin123
```

Start one service:

```bash
mvn -pl rqzb-auth-service -am spring-boot:run
```

```bash
mvn -pl rqzb-system-service -am spring-boot:run
```

```bash
mvn -pl rqzb-renqing-service -am spring-boot:run
```

Health checks:

```text
GET http://localhost:9001/api/auth/health
GET http://localhost:9002/api/system/health
GET http://localhost:9003/api/renqing/health
```

## Basic APIs

Auth service:

```text
POST http://localhost:9001/api/auth/login
POST http://localhost:9001/api/auth/logout
GET  http://localhost:9001/api/auth/me
```

System service:

```text
GET    http://localhost:9002/api/system/users/page
POST   http://localhost:9002/api/system/users
PUT    http://localhost:9002/api/system/users/{id}
DELETE http://localhost:9002/api/system/users/{id}
PUT    http://localhost:9002/api/system/users/{id}/roles
PUT    http://localhost:9002/api/system/users/{id}/posts

GET    http://localhost:9002/api/system/roles/page
PUT    http://localhost:9002/api/system/roles/{id}/menus

GET    http://localhost:9002/api/system/depts/tree
GET    http://localhost:9002/api/system/menus/tree
GET    http://localhost:9002/api/system/dict-data/type/{dictType}
```

Most system resources also support the same basic CRUD pattern:
`/page`, `/list`, `/{id}`, `POST`, `PUT /{id}`, and `DELETE /{id}`.

Renqing service:

```text
GET    http://localhost:9003/api/renqing/persons/page
POST   http://localhost:9003/api/renqing/persons
PUT    http://localhost:9003/api/renqing/persons/{id}
DELETE http://localhost:9003/api/renqing/persons/{id}
GET    http://localhost:9003/api/renqing/persons/name/{name}

GET    http://localhost:9003/api/renqing/records/page
POST   http://localhost:9003/api/renqing/records
PUT    http://localhost:9003/api/renqing/records/{id}
DELETE http://localhost:9003/api/renqing/records/{id}
GET    http://localhost:9003/api/renqing/records/name/{name}
GET    http://localhost:9003/api/renqing/records/type/{type}
GET    http://localhost:9003/api/renqing/records/occasion/{occasion}

GET    http://localhost:9003/api/renqing/gift-records/page
POST   http://localhost:9003/api/renqing/gift-records
PUT    http://localhost:9003/api/renqing/gift-records/{id}
DELETE http://localhost:9003/api/renqing/gift-records/{id}
GET    http://localhost:9003/api/renqing/gift-records/person/{personId}
GET    http://localhost:9003/api/renqing/gift-records/direction/{direction}

GET    http://localhost:9003/api/renqing/settings/page
POST   http://localhost:9003/api/renqing/settings
PUT    http://localhost:9003/api/renqing/settings/{settingKey}
DELETE http://localhost:9003/api/renqing/settings/{settingKey}

GET    http://localhost:9003/api/renqing/gift-record-views/page
GET    http://localhost:9003/api/renqing/gift-record-views/person/{personId}
GET    http://localhost:9003/api/renqing/summary/yearly
GET    http://localhost:9003/api/renqing/summary/yearly/{year}
```
