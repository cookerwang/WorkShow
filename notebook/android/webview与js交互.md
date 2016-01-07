##WebView与JS交互
优秀开源库：[JSBridg](https://github.com/lzyzsd/JsBridge)
###一、原理
native可以直接调用js方法，但是js不可以直接调用nativie方法。  
  

WebView需要开启js使能：  
```
WebSettings setting = webView.getSettings();       
setting.setJavaScriptEnabled(true);//支持js
```

###js调用本地代码
1. 开发者须在代码申明JavascriptInterface，在4.0之前webView加载js：mWebView.addJavascriptInterface(new JsToJava(), "myjsfunction");，而4.4之后调用需要在调用方法加入加入@JavascriptInterface注解，如果代码无此申明，那么也就无法使得js生效，可避免恶意网页利用js对安卓客户端的窃取和攻击。
2. 用webview的代理方法进行字段拦截（判断url的scheme），实现js间接调用native的method。  
   WebViewClient的shouldOverrideUrlLoading(WebView view, String url)中对url拦截完成js与本地交互。

###本地调用js代码
1. Android向WebView注入js代码，通过webview.loadUrl("javascript:xxx")实现。
   + WebView加载完后(onPageFinished(...))读取整个js文件字符串内容，调用webview.loadUrl("javascript:fileContentString")注入，去掉注释的文件内容。
   + WebView页面加载完之后，直接向webview对应的html中加入"script"便签，并包含要注入的js的Url地址。  
	String js = "var newscript = document.createElement(\"script\");";  
	js += "newscript.src=\"http://www.123.456/789.js\";";  
	js += "newscript.onload=function(){xxx();};";  //xxx()代表js中某方法  
	js += "document.body.appendChild(newscript);";  
	webview.loadUrl("javascript:" + js);
2
