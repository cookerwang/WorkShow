##retrolambda使用
1. 根目录的build.gradle中添加 classpath 'me.tatarka:gradle-retrolambda:3.2.0'   
2. 对应module的build.gradle中添加 apply plugin: 'me.tatarka.retrolambda' 以及  
	compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8  
        targetCompatibility JavaVersion.VERSION_1_8  
    }  
