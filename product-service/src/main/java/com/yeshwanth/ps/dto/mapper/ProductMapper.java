package com.yeshwanth.ps.dto.mapper;

import com.yeshwanth.ps.dto.ProductRequest;
import com.yeshwanth.ps.dto.ProductResponse;
import com.yeshwanth.ps.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product dtoToEntity(ProductRequest product)
    {
        return Product.builder()
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .category(product.getCategory())
                .build();

    }

    public ProductResponse entityToDto(Product product)
    {
        return ProductResponse.builder()
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .category(product.getCategory())
                .imageUrl(product.getImageUrl())
                .id(product.getId())
                .build();

    }

}
