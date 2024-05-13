package org.khalid.labocontrol.controller;

import org.khalid.labocontrol.entities.Category;
import org.khalid.labocontrol.entities.Product;
import org.khalid.labocontrol.service.CategoryService;
import org.khalid.labocontrol.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:4200/")
@RequestMapping("api/product")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;


    @Autowired
    public ProductController(ProductService productService,CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }
    @GetMapping("/all")
    public ResponseEntity<List<Product>> getProducts() {
        List<Product> products = productService.getAllProducts();

        if (products.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(products);
        }
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<?> addProduct(@RequestParam("image") MultipartFile image,
                                        @RequestParam("name") String name,
                                        @RequestParam("description") String description,
                                        @RequestParam("category") String category,
                                        @RequestParam("stockQuantity") int stockQuantity,
                                        @RequestParam("price") double price) {
        try {
            // Convert category to Category object if needed
            Long categoryId = Long.parseLong(category);
            Category categoryObject = categoryService.getCategoryById(categoryId);
            if (categoryObject == null) {
                return new ResponseEntity<>("Category with ID " + categoryId + " not found", HttpStatus.NOT_FOUND);
            }

            // Read image data as byte array
            byte[] imageData = image.getBytes();

            // Create a new Product object with the provided data
            Product product = new Product(null, name, description, price, stockQuantity, categoryObject, imageData, null);

            // Save the product to the database
            Product addedProduct = productService.addProduct(product);
            return new ResponseEntity<>(addedProduct, HttpStatus.CREATED);
        } catch (NumberFormatException e) {
            // Handle invalid category ID format
            return new ResponseEntity<>("Invalid category ID format", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Handle any other exceptions
            return new ResponseEntity<>("Failed to add product: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }





    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        boolean deleted = productService.deleteProduct(id);

        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(id, product);

        if (updatedProduct == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(updatedProduct);
        }
    }

}
