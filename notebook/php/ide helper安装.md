###[laravel-ide-helper](https://github.com/barryvdh/laravel-ide-helper)
1. 项目中执行composer require barryvdh/laravel-ide-helper 或 修改项目根目录下composer.json的require中添加"barryvdh/laravel-ide-helper": "^2.1"
2. 项目中执行composer update
3. config/app.php的provider中添加Barryvdh\LaravelIdeHelper\IdeHelperServiceProvider::class,
4. 项目中执行
	1. php artisan clear-compiled
	2. php artisan ide-helper:generate
	3. php artisan optimize
5. 修改项目中composer.json的script

----------
	"post-update-cmd": [
        "php artisan clear-compiled",
        "php artisan ide-helper:generate",
        "php artisan optimize"
    ]
	