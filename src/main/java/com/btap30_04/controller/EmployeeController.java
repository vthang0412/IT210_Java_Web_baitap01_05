package com.btap30_04.controller;

import com.btap30_04.dto.EmployeeDTO;
import com.btap30_04.entity.Department;
import com.btap30_04.entity.Employee;
import com.btap30_04.repository.DepartmentRepository;
import com.btap30_04.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public String list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "name") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String keyword,
            Model model) {

        int size = 3;

        Sort sort = sortDir.equals("asc") ?
                Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Employee> employeePage;

        if (keyword != null && !keyword.isEmpty()) {
            employeePage = employeeRepository.findByNameContainingIgnoreCase(keyword, pageable);
        } else {
            employeePage = employeeRepository.findAll(pageable);
        }

        model.addAttribute("employees", employeePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", employeePage.getTotalPages());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("keyword", keyword);

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