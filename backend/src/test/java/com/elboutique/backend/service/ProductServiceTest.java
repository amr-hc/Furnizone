package com.elboutique.backend.service;

import com.elboutique.backend.DTO.response.ProductResponse;
import com.elboutique.backend.model.Product;
import com.elboutique.backend.repository.ProductRepository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @MockitoBean
    private ProductRepository productRepository;

   @Test
    public void testGetProductById() {

        int productId = 5555;
        Product fakeProduct = Product.builder()
            .id(productId)
            .title("Mocked Product")
            .description("From Mock Repository")
            .price(BigDecimal.valueOf(999.99))
            .stock(999)
            .image("mock-image.jpg")
            .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(fakeProduct));

        ProductResponse result = productService.getProductById(productId);

        assertNotNull(result);
        assertEquals(result.getTitle(), fakeProduct.getTitle());
    }
}
