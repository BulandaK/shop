package com.bulanda.shop.controller;

import com.bulanda.shop.dto.product.CreateProductCommand;
import com.bulanda.shop.dto.product.ProductDTO;
import com.bulanda.shop.dto.product.ProductHistoryDTO;
import com.bulanda.shop.dto.product.UpdateProductCommand;
import com.bulanda.shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductDTO> getAll(){
        return productService.getAll();
    }

    @GetMapping("/{id}")
    public ProductDTO get(@PathVariable Long id) {
        return productService.get(id);
    }

    @GetMapping("/{id}/history")
    public List<ProductHistoryDTO> getHistory(@PathVariable Long id) {
        return productService.getHistory(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO add(@RequestBody @Validated CreateProductCommand command) {
        return productService.add(command);
    }

    @PutMapping("/{id}")
    public ProductDTO update(@PathVariable Long id, @RequestBody @Validated UpdateProductCommand command) {
        return productService.update(id, command);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }
}
