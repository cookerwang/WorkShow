##一、元注解(meta-annotation)：  
1. 作用：负责注解其他注解（例如自定义注解)  
2. 类型：@Target,@Retention,@Documented,@Inherited (java.lang.annotation包下)

###@Target:  
1. 作用: 描述注解使用范围，即使用地方
2. 取值:(ElementType.)  
   + CONSTRUCTOR: 用于描述构造器
   + FIELD: 用于描述域
   + LOCAL_VARIABLE:用于描述局部变量
   + METHOD:用于描述方法
   + PACKAGE:用于描述包
   + PARAMETER:用于描述参数
   + TYPE: 用于描述类、接口、注解或enum声明 

###@Retention：  
1. 作用：描述注解生命周期，即注解的有效范围
2. 取值:(RetentionPolicy.)  
   + source:源文件有效（保留）
   + class:class文件有效（保留）
   + runtime：运行时有效（保留）,配合反射进行逻辑处理  

###@Documented:
1. 作用：标注注解，将注解包含在javadoc中
2. 取值：无

###@Inherited:
1. 作用：允许子类继承父类的注解
2. 取值：无

##二、自定义注解
1. 作用：采用@interface自定义注解，自动继承java.lang.annotation.Annotation接口，由编译程序自动完成其他细节。在定义注解时，不能继承其他的注解或接口。@interface用来声明一个注解，其中的每一个方法实际上是声明了一个配置参数。方法的名称就是参数的名称，返回值类型就是参数的类型（返回值类型只能是基本类型、Class、String、enum）。可以通过default来声明参数的默认值。
2. 格式： public @interface 注解名 {定义体}
3. 注解参数可支持类型：
   + 所有基本数据类型（int,float,boolean,byte,double,char,long,short)
   + String、Class、Enum、Annotation
   + 以上类型的数组
4. 注解参数设定：
   + 只能用public或默认(default)这两个访问权修饰.例如,String value();这里把方法设为defaul默认类型；
   + 如果只有一个参数成员,最好把参数名称设为"value",后加小括号.
5. 注解元素默认值：注解元素必须有确定的值，要么在定义注解的默认值中指定，要么在使用注解时指定，非基本类型的注解元素的值不可为null。因此, 使用空字符串或0作为默认值是一种常用的做法。这个约束使得处理器很难表现一个元素的存在或缺失的状态，因为每个注解的声明中，所有元素都存在，并且都具有相应的值，为了绕开这个约束，我们只能定义一些特殊的值，例如空字符串或者负数，一次表示某个元素不存在，在定义注解时，这已经成为一个习惯用法。
5. 例子：  

----------

	@Retention(RetentionPolicy.SOURCE)
	@Target({ElementType.TYPE})
	public @interface AutoJson {
	    public @interface ToField {
	        String[] name() default {};
	
	        Class typeConverter() default void.class;
	    }
	
	    @Retention(RetentionPolicy.SOURCE)
	    @Target({ElementType.METHOD})
	    public @interface Field {
	        String[] name() default {};
	
	        Class typeConverter() default void.class;
	    }
	
	    @Retention(RetentionPolicy.SOURCE)
	    @Target({ElementType.METHOD})
	    public @interface Validate {
	    }
	
	    @Retention(RetentionPolicy.SOURCE)
	    @Target({ElementType.TYPE})
	    public @interface Builder {
	    }
	}

	@AutoJson
	public abstract class Action {
	    @Nullable
	    @AutoJson.Field
	    public abstract String name();
	    @Nullable
	    @AutoJson.Field
	    public abstract String link();
	
	    @AutoJson.Builder
	    public abstract static class Builder {
	        public abstract Builder name(String x);
	        public abstract Builder link(String x);
	
	        public abstract Action build();
	    }
	
	    public static Builder builder() {
	        return new AutoJson_Action.Builder(); // AutoJson_Action编译时期注解处理器自动生成
	    }
	}
##[思维导图]
<img src="./images/annotation.jpg"/>
