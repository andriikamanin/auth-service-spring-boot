package it.volta.ts.productservice.service.impl;

import it.volta.ts.productservice.dto.*;
import it.volta.ts.productservice.entity.*;
import it.volta.ts.productservice.repository.*;
import it.volta.ts.productservice.service.ProductService;
import it.volta.ts.productservice.service.S3Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final S3Service s3Service;

    @Override
    public ProductDto create(CreateProductRequest request, MultipartFile image) {
        String imageUrl = request.imageUrl();
        if (image != null && !image.isEmpty()) {
            try {
                imageUrl = s3Service.uploadProductImage(image);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload image", e);
            }
        }

        Product product = Product.builder()
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .available(request.available())
                .imageUrl(imageUrl)
                .categories(resolveCategories(request.categories()))
                .build();

        return toDto(productRepository.save(product));
    }

    @Override
    public ProductDto update(Long id, UpdateProductRequest request, MultipartFile image) {
        Product product = productRepository.findById(id).orElseThrow();

        if (request.name() != null) product.setName(request.name());
        if (request.description() != null) product.setDescription(request.description());
        if (request.price() != null) product.setPrice(request.price());
        if (request.available() != null) product.setAvailable(request.available());
        if (request.categories() != null) product.setCategories(resolveCategories(request.categories()));

        if (image != null && !image.isEmpty()) {
            try {
                product.setImageUrl(s3Service.uploadProductImage(image));
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload image", e);
            }
        } else if (request.imageUrl() != null) {
            product.setImageUrl(request.imageUrl());
        }

        return toDto(productRepository.save(product));
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public ProductDto findById(Long id) {
        return productRepository.findById(id).map(this::toDto).orElseThrow();
    }

    @Override
    public List<ProductDto> findAll(int page, int size, String filter) {
        return productRepository.findAll().stream()
                .map(this::toDto).collect(Collectors.toList()); // TODO: paginate & filter
    }

    private Set<Category> resolveCategories(Set<String> names) {
        if (names == null) return Set.of();
        return names.stream()
                .map(name -> categoryRepository.findByName(name).orElseGet(() -> {
                    Category cat = Category.builder().name(name).build();
                    return categoryRepository.save(cat);
                }))
                .collect(Collectors.toSet());
    }

    private ProductDto toDto(Product product) {
        Set<String> categories = product.getCategories().stream()
                .map(Category::getName).collect(Collectors.toSet());
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.isAvailable(),
                product.getImageUrl(),
                categories
        );
    }
}
