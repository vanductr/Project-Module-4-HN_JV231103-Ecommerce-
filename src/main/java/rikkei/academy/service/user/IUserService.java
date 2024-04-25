package rikkei.academy.service.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import rikkei.academy.model.dto.request.FormLogin;
import rikkei.academy.model.dto.request.FormRegister;
import rikkei.academy.model.dto.response.JWTResponse;
import rikkei.academy.model.entity.RoleName;
import rikkei.academy.model.entity.User;
import rikkei.academy.service.IGenericService;

import java.util.List;


public interface IUserService extends IGenericService<User, Long> {
    boolean register(FormRegister formRegister);

    JWTResponse login(FormLogin formLogin);

    Page<User> findByRoleNameNot(RoleName roleName, Pageable pageable);

    List<User> findByUsernameContaining(String username);
}
