package dd.android.GameSaveManager;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;


public class GameSaveService extends Service {

	private MyBinder my_binder = new MyBinder();
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Log.d("GameSaveService","onBind");
		return my_binder;
	}
	
	@Override
	public void onRebind(Intent intent) {
		// TODO Auto-generated method stub
		Log.d("GameSaveService","onRebind");
		super.onRebind(intent);
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		Log.d("GameSaveService","onUnbind");
		return super.onUnbind(intent);
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.d("GameSaveService","onCreate");
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.d("GameSaveService","onDestroy");
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent,int start_id) {
		// TODO Auto-generated method stub
		Log.d("GameSaveService","onStart");
		super.onStart(intent, start_id);
	}		
	
	public class MyBinder extends Binder
	{
		GameSaveService getService(){
			return GameSaveService.this;
		}
	}

}
