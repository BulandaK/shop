package com.bulanda.shop;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductHistoryIntegrationTests extends AbstractIntegrationTest {

    @Test
    void recordsCreateUpdateAndDelete() throws Exception {
        long id = createProduct("Wiedzmin", "100.00", "BOOKS", 5);

        mockMvc.perform(put(PRODUCTS + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson("Wiedzmin", "120.00", "BOOKS", 3)))
                .andExpect(status().isOk());
        mockMvc.perform(delete(PRODUCTS + "/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(PRODUCTS + "/{id}/history", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[*].changeType", contains("CREATED", "UPDATED", "DELETED")))
                .andExpect(jsonPath("$[0].changedFields", contains("name", "price", "category", "availableCount")))
                .andExpect(jsonPath("$[0].price").value(100.00))
                .andExpect(jsonPath("$[1].changedFields", contains("price", "availableCount")))
                .andExpect(jsonPath("$[1].price").value(120.00))
                .andExpect(jsonPath("$[1].availableCount").value(3))
                .andExpect(jsonPath("$[2].name").value("Wiedzmin"))
                .andExpect(jsonPath("$[2].availableCount").value(3))
                .andExpect(jsonPath("$[2].changedFields", empty()))
                .andExpect(jsonPath("$[0].changedAt").isString());
    }

    @Test
    void updateWithoutRealChangeDoesNotCreateRevision() throws Exception {
        long id = createProduct("Wiedzmin", "100", "BOOKS", 5);

        // 100 i 100.00 to ta sama cena - Hibernate nie widzi zmiany, więc nie ma UPDATE ani nowej rewizji
        mockMvc.perform(put(PRODUCTS + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson("Wiedzmin", "100.00", "BOOKS", 5)))
                .andExpect(status().isOk());

        mockMvc.perform(get(PRODUCTS + "/{id}/history", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].changeType").value("CREATED"));
    }

    @Test
    void returns404ForProductWithoutHistory() throws Exception {
        mockMvc.perform(get(PRODUCTS + "/{id}/history", 999))
                .andExpect(status().isNotFound());
    }
}
