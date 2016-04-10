package com.download.app;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.download.entities.FileInfo;
import com.download.services.DownloadService;
import com.imooc.DownLoad.R;

public class MainActivity extends Activity {

	public static MainActivity mMainActivity = null;
	private ListView mListView = null;
	private List<FileInfo> mFileInfoList = null;
	private FileListAdapter mAdapter = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mListView = (ListView) findViewById(R.id.lv_downLoad);
        mFileInfoList = new ArrayList<FileInfo>();
        
        // ��ʼ���ļ���Ϣ����
        FileInfo fileInfo = null;
        // Ϊ������ԣ���Tomcat��������
        for (int i = 0; i < 13; i++)
		{
        	fileInfo = new FileInfo(i, 
            		"http://gdown.baidu.com/data/wisegame/355f16d661e9c084/baidushoujizhushou_16788605.apk",
            		"imooc" + i + ".apk", 0, 0);
        	mFileInfoList.add(fileInfo);
		}
        
        mAdapter = new FileListAdapter(this, mFileInfoList);
        mListView.setAdapter(mAdapter);
        
        // ע��㲥������
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadService.ACTION_UPDATE);
        filter.addAction(DownloadService.ACTION_FINISHED);
        registerReceiver(mReceiver, filter);
        
        mMainActivity = this;
    }
    
    protected void onDestroy() 
    {
    	super.onDestroy();
    	unregisterReceiver(mReceiver);
    }
    
    /** 
     * ����UI�Ĺ㲥������
     */
    BroadcastReceiver mReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			if (DownloadService.ACTION_UPDATE.equals(intent.getAction()))
			{
				int finised = intent.getIntExtra("finished", 0);
				int id = intent.getIntExtra("id", 0);
				mAdapter.updateProgress(id, finised);
				Log.i("mReceiver", id + "-finised = " + finised);
			}
			else if (DownloadService.ACTION_FINISHED.equals(intent.getAction()))
			{
				// ���ؽ���
				FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
				mAdapter.updateProgress(fileInfo.getId(), 0);
				Toast.makeText(MainActivity.this, 
						mFileInfoList.get(fileInfo.getId()).getFileName() + "�������", 
						0).show();
			}
		}
	};
	
	/**
	 * �������ؼ�
	 * @see android.app.Activity#onKeyUp(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) 
	{
//		if (KeyEvent.KEYCODE_BACK == keyCode && mStartBtn != null)   // ���˷��ؼ�ʱӦ��ͣ����
		{
//			mStopBtn.performClick();  // ģ�ⰴ����ͣ��ť
		}

		return super.onKeyUp(keyCode, event);
	}
}
