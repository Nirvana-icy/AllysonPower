package com.allysonpower;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class messge_info {

  //Message publish time
    private String time;
    public String getTime(){
        return time;
    }
    public void setTime(String time)
    {
        this.time=time;
    }
  //Message publish area
    private String area;
    public String getArea(){
        return area;
    }
    public void getArea(String area)
    {
        this.area=area;
    }
	//Message publish user name
    private String userName;
    public String getUserName(){
        return userName;
    }
    public void setUserName(String userName){
        this.userName=userName;
    }
    //Message publish subject
    private String subject;
    public String getSubject(){
        return subject;
    }
    public void getSubject(String subject){
        this.subject=subject;
    }
   //Message publish content
    private String content;
    public String getText(){
        return content;
    }
    public void setText(String content){
        this.content=content;
    }
    

    private List<messge_info> mgList;
    public class MessageAdapter extends BaseAdapter {
    	
    	@Override
    	public int getCount() {
    		// TODO Auto-generated method stub
    		return mgList.size();
    	}
    	
    	@Override
        public Object getItem(int position) {
            return mgList.get(position);
        }
    	
    	@Override
        public long getItemId(int position) {
            return position;
        }
        
    	@Override
        public View getView(int position, View convertView, ViewGroup parent) {
            
//            convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.weibo, null);
//            Poster pr = new Poster();
//            pr.wbicon = (ImageView) convertView.findViewById(R.id.wbicon);
//            pr.wbtext = (TextView) convertView.findViewById(R.id.wbtext);
//            pr.wbtime = (TextView) convertView.findViewById(R.id.wbtime);
//            pr.wbuser = (TextView) convertView.findViewById(R.id.wbuser);
//            pr.wbimage=(ImageView) convertView.findViewById(R.id.wbimage);
//            messge_info mg = mgList.get(position);
//            if(mg!=null){
//                convertView.setTag(mg.getId());
//                pr.wbuser.setText(mg.getUserName());
//                pr.wbtime.setText(mg.getTime());
//                wh.wbtext.setText(mg.getText(), TextView.BufferType.SPANNABLE);
//                textHighlight(wh.wbtext,new char[]{'#'},new char[]{'#'});
//                textHighlight(wh.wbtext,new char[]{'@'},new char[]{':',' '});
//                textHighlight2(wh.wbtext,"http://"," ");
//    	}
    	return convertView;
    	}
    }
}
