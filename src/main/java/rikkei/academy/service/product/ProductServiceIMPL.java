package rikkei.academy.service.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rikkei.academy.exception.DataExistException;
import rikkei.academy.model.dto.request.FormProductRequest;
import rikkei.academy.model.entity.Category;
import rikkei.academy.model.entity.Product;
import rikkei.academy.repository.ICategoryRepository;
import rikkei.academy.repository.IProductRepository;

import java.util.*;

@Service
public class ProductServiceIMPL implements IProductService {
    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private ICategoryRepository categoryRepository;

    @Override
    public Product findById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElseThrow(() -> new NoSuchElementException("Message: Không tồn tại Id"));
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Product save(Product entity) {
        return productRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Không tồn tại sản phẩm!"));
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> findByProductNameContaining(String partialName) {
        return productRepository.findByProductNameContaining(partialName);
    }

    @Override
    public List<Product> findFirst10ByOrderByCreatedAtDesc() {
        return productRepository.findFirst10ByOrderByCreatedAtDesc();
    }

    @Override
    public List<Product> findByCategory(Category category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public Product save(FormProductRequest productRequest) throws DataExistException{
        if (productRepository.existsByProductName(productRequest.getProductName())) {
            throw new DataExistException("Tên sản phẩm đã tồn tại!", "productName");
        }
        Product product = Product.builder()
                .productName(productRequest.getProductName())
                .description(productRequest.getDescription())
                .unitPrice(productRequest.getUnitPrice())
                .stockQuantity(productRequest.getStockQuantity())
                .image(productRequest.getImage())
                .build();
        Optional<Category> categoryOptional = categoryRepository.findById(productRequest.getCategory());
        Category category = categoryOptional.orElseThrow(() -> new NoSuchElementException("Message: Không tồn tại Id"));
        product.setCategory(category);
        product.setSku(UUID.randomUUID().toString());
        product.setCreatedAt(new Date());
        product.setUpdatedAt(new Date());

        return productRepository.save(product);
    }

    @Override
    public Product save(FormProductRequest formProductRequest, Long productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        Product product = productOptional.orElseThrow(() -> new NoSuchElementException("Không tồn tại Id"));
        product.setProductName(formProductRequest.getProductName());
        product.setDescription(formProductRequest.getDescription());
        product.setUnitPrice(formProductRequest.getUnitPrice());
        product.setStockQuantity(formProductRequest.getStockQuantity());
        product.setImage(formProductRequest.getImage());
        product.setCategory(categoryRepository.findById(formProductRequest.getCategory()).orElseThrow(() -> new NoSuchElementException("Không tồn tại Id")));
        product.setUpdatedAt(new Date());
        return productRepository.save(product);
    }
}
