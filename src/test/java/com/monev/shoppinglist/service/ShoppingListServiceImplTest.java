package com.monev.shoppinglist.service;

import com.monev.shoppinglist.model.Product;
import com.monev.shoppinglist.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


/**
 * A simple test class showcasing tests for {@link ShoppingListServiceImpl}
 */
@ExtendWith(MockitoExtension.class)
public class ShoppingListServiceImplTest {
    @Mock  ProductRepository repo;
    @InjectMocks  ShoppingListServiceImpl service;

    /**
     * Verifies, that the function {@link ShoppingListService#createProduct(String, int)} saves an entity in the DB
     * and returns it with a new id.
     */
    @Test
    void createProduct_saves_and_returns_entity_with_id() {
        when(repo.save(any())).thenAnswer(inv -> {
            Product p = inv.getArgument(0, Product.class);
            p.setId(1L);
            return p;
        });

        Product result = service.createProduct("Milk", 2);

        ArgumentCaptor<Product> cap = ArgumentCaptor.forClass(Product.class);
        verify(repo).save(cap.capture());
        assertThat(cap.getValue().getName()).isEqualTo("Milk");
        assertThat(result.getId()).isEqualTo(1L);
    }

    /**
     * Verifies, that the function {@link ShoppingListService#updateProduct(long, String, int)} updates
     * an already existing entity in the DB if the given parameter ID exists.
     */
    @Test
    void updateProduct_changes_fields_when_id_exists() {
        Product existing = new Product(5L, "Bread", 1);
        when(repo.findById(5L)).thenReturn(Optional.of(existing));
        when(repo.save(existing)).thenReturn(existing);

        Product out = service.updateProduct(5L, "Baguette", 3);

        assertThat(out.getName()).isEqualTo("Baguette");
        assertThat(out.getQuantity()).isEqualTo(3);
    }

    /**
     * Checks if the function {@link ShoppingListService#updateProduct(long, String, int)} 
     * throws an EntityNotFoundException if the given ID parameter does not exist in the DB.
     */
    @Test
    void updateProduct_throws_when_id_missing() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.updateProduct(99L, "x", 1))
                .isInstanceOf(EntityNotFoundException.class);
    }

    /**
     * Verifies, that the function {@link ShoppingListService#getProducts()} returns a list of all entities
     * in the DB for the table Product.
     */
    @Test
    void getProducts_returns_sorted_list() {
        when(repo.findAll(any(Sort.class)))
                .thenReturn(List.of(
                        new Product(2L, "B", 1),
                        new Product(1L, "A", 1)));

        List<Product> list = service.getProducts();

        assertThat(list).extracting(Product::getId)
                .containsExactly(2L, 1L);
    }

    /**
     * Verifies, that {@link ShoppingListService#deleteProduct(long)}
     * successfully removes an entity from the DB when the given ID exists.
     */
    @Test
    void deleteProduct_removes_when_id_exists() {
        when(repo.existsById(9L)).thenReturn(true);

        service.deleteProduct(9L);

        verify(repo).deleteById(9L);
    }

    /**
     * Checks, that the function {@link ShoppingListService#deleteProduct(long)}
     * throws an {@link jakarta.persistence.EntityNotFoundException} if the given ID cannot be found in the DB.
     */
    @Test
    void deleteProduct_throws_when_id_missing() {
        when(repo.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.deleteProduct(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    /**
     * Verifies, that function {@link ShoppingListService#getProduct(long)}
     * returns {@code Optional.empty()} when no entity with the specified ID exists in the DB.
     */
    @Test
    void getProduct_returns_empty_optional_when_missing() {
        when(repo.findById(123L)).thenReturn(Optional.empty());

        Optional<Product> result = service.getProduct(123L);

        assertThat(result).isEmpty();
    }
}
