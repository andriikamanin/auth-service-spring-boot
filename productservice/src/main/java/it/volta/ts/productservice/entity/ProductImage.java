// ProductImage.java
package it.volta.ts.productservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    private int sortOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;
}