package com.monev.shoppinglist.controller;

import com.monev.shoppinglist.service.ShoppingListService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Controller
@Validated
public class ViewController {

    private final ShoppingListService shoppingListService;

    public ViewController(ShoppingListService shoppingListService) { this.shoppingListService = shoppingListService; }

    /** List + add-form */
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("products", shoppingListService.getProducts());
        model.addAttribute("form", new Form());
        return "index";
    }

    /** Edit form pre-fill */
    @GetMapping("/products/{id}")
    public String edit(@PathVariable long id, Model model) {
        var product = shoppingListService.getProduct(id).orElseThrow();
        model.addAttribute("products", shoppingListService.getProducts());
        model.addAttribute("form", new Form(product.getId(), product.getName(), product.getQuantity()));
        return "index";
    }

    /** Create or update */
    @PostMapping("/products")
    public String save(@ModelAttribute @Validated Form form) {
        if (form.id() == null) shoppingListService.createProduct(form.name(), form.quantity());
        else                shoppingListService.updateProduct(form.id(), form.name(), form.quantity());
        return "redirect:/";
    }

    /** Delete */
    @PostMapping("/products/{id}")
    public String delete(@PathVariable long id, @RequestParam("_method") String m) {
        if ("delete".equalsIgnoreCase(m)) shoppingListService.deleteProduct(id);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // maps to templates/login.html
    }

    /** simple DTO used by the form */
    public record Form(Long id, String name, int quantity) {
        public Form() {
            this(null, "", 1);
        }
    }

    @ExceptionHandler({EntityNotFoundException.class, NoSuchElementException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    void handleNotFound() {}
}
