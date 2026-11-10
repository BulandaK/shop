package com.bulanda.shop.service;

import com.bulanda.shop.dto.product.CreateProductCommand;
import com.bulanda.shop.dto.product.ProductDTO;
import com.bulanda.shop.dto.product.ProductHistoryDTO;
import com.bulanda.shop.dto.product.UpdateProductCommand;
import com.bulanda.shop.exception.product.InvalidProductPriceException;
import com.bulanda.shop.exception.product.ProductAlreadyExistsException;
import com.bulanda.shop.exception.product.ProductNotFoundException;
import com.bulanda.shop.mapper.ProductMapper;
import com.bulanda.shop.model.Category;
import com.bulanda.shop.model.Product;
import com.bulanda.shop.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.history.Revision;
import org.springframework.data.history.RevisionMetadata;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ForbiddenPhraseService forbiddenPhraseService;

    public List<ProductDTO> getAll() {
        return productMapper.toDtoList(productRepository.findAll());
    }

    public ProductDTO get(Long id) {
        return productMapper.toDto(findOrThrow(id));
    }

    @Transactional
    public ProductDTO add(CreateProductCommand command) {
        validatePrice(command.category(), command.price());
        forbiddenPhraseService.assertNameAllowed(command.name());
        if (productRepository.existsByName(command.name())) {
            throw new ProductAlreadyExistsException("produkt o takim name już istnieje");
        }
        Product product = new Product(null, command.name(), command.price(), command.category(), command.availableCount());
        Product saved = productRepository.save(product);
        return productMapper.toDto(saved);
    }

    @Transactional
    public ProductDTO update(Long id, UpdateProductCommand command) {
        Product product = findOrThrow(id);
        validatePrice(command.category(), command.price());
        forbiddenPhraseService.assertNameAllowed(command.name());
        if (productRepository.existsByNameAndIdNot(command.name(), id)) {
            throw new ProductAlreadyExistsException("produkt o takim name już istnieje");
        }
        product.setName(command.name());
        product.setPrice(command.price());
        product.setCategory(command.category());
        product.setAvailableCount(command.availableCount());
        return productMapper.toDto(productRepository.save(product));
    }

    @Transactional
    public void delete(Long id) {
        productRepository.delete(findOrThrow(id));
    }

    public List<ProductHistoryDTO> getHistory(Long id) {
        List<Revision<Long, Product>> revisions = productRepository.findRevisions(id).getContent();
        if (revisions.isEmpty()) {
            throw new ProductNotFoundException("Produkt o takim id nie istnieje");
        }
        List<ProductHistoryDTO> history = new ArrayList<>(revisions.size());
        Product previous = null;
        for (Revision<Long, Product> revision : revisions) {
            Product current = revision.getEntity();
            RevisionMetadata.RevisionType type = revision.getMetadata().getRevisionType();
            history.add(new ProductHistoryDTO(
                    revision.getRequiredRevisionNumber(),
                    revision.getRequiredRevisionInstant(),
                    toChangeType(type),
                    current.getName(),
                    current.getPrice(),
                    current.getCategory() != null ? current.getCategory().name() : null,
                    current.getAvailableCount(),
                    type == RevisionMetadata.RevisionType.DELETE ? List.of() : changedFields(previous, current)
            ));
            previous = current;
        }
        return history;
    }

    private static String toChangeType(RevisionMetadata.RevisionType type) {
        return switch (type) {
            case INSERT -> "CREATED";
            case UPDATE -> "UPDATED";
            case DELETE -> "DELETED";
            case UNKNOWN -> "UNKNOWN";
        };
    }

    private static List<String> changedFields(Product previous, Product current) {
        List<String> changed = new ArrayList<>();
        if (previous == null || !Objects.equals(previous.getName(), current.getName())) {
            changed.add("name");
        }
        if (previous == null || !samePrice(previous.getPrice(), current.getPrice())) {
            changed.add("price");
        }
        if (previous == null || previous.getCategory() != current.getCategory()) {
            changed.add("category");
        }
        if (previous == null || !Objects.equals(previous.getAvailableCount(), current.getAvailableCount())) {
            changed.add("availableCount");
        }
        return changed;
    }

    private static boolean samePrice(BigDecimal a, BigDecimal b) {
        return a == null ? b == null : b != null && a.compareTo(b) == 0;
    }

    private Product findOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Produkt o takim id nie istnieje"));
    }

    private static void validatePrice(Category category, BigDecimal price) {
        if (!category.isPriceAllowed(price)) {
            throw new InvalidProductPriceException("cena sie nie zgadza z przedziałem dla kategorii");
        }
    }
}
