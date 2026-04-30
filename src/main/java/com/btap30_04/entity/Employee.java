package com.btap30_04.entity;

import jakarta.persistence.*;

@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int age;
    private String avatar;
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    public Employee() {}

    public Employee(String name, int age, String avatar, String status, Department department) {
        this.name = name;
        this.age = age;
        this.avatar = avatar;
        this.status = status;
        this.department = department;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getAvatar() { return avatar; }
    public String getStatus() { return status; }
    public Department getDepartment() { return department; }

    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public void setStatus(String status) { this.status = status; }
    public void setDepartment(Department department) { this.department = department; }
}