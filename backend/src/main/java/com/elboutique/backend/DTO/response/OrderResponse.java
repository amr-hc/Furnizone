package com.elboutique.backend.DTO.response;

import com.elboutique.backend.model.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    public static OrderResponse fromEntity(Order order) {
        return OrderResponse.builder()
            .id(order.getId())
            .status(order.getStatus().toString())
            .createdAt(order.getCreatedAt())
            .updatedAt(order.getUpdatedAt())
            .products(order.getOrderProducts().stream()
                .map(orderProduct -> ProductDetails.builder()
                    .id(orderProduct.getProduct().getId())
                    .title(orderProduct.getProduct().getTitle())
                    .price(orderProduct.getPrice())
                    .quantity(orderProduct.getQuantity())
                    .build())
                .collect(Collectors.toList()))
            .build();
    }

    private Integer id;
    private String status;
    private Date createdAt;
    private Date updatedAt;
    private List<ProductDetails> products;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductDetails {
        private Integer id;
        private String title;
        private BigDecimal price;
        private Integer quantity;
    }


}
