package com.elboutique.backend.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {
    private List<ProductOrder> products;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductOrder {
        private Integer product_id;
        private Integer quantity;
    }
}
// until now i dont use it but will use in future versions
