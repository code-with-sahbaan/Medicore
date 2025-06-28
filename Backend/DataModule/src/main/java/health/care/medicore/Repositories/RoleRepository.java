package health.care.medicore.Repositories;

import health.care.medicore.Entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByRoleIgnoreCase(String role);
}
