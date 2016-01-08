###一、持续构建步骤（每日构建）[可看论文](http://blog.csdn.net/hmsiwtv/article/details/19498255)
1. 定期检查源代码库；
2. 如果源代码库中发生改变将触发构建和后续测试；
3. 给源代码库的当前构建作一个标签，以便将来可以在当前够的基础上进行重新构建；
4. 给相应开发人员发送构建情况反馈。

###二、持续构建优点
持续集成在集成失败后向开发人员提供快速的反馈，让开发人员可以迅速修复错误，而不仅是在成功构建后提供一个稳定的版本；最后持续集成鼓励开发人员频繁的提交代码修改并得到尽快的反馈，每次修改的代码量减少后，出现问题的修复也变得容易和迅速。

###三、持续构建原则
1. 需要版本控制软件保障团队成员提交的代码不会导致集成失败。ClearCase、CVS、Subversion、Git 等；
2. 开发人员必须及时向版本控制库中提交代码，也必须经常性地从版本控制库中更新代码到本地；
3. 需要有专门的集成服务器来执行集成构建。根据项目的具体实际，集成构建可以被软件的修改来直接触发，也可以定时启动，如每半个小时构建一次；
4. 必须保证构建的成功。如果构建失败，修复构建过程中的错误是优先级最高的工作。一旦修复，需要手动启动一次构建。

###四、持续构建方案
1. 一个自动构建过程，包括自动编译、分发、部署和测试等。可使用ANT或者Maven等工具；
2. 一个代码存储库，即需要版本控制软件来保障代码的可维护性，同时作为构建过程的素材库。例如Subversion、Git、CVS；
3. 一个持续集成服务器。Cruise Control、Jenkins等。
4. 按照该组成，你可以选用自己喜欢的工具来设计你自己的持续集成系统方案。比如PMS平台（JIRA+CruiseControl+Subversion）[10]、Git+Jenkins+Gerrit[11]等。

###五、jenkins下载安装
1. 地址：http://mirrors.jenkins-ci.org/war/latest/jenkins.war，[官网](http://mirrors.jenkins-ci.org/)
2. jenkins.war包直接放到tomcat下的webapps目录，启动tomcat即可安装完成，tomcat的server.xml配置，在Connector标签添加上 URIEncoding="UTF-8" 属性，xp下建议下载安装包。
3. 浏览器输入：localhost:8080/jenkins 或 安装包直接打开里面的jenkins.exe，输入localhost:8080即可。
4. 系统管理->管理插件-> 可选插件 -> 选择Andriod Gradle Plugin、Android Lint Plugin、Extended E-mail Notification（Email Extension Plugin）、Git Plugin（网络不好建议下载再安装，注意要先安装好依赖插件,[下载](https://wiki.jenkins-ci.org/display/JENKINS/Plugins)）
   + 自动文档生成之Doxygen
   + Gerrit Trigger、Git plugin、Git Client plugin  
   为使Jenkins能获取代码审核工具Gerrit的变更信息，需安装Gerrit Trigger plugin，安装后通过简要配置连接的用户信息就能实时的获取Gerrit中的代码情况，并能监视变动进而触发相应的自动构建事件，最终将构建结果反馈给Gerrit，并设置review和verify的值。
   + Jenkins Mailer plugin
   实现构建结果通知。当构建结果不稳定或者失败的时候，可给指定的接收人邮箱发送邮件。接收人的邮箱地址支持变量输入，比如$GERRIT_CHANGE_OWNER_EMAIL，可以实现对向Gerrit提交代码修改的人发送邮件。
   +

###六、配置自动构建
1. 管理配置。。。。
2. 构建触发器
   + 格式说明[* *　* * *]： * 表示任意   
   第1列分钟1～59  第2列小时1～23（0表示子夜）第3列日1～31 第4列月1～12 第5列星期0～6（0表示星期天）  
   + Poll SCM：定时检查源码变更（根据SCM软件的版本号），如果有更新就checkout最新code下来，然后执行构建动作。 如 */5 * * * *  （每5分钟检查一次源码变化）  
   + Build periodically：周期进行项目构建（它不care源码是否发生变化）。 如 0 2 * * *  （每天2:00 必须build一次源码）  
3.  Switches ：即gradle 后面所接的命令。上面相当于执行gradle clean build命令。
4.  增加构建后步骤，email notification


