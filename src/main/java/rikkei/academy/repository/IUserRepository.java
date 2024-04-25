package rikkei.academy.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rikkei.academy.model.entity.RoleName;
import rikkei.academy.model.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.username LIKE %:username%")
    List<User> findByUsernameContaining(@Param("username") String username);

    @Query("SELECT u FROM User u WHERE NOT EXISTS (SELECT 1 FROM u.roleSet r WHERE r.roleName = :roleName)")
    Page<User> findByRoleNameNot(@Param("roleName") RoleName roleName, Pageable pageable);
}
