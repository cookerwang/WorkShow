###一、Dagger2优点：
1. 依赖的注入和配置独立于组件之外，注入的对象在一个独立、不耦合的地方初始化，这样在改变注入对象时，我们只需要修改对象的实现方法，而不用大改代码库。
2. 依赖可以注入到一个组件中：我们可以注入这些依赖的模拟实现，这样使得测试更加简单。
3. app中的组件不需要知道有关实例创建和生命周期的任何事情，这些由我们的依赖注入框架管理的。

####android studio配置
根目录的build.gradle添加路径

	classpath 'com.neenbedankt.gradle.plugins:android-apt:1.4+'	

模块目录的build.gradle中应用插件、添加依赖

	apply plugin: "android-apt"	
	def Dagger2_Version = '2.0.2';
	dependencies {
	    // ....
	    compile "com.google.dagger:dagger:$Dagger2_Version"
	    apt "com.google.dagger:dagger-compiler:$Dagger2_Version"	
		compile 'org.glassfish:javax.annotation:10.0-b28' 
		// Dagger 2 中会用到@Generated注解（后面讲），而javax.anotation.generated在java 6及以上的版本中都有，在Android API 中是没有
	}

###android-apt理解
The android-apt plugin assists in working with annotation processors in combination with Android Studio. It has two purposes:  
1. Allow to configure a compile time only annotation processor as a dependency, not including the artifact in the final APK or library  
2. Set up the source paths so that code that is generated from the annotation processor is correctly picked up by Android Studio.

###Dagger关键注解
1. @Inject：注解构造方法、成员变量
2. @Provides：提供依赖的方法，注解provideXX方法，与@Inject协作完成依赖关系，注意入参与出参不能同一个类
2. @Module：标识类型为module,提供被@Provides、[@Singleton]等注解的provideXX(...)方法
3. @Component：连接提供依赖和消费依赖对象的组件被称为Injector。关联@Inject、@Provider，建议通过它来获取实例。一个Component可以是另一个Component的依赖。
4. @Scope注释
###dagger知识
1. Dagger通过@Module中的@Provides方法调用构造函数来获得实例对象。如果你@Inject fields却没有@Inject构造函数，Dagger就会使用一个存在的无参构造函数，若没有@Inject构造函数，就会出错。
2. @Module注解的类，Dagger要求所有的@Provides必须属于一个Module（即@Module注解的类）。  
@Inject实现注入，@Provides实现依赖关系。@Provides方法方法的返回类型就定义了它所满足的依赖。
3. Dagger 1.x中，@Inject和@Provides annotation 构成了对象图谱（graph），依靠之间的依赖关系而链接在一起。通过定义好的图谱集（ObjectGraph）可以方便的调用代码。而在Dagger 2中，这种关系被带有无参方法的接口代替，这种方法返回的类型就是所需类型。这种接口的实现是通过@Component 注解且传入modules参数来定义的。如：

	@Component(  
		// dependencies = ApplicationComponent.class,  
	    modules = ActivityModule.class
		/*假设改module中提供
		Heater provideHeater(ElectricHeater heater) {
			return heater;
		}
		*/   
	)  
	public interface ActivityComponent {  
	    void injectActivity(MainActivity activity);	  
	    ToastHelper getToastHelper();  
	}  

	public class MainActivity extends Activity {
		@Inject Heater heater;
		public void onCreate(Bundle b) {
			super.onCreate(b);
			setContentView(R.layout.main_activity);
			ActivityComponent component = Dagger_ActivityComponent.builder()
			//.applicationComponent(((DaggerApplication) getApplication())
			//.getComponent())  
            //.activityModule(new ActivityModule(this))  
            .build();
			ToastHelper toastHelper =  component.getToastHelper();
			// 或者
			// 完成MainActivity中被@Inject注解的成员变量的实例化，调用
			component.injectActivity(this);
		}
		
	}
在编译时，Dagger 2会自动生成以Dagger_为前缀的此接口的实现Dagger_AcitvityComponent.通过调用此的builder()方法来获得一个实例，通过该方法返回的builder来设置其他依赖，通过build来获得一个新的实例。  	 
如果@Component注解的接口中的方法没有参数，生成的实例中会生成一个create()方法，此create()方法实际上就是
builder().build();
4. Dagger 2 消除了Dagger 1.x 中所有的映射（reflection），通过添加@Component，移除ObjectGraph／Injector使代码更加的清晰。

###二、MVP
1. 传统MVP: M层处理业务逻辑，P层仅仅是V和M的桥梁。
2. 另类MVP：P层同时处理与model相关的业务逻辑，不处理View层次的逻辑，View层次的逻辑交给V自己处理，M层仅仅是bean。

###三、dagger结构图
####mvp结构图  
<img src="./images/dagger_structure.jpg"/>

####1. AppComponent: 
生命周期跟Application一样的组件。可注入到自定义的Application类中，@Singletion代表各个注入对象为单例。

	@Singleton
	@Component(modules = AppModule.class)
	public interface AppComponent {
	    Context context();  // 提供Applicaiton的Context	
	    ThreadExecutor threadExecutor();   // 线程池	
	    ApiService apiService();  // 所有Api请求的管理类	
	    SpfManager spfManager();  // SharedPreference管理类	
	    DBManager dbManager();  // 数据库管理类
	}
####2. AppModule: 
这里提供了AppComponent里的需要注入的对象。

	@Module
	public class AppModule {
	    private final MyApplication application;	
	    public AppModule(MyApplication application) {
	        this.application = application;
	    }
	
	    @Provides
	    @Singleton
	    Context provideApplicationContext() { 
	       return application;
	    }
	
	    @Provides
	    @Singleton
	    ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
	        return jobExecutor;
	    }
	
	    @Provides
	    @Singleton
	    ApiService providesApiService(RetrofitManager retrofitManager) {
	        return retrofitManager.getService();
	    }
	
	    @Provides
	    @Singleton
	    SpfManager provideSpfManager() {
	        return new SpfManager(application);
	    }
	
	    @Provides
	    @Singleton
	    DBManager provideDBManager() {
	        return new DBManager(application);
	    }
		/*
		@Provides
	    @Singleton
		DBManager provideDBManager(DBManager dbManager) {
		  return dbManager;
		}
		*/
		// 入参和出参类型相同，编译会报一个循环依赖的错误，因此直接返回参数时需要不是同一个类。
	}
	

	// 直接返回的类JobExecutor、RetrofitManager，它们类的构造函数一定要加上@Inject的注解：
	class JobExecutor {
		@Inject
		public JobExecutor() {
		    // 初始化
		    // ......
		}
		// ...
	}

####3. ActivityComponent
@ActivityScope注解是自定义的，对应Activity的生命周期，Dagger2可以通过自定义注解限定注解作用域，一般在Module里规定scope的生命周期，比如下面的ActivityScope在ActivityModule里绑定。

ActivityComponent：生命周期跟Activity一样的组件，这里提供了inject方法将Activity注入到ActivityComponent中，通过该方法，将Activity中需要注入的对象注入到该Activity中。
	@Scope
	@Retention(RUNTIME)
	public @interface ActivityScope {
	}
	
	
	@ActivityScope
	@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
	public interface ActivityComponent {
	    Activity activity();	
	    void inject(LoginActivity loginActivity);	
	    void inject(MainActivity mainActivity);	
	    // ....
	}

####4. ActivityModule
注入Activity，同时规定Activity所对应的域是@ActivityScope

	@Module
	public class ActivityModule {
	    private final Activity activity;
	
	    public ActivityModule(Activity activity) {
	        this.activity = activity;
	    }
	
	    @Provides
	    @ActivityScope
	    Activity activity() {
	        return this.activity;
	    }
	}

####5. dagger推荐结构图
<img src="./images/dagger_g__structure.jpg"/>

对于不同的Activity，创建各个对应的ActivityCompontent，同时把Presenter(Biz)注入到Component的视图中，这也是dagger2推荐的做法，Dagger 2希望使用@Component注解接口将依赖关系链接起来。

####6.注入
此处没有把Presenter注入到ActivityComponent中，因为Presenter的作用域和Activity一样，好处是节省代码（－ －），大家可以自行选择注入方式。


	public class LoginActivity extends BaseActivity implements LoginView, ValidCodeView {
	    @Inject LoginPresenter loginPresenter;	
	    @Inject ValidCodePresenter validCodePresenter;
	
	   @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_login);
	
	        initInject();
	        // 此处省略N行代码
	    }
	
	    private void initInject() {
	        // 构建Component并注入
	        getActivityComponent().inject(this);
	        loginPresenter.attachView(this);
	        validCodePresenter.attachView(this);
	    }
	
	    //  建议写在基类Activity里
	    protect ActivityComponent getActivityComponent(){
	          return  DaggerActivityComponent.builder()
	                      .appComponent(getAppComponent())
	                      .activityModule(getActivityModule())
	                      .build();
	    }
	
	    //  建议写在基类Activity里
	    protect ActivityModule getActivityModule(){
	          return new ActivityModule(this);
	    }
	
	    // 建议写在MyApplication类里
	    public AppComponent getAppComponent(){
	         return DaggerAppComponent.builder()
	         .appModule(new AppModule((MyApplication)getApplicationContext()))
	         .build();
	    }
	}

	@ActivityScope
	public class LoginPresenter extends DefaultMvpPresenter<LoginView, RESTResult<UserVO>> {
	     // 此处省略
	
	    @Inject
	    public LoginPresenter(ApiService apiService, ThreadExecutor jobExecutor, SpfManager spfManager) {
	        this.apiService = apiService;
	        this.jobExecutor = jobExecutor;
	        this.spfManager = spfManager;
	    }
	    public void login(String mobile, String code) {
	          // todo
	    }
	}

###参考
1. [Dagger 2 官网及例子](https://github.com/google/dagger)
2. [Android 依赖注入： Dagger 2 实例讲解（一）](http://blog.csdn.net/zjbpku/article/details/42109891)
3. [详解dagger 2](http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0519/2892.html)
4. [dagger2 使用](http://www.jianshu.com/p/c2feb21064bb)
5. [使用Dagger 2进行依赖注入](http://codethink.me/2015/08/06/dependency-injection-with-dagger-2/?utm_source=tuicool&utm_medium=referral)