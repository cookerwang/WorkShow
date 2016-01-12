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

		// login activiy的启动action
		private final String action;

		public static final String KEY_TOKEN_TYPE = "account_token_type";
		/**
		 * Creates an Intent to open the Activity to login
		 *
		 * @param response    needed parameter
		 * @param accountType The account Type
		 * @param tokenType   The requested token type
		 * @param accountName The name of the account
		 * @return a bundle to open the activity
		 */
		private Bundle createAuthBundle(AccountAuthenticatorResponse response, String accountType, String tokenType, String accountName) {
			Intent intent = new Intent(action);
			intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
			intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, accountType);
			intent.putExtra(KEY_TOKEN_TYPE, tokenType);
			if (null != accountName) {
				intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, accountName);
			}
			final Bundle bundle = new Bundle();
			bundle.putParcelable(AccountManager.KEY_INTENT, intent);
			return bundle;
		}


        @Override
        public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
            return null;
        }

        @Override
        public Bundle addAccount(AccountAuthenticatorResponse response, 
				String accountType, String authTokenType, 
				String[] requiredFeatures, Bundle options) throws NetworkErrorException {
            return createAuthBundle(response, accountType, authTokenType, null);
        }

        @Override
        public Bundle confirmCredentials(AccountAuthenticatorResponse response, 
				Account account, Bundle options) throws NetworkErrorException {
            return null;
        }

        @Override
        public Bundle getAuthToken(AccountAuthenticatorResponse response, 
				Account account, String authTokenType, Bundle options) throws NetworkErrorException {
            return createAuthBundle(response, account.type, authTokenType, account.name);
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
  

###封装accountmanager
	import android.accounts.Account;
	import android.accounts.AccountManager;
	import android.accounts.AccountManagerFuture;
	import android.accounts.AuthenticatorException;
	import android.accounts.OperationCanceledException;
	import android.app.Activity;
	import android.content.Context;
	import android.content.DialogInterface;
	import android.content.SharedPreferences;
	import android.os.Bundle;
	import android.support.annotation.NonNull;
	import android.support.annotation.Nullable;
	import android.support.v7.app.AlertDialog;
	import android.util.Log;
	
	import java.io.IOException;
	import java.util.ArrayList;
	
	import eu.unicate.retroauth.exceptions.AuthenticationCanceledException;
	import eu.unicate.retroauth.interfaces.BaseAccountManager;
	import rx.Observable;
	import rx.Subscriber;
	
	/**
	 * This class wraps the Android AccountManager and adds some retroauth specific
	 * functionality. This is the main helper class, when working with retroauth.
	 */
	public final class AuthAccountManager implements BaseAccountManager {
	
		static final String RETROAUTH_ACCOUNTNAME_KEY = "retroauthActiveAccount";
		private Context context;
		private AccountManager accountManager;
	
		/**
		 * initializes the class with a context and an AccountManager
		 *
		 * @param context the Android Context
		 */
		public AuthAccountManager(Context context) {
			this.context = context;
			this.accountManager = AccountManager.get(context);
		}
	
		/**
		 * initializes the class with a context and an AccountManager
		 *
		 * @param context        the Android Context
		 * @param accountManager an AccountManager to use
		 */
		public AuthAccountManager(Context context, AccountManager accountManager) {
			this.context = context;
			this.accountManager = accountManager;
		}
	
		/**
		 * {@inheritDoc}
		 */
		@Nullable
		@Override
		public Account getActiveAccount(@NonNull String accountType, boolean showDialog) {
			return getAccountByName(getActiveAccountName(accountType, showDialog), accountType);
		}
	
		/**
		 * {@inheritDoc}
		 */
		@Nullable
		@Override
		public Account getAccountByName(@Nullable String accountName, @NonNull String accountType) {
			// if there's no name, there's no account
			if (accountName == null) return null;
			Account[] accounts = accountManager.getAccountsByType(accountType);
			if (accounts.length == 0) return null;
			if (accounts.length > 1) {
				for (Account account : accounts) {
					if (accountName.equals(account.name)) return account;
				}
				return null;
			}
			return accounts[0];
		}
	
		/**
		 * {@inheritDoc}
		 */
		@Nullable
		@Override
		public String getActiveAccountName(@NonNull String accountType, boolean showDialog) {
			Account[] accounts = accountManager.getAccountsByType(accountType);
			if (accounts.length < 1) {
				return null;
			} else if (accounts.length > 1) {
				// check if there is an account setup as current
				SharedPreferences preferences = context.getSharedPreferences(accountType, Context.MODE_PRIVATE);
				String accountName = preferences.getString(RETROAUTH_ACCOUNTNAME_KEY, null);
				if (accountName != null) {
					for (Account account : accounts) {
						if (accountName.equals(account.name)) return account.name;
					}
				}
			} else {
				return accounts[0].name;
			}
			return showDialog ? showAccountPickerDialog(accountType).toBlocking().first() : null;
		}
	
		/**
		 * {@inheritDoc}
		 */
		@Nullable
		@Override
		public String getTokenFromActiveUser(@NonNull String accountType, @NonNull String tokenType) {
			Account activeAccount = getActiveAccount(accountType, false);
			if (activeAccount == null) return null;
			return accountManager.peekAuthToken(activeAccount, tokenType);
		}
	
		/**
		 * {@inheritDoc}
		 */
		@Nullable
		@Override
		public String getUserData(@NonNull String accountType, @NonNull String key) {
			return accountManager.getUserData(getActiveAccount(accountType, false), key);
		}
	
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void invalidateTokenFromActiveUser(@NonNull String accountType, @NonNull String tokenType) {
			String token = getTokenFromActiveUser(accountType, tokenType);
			if (token == null) return;
			accountManager.invalidateAuthToken(accountType, token);
		}
	
		/**
		 * {@inheritDoc}
		 */
		@Nullable
		@Override
		public Account setActiveAccount(@NonNull String accountName, @NonNull String accountType) {
			SharedPreferences preferences = context.getSharedPreferences(accountType, Context.MODE_PRIVATE);
			preferences.edit().putString(RETROAUTH_ACCOUNTNAME_KEY, accountName).apply();
			return getAccountByName(accountName, accountType);
		}
	
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void resetActiveAccount(@NonNull String accountType) {
			SharedPreferences preferences = context.getSharedPreferences(accountType, Context.MODE_PRIVATE);
			preferences.edit().remove(RETROAUTH_ACCOUNTNAME_KEY).apply();
		}
	
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void addAccount(@NonNull Activity activity, @NonNull String accountType, @Nullable String tokenType) {
			accountManager.addAccount(accountType, tokenType, null, null, activity, null, null);
		}
	
	
		/**
		 * Shows an account picker for the user to choose an account
		 *
		 * @param accountType     Account type of the accounts the user can choose
		 * @param onItemSelected  a listener to get a callback when the user selects on item
		 * @param onOkClicked     a listener for the click on the ok button
		 * @param onCancelClicked a listener for the click on the cancel button
		 * @param canAddAccount   if <code>true</code> the user has the option to add an account
		 * @return the accounts the user chooses from
		 */
		public Account[] showAccountPickerDialog(String accountType, DialogInterface.OnClickListener onItemSelected, DialogInterface.OnClickListener onOkClicked, DialogInterface.OnClickListener onCancelClicked, boolean canAddAccount) {
			final Account[] accounts = accountManager.getAccountsByType(accountType);
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			final ArrayList<String> accountList = new ArrayList<>();
			for (Account account : accounts) {
				accountList.add(account.name);
			}
			if (canAddAccount)
				accountList.add(context.getString(R.string.add_account_button_label));
			builder.setTitle(context.getString(R.string.choose_account_label));
			builder.setSingleChoiceItems(accountList.toArray(new String[accountList.size()]), 0, onItemSelected);
			builder.setPositiveButton(android.R.string.ok, onOkClicked);
			builder.setNegativeButton(android.R.string.cancel, onCancelClicked);
			builder.show();
			return accounts;
		}
	
		/**
		 * Shows an account picker dialog to let the user choose an account
		 *
		 * @param accountType Account type of the accounts the user can choose
		 * @return an observable that emmits a string with the name of the account, the user chose or
		 * <code>null</code> if the current context was not an activity
		 */
		private Observable<String> showAccountPickerDialog(final String accountType) {
			final Account[] accounts = accountManager.getAccountsByType(accountType);
			return Observable.create(new Observable.OnSubscribe<String>() {
				int choosenAccount = 0;
	
				@Override
				public void call(final Subscriber<? super String> subscriber) {
					// make sure the context is an activity. in case of a service
					// this can and should not work
					if (context instanceof Activity) {
						showAccountPickerDialog(accountType,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										choosenAccount = which;
									}
								},
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										if (choosenAccount >= accounts.length) {
											subscriber.onNext(null);
										} else {
											setActiveAccount(accounts[choosenAccount].name, accountType);
											SharedPreferences preferences = context.getSharedPreferences(accountType, Context.MODE_PRIVATE);
											preferences.edit().putString(RETROAUTH_ACCOUNTNAME_KEY, accounts[choosenAccount].name).apply();
											subscriber.onNext(accounts[choosenAccount].name);
										}
										subscriber.onCompleted();
									}
								},
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										subscriber.onError(new OperationCanceledException());
									}
								}, true);
					} else {
						subscriber.onNext(null);
					}
				}
			}).subscribeOn(AndroidScheduler.mainThread()); // dialogs have to run on the main thread
		}
	
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getAuthToken(@Nullable Account account, @NonNull String accountType, @NonNull String tokenType) throws AuthenticationCanceledException {
			try {
				String token;
				Activity activity = (context instanceof Activity) ? (Activity) context : null;
				if (account == null) {
					token = createAccountAndGetToken(activity, accountType, tokenType);
				} else {
					token = getToken(activity, account, tokenType);
				}
				if(token == null)
					throw new OperationCanceledException("user canceled the login!");
				return token;
			} catch (AuthenticatorException | OperationCanceledException | IOException e) {
				throw new AuthenticationCanceledException(e);
			}
		}
	
		private String createAccountAndGetToken(@Nullable Activity activity, @NonNull String accountType, @NonNull String tokenType) throws AuthenticatorException, OperationCanceledException, IOException {
			AccountManagerFuture<Bundle> future = accountManager.addAccount(accountType, tokenType, null, null, activity, null, null);
			Bundle result = future.getResult();
			String accountName = result.getString(AccountManager.KEY_ACCOUNT_NAME);
			if(accountName != null) {
				Account account = new Account(result.getString(AccountManager.KEY_ACCOUNT_NAME), result.getString(AccountManager.KEY_ACCOUNT_TYPE));
				return accountManager.peekAuthToken(account, tokenType);
			}
			return null;
		}
	
		private String getToken(@Nullable Activity activity, @NonNull Account account, @NonNull String tokenType) throws AuthenticatorException, OperationCanceledException, IOException {
			// Clear the interrupted flag
			Thread.interrupted();
			AccountManagerFuture<Bundle> future = accountManager.getAuthToken(account, tokenType, null, activity, null, null);
			Bundle result = future.getResult();
			String token = result.getString(AccountManager.KEY_AUTHTOKEN);
			if(token == null) {
				token = accountManager.peekAuthToken(account, tokenType);
			}
			return token;
		}
	}
