package rikkei.academy.service.product;

import org.springframework.data.domain.Pageable;
import rikkei.academy.exception.DataExistException;
import rikkei.academy.model.dto.PageDTO;
import rikkei.academy.model.dto.request.FormProductRequest;
import rikkei.academy.model.dto.response.ProductResponse;
import rikkei.academy.model.entity.Category;
import rikkei.academy.model.entity.Product;
import rikkei.academy.service.IGenericService;

import java.util.List;

public interface IProductService extends IGenericService<Product, Long> {
    List<Product> findByProductNameContaining(String partialName);

    List<Product> findFirst10ByOrderByCreatedAtDesc();

    List<Product> findByCategory(Category category);

    Product save(FormProductRequest productRequest) throws DataExistException;

    Product save(FormProductRequest formProductRequest, Long productId);

    public PageDTO<ProductResponse> getAllProductRolePermitAll(Pageable pageable);
}
