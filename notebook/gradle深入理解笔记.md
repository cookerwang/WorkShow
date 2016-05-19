# gradle深入理解笔记
### Gradle 工程配置说明：  
1. 每个 Project 都须设置一个 build.gradle 文件。 multi-projects build，需在根目录下放一个 build.gradle 和 settings.gradle。 
2. 一个 Project 是由若干 tasks 来组成的，可通过gradle xxx 来执行具体任务
3. 具体工作和不同的插件有关系。插件定义很多相关工作的task。

### Gradle 工作三个阶段：  
1. 初始化阶段。multi-project build 是执行 settings.gradle。
2. 配置阶段。Configration 阶段的目标是解析每个 project 中的 build.gradle，确定整个build的project以及内部task的依赖关系。在这两个阶段之间，可通过api来加一些定制化的 Hook。  
3. 执行任务。根据task依赖关系，依次执行任务，其后还添加 Hook。
  
### Gradle 编程模型及[重要文档](https://docs.gradle.org/current/dsl/)：  

1. Gradle 对象：执行任务时 gradle 会从默认的配置脚本中构造出一个 Gradle 对象。在整个执行过程中，只有这么一个对象。
2. Project 对象：每一个 build.gradle 会转换成一个 Project，可直接使用其属性。 对象。[查看属性函数](https://docs.gradle.org/current/dsl/org.gradle.api.Project.html)
3. Settings 对象：显然，每一个 settings.gradle 都会转换成一个 Settings 对象。
4. 可直接在build.gradle中直接使用gradle、project、settings对象。

Project 对象对应的是 Build Script。具体工作：
1. 加载插件。Project 包含若干 Tasks。对应具体的工程，为其加载所需要的插件。通过 apply 函数，不同参数名来调用，apply from: '插件名'，apply from: 'gradle文件路径名'
2. 不同插件有不同的配置。需要在 Project 中配置好，插件就知道从哪里读取源文件等。
3. 设置属性。多个 gradle 文件中共用参数，可通过全局唯一gradle对象 设置 ext 属性来实现。Gradle 的 extra property 设置需在第一次定义该属性的时候需要通过 ext 前缀来标示它是一个额外的属性。Project 和 Gradle 对象都可以设置 ext 属性。

例子：  

    [settings.gradle]
    def initMinshengGradleEnvironment(){
        //属性值从 local.properites 中读取  
        Properties properties = new Properties()
        File propertyFile = new File(rootDir.getAbsolutePath() + "/local.properties")
        properties.load(propertyFile.newDataInputStream())
        //gradle 就是 Gradle 对象。它默认是 Settings 和 Project 的成员变量。
        // 可直接获取 ext 前缀，表明操作的是外置属性。
        // api 是一个新的属性名。
        // 第一次定义或者设置它的时候需要 ext 前缀  
        gradle.ext.api = properties.getProperty('sdk.api')
        println gradle.api  //再次存取 api 的时候，就不需要 ext 前缀了  
    }

    [utils.gradle]
    // utils.gradle 中定义了一个获取 AndroidManifests.xml versionName 的函数  
    def  getVersionNameAdvanced(){
       // 下面这行代码中的 project 是谁？ project 就是加载 utils.gradle 的 project。
       def xmlFile = project.file("AndroidManifest.xml") 
       def rootManifest = new XmlSlurper().parse(xmlFile)
       return rootManifest['@android:versionName']   
    }
    // 现在，想把这个 API 输出到各个 Project。
    // 由于这个 utils.gradle 会被每一个 Project Apply，
    // 所以我可以把 getVersionNameAdvanced 定义成一个 closure，
    // 然后赋值到一个外部属性  
    // 下面的 ext 是谁的 ext？  是 Project 对应的 ext 。
    ext { //此段花括号中代码是闭包  
        // 除了 ext.xxx=value 这种定义方法外，还可以使用 ext{}这种书写方法。
        // ext{}不是 ext(Closure)对应的函数调用。但是 ext{}中的{}确实是闭包。  
        getVersionNameAdvanced = this.&getVersionNameAdvanced
    }
    
#### 答疑：  
Project 和 utils.gradle 对于的 Script 的对象的关系是：  
当一个 Project apply 一个 gradle 文件的时候，这个 gradle 文件会转换成一个 Script 对象。  
Script 中有一个 delegate 对象，这个 delegate 默认是加载（即调用 apply）它的 Project 对象。在 apply 函数中，有一个 from 参数，还有一个 to 参数。通过 to 参数，你可以把 delegate 对象指定为别的东西。  
当你在 Script 中操作一些不是 Script 自己定义的变量，或者函数时候，gradle 会到 Script 的 delegate 对象去找，看看有没有定义这些变量或函数。  

[Task](https://docs.gradle.org/current/dsl/org.gradle.api.Task.html) 介绍：  
Task 是 Gradle 中的一种数据类型，代表一些要执行的工作。每个 Task 都需要和一个 Project 关联。

    [build.gradle]
    //Task 是和 Project 关联的，所以，我们要利用 Project 的 task 函数来创建一个 Task
    task myTask  <==myTask 是新建 Task 的名字  
    task myTask { configure closure }
    task myType << { task action }  <==注意，<<符号 是 doLast 的缩写  
    task myTask(type: SomeType)
    task myTask(type: SomeType) { configure closure }

一个 Task 包含若干 Action。所以，Task 有 doFirst 和 doLast 两个函数，用于添加需要最先执行的 Action 和需要和需要最后执行的 Action。Action 就是一个闭包。  
Task 创建的时候可以指定 Type，通过 type:名字表达。就是告诉 Gradle，这个新建的 Task 对象会从哪个基类 Task 派生。Copy 是 Gradle 中的一个类。task myTask(type:Copy)，创建的 Task 就是一个 Copy Task。
task myTask << {xxx}，创建了一个 Task 对象，同时把 closure 做为一个 action 加到这个 Task 的 action 队列中，并且告诉它“最后才执行这个 closure”（注意，<<符号是 doLast 的代表）。

例子

    [utils.gradle]
    import groovy.util.XmlSlurper  //解析 XML 时候要引入这个 groovy 的 package
    // 拷贝文件函数，用于将最后的生成物拷贝到指定的目录  
    def copyFile(String srcFile,dstFile){}
    // 删除指定目录中的文件  
    def rmFile(String targetFile){}
    // clean 的时候清理  
    def cleanOutput(boolean bJar = true){}
    // copyOutput 内部会调用 copyFile 完成一次 build 的产出物拷贝  
    def copyOutput(boolean bJar = true){}
    def getVersionNameAdvanced(){
       def xmlFile = project.file("AndroidManifest.xml")
       def rootManifest = new XmlSlurper().parse(xmlFile)
       return rootManifest['@android:versionName']   
    }
    // 对于 android library 编译，我会 disable 所有的 debug 编译任务  
    def disableDebugBuild(){
      //project.tasks 包含了所有的 tasks，
      // findAll 是寻找那些名字中带 debug 的 Task。
      //返回值保存到 targetTasks 容器中  
      def targetTasks = project.tasks.findAll{ task ->
          task.name.contains("Debug")
      }
      // 对满足条件的 task，设置它为 disable。如此这般，这个 Task 就不会被执行  
      targetTasks.each {
         println "disable debug task  : ${it.name}"
         it.setEnabled false
      }
    }
    // 将函数设置为 extra 属性中去
    // 加载 utils.gradle 的 Project 就能调用此文件中定义的函数
    ext{
        copyFile = this.&copyFile
        rmFile = this.&rmFile
        cleanOutput = this.&cleanOutput
        copyOutput = this.&copyOutput
        getVersionNameAdvanced = this.&getVersionNameAdvanced
        disableDebugBuild = this.&disableDebugBuild
    }

    [settings.gradle]
    /** 编译环境初始化函数  
     * 1  解析一个名为 local.properties 的文件，读取 Android SDK 和 NDK 的路径  
     * 2  获取最终产出物目录的路径。编译完的 apk 或者 jar 包将拷贝到这个最终产出物目录中  
     * 3 获取 Android SDK 指定编译的版本  
     **/
    def initMinshengGradleEnvironment(){
        println "initialize Minsheng Gradle Environment ....."
        Properties properties = new Properties()
        File propertyFile = new File(rootDir.getAbsolutePath() + "/local.properties")
        properties.load(propertyFile.newDataInputStream())
        /** 根据 Project、Gradle 生命周期，settings 对象的创建位于具体 Project 创建之前。
          * Gradle 对象已经创建好了。
          * local.properties 的信息读出来后，通过extra 属性的方式设置到 gradle 对象中
          * 具体 Project 在执行时，直接从 gradle 对象中得到这些属性了！
          **/
        gradle.ext.api = properties.getProperty('sdk.api')
        gradle.ext.sdkDir = properties.getProperty('sdk.dir')
        gradle.ext.ndkDir = properties.getProperty('ndk.dir')
        gradle.ext.localDir = properties.getProperty('local.dir')
        gradle.ext.debugKeystore = properties.getProperty('debug.keystore')
        gradle.ext.releaseKeystore = properties.getProperty('release.keystore')
         ......
        println "initialize Minsheng Gradle Environment completes..."
    }
    //初始化  
    initMinshengGradleEnvironment()
    //添加子 Project 信息  
    include 'CPosSystemSdk' , 'CPosDeviceSdk' , 'CPosSdkDemo','CPosDeviceServerApk', 'CPosSystemSdkWizarPosImpl'

    // 敏感信息隐藏，可单独建立文件，不做版本控制
    [local.properties]
    local.dir=/home/innost/workspace/minsheng-flat-dir/
    sdk.dir=/home/innost/workspace/android-aosp-sdk/
    ndk.dir=/home/innost/workspace/android-aosp-ndk/
    
    debug.keystore=/home/innost/workspace/tools/mykeystore.jks
    sdk.api=android-19
    
    //clean 是一个 Task 的名字，是插件引入的。  
    //dependsOn 是一个函数
    //clean 任务依赖 cposCleanTask 任务。
    //gradle clean 执行 clean Task 时，cposCleanTask 也会执行  
    clean.dependsOn 'cposCleanTask'
    task cposCleanTask() << {
        //cleanOutput 是 utils.gradle 中通过 extra 属性设置的 Closure
        cleanOutput(true)
    }
    
    //tasks 代表一个 Projects 中的所有 Task，是一个容器。
    // getByName 表示找到指定名称的任务。
    tasks.getByName("assemble") {
        it.doLast{
            println "$project.name: After assemble, jar libs are copied to local repository"
            copyOutput(true)
         }
    }
    // 当 Project 创建完所有任务的有向图后，通过 afterEvaluate 函数设置一个回调 Closure。
    // 在这个回调 Closure 里， disable 了所有 Debug 的 Task
    project.afterEvaluate{
        disableDebugBuild()
    }

    // 创建一个 Task，类型是 Exec，表明它会执行一个命令
    // tasks.withType(JavaCompile) 设置的依赖关系
    task buildNative(type: Exec, description: 'Compile JNI source via NDK') {
            if( project.gradle.ndkDir == null )
               println "CANNOT Build NDK"
            else{
                commandLine "/${project.gradle.ndkDir}/ndk-build",
                    '-C', file('jni').absolutePath,
                    '-j', Runtime.runtime.availableProcessors(),
                    'all', 'NDK_DEBUG=0'
            }
      }
     tasks.withType(JavaCompile) {
            compileTask -> compileTask.dependsOn buildNative
      }
      
      
      
    [build.gradle]
    apply plugin: 'com.android.application'
    apply from: rootProject.getRootDir().getAbsolutePath() + "/utils.gradle"
    //buildscript 设置 android app 插件的位置
    buildscript {
        repositories { jcenter() }
        dependencies { classpath 'com.android.tools.build:gradle:1.2.3' }
    }
    //android ScriptBlock
    android {
        compileSdkVersion gradle.api
        buildToolsVersion "22.0.1"
       sourceSets{ //源码设置 SB
            main{
                manifest.srcFile 'AndroidManifest.xml'
                jni.srcDirs = []
                jniLibs.srcDir 'libs'
                aidl.srcDirs=['src']
                java.srcDirs=['src']
                res.srcDirs=['res']
                assets.srcDirs = ['assets'] //多了一个 assets 目录  
    
            }
        }
        signingConfigs {//签名设置  
            debug {  //debug 对应的 SB。注意  
                if(project.gradle.debugKeystore != null){
                    storeFile file("file://${project.gradle.debugKeystore}")
                    storePassword "android"
                    keyAlias "androiddebugkey"
                    keyPassword "android"
                }
            }
        }
        /*
        buildTypes ScriptBlock.
        buildTypes 和上面的 signingConfigs，在 build.gradle 中通过{}配置它的时候，
        背后所代表对象是 NamedDomainObjectContainer<BuildType> 和  
        NamedDomainObjectContainer<SigningConfig>
        NamedDomainObjectContainer<BuildType/或者 SigningConfig>是一种容器，  
        容器的元素是 BuildType 或者 SigningConfig。
        在 debug{}要填充 BuildType 或者 SigningConfig 所包的元素，
        比如 storePassword 就是 SigningConfig 类的成员。
        proguardFile 等是 BuildType 的成员。  
        */
        signingConfig{//这是一个 NamedDomainObjectContainer<SigningConfig>
           test1{//新建一个名为 test1 的 SigningConfig 元素，然后添加到容器里  
             //在这个花括号中设置 SigningConfig 的成员变量的值  
           }
          test2{//新建一个名为 test2 的 SigningConfig 元素，然后添加到容器里  
             //在这个花括号中设置 SigningConfig 的成员变量的值  
          }
        }
        //在 buildTypes 中，Android 默认为这几个 NamedDomainObjectContainer 添加了  
        debug 和 release 对应的对象。
        //如果我们再添加别的名字的东西，那么 gradle assemble 的时候  
        //也会编译这个名字的 apk 出来。
        
        buildTypes{
            debug{ //修改 debug 的 signingConfig 为 signingConfig.debug 配置  
                signingConfig signingConfigs.debug
            }
            demo{ //demo 版需要混淆
                proguardFile 'proguard-project.txt'
                signingConfig signingConfigs.debug
            }
           //release 版没有设置，所以默认没有签名，没有混淆  
        }
       //来看如何动态生成 runtime_config 文件  
       def runtime_config_file = 'assets/runtime_config'
       /*
       我们在 gradle 解析完整个任务之后，找到对应的 Task，然后在里边添加一个 doFirst Action
       这样能确保编译开始的时候，我们就把 runtime_config 文件准备好了。
       注意，必须在 afterEvaluate 里边才能做，否则 gradle 没有建立完任务有向图，你是找不到什么 preDebugBuild 之类的任务的
       */
        project.afterEvaluate{
          //找到 preDebugBuild 任务，然后添加一个 Action  
          tasks.getByName("preDebugBuild"){
                it.doFirst{
                    println "generate debug configuration for ${project.name}"
                    def configFile = new File(runtime_config_file)
                    configFile.withOutputStream{os->
                        os << I am Debug\n'  //往配置文件里写 I am Debug
                     }
                }
            }
           //找到 preReleaseBuild 任务  
            tasks.getByName("preReleaseBuild"){
                it.doFirst{
                    println "generate release configuration for ${project.name}"
                    def configFile = new File(runtime_config_file)
                    configFile.withOutputStream{os->
                        os << I am release\n'
                    }
                }
            }
           //找到 preDemoBuild，此任务是在 buildType 里添加了一个 demo 的元素  
          //所以 Android APP 插件自动为我们生成的  
            tasks.getByName("preDemoBuild"){
                it.doFirst{
                    println "generate offlinedemo configuration for ${project.name}"
                    def configFile = new File(runtime_config_file)
                    configFile.withOutputStream{os->
                        os << I am Demo\n'
                    }
                }
            }
        }
    }
    
[参考一](http://wiki.jikexueyuan.com/project/deep-android-gradle/)
[参考二](https://segmentfault.com/a/1190000004229002)
