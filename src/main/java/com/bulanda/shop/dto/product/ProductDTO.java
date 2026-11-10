package com.bulanda.shop.dto.product;

import java.math.BigDecimal;

public record ProductDTO(Long id, String name, BigDecimal price, String category,Long availableCount) {
}
