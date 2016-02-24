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
###2.Configure projects on demand
1. Have a global gradle.properties that all your projects will inherit;
2. Run the gradle build profile tool;
3. Stick to essential module dependencies (based on the profile tool results);
4. Skip unessencial gradle tasks