package com.elboutique.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.elboutique.backend.DTO.request.ProductRequest;
import com.elboutique.backend.DTO.response.ProductResponse;
import com.elboutique.backend.model.Product;
import com.elboutique.backend.repository.ProductRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final FileStorageService fileStorageService;

    @Value("${app.base-url}")
    private String baseUrl;

    private Product findProductById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
    }

    public Product createProduct(ProductRequest request, MultipartFile image) {

        String imagePath = fileStorageService.saveFile(image, "product");
        // Set the image path in the product
        Product product = Product.builder()
            .title(request.getTitle())
            .description(request.getDescription())
            .price(request.getPrice())
            .stock(request.getStock())
            .image(imagePath)
            .build();

        return productRepository.save(product);
    }

    public ProductResponse getProductById(Integer id) {
        Product product = findProductById(id);
        return ProductResponse.fromProduct(product, baseUrl);
    }

    public Page<ProductResponse> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, Math.min(size, 100));
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(product -> ProductResponse.fromProduct(product, baseUrl));
    }

    public Product updateProduct(Integer id, Product productDetails) {
        Product product = findProductById(id);

        product.setTitle(productDetails.getTitle());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setImage(productDetails.getImage());
        product.setStock(productDetails.getStock());

        return productRepository.save(product);
    }

    public void deleteProduct(Integer id) {
        Product product = findProductById(id);
        productRepository.delete(product);
    }

    public List<Product> searchProductsByTitle(String title) {
        return productRepository.findByTitleContainingIgnoreCase(title);
    }

}
