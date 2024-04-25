package rikkei.academy.service.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rikkei.academy.model.entity.Role;
import rikkei.academy.repository.IRoleRepository;

@Service
public class RoleServiceIMPL implements IRoleService{
    @Autowired
    private IRoleRepository roleRepository;

    @Override
    public Role findById(Long id) {
        return null;
    }

    @Override
    public Page<Role> findAll(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }

    @Override
    public Role save(Role entity) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }
}
