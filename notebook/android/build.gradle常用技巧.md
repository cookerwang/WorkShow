##一、manifestPlaceholders的一些坑
渠道配置：

	AndroidManifest.xml：
	<meta-data
	        android:name="UMENG_APPKEY"
	        android:value="${UMENG_APPKEY_VALUE1}" />
	<meta-data
	        android:name="UMENG_CHANNEL"
	        android:value="${UMENG_CHANNEL_VALUE2}" />

----------
编译脚本配置：

	build.gradle:
	android {  
	    defaultConfig {
	        //manifestPlaceholders = [UMENG_CHANNEL_VALUE : "umeng"]
	        //manifestPlaceholders = [UMENG_APPKEY_VALUE : "appkey"]			
			manifestPlaceholders = [
						 UMENG_CHANNEL_VALUE1 : "umeng1",
						 UMENG_CHANNEL_VALUE2 : "umeng2",
                         UMENG_APPKEY_VALUE : "appkey",
                         YOUMI_CHANNEL_VALUE : "value"
                        ]
			manifestPlaceholders.put("UMENG_CHANNEL_VALUE0",'google')
	    }
	    productFlavors {
	        meizu {
				manifestPlaceholders.put("UMENG_CHANNEL_VALUE0",'meizu')
			}
	        wandoujia {}
	        ……
	    }  
	    productFlavors.all { 
	        flavor -> flavor.manifestPlaceholders = [UMENG_CHANNEL_VALUE: name] 
	    }
	}

##二、隐藏隐私的配置信息 [参考](https://github.com/CycloneAxe/phphub-android)
1. local.properties文件，在.gitignore中忽略，或gradle.properties文件自动加载（忽略版本控制）
2. 脚本配置：  

---------- 
	build.gradle:  
	apply plugin: 'com.android.application'  
	Properties properties = new Properties()  
	properties.load(project.rootProject.file('local.properties').newDataInputStream())  
	android {
	    ……省略很多
	        
	    manifestPlaceholders = [UMENG_CHANNEL_VALUE : "umeng",
	                            UMENG_APPKEY_VALUE : properties.getProperty("umengAppKey"),
							   ]

----------
	local.properties：
	sdk.dir=/Users/lanyy/Documents/adt/sdk
	umengAppKey=*************    

##三、增加.so动态库
1. libs下arm64-v8a、armeabi、armeabi-v7a、x86、x86_64中添加.so文件
2. build.gradle中android内添加
	sourceSets.main {
        jniLibs.srcDirs = ['libs']
    }

##四、assemble结合Build Variants来创建task
./gradlew assembleDebug 和 ./gradlew assembleRelease 之外，assemble 还能和 Product Flavor 结合创建新的任务，Build Variants = Build Type + Product Flavor。  
打包wandoujia渠道的release版本： ./gradlew assembleWandoujiaRelease  
只打wandoujia渠道版本(d&r)： ./gradlew assembleWandoujia  
Product Flavor下所有渠道的Release版本:./gradlew assembleRelease  

**assemble**： 允许直接构建一个Variant版本，例如assembleFlavor1Debug。  
**assemble**： 允许构建指定Build Type的所有APK，例如assembleDebug将会构建Flavor1Debug和Flavor2Debug两个Variant版本。  
**assemble**： 允许构建指定flavor的所有APK，例如assembleFlavor1将会构建Flavor1Debug和Flavor1Release两个Variant版本。  

##四、多版本编译 [美团技术方案](http://tech.meituan.com/mt-apk-packaging.htmle)
###方式一：
1. 原理：采用占位符方式
2. 步骤：
   + 在AndroidManifest.xml里配置PlaceHolder

----------

	<meta-data android:name="UMENG_CHANNEL" android:value="${UMENG_CHANNEL_VALUE}" />

   + 在build.gradle设置productFlavors 

----------

	android {  
	    productFlavors {
	        xiaomi {
	            manifestPlaceholders = [UMENG_CHANNEL_VALUE: "xiaomi"]
	        }
	        _360 {
	            manifestPlaceholders = [UMENG_CHANNEL_VALUE: "_360"]
	        }
	        baidu {
	            manifestPlaceholders = [UMENG_CHANNEL_VALUE: "baidu"]
	        }
	        wandoujia {
	            manifestPlaceholders = [UMENG_CHANNEL_VALUE: "wandoujia"]
	        }
	    }  
	}
   或者批量修改
	android {  
	    productFlavors {
	        xiaomi {}
	        _360 {}
	        baidu {}
	        wandoujia {}
	    }  
	
	    productFlavors.all { 
	        flavor -> flavor.manifestPlaceholders = [UMENG_CHANNEL_VALUE: name] 
	    }
	}	

###方式二：
1. 原理：采用动态替换渠道字符串的方式，生成各渠道的AndroidManifest.xml文件并打包。[参考](https://github.com/umeng/umeng-muti-channel-build-tool/tree/master/Gradle)，以下代码gradle 2.0以后有效。
2. 关键代码：

----------	
	//替换AndroidManifest.xml的default字符串为渠道名称
	android.applicationVariants.all { variant ->
		println "----------------------------------${variant.productFlavors[0].name}"
	    variant.outputs[0].processManifest.doLast {
	        //之前这里用的copy{}，我换成了文件操作，这样可以在v1.11版本正常运行，并保持文件夹整洁
	        //${buildDir}是指build文件夹
	        //${variant.dirName}是flavor/buildtype，例如GooglePlay/release，运行时会自动生成
	        //下面的路径是类似这样：build/manifests/GooglePlay/release/AndroidManifest.xml
			//路径需要根据实际情况做相应调整，gradlew build查看下
	        def manifestFile = "${buildDir}/intermediates/manifests/full/${variant.dirName}/AndroidManifest.xml"
	        //将字符串default替换成flavor的名字
	        def updatedContent = new File(manifestFile).getText('UTF-8').replaceAll("default", "${variant.productFlavors[0].name}")
	        new File(manifestFile).write(updatedContent, 'UTF-8')
	        //将此次flavor的AndroidManifest.xml文件指定为我们修改过的这个文件
	        variant.outputs[0].processResources.manifestFile = file("${buildDir}/intermediates/manifests/full/${variant.dirName}/AndroidManifest.xml")
	    }
	}

3. 具体步骤
AndroidManifest.xml中添加渠道meta
<application
        android:label="@string/app_name">
        <meta-data android:name="APP_PID" android:value="default" />
        <activity
            android:name="com.jayfeng.xxx"
            android:label="@string/app_name" >
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

4. 完整代码：

----------
	apply plugin: 'android'	
	android {
	    compileSdkVersion 19
	    buildToolsVersion '19.1'
	    defaultConfig {
	        applicationId 'com.jayfeng.app.test'
	        minSdkVersion 15
	        targetSdkVersion 19
	        versionCode 1
	        versionName 'V1.0'
	    }
		// 配置不同签名
	    signingConfigs {
	        debug {
	            storeFile file("xxxxxx.keystore")
	            storePassword "xxxxxxx"
	            keyAlias "xxxxx"
	            keyPassword "xxxxxx"
	        }
			release {
	            def signingFile = file('../signing.properties')
	            if( signingFile.canRead() ) {
	                def Properties props = new Properties()
	                props.load(new FileInputStream(signingFile))
	                try {
	                    storeFile = file( props['STORE_FILE'] )
	                    storePassword = props['STORE_PASSWORD']
	                    keyAlias = props['KEY_ALIAS']
	                    keyPassword = props['KEY_PASSWORD']
	                    println "RELEASE_BUILD: Signing..."
	                } catch (e) {
	                    throw new InvalidUserDataException("You should define STORE_FILE and STORE_PASSWORD and KEY_ALIAS and KEY_PASSWORD in signing.properties.")
	                }
	            } else {
	                println "RELEASE_BUILD: signing.properties not found"
	            }	
	        }
	    }
	
	    buildTypes {
			debug {
	            versionNameSuffix "-dev"
	            signingConfig signingConfigs.debug
	        }
	        release {
				// 读取key等敏感配置信息
	            def releaseFile = file('../release.properties')
	            if( releaseFile.canRead() ) {
	                def Properties props = new Properties()
	                props.load(new FileInputStream(releaseFile))
	                println "RELEASE_BUILD: load release.properties..."
	
	                if (props.containsKey('APP_CLIENT_ID')) {
	                    buildConfigField "String", "CLIENT_ID", "\"${props['APP_CLIENT_ID']}\""
	                }	
	                if (props.containsKey('APP_CLIENT_SECRET')) {
	                    buildConfigField "String", "CLIENT_SECRET", "\"${props['APP_CLIENT_SECRET']}\""
	                }	
	                if (props.containsKey('API_ENDPOINT')) {
	                    buildConfigField "String", "ENDPOINT", "\"${props['API_ENDPOINT']}\""
	                }
	            }
	            signingConfig signingConfigs.release
	            minifyEnabled false
	            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'

				applicationVariants.all { variant ->
	                variant.outputs.each { output ->
	                    def outputFile = output.outputFile
	                    if (outputFile != null && outputFile.name.endsWith('.apk')) {
	                    	// 输出apk名称为boohee_v1.0_2015-01-15_wandoujia.apk
	                        def fileName = "boohee_v${defaultConfig.versionName}_${releaseTime()}_${variant.productFlavors[0].name}.apk"
	                        output.outputFile = new File(outputFile.parent, fileName)
	                    }
	                }
	            }
	        }	        
	    }

		// This is important, it will run lint checks but won't abort build
		lintOptions {
		    abortOnError false
		    disable 'InvalidPackage'
		}

	    productFlavors {
	        "default" {}
	        google {
				applicationId "org.flysnow.google.dev"
			}
	        "91" {
				applicationId "org.flysnow.nine.one.dev"
			}
	        hiapk {}
	        gfan {}
	        goapk {}
	        appChina {}
	        mumayi {}
	        eoe {}
	        nduo {}
	        feiliu {}
	        crossmo {}
	        huawei {}
	        QQ {}
	        "3G" {}
	        "360" {}
	        baidu {}
	        sohu {}
	        "163" {}
	        UC {}
	        dangle {}
	        samsung {}
	        mmw {}
	        xiaomi {}
	        lenovo {}
	        nearme {}
	    }
	}
	
	dependencies {
	    compile fileTree(dir: 'libs', include: ['*.jar'])
	    compile project(':mblog')
	}
	// 防止编译时Gradle报乱码，2.0以前是Compile
	tasks.withType(JavaCompile) {
    	options.encoding = "UTF-8"
	}
	
	//替换AndroidManifest.xml的default字符串为渠道名称
	android.applicationVariants.all { variant ->
 		println "----------------------------------${variant.productFlavors[0].name}"
	    variant.outputs[0].processManifest.doLast {
	        //之前这里用的copy{}，我换成了文件操作，这样可以在v1.11版本正常运行，并保持文件夹整洁
	        //${buildDir}是指build文件夹
	        //${variant.dirName}是flavor/buildtype，例如GooglePlay/release，运行时会自动生成
	        //下面的路径是类似这样：build/manifests/GooglePlay/release/AndroidManifest.xml
			//路径需要根据实际情况做相应调整，gradlew build查看下
	        def manifestFile = "${buildDir}/intermediates/manifests/full/${variant.dirName}/AndroidManifest.xml"
	        //将字符串default替换成flavor的名字
	        def updatedContent = new File(manifestFile).getText('UTF-8').replaceAll("default", "${variant.productFlavors[0].name}")
	        new File(manifestFile).write(updatedContent, 'UTF-8')
	        //将此次flavor的AndroidManifest.xml文件指定为我们修改过的这个文件
	        variant.outputs[0].processResources.manifestFile = file("${buildDir}/intermediates/manifests/full/${variant.dirName}/AndroidManifest.xml")
	    }
	}
	