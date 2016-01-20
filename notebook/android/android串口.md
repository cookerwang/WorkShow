###一、模拟器使用pc串口
1. cmd进入Android\sdk\tools目录，执行
	
	// 启动名字为android4_2的虚拟机
	emulator @android4_2 -qemu -serial COM1

2. 进入android系统shell执行

	adb shell	
	ll /dev/* | grep ttyS  #看到ttyS0 ttyS1 ttyS2，则ttyS2是新挂载的com1   
	ll /dev/ttyS2 #查看权限	
	chmod 777 /dev/ttyS2  #修改为可读写执行权限

3. 打开串口设备文件，读写文件流

###参考
1. [android-serialport-api](https://github.com/cepr/android-serialport-api)