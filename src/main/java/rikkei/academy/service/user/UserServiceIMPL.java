package rikkei.academy.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rikkei.academy.exception.AuthenticationFailedException;
import rikkei.academy.exception.DataExistException;
import rikkei.academy.exception.EmailAlreadyExistsException;
import rikkei.academy.exception.UsernameAlreadyExistsException;
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
import rikkei.academy.service.StorageService;
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

    @Autowired
    private StorageService storageService;

    @Override
    public boolean register(FormRegister formRegister) {
        if (userRepository.existsByUsername(formRegister.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }
        if (userRepository.existsByEmail(formRegister.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
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
        return true;
    }

    @Override
    public JWTResponse login(FormLogin formLogin) throws DataExistException {
        // Xác thực thông qua username và password.
        Authentication authentication;
        try {
            authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(formLogin.getUsername(), formLogin.getPassword()));
        } catch (AuthenticationException e) {
            throw new AuthenticationFailedException("Invalid username or password");
        }
        UserDetailsCustom detailsCustom = (UserDetailsCustom) authentication.getPrincipal();

        String accessToken = jwtProvider.generateAccessToken(detailsCustom);

        return JWTResponse.builder()
                .email(detailsCustom.getEmail())
                .fullName(detailsCustom.getFullName())
                .roleSet(detailsCustom.getAuthorities())
                .status(detailsCustom.isStatus())
                .phone(detailsCustom.getPhone())
                .avatar(detailsCustom.getAvatar())
                .address(detailsCustom.getAddress())
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

        List<? extends GrantedAuthority> authorityList = user.getRoleSet().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName().name())).toList();

        return UserDetailResponse.builder()
                .roleSet(authorityList)
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
        String fileUrl = null;
        MultipartFile file = formEditUserRequest.getImage();
        if (file != null && !file.isEmpty()) {
            // Upload file nếu file không rỗng
            fileUrl = storageService.uploadFile(file);
        }

        User user = findById(userDetailsCustom.getId());
        user.setEmail(formEditUserRequest.getEmail());
        user.setAddress(formEditUserRequest.getAddress());
        user.setFullName(formEditUserRequest.getFullName());
        user.setAvatar(fileUrl);
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
