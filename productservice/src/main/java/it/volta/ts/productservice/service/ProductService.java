package it.volta.ts.productservice.service;

import it.volta.ts.productservice.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    ProductDto create(CreateProductRequest request, MultipartFile image);
    ProductDto update(Long id, UpdateProductRequest request, MultipartFile image);
    void delete(Long id);
    ProductDto findById(Long id);
    List<ProductDto> findAll(int page, int size, String filter);
}
