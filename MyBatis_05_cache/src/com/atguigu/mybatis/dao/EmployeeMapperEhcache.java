package com.atguigu.mybatis.dao;

import com.atguigu.mybatis.bean.Employee;

public interface EmployeeMapperEhcache {
    public Employee getEmpById(Integer id);

    public Long addEmp(Employee employee);
}
