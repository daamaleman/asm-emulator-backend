package com.uam.asmemulator.emulator;

public enum StopReason {
    END_OF_PROGRAM("Se ejecutó la última instrucción"),
    RETURN("RET: la función terminó; el valor de retorno está en EAX"),
    STEP_LIMIT("Se alcanzó el límite de pasos (¿ciclo infinito?)");

    private final String description;

    StopReason(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }
}

