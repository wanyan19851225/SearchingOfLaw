package comm;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class SOLRemoteIndex extends JFrame{
	
	public IOTable t;
	private Vector<Vector<String>> data;
	private JButton lbt;
	
	public SOLRemoteIndex(){
		Container contentpane=this.getContentPane();
		contentpane.setLayout(new BorderLayout(3,3));
		
		Vector<String> cname = new Vector<String>();
		cname.add("序号");
		cname.add("文件名");  
        cname.add("法条总数");
        cname.add("作者");
        cname.add("创建时间");
        cname.add("是否删除");
        data = new Vector<Vector<String>>();
    
		try {
			Map<String, String[]> rfre = this.GetRemoteIndex(Path.urlpath);
	        if(!rfre.isEmpty()){
	        	int i=0;
	        	for(Entry<String,String[]> entry: rfre.entrySet()){
	        		Vector<String> line=new Vector<String>();
	        		line.add(String.valueOf(i++));
	        		line.add(entry.getKey());
	        		String[] info=entry.getValue();
	        		line.add(info[2]);
	        		line.add(info[0]);
	        		line.add(info[1]);
	        		data.add(line);
	        	}
	        }
		} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JOptionPane.showMessageDialog(null,e.getMessage(), "警告", JOptionPane.ERROR_MESSAGE);
		}
        
        t=new IOTable(cname,data);
        t.InitTable(false);
		
		lbt=new JButton("删除");
		lbt.setPreferredSize(new Dimension(60,35));
		if(DisplayGui.star.GetLoginStatus()){	//判断是否处于登录状态，如果登录状态，则删除按钮启用，否则，删除按钮禁止，修改时间2018-2-1
			if(DisplayGui.star.GetUserName().equals("wangyan"))
				lbt.setEnabled(true);
			else
				lbt.setEnabled(false);
		}
		else
			lbt.setEnabled(false);
		
		JScrollPane jsp=new JScrollPane();
		jsp.setPreferredSize(new Dimension(FrameSize.X,FrameSize.Y-88));
		jsp.setViewportView(t);

		
		lbt.addActionListener(new SOLEvents.DeleteRemoteIndexEvent(this));
		
		t.addMouseListener(new SOLEvents.ShowSOLShowRemoteLawsEvent(this));
		
		JPanel cpane=new JPanel();
	    cpane.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
	    JPanel spane=new JPanel();
		
		
		cpane.add(jsp);
		spane.add(lbt);
		
		contentpane.add(cpane,BorderLayout.CENTER);
		contentpane.add(spane,BorderLayout.SOUTH);
		
	    this.setTitle("Searching Of Laws");//窗体标签  
	    this.setSize(FrameSize.X,FrameSize.Y);//窗体大小  
	    this.setLocationRelativeTo(null);//在屏幕中间显示(居中显示)  
	    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//退出关闭JFrame  
	    this.setVisible(true);//显示窗体
	    this.setResizable(false); //锁定窗体
	}
	
	public void RemoveData(Vector<String> obj){
		data.remove(obj);
	}

	public Vector<Vector<String>> GetData(){
		return data;
	}
	
	public void SetSearchButtonEnable(Boolean f){
		lbt.setEnabled(f);
	} 
	
	/*
	 *
	 * Copyright @ 2017 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2017-11-1 
	 * 
	 * GetRemoteIndex方法用于向服务器请求获取文档信息包括文档名称，作者，创建日期，法条总数
	 *
	 * @params url 
	 * 				服务器地址
	 * 				
	 * @return Map<String,String[]>
	 * 				检索结果以Map<文档名称，[作者，创建日期，法条总数]>的形式返回
	 * 
	 * Modeified Date:2018-7-26
	 * 				修改方法返回结果，将文件信息以Map<文档名称，[作者，创建日期，法条总数]>的形式返回 			
	 * 
	 */
	
	public Map<String,String[]> GetRemoteIndex(String url) throws Exception{
		Map<String,String[]> fre=new HashMap<String,String[]>();
		Map<String,String> send=new HashMap<String,String>();
		IOHttp http=new IOHttp(url);
		JSONObject data,response;	
		data=new JSONObject();
        data.put("user_name","");

        send.put("command","102");
        send.put("token","");		
        send.put("data",data.toString());
        

        JSONObject body=http.Map2Json(send);
		GZipUntils gzip=new GZipUntils();
		String sends=gzip.S2Gzip(body.toString());
		response=http.sendPost(sends);
	    JSONArray objarry=response.getJSONArray("FileList");
	    JSONObject tem=new JSONObject();
	    for(int i=0;i<objarry.size();i++){		
	    	tem=objarry.getJSONObject(i);
	    	String info[]=new String[3];
	    	info[0]=tem.getString("author");
	    	info[1]=tem.getString("time");
	    	info[2]=String.valueOf(tem.getInt("lawnum"));
	        fre.put(tem.getString("file"),info); 
	    }
		return fre;	
	}
	
	public boolean DeleteRemoteIndex(String url,List<String> file) throws Exception{
		boolean f;
		String user=DisplayGui.star.GetUserName();
				JSONObject body=new JSONObject();
				JSONObject response=new JSONObject();
				IOHttp http=new IOHttp(url);
				body.accumulate("token","");
				body.accumulate("command","104");
				body.accumulate("user",user);
				JSONArray fileslist=new JSONArray();
				for(int i=0;i<file.size();i++){
					JSONObject tem=new JSONObject();
					tem.accumulate("file",file.get(i));
					fileslist.add(tem);
				}
				body.accumulate("fileslist",fileslist);
				GZipUntils gzip=new GZipUntils();
				String sends=gzip.S2Gzip(body.toString());
				response=http.sendPost(sends);
				
				int res=response.getInt("result");		//获取服务器写入索引文件成功的法条总数
				if(res==1)
					f=true;
				else
					f=false;
			return f;
	}
	
	
}
