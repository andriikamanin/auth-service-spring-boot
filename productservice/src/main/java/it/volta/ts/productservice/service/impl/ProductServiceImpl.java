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
    public ProductDto create(CreateProductRequest request, List<MultipartFile> images) {
        Product product = Product.builder()
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .available(request.available())
                .categories(resolveCategories(request.categories()))
                .build();

        if (images != null && !images.isEmpty()) {
            List<ProductImage> imageEntities = new ArrayList<>();
            for (int i = 0; i < images.size(); i++) {
                MultipartFile file = images.get(i);
                try {
                    String url = s3Service.uploadProductImage(file);
                    imageEntities.add(ProductImage.builder()
                            .url(url)
                            .sortOrder(i)
                            .product(product)
                            .build());
                } catch (IOException e) {
                    throw new RuntimeException("Image upload failed", e);
                }
            }
            product.setImages(imageEntities);
        }

        return toDto(productRepository.save(product));
    }

    @Override
    public ProductDto update(Long id, UpdateProductRequest request, List<MultipartFile> images) {
        Product product = productRepository.findById(id).orElseThrow();

        if (request.name() != null) product.setName(request.name());
        if (request.description() != null) product.setDescription(request.description());
        if (request.price() != null) product.setPrice(request.price());
        if (request.available() != null) product.setAvailable(request.available());
        if (request.categories() != null) product.setCategories(resolveCategories(request.categories()));

        if (images != null && !images.isEmpty()) {
            // Удаляем старые изображения
            product.getImages().clear();

            List<ProductImage> newImages = new ArrayList<>();
            for (int i = 0; i < images.size(); i++) {
                MultipartFile file = images.get(i);
                try {
                    String url = s3Service.uploadProductImage(file);
                    newImages.add(ProductImage.builder()
                            .url(url)
                            .sortOrder(i)
                            .product(product)
                            .build());
                } catch (IOException e) {
                    throw new RuntimeException("Failed to upload image", e);
                }
            }

            product.setImages(newImages);
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

        List<String> imageUrls = product.getImages().stream()
                .sorted(Comparator.comparingInt(ProductImage::getSortOrder))
                .map(ProductImage::getUrl)
                .toList();

        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.isAvailable(),
                imageUrls,
                categories
        );
    }
}
