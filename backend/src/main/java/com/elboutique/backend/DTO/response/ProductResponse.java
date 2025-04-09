package com.elboutique.backend.DTO.response;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse implements Serializable {
    private Integer id;
    private String title;
    private String description;
    private BigDecimal price;
    private String image;
    private Integer stock;
}