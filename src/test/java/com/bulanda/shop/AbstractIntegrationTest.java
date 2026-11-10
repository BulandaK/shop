package com.bulanda.shop;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Baza dla testów integracyjnych: pełny kontekst Springa + MockMvc + PostgreSQL z Testcontainers.
 * <p>
 * Testy celowo NIE są {@code @Transactional}: Envers zapisuje historię dopiero przy commicie,
 * a rollback testu zostawiłby ją pustą. Zamiast tego baza jest czyszczona przed każdym testem.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
abstract class AbstractIntegrationTest {

    protected static final String PRODUCTS = "/api/v1/products";
    protected static final String FORBIDDEN_PHRASES = "/api/v1/forbidden-phrases";

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanDatabase() {
        jdbcTemplate.execute(
                "TRUNCATE product, product_aud, audit_revision, forbidden_phrase RESTART IDENTITY CASCADE");
    }

    protected static String productJson(String name, String price, String category, long availableCount) {
        return """
                {"name": "%s", "price": %s, "category": "%s", "availableCount": %d}
                """.formatted(name, price, category, availableCount);
    }

    protected long createProduct(String name, String price, String category, long availableCount) throws Exception {
        String response = mockMvc.perform(post(PRODUCTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson(name, price, category, availableCount)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return ((Number) JsonPath.read(response, "$.id")).longValue();
    }

    protected long addForbiddenPhrase(String phrase) throws Exception {
        String response = mockMvc.perform(post(FORBIDDEN_PHRASES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"phrase": "%s"}
                                """.formatted(phrase)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return ((Number) JsonPath.read(response, "$.id")).longValue();
    }
}
