package com.uam.asmemulator.api;

import com.uam.asmemulator.api.dto.ExampleDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ExamplesController {

    private final ExampleCatalog catalog;

    public ExamplesController(ExampleCatalog catalog) {
        this.catalog = catalog;
    }

    @GetMapping("/examples")
    public List<ExampleDto> examples() {
        return catalog.all();
    }
}