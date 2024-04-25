package rikkei.academy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rikkei.academy.model.dto.response.ResponseDtoSuccess;
import rikkei.academy.model.dto.response.UserResponse;
import rikkei.academy.model.entity.Role;
import rikkei.academy.model.entity.RoleName;
import rikkei.academy.model.entity.User;
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
}
