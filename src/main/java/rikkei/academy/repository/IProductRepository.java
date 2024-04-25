package rikkei.academy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rikkei.academy.model.entity.Category;
import rikkei.academy.model.entity.Product;

import java.util.List;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByProductNameContaining(String partialName);

    List<Product> findFirst10ByOrderByCreatedAtDesc();

    List<Product> findByCategory(Category category);
}
