## Gradle打包上传，Maven仓库地址修改使用##
1. 登录maven私服nexus，创建host类型私有仓库。
2. 在as中建立module，编写代码。
3. 在对应module的build.gradle后面添加如下签名、上传任务代码。   
```
apply plugin: 'maven'  
apply plugin: 'signing'  
signing {  
    required { has("release") && gradle.taskGraph.hasTask("uploadArchives") }  
    sign configurations.archives  
}  
uploadArchives {  
    configuration = configurations.archives  
    repositories.mavenDeployer {  
        beforeDeployment { MavenDeployment deployment -> signing.signPom(deployment) }  
		// maven私服仓库地址，用户名与密码  
        repository(url: 'http://10.40.10.201:8081/nexus/content/repositories/  android_public_repositories') {  
            authentication(userName: "admin", password: "admin123")  
        }  
        pom.project {  
            name 'tt-library' // 名称  
            packaging 'aar'   // 库后缀名  
            description 'none' // 描述  
            url 'http://10.40.10.201:8081/nexus/content/repositories/  android_public_repositories/'//仓库地址  
            groupId "com.xiaoxin.avva" // maven groupId  
            artifactId "ttlib" // maven artifactId  
            version android.defaultConfig.versionName // maven version  
        }  
    }  
}  
```  
另一写法，build.gradle同目录下新建deploy.gradle文件，参考retrofacebook项目。  
在build.gradle同目录下添加gradle.properties文件，配置相关属性。
  
4. build->make project; build-> make lib('\*\*')(选中\*\*module); 右侧Gradle面板对应module下双击执行uploadArchives任务，控制台输出BUILD SUCCESSFUL即成功，查看仓库私服是否已有该库。遇到错误多尝试几次。
5. 使用私有仓库地址
6. + 方法一：在build.gradle的jcenter()前面添加maven {url "http://10.40.10.201:8081/nexus/content/repositories/android_public_repositories/"}；nexus中添加proxy仓库，远程地址指向http://jcenter.bintray.com/，同样添加maven {url "proxy仓库地址"}，可以加速下载使用过的依赖库，或修改为jcenter为国内镜像（ maven{ url 'http://maven.oschina.net/content/groups/public/'}），注意修改allprojects内。        

7. + 方法二：USER_HOME/.gradle/文件夹下建立init.gradle文件，添加如下代码。
```
allprojects{
    repositories {
        def REPOSITORY_URL = 'http://maven.oschina.net/content/groups/public'
        all { ArtifactRepository repo ->
            if(repo instanceof MavenArtifactRepository){
                def url = repo.url.toString()
                if (url.startsWith('https://repo1.maven.org/maven2') || url.startsWith('https://jcenter.bintray.com/')) {
                    project.logger.lifecycle "Repository ${repo.url} replaced by $REPOSITORY_URL."
                    remove repo
                }
            }
        }
        maven {
            url REPOSITORY_URL
        }
    }
}
```  
8. [英文可以参考](http://central.sonatype.org/pages/ossrh-guide.html的gradle部分)，[中文可参考](http://www.cnblogs.com/tiantianbyconan/p/4388175.html)

