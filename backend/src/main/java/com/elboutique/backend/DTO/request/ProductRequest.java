package com.elboutique.backend.DTO.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

import org.springframework.web.multipart.MultipartFile;

import com.elboutique.backend.validation.ValidImage;

@Data
public class ProductRequest {

    @NotBlank(message = "Title is required.")
    @Size(max = 255, message = "Title must not exceed 255 characters.")
    private String title;

    @Size(max = 500, message = "Description must not exceed 500 characters.")
    private String description;

    @NotNull(message = "Price is required.")
    @DecimalMin(value = "0.01", message = "Price must be at least 0.01.")
    private BigDecimal price;

    @NotNull(message = "Stock is required.")
    @Min(value = 0, message = "Stock must be at least 0.")
    private Integer stock;

    @ValidImage(message = "Please upload a valid image file.")
    private MultipartFile image;
}
