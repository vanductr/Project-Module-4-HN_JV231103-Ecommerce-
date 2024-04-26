package rikkei.academy.service.shopping_cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rikkei.academy.exception.DataExistException;
import rikkei.academy.model.entity.*;
import rikkei.academy.repository.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Service
public class ShoppingCartServiceIMPL implements IShoppingCartService {
    @Autowired
    private IShoppingCartRepository shoppingCartRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    IProductRepository productRepository;

    @Autowired
    IOrderStatusRepository orderStatusRepository;

    @Autowired
    IOrderRepository orderRepository;

    @Autowired
    IOrderDetailRepository orderDetailRepository;

    @Override
    public ShoppingCart findById(Long id) {
        return shoppingCartRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Không tìm thấy tài nguyên"));
    }

    @Override
    public Page<ShoppingCart> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public ShoppingCart save(ShoppingCart entity) {
        return shoppingCartRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public List<ShoppingCart> findByUser(User user) {
        return shoppingCartRepository.findByUser(user);
    }

    @Override
    public void save(Long userId, Long productId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("Không tồn tại Id"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new NoSuchElementException("Không tìm thấy Id"));
        List<ShoppingCart> shoppingCartList = shoppingCartRepository.findByUser(user);
        boolean flag = false; // Sản phẩm đã có trong giỏ hàng hay chưa? - Mặc định là chưa.
        for (ShoppingCart cart : shoppingCartList) {
            if (cart.getProduct().equals(product)) {
                int orderQuantity = cart.getOrderQuantity();
                cart.setOrderQuantity(orderQuantity + 1);
                shoppingCartRepository.save(cart);
                flag = true; // Sản phẩm đã có trong giỏ hàng
                break;
            }
        }
        if (!flag) { // Nếu chưa có sản phẩm trong giỏ hàng.
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setUser(user);
            shoppingCart.setProduct(product);
            shoppingCart.setOrderQuantity(1);
            shoppingCartRepository.save(shoppingCart);
        }
    }

    @Override
    public void changeQuantityCartItem(Long userId, Long productId, Integer quantity) throws DataExistException {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("Không tồn tại Id"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new NoSuchElementException("Không tìm thấy Id"));
        List<ShoppingCart> shoppingCartList = shoppingCartRepository.findByUser(user);
        boolean flag = false; // Sản phẩm đã có trong giỏ hàng hay chưa? - Mặc định là chưa.
        for (ShoppingCart cart : shoppingCartList) {
            if (cart.getProduct().equals(product)) {
                int orderQuantity = cart.getOrderQuantity();
                cart.setOrderQuantity(quantity);
                shoppingCartRepository.save(cart);
                flag = true; // Sản phẩm đã có trong giỏ hàng
                break;
            }
        }
        if (!flag) { // Nếu chưa có sản phẩm trong giỏ hàng.
            // Trường hợp 1: Vẫn cho lưu vào Giỏ hàng
//            ShoppingCart shoppingCart = new ShoppingCart();
//            shoppingCart.setUser(user);
//            shoppingCart.setProduct(product);
//            shoppingCart.setOrderQuantity(quantity);
//            shoppingCartRepository.save(shoppingCart);

            // Trường hợp 2: Không cho lưu vào trong giỏ hàng và thông báo lỗi
            throw new DataExistException("Sản phẩm này chưa có trong giỏ hàng", "cartItem");
        }
    }

    @Override
    public void deleteCartItem(Long userId, Long cartItemId) throws DataExistException {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("Không tồn tại Id"));
        ShoppingCart shoppingCart = findById(cartItemId);
        if (shoppingCart.getUser().equals(user)) {
            shoppingCartRepository.deleteById(cartItemId);
        } else {
            throw new DataExistException("Sản phẩm này không có trong giỏ hàng của bạn", "Lỗi: ");
        }
    }

    @Override
    @Transactional
    public void clearShoppingCart(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("Không tồn tại Id"));
        shoppingCartRepository.deleteByUser(user);
    }

    @Override
    public ShoppingCart checkout(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("Không tồn tại Id"));
        List<ShoppingCart> shoppingCartList = shoppingCartRepository.findByUser(user);

        for (ShoppingCart shoppingCart : shoppingCartList) {
            Integer cartItemQuantity = shoppingCart.getOrderQuantity();
            Product item = shoppingCart.getProduct();
            Product product = productRepository.findById(item.getProductId()).orElse(null); // Lấy ra sản phẩm người dùng muốn mua trong DTB

            assert product != null;
            if (cartItemQuantity > product.getStockQuantity()) {
                return shoppingCart; // Trả về Sản phẩm không có đủ số lượng trong kho
            }
        }

        // Nếu tất cả các sản phẩm trong Giỏ hàng có đủ số lượng trong kho
        Order order = new Order();
        order.setSerialNumber(UUID.randomUUID().toString());
        order.setUser(user);
        OrderStatus orderStatus = orderStatusRepository.findByOrderStatusName(OrderStatusName.WAITING);
        order.setStatus(orderStatus);
        order.setNote("Thank you!!!");
        order.setReceiveName(user.getFullName());
        order.setReceiveAddress(user.getAddress());
        order.setReceivePhone(user.getPhone());
        order.setCreatedAt(LocalDate.now());
        order.setReceivedAt(LocalDate.now().plusDays(4));
        orderRepository.save(order);

        double totalPrice = 0;
        for (ShoppingCart shoppingCart : shoppingCartList) {
            Integer cartItemQuantity = shoppingCart.getOrderQuantity(); // Số lượng người ta mua
            Product product = productRepository.findById(shoppingCart.getProduct().getProductId()).orElseThrow(() -> new NoSuchElementException("Tài nguyên không tồn tại"));
            Integer productQuantityRepo = product.getStockQuantity(); // Lấy ra số lượng trong kho
            Integer stockQuantity = productQuantityRepo - cartItemQuantity;
            product.setStockQuantity(stockQuantity);
            productRepository.save(product);

            shoppingCartRepository.deleteById(shoppingCart.getShoppingCartId()); // Reset lại Giỏ hàng, chuyển nó vào OrderDetail

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(shoppingCart.getProduct());
            orderDetail.setOrderQuantity(shoppingCart.getOrderQuantity());
            double orderDetailPrice = shoppingCart.getProduct().getUnitPrice() * shoppingCart.getOrderQuantity();
            totalPrice += orderDetailPrice;
            orderDetail.setUnitPrice(BigDecimal.valueOf(orderDetailPrice));
            orderDetailRepository.save(orderDetail);
        }

        order.setTotalPrice(BigDecimal.valueOf(totalPrice));

        orderRepository.save(order);

        return null;
    }
}
