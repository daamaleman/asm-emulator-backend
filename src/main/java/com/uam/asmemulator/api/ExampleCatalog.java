package com.uam.asmemulator.api;

import com.uam.asmemulator.api.dto.ExampleDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Ejemplos tomados de las presentaciones del curso.
 */
@Component
public class ExampleCatalog {

    private static final String S1 = "Sesión 1";
    private static final String S2 = "Sesión 2";

    private static final List<ExampleDto> EXAMPLES = List.of(
            new ExampleDto("sumar", "Sumar dos enteros", S1,
                    "int sumar(int a, int b) { return a + b; }",
                    """
                    sumar:
                        mov eax, edi  ; eax = a
                        add eax, esi  ; eax = a + b
                        ret           ; devuelve eax
                    """,
                    Map.of("EDI", "3", "ESI", "5")),

            new ExampleDto("operar", "Resta y multiplicación", S1,
                    "int operar(int a, int b) { return (a - b) * 2; }",
                    """
                    operar:
                        mov eax, edi   ; a
                        sub eax, esi   ; a - b
                        add eax, eax   ; x2
                        ret
                    """,
                    Map.of("EDI", "7", "ESI", "4")),

            new ExampleDto("misterio", "Ensamblador a C: misterio", S1,
                    "int misterio(int a, int b) { return a - b + 5; }",
                    """
                    misterio:
                        mov eax, edi
                        sub eax, esi
                        add eax, 5
                        ret
                    """,
                    Map.of("EDI", "8", "ESI", "3")),

            new ExampleDto("producto-menos-uno", "Ejercicio 3: IMUL", S1,
                    "int f(int a, int b) { return a * b - 1; }",
                    """
                        mov eax, edi
                        imul eax, esi
                        sub eax, 1
                        ret
                    """,
                    Map.of("EDI", "4", "ESI", "3")),

            new ExampleDto("mayor", "Si a > b, devuelve a", S2,
                    "int mayor(int a, int b) { if (a > b) return a; return b; }",
                    """
                    mayor:
                        mov eax, edi
                        cmp edi, esi
                        jg fin
                        mov eax, esi
                    fin:
                        ret
                    """,
                    Map.of("EDI", "8", "ESI", "5")),

            new ExampleDto("acceso", "IF/ELSE: clasificar edad", S2,
                    "int acceso(int edad) { if (edad >= 18) return 1; else return 0; }",
                    """
                    acceso:
                        cmp edi, 18
                        jl menor
                        mov eax, 1
                        ret
                    menor:
                        xor eax, eax
                        ret
                    """,
                    Map.of("EDI", "17")),

            new ExampleDto("contar", "WHILE: contador", S2,
                    "int contar(void) { int x = 0; while (x < 3) x++; return x; }",
                    """
                    contar:
                        xor eax, eax
                    inicio:
                        cmp eax, 3
                        jge fin
                        add eax, 1
                        jmp inicio
                    fin:
                        ret
                    """,
                    Map.of()),

            new ExampleDto("sumar-1-a-4", "FOR: sumar de 1 a 4", S2,
                    "int sumar_1_a_4(void) { int total = 0; for (int i = 1; i <= 4; i++) total += i; return total; }",
                    """
                        xor eax, eax   ; total=0
                        mov ecx, 1     ; i=1
                    inicio:
                        cmp ecx, 4
                        jg fin
                        add eax, ecx
                        add ecx, 1
                        jmp inicio
                    fin:
                        ret
                    """,
                    Map.of()),

            new ExampleDto("absoluto", "Ejercicio 2: valor absoluto", S2,
                    "int absoluto(int x) { if (x < 0) return -x; return x; }",
                    """
                    absoluto:
                        mov eax, edi
                        cmp eax, 0
                        jge fin
                        neg eax
                    fin:
                        ret
                    """,
                    Map.of("EDI", "-3")),

            new ExampleDto("do-while", "Ejercicio 3: comparar al final", S2,
                    "int f(void) { int total = 0, i = 1; do { total += i; i++; } while (i < 5); return total; }",
                    """
                        mov eax, 0
                        mov ecx, 1
                    inicio:
                        add eax, ecx
                        add ecx, 1
                        cmp ecx, 5
                        jl inicio
                        ret
                    """,
                    Map.of()),

            new ExampleDto("acarreo", "JC: detectar acarreo sin signo", S2,
                    "unsigned char x = 255; x = x + 1; /* ¿hubo acarreo? */",
                    """
                    acarreo:
                        mov ebx, 0xFF   ; BL = 255
                        add bl, 1       ; 256 no cabe en 8 bits → CF=1
                        jc hubo_acarreo
                        mov eax, 0
                        ret
                    hubo_acarreo:
                        mov eax, 1
                        ret
                    """,
                    Map.of()),

            new ExampleDto("ja-vs-jg", "Ejercicio 4: JA con números negativos", S2,
                    "int mayor(int a, int b) { return a > b; }  // con a=-1, b=1 debe dar 0",
                    """
                    ; JA compara SIN signo: -1 se ve como 0xFFFFFFFF
                    ; Cambia JA por JG para obtener el resultado correcto
                        mov eax, 0
                        cmp edi, esi
                        ja es_mayor
                        ret
                    es_mayor:
                        mov eax, 1
                        ret
                    """,
                    Map.of("EDI", "-1", "ESI", "1")));

    public List<ExampleDto> all() {
        return EXAMPLES;
    }

    public Optional<ExampleDto> findById(String id) {
        return EXAMPLES.stream().filter(e -> e.id().equals(id)).findFirst();
    }
}