package rikkei.academy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rikkei.academy.model.dto.PageDTO;
import rikkei.academy.model.dto.response.ProductResponse;
import rikkei.academy.model.dto.response.ResponseDtoSuccess;
import rikkei.academy.model.entity.Product;
import rikkei.academy.service.category.ICategoryService;
import rikkei.academy.service.product.IProductService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api.myservice.com/v1/products")
public class ProductController {
    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

    // API: Chức năng Tìm kiếm Sản phẩm theo tên.
    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam(name = "search") String search) {
        List<Product> productList = productService.findByProductNameContaining(search);
        if (productList.isEmpty()) {
            return new ResponseEntity<>("Không tìm thấy Sản phẩm có tên: " + search, HttpStatus.NOT_FOUND);
        }
        return getResponseEntity(productList);
    }

    // API: Danh sách sản phẩm được bán(có phân trang và sắp xếp)
    @GetMapping
    public ResponseEntity<?> getAllProduct(Pageable pageable) {
        PageDTO<ProductResponse> productResponsePageDTO = productService.getAllProductRolePermitAll(pageable);
        return new ResponseEntity<>(new ResponseDtoSuccess<>(productResponsePageDTO, HttpStatus.OK), HttpStatus.OK);
    }

    // Chuyển đổi đối tượng Product sang DTO
    private ResponseEntity<?> getResponseEntity(List<Product> productList) {
        return getResponseEntity(productList);
    }

    // API: Danh sách Sản phẩm mới: Lấy ra 10 Sản phẩm được thêm gần đây nhất
    @GetMapping("/new-products")
    public ResponseEntity<?> getNewProducts() {
        List<Product> productList = productService.findFirst10ByOrderByCreatedAtDesc();
        return getResponseEntity(productList);
    }

    // API: Danh sách Sản phẩm theo Danh Mục
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<?> getProductsByCategory(@PathVariable Long categoryId) {
        List<Product> productList = productService.findByCategory(categoryService.findById(categoryId));
        return getResponseEntity(productList);
    }

    // API: Chi tiết Sản phẩm theo ID
    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable Long productId) {
        Product product = productService.findById(productId);
        if (product == null) {
            return new ResponseEntity<>("Không tìm thấy Sản phẩm có ID: " + productId, HttpStatus.NOT_FOUND);
        }
        ProductResponse productResponse;
        productResponse = ProductResponse.builder()
                .sku(product.getSku())
                .productName(product.getProductName())
                .description(product.getDescription())
                .unitPrice(product.getUnitPrice())
                .stockQuantity(product.getStockQuantity())
                .image(product.getImage())
                .category(product.getCategory().getCategoryName())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
        return new ResponseEntity<>(new ResponseDtoSuccess<>(productResponse, HttpStatus.OK), HttpStatus.OK);
    }
}
