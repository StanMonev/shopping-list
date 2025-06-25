package com.monev.shoppinglist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.monev.shoppinglist.config.SecurityConfig;
import com.monev.shoppinglist.model.Product;
import com.monev.shoppinglist.service.ShoppingListService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * A simple test class showcasing a unit test for the main CRUD controller.
 */
@WebMvcTest(ProductController.class)
@TestPropertySource("classpath:application-local.properties")
@Import(SecurityConfig.class)
class ProductControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @MockitoBean ShoppingListService service;

    @Value("${spring.security.user.name}")
    String username;

    @Value("${spring.security.user.password}")
    String password;

    /**
     * Tests if the service logic of {@link ShoppingListService} getProducts
     * will deliver all the products as a json array.
     * @throws Exception a general exception if something goes wrong.
     */
    @Test
    void getAll_returns_json_array() throws Exception {
        given(service.getProducts()).willReturn(
                List.of(new Product(1L, "Milk", 1)));

        mvc.perform(get("/api/products").with(httpBasic(username, password)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Milk"));
    }

    /**
     * Checks if the post method saves the product in a DB and returns it with a status code 201.
     * @throws Exception a general exception if something goes wrong.
     */
    @Test
    void post_returns_201_and_entity() throws Exception {
        Product req = new Product(null, "Eggs", 12);
        Product res = new Product(3L, "Eggs", 12);

        given(service.createProduct(eq("Eggs"), eq(12))).willReturn(res);

        mvc.perform(post("/api/products").with(httpBasic(username, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3));
    }

    /**
     * Checks if the getById method in the {@link ShoppingListService} returns and optional empty object
     * for an element that does not exist in the DB.
     * @throws Exception a general exception if something goes wrong.
     */
    @Test
    void getById_returns_404_when_missing() throws Exception {
        given(service.getProduct(42L)).willReturn(Optional.empty());

        mvc.perform(get("/api/products/42").with(httpBasic(username, password)))
                .andExpect(status().isNotFound());
    }

    /**
     * Verifies if the delete function, deletes the given object from the DB and returns a 204 on success.
     * @throws Exception a general exception if something goes wrong.
     */
    @Test
    void delete_returns_204_on_success() throws Exception {
        mvc.perform(delete("/api/products/5").with(httpBasic(username, password)))
                .andExpect(status().isNoContent());

        verify(service).deleteProduct(5L);
    }

    /**
     * Checks on delete if the controller will return a 404 Not Found error if the product is not in the DB.
     * @throws Exception a general exception if something goes wrong.
     */
    @Test
    void delete_returns_404_when_missing() throws Exception {
        doThrow(new EntityNotFoundException()).when(service).deleteProduct(77L);

        mvc.perform(delete("/api/products/77").with(httpBasic(username, password)))
                .andExpect(status().isNotFound());
    }

}
