package health.care.medicore.ServicesImpl;

import health.care.medicore.Entities.Role;
import health.care.medicore.Repositories.RoleRepository;
import health.care.medicore.Services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends GenericServiceImpl<Role> implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public RoleServiceImpl() {
        super(Role.class);
    }

    @Override
    public Role getByRole(String role) {
        return roleRepository.findByRoleIgnoreCase(role);
    }
}
