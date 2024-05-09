package org.khalid.labocontrol.service;

import org.khalid.labocontrol.entities.Product;
import org.khalid.labocontrol.repository.ProductRepository;
import org.khalid.labocontrol.service.security.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private ProductRepository productRepository;
    @Autowired
    private PictureService pictureService;
    @Autowired
    public ProductService(ProductRepository productRepository){
        this.productRepository= productRepository;
    }

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }
    public boolean deleteProduct(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isPresent()) {
            productRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isPresent()) {
            updatedProduct.setId(id); // Ensure the ID is set correctly
            return productRepository.save(updatedProduct);
        } else {
            return null;
        }
    }
/*
    public Product addProduct(Product product, MultipartFile image) {
        // Handle the image file using the PictureService
        try {
            String imagePath = pictureService.saveProfilePicture(image);
            product.setImagePath(imagePath);
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception
        }

        // Save the product to the database
        return productRepository.save(product);
    }
*/

    public Product addProduct(Product product) {
        // Save the product to the database
        return productRepository.save(product);
    }

}
