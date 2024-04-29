package rikkei.academy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import rikkei.academy.exception.DataExistException;
import rikkei.academy.model.dto.request.FormAddToCartRequest;
import rikkei.academy.model.dto.request.FormChangeQuantityCartItem;
import rikkei.academy.model.dto.response.OrderResponse;
import rikkei.academy.model.dto.response.ResponseDtoSuccess;
import rikkei.academy.model.dto.response.ShoppingCartResponse;
import rikkei.academy.model.entity.Product;
import rikkei.academy.model.entity.ShoppingCart;
import rikkei.academy.model.entity.User;
import rikkei.academy.security.principle.UserDetailsCustom;
import rikkei.academy.service.order.IOrderService;
import rikkei.academy.service.product.IProductService;
import rikkei.academy.service.shopping_cart.IShoppingCartService;
import rikkei.academy.service.user.IUserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api.myservice.com/v1/user")
public class UserController {
    @Autowired
    private IUserService userService;

    @Autowired
    private IShoppingCartService shoppingCartService;

    @Autowired
    private IProductService productService;

    @Autowired
    private IOrderService orderService;

    // API: Danh sách sản phẩm trong giỏ hàng - /api.myservice.com/v1/user/cart/list
    @GetMapping("/cart/list")
    public ResponseEntity<?> getCartList(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom) {
        User user = userService.findById(userDetailsCustom.getId());
        List<ShoppingCart> shoppingCartList = shoppingCartService.findByUser(user);
        Map<String, Integer> cart = new HashMap<>();
        for (ShoppingCart shoppingCart : shoppingCartList) {
            cart.put(shoppingCart.getProduct().getProductName(), shoppingCart.getOrderQuantity());
        }
        ShoppingCartResponse shoppingCartResponse = new ShoppingCartResponse(cart);
        return new ResponseEntity<>(new ResponseDtoSuccess<>(shoppingCartResponse, HttpStatus.OK), HttpStatus.OK);
    }

    // API: Thêm mới sản phẩm vào giỏ hàng (payload: productId and quantity) - /api.myservice.com/v1/user/cart/add
    @PostMapping("/cart/add")
    public ResponseEntity<?> addToCart(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom, @RequestBody FormAddToCartRequest formAddToCartRequest) {
        shoppingCartService.save(userDetailsCustom.getId(), formAddToCartRequest.getProductId());
        User user = userService.findById(userDetailsCustom.getId());
        List<ShoppingCart> shoppingCartList = shoppingCartService.findByUser(user);
        Map<String, Integer> cart = new HashMap<>();
        for (ShoppingCart shoppingCart : shoppingCartList) {
            cart.put(shoppingCart.getProduct().getProductName(), shoppingCart.getOrderQuantity());
        }
        ShoppingCartResponse shoppingCartResponse = new ShoppingCartResponse(cart);
        return new ResponseEntity<>(new ResponseDtoSuccess<>(shoppingCartResponse, HttpStatus.OK), HttpStatus.OK);
    }

    // API: Thay đổi số lượng đặt hàng của 1 sản phẩm  (payload :quantity) - /api.myservice.com/v1/user/cart/items/{cartItemId}
    @PutMapping("/cart/items/{cartItemId}")
    public ResponseEntity<?> changeQuantityCartItem(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom, @PathVariable Long cartItemId, @RequestBody FormChangeQuantityCartItem changeQuantityCartItem) throws DataExistException {
        shoppingCartService.changeQuantityCartItem(userDetailsCustom.getId(), cartItemId, changeQuantityCartItem.getCartItemQuantity());
        User user = userService.findById(userDetailsCustom.getId());
        List<ShoppingCart> shoppingCartList = shoppingCartService.findByUser(user);
        Map<String, Integer> cart = new HashMap<>();
        for (ShoppingCart shoppingCart : shoppingCartList) {
            cart.put(shoppingCart.getProduct().getProductName(), shoppingCart.getOrderQuantity());
        }
        ShoppingCartResponse shoppingCartResponse = new ShoppingCartResponse(cart);
        return new ResponseEntity<>(new ResponseDtoSuccess<>(shoppingCartResponse, HttpStatus.OK), HttpStatus.OK);
    }

    // API: Xóa 1 sản phẩm trong giỏ hàng - /api.myservice.com/v1/user/cart/items/{cartItemId}
    @DeleteMapping("/cart/items/{cartItemId}")
    public ResponseEntity<?> deleteCartItem(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom, @PathVariable Long cartItemId) throws DataExistException {
        shoppingCartService.deleteCartItem(userDetailsCustom.getId(), cartItemId);
        return new ResponseEntity<>(new ResponseDtoSuccess<>("Bạn đã bỏ một sản phẩm ra khỏi giỏ hàng", HttpStatus.OK), HttpStatus.OK);
    }

    // API: Xóa toàn bộ sản phẩm trong giỏ hàng - /api.myservice.com/v1/user/cart/clear
    @DeleteMapping("/cart/clear")
    public ResponseEntity<?> clearShoppingCart(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom) {
        shoppingCartService.clearShoppingCart(userDetailsCustom.getId());
        return new ResponseEntity<>(new ResponseDtoSuccess<>("Bạn đã Xoá toàn bộ Sản phẩm khỏi giỏ hàng", HttpStatus.OK), HttpStatus.OK);
    }

    // API: Đặt hàng - /api.myservice.com/v1/user/cart/checkout
    @PostMapping("/cart/checkout")
    public ResponseEntity<?> checkout(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom) {
        ShoppingCart shoppingCart = shoppingCartService.checkout(userDetailsCustom.getId());
        if (shoppingCart != null) {
            Map<String, Integer> map = new HashMap<>();
            Product product = productService.findById(shoppingCart.getProduct().getProductId());
            map.put("Sản phẩm " + shoppingCart.getProduct().getProductName() + " hiện tại không có đủ số lượng trong Kho. Số lượng hiện tại: ", product.getStockQuantity());
            return new ResponseEntity<>(new ResponseDtoSuccess<>(map, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
        Map<String, OrderResponse> map = new HashMap<>();
        OrderResponse orderResponse = orderService.getOrderResponse(userDetailsCustom);
        map.put("Đã chuyển Đơn hàng Sang ORDER", orderResponse);
        return new ResponseEntity<>(new ResponseDtoSuccess<>(map, HttpStatus.OK), HttpStatus.OK);
    }
}
