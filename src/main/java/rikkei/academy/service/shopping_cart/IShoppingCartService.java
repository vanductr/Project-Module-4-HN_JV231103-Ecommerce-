package rikkei.academy.service.shopping_cart;

import rikkei.academy.exception.DataExistException;
import rikkei.academy.model.entity.ShoppingCart;
import rikkei.academy.model.entity.User;
import rikkei.academy.service.IGenericService;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface IShoppingCartService extends IGenericService<ShoppingCart, Long> {
    List<ShoppingCart> findByUser(User user);

    void save(Long userId, Long productId);

    void changeQuantityCartItem(Long userId, Long productId, Integer quantity) throws DataExistException;

    void deleteCartItem(Long userId, Long cartItemId) throws DataExistException;

    void clearShoppingCart(Long userId);

    ShoppingCart checkout(Long userId);
}
