package rikkei.academy.service.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import rikkei.academy.exception.DataExistException;
import rikkei.academy.model.dto.request.FormChangePasswordRequest;
import rikkei.academy.model.dto.request.FormEditUserRequest;
import rikkei.academy.model.dto.request.FormLogin;
import rikkei.academy.model.dto.request.FormRegister;
import rikkei.academy.model.dto.response.JWTResponse;
import rikkei.academy.model.dto.response.UserDetailResponse;
import rikkei.academy.model.dto.response.UserResponse;
import rikkei.academy.model.entity.RoleName;
import rikkei.academy.model.entity.User;
import rikkei.academy.security.principle.UserDetailsCustom;
import rikkei.academy.service.IGenericService;

import java.util.List;


public interface IUserService extends IGenericService<User, Long> {
    boolean register(FormRegister formRegister);

    JWTResponse login(FormLogin formLogin) throws DataExistException;

    Page<User> findByRoleNameNot(RoleName roleName, Pageable pageable);

    List<User> findByUsernameContaining(String username);

    UserDetailResponse getUserDetail(UserDetailsCustom userDetailsCustom);

    UserDetailResponse editUserDetail(UserDetailsCustom userDetailsCustom, FormEditUserRequest formEditUserRequest);

    void changePassword(UserDetailsCustom userDetailsCustom, FormChangePasswordRequest formChangePasswordRequest) throws DataExistException;
}
