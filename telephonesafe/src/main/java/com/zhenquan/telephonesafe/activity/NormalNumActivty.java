package com.zhenquan.telephonesafe.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.zhenquan.telephonesafe.R;
import com.zhenquan.telephonesafe.adapter.CommonNumAdapter;
import com.zhenquan.telephonesafe.utils.CommonNumberManager;

public class NormalNumActivty extends Activity {

	private ExpandableListView elv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_normal_num_activty);
		
		initUI();
		initData();
	}

	private void initData()  {
		AssetManager assets = getAssets();
		File file = new File(getFilesDir(), "commonnum.db");
		if (!file.exists()) {
			try {
				InputStream inputStream = assets.open("commonnum.db");
				FileOutputStream fos = new FileOutputStream(new File(getFilesDir(),"commonnum.db"));
				int len;
				byte buffer[] = new byte[1024];
				while ((len = inputStream.read(buffer))!=-1) {
					fos.write(buffer,0,len);
				}
				fos.close();
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		CommonNumAdapter cna = new CommonNumAdapter(getApplicationContext());
		elv.setAdapter(cna);
		elv.setOnGroupClickListener(new OnGroupClickListener() {
			int previousPositon = 0;
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				if (previousPositon!=-1&&previousPositon!=groupPosition) {
					elv.collapseGroup(previousPositon);
				}
				
				if (elv.isGroupExpanded(groupPosition)) {
					elv.collapseGroup(groupPosition);
				}else {
					elv.expandGroup(groupPosition);
					previousPositon = groupPosition;
				}
				elv.setSelection(groupPosition);
				return true;
			}
		});
		elv.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				String[] childText = CommonNumberManager.getChildText(getApplicationContext(), groupPosition, childPosition);
				String number = childText[1];
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_DIAL);
				intent.setData(Uri.parse("tel:"+number));
				startActivity(intent);
				return true;
			}
		});
		
	}

	private void initUI() {
		elv = (ExpandableListView) findViewById(R.id.elv);
	}
}
