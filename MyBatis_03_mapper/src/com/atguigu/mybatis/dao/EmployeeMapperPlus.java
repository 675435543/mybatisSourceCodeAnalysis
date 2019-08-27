package com.atguigu.mybatis.dao;

import java.util.List;

import com.atguigu.mybatis.bean.Employee;

public interface EmployeeMapperPlus {
	
	public Employee getEmpById(Integer id);
	
	public Employee getEmpAndDept01(Integer id);

	public Employee getEmpAndDept02(Integer id);

	public Employee getEmpByIdStep(Integer id);
	
	public List<Employee> getEmployeeListByDepartmentId(Integer deptId);

}
