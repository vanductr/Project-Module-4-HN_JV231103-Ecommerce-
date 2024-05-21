package rikkei.academy.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rikkei.academy.exception.DataExistException;
import rikkei.academy.model.dto.request.FormChangePasswordRequest;
import rikkei.academy.model.dto.request.FormEditUserRequest;
import rikkei.academy.model.dto.request.FormLogin;
import rikkei.academy.model.dto.request.FormRegister;
import rikkei.academy.model.dto.response.JWTResponse;
import rikkei.academy.model.dto.response.UserDetailResponse;
import rikkei.academy.model.entity.Role;
import rikkei.academy.model.entity.RoleName;
import rikkei.academy.model.entity.User;
import rikkei.academy.repository.IRoleRepository;
import rikkei.academy.repository.IUserRepository;
import rikkei.academy.security.jwt.JWTProvider;
import rikkei.academy.security.principle.UserDetailsCustom;
import rikkei.academy.validator.DataValidator;

import java.time.LocalDate;
import java.util.*;

@Service
public class UserServiceIMPL implements IUserService{
    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private JWTProvider jwtProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private IUserRepository userRepository;

    @Override
    public String register(FormRegister formRegister) {
        if (userRepository.existsByUsername(formRegister.getUsername())) {
            return "username-error";
        }
        User user = User.builder()
                .email(formRegister.getEmail())
                .username(formRegister.getUsername())
                .fullName(formRegister.getFullName())
                .address(formRegister.getAddress())
                .password(passwordEncoder.encode(formRegister.getPassword()))
                .status(true)
                .build();

        if (formRegister.getRoles() != null && !formRegister.getRoles().isEmpty()) {
            Set<Role> roles = new HashSet<>();
            formRegister.getRoles().forEach(
                    r -> {
                        switch (r) {
                            case "ADMIN":
                                roles.add(roleRepository.findByRoleName(RoleName.ROLE_ADMIN).orElseThrow(() -> new NoSuchElementException("role not found")));
                            case "MANAGER":
                                roles.add(roleRepository.findByRoleName(RoleName.ROLE_MANAGER).orElseThrow(() -> new NoSuchElementException("role not found")));
                            case "USER":
                                roles.add(roleRepository.findByRoleName(RoleName.ROLE_USER).orElseThrow(() -> new NoSuchElementException("role not found")));
                            default:
                                roles.add(roleRepository.findByRoleName(RoleName.ROLE_USER).orElseThrow(() -> new NoSuchElementException("role not found")));
                        }
                    }
            );
            user.setRoleSet(roles);

        } else {
            // Mặc định là User
            Set<Role> roleSet = new HashSet<>();
            roleSet.add(roleRepository.findByRoleName(RoleName.ROLE_USER).orElseThrow(() -> new NoSuchElementException("role not found")));
            user.setRoleSet(roleSet);
        }
        userRepository.save(user);
        return "true";
    }

    @Override
    public JWTResponse login(FormLogin formLogin) throws DataExistException {
        // Xác thực thông qua username và password.
        Authentication authentication;
        try {
            authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(formLogin.getUsername(), formLogin.getPassword()));
        } catch (AuthenticationException e) {
            throw new DataExistException("username hoặc password không chính xác", "Lỗi");
        }
        UserDetailsCustom detailsCustom = (UserDetailsCustom) authentication.getPrincipal();

        String accessToken = jwtProvider.generateAccessToken(detailsCustom);

        return JWTResponse.builder()
                .email(detailsCustom.getEmail())
                .fullName(detailsCustom.getFullName())
                .roleSet(detailsCustom.getAuthorities())
                .status(detailsCustom.isStatus())
                .accessToken(accessToken)
                .build();
    }

    @Override
    public User findById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.orElseThrow(() -> new NoSuchElementException("Message: Không tồn tại Id"));
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public User save(User entity) {
        return userRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Page<User> findByRoleNameNot(RoleName roleName, Pageable pageable) {
        return userRepository.findByRoleNameNot(roleName, pageable);
    }

    @Override
    public List<User> findByUsernameContaining(String username) {
        return userRepository.findByUsernameContaining(username);
    }

    @Override
    public UserDetailResponse getUserDetail(UserDetailsCustom userDetailsCustom) {
        User user = findById(userDetailsCustom.getId());
        return UserDetailResponse.builder()
                .username(user.getUsername())
                .phone(user.getPhone())
                .avatar(user.getAvatar())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .address(user.getAddress())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .build();
    }

    @Override
    public UserDetailResponse editUserDetail(UserDetailsCustom userDetailsCustom, FormEditUserRequest formEditUserRequest) {
        User user = findById(userDetailsCustom.getId());
        user.setEmail(formEditUserRequest.getEmail());
        user.setAddress(formEditUserRequest.getAddress());
        user.setFullName(formEditUserRequest.getFullName());
        user.setAvatar(formEditUserRequest.getAvatar());
        user.setPhone(formEditUserRequest.getPhone());
        user.setUpdatedAt(LocalDate.now());

        userRepository.save(user);

        return getUserDetail(userDetailsCustom);
    }

    @Override
    public void changePassword(UserDetailsCustom userDetailsCustom, FormChangePasswordRequest formChangePasswordRequest) throws DataExistException {
        User user = findById(userDetailsCustom.getId());

        // Xác thực thông qua username và password.
        Authentication authentication;
        try {
            authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), formChangePasswordRequest.getOldPass()));
        } catch (AuthenticationException e) {
            throw new DataExistException("Mật khẩu cũ không chính xác.", "Lỗi");
        }
        UserDetailsCustom detailsCustom = (UserDetailsCustom) authentication.getPrincipal();

        if (!DataValidator.isValidPassword(formChangePasswordRequest.getNewPass())) {
            throw new DataExistException("Mật khẩu không đúng định dạng. Phải lớn hơn 8 kí tự, có chứa chữ In hoa và số!", "Lỗi");
        }

        if (!formChangePasswordRequest.getNewPass().equals(formChangePasswordRequest.getConfirmNewPass())) {
            throw new DataExistException("Nhập lại mật khẩu không đúng!", "Lỗi");
        }

        user.setPassword(passwordEncoder.encode(formChangePasswordRequest.getNewPass()));
        userRepository.save(user);
    }
}
