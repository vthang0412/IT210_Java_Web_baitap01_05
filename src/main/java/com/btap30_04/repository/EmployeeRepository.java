package com.btap30_04.repository;

import com.btap30_04.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Page<Employee> findByNameContainingIgnoreCase(String name, Pageable pageable);

}