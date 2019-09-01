package com.atguigu.mybatis.test;

import com.atguigu.mybatis.bean.Department;
import com.atguigu.mybatis.dao.DepartmentMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class DepartmentMapperTest {

    public SqlSessionFactory getSqlSessionFactory() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        return new SqlSessionFactoryBuilder().build(inputStream);
    }

    /**
     * 根据部门id，查部门信息。与根据员工id，查员工信息类似
     */
    @Test
    public void test15() throws IOException{
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try{
            DepartmentMapper mapper = openSession.getMapper(DepartmentMapper.class);
            Department department = mapper.getDeptById(2);
            System.out.println(department);
        }finally{
            openSession.close();
        }
    }


    /**
     * 根据部门id，查询部门信息。嵌套结果集的方式，将对应员工信息也查出来
     */
    @Test
    public void test16() throws IOException{
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try{
            DepartmentMapper mapper = openSession.getMapper(DepartmentMapper.class);
            Department department = mapper.getDeptByIdPlus(1);
            System.out.println(department);
            System.out.println(department.getEmps());
        }finally{
            openSession.close();
        }
    }

    /**
     * 根据部门id，查询部门信息。分段查询的方式，将对应员工信息也查出来
     */
    @Test
    public void test17() throws IOException{
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();

        try{
            DepartmentMapper mapper = openSession.getMapper(DepartmentMapper.class);
            Department deptByIdStep = mapper.getDeptByIdStep(1);
            System.out.println(deptByIdStep.getDepartmentName());
            System.out.println(deptByIdStep.getEmps());
        }finally{
            openSession.close();
        }
    }

}
