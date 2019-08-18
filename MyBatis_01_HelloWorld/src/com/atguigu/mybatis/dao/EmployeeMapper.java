package com.atguigu.mybatis.dao;

import com.atguigu.mybatis.bean.Employee;

public interface EmployeeMapper {
	public Employee selectEmp(Integer id);
	public Employee getEmpById(Integer id);
}
