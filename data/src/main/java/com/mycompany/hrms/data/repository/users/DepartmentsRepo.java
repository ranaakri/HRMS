package com.mycompany.hrms.data.repository.users;

import com.mycompany.hrms.data.entity.user.Departments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentsRepo extends JpaRepository<Departments, Long> {
}