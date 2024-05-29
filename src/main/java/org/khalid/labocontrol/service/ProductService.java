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


    public Product addProduct(Product product) {
        return productRepository.save(product);
    }


    public Product getProductById(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        return productOptional.orElse(null);
    }

    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }
}
