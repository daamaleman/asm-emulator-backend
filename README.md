# ASM Emulator – Backend

API REST (Spring Boot) de un emulador didáctico de ensamblador x86-64
(sintaxis Intel) con traducción a C++.

## Requisitos
- Java 21+
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
| POST | `/api/emulate` | Ejecuta código ensamblador y devuelve la traza |
| GET | `/api/examples` | Ejemplos de las presentaciones |
| POST | `/api/translate` | Traduce a C++ *(próximamente)* |

### POST /api/emulate
```json
{
  "code": "mov eax, edi\nadd eax, esi\nret",
  "initialRegisters": { "EDI": "3", "ESI": "0x5" }
}
```
Respuesta: `initialState`, `finalState`, `trace[]` (una fila por instrucción,
con `jumpTaken` y `changedRegisters`), `steps`, `stopReason`
(`RETURN`, `END_OF_PROGRAM`, `STEP_LIMIT`) y `stopMessage`.

Los registros se envían como texto (`hex`, `unsigned`, `signed`) porque
JavaScript no representa con exactitud enteros mayores a 2^53.

### Errores (400)
```json
{ "error": "SYNTAX_ERROR", "message": "Instrucción desconocida 'MOVE'", "line": 2, "column": 1 }
```
Tipos: `SYNTAX_ERROR`, `INVALID_REQUEST`, `VALIDATION_ERROR`, `INVALID_JSON`.