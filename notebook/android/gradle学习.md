##Gradle构建系统
###一、生命周期
1. 初始化：读取根工程中setting.gradle的include信息，决定哪些工程加入构建，创建project实例。
2. 配置:执行所有工程的build.gradle脚本，配置project对象，创建、配置task及相关信息。
3. 运行:根据gradle命令传递的task名称，执行相关依赖任务。

###二、相关知识：
1. Gradle中每一个待编译的工程都是一个Project，一个具体的编译过程是由一个一个的Task来定义和执行的。
2. afterEvaluate{}，配置阶段要结束，项目评估完执行
3. [深入理解gradle](http://blog.csdn.net/innost/article/details/48228651)
###三、小知识：
1. 添加编译变量：buildConfigField "boolean", "LOG_DEBUG", "false"可在buildTypes的debug和release中添加
2. dex突破65535的限制 android.defaultConfig{ multiDexEnabled true }
3. 启用混淆：

----------
	buildTypes {
	    release {
	        minifyEnabled true
			shrinkResources true
	        proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
	    }
	}
4. zipAlignEnabled true
5. 移除无用的resource文件 shrinkResources true
###四、常用命令
1. gradlew: gradle wrapper，在9GAG/gradle/wrapper/gralde-wrapper.properties文件中声明了它指向的目录和版本。只要下载成功即可用grdlew wrapper的命令代替全局的gradle命令。
2. gradlew -v 版本号
3. gradlew clean 清除9GAG/app目录下的build文件夹
4. gradlew build 检查依赖并编译打包(debug、release环境的包)
5. gradlew assembleDebug 编译并打Debug包
6. gradlew assembleRelease 编译并打Release的包
7. assemble还可以和productFlavors结合使用，具体见多渠道打包解释。
8. gradlew installRelease Release模式打包并安装
9. gradlew uninstallRelease 卸载Release模式包

###五、[gradle插件查找地址]
1. [https://plugins.gradle.org/](https://plugins.gradle.org/)
2. [https://bintray.com/gradle/gradle-plugins](https://bintray.com/gradle/gradle-plugins)

###六、(查看gradle最新版本)(http://gradle.org/gradle-download/)
gradle/wrapper/gradle-wrapper.properties中修改其版本号为最新版本

###七、gradle版本升级:[http://tools.android.com/recent](http://tools.android.com/recent)查看最新版本，[候选发布版本http://gradle.org/release-candidate/](http://gradle.org/release-candidate/)，[版本号查看](https://jcenter.bintray.com/com/android/tools/build/gradle/)
方法一：gradle/wrapper/gradle.properties文件中，打开对应网址[https://services.gradle.org/distributions](https://services.gradle.org/distributions)，找到新版本，修改其版本号
方法二：根目录build.gradle中添加任务，执行gradle wrapper命令：

	task wrapper(type: Wrapper) {  
	  gradleVersion = "2.0-rc-2"  
	}    
方法三：gradle wrapper --gradle-version 2.12-rc-1]

[gradle编译版本查看](http://jcenter.bintray.com/com/android/tools/build/gradle-core/)