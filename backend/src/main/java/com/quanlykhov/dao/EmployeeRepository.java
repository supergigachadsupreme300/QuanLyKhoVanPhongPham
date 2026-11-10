package com.quanlykhov.dao;

import com.quanlykhov.model.Employee;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
  public interface EmployeeRepository extends JpaRepository<Employee, String>
  {
    List<Employee> findByFullNameContainingIgnoreCase (String keyword); //Tìm nhân viên theo tên
    Employee findByPhone (String phone);//Tìm nhân viên theo sdt
  }
