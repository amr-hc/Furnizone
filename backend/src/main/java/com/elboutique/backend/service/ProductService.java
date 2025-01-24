package com.elboutique.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.elboutique.backend.DTO.request.ProductRequest;
import com.elboutique.backend.model.Product;
import com.elboutique.backend.repository.ProductRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final FileStorageService fileStorageService;

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

    public Product getProductById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product updateProduct(Integer id, Product productDetails) {
        Product product = getProductById(id);

        product.setTitle(productDetails.getTitle());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setImage(productDetails.getImage());
        product.setStock(productDetails.getStock());

        return productRepository.save(product);
    }

    public void deleteProduct(Integer id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }

}
