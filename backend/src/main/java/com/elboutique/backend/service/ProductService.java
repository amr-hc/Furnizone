package com.elboutique.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.elboutique.backend.DTO.request.ProductRequest;
import com.elboutique.backend.DTO.response.ProductResponse;
import com.elboutique.backend.mapper.ProductMapper;
import com.elboutique.backend.model.Product;
import com.elboutique.backend.repository.ProductRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;


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

    @CacheEvict(value = "products", allEntries = true)
    public ProductResponse createProduct(ProductRequest request) {
        String imagePath = fileStorageService.saveFile(request.getImage(), "product");
        Product product = productMapper.toEntity(request);
        product.setImage(imagePath);
        return productMapper.toDto(productRepository.save(product));
    }

    @Cacheable(value = "productCache", key = "#id")
    public ProductResponse getProductById(Integer id) {
        Product product = findProductById(id);
        return productMapper.toDto(product);
    }

    @Cacheable(value = "products", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(product -> productMapper.toDto(product));
    }

    @Caching(
        evict = {
            @CacheEvict(value = "productCache", key = "#id"),
            @CacheEvict(value = "products", allEntries = true)
        }
    )
    public ProductResponse updateProduct(Integer id, ProductRequest productDetails) {
        Product product = findProductById(id);
        productMapper.updateProduct(productDetails, product);
        if (productDetails.getImage() != null && !productDetails.getImage().isEmpty()) {
            product.setImage(fileStorageService.saveFile(productDetails.getImage(), "product"));
        }
        return productMapper.toDto(productRepository.save(product));
    }

    @Caching(
        evict = {
            @CacheEvict(value = "productCache", key = "#id"),
            @CacheEvict(value = "products", allEntries = true)
        }
    )
    public void deleteProduct(Integer id) {
        Product product = findProductById(id);
        productRepository.delete(product);
    }

    @Cacheable(value = "products", key = "'Title-' + #title")
    public List<ProductResponse> searchProductsByTitle(String title) {
        return productMapper.toDtoList(productRepository.findByTitleContainingIgnoreCase(title));
    }

}
