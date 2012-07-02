package dd.android.GameSaveManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stericson.RootTools.RootTools;
import com.waps.AdView;
import com.waps.AppConnect;

import dd.android.common.FilesControl;

public class AppController extends Activity implements OnItemClickListener {

	private List<PackageInfo> apps;
	private TextView lName;
	private TextView lPackageName;
	String name, package_name, data_path, zip_path, target_dir_with_ticks;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// if (RootTools.isBusyboxAvailable()) {
		setContentView(R.layout.control_content);
		// } else {
		// RootTools.offerBusyBox(this);
		// }

		Bundle extras = getIntent().getExtras();

		package_name = extras.getString("pPackageName");

		name = extras.getString("pName");
		lName = (TextView) findViewById(R.id.tName);
		lName.setText(name);

		data_path = "/data/data/" + package_name;
		zip_path = Environment.getExternalStorageDirectory().getPath()
				+ "/.gamesave/" + package_name + ".zip";

		// RootTools.debugMode = true;
		// RootTools.log(target_path);

		// if (RootTools.isAccessGiven()) {
		// recover
		// File[] recovers = FilesControl.GetNames(target_path);
		// if (recovers != null && recovers.length > 0) {
		final File zip_file = new File(zip_path);
		Button b1 = (Button) findViewById(R.id.btnRecover);
		if (zip_file.exists()) {
			Long lastTicks = zip_file.lastModified();
			Date date = new Date(lastTicks);

			lPackageName = (TextView) findViewById(R.id.tDatetime);
			lPackageName.setText("最后备份时间：" + date.toLocaleString());
			OnClickListener lis_recover = new OnClickListener() {
				public void onClick(View v) {
					if (RootTools.isRootAvailable()) {
						FilesControl.chmod_rw(data_path);
						File data_dir = new File(data_path);
						try {
							FilesControl.unzip(zip_file, data_dir);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// FilesControl.copy(strLastBackupPath, source_dir +
						// "/");
					}
					finish(); // 关闭当前activity，返回前一个activity
				}
			};
			b1.setOnClickListener(lis_recover);
		} else {
			b1.setVisibility(View.GONE);
		}

		// backup
		OnClickListener lis_backup = new OnClickListener() {
			public void onClick(View v) {

				// target_dir_with_ticks = target_dir + "/"
				// + System.currentTimeMillis() + "/";
				// File target = new File(target_dir + "/");
				// if (!target.exists()) {
				// target.mkdirs();
				// }
				// FilesControl.copy(source_dir, target_dir_with_ticks);

				// new backup
				// FilesControl.zipDirecory( target_dir +
				// ".zip",source_dir);
				FilesControl.chmod_rw(data_path);
				File source_file = new File(data_path);
				File target_file = new File(zip_path);
				Log.d("*** DEBUG ***", "RootTools .copyFile(" + data_path + ","
						+ target_file.getAbsolutePath() + ", true, false);");
				try {
					FilesControl.zipDirectory(source_file, target_file);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				finish(); // 关闭当前activity，返回前一个activity
			}
		};
		Button b = (Button) findViewById(R.id.btnBackup);
		b.setOnClickListener(lis_backup);

		// File is = new File("/etc");
		// FileOutputStream fos;
		// try {
		// fos = new FileOutputStream(new File("ajavauser.dat"));
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// return;
		// }
		// try {
		// GZIPOutputStream gos = new GZIPOutputStream(fos);
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// }
		LinearLayout container = (LinearLayout) findViewById(R.id.AdLinearLayout);
		new AdView(this, container).DisplayAd();
	}

	// @Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// Intent intent = new Intent();
		// intent.setComponent(apps.get(position).getIntent());
		// startActivity(intent);
	}
	
	@Override
	protected void onDestroy() {
		AppConnect.getInstance(this).finalize();
		super.onDestroy();
	}
}
