1. 安装node.js，[下载msi文件](http://npm.taobao.org/mirrors/node/latest/)， 查看版本：node -v
2. 安装npm，[下载](https://npm.taobao.org/mirrors/npm/),解压进入，执行  
git clone --recursive git://github.com/isaacs/npm.git  
node cli.js install npm -gf  
3. 设置[npm淘宝源:](http://npm.taobao.org/)  
npm install -g cnpm --registry=https://registry.npm.taobao.org
4. 安装bower，cnpm install -g bower

绕过某些不必要的网络问题来安装第三方库：
npm --registry "http://npm.hacknodejs.com/" install underscore 
设为默认的资源库：
npm config set registry "http://npm.hacknodejs.com/"

