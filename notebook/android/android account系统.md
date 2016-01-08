## Android账号系统
系统设置中有账号管理，微信、淘宝、qq、github等都有有自己的账号系统，进行设备账户管理的时候，会通过一个 AccountManager 类获取系统的账户管理类，通过 AccountManager 对象对账号系统进行操作。

[学习案例retroauth](https://github.com/andretietz/retroauth)

###一、使用方法
1. 获取账户管理类：

----------
	AccountManager mAccountManager = (AccountManager) getSystemService(ACCOUNT_SERVICE)；
	或
	AccountManager accountManager = AccountManager.get(context);
2. 查看获取账户信息权限：


----------
  	<!-- 获取账户信息权限 -->
	<uses-permission android:name="android.permission.GET_ACCOUNTS"/>
	<!-- 添加账户权限 -->
	<uses-permission android:name="android.permission.AUTHENTICATE_ACCOUNTS"/>  
3. 相关操作api  
   每种type的Account支持的AUTHORITY（比如ContactsContract.AUTHORITY）并不尽相同。
   通过ContentResolver.getSyncAdapterTypes()取得的SyncAdapterType来查询每种type的Account支持那些AUTHORITY。

----------
	AccountManager am = AccountManager.get(context);

	Account account = am.getAccountsByType("com.github.blabla");// 获取对应类型的账户	
	
	Account[] accounts = am.getAccounts(); // 获取手机所有账户信息
	for (Account acct : accounts) {
		Log.i(tag, "name:" + acct.name + " type:" + acct.type);
	}

	SyncAdapterType[] syncs = ContentResolver.getSyncAdapterTypes();
	for( SyncAdapterType sync : syncs ) {
		Log.i(tag,"type:"+sync.accountType+", autohrity:"+sync.authority);		
	}

###二、 建立自己的账户服务
1. manifest 文件中声明一个关于账号的Service

----------
	<service
        android:name="com.cook.account.app.account.AccountService"
        android:enabled="true"
        android:exported="true">
	    <intent-filter>
	        <action android:name="android.accounts.AccountAuthenticator"/>
	    </intent-filter>
	    <meta-data
	            android:name="android.accounts.AccountAuthenticator"
	            android:resource="@xml/authenticator"/>
	</service>
设置 intent-filter 告知系统，当前应用中有一个账号服务，具体的账号信息则放在 meta-data 中的 android:resource 文件中提供， 该文件为authenticator.xml，放置路径为 res/xml，内容(即设置里的账户信息)：

----------
	<?xml version="1.0" encoding="utf-8"?>
	<account-authenticator
        xmlns:android="http://schemas.android.com/apk/res/android"
        android:accountType="com.cook"
        android:icon="@drawable/ic_launcher"
        android:smallIcon="@drawable/ic_launcher"
        android:label="@string/app_name"/>  
2. 账户操作实现，继承系统提供的AbstractAccountAuthenticator的抽象类即可  

	public static class Authenticator extends AbstractAccountAuthenticator {
        public Authenticator(Context context) {
            super(context);
        }

        @Override
        public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
            return null;
        }

        @Override
        public Bundle addAccount(AccountAuthenticatorResponse response, 
				String accountType, String authTokenType, 
				String[] requiredFeatures, Bundle options) throws NetworkErrorException {
            return null;
        }

        @Override
        public Bundle confirmCredentials(AccountAuthenticatorResponse response, 
				Account account, Bundle options) throws NetworkErrorException {
            return null;
        }

        @Override
        public Bundle getAuthToken(AccountAuthenticatorResponse response, 
				Account account, String authTokenType, Bundle options) throws NetworkErrorException {
            return null;
        }

        @Override
        public String getAuthTokenLabel(String authTokenType) {
            return null;
        }

        @Override
        public Bundle updateCredentials(AccountAuthenticatorResponse response, 
				Account account, String authTokenType, Bundle options) throws NetworkErrorException {
            return null;
        }

        @Override
        public Bundle hasFeatures(AccountAuthenticatorResponse response, 
				Account account, String[] features) throws NetworkErrorException {
            return null;
        }
    }
3. 声明的账户服务中提供账户，com.cook.account.app.account.AccountService提供账户操作对象，以供调用

----------
	private Authenticator authenticator;

    @Override
    public void onCreate() {
        super.onCreate();
        authenticator = new Authenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return authenticator.getIBinder();
    }

4. 添加账户

----------
	<!-- 权限 -->	 
	<uses-permission android:name="android.permission.AUTHENTICATE_ACCOUNTS"/>  
重写 Authenticator 的 addAccount 方法。
当用户在添加账户页面选择账户进行添加或者调用accountManager.addAccount 的时候，系统会默认调用 AbstractAccountAuthenticator 中的 addAccount 方法，因此你需要重写 addAccount 方法，直接添加默认账户，或者跳转到某个页面，让用户填写用户信息，然后添加账户。

使用 addAccountExplicitly 直接添加账户

----------
	Account account = new Account("cook","com.kifile");  
	accountManager.addAccountExplicitly(account, password, bundleUserdata);  

###三、同步数据
1. 登陆验证
为保证信息和账户安全，丢失手机后会修改淘宝，QQ等的密码，而当丢失手机账户系统运行的时候，他会发现账户系统验证失败，就无法访问你相关账户了，以确保你的账户安全。

2. 信息同步  
   不同的设备上同一账号的用户的某些信息要保持同步的话，那么你就可以通过账户系统自带的同步服务来实现

   + 自动同步：在这里需要留意的是，虽然前者有设置一个固定时间间隔，但是 android 会尽量将所有同步数据的时间都安排在一起，以减少唤醒设备的次数，因此你可能发现虽然你设置了一个固定的间隔时间，但是到了那个时间点，系统其实并没有按时同步数据。   

----------
	ContentResolver.setSyncAutomatically(account1, "com.cook.provider", true);
	ContentResolver.addPeriodicSync(account1, "com.cook.provider", bundle, 10); // 单位秒 

   + 手动同步，调用账户同步接口   

----------
	ContentResolver.requestSync(account1, "com.cook.provider", bundle);
  