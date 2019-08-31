package com.atguigu.mybatis.test;

import com.atguigu.mybatis.bean.Department;
import com.atguigu.mybatis.bean.Employee;
import com.atguigu.mybatis.dao.EmployeeMapperDynamicSQL;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EmployeeMapperDynamicSQLTest {
    public SqlSessionFactory getSqlSessionFactory() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        return new SqlSessionFactoryBuilder().build(inputStream);
    }


    /**
     * if where  trim
     * Choose when otherwise
     */
    @Test
    public void testDynamicSql() throws IOException{
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try{
            EmployeeMapperDynamicSQL mapper = openSession.getMapper(EmployeeMapperDynamicSQL.class);
            //测试if where
            Employee employee = new Employee(1, "java%", null, "1");
            List<Employee> emps = mapper.getEmpsByConditionIf(employee );
            for (Employee emp : emps) {
                System.out.println(emp);
            }
            System.out.println("---------getEmpsByConditionIf-----------");
            Employee employeeWhere = new Employee(5, "jer%", null, "");
            List<Employee> empsWhere = mapper.getEmpsByConditionWhere(employeeWhere);
            for (Employee emp : empsWhere) {
                System.out.println(emp);
            }
            System.out.println("---------getEmpsByConditionWhere-----------");

            //测试Trim
            List<Employee> empsTrim1 = mapper.getEmpsByConditionTrim1(employee);
            for (Employee emp : empsTrim1) {
                System.out.println(emp);
            }
            System.out.println("---------getEmpsByConditionTrim1-----------");
            List<Employee> empsTrim2 = mapper.getEmpsByConditionTrim2(employee);
            for (Employee emp : empsTrim2) {
                System.out.println(emp);
            }
            System.out.println("---------getEmpsByConditionTrim2-----------");

            //测试choose
            List<Employee> list = mapper.getEmpsByConditionChoose(employee);
            for (Employee emp : list) {
                System.out.println(emp);
            }
            System.out.println("---------getEmpsByConditionChoose-----------");
        }finally{
            openSession.close();
        }
    }


    /**
     * 测试set标签
     */
    @Test
    public void testUpdateSet() throws IOException{
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try{
            EmployeeMapperDynamicSQL mapper = openSession.getMapper(EmployeeMapperDynamicSQL.class);
            Employee employee1 = new Employee(5, "java%", null, null);
            mapper.updateEmpSet(employee1);

            Employee employee2 = new Employee(6, "java%", null, null);
            mapper.updateEmpTrimSet(employee2);
            openSession.commit();
        }finally{
            openSession.close();
        }
    }

    /**
     * foreach
     */
    @Test
    public void testForeach() throws IOException{
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try{
            EmployeeMapperDynamicSQL mapper = openSession.getMapper(EmployeeMapperDynamicSQL.class);
            List<Employee> list = mapper.getEmpsByConditionForeach(Arrays.asList(1,2));
            for (Employee emp : list) {
                System.out.println(emp);
            }
        }finally{
            openSession.close();
        }
    }

    /**
     * 批量保存
     */
    @Test
    public void testBatchSave01() throws IOException{
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try{
            EmployeeMapperDynamicSQL mapper = openSession.getMapper(EmployeeMapperDynamicSQL.class);
            List<Employee> emps = new ArrayList<>();
            emps.add(new Employee(null, "smith0x1", "smith0x1@atguigu.com", "1",new Department(1)));
            emps.add(new Employee(null, "allen0x1", "allen0x1@atguigu.com", "0",new Department(1)));
            mapper.addEmps01(emps);
            openSession.commit();
        }finally{
            openSession.close();
        }
    }

    /**
     * 批量保存,需要数据库连接属性
     */
    @Test
    public void testBatchSave02() throws IOException{
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try{
            EmployeeMapperDynamicSQL mapper = openSession.getMapper(EmployeeMapperDynamicSQL.class);
            List<Employee> emps = new ArrayList<>();
            emps.add(new Employee(null, "smith0x1", "smith0x1@atguigu.com", "1",new Department(1)));
            emps.add(new Employee(null, "allen0x1", "allen0x1@atguigu.com", "0",new Department(1)));
            mapper.addEmps02(emps);
            openSession.commit();
        }finally{
            openSession.close();
        }
    }

    @Test
    public void testInnerParamSingle() throws IOException{
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try{
            EmployeeMapperDynamicSQL mapper = openSession.getMapper(EmployeeMapperDynamicSQL.class);
            String lastName = "%e%";
            List<Employee> list = mapper.getEmpsTestInnerParameterSingle(lastName);
            for (Employee employee : list) {
                System.out.println(employee);
            }
        }finally{
            openSession.close();
        }
    }

    @Test
    public void testInnerParamMuti() throws IOException{
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try{
            EmployeeMapperDynamicSQL mapper = openSession.getMapper(EmployeeMapperDynamicSQL.class);
            Employee employee2 = new Employee();
            employee2.setLastName("e");
            List<Employee> list = mapper.getEmpsTestInnerParameterMuti(employee2);
            for (Employee employee : list) {
                System.out.println(employee);
            }
        }finally{
            openSession.close();
        }
    }
}
