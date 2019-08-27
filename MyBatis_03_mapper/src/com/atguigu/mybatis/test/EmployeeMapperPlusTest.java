package com.atguigu.mybatis.test;

import com.atguigu.mybatis.bean.Employee;
import com.atguigu.mybatis.dao.EmployeeMapperPlus;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class EmployeeMapperPlusTest {

    public SqlSessionFactory getSqlSessionFactory() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        return new SqlSessionFactoryBuilder().build(inputStream);
    }


    /**
     * 查询Employee的同时查询员工对应的部门
     */
    @Test
    public void test13() throws IOException{
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try{
            EmployeeMapperPlus mapper = openSession.getMapper(EmployeeMapperPlus.class);
		/*	Employee empById = mapper.getEmpById(1);
			System.out.println("===getEmpById===");
			System.out.println(empById);

			Employee empAndDept01 = mapper.getEmpAndDept01(1);
			System.out.println("===(1)getEmpAndDept01===");
			System.out.println(empAndDept01);
			System.out.println(empAndDept01.getDept());

			Employee empAndDept02 = mapper.getEmpAndDept02(1);
			System.out.println("===(2)getEmpAndDept02===");
			System.out.println(empAndDept02);
			System.out.println(empAndDept02.getDept());*/

            Employee employee = mapper.getEmpByIdStep(3);
            System.out.println("===(3)getEmpByIdStep===");
            System.out.println(employee.getLastName());
            //如果配置了懒加载employee.getLastName()时不会去查部门信息
//			System.out.println(employee.getDept());
        }finally{
            openSession.close();
        }
    }

    /**
     * 将部门对应的所有员工信息也查询出来
     */
    @Test
    public void test14() throws IOException{
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try{
            EmployeeMapperPlus mapper = openSession.getMapper(EmployeeMapperPlus.class);
            List<Employee> list = mapper.getEmployeeListByDepartmentId(1);
            System.out.println(list);
        }finally{
            openSession.close();
        }
    }

    /**
     * 查询Employee,鉴别器,根据某列的值改变封装行为
     */
    @Test
    public void testGetEmpByIdMyEmpDis() throws IOException{
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try{
            EmployeeMapperPlus mapper = openSession.getMapper(EmployeeMapperPlus.class);
            Employee employee = mapper.getEmpByIdMyEmpDis(1);
            System.out.println(employee);
        }finally{
            openSession.close();
        }
    }
}
