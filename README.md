# ASM Emulator – Backend

API REST (Spring Boot) de un emulador didáctico de ensamblador x86-64
(sintaxis Intel) con traducción a C++.

## Requisitos
- Java 25+
- Maven (incluido mediante `mvnw`)

## Ejecutar
```bash
./mvnw spring-boot:run
```
API disponible en `http://localhost:8080/api`.

## Endpoints
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/health` | Estado del servicio |
| POST | `/api/emulate` | Ejecuta código ensamblador *(próximamente)* |
| POST | `/api/translate` | Traduce a C++ *(próximamente)* |
| GET | `/api/examples` | Ejemplos de clase *(próximamente)* |
