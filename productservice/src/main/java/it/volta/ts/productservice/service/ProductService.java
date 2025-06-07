package it.volta.ts.productservice.service;

import it.volta.ts.productservice.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    public ProductDto create(CreateProductRequest request, List<MultipartFile> images);
    public ProductDto update(Long id, UpdateProductRequest request, List<MultipartFile> images);
    void delete(Long id);
    ProductDto findById(Long id);
    List<ProductDto> findAll(int page, int size, String filter);
}
