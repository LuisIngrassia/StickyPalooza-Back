package com.g12.tpo.server.service.implementations;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.g12.tpo.server.entity.Product;
import com.g12.tpo.server.exceptions.ProductNotFoundException;
import com.g12.tpo.server.repository.ProductRepository;
import com.g12.tpo.server.service.interfaces.ImagenesProdService;
import com.g12.tpo.server.service.interfaces.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImagenesProdService fileUploadService;

    @Override
    public Product createProduct(Product product, MultipartFile imageFile) {
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String imagePath = fileUploadService.uploadImage(imageFile);
                product.setImage(imagePath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload image: " + e.getMessage());
            }
        }
        return productRepository.save(product);
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado con ID: " + id));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    @Transactional
    public Product updateProduct(Long id, Product productDetails, MultipartFile imageFile) {
        Product product = getProductById(id);

        Optional.ofNullable(productDetails.getName()).ifPresent(product::setName);
        Optional.ofNullable(productDetails.getDescription()).ifPresent(product::setDescription);
        Optional.ofNullable(productDetails.getPrice()).ifPresent(product::setPrice);
        Optional.ofNullable(productDetails.getCategory()).ifPresent(product::setCategory);
        Optional.ofNullable(productDetails.getStockQuantity()).ifPresent(product::setStockQuantity);

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String imagePath = fileUploadService.uploadImage(imageFile);
                product.setImage(imagePath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload image: " + e.getMessage());
            }
        }

        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Producto no encontrado con ID: " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    public Product updateProductImage(Long productId, String image) {
        Product product = getProductById(productId);
        product.setImage(image);
        return productRepository.save(product);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }
}
