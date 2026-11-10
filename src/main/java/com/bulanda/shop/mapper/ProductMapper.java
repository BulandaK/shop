package com.bulanda.shop.mapper;

import com.bulanda.shop.dto.product.ProductDTO;
import com.bulanda.shop.model.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDTO toDto(Product product);

    List<ProductDTO> toDtoList(List<Product> products);
}
