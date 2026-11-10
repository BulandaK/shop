package com.bulanda.shop;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerIntegrationTests extends AbstractIntegrationTest {

    @Test
    void createsProductAndReturnsItById() throws Exception {
        mockMvc.perform(post(PRODUCTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson("Laptop", "2500.00", "ELECTRONICS", 7)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(2500.00))
                .andExpect(jsonPath("$.category").value("ELECTRONICS"))
                .andExpect(jsonPath("$.availableCount").value(7));

        long id = createProduct("Telefon", "1200", "ELECTRONICS", 3);

        mockMvc.perform(get(PRODUCTS + "/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Telefon"));
    }

    @Test
    void listsAllProducts() throws Exception {
        createProduct("Laptop", "2500", "ELECTRONICS", 1);
        createProduct("Wiedzmin", "45", "BOOKS", 10);

        mockMvc.perform(get(PRODUCTS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("Laptop", "Wiedzmin")));
    }

    @Test
    void returns404ForMissingProduct() throws Exception {
        mockMvc.perform(get(PRODUCTS + "/{id}", 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Produkt o takim id nie istnieje"));
    }

    @Test
    void rejectsInvalidInput() throws Exception {
        mockMvc.perform(post(PRODUCTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "ab", "category": "BOOKS", "availableCount": -1}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Niepoprawne dane wejściowe"))
                .andExpect(jsonPath("$.errors.name").exists())
                .andExpect(jsonPath("$.errors.price").value("Cena jest wymagana"))
                .andExpect(jsonPath("$.errors.availableCount").value("Ilość nie może być ujemna"));
    }

    @Test
    void rejectsUnknownCategory() throws Exception {
        mockMvc.perform(post(PRODUCTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson("Laptop", "2500", "FOOD", 1)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.category", containsString("ELECTRONICS, BOOKS, CLOTHES")));
    }

    @Test
    void rejectsPriceOutsideCategoryRange() throws Exception {
        mockMvc.perform(post(PRODUCTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson("Droga ksiazka", "1000", "BOOKS", 1)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("cena sie nie zgadza z przedziałem dla kategorii"));
    }

    @Test
    void rejectsDuplicateName() throws Exception {
        createProduct("Laptop", "2500", "ELECTRONICS", 1);

        mockMvc.perform(post(PRODUCTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson("Laptop", "3000", "ELECTRONICS", 2)))
                .andExpect(status().isConflict());
    }

    @Test
    void rejectsNameWithForbiddenPhrase() throws Exception {
        addForbiddenPhrase("tani");

        mockMvc.perform(post(PRODUCTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson("Super TANI laptop", "2500", "ELECTRONICS", 1)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail", containsString("tani")));
    }

    @Test
    void updatesProduct() throws Exception {
        long id = createProduct("Laptop", "2500", "ELECTRONICS", 1);

        mockMvc.perform(put(PRODUCTS + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson("Laptop Pro", "4000", "ELECTRONICS", 5)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop Pro"))
                .andExpect(jsonPath("$.price").value(4000))
                .andExpect(jsonPath("$.availableCount").value(5));

        mockMvc.perform(get(PRODUCTS + "/{id}", id))
                .andExpect(jsonPath("$.name").value("Laptop Pro"));
    }

    @Test
    void rejectsUpdateToNameOfAnotherProduct() throws Exception {
        createProduct("Laptop", "2500", "ELECTRONICS", 1);
        long id = createProduct("Telefon", "1200", "ELECTRONICS", 1);

        mockMvc.perform(put(PRODUCTS + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson("Laptop", "1200", "ELECTRONICS", 1)))
                .andExpect(status().isConflict());
    }

    @Test
    void returns404WhenUpdatingMissingProduct() throws Exception {
        mockMvc.perform(put(PRODUCTS + "/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson("Laptop", "2500", "ELECTRONICS", 1)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletesProduct() throws Exception {
        long id = createProduct("Laptop", "2500", "ELECTRONICS", 1);

        mockMvc.perform(delete(PRODUCTS + "/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(PRODUCTS + "/{id}", id))
                .andExpect(status().isNotFound());
    }
}
