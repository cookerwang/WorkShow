###retrofit源码剖析
###一、核心技术：动态代理和反射。
其实Retrofit无非就是让用户创建接口，使用自己指定的规则进行网络访问，把接口传入Retrofit，接口上附着的规则由Retrofit进行层层解析后，再进行实际的网络调用。Retrofit所做的事情就是帮助用户简化了大量的网络访问代码，用户只需写少量代码就能得到想要的结果。

###二、动态代理理解
	import java.lang.reflect.InvocationHandler;
	import java.lang.reflect.Method;
	import java.lang.reflect.Proxy;
	public class DynamicProxyTest {
		interface IHello {
			public void sayHello();
		}
		static class Hello implements IHello {
			@Override
			public void sayHello() {
				System.out.println("Hello dynamic proxy");			
			}
		}
		static class DynamicProxy implements InvocationHandler {
			Object oriObj;
			
			Object bind(Object obj) {
				this.oriObj = obj;
				return Proxy.newProxyInstance(oriObj.getClass().getClassLoader(), 
						oriObj.getClass().getInterfaces(), this);
			}
			@Override
			public Object invoke(Object proxy, Method method, Object[] args)
					throws Throwable {
				System.out.println("wellcom to DynamicProxy.invaoke");			
				return method.invoke(oriObj, args);
			}
		}
		public static void main(String[] args) {
			IHello hello = (IHello)new DynamicProxy().bind(new Hello());
			hello.sayHello();
		}
	}

！
