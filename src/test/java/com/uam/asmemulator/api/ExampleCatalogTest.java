package com.uam.asmemulator.api;

import com.uam.asmemulator.api.dto.EmulateRequest;
import com.uam.asmemulator.api.dto.EmulateResponse;
import com.uam.asmemulator.api.dto.ExampleDto;
import com.uam.asmemulator.cpu.Bits;
import com.uam.asmemulator.emulator.Emulator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class ExampleCatalogTest {

    private final ExampleCatalog catalog = new ExampleCatalog();
    private final EmulationService service = new EmulationService(new Emulator());

    private EmulateResponse run(ExampleDto example) {
        return service.emulate(new EmulateRequest(example.code(), example.initialRegisters()));
    }

    @Test
    void idsAreUnique() {
        assertThat(catalog.all().stream().map(ExampleDto::id).distinct().count())
                .isEqualTo(catalog.all().size());
    }

    @Test
    void everyExampleRunsUntilRet() {
        for (ExampleDto example : catalog.all()) {
            assertThat(run(example).stopReason()).as(example.id()).isEqualTo("RETURN");
        }
    }

    @ParameterizedTest(name = "{0} → EAX = {1}")
    @CsvSource({
            "sumar, 8", "operar, 6", "misterio, 10", "producto-menos-uno, 11",
            "mayor, 8", "acceso, 0", "contar, 3", "sumar-1-a-4, 10",
            "absoluto, 3", "do-while, 10", "acarreo, 1", "ja-vs-jg, 1"})
    void exampleReturnsExpectedEax(String id, long expected) {
        EmulateResponse response = run(catalog.findById(id).orElseThrow());
        long rax = Long.parseUnsignedLong(response.finalState().registers().get(0).unsigned());

        assertThat(Bits.signExtend(rax, 32)).isEqualTo(expected);
    }
}