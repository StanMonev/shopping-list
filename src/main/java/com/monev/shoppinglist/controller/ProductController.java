package com.monev.shoppinglist.controller;

import com.monev.shoppinglist.model.Product;
import com.monev.shoppinglist.service.ShoppingListService;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST endpoints for CRUD operations on Shopping List products.
 * <p>
 * Base URL: /api/products
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ShoppingListService service;

    public ProductController(ShoppingListService service) {
        this.service = service;
    }

    /* ---------- Read ---------- */

    @GetMapping
    public List<Product> getAll() {
        return service.getProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable long id) {
        Optional<Product> product = service.getProduct(id);
        return product.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /* ---------- Create ---------- */

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody @Validated Product body) {
        Product created = service.createProduct(body.getName(), body.getQuantity());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /* ---------- Update ---------- */

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable long id,
                                          @RequestBody @Validated Product body) {
        Product updated = service.updateProduct(id, body.getName(), body.getQuantity());
        return ResponseEntity.ok(updated);
    }

    /* ---------- Delete ---------- */

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        service.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
