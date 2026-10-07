//Bảo
package com.quanlykhov.model;

import jakarta.persistence.*;

@Entity
@Table(name = "NhanVien")
public class Employee {

    @Id
    @Column(name = "MaNV", length = 20)
    private String id; 
    @Column(name = "HoTen", length = 100, nullable = false)
    private String fullName;

    @Column(name = "Sdt", length = 15)
    private String phone;

    public Employee() {
    }

    public Employee(String id, String fullName, String phone) {
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
    public void setSalary(Double salary) {
        this.salary = salary;
    }
}
