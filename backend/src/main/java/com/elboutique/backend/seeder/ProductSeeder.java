package com.elboutique.backend.seeder;

import com.elboutique.backend.model.Product;
import com.elboutique.backend.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.stream.IntStream;

import com.github.javafaker.Faker;

import lombok.RequiredArgsConstructor;

@Component
@Profile("dev")
@RequiredArgsConstructor 
public class ProductSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) {
        if (productRepository.count() == 0) {
            Faker faker = new Faker(Locale.ENGLISH);
            IntStream.rangeClosed(1, 40).forEach(i -> {
                BigDecimal price = BigDecimal.valueOf(faker.number().randomDouble(2, 10, 1000));
 
                Product product = Product.builder()
                    .title(faker.commerce().productName())
                    .description(faker.lorem().sentence())
                    .price(price)
                    .stock(faker.number().numberBetween(1, 100))
                    .image("uploads/images/product/default.png")
                    .build();
                    
                productRepository.save(product);
            });
            System.out.println("Seeded 40 fake products successfully");
        }
    }
}
