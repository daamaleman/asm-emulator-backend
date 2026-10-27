package com.uam.asmemulator.instructions;

import com.uam.asmemulator.cpu.Register;
import com.uam.asmemulator.parser.model.Operand;
import com.uam.asmemulator.parser.model.ImmediateOperand;
import com.uam.asmemulator.parser.model.ParsedInstruction;
import com.uam.asmemulator.parser.model.Program;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static com.uam.asmemulator.instructions.OperandRules.destination;
import static com.uam.asmemulator.instructions.OperandRules.error;
import static com.uam.asmemulator.instructions.OperandRules.expectCount;
import static com.uam.asmemulator.instructions.OperandRules.source;
import static com.uam.asmemulator.instructions.OperandRules.jumpTarget;
import static com.uam.asmemulator.instructions.OperandRules.register64;

/**
 * Convierte instrucciones del parser en instrucciones ejecutables,
 * validando el mnemónico y sus operandos.
 */
public class InstructionFactory {

    @FunctionalInterface
    public interface Builder {
        Instruction build(ParsedInstruction parsed, Program program);
    }

    @FunctionalInterface
    private interface BinaryConstructor {
        Instruction create(ParsedInstruction parsed, Register destination, Operand source);
    }

    @FunctionalInterface
    private interface UnaryConstructor {
        Instruction create(ParsedInstruction parsed, Register destination);
    }

    private final Map<String, Builder> builders = new HashMap<>();

    public InstructionFactory() {
        binary("MOV", Mov::new);
        binary("ADD", Add::new);
        binary("SUB", Sub::new);
        binary("AND", And::new);
        binary("OR", Or::new);
        binary("XOR", Xor::new);
        binary("CMP", Cmp::new);
        binary("TEST", TestInstruction::new);

        register("IMUL", (p, program) -> {
            expectCount(p, 2);
            Register d = destination(p);
            if (d.bits() == 8) {
                throw error(p, "IMUL de dos operandos no admite registros de 8 bits");
            }
            return new Imul(p, d, source(p, d));
        });

        // Saltos
        register("JMP", (p, program) -> new Jump(p, jumpTarget(p, program)));
        ConditionalJump.MNEMONICS.forEach((mnemonic, condition) ->
                register(mnemonic, (p, program) ->
                        new ConditionalJump(p, condition, jumpTarget(p, program))));

        // Pila y retorno
        register("PUSH", (p, program) -> {
            expectCount(p, 1);
            Operand operand = p.operands().get(0);
            if (operand instanceof ImmediateOperand i) {
                if (i.value() < Integer.MIN_VALUE || i.value() > Integer.MAX_VALUE) {
                    throw error(p, "PUSH admite inmediatos de hasta 32 bits con signo");
                }
                return new Push(p, operand);
            }
            register64(p);
            return new Push(p, operand);
        });
        register("POP", (p, program) -> {
            expectCount(p, 1);
            return new Pop(p, register64(p));
        });
        register("RET", (p, program) -> {
            expectCount(p, 0);
            return new Ret(p);
        });

        unary("INC", Inc::new);
        unary("DEC", Dec::new);
        unary("NEG", Neg::new);
        unary("NOT", Not::new);
    }

    public Instruction create(ParsedInstruction parsed, Program program) {
        Builder builder = builders.get(parsed.mnemonic());
        if (builder == null) {
            throw error(parsed, "Instrucción desconocida '%s'".formatted(parsed.mnemonic()));
        }
        return builder.build(parsed, program);
    }

    public List<Instruction> createAll(Program program) {
        return program.instructions().stream()
                .map(parsed -> create(parsed, program))
                .toList();
    }

    public Set<String> supportedMnemonics() {
        return Collections.unmodifiableSet(new TreeSet<>(builders.keySet()));
    }

    protected final void register(String mnemonic, Builder builder) {
        builders.put(mnemonic, builder);
    }

    private void binary(String mnemonic, BinaryConstructor constructor) {
        register(mnemonic, (p, program) -> {
            expectCount(p, 2);
            Register d = destination(p);
            return constructor.create(p, d, source(p, d));
        });
    }

    private void unary(String mnemonic, UnaryConstructor constructor) {
        register(mnemonic, (p, program) -> {
            expectCount(p, 1);
            return constructor.create(p, destination(p));
        });
    }
}
