# rqzb-be

Spring Cloud multi-module backend with MyBatis-Plus, MySQL, Lombok, and Sa-Token.

## Modules

- `rqzb-common`: shared DTOs and utilities
- `rqzb-gateway-service`: API gateway, default port `9000`
- `rqzb-auth-service`: auth service, default port `9001`
- `rqzb-system-service`: system service, default port `9002`
- `rqzb-renqing-service`: renqing and gift business service, default port `9003`
- `rqzb-userinfo-service`: user info service, default port `9004`

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
mvn -pl rqzb-gateway-service spring-boot:run
```

```bash
mvn -pl rqzb-auth-service -am spring-boot:run
```

```bash
mvn -pl rqzb-system-service -am spring-boot:run
```

```bash
mvn -pl rqzb-renqing-service -am spring-boot:run
```

```bash
mvn -pl rqzb-userinfo-service -am spring-boot:run
```

Health checks:

```text
GET http://localhost:9000/api/auth/health
GET http://localhost:9001/api/auth/health
GET http://localhost:9002/api/system/health
GET http://localhost:9003/api/renqing/health
GET http://localhost:9004/api/userinfo/health
```

Gateway routes:

```text
http://localhost:9000/api/auth/**    -> http://localhost:9001/api/auth/**
http://localhost:9000/api/system/**  -> http://localhost:9002/api/system/**
http://localhost:9000/api/renqing/** -> http://localhost:9003/api/renqing/**
http://localhost:9000/api/userinfo/** -> http://localhost:9004/api/userinfo/**
```

Auth tokens are stateless Sa-Token JWT tokens. All services must use the same
`RQZB_JWT_SECRET` value. The local default is shared for development, but set a
real secret in production.

After login, send the returned token with either header:

```text
satoken: <token>
Authorization: Bearer <token>
```

`Authorization: Bearer <token>` is normalized by the gateway to the `satoken`
header expected by the services.

## AI Module

The project now includes `rqzb-ai-module`, which wraps `langchain4j` in a
project-local Spring configuration. This avoids forcing a Spring Boot upgrade
while still making AI capabilities available to every service that depends on
`rqzb-common`.

The default provider is now Qwen through Alibaba Cloud DashScope's
OpenAI-compatible endpoint.

Enable it in the service that needs AI:

```yaml
rqzb:
  ai:
    enabled: true
    api-key: ${DASHSCOPE_API_KEY}
    base-url: https://dashscope.aliyuncs.com/compatible-mode/v1
    chat-model: qwen-plus
    timeout: 30s
    log-requests: false
    log-responses: false
```

After enabling the configuration, inject `dev.langchain4j.model.chat.ChatModel`,
`dev.langchain4j.model.chat.StreamingChatModel`, or
`com.rqzb.ai.service.RqzbAiChatService` in your business service.

`base-url` can also point to another region or another OpenAI-compatible
platform if needed.

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
POST   http://localhost:9002/api/system/ai/chat  (SSE stream)

GET    http://localhost:9002/api/system/roles/page
PUT    http://localhost:9002/api/system/roles/{id}/menus

GET    http://localhost:9002/api/system/depts/tree
GET    http://localhost:9002/api/system/menus/tree
GET    http://localhost:9002/api/system/dict-data/type/{dictType}
```

Most system resources also support the same basic CRUD pattern:
`/page`, `/list`, `/{id}`, `POST`, `PUT /{id}`, and `DELETE /{id}`.

AI example request:

```bash
curl -N -X POST "http://localhost:9002/api/system/ai/chat" \
  -H "Accept: text/event-stream" \
  -H "Content-Type: application/json" \
  -d "{\"message\":\"Introduce this system in one sentence.\"}"
```

The system service example reads `RQZB_AI_ENABLED`, `DASHSCOPE_API_KEY`,
`RQZB_AI_BASE_URL`, `RQZB_AI_CHAT_MODEL`, and `RQZB_AI_TIMEOUT`.

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
GET    http://localhost:9003/api/renqing/thread-pool/example
```

UserInfo service:

```text
GET http://localhost:9004/api/userinfo/health
GET http://localhost:9004/api/userinfo/me
GET http://localhost:9004/api/userinfo/{id}
```
