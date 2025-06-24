package com.monev.shoppinglist.service;

import com.monev.shoppinglist.model.Product;
import com.monev.shoppinglist.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Default business-logic implementation.
 */
@Service
@Transactional
public class ShoppingListServiceImpl implements ShoppingListService {

    private final ProductRepository repo;

    public ShoppingListServiceImpl(ProductRepository repo) {
        this.repo = repo;
    }

    /* ---------- Read ---------- */

    @Override
    @Transactional(readOnly = true)
    public List<Product> getProducts() {
        return repo.findAll(Sort.by("id"));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> getProduct(long id) {
        return repo.findById(id);
    }

    /* ---------- Create ---------- */

    @Override
    public Product createProduct(String name, int quantity) {
        Product p = new Product(null, name, quantity);
        return repo.save(p);
    }

    /* ---------- Update ---------- */

    @Override
    public Product updateProduct(long id, String name, int quantity) {
        Product p = repo.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Product " + id + " not found"));

        p.setName(name);
        p.setQuantity(quantity);
        return repo.save(p);
    }

    /* ---------- Delete ---------- */

    @Override
    public void deleteProduct(long id) {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Product " + id + " not found");
        }
        repo.deleteById(id);
    }
}
