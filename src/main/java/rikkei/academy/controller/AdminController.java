package rikkei.academy.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rikkei.academy.exception.DataExistException;
import rikkei.academy.model.dto.request.FormCategoryRequest;
import rikkei.academy.model.dto.request.FormChangeOrderStatus;
import rikkei.academy.model.dto.request.FormProductRequest;
import rikkei.academy.model.dto.response.OrderResponseRoleAdmin;
import rikkei.academy.model.dto.response.ProductResponse;
import rikkei.academy.model.dto.response.ResponseDtoSuccess;
import rikkei.academy.model.dto.response.UserResponse;
import rikkei.academy.model.entity.*;
import rikkei.academy.service.category.ICategoryService;
import rikkei.academy.service.order.IOrderService;
import rikkei.academy.service.product.IProductService;
import rikkei.academy.service.role.IRoleService;
import rikkei.academy.service.user.IUserService;

import java.util.*;

@RestController
@RequestMapping("/api.myservice.com/v1/admin")
public class AdminController {
    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IOrderService orderService;

    // API: Lấy ra Danh sách Tất cả người dùng
    @GetMapping("/users")
    public ResponseEntity<?> getAllUser(Pageable pageable) {
        Page<User> userPage = userService.findByRoleNameNot(RoleName.ROLE_ADMIN, pageable);
        List<User> userList = userPage.getContent();
        return getResponseEntity(userList);
    }

    // API: Khoá / Mở khoá người dùng
    @PutMapping("/users/{userId}")
    public ResponseEntity<?> blockOrUnBlockUser(@PathVariable Long userId) {
        User u = userService.findById(userId);
        u.setStatus(!u.getStatus());
        userService.save(u);
        User user = userService.findById(u.getUserId());
        UserResponse userResponse;
        userResponse = UserResponse.builder()
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .avatar(user.getAvatar())
                .address(user.getAddress())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .isDeleted(user.getIsDeleted())
                .build();
        Set<Role> roleSet = user.getRoleSet();
        Set<RoleName> roleNames = new HashSet<>();
        for (Role role : roleSet) {
            RoleName roleName = role.getRoleName();
            roleNames.add(roleName);
        }
        userResponse.setRoleSet(roleNames);
        return new ResponseEntity<>(new ResponseDtoSuccess<>(userResponse, HttpStatus.OK), HttpStatus.OK);
    }

    // API: Lấy về Danh sách các quyền
    @GetMapping("/roles")
    public ResponseEntity<?> getAllRole(Pageable pageable) {
        Page<Role> rolePage = roleService.findAll(pageable);
        List<Role> roleList = rolePage.getContent();
        Set<RoleName> roleNames = new HashSet<>();
        for (Role role : roleList) {
            RoleName roleName = role.getRoleName();
            roleNames.add(roleName);
        }
        return new ResponseEntity<>(new ResponseDtoSuccess<>(roleNames, HttpStatus.OK), HttpStatus.OK);
    }

    // API: Tìm kiếm người dùng theo tên
    @GetMapping("/users/search")
    public ResponseEntity<?> searchUserByName(@RequestParam(name = "search") String search) {
        List<User> userList = userService.findByUsernameContaining(search);
        return getResponseEntity(userList);
    }

    // Chuyển đổi từ Dữ liệu (Data) sang Object.
    private ResponseEntity<?> getResponseEntity(List<User> userList) {
        List<UserResponse> userResponseList = new ArrayList<>();
        for (User user : userList) {
            UserResponse userResponse;
            userResponse = UserResponse.builder()
                    .username(user.getUsername())
                    .fullName(user.getFullName())
                    .email(user.getEmail())
                    .phone(user.getPhone())
                    .avatar(user.getAvatar())
                    .address(user.getAddress())
                    .status(user.getStatus())
                    .createdAt(user.getCreatedAt())
                    .updatedAt(user.getUpdatedAt())
                    .isDeleted(user.getIsDeleted())
                    .build();
            Set<Role> roleSet = user.getRoleSet();
            Set<RoleName> roleNames = new HashSet<>();
            for (Role role : roleSet) {
                RoleName roleName = role.getRoleName();
                roleNames.add(roleName);
            }
            userResponse.setRoleSet(roleNames);
            userResponseList.add(userResponse);
        }
        return new ResponseEntity<>(new ResponseDtoSuccess<>(userResponseList, HttpStatus.OK), HttpStatus.OK);
    }

    // API: Lấy về Danh sách Tất cả Sản phẩm (có Sắp xếp và phân trang)
    @GetMapping("/products")
    public ResponseEntity<?> getAllProduct(Pageable pageable) {
        Page<Product> productPage = productService.findAll(pageable);
        List<Product> productList = productPage.getContent();
        return getProductResponseEntity(productList);
    }

    static ResponseEntity<?> getProductResponseEntity(List<Product> productList) {
        List<ProductResponse> productResponseList = new ArrayList<>();
        for (Product product : productList) {
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
            productResponseList.add(productResponse);
        }
        return new ResponseEntity<>(new ResponseDtoSuccess<>(productResponseList, HttpStatus.OK), HttpStatus.OK);
    }

    // API: Thông tin chi tiết của Sản phẩm theo ID
    @GetMapping("/products/{productId}")
    public ResponseEntity<?> getDetailsProductById(@PathVariable Long productId) {
        Product product = productService.findById(productId);
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

    // API: Thêm mới Sản phẩm
    @PostMapping("/products")
    public ResponseEntity<?> addNewProduct(@RequestBody FormProductRequest formProductRequest) throws DataExistException {
        Map<String, Object> map = new HashMap<>();
        Product product = productService.save(formProductRequest);
        map.put("Đã thêm thành công Sản phẩm: ", product);
        return new ResponseEntity<>(new ResponseDtoSuccess<>(map, HttpStatus.CREATED), HttpStatus.CREATED);
    }

    // API: Chỉnh sửa thông tin sản phẩm
    @PutMapping("/products/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable Long productId, @Valid @RequestBody FormProductRequest formProductRequest) {
        return new ResponseEntity<>(new ResponseDtoSuccess<>(productService.save(formProductRequest, productId), HttpStatus.OK), HttpStatus.OK);
    }

    // API: Xoá Sản phẩm
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        productService.deleteById(productId);
        return new ResponseEntity<>(new ResponseDtoSuccess<>("Đã xoá thành công sản phẩm có ID: " + productId, HttpStatus.OK), HttpStatus.OK);
    }

    // API: Lấy về danh sách tất cả danh mục (sắp xếp và phân trang)
    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategory(Pageable pageable) {
        Page<Category> categoryPage = categoryService.findAll(pageable);
        return new ResponseEntity<>(new ResponseDtoSuccess<>(categoryPage, HttpStatus.OK), HttpStatus.OK);
    }

    // API: Lấy về thông tin danh mục theo id
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<?> getDetailsCategoryById(@PathVariable Long categoryId) {
        Category category = categoryService.findById(categoryId);
        return new ResponseEntity<>(new ResponseDtoSuccess<>(category, HttpStatus.OK), HttpStatus.OK);
    }

    // API: Thêm mới danh mục
    @PostMapping ("/categories")
    public ResponseEntity<?> addNewCategory(@RequestBody FormCategoryRequest categoryRequest) throws DataExistException {
        return new ResponseEntity<>(new ResponseDtoSuccess<>(categoryService.save(categoryRequest), HttpStatus.CREATED), HttpStatus.CREATED);
    }

    // API: Chỉnh sửa thông tin danh mục - /api.myservice.com/v1/admin/categories/{categoryId}
    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<?> updateCategory(@PathVariable Long categoryId, @RequestBody FormCategoryRequest categoryRequest) {
        return new ResponseEntity<>(new ResponseDtoSuccess<>(categoryService.save(categoryService.save(categoryRequest, categoryId)), HttpStatus.OK), HttpStatus.OK);
    }

    // API: Xóa danh mục - /api.myservice.com/v1/admin//categories/{categoryId}
    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteById(categoryId);
        return new ResponseEntity<>(new ResponseDtoSuccess<>("Đã xoá thành công Danh mục có Id: " + categoryId, HttpStatus.OK), HttpStatus.OK);
    }

    // API: Danh sách tất cả đơn hàng: - /api.myservice.com/v1/admin/orders
    @GetMapping("/orders")
    public ResponseEntity<?> getAllOrder(Pageable pageable) {
        Page<OrderResponseRoleAdmin> orderPage = orderService.getAllOrderRoleAdmin(pageable);
        List<OrderResponseRoleAdmin> orderList = orderPage.getContent();
        return new ResponseEntity<>(new ResponseDtoSuccess<>(orderList, HttpStatus.OK), HttpStatus.OK);
    }

    // API: Danh sách đơn hàng theo trạng thái - /api.myservice.com/v1/admin/orders/{orderStatus}
    @GetMapping("/orders/{orderStatus}")
    public ResponseEntity<?> getOrdersByStatus(@PathVariable String orderStatus, Pageable pageable) throws DataExistException {
        Page<OrderResponseRoleAdmin> orderResponseRoleAdmins = orderService.findByStatusOrderStatusName(orderStatus, pageable);
        List<OrderResponseRoleAdmin> orderList = orderResponseRoleAdmins.getContent();
        return new ResponseEntity<>(new ResponseDtoSuccess<>(orderList, HttpStatus.OK), HttpStatus.OK);
    }

    // API: Chi tiết đơn hàng - /api.myservice.com/v1/admin/orders/{orderId}
    @GetMapping("/orders2/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable Long orderId) {
        OrderResponseRoleAdmin orderResponseRoleAdmin = orderService.getOrderById(orderId);
        return new ResponseEntity<>(new ResponseDtoSuccess<>(orderResponseRoleAdmin, HttpStatus.OK), HttpStatus.OK);
    }

    // API: Cập nhật trạng thái đơn hàng (payload : orderStatus) - /api.myservice.com/v1/admin/orders/{orderId}/status
    @PutMapping("/orders/{orderId}/status")
    public ResponseEntity<?> changeOrderStatus(@PathVariable Long orderId, @RequestBody FormChangeOrderStatus formChangeOrderStatus) {
        OrderResponseRoleAdmin orderResponseRoleAdmin = orderService.updateOrderStatusById(orderId, formChangeOrderStatus.getOrderStatusName());
        return new ResponseEntity<>(new ResponseDtoSuccess<>(orderResponseRoleAdmin, HttpStatus.OK), HttpStatus.OK);
    }
}
