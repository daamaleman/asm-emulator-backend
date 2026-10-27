package com.uam.asmemulator.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class EmulatorControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    private ResultActions emulate(String json) throws Exception {
        return mvc.perform(post("/api/emulate").contentType(MediaType.APPLICATION_JSON).content(json));
    }

    @Test
    void emulatesMayorExample() throws Exception {
        emulate("""
                {
                  "code": "mayor:\\n  mov eax, edi\\n  cmp edi, esi\\n  jg fin\\n  mov eax, esi\\nfin:\\n  ret",
                  "initialRegisters": {"EDI": "8", "ESI": "5"}
                }
                """)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stopReason").value("RETURN"))
                .andExpect(jsonPath("$.steps").value(4))
                .andExpect(jsonPath("$.finalState.registers[0].name").value("RAX"))
                .andExpect(jsonPath("$.finalState.registers[0].signed").value("8"))
                .andExpect(jsonPath("$.trace[0].changedRegisters[0]").value("RAX"))
                .andExpect(jsonPath("$.trace[2].instruction").value("jg fin"))
                .andExpect(jsonPath("$.trace[2].jumpTaken").value(true));
    }

    @Test
    void acceptsHexInitialValues() throws Exception {
        emulate("""
                {"code": "mov eax, edi\\nret", "initialRegisters": {"edi": "0x10"}}
                """)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.finalState.registers[0].signed").value("16"))
                .andExpect(jsonPath("$.finalState.registers[0].hex").value("0x0000000000000010"));
    }

    @Test
    void returnsSyntaxErrorWithPosition() throws Exception {
        emulate("""
                {"code": "mov eax, 1\\nmove eax, 2"}
                """)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("SYNTAX_ERROR"))
                .andExpect(jsonPath("$.line").value(2));
    }

    @Test
    void rejectsUnknownInitialRegister() throws Exception {
        emulate("""
                {"code": "ret", "initialRegisters": {"XYZ": "1"}}
                """)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("INVALID_REQUEST"));
    }

    @Test
    void rejectsBlankCode() throws Exception {
        emulate("""
                {"code": ""}
                """)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));
    }

    @Test
    void rejectsMalformedJson() throws Exception {
        emulate("{ esto no es json")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("INVALID_JSON"));
    }

    @Test
    void listsClassExamples() throws Exception {
        mvc.perform(get("/api/examples"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("sumar"))
                .andExpect(jsonPath("$[0].code").isNotEmpty());
    }
}
