package com.mycompany.hrms.data.repository.users;

import com.mycompany.hrms.data.entity.user.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepo extends JpaRepository<Roles, Long> {
}
