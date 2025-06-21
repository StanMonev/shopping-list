package com.monev.shoppinglist.repository;

import com.monev.shoppinglist.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {}
