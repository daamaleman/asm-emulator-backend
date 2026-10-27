package com.uam.asmemulator.config;

import com.uam.asmemulator.emulator.Emulator;
import com.uam.asmemulator.instructions.InstructionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmulatorConfig {

    @Bean
    public InstructionFactory instructionFactory() {
        return new InstructionFactory();
    }

    @Bean
    public Emulator emulator(InstructionFactory factory,
                             @Value("${app.emulator.max-steps}") int maxSteps) {
        return new Emulator(factory, maxSteps);
    }
}

