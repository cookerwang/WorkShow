###MVP设计模式
###MVC模式图  
<img src="../images/mvc.png"/>
###model2_mvc模式图  
<img src="../images/model2_mvc.png"/>  
###MVC
1. Input 被引导到 Controller.
2. Controller 决定渲染哪个 View, 并且生成 View 对应的 Model.
3. 一个 Controller 可以从很多个 View 当中选择一个渲染.
4. View 没有他的 Controller 的信息.
5. 业务逻辑存在于 Controller 当中.
6. 当多个用户请求之间(基于 HTTP, 无状态的协议), 状态不能被维护的情况下, MVC 是有用的.

###MVP模式图  
<img src="../images/mvp.png"/>
1. Input 被引导到 View.
2. 往往是在 View 抛出一个事件时, 作为响应, Presenter 对 View 进行更新.
4. State 被高效地存储在 View 当中.
5. 业务逻辑存在于 Presenter.

###MVVM模式图  
<img src="../images/mvvm.png"/>

1. Input 被引导到 View.
2. View 只知道 ViewModel, 不知道其他的信息.
3. ViewModel 只知道 Model, 不知道其他的信息.
4. View 从 ViewModel 获取数据, 而不是直接从 Model. 这通常通过数据绑定实现.
5. State 跟业务逻辑存在于 ViewModel.
6. ViewModel 可以被认为是 UI 的抽象表示.
7. State 可以在多个用户请求能被维护的情况下会很有用(比如 Silverlight, WPF 等).

###异同
1. MVP与MVC不同：MVP模式中不容许View直接访问Model，V、P之间的交互是通过接口
2. MVVM与MVP的不同：VM与View自动更新

###MVP模式主要有两种实现：
1. Passive View: View被设计的尽可能简单，所有的表现和业务逻辑都放在Presenter中实现。
2. Supervising Controller： View中可以包含简单的逻辑。View中无法满足的业务逻辑才放在Presenter中实现。

###Android的MVP实现里通常包含4个要素
1. View: 负责绘制UI元素、与用户进行交互(Activity或Fragment);
2. View interface: View需要实现的接口，View通过View interface与Presenter进行交互，降低耦合，方便单元测试
3. Model: 负责业务Bean的操作。
4. Presenter: 作为View与Model交互的纽带，承载了大部分的复杂逻辑。



###参考
1. [MVC/MVP/MVVM模式对比](http://segmentfault.com/a/1190000002738508)
2. [Philm项目源码分解析(1)(2)(3): 基本概念](http://codethink.me/2015/03/26/philm-source-code-analysis-1/)
3. [开源项目Philm的MVP架构分析](http://www.lightskystreet.com/2015/02/10/philm_mvp/)
4. [Philm.MVP 实践——UI篇](http://blog.csdn.net/ewrfedf/article/details/49635223)
5. 