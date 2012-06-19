package dd.android.gamesave;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.android.test.R;
import com.stericson.RootTools.RootTools;

import dd.android.common.FilesControl;

public class AppController extends Activity implements
		OnItemClickListener {

	private List<ApplicationInfo> apps;
	private TextView lName;
	private TextView lPackageName;
	String name, package_name, source_dir, target_dir, target_dir_with_ticks;

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

		source_dir = "/data/data/" + package_name;
		target_dir = Environment.getExternalStorageDirectory().getPath() + "/.gamesave/" + package_name;

		RootTools.debugMode = true;
		RootTools.log(target_dir);

		if (RootTools.isAccessGiven()) {
			// recover
			File[] recovers = FilesControl.GetNames(target_dir  + "/");
			if (recovers != null && recovers.length > 0) {
				String strLastBackupTicks = recovers[recovers.length - 1]
						.getName();
				RootTools.log(strLastBackupTicks);
				Long lastTicks = new Long(strLastBackupTicks);
				Date date = new Date(lastTicks);

				lPackageName = (TextView) findViewById(R.id.tDatetime);
				lPackageName.setText("最后更新时间" + date.toLocaleString());
				final String strLastBackupPath = recovers[recovers.length - 1]
						.getPath();
				OnClickListener lis_recover = new OnClickListener() {
					public void onClick(View v) {
						if (RootTools.isRootAvailable()) {
							FilesControl.copy(strLastBackupPath, source_dir
									+ "/");
						}
						finish(); // 关闭当前activity，返回前一个activity
					}
				};
				Button b1 = (Button) findViewById(R.id.btnRecover);
				b1.setOnClickListener(lis_recover);
			}

			// backup
			OnClickListener lis_backup = new OnClickListener() {
				public void onClick(View v) {

					target_dir_with_ticks = target_dir  + "/"
							+ System.currentTimeMillis() + "/";
					File target = new File(target_dir + "/");
					if (!target.exists()) {
						target.mkdirs();
					}
					Log.d("*** DEBUG ***", "RootTools .copyFile(" + source_dir
							+ "," + target_dir_with_ticks + ", true, false);");
//					FilesControl.copy(source_dir, target_dir_with_ticks);
					
					//new backup
//					FilesControl.zipDirecory( target_dir + ".zip",source_dir);
					File source_file = new File(source_dir);
					File target_file = new File(target_dir + ".zip");
					try {
						FilesControl.zipDirectory(source_file,target_file);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					
					finish(); // 关闭当前activity，返回前一个activity
				}
			};
			Button b = (Button) findViewById(R.id.btnBackup);
			b.setOnClickListener(lis_backup);

			File is = new File("/etc");
			FileOutputStream fos;
			try {
				fos = new FileOutputStream(new File("ajavauser.dat"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			try {
				GZIPOutputStream gos = new GZIPOutputStream(fos);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// Intent intent = new Intent();
		// intent.setComponent(apps.get(position).getIntent());
		// startActivity(intent);
	}
}
