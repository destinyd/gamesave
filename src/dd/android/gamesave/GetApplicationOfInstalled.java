package dd.android.gamesave;

import java.util.ArrayList;
import java.util.List;

import com.android.test.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class GetApplicationOfInstalled extends Activity implements OnItemClickListener {
    
    private ListView mListView;
    private InstalledPackageAdapter maAdapter;
    private List<PackageInfo> mApps;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.installed_app);
        mListView = (ListView) findViewById(R.id.mylist);
        mListView.setOnItemClickListener(this);
        mApps = loadPackageInfo(this);
        maAdapter = new InstalledPackageAdapter(this, mApps);
        mListView.setAdapter(maAdapter);
    }
    
    private List<PackageInfo> loadPackageInfo(Context context) {
        List<PackageInfo> apps = new ArrayList<PackageInfo>();
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packageList = pm.getInstalledPackages(0);
        for(int i=0; i<packageList.size(); i++) {
            PackageInfo info = packageList.get(i);
            if((info.applicationInfo.flags & info.applicationInfo.FLAG_SYSTEM) <= 0) {
                apps.add(info);
            }
        }
        return apps;
    }

    @Override
    public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        PackageInfo packageInfo = mApps.get(position);
        startActivity(intent);
    }
}
