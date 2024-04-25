package rikkei.academy.service.product;

import rikkei.academy.model.entity.Category;
import rikkei.academy.model.entity.Product;
import rikkei.academy.service.IGenericService;

import java.util.List;

public interface IProductService extends IGenericService<Product, Long> {
    List<Product> findByProductNameContaining(String partialName);

    List<Product> findFirst10ByOrderByCreatedAtDesc();

    List<Product> findByCategory(Category category);
}
