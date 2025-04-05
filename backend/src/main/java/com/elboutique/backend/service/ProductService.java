package com.elboutique.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.elboutique.backend.DTO.request.ProductRequest;
import com.elboutique.backend.DTO.response.ProductResponse;
import com.elboutique.backend.mapper.ProductMapper;
import com.elboutique.backend.model.Product;
import com.elboutique.backend.repository.ProductRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final FileStorageService fileStorageService;
    private final ProductMapper productMapper;

    @Value("${app.base-url}")
    private String baseUrl;

    private Product findProductById(Integer id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
    }

    public ProductResponse createProduct(ProductRequest request) {
        String imagePath = fileStorageService.saveFile(request.getImage(), "product");
        Product product = productMapper.toEntity(request);
        product.setImage(imagePath);
        return productMapper.toDto(productRepository.save(product));
    }

    public ProductResponse getProductById(Integer id) {
        Product product = findProductById(id);
        return productMapper.toDto(product);
    }

    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(product -> productMapper.toDto(product));
    }

    public ProductResponse updateProduct(Integer id, ProductRequest productDetails) {
        Product product = findProductById(id);
        productMapper.updateProduct(productDetails, product);
        if (productDetails.getImage() != null && !productDetails.getImage().isEmpty()) {
            product.setImage(fileStorageService.saveFile(productDetails.getImage(), "product"));
        }
        return productMapper.toDto(productRepository.save(product));
    }

    public void deleteProduct(Integer id) {
        Product product = findProductById(id);
        productRepository.delete(product);
    }

    public List<ProductResponse> searchProductsByTitle(String title) {
        return productMapper.toDtoList(productRepository.findByTitleContainingIgnoreCase(title));
    }

}
