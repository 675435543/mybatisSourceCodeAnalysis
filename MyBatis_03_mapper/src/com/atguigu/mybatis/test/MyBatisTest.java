package com.atguigu.mybatis.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import com.atguigu.mybatis.bean.Department;
import com.atguigu.mybatis.bean.Employee;
import com.atguigu.mybatis.dao.DepartmentMapper;
import com.atguigu.mybatis.dao.EmployeeMapper;
import com.atguigu.mybatis.dao.EmployeeMapperAnnotation;
import com.atguigu.mybatis.dao.EmployeeMapperPlus;


public class MyBatisTest {

	public SqlSessionFactory getSqlSessionFactory() throws IOException {
		String resource = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		return new SqlSessionFactoryBuilder().build(inputStream);
	}

	@Test
	public void test01() throws IOException {
/**
 1，根据xml配置文件（全局配置文件）得到SqlSessionFactory对象；
 2，使用SqlSessionFactory，得到sqlSession对象。能直接执行已经映射的sql语句
 用它来执行增删改查 。一个sqlSession就是代表和数据库的一次会话，用完关闭。SqlSession和connection一样她都是非线程安全。每次使用都应该去获取新的对象。
 3，使用sql的唯一标志来告诉MyBatis执行哪个sql。sql都是保存在sql映射文件中的。
 **/
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession openSession = sqlSessionFactory.openSession();
		try {
			Employee employee = openSession.selectOne(
					"com.atguigu.mybatis.dao.EmployeeMapper.selectEmp", 1);
			System.out.println(employee);
		} finally {
			openSession.close();
		}
	}

	@Test
	public void test02() throws IOException {
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession openSession = sqlSessionFactory.openSession();
		try {
/**
 3，获取接口的实现类对象。会为接口自动的创建一个代理对象，代理对象去执行增删改查方法
 **/
			EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
			Employee employee = mapper.getEmpById(1);
			System.out.println(mapper.getClass());
			System.out.println(employee);
		} finally {
			openSession.close();
		}

	}
	
	@Test
	public void test03() throws IOException{
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession openSession = sqlSessionFactory.openSession();
		try{
			EmployeeMapperAnnotation mapper = openSession.getMapper(EmployeeMapperAnnotation.class);
			Employee empById = mapper.getEmpById(1);
			System.out.println(empById);
		}finally{
			openSession.close();
		}
	}
	
	/**
	 增删改只支持以下返回值类型,并且可定义以下任意一种返回值类型：
	 		Integer、Long、Boolean、void
	 提交数据的方式
	 	1,手动提交==》sqlSessionFactory.openSession();
	 	2,自动提交==》sqlSessionFactory.openSession(true);
	 */
	@Test
	public void test04() throws IOException{
		
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		//这种方式获取SqlSession，需要手动提交数据
		SqlSession openSession = sqlSessionFactory.openSession();
		
		try{
			EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
			//测试添加
			Employee employee = new Employee(null, "jerry4",null, "1");
			Long res = mapper.addEmp(employee);
			System.out.println(employee.getId());
			System.out.println(res);
			//测试修改
/*			Employee employee = new Employee(3, "Tom", "jerry@atguigu.com", "0");
			boolean updateEmp = mapper.updateEmp(employee);
			System.out.println(updateEmp);*/

			//测试删除
/*			mapper.deleteEmpById(7);*/
			//手动提交数据
			openSession.commit();
		}finally{
			openSession.close();
		}
		
	}

	/**
	 * 多个参数
	 */
	@Test
	public void test05() throws IOException{
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession openSession = sqlSessionFactory.openSession();
		try{
			EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
			Employee employee = mapper.getEmpByIdAndLastNameDefault(1, "javahiker");
			System.out.println(employee);
		}finally{
			openSession.close();
		}
	}

	/**
	 * 参数传map
	 */
	@Test
	public void test06() throws IOException{
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession openSession = sqlSessionFactory.openSession();
		try{
			EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
			Map<String, Object> map = new HashMap<>();
			map.put("id", 3);
			map.put("lastName", "Tom");
			map.put("tableName", "tbl_employee");
			Employee employee = mapper.getEmpByMap(map);
			System.out.println(employee);
		}finally{
			openSession.close();
		}
	}

	/**
	 * 命名参数
	 */
	@Test
	public void test07() throws IOException{
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession openSession = sqlSessionFactory.openSession();
		try{
			EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
			Employee employee = mapper.getEmpByIdAndLastName(1,"javahiker");
			System.out.println(employee);
		}finally{
			openSession.close();
		}
	}

	/**
	 * POJO
	 */
	@Test
	public void test08() throws IOException{
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession openSession = sqlSessionFactory.openSession();
		try{
			EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
			Employee employeeParam = new Employee();
			employeeParam.setId(1);
			employeeParam.setLastName("javahiker");
			Employee employee = mapper.getEmpByPojo(employeeParam);
			System.out.println(employee);
		}finally{
			openSession.close();
		}
	}

	/**
	 Collection  List  数组
 	 key值：Collection==> collection/list
	 		List==>collection/list
	 		Array==>array
	 */
	@Test
	public void test09() throws IOException{
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession openSession = sqlSessionFactory.openSession();
		try{
			EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
			Collection collection = new ArrayList();
			collection.add(1);
			collection.add(3);
			List<Integer> list = new ArrayList<>();
			list.add(1);
			list.add(3);
			Set<Integer>set = new HashSet<>();
			set.add(1);
			set.add(3);
			Integer [] array = new Integer[]{1,3};
			Employee employee = mapper.getEmpByList(list);
			System.out.println(employee);
		}finally{
			openSession.close();
		}
	}

	/**
	 * 返回值是一个集合，要写集合中元素的类型
	 */
	@Test
	public void test10() throws IOException{
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession openSession = sqlSessionFactory.openSession();
		try{
			EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
			List<Employee> like = mapper.getEmpsByLastNameLike("%e%");
			for (Employee employee : like) {
				System.out.println(employee);
			}
		}finally{
			openSession.close();
		}
	}

	/**
	 * 返回一条记录的map；key就是列名，值就是对应的值
	 */
	@Test
	public void test11() throws IOException{
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession openSession = sqlSessionFactory.openSession();
		try{
			EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
			Map<String, Object> map = mapper.getEmpByIdReturnMap(1);
			System.out.println(map);
		}finally{
			openSession.close();
		}
	}

	/**
	 *返回一个map；key是指定的列名，值是封装后的javaBean
	 */
	@Test
	public void test12() throws IOException{
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession openSession = sqlSessionFactory.openSession();
		try{
			EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
			Map<String, Employee> map = mapper.getEmpByLastNameLikeReturnMap("%r%");
			System.out.println(map);
		}finally{
			openSession.close();
		}
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

	/**
	 * 根据部门id，查询部门信息。与根据员工id，查员工信息类似
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
