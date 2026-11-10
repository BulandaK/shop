package com.bulanda.shop.dto.product;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record ProductHistoryDTO(
        Long revision,
        Instant changedAt,
        String changeType,
        String name,
        BigDecimal price,
        String category,
        Long availableCount,
        List<String> changedFields
) {
}
