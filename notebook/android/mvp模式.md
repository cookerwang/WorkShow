###MVP设计模式
###MVC模式图  
<img src="../images/mvc.png"/>
###model2_mvc模式图  
<img src="../images/model2_mvc.png"/>
###MVP模式图  
<img src="../images/mvp.png"/>
###MVVM模式图  
<img src="../images/mvvm.png"/>

###MVP模式主要有两种实现：
1. Passive View: View被设计的尽可能简单，所有的表现和业务逻辑都放在Presenter中实现。
2. Supervising Controller： View中可以包含简单的逻辑。View中无法满足的业务逻辑才放在Presenter中实现。

###Android的MVP实现里通常包含4个要素
1. View: 负责绘制UI元素、与用户进行交互(Activity或Fragment);
2. View interface: View需要实现的接口，View通过View interface与Presenter进行交互，降低耦合，方便单元测试
3. Model: 负责业务Bean的操作。
4. Presenter: 作为View与Model交互的纽带，承载了大部分的复杂逻辑。



###参考
1. [Philm项目源码分解析(1): 基本概念](http://codethink.me/2015/03/26/philm-source-code-analysis-1/)