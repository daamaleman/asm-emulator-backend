package com.uam.asmemulator.instructions;

import com.uam.asmemulator.parser.AsmSyntaxException;
import com.uam.asmemulator.parser.Parser;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InstructionValidationTest {

    private final InstructionFactory factory = new InstructionFactory();

    private AsmSyntaxException invalid(String code) {
        return assertThrows(AsmSyntaxException.class, () -> factory.createAll(Parser.parse(code)));
    }

    @Test
    void rejectsUnknownMnemonic() {
        assertThat(invalid("move eax, 1").getMessage()).contains("MOVE");
    }

    @Test
    void rejectsWrongOperandCount() {
        assertThat(invalid("mov eax").getMessage()).contains("2 operandos");
    }

    @Test
    void rejectsImmediateDestination() {
        assertThat(invalid("mov 5, eax").getMessage()).contains("destino");
    }

    @Test
    void rejectsMismatchedRegisterSizes() {
        assertThat(invalid("add eax, rbx").getMessage()).contains("EAX").contains("RBX");
    }

    @Test
    void rejectsImmediateThatDoesNotFit() {
        assertThat(invalid("mov al, 300").getMessage()).contains("300");
    }

    @Test
    void acceptsNegativeImmediateThatFits() {
        assertDoesNotThrow(() -> factory.createAll(Parser.parse("mov al, -1")));
    }

    @Test
    void rejectsLabelAsDataOperand() {
        assertThat(invalid("inicio:\nmov eax, inicio").getMessage()).contains("inicio");
    }

    @Test
    void rejectsEightBitImul() {
        assertThat(invalid("imul al, bl").getMessage()).contains("8 bits");
    }

    @Test
    void reportsLineOfInvalidInstruction() {
        assertThat(invalid("mov eax, 1\nadd eax").getLine()).isEqualTo(2);
    }

    @Test
    void exposesSupportedMnemonics() {
        assertThat(factory.supportedMnemonics()).contains("MOV", "ADD", "IMUL", "NOT");
    }

    @Test
    void rejectsRegisterAsJumpTarget() {
        assertThat(invalid("jmp eax").getMessage()).contains("etiqueta");
    }

    @Test
    void rejectsNon64BitPush() {
        assertThat(invalid("push eax").getMessage()).contains("64 bits");
    }

    @Test
    void rejectsImmediateAsCompareLeftOperand() {
        assertThat(invalid("cmp 5, eax").getMessage()).contains("registro");
    }

    @Test
    void rejectsRetWithOperands() {
        assertThat(invalid("ret eax").getMessage()).contains("0 operandos");
    }

    @Test
    void supportsJumpAliases() {
        assertThat(factory.supportedMnemonics()).contains("JC", "JNC", "JGE", "JZ", "JA", "CMP", "TEST");
    }
}
