1. 安装，在settings->plugin中输入idea vim查找安装重启ide
2. ctl+shif+A -> keymap -> 进入keymap设置
3. 点击 find action by shortcut 搜索图标，输入ctl+shift+v找到vim Emulate的快捷设置，右击删除，右击添加ctrl+shit+=，解决vim emulate模式切换的快捷键冲突问题
###vim使用
####1. 选择文本
在Vim中，选择文本需要进入“可视模式”（Visual Mode），在Vim中选择区域会高亮显示，因此称为“可视模式”。
v - 进入字符选择模式， V - 进入行选择模式， Ctrl+v - 进入块选择模式。
进入相应模式后移动光标即可选中文本。过程中可按o键令光标在选区两端切换。
在块选择模式中选中多行，然后按I或A后输入文本，再退出插入模式，所输入的文本将自动加入到每一行的开头或结尾。 
####2. 查找命令
	/text　　查找text，按n健查找下一个，按N健查找前一个。  
	?text　　查找text，反向查找，按n健查找下一个，按N健查找前一个。  
	vim中有一些特殊字符在查找时需要转义　　.*[]^%/?~$  
	:set ignorecase　　忽略大小写的查找  
	:set noignorecase　　不忽略大小写的查找
	查找很长的词，如果一个词很长，键入麻烦，可以将光标移动到该词上，按*或#键即可以该单词进行搜索，相当于/搜索。而#命令相当于?搜索。
	:set hlsearch　　高亮搜索结果，所有结果都高亮显示，而不是只显示一个匹配。  
	:set nohlsearch　　关闭高亮搜索显示  
	:nohlsearch　　关闭当前的高亮显示，如果再次搜索或者按下n或N键，则会再次高亮。  
	:set incsearch　　逐步搜索模式，对当前键入的字符进行搜索而不必等待键入完成。  
	:set wrapscan　　重新搜索，在搜索到文件头或尾时，返回继续搜索，默认开启。
####3. 替换命令

	ra 将当前字符替换为a，当期字符即光标所在字符。
	s/old/new/ 用old替换new，替换当前行的第一个匹配
	s/old/new/g 用old替换new，替换当前行的所有匹配
	%s/old/new/ 用old替换new，替换所有行的第一个匹配
	%s/old/new/g 用old替换new，替换整个文件的所有匹配
	:10,20 s/^/    /g 在第10行知第20行每行前面加四个空格，用于缩进。
	ddp 交换光标所在行和其下紧邻的一行。
####4. 移动命令
	Ctrl + e 向下滚动一行
	Ctrl + y 向上滚动一行
	Ctrl + d 向下滚动半屏
	Ctrl + u 向上滚动半屏
	Ctrl + f 向下滚动一屏
	Ctrl + b 向上滚动一屏
####5. 撤销和重做
	u 撤销（Undo）
	U 撤销对整行的操作
	Ctrl + r 重做（Redo），即撤销的撤销。
####6.窗口命令
	:split或new 打开一个新窗口，光标停在顶层的窗口上
	:split file或:new file 用新窗口打开文件
	split打开的窗口都是横向的，使用vsplit可以纵向打开窗口。
	Ctrl+ww 移动到下一个窗口
	Ctrl+wj 移动到下方的窗口
	Ctrl+wk 移动到上方的窗口
	关闭窗口
	:close 最后一个窗口不能使用此命令，可以防止意外退出vim。
	:q 如果是最后一个被关闭的窗口，那么将退出vim。
	ZZ 保存并退出。
	关闭所有窗口，只保留当前窗口
	:only