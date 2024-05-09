package org.khalid.labocontrol;

import org.khalid.labocontrol.entities.Category;
import org.khalid.labocontrol.entities.Product;
import org.khalid.labocontrol.repository.CategoryRepository;
import org.khalid.labocontrol.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class LabocontrolApplication {

    public static void main(String[] args) {
        SpringApplication.run(LabocontrolApplication.class, args);
    }
 // @Bean
    public CommandLineRunner commandLineRunner(ProductRepository productRepository, CategoryRepository categoryRepository) {
        return args -> {
            // Create and save categories
            Category category1 = new Category(null, "Electronics", "Electronic products");
            Category category2 = new Category(null, "Clothing", "Clothing items");

            categoryRepository.saveAll(Arrays.asList(category1, category2));

            // Create and save products
            Product product1 = new Product(null, "T-Shirt", "Casual t-shirt", 25.0, 450, category2,null,null);
            Product product2 = new Product(null, "ma7laba", "Casual t-shirt and this is a example of a discription", 25.0, 450, category2,null,null);
            Product product3 = new Product(null, "laptop", "Casual t-shirt", 25.0, 450, category2,null,null);

            productRepository.saveAll(Arrays.asList(product1, product2, product3));

            System.out.println("Data initialized successfully!");
        };
    }


}
