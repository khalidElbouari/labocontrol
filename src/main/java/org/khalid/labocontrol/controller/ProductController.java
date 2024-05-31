package org.khalid.labocontrol.controller;

import org.khalid.labocontrol.entities.Category;
import org.khalid.labocontrol.entities.Product;
import org.khalid.labocontrol.service.CartItemService;
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
    private final CartItemService cartItemService;


    @Autowired
    public ProductController(ProductService productService,CategoryService categoryService,
                             CartItemService cartItemService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.cartItemService = cartItemService;
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
        // Validate the product
        ResponseEntity<?> validationResponse = validateProduct(name, description, category, price, stockQuantity, image);
        if (validationResponse != null) {
            return validationResponse; // Return validation error if any
        }
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

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id,
                                                 @RequestParam("image") MultipartFile image,
                                                 @RequestParam("name") String name,
                                                 @RequestParam("description") String description,
                                                 @RequestParam("category") String category,
                                                 @RequestParam("stockQuantity") int stockQuantity,
                                                 @RequestParam("price") double price) {
        try {
            // Retrieve the existing product by ID
            Product existingProduct = productService.getProductById(id);
            if (existingProduct == null) {
                return ResponseEntity.notFound().build();
            }
            // Read image data as byte array
            byte[] imageData = image.getBytes();
            // Update the existing product with the new values
            existingProduct.setName(name);
            existingProduct.setDescription(description);
            existingProduct.setPrice(price  );
            existingProduct.setStockQuantity(stockQuantity);
            Category c=categoryService.getCategoryById(Long.parseLong(category));
            existingProduct.setCategory(c);
            existingProduct.setImageData(imageData);
            // Save the updated product to the database
            Product updatedProduct = productService.updateProduct(existingProduct);

            return ResponseEntity.ok().body(updatedProduct);
        } catch (Exception e) {
            // Handle any exceptions
            return ResponseEntity.internalServerError().build();
        }
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        // Check if the product is in any carts
        boolean isInCarts = cartItemService.isProductInCarts(id);

        if (isInCarts) {
            // Product is in carts, cannot delete
            return ResponseEntity.badRequest().build();
        } else {
            // Product is not in carts, proceed with deletion
            boolean deleted = productService.deleteProduct(id);
            if (deleted) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        }
    }


    private ResponseEntity<?> validateProduct(String name, String description, String category, double price, int stockQuantity, MultipartFile image) {
        // Validate the input parameters
        if (name == null || name.isEmpty()) {
            return new ResponseEntity<>("Name is required", HttpStatus.BAD_REQUEST);
        }
        if (description == null || description.isEmpty()) {
            return new ResponseEntity<>("Description is required", HttpStatus.BAD_REQUEST);
        }
        if (category == null || category.isEmpty()) {
            return new ResponseEntity<>("Category is required", HttpStatus.BAD_REQUEST);
        }
        if (price <= 0) {
            return new ResponseEntity<>("Price must be greater than 0", HttpStatus.BAD_REQUEST);
        }
        if (stockQuantity < 0) {
            return new ResponseEntity<>("Stock quantity must be non-negative", HttpStatus.BAD_REQUEST);
        }
        if (image == null || image.isEmpty()) {
            return new ResponseEntity<>("Image is required", HttpStatus.BAD_REQUEST);
        }
        return null; // No validation errors
    }
}
