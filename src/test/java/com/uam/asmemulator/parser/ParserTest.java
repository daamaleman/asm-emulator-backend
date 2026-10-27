package com.uam.asmemulator.parser;

import com.uam.asmemulator.cpu.Register;
import com.uam.asmemulator.parser.model.ImmediateOperand;
import com.uam.asmemulator.parser.model.LabelOperand;
import com.uam.asmemulator.parser.model.ParsedInstruction;
import com.uam.asmemulator.parser.model.Program;
import com.uam.asmemulator.parser.model.RegisterOperand;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParserTest {

    @Test
    void parsesSumarExample() {
        Program program = Parser.parse("""
                sumar:
                    mov eax, edi  ; eax = a
                    add eax, esi  ; eax = a + b
                    ret           ; devuelve eax
                """);

        assertThat(program.instructions().stream().map(ParsedInstruction::mnemonic).toList())
                .containsExactly("MOV", "ADD", "RET");
        assertThat(program.labels()).containsEntry("sumar", 0);
        assertThat(program.instructions().get(0).operands())
                .containsExactly(new RegisterOperand(Register.EAX), new RegisterOperand(Register.EDI));
        assertThat(program.instructions().get(2).operands()).isEmpty();
    }

    @Test
    void resolvesLabelsInWhileLoop() {
        Program program = Parser.parse("""
                contar:
                    xor eax, eax
                inicio:
                    cmp eax, 3
                    jge fin
                    add eax, 1
                    jmp inicio
                fin:
                    ret
                """);

        assertThat(program.size()).isEqualTo(6);
        assertThat(program.addressOf("contar")).isZero();
        assertThat(program.addressOf("inicio")).isEqualTo(1);
        assertThat(program.addressOf("fin")).isEqualTo(5);
        assertThat(program.instructions().get(1).operands())
                .containsExactly(new RegisterOperand(Register.EAX), new ImmediateOperand(3));
        assertThat(program.instructions().get(2).operands())
                .containsExactly(new LabelOperand("fin"));
    }

    @Test
    void acceptsLabelAndInstructionOnSameLine() {
        Program program = Parser.parse("""
                mov eax, 0
                mov ecx, 1
                inicio: add eax, ecx
                add ecx, 1
                cmp ecx, 5
                jl inicio
                ret
                """);

        assertThat(program.addressOf("inicio")).isEqualTo(2);
        assertThat(program.instructions().get(2).mnemonic()).isEqualTo("ADD");
        assertThat(program.instructions().get(2).sourceText()).isEqualTo("inicio: add eax, ecx");
    }

    @Test
    void parsesImmediateFormats() {
        Program program = Parser.parse("mov eax, -3\nmov ebx, 0x1F\nmov ecx, 0FFh\nmov edx, 0b101");

        assertThat(program.instructions().stream().map(i -> i.operands().get(1)).toList())
                .containsExactly(
                        new ImmediateOperand(-3),
                        new ImmediateOperand(31),
                        new ImmediateOperand(255),
                        new ImmediateOperand(5));
    }

    @Test
    void isCaseInsensitiveForMnemonicsAndRegisters() {
        ParsedInstruction instruction = Parser.parse("MoV EAX, Edi").instructions().get(0);

        assertThat(instruction.mnemonic()).isEqualTo("MOV");
        assertThat(instruction.operands())
                .containsExactly(new RegisterOperand(Register.EAX), new RegisterOperand(Register.EDI));
    }

    @Test
    void emptySourceProducesEmptyProgram() {
        assertThat(Parser.parse("  ; solo un comentario\n\n").size()).isZero();
    }

    @Test
    void rejectsUndefinedLabel() {
        AsmSyntaxException ex = assertThrows(AsmSyntaxException.class,
                () -> Parser.parse("cmp eax, 3\njge fin"));
        assertThat(ex.getLine()).isEqualTo(2);
        assertThat(ex.getMessage()).contains("fin");
    }

    @Test
    void rejectsDuplicateLabel() {
        AsmSyntaxException ex = assertThrows(AsmSyntaxException.class,
                () -> Parser.parse("a:\nret\na:\nret"));
        assertThat(ex.getLine()).isEqualTo(3);
    }

    @Test
    void rejectsMissingComma() {
        AsmSyntaxException ex = assertThrows(AsmSyntaxException.class,
                () -> Parser.parse("mov eax edi"));
        assertThat(ex.getLine()).isEqualTo(1);
        assertThat(ex.getMessage()).contains("edi");
    }

    @Test
    void rejectsInvalidNumber() {
        assertThrows(AsmSyntaxException.class, () -> Parser.parse("mov eax, 0xZZ"));
    }

    @Test
    void rejectsRegisterAsLabel() {
        assertThrows(AsmSyntaxException.class, () -> Parser.parse("eax:\nret"));
    }
}