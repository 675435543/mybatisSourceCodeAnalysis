package com.atguigu.mybatis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.atguigu.mybatis.bean.Employee;

public interface EmployeeMapperDynamicSQL {

	//携带了哪个字段查询条件就带上这个字段的值
	public List<Employee> getEmpsByConditionIf(Employee employee);

	public List<Employee> getEmpsByConditionWhere(Employee employee);

	public List<Employee> getEmpsByConditionTrim1(Employee employee);

	public List<Employee> getEmpsByConditionTrim2(Employee employee);

	public List<Employee> getEmpsByConditionChoose(Employee employee);

	public void updateEmpSet(Employee employee);

	public void updateEmpTrimSet(Employee employee);

	//查询员工id'在给定集合中的
	public List<Employee> getEmpsByConditionForeach(List<Integer> ids);

	public void addEmps01(@Param("emps")List<Employee> emps);

	public void addEmps02(@Param("emps")List<Employee> emps);

	public List<Employee> getEmpsTestInnerParameterSingle(String lastName);

	public List<Employee> getEmpsTestInnerParameterMuti(Employee employee);

}
