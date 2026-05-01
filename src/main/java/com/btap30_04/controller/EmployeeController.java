package com.btap30_04.controller;

import com.btap30_04.dto.EmployeeDTO;
import com.btap30_04.entity.Department;
import com.btap30_04.entity.Employee;
import com.btap30_04.repository.DepartmentRepository;
import com.btap30_04.repository.EmployeeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Controller
public class EmployeeController {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    private final String UPLOAD_DIR = "uploads/";

    public EmployeeController(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    @GetMapping("/employees")
    public String list(Model model) {
        model.addAttribute("employees", employeeRepository.findAll());
        return "employee-list";
    }

    @GetMapping("/employees/add")
    public String showForm(Model model) {
        model.addAttribute("employeeDTO", new EmployeeDTO());
        model.addAttribute("departments", departmentRepository.findAll());
        return "employee-form";
    }

    @PostMapping("/employees/add")
    public String save(@ModelAttribute EmployeeDTO dto) {

        String fileName = "default.png";

        try {
            MultipartFile file = dto.getFile();

            if (file != null && !file.isEmpty()) {

                String originalName = file.getOriginalFilename();
                String newName = UUID.randomUUID() + "_" + originalName;

                String uploadPath = System.getProperty("user.dir") + "/uploads/";

                File dir = new File(uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                file.transferTo(new File(uploadPath + newName));

                fileName = newName;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Department dept = departmentRepository.findById(dto.getDepartmentId()).orElse(null);

        Employee emp = new Employee(
                dto.getName(),
                dto.getAge(),
                fileName,
                dto.getStatus(),
                dept
        );

        employeeRepository.save(emp);

        return "redirect:/employees";
    }
}