###android问题解决总结
1: 两个库依赖不同版本的相同库，gradle编译冲突（Duplicate files copied in APK META-INF/maven/com.squareup.okio/okio/pom.xml File1: C:\Users\Administrator\.gradle\caches\modules-2\files-2.1\com.squareup.okio\okio\1.4.0\5b72bf48563ea8410e650de14aa33ff69a3e8c35\okio-1.4.0.jar File2: F:\opensource\phphub-android\app\build\intermediates\exploded-aar\com.umeng\message\2.4.1\jars\libs\com.umeng.message.lib_v2.4.1.jar）  
   解决方法(编译排除文件)：

----------
	packagingOptions {
		exclude 'META-INF/maven/com.squareup.okio/okio/pom.xml'
        exclude 'META-INF/maven/com.squareup.okio/okio/pom.properties'
        
		// Butter Knife
        exclude 'META-INF/services/javax.annotation.processing.Processor'
        exclude 'META-INF/NOTICE.txt'
        exclude 'META-INF/LICENSE.txt'
        exclude 'META-INF/DEPENDENCIES.txt'

        exclude 'META-INF/NOTICE'
        exclude 'META-INF/LICENSE'
        exclude 'META-INF/DEPENDENCIES'
        exclude 'META-INF/notice.txt'
        exclude 'META-INF/license.txt'
        exclude 'META-INF/LGPL2.1'        
    }
2: 引入某个库，排除改库中引用的库

	compile ('com.uwetrottmann:trakt-java:4.5.2') {
        exclude group: 'org.json', module: 'json'
        exclude group: 'joda-time', module: 'joda-time'
    }
	compile('com.google.api-client:google-api-client-android:1.20.0') {
        exclude group: 'com.google.android.google-play-services'
        exclude(group: 'org.apache.httpcomponents', module: 'httpclient')
    }

3: 错误：

----------
	 UNEXPECTED TOP-LEVEL EXCEPTION:
	    com.android.dx.cf.iface.ParseException: bad class file magic (cafebabe) or version (0034.0000)
	原因：库编译所用jdk版本与当前android jdk版本不兼容所致。库与库的jdk版本不兼容等。
	解决方法：排除该额外库引入，参考上
	android application则：
	compileOptions {
	    encoding = "UTF-8"
	    sourceCompatibility JavaVersion.VERSION_1_7
	    targetCompatibility JavaVersion.VERSION_1_7
	  }

	java module中
	apply plugin: 'java'
	sourceCompatibility=JavaVersion.VERSION_1_7
	targetCompatibility=JavaVersion.VERSION_1_7

4: Android Gradle Plugin版本兼容问题，gradle版本升级导致。

	http://tools.android.com/recent查看最新。
	改成
	buildscript {
	  repositories {
	      //mavenCentral()
	      maven { url "http://maven.oschina.net/content/groups/public/" }
	   }	 
	}

	https://jcenter.bintray.com/com/android/tools/build/gradle/查看最新版本，多刷几次

	or	
	change classpath 'com.android.tools.build:gradle:2.0.0-alpha1' to classpath 'com.android.tools.build:gradle:2.0.+'

	or 
	http://www.androiddevtools.cn/下载最新的gradle版本zip包，gradle-2.10-all.zip
	放入~/.gradle\wrapper\dists\gradle-2.10-all下的随机字符串文件名的目录下重启即可

5：添加全局国内镜像
	gradle 初始化脚本 修改默认的repositories
	
	* 修改项目中的 build.gradle		
	repositories{
	    maven {
	        url "http://maven.oschina.net/content/groups/public/" //开源中国的maven镜像
	    }
	    jcenter()
	}
	初始化脚本：	
		命令行 (这个我就不说了)
		放一个init.gradle 文件到USER_HOME/.gradle/目录下
		放一个后缀是.gradle的文件到 USER_HOME/.gradle/init.d/ 目录下.
		放一个后缀是.gradle的文件到 GRADLE_HOME/init.d/ 目录下.

	init.gradle	
	allprojects {
	    repositories {
	         maven {
	             name "oschinaRepo"
	             url "http://maven.oschina.net/content/groups/public/"
	         }
	    }
	}