package com.allysonpower.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.allysonpower.parse.NewsInfo;

public class parta extends Activity {
	LinearLayout loadingLayout;
	ImageButton refreshBtn;
	
	private List<NewsInfo> newsList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.parta_home);

		loadingLayout = (LinearLayout) findViewById(R.id.loadingLayout);

		refreshBtn = (ImageButton) findViewById(R.id.refreshBtn);
		refreshBtn.setOnClickListener(new refresh());
		
		
	}

	class refresh implements OnClickListener {

		@Override
		public void onClick(View v) {
			newsList.clear();
			loadingLayout.setVisibility(View.VISIBLE);
			OpenThread();
		}
	}

	//点击更新后 启动的加载数据的线程
	public void OpenThread() {

		new Thread() {
			@Override
			public void run() {
				//Parse load上十条news  (简化问题 Allysonpower启动加载最新的十条news  点击refres按钮后 加载上十条news ..这样refres按钮需要改名字)
				//Parse retrive data code here
				
				//将获取到的news 以此匹配到newsList上  need to do:将newsList改造为匹配Allysonpower数据字段的list..
				for (int i = 0; i < 10; i++) {
					if (newsList == null) {
						newsList = new ArrayList<NewsInfo>();
					}
					NewsInfo newsInfo = new NewsInfo();
					newsInfo.setEventText("Event Text test...");
					newsInfo.setPositive(true);
					
					newsList.add(newsInfo);
				}
				//数据更新后 发送msg 消除在转动的spinner
				//Message message = handler.obtainMessage(0);
				//handler.sendMessage(message); 
			}
		}.start();
	}

	Handler handler = new Handler() {
		public void handleMessage(Message message) {
			if (newsList != null) {
				//WeiBoAdapater adapater = new WeiBoAdapater();
//				ListView Msglist = (ListView) findViewById(R.id.Msglist);
//				Msglist.setOnItemClickListener(new OnItemClickListener() {
//					@Override
//					public void onItemClick(AdapterView<?> arg0, View view,
//							int arg2, long arg3) {
//						Object obj = view.getTag();
//						if (obj != null) {
//							String id = obj.toString();
							//news 详情activity  待实现
//							Intent intent = new Intent(parta.this,
//									ViewActivity.class);
//							Bundle b = new Bundle();
//							b.putString("key", id);
//							intent.putExtras(b);
//							startActivity(intent);
//						}
//					}

//				});

				/*
				 * adapater.notifyDataSetChanged();
				 * Msglist.clearDisappearingChildren();
				 */
//				Msglist.setAdapter(adapater);
//				loadingdingLayout.setVisibility(View.GONE);
			}
		}
	};
//End of load more data
	
//	public class WeiBoAdapater extends BaseAdapter {
//
//		@Override
//		public int getCount() {
//			return newsList.size();
//		}
//
//		@Override
//		public Object getItem(int position) {
//			return newsList.get(position);
//		}
//
//		@Override
//		public long getItemId(int position) {
//			return position;
//		}

//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			convertView = LayoutInflater.from(getApplicationContext()).inflate(
//					R.layout.weibo, null);
//			WeiBoHolder wh = new WeiBoHolder();
//			wh.wbicon = (ImageView) convertView.findViewById(R.id.wbicon);
//			wh.wbtext = (TextView) convertView.findViewById(R.id.wbtext);
//			wh.wbtime = (TextView) convertView.findViewById(R.id.wbtime);
//			wh.wbuser = (TextView) convertView.findViewById(R.id.wbuser);
//			wh.wbimage = (ImageView) convertView.findViewById(R.id.wbimage);
//			NewsInfo wb = newsList.get(position);
//			if (wb != null) {
//				convertView.setTag(wb.getId());
//				wh.wbuser.setText(wb.getUserName());
//				wh.wbtime.setText(wb.getTime());
//				wh.wbtext.setText(wb.getText(), TextView.BufferType.SPANNABLE);
//			}
//
//			return convertView;
//		}
//	}
}
