###单元测试
一、添加依赖（junit与断言库)

	testCompile 'junit:junit:4.12'
    compile 'org.assertj:assertj-core:3.3.0'

二、创建测试代码
1. 创建src/test/java目录
2. 新建测试代码XXTest.java或TestXX.java，测试方法名以test_起头，并采用@Test注解
3. 测试，运行的图标处，edit configuration编辑，junit下添加即可

三、junit 4.0知识
1.  知识要点：    

    如果一个测试类里有8个测试方法，那么每个测试方法都需要创建一个测试类对象，每一个对象只会调用一个测试方法（为了符合命令模式的约定），总共创建了10个测试类对象， 
	在添加测试方法之前，需要对测试方法做一些判断：
	1）修饰符设为 public
	2）返回类型 void
	3）没有方法参数
	4）方法名称必须以test开头

2. 注解

	@Test：	表明该方法是一个测试方法
	 
	@BeforeClass 和 @AfterClass：
	测试用例初始化时执行 @BeforeClass方法，当所有测试执行完毕之后，执行@AfterClass进行收尾工作。标注、@BeforeClass 和 @AfterClass的方法必须是static的，因为方法将在类被装载的时候就被调用，那时候还没创建测试对象实例。
	 
	@Before：使用了该元数据的方法在每个测试方法执行之前都要执行一次。
	@After： 使用了该元数据的方法在每个测试方法执行之后要执行一次。
	 
	@Test(expected=*.class) ：
	通过@Test元数据中的expected属性验证是否抛出期望的异常，expected属性的值是一个异常的类型，如果抛出了期望的异常，则测试通过，否则不通过。
	 
	@Test(timeout=xxx)：
	该元数据传入了一个时间（毫秒）给测试方法，如果测试方法在制定的时间之内没有运行完，则测试也失败。
	 
	@Ignore： 
	该元数据标记的测试方法在测试中会被忽略。同时可以为该标签传递一个String的参数，来表明为什么会忽略这个测试方法。比如：@lgnore("该方法还没有实现")，在执行的时候，仅会报告该方法没有实现，而不会运行测试方法。
	 
	在test方法内除了使用Assert的assertEquals()方法外，还能使用assertFalse()、assertTrue()、assertNull()、assertNotNull()、assertSame()、assertNotSame()等断言函数。而且如果使用的是Junit4，结合Hamcrest，使用
	assertThat([value], [matcher statement])方法可以实现更灵活的断言判断（前提是引入hamcrest的jar包）。
	例如：
	// is匹配符表明如果前面待测的object等于后面给出的object，则测试通过 
	assertThat( testedObj, is( object) ); 
	 
	// containsString匹配符表明如果测试的字符串包含指定的子字符串则测试通过
	assertThat( testedString, containsString( "developerWorks" ) );
	 
	// greaterThan匹配符表明如果所测试的数值testedNumber大于16.0则测试通过
	assertThat( testedNumber, greaterThan(16.0) ); 
	 
	// closeTo匹配符表明如果所测试的浮点型数testedDouble在20.0±0.5范围之内则测试通过 
	assertThat( testedDouble, closeTo( 20.0, 0.5 ) );
	 
	//hasItem匹配符表明被测的迭代对象含有元素element项则测试通过assertThat(iterableObject, hasItem (element));


###Table 1. Annotations

######@Test public void method():	  
Annotation @Test identifies that this method is a test method.

######@Before public void method():
Will perform the method() before each test. This method can prepare the test environment, e.g. read input data, initialize the class)

######@After public void method():	Test method must start with test
	
######@BeforeClass public void method()	
Will perform the method before the start of all tests. This can be used to perform time intensive activities for example be used to connect to a database

######@AfterClass public void method()	
Will perform the method after all tests have finished. This can be used to perform clean-up activities for example be used to disconnect to a database
	
######@Ignore	
Will ignore the test method, e.g. useful if the underlying code has been changed and the test has not yet been adapted or if the runtime of this test is just to long to be included.
	
######@Test(expected=IllegalArgumentException.class)	
Tests if the method throws the named exception

######@Test(timeout=100)	
Fails if the method takes longer then 100 milliseconds

####Table 2. Test methods
######fail(String)	
Let the method fail, might be usable to check that a certain part of the code is not reached.

######assertTrue(true);	True

######assertsEquals([String message], expected, actual)	
Test if the values are the same. Note: for arrays the reference is checked not the content of the arrays

######assertsEquals([String message], expected, actual, tolerance)	Usage for float and double; the tolerance are the number of decimals which must be the same

######assertNull([message], object)	Checks if the object is null

######assertNotNull([message], object)	Check if the object is not null

######assertSame([String], expected, actual)	Check if both variables refer to the same object

######assertNotSame([String], expected, actual)	Check that both variables refer not to the same object

######assertTrue([message], boolean condition)	Check if the boolean condition is true.