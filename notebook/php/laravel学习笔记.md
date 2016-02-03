###一、laravel学习网址
1. [为 WEB 艺术家创造的 PHP 框架](http://www.golaravel.com/)
2. [laravel artist](https://laravist.com/)
3. [laravel 学院](http://laravelacademy.org/)
4. [phphub](https://phphub.org/)
5. [博客](https://jellybool.com)
###二、安装使用
1. 学习理解composer，并配置国内镜像
2. 安装（三种方法）
   + [下载一键包](http://www.golaravel.com/download/)，即已执行过composer install，并下载好依赖到vender目录
   + 通过 Laravel 安装工具安装 Laravel，首先，使用 Composer 下载 Laravel 安装包：
	composer global require "laravel/installer"，将 ~/.composer/vendor/bin 目录添加到path，可执行文件 laravel 就能全局使用。创建项目blog：laravel new blog
   + Composer 的 create-project 命令来安装 Laravel：  
	composer create-project laravel/laravel --prefer-dist
	composer create-project laravel/laravel blog 5.2
3. 创建例子
   + composer create-project laravel/laravel blog 5.2
   + cd blog
   + php artisan serve
   + 浏览器查看：localhost:8000
   