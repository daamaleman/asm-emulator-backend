package com.uam.asmemulator.api;

import com.uam.asmemulator.api.dto.EmulateRequest;
import com.uam.asmemulator.api.dto.EmulateResponse;
import com.uam.asmemulator.cpu.Bits;
import com.uam.asmemulator.cpu.Register;
import com.uam.asmemulator.emulator.Emulator;
import com.uam.asmemulator.parser.AsmSyntaxException;
import com.uam.asmemulator.parser.NumberLiteral;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class EmulationService {

    private final Emulator emulator;

    public EmulationService(Emulator emulator) {
        this.emulator = emulator;
    }

    public EmulateResponse emulate(EmulateRequest request) {
        Map<Register, Long> initialValues = parseInitialRegisters(request.initialRegisters());
        return EmulationMapper.toResponse(emulator.run(request.code(), initialValues));
    }

    private Map<Register, Long> parseInitialRegisters(Map<String, String> raw) {
        Map<Register, Long> result = new LinkedHashMap<>();
        if (raw == null) {
            return result;
        }
        raw.forEach((name, text) -> {
            Register register = Register.fromName(name.strip())
                    .orElseThrow(() -> new InvalidRequestException("Registro desconocido: '" + name + "'"));
            if (text == null || text.isBlank()) {
                return; // vacío = no se inicializa
            }
            long value;
            try {
                value = NumberLiteral.parse(text.strip(), 0, 0);
            } catch (AsmSyntaxException e) {
                throw new InvalidRequestException("Valor inválido para %s: '%s'".formatted(register, text));
            }
            if (!Bits.fits(value, register.bits())) {
                throw new InvalidRequestException("El valor %s no cabe en %s (%d bits)"
                        .formatted(text, register, register.bits()));
            }
            result.put(register, value);
        });
        return result;
    }
}

