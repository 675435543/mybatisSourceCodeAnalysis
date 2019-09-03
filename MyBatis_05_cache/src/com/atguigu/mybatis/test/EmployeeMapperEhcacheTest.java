package com.atguigu.mybatis.test;

import com.atguigu.mybatis.bean.Employee;
import com.atguigu.mybatis.dao.EmployeeMapperEhcache;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class EmployeeMapperEhcacheTest {
    public SqlSessionFactory getSqlSessionFactory() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        return new SqlSessionFactoryBuilder().build(inputStream);
    }


    /**
     *第三方缓存整合：
     *		1）、导入第三方缓存包即可；
     *		2）、导入与第三方缓存整合的适配包；官方有；
     *		3）、mapper.xml中使用自定义缓存
     *		<cache type="org.mybatis.caches.ehcache.EhcacheCache"></cache>
     *
     *引用缓存：namespace：指定和哪个名称空间下的缓存一样
     *      <cache-ref namespace="com.atguigu.mybatis.dao.EmployeeMapperCache"/>
     *
     *
     */

    /**
     * EmployeeMapperEhcache
     */
    @Test
    public void testSecondLevelCacheEhcache() throws IOException{
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession1 = sqlSessionFactory.openSession();
        SqlSession openSession2 = sqlSessionFactory.openSession();
        try{
            EmployeeMapperEhcache mapper1 = openSession1.getMapper(EmployeeMapperEhcache.class);
            EmployeeMapperEhcache mapper2 = openSession2.getMapper(EmployeeMapperEhcache.class);

            Employee emp01 = mapper1.getEmpById(1);
            System.out.println(emp01);
            openSession1.close();

            //mapper2.addEmp(new Employee(null, "aaa", "nnn", "0"));
            Employee emp02 = mapper2.getEmpById(1);
            System.out.println(emp02);
            System.out.println(emp01==emp02);
            openSession2.close();

        }finally{

        }
    }

}
