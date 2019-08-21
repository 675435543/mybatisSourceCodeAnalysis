package com.atguigu.mybatis.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.atguigu.mybatis.bean.Employee;

public interface EmployeeMapper {

	public Employee selectEmp(Integer id);

	public Employee getEmpById(Integer id);

	public Long addEmp(Employee employee);

	public boolean updateEmp(Employee employee);

	public void deleteEmpById(Integer id);

	public Employee getEmpByIdAndLastNameDefault(Integer id, String lastName);

	public Employee getEmpByMap(Map<String, Object> map);

	public Employee getEmpByIdAndLastName(@Param("id")Integer id,@Param("lastName")String lastName);

	public Employee getEmpByPojo(Employee employee);

	public Employee getEmpByList(List<Integer> list );

	public List<Employee> getEmpsByLastNameLike(String lastName);

	public Map<String, Object> getEmpByIdReturnMap(Integer id);

	@MapKey("lastName")
	public Map<String, Employee> getEmpByLastNameLikeReturnMap(String lastName);

}
