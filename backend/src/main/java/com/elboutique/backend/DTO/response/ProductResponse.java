package com.elboutique.backend.DTO.response;

import java.math.BigDecimal;

import com.elboutique.backend.model.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private Integer id;
    private String title;
    private String description;
    private BigDecimal price;
    private String image;

    public static ProductResponse fromProduct(Product product, String baseUrl) {
        return ProductResponse.builder()
            .id(product.getId())
            .title(product.getTitle())
            .description(product.getDescription())
            .price(product.getPrice())
            .image(product.getImage() != null ? baseUrl + product.getImage() : null)
            .build();
    }
}