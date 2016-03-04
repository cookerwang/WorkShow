###gradle加速
####1. 开启gradle单独的守护进程
	创建gradle.properties文件：
	/home/<username>/.gradle/ (Linux)
	/Users/<username>/.gradle/ (Mac)
	C:\Users\<username>\.gradle (Windows)
	增加： org.gradle.daemon=true
	Parallel Project Execution添加： org.gradle.parallel=true

	# Specifies the JVM arguments used for the daemon process.
	# The setting is particularly useful for tweaking memory settings.
	# Default value: -Xmx10248m -XX:MaxPermSize=256m
	org.gradle.jvmargs=-Xmx2048m -XX:MaxPermSize=512m -XX:+HeapDumpOnOutOfMemoryError -Dfile.encoding=UTF-8


	# The Gradle daemon aims to improve the startup and execution time of Gradle.
	# When set to true the Gradle daemon is to run the build.
	org.gradle.daemon=true
	
	# Specifies the JVM arguments used for the daemon process.
	# The setting is particularly useful for tweaking memory settings.
	# Default value: -Xmx10248m -XX:MaxPermSize=256m
	org.gradle.jvmargs=-Xmx2048m -XX:MaxPermSize=512m -XX:+HeapDumpOnOutOfMemoryError -Dfile.encoding=UTF-8
	
	# When configured, Gradle will run in incubating parallel mode.
	# This option should only be used with decoupled projects. More details, visit
	# http://www.gradle.org/docs/current/userguide/multi_project_builds.html#sec:decoupled_projects
	org.gradle.parallel=true
	
	# Enables new incubating mode that makes Gradle selective when configuring projects.
	# Only relevant projects are configured which results in faster builds for large multi-projects.
	# http://www.gradle.org/docs/current/userguide/multi_project_builds.html#sec:configuration_on_demand
	org.gradle.configureondemand=true

###2.Configure projects on demand
1. Have a global gradle.properties that all your projects will inherit;
2. Run the gradle build profile tool;
3. Stick to essential module dependencies (based on the profile tool results);
4. Skip unessencial gradle tasks

###3.项目根目录gradle.properties
	org.gradle.daemon=true
	org.gradle.jvmargs=-Dfile.encoding=UTF-8
	org.gradle.parallel=true
	org.gradle.configureondemand=true

###参考
1.[https://medium.com/@shelajev/6-tips-to-speed-up-your-gradle-build-3d98791d3df9#.ksr8btgzy](https://medium.com/@shelajev/6-tips-to-speed-up-your-gradle-build-3d98791d3df9#.ksr8btgzy)