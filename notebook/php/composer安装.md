1.配置编译环境时，php.exe报系统错误，无法启动此程序提示丢失*.dll。

2.在C:\Windows找到php.ini文件，ctrl+f找到extension=*.dll修改为绝对路径即可。

3.[php安装](http://php.net/)

4.[composer安装](http://pkg.phpcomposer.com/)，[composer使用](http://www.phpcomposer.com/)

	xp:
	php -r "readfile('http://install.phpcomposer.com/installer');" | php
	echo @php "%~dp0composer.phar" %*>composer.bat
	linux:
	curl -sS http://install.phpcomposer.com/installer | sudo php -- --install-dir=/usr/local/bin --filename=composer