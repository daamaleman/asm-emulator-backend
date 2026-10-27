package com.uam.asmemulator.api;

import com.uam.asmemulator.api.dto.EmulateRequest;
import com.uam.asmemulator.api.dto.EmulateResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class EmulatorController {

    private final EmulationService service;

    public EmulatorController(EmulationService service) {
        this.service = service;
    }

    @PostMapping("/emulate")
    public EmulateResponse emulate(@Valid @RequestBody EmulateRequest request) {
        return service.emulate(request);
    }
}