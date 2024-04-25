package rikkei.academy.service.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rikkei.academy.model.entity.Category;
import rikkei.academy.model.entity.Product;
import rikkei.academy.repository.IProductRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ProductServiceIMPL implements IProductService {
    @Autowired
    private IProductRepository productRepository;

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
        return null;
    }

    @Override
    public void deleteById(Long id) {

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
}
