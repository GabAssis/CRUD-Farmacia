package com.gabriel.drugstore.controller;

import com.gabriel.drugstore.model.Category;
import com.gabriel.drugstore.model.Product;
import com.gabriel.drugstore.repository.CategoryRepository;
import com.gabriel.drugstore.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;


    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(productRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(response -> ResponseEntity.ok(response))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<Product>> getByName(@PathVariable String name) {
        return ResponseEntity.ok(productRepository.findAllByNameContainingIgnoreCase(name));
    }

    @GetMapping("/expiration/{expiration}")
    public ResponseEntity<List<Product>> getByExpiration(@PathVariable LocalDate expiration){
        return ResponseEntity.ok(productRepository.findByExpirationBefore(expiration));
    }


    @PutMapping
    public ResponseEntity<Product> put(@Valid @RequestBody Product product) {
        if (productRepository.existsById(product.getId())) {

            if (categoryRepository.existsById(product.getCategory().getId()))
                return ResponseEntity.status(HttpStatus.OK)
                        .body(productRepository.save(product));

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category doesn't exist!", null);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping
    public ResponseEntity<Product> post(@Valid @RequestBody Product product) {
        if (categoryRepository.existsById(product.getCategory().getId()))
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(productRepository.save(product));

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category doesn't exist!", null);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        productRepository.deleteById(id);

    }
}
