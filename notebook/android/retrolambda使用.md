##[retrolambda使用](https://medium.com/android-news/retrolambda-on-android-191cc8151f85#.s7bntwcrs)
1. 根目录的build.gradle中添加 classpath 'me.tatarka:gradle-retrolambda:3.2.3'   
2. 对应module的build.gradle中添加 apply plugin: 'me.tatarka.retrolambda' 以及  
	compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8  
        targetCompatibility JavaVersion.VERSION_1_8  
    }  


	retrolambda {
	  jvmArgs '-noverify'
	}