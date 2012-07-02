package dd.android.GameSaveManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.stericson.RootTools.RootTools;
import com.waps.AdView;
import com.waps.AppConnect;

public class Apps extends Activity implements OnItemClickListener {

	private ListView mListView;
	// private ApplicationAdapter mAdapter;
	private AppAdapter mAdapter;

	// private List<ApplicationInfo> apps;
	private List<PackageInfo> myApps;

	static public String getStaticPath() {
		return Environment.getExternalStorageDirectory().getPath();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.system_app);

		// 方式①：通过AndroidManifest文件读取WAPS_ID和WAPS_PID
		AppConnect.getInstance(this); // 必须确保AndroidManifest文件内配置了WAPS_ID
		// 方式②：通过代码设置WAPS_ID和WAPS_PID
		AppConnect.getInstance("WAPS_ID", "WAPS_PID", this);

		mListView = (ListView) findViewById(R.id.mylist);
		mListView.setOnItemClickListener(this);
		// apps = loadAppInfomation(this);
		myApps = loadPackageInfo(this);

		mAdapter = new AppAdapter(this, myApps);
		mListView.setAdapter(mAdapter);

		File base = new File(getStaticPath() + "/.gamesave/");
		if (!base.exists()) {
			base.mkdirs();
		}
		if (!RootTools.isRootAvailable()) {
			new AlertDialog.Builder(this)
					.setMessage("没有root，操作失败")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialoginterface, int i) {

									finish();
									return;
									// 按钮事件
								}
							}).show();
		}

//		AppConnect.getInstance(this).showOffers(this);
		LinearLayout container = (LinearLayout) findViewById(R.id.AdLinearLayout);
		new AdView(this, container).DisplayAd();

	}

	private List<PackageInfo> loadPackageInfo(Context context) {
		List<PackageInfo> apps = new ArrayList<PackageInfo>();
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> packageList = pm.getInstalledPackages(0);
		for (int i = 0; i < packageList.size(); i++) {
			PackageInfo info = packageList.get(i);
			if ((info.applicationInfo.flags & info.applicationInfo.FLAG_SYSTEM) <= 0) {
				apps.add(info);
			}
		}
		return apps;
	}

	// private List<ApplicationInfo> loadAppInfomation(Context context) {
	// List<ApplicationInfo> apps = new ArrayList<ApplicationInfo>();
	// PackageManager pm = context.getPackageManager();
	// Intent intent = new Intent(Intent.ACTION_MAIN, null);
	// intent.addCategory(Intent.CATEGORY_LAUNCHER);
	// List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
	// Collections.sort(infos, new ResolveInfo.DisplayNameComparator(pm));
	// if (infos != null) {
	// apps.clear();
	// for (int i = 0; i < infos.size(); i++) {
	// ApplicationInfo app = new ApplicationInfo();
	// ResolveInfo info = infos.get(i);
	// app.setName(info.loadLabel(pm).toString());
	// app.setIcon(info.loadIcon(pm));
	// app.setIntent(new ComponentName(info.activityInfo.packageName,
	// info.activityInfo.name));
	// apps.add(app);
	// }
	// }
	// return apps;
	// }

	// @Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(Apps.this, AppController.class);
		// intent.setComponent(apps.get(position).getIntent());

		// intent.putExtra("pName", apps.get(position).getName());
		// intent.putExtra("pPackageName", apps.get(position).getIntent()
		// .getPackageName());
		intent.putExtra("pName", myApps.get(position).applicationInfo
				.loadLabel(this.getPackageManager()));
		intent.putExtra("pPackageName", myApps.get(position).packageName);
		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		AppConnect.getInstance(this).finalize();
		super.onDestroy();
	}
}
