###android问题解决总结
1. 两个库依赖不同版本的相同库，gradle编译冲突（Duplicate files copied in APK META-INF/maven/com.squareup.okio/okio/pom.xml File1: C:\Users\Administrator\.gradle\caches\modules-2\files-2.1\com.squareup.okio\okio\1.4.0\5b72bf48563ea8410e650de14aa33ff69a3e8c35\okio-1.4.0.jar File2: F:\opensource\phphub-android\app\build\intermediates\exploded-aar\com.umeng\message\2.4.1\jars\libs\com.umeng.message.lib_v2.4.1.jar）  
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