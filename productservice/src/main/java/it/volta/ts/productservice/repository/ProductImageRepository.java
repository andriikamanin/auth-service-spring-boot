package it.volta.ts.productservice.repository;

import it.volta.ts.productservice.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
}