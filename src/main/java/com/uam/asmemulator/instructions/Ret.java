package com.uam.asmemulator.instructions;

import com.uam.asmemulator.cpu.Cpu;
import com.uam.asmemulator.parser.model.ParsedInstruction;

/**
 * RET: regresa de la función. Como el emulador no soporta CALL,
 * RET termina la ejecución y el valor de retorno queda en EAX/RAX.
 */
public final class Ret extends AbstractInstruction {

    public Ret(ParsedInstruction parsed) {
        super(parsed);
    }

    @Override
    public void execute(Cpu cpu) {
        cpu.halt();
    }
}

