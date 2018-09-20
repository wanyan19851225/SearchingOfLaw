package SOLRemoteIndexs;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;

import Com.DisplayGui;
import Com.FrameSize;
import Com.GZipUntils;
import Com.TableFrame;
import Com.IOHttp;
import Com.IOTable;
import Com.Path;
import Com.SOLEvents;
import Com.UpdateString;
import Com.SOLEvents.DeleteRemoteIndexEvent;
import Com.SOLEvents.FilterEvent;
import Com.SOLEvents.ShowSOLShowRemoteLawsEvent;
import SOLAddIndexs.SOLAddIndex;
import SOLShowIndexs.SOLShowIndex;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class SOLRemoteIndex extends TableFrame{
	
//	public IOTable t;
//	private Vector<Vector<String>> data;
//	private JButton lbt;
//	private JTextField stf;
	private static SOLRemoteIndex single=null;
	
	public SOLRemoteIndex(){
//		Container contentpane=this.getContentPane();
//		contentpane.setLayout(new BorderLayout(3,1));
		
		Vector<String> cname = new Vector<String>();
		cname.add("序号");
		cname.add("文件名");  
        cname.add("法条总数");
        cname.add("作者");
        cname.add("创建时间");
        cname.add("是否删除");
//        data = new Vector<Vector<String>>();
    
        this.BuildIndexs();
       
        t=new IOTable(cname,data);
        t.InitTable(false);
		
//        stf=new JTextField(50);
//		stf.setPreferredSize(new Dimension(300,30));
//		
//		JButton sbt2=new JButton("搜索");
//		sbt2.setPreferredSize(new Dimension(60,30));
		
		lbt.setText("删除");
//		lbt.setPreferredSize(new Dimension(60,30));
//		
//		JButton sbt=new JButton("全选");
//		sbt.setPreferredSize(new Dimension(60,30));
//		
//		JButton sbt1=new JButton("反选");
//		sbt1.setPreferredSize(new Dimension(60,30));
		
		if(DisplayGui.star.GetLoginStatus()){	//判断是否处于登录状态，如果登录状态，则删除按钮启用，否则，删除按钮禁止，修改时间2018-2-1
			if(DisplayGui.star.GetUserName().equals("wangyan"))
				lbt.setEnabled(true);
			else
				lbt.setEnabled(false);
		}
		else
			lbt.setEnabled(false);
		
//		JScrollPane jsp=new JScrollPane();
//		jsp.setPreferredSize(new Dimension(FrameSize.X,FrameSize.Y-100));
		jsp.setViewportView(t);

		
		lbt.addActionListener(new SOLEvents.ConfirmDeleteRemoteIndexEvent(this));
//		sbt.addActionListener(new SOLEvents.SelEvent(this));
//		sbt1.addActionListener(new SOLEvents.UnselEvent(this));
//		sbt2.addActionListener(new SOLEvents.FilterEvent(this));
		t.addMouseListener(new SOLEvents.ShowSOLShowRemoteLawsEvent(this));
		
//		JPanel npane=new JPanel();		//搜索框、搜索按钮面板
//		npane.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
//		JPanel cpane=new JPanel();		//列表面板
//	    cpane.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
//	    JPanel spane=new JPanel();		//全选、反选、删除按钮面板
//	    spane.setLayout(new FlowLayout(FlowLayout.CENTER,5,0));	
//		
//		npane.add(stf);
//		npane.add(sbt2);
//		cpane.add(jsp);
//		spane.add(sbt);
//		spane.add(sbt1);
//		spane.add(lbt);
//		
//		contentpane.add(npane,BorderLayout.NORTH);
//		contentpane.add(cpane,BorderLayout.CENTER);
//		contentpane.add(spane,BorderLayout.SOUTH);
		
	    this.setTitle("查看远程仓库");//窗体标签  
//	    this.setSize(FrameSize.X,FrameSize.Y);//窗体大小  
//	    this.setLocationRelativeTo(null);//在屏幕中间显示(居中显示)  
//	    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//退出关闭JFrame  
//	    this.setVisible(true);//显示窗体
//	    this.setResizable(false); //锁定窗体
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
	
	public void BuildIndexs(){
		data.clear();
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
	}
	
	/*
	 *
	 * Copyright @ 2018 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2018-8-27
	 * 
	 * 使用关键词，检索服务器端的文档信息
	 *
	 * @params urlpath
	 * 				服务器地址
	 * 			keywords
	 * 				关键词
	 * @return Map<Stirng,List<String[]>>
	 * 				将搜索结果以Map<文档名称，[文档作者，创建日期，段落总数，文档名称检索]>的映射关系，返回查询结果		   
	 *
	 */
	public Map<String,String[]> GetRemoteFileInfo(String url,String keywords){
		Map<String,String[]> fre=new HashMap<String,String[]>();
		JSONObject send=new JSONObject();
		IOHttp http=new IOHttp(url);
		JSONObject response;
		try {
			send.accumulate("command","108");
			send.accumulate("token","");		
			send.accumulate("user","");
			send.accumulate("keywords",keywords);
//			System.out.println(send.toString());
			GZipUntils gzip=new GZipUntils();
			String body = gzip.S2Gzip(send.toString());
			response=http.sendPost(body);
		    JSONArray objarry=response.getJSONArray("FileList");
		    JSONObject tem=new JSONObject();
		    for(int i=0;i<objarry.size();i++){		
		    	tem=objarry.getJSONObject(i);
		    	String infos[]=new String[4];
		    	infos[0]=tem.getString("author");
		    	infos[1]=tem.getString("time");
		    	infos[2]=String.valueOf(tem.getInt("segments"));
		    	infos[3]=tem.getString("findex");
		        fre.put(tem.getString("file"),infos); 
		    }
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,e.getMessage(), "警告", JOptionPane.ERROR_MESSAGE);
		}
		return fre;	
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
					tem.accumulate("file",file.get(i).replaceAll("<[^>]+>",""));
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
	
//	public String GetKeywordsInputText(Boolean f){		//参数f判断是否使用模糊搜索方式
//
//		StringBuffer s=new StringBuffer();
//		String[] k=this.InputText2Keywords();
//		if(k!=null){		//判断输入框是否为空
//			if(k.length!=0){
//				if(f)		//使用模糊搜索
//					for(int i=0;i<k.length;i++){
//						if(i==k.length-1)
//							s.append(k[i]);
//						else
//							s.append(k[i]+" AND ");
//					}
//				else		//使用精确搜索
//					for(int i=0;i<k.length;i++){
//						if(i==k.length-1)
//							s.append("\""+k[i]+"\"");
//						else
//							s.append("\""+k[i]+"\""+" AND ");
//						
//					}
//			}
//		}
//		return s.toString();
//	}
//	
//	public String[] InputText2Keywords(){
//		String s=stf.getText().trim();
//		String[] keywords=null;
//		if(!s.isEmpty()){		//判断输入框是否为空
//			UpdateString us=new UpdateString();
//			String fk=us.FilterDoubleString(s," ");
//			keywords=fk.split(" ");
//		}
//		return keywords;
//	}
	
//	public void SelectAll(){
//		int cnum=t.getColumnCount();
//		int rnum=t.getRowCount();
//		for(int i=0;i<rnum;i++){
//			t.setValueAt(true,i,cnum-1);
//		}
//	}
//	
//	public void SelectInvert(){
//		int cnum=t.getColumnCount();
//		int rnum=t.getRowCount();
//		for(int i=0;i<rnum;i++){
//			
//			if(((Boolean)t.getValueAt(i,cnum-1)).booleanValue())
//				t.setValueAt(false,i,cnum-1);
//			else
//				t.setValueAt(true,i,cnum-1);
//		}	
//	}
	
	public static SOLRemoteIndex getInstance(){
		if(single!=null){
			if(!single.isShowing())
				single=new SOLRemoteIndex();			
			else
				single.requestFocus();		
		}
		else
			single=new SOLRemoteIndex();
		return single;	
	}
}
