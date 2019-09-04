package com.atguigu.mybatis.controller;

import java.awt.*;
import java.util.List;
import java.util.Map;

import com.atguigu.mybatis.bean.NameAndVal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.atguigu.mybatis.bean.Employee;
import com.atguigu.mybatis.service.EmployeeService;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	EmployeeService employeeService;
	
	@RequestMapping("/getemps")
	public String getemps(Map<String,Object> map){
		long startTime = System.currentTimeMillis();
		List<Employee> emps = employeeService.getEmps();
		long endTime = System.currentTimeMillis();
		System.out.println("getemps:"+ (endTime - startTime) + "ms");
		map.put("allEmps", emps.subList(0,1));
		return "list";
	}

	@RequestMapping(value = "/getNameAndVal")
	public String getNameAndVal(){
		long startTime = System.currentTimeMillis();
		List<NameAndVal> nameAndValList = employeeService.getNameAndValue();
		long endTime = System.currentTimeMillis();
		System.out.println("getNameAndVal:"+ (endTime - startTime) + "ms");
		return "list";
	}

}
