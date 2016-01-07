###配置php编译
tools->build system->new build system：
{
	"cmd": ["D:\\xampp\\php\\php", "$file"], "file_regex": "php$", "path":"D:\\xampp\\php","selector": "source.php" 
}
保存为php.sublime-build

###个性配置
Settings Default 内容拷贝修改项 到 Settings User中。font_size、tab_size、"draw_white_space": "all"、"line_numbers": true、"translate_tabs_to_spaces": true,等保存

###插件安装
1. [Package Control](https://packagecontrol.io/installation)
2. Preferences -> Package Control -> install packages (enter) -> emment
3. 同2安装 PHPNinjaManual