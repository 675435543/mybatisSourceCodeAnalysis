package com.atguigu.mybatis.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import com.atguigu.mybatis.dao.*;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import com.atguigu.mybatis.bean.Department;
import com.atguigu.mybatis.bean.Employee;


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
	 * 			false：关闭二级缓存，而一级缓存是一直可用的
	 * 		2）、每个select标签都有useCache="true"：
	 * 				false：不使用缓存（二级缓存不使用，而一级缓存依然使用）
	 * 		3）、【每个增删改标签的：flushCache="true"：（一级二级都会清除）】
	 * 				增删改执行完成后就会清楚缓存；
	 * 				测试：flushCache="true"：一级缓存就清空了；二级也会被清除；
	 * 				查询标签：flushCache="false"：
	 * 					如果flushCache=true;每次查询之后都会清空缓存；缓存是没有被使用的；
	 * 		4）、sqlSession.clearCache();只是清楚当前session的一级缓存；
	 * 		5）、localCacheScope：本地缓存作用域：（一级缓存SESSION）；当前会话的所有数据保存在会话缓存中；
	 * 								STATEMENT：可以禁用一级缓存；		
	 * 				
	 *第三方缓存整合：
	 *		1）、导入第三方缓存包即可；
	 *		2）、导入与第三方缓存整合的适配包；官方有；
	 *		3）、mapper.xml中使用自定义缓存
	 *		<cache type="org.mybatis.caches.ehcache.EhcacheCache"></cache>
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
	 * 第二次查询是从二级缓存中拿到的数据，并没有发送新的sql
	 */
	@Test
	public void testSecondLevelCache() throws IOException{
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession openSession1 = sqlSessionFactory.openSession();
		SqlSession openSession2 = sqlSessionFactory.openSession();
		try{
			EmployeeMapperCache mapper1 = openSession1.getMapper(EmployeeMapperCache.class);
			EmployeeMapperCache mapper2 = openSession2.getMapper(EmployeeMapperCache.class);

			Employee emp01 = mapper1.getEmpById(1);
			System.out.println(emp01);
			openSession1.close();

			//mapper2.addEmp(new Employee(null, "aaa", "nnn", "0"));
			Employee emp02 = mapper2.getEmpById(1);
			System.out.println(emp02);
			System.out.println(emp01==emp02);//一级缓存相同的话它们为true，二级缓存并没有这个规律
			openSession2.close();

		}finally{

		}
	}

	/**
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
			
			Department deptById = mapper1.getDeptById(1);
			System.out.println(deptById);
			openSession1.close();
			
			Department deptById2 = mapper2.getDeptById(1);
			System.out.println(deptById2);
			openSession2.close();

		}finally{
			
		}
	}


}
