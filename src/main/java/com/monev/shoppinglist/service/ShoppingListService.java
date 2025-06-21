package com.monev.shoppinglist.service;

import com.monev.shoppinglist.model.Product;

import java.util.List;
import java.util.Optional;

/**
 * Interface with the logic for shopping-list operations.
 */
public interface ShoppingListService {

    /**
     * Fetch every product currently on the shopping list.
     */
    List<Product> getProducts();

    /**
     * Fetch a single product by its database ID.
     *
     * @param id the primary-key value
     * @return an {@code Optional<Product>} that is empty when no row exists
     */
    Optional<Product> getProduct(long id);

    /**
     * Create and save a new product to the DB.
     *
     * @param name      e.g. “Milk”
     * @param quantity  e.g. 2
     * @return the newly created {@link Product} with its generated ID
     */
    Product createProduct(String name, int quantity);

    /**
     * Update the name and/or quantity of an existing product.
     *
     * @param id        primary-key value of the product to update
     * @param name      new name
     * @param quantity  new quantity
     * @return the updated {@link Product}
     * @throws jakarta.persistence.EntityNotFoundException if no such ID exists
     */
    Product updateProduct(long id, String name, int quantity);

    /**
     * Remove a product from the list and the DB.
     *
     * @param id primary-key value of the product to delete
     */
    void deleteProduct(long id);
}
