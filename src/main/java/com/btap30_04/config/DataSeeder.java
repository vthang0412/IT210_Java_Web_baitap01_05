package com.btap30_04.config;

import com.btap30_04.entity.Department;
import com.btap30_04.entity.Employee;
import com.btap30_04.repository.DepartmentRepository;
import com.btap30_04.repository.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initData(DepartmentRepository deptRepo, EmployeeRepository empRepo) {
        return args -> {
            if (deptRepo.count() == 0) {

                Department it = new Department("IT", "Ha Noi");
                Department hr = new Department("HR", "Ho Chi Minh");

                deptRepo.save(it);
                deptRepo.save(hr);

                empRepo.save(new Employee("Thang", 20, "img1.jpg", "ACTIVE", it));
                empRepo.save(new Employee("Dat", 22, "img2.jpg", "INACTIVE", it));
                empRepo.save(new Employee("Mai", 21, "img3.jpg", "ACTIVE", hr));
            }
        };
    }
}