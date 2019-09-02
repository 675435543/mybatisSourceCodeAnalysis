package com.atguigu.mybatis.test;

import com.atguigu.mybatis.bean.Department;
import com.atguigu.mybatis.bean.Employee;
import com.atguigu.mybatis.dao.DepartmentMapperCache;
import com.atguigu.mybatis.dao.EmployeeMapper;
import com.atguigu.mybatis.dao.EmployeeMapperCache;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class EmployeeMapperCacheTest {
    public SqlSessionFactory getSqlSessionFactory() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        return new SqlSessionFactoryBuilder().build(inputStream);
    }


    /**
     * 缓存：
     * 一级缓存：（本地缓存）：sqlSession级别的缓存。
     * 		一级缓存是一直开启的；SqlSession级别的一个Map
     * 		与数据库同一次会话期间查询到的数据会放在本地缓存中。
     * 		以后如果需要获取相同的数据，直接从缓存中拿，没必要再去查询数据库；
     *
     * 一级缓存失效情况（没有使用到当前一级缓存的情况，效果就是，需要再次向数据库发出查询）：
     * 		1、sqlSession不同。
     * 		2、sqlSession相同，查询条件不同.(当前一级缓存中还没有这个数据)
     * 		3、sqlSession相同，两次查询之间执行了增删改操作(这次增删改可能对当前数据有影响)
     * 		4、sqlSession相同，手动清除了一级缓存（缓存清空）
     *
     * 二级缓存：（全局缓存）：namespace级别的缓存：
     * 		一个namespace对应一个二级缓存：
     * 		工作机制：
     * 		1、一个会话，查询一条数据，这个数据就会被放在当前会话的一级缓存中；
     * 		2、如果会话关闭；一级缓存中的数据会被保存到二级缓存中；新的会话查询信息，就可以参照二级缓存中的内容；
     * 		3、sqlSession===EmployeeMapper==>Employee
     * 						DepartmentMapper===>Department
     * 			不同namespace查出的数据会放在自己对应的缓存中（map）
     * 			效果：数据会从二级缓存中获取
     * 				查出的数据都会被默认先放在一级缓存中。
     * 				只有会话提交或者关闭以后，一级缓存中的数据才会转移到二级缓存中
     * 二级缓存的使用：
     * 		1）、开启全局二级缓存配置：<setting name="cacheEnabled" value="true"/>
     * 		2）、去mapper.xml中配置使用二级缓存：
     * 			<cache></cache>
     * 		3）、我们的POJO需要实现序列化接口
     *
     * 和缓存有关的设置/属性：
     * 		1）、cacheEnabled=true  开启二级缓存
     * 			false：关闭二级缓存，而一级缓存依然开启
     * 		2）、useCache="true"：使用二级缓存
     * 			 useCache="false"：不使用二级缓存, 而一级缓存依然使用
     * 				也就是说，不管你前面怎么配置的，这里使不使用缓存是我说了算。
     * 			    查询标签有，增删改便签没有
     * 		3）、flushCache="true" 执行完成后就会清除缓存；一级二级都会清除
     * 			flushCache="false"：执行完成后就会清除缓存；一级二级不会被清除
     * 				对于查询标签 和 增删改便签都可设置
     * 			默认的配置（3.5.2版本的mybatis）
     * 			<select ... flushCache="false" useCache="true"/>
     * 			<insert ... flushCache="true"/>
     * 			<update ... flushCache="true"/>
     * 			<delete ... flushCache="true"/>
     * 		4）、sqlSession.clearCache();只是清除当前session的一级缓存；
     * 		5）、localCacheScope：SESSION | STATEMENT
     * 			本地缓存作用域：默认是SESSION，指一级缓存；当前会话的所有数据保存在会话缓存中；
     * 							STATEMENT：禁用一级缓存；
     * 缓存的顺序：
     * 		二级缓存；
     * 		一级缓存：
     * 		数据库
     *
     *
     */


    /**
     * sqlSession相同，查询条件也相同
     */
    @Test
    public void testFirstLevelCache01() throws IOException{
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try{
            EmployeeMapperCache mapper = openSession.getMapper(EmployeeMapperCache.class);
            Employee emp01 = mapper.getEmpById(1);
            System.out.println(emp01);

            Employee emp02 = mapper.getEmpById(1);
            System.out.println(emp02);
            System.out.println(emp01==emp02);

        }finally{
            openSession.close();
        }
    }


    /**
     *  缓存失效，1，sqlSession不同
     */
    @Test
    public void testFirstLevelCache02() throws IOException{
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession1 = sqlSessionFactory.openSession();
        SqlSession openSession2 = sqlSessionFactory.openSession();
        try{
            EmployeeMapperCache mapper = openSession1.getMapper(EmployeeMapperCache.class);
            Employee emp01 = mapper.getEmpById(1);
            System.out.println(emp01);

            //1、sqlSession不同。
            EmployeeMapper mapper2 = openSession2.getMapper(EmployeeMapper.class);
            Employee emp02 = mapper2.getEmpById(1);
            System.out.println(emp02);
            System.out.println(emp01==emp02);

        }finally{
            openSession1.close();
            openSession2.close();
        }
    }


    /**
     *  缓存失效，2、sqlSession相同，查询条件不同
     */
    @Test
    public void testFirstLevelCache03() throws IOException{
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try{
            EmployeeMapperCache mapper = openSession.getMapper(EmployeeMapperCache.class);
            Employee emp01 = mapper.getEmpById(1);
            System.out.println(emp01);

            //2、sqlSession相同，查询条件不同
            Employee emp02 = mapper.getEmpById(3);
            System.out.println(emp02);
            System.out.println(emp01==emp02);
        }finally{
            openSession.close();
        }
    }


    /**
     *  缓存失效，3、sqlSession相同，两次查询之间执行了增删改操作
     */
    @Test
    public void testFirstLevelCache04() throws IOException{
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try{
            EmployeeMapperCache mapper = openSession.getMapper(EmployeeMapperCache.class);
            Employee emp01 = mapper.getEmpById(1);
            System.out.println(emp01);

            //3、sqlSession相同，两次查询之间执行了增删改操作(这次增删改可能对当前数据有影响)
            mapper.addEmp(new Employee(null, "testCache", "cache", "1"));
            System.out.println("数据添加成功");
            Employee emp02 = mapper.getEmpById(1);
            System.out.println(emp02);
            System.out.println(emp01==emp02);
            openSession.commit();
        }finally{
            openSession.close();
        }
    }

    /**
     *  缓存失效，4、sqlSession相同，手动清除了一级缓存（缓存清空）
     */
    @Test
    public void testFirstLevelCache05() throws IOException{
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession = sqlSessionFactory.openSession();
        try{
            EmployeeMapperCache mapper = openSession.getMapper(EmployeeMapperCache.class);
            Employee emp01 = mapper.getEmpById(1);
            System.out.println(emp01);

            //4、sqlSession相同，手动清除了一级缓存（缓存清空）
            openSession.clearCache();

            Employee emp02 = mapper.getEmpById(1);
            System.out.println(emp02);
            System.out.println(emp01==emp02);
        }finally{
            openSession.close();
        }
    }

    /**
     * EmployeeMapperCache
     * 第二次查询是从二级缓存中拿到的数据，并没有发送新的sql
     */
    @Test
    public void testSecondLevelCache01() throws IOException{
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession1 = sqlSessionFactory.openSession();
        SqlSession openSession2 = sqlSessionFactory.openSession();
        try{
            EmployeeMapperCache mapper1 = openSession1.getMapper(EmployeeMapperCache.class);
            EmployeeMapperCache mapper2 = openSession2.getMapper(EmployeeMapperCache.class);

            Employee emp01 = mapper1.getEmpById(1);
            System.out.println(emp01);
            openSession1.close();
            Employee emp02 = mapper2.getEmpById(1);
            System.out.println(emp02);
            System.out.println(emp01==emp02);//一级缓存相同的话它们为true，二级缓存并没有这个规律
            openSession2.close();

        }finally{

        }
    }

    /**
     * DepartmentMapperCache
     * 第二次查询是从二级缓存中拿到的数据，并没有发送新的sql
     */
    @Test
    public void testSecondLevelCache02() throws IOException{
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession1 = sqlSessionFactory.openSession();
        SqlSession openSession2 = sqlSessionFactory.openSession();
        try{
            DepartmentMapperCache mapper1 = openSession1.getMapper(DepartmentMapperCache.class);
            DepartmentMapperCache mapper2 = openSession2.getMapper(DepartmentMapperCache.class);

            Department deptById1 = mapper1.getDeptById(1);
            System.out.println(deptById1);
            openSession1.close();

            Department deptById2 = mapper2.getDeptById(1);
            System.out.println(deptById2);
            System.out.println(deptById1==deptById2);
            openSession2.close();

        }finally{

        }
    }

    /**
     * EmployeeMapperCache
     * 增删改标签的：flushCache="true"：
     */
    @Test
    public void testSecondLevelCache03() throws IOException{
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession openSession1 = sqlSessionFactory.openSession();
        SqlSession openSession2 = sqlSessionFactory.openSession();
        try{
            EmployeeMapperCache mapper1 = openSession1.getMapper(EmployeeMapperCache.class);
            EmployeeMapperCache mapper2 = openSession2.getMapper(EmployeeMapperCache.class);

            Employee emp01 = mapper1.getEmpById(1);
            System.out.println(emp01);
            openSession1.close();

            mapper2.addEmp(new Employee(null, "aaa", "nnn", "0"));
            Employee emp02 = mapper2.getEmpById(1);
            System.out.println(emp02);
            System.out.println(emp01==emp02);
            openSession2.close();

        }finally{

        }
    }


}
