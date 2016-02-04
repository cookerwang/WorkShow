###一、[php安装](http://php.net/)
1.配置编译环境时，php.exe报系统错误，无法启动此程序提示丢失*.dll。

2.在C:\Windows找到php.ini文件，ctrl+f找到extension=*.dll修改为绝对路径即可。

###二、[composer安装](http://pkg.phpcomposer.com/)
1.下载安装 

	xp:
	php -r "readfile('http://install.phpcomposer.com/installer');" | php
	echo @php "%~dp0composer.phar" %*>composer.bat
	linux:
	curl -sS http://install.phpcomposer.com/installer | sudo php -- --install-dir=/usr/local/bin --filename=composer
2.修改配置(国内镜像)   
	[国内镜像](http://packagist.cn/)  
	1. 全局配置：composer config -g repo.packagist composer http://packagist.phpcomposer.com
	2. 局部配置，在项目中composer.json文件结尾添加：  	
	"repositories": {
	    "packagist": {
	        "type": "composer",
	        "url": "http://packagist.phpcomposer.com"
	    }
	}

3.保持composer自我更新，composer selfupdate

4.[查看composer可用包](https://packagist.org/explore/)

###三、[composer使用](http://www.phpcomposer.com/)
####composer安装依赖
1.composer install项目安装，需要编写composer.json，告知依赖包，如：

	{
	    "require": {
	        "monolog/monolog": "1.0.*"
	    }
	}
	require 需要一个 包名称 （例如 monolog/monolog） 映射到 包版本 （例如 1.0.*） 的对象
	包名称：由供应商名称和其项目名称构成
	包版本：确切的版本号: 1.0.2
		   范围 	>=1.0 >=1.0,<2.0 >=1.0,<1.1|>=1.2	
		   通配符  1.0.* 	
		   赋值运算符 ~1.2 

3.composer install执行后，将依赖包下载到vendor目录中，并把安装时确切的版本号列表写入 composer.lock 文件。这将锁定改项目的特定版本。  

4.install 命令将会检查锁文件是否存在，如果存在，它将下载指定的版本（忽略 composer.json 文件中的定义）。  

5.更新：php composer.phar update 或 php composer.phar update monolog/monolog [...]
####自动加载
1.库的自动加载信息，Composer 生成了一个 vendor/autoload.php 文件。简单引入这个文件，得到免费的自动加载支持。

	require 'vendor/autoload.php';
	可以在 composer.json 的 autoload 字段中增加自己的 autoloader。
	{
	    "autoload": {
	        "psr-4": {"Acme\\": "src/"}
	    }
	}
	Composer 将注册一个 PSR-4 autoloader 到 Acme 命名空间

####每一个项目都是一个包
只要你有一个 composer.json 文件在目录中，那么整个目录就是一个包。当你添加一个 require 到项目中，你就是在创建一个依赖于其它库的包。你的项目和库之间唯一的区别是，你的项目是一个没有名字的包。为了使它成为一个可安装的包，你需要给它一个名称。你可以通过 composer.json 中的 name 来定义：

	{
	    "name": "acme/hello-world",
	    "require": {
	        "monolog/monolog": "1.0.*"
	    }
	}

####创建项目 create-project
你可以使用 Composer 从现有的包中创建一个新的项目。这相当于执行了一个 git clone 或 svn checkout 命令后将这个包的依赖安装到它自己的 vendor 目录。常见的用途：

    你可以快速的部署你的应用。
    你可以检出任何资源包，并开发它的补丁。
    多人开发项目，可以用它来加快应用的初始化。

要创建基于 Composer 的新项目，你可以使用 "create-project" 命令。传递一个包名，它会为你创建项目的目录。你也可以在第三个参数中指定版本号，否则将获取最新的版本。

如果该目录目前不存在，则会在安装过程中自动创建。

php composer.phar create-project doctrine/orm path 2.2.*

此外，你也可以无需使用这个命令，而是通过现有的 composer.json 文件来启动这个项目。

默认情况下，这个命令会在 packagist.org 上查找你指定的包。