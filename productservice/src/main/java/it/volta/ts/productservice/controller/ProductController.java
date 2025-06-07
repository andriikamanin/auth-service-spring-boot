package it.volta.ts.productservice.controller;

import it.volta.ts.productservice.dto.*;
import it.volta.ts.productservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductDto> getAll(@RequestParam int page, @RequestParam int size, @RequestParam(required = false) String filter) {
        return productService.findAll(page, size, filter);
    }

    @GetMapping("/{id}")
    public ProductDto getById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ProductDto create(
            @RequestPart("request") @Valid CreateProductRequest request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        return productService.create(request, images);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProductDto update(@PathVariable Long id,
                             @RequestPart("request") UpdateProductRequest request,
                             @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        return productService.update(id, request, images);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }
}