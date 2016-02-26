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
   + php artisan serve （php artisan查看其使用方法）或 php -S localhost:8000 -t public
   + 浏览器查看：localhost:8000
   + 或使用honestead
###三、 基本工作流程：Router，Views，Controllers
1.访问网站根目录的请求到来，app/Http/routes.php被执行，并返回视图。

	// 匿名函数加载视图
	Route::get('/', function () {
	    return view('welcome');
	});
	// 匿名函数加载视图resources/views/welcome.blade.php
	// Laravel的模板文件就是blade.php结尾
	// Laravel的view()方法会默认从views文件夹查找

	// 模板文件在resources/views/site/about.blade.php
	Route::get('/about', function () {
	    return view('site.about');
	});
	// 添加路由，可选参数，否则不带参报错
	Route::get('/user/{name?}', function ($name = 'wrx') {
	    return 'Hello '.$name;
	});
	// localhost:8000/user/wangrenxing

	// 控制器加载视图，控制器定义在app/Http/Controllers目录下
	// 创建控制器：php artisan make:controller ArticleController 
	// 在改控制类中添加index与about方法
	Route::get('/','ArticleController@index');
	Route::get('/about','ArticleController@about');
	
	// 基本编写流程
	1. 在routes.php中注册路由 ---> Route::get('/','ArticleController@index');
	2. 创建对应的控制器 ---> php artisan make:controller ArticleController
	3. 在控制器中得对于方法加载视图 --->public function index() { return view ('articles.lists'); }

###四、视图传值
######blade模板变量输出
	{{ $title }} // 变量内容转义输出
	{!! $title !!} // 变量内容不转义输出
######方法1: with()方法
	public function index() {
        $title = '文章标题1';
        return view('articles.lists')->with('title',$title);
    }
	public function index() {
        $title = '文章标题1';
		$content = '内容';
        return view('articles.lists')->with(['title'=>$title, 'content'=>$cotent]);
    }
######方法2：直接给view()传参数
	public function index() {
        $title = '<span style="color: red">文章</span>标题1';
        return view('articles.lists',['title'=>$title]);
    }
	public function index() {
        $title = '<span style="color: red">文章</span>标题1';
        $intro = '文章一的简介';
        return view('articles.lists',[
			'title'=>$title,
			'introduction'=>$intro
			]);
######方法三：使用compact
	public function index() {
        $title = '<span style="color: red">文章</span>标题1';
        $intro = '文章一的简介';
        return view('articles.lists',compact('title','intro'));
    }
###五、blade基本用法
1.@yield('content') // 占位区域content，公共部分写好，变化部分占位，供子模板改写占位  
2.@extends('app') // 继承自app.blade.php  
3.替换父模板中的占位区域  

	@section('content')
	<h1>{!! $title !!}</h1>
	<p>{{ $intro }}</p>
	@endsection 

4.条件判断

	@if (count($records) === 1)
    I have one record!
	@elseif (count($records) > 1)
	    I have multiple records!
	@else
	    I don't have any records!
	@endif

	// 如果不
	@unless (Auth::check())
    You are not signed in.
	@endunless
	
5.循环输出

	public function index() {
        $first = ['jelly','bool'];
        return view('articles.lists',compact('first'));
    }

	@extends('app')
	@section('content')
	@foreach( $first as $name)
	    <h1> {{ $name }}</h1>
	@endforeach
	@endsection

###六、db与eloquent
#####1. 数据库配置
	config/database.php
	.env文件
#####2. Migration
	// 生成migration文件，位于database/migrations/目录下
	php artisan make:migration create_article_table --create='articles'
	// 修改migration的up()，执行如下命令生成创建表等
	php artisan migrate
	// 修改down()，执行回滚命令
	php artisan migrate:rollback
	// 表面复数，对应model单数，首写字母大写，创建model文件
	php artisan make:model Article

	// model命令交互窗口
	php artisan tinker
	$article = new App\Article
	// model相关操作看对应文档
	$articles = App\Article::where('id', '=', '1')->get();
	$articles = App\Article::where('id', '=', '1')->first();
	// 查询所有
	$articles = App\Article::all();
	// 查找指定id数据集
	$article = App\Article::find(1);
	$article->content = '修改内容';
	$article->save(); // 保存到数据库
	// model中添加
	protected $fillable = ['title', 'content', 'intro', 'pubished_at'];
	$article->update(['content'=>'修改内容']);// 有效
	App\Article::create(['title'=>'标题', 'content'=>'内容']);
	$article->delete(); // 删除
	App\Article::destroy(1); // 接收id或id的数组
	App\Article::all()->toArray();
	App\Article::all()->toJson();
	


#####3.创建controller
	php artisan make:controller ArticleController --plain

###七、MVC基本流程
	1.注册路由, 首页路由：app/Http/routes.php添加Route::get('/', 'ArticleController@index');
	2.创建控制器，php artisan make:controller ArticleController --plain
	3.控制器中获取数据库数据（配置db，创建migration、model等，查看db与eloquent）
	4.视图中展示数据,链接可用路由中的路径、action、url方法

###八、使用laravelcollective/html
	package与laravel关联：通过提供Service Provider和指定Facade
	1.composer require laravelcollective/html
	2.composer update
    3.php artisan optimize
	4.config/app.php中添加
		//增加providers数组项
		  'providers' => [
		    // ...
		    Collective\Html\HtmlServiceProvider::class,
		    // ...
		  ],
		
		 //增加alias数组项 
		  'aliases' => [
		    // ...
		      'Form' => Collective\Html\FormFacade::class,
		      'Html' => Collective\Html\HtmlFacade::class,
		    // ...
		  ],
	5.视图文件中使用
	  {!! Form::open('/article/create') !!} 
	  {!! Form::label('title', '标题:') !!}
      {!! Form::text('title', null, ['class'=>'form-control']) !!}
	  {!! Form::close() !!}
	
###九、小知识点
	1.queryScope: 查询语句，创建方法，model中添加public function scopePublished($query) { return $query->where('published_at', '<=', Carbon::now()); },则在controller中可用Article::all()->published()->get();
	2.setAttribute: 数据保存前预处理, 对published_at保存前处理，model中添加public function setPublishedAtAttribute($date) {}
	3.model中添加protected $dates = ['published_at']，则published_at以Carbon对象存在