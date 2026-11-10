package com.bulanda.shop;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ForbiddenPhraseControllerIntegrationTests extends AbstractIntegrationTest {

    @Test
    void normalizesPhraseOnCreate() throws Exception {
        mockMvc.perform(post(FORBIDDEN_PHRASES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"phrase": "  BaD "}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.phrase").value("bad"));
    }

    @Test
    void rejectsDuplicateIgnoringCase() throws Exception {
        addForbiddenPhrase("bad");

        mockMvc.perform(post(FORBIDDEN_PHRASES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"phrase": "BAD"}
                                """))
                .andExpect(status().isConflict());
    }

    @Test
    void rejectsBlankAndTooLongPhrase() throws Exception {
        mockMvc.perform(post(FORBIDDEN_PHRASES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"phrase": "   "}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.phrase").value("Fraza jest wymagana"));

        mockMvc.perform(post(FORBIDDEN_PHRASES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"phrase": "%s"}
                                """.formatted("x".repeat(21))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.phrase").value("Fraza może mieć maksymalnie 20 znaków"));
    }

    @Test
    void deletesPhrase() throws Exception {
        long id = addForbiddenPhrase("bad");

        mockMvc.perform(delete(FORBIDDEN_PHRASES + "/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(delete(FORBIDDEN_PHRASES + "/{id}", id))
                .andExpect(status().isNotFound());
    }
}
