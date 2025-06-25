package com.monev.shoppinglist.controller;

import com.monev.shoppinglist.config.SecurityConfig;
import com.monev.shoppinglist.model.Product;
import com.monev.shoppinglist.service.ShoppingListService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests for {@link ViewController}.
 * Left un-commented for discussion purposes.
 */
@WebMvcTest(ViewController.class)
@Import(SecurityConfig.class)
@TestPropertySource("classpath:application-local.properties")
@ExtendWith(SpringExtension.class)
class ViewControllerTest {

    @Autowired MockMvc mvc;
    @MockitoBean ShoppingListService service;

    @Value("${spring.security.user.name}")     String username;
    @Value("${spring.security.user.password}") String password;

    /* ---------- Public pages ---------- */

    @Test
    void login_page_is_public() throws Exception {
        mvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void unauthenticated_root_returns_401() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isUnauthorized());
    }

    /* ---------- List page ---------- */

    @Test
    void index_lists_products_and_shows_blank_form() throws Exception {
        given(service.getProducts()).willReturn(
                List.of(new Product(1L, "Milk", 1)));

        mvc.perform(get("/")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic(username, password)))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("products", hasSize(1)))
                .andExpect(model().attribute("form", notNullValue()));
    }

    /* ---------- Edit page ---------- */

    @Test
    void edit_prefills_form_when_id_exists() throws Exception {
        Product p = new Product(2L, "Bread", 2);
        given(service.getProduct(2L)).willReturn(Optional.of(p));
        given(service.getProducts()).willReturn(List.of(p));

        mvc.perform(get("/products/2")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic(username, password)))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute(
                        "form",
                        samePropertyValuesAs(new ViewController.Form(2L, "Bread", 2))));
    }

    @Test
    void edit_returns_404_when_id_missing() throws Exception {
        given(service.getProduct(99L)).willReturn(Optional.empty());

        mvc.perform(get("/products/99")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic(username, password)))
                .andExpect(status().isNotFound());
    }

    /* ---------- Save (create / update) ---------- */

    @Test
    void save_creates_new_product_and_redirects() throws Exception {
        mvc.perform(post("/products")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic(username, password))
                        .param("name", "Eggs")
                        .param("quantity", "12")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        then(service).should().createProduct("Eggs", 12);
    }

    @Test
    void save_updates_existing_product_and_redirects() throws Exception {
        mvc.perform(post("/products")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic(username, password))
                        .param("id", "7")
                        .param("name", "Cheese")
                        .param("quantity", "3")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        then(service).should().updateProduct(7L, "Cheese", 3);
    }

    /* ---------- Delete ---------- */

    @Test
    void delete_existing_product_and_redirects() throws Exception {
        mvc.perform(post("/products/5")
                        .param("_method", "delete")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic(username, password))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        then(service).should().deleteProduct(5L);
    }

    @Test
    void delete_returns_404_when_id_missing() throws Exception {
        willThrow(new EntityNotFoundException())
                .given(service).deleteProduct(42L);

        mvc.perform(post("/products/42")
                        .param("_method", "delete")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic(username, password))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isNotFound());
    }
}
