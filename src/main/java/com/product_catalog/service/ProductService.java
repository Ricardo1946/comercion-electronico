package com.product_catalog.service;

import com.product_catalog.entity.Product;
import com.product_catalog.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(updatedProduct.getName());
                    product.setDescription(updatedProduct.getDescription());
                    product.setPrice(updatedProduct.getPrice());
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public void deleteDuplicateProducts() {
        List<Product> products = productRepository.findAll();
        products.stream()
                .collect(Collectors.groupingBy(Product::getName))
                .values().stream()
                .filter(group -> group.size() > 1)
                .forEach(duplicates -> duplicates.stream().skip(1).forEach(duplicate -> productRepository.deleteById(duplicate.getId())));
    }
}
