package SOLDownloadIndexs;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import Com.FileIndexs;
import Com.FrameSize;
import Com.GZipUntils;
import Com.TableFrame;
import Com.HandleLucene;
import Com.IOHttp;
import Com.IOTable;
import Com.Path;
import Com.SOLEvents;
import Com.UpdateString;
import Com.SOLEvents.DownloadIndexEvent;
import Com.SOLEvents.FilterEvent;
import Com.SOLEvents.SelEvent;
import Com.SOLEvents.UnselEvent;
import SOLAddIndexs.SOLAddIndex;
import SOLShowIndexs.SOLShowIndex;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class SOLDownloadIndex extends TableFrame{
//	public IOTable t;
//	private Vector<Vector<String>> data;
//	private JButton lbt;
//	private JTextField stf;
	private static SOLDownloadIndex single=null;
	
	public SOLDownloadIndex(){
//		Container contentpane=this.getContentPane();
//		contentpane.setLayout(new BorderLayout(3,1));

		Vector<String> cname = new Vector<String>();
		cname.add("序号");
		cname.add("文件名");  
        cname.add("法条总数");
        cname.add("作者");
        cname.add("创建时间");
        cname.add("是否导入");
//        data = new Vector<Vector<String>>();
		try {
			Map<String, String[]> rfre = this.GetRemoteIndex(Path.urlpath);
//			HandleLucene handle=new HandleLucene();
			FileIndexs findexs=new FileIndexs();
			Map<String,String[]> lfre=findexs.GetFileInfo(Path.filepath);
		    if(!lfre.isEmpty()&&!rfre.isEmpty()){	
		    	for(String key:lfre.keySet()){
		        	if(rfre.containsKey(key))	
		        		rfre.remove(key);	
		    	}	   
		    }
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
        t.InitTable(true);
		
//        stf=new JTextField(50);
//		stf.setPreferredSize(new Dimension(300,30));
//		JButton sbt2=new JButton("搜索");
//		sbt2.setPreferredSize(new Dimension(60,30));
//		lbt=new JButton("导入");
//		lbt.setPreferredSize(new Dimension(60,30));
//		JButton sbt=new JButton("全选");
//		sbt.setPreferredSize(new Dimension(60,30));
//		JButton sbt1=new JButton("反选");
//		sbt1.setPreferredSize(new Dimension(60,30));
//		JScrollPane jsp=new JScrollPane();
//		jsp.setPreferredSize(new Dimension(FrameSize.X,FrameSize.Y-100));
        lbt.setText("导入");
		jsp.setViewportView(t);

		lbt.addActionListener(new SOLEvents.ConfirmDownloadIndexEvent(this));
//		sbt.addActionListener(new SOLEvents.SelEvent(this));
//		sbt1.addActionListener(new SOLEvents.UnselEvent(this));
//		sbt2.addActionListener(new SOLEvents.FilterEvent(this));
		//t.addMouseListener(new SOLEvents.ShowSOLShowLawsEvent(this));
		
//		JPanel npane=new JPanel();		//搜索框、搜索按钮面板
//	    npane.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
//		JPanel cpane=new JPanel();		//列表面板
//	    cpane.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
//	    JPanel spane=new JPanel();		//全选，反选，删除按钮面板
//	    spane.setLayout(new FlowLayout(FlowLayout.CENTER,5,0));
		
//		npane.add(stf);
//		npane.add(sbt2);
//		cpane.add(jsp);
//		spane.add(sbt);
//		spane.add(sbt1);
//		spane.add(lbt);
		
//		contentpane.add(npane,BorderLayout.NORTH);
//		contentpane.add(cpane,BorderLayout.CENTER);
//		contentpane.add(spane,BorderLayout.SOUTH);
		
	    this.setTitle("导入文件");//窗体标签  
//	    this.setSize(FrameSize.X,FrameSize.Y);//窗体大小  
//	    this.setLocationRelativeTo(null);//在屏幕中间显示(居中显示)  
//	    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//退出关闭JFrame  
//	    this.setVisible(true);//显示窗体
//	    this.setResizable(false); //锁定窗体

}
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
	
	/*
	 *
	 * Copyright @ 2018 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2018-8-23 
	 * 
	 * 使用关键词，在指定范围内，检索服务器端的文档信息
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
		JSONArray list=new JSONArray();
		try {
			send.accumulate("command","107");
			send.accumulate("token","");		
			send.accumulate("user","");
			send.accumulate("keywords",keywords);
			List<String> file=this.GetFiles();
			if(file.isEmpty())
				send.accumulate("FileList",list);
			else{
				for(int i=0;i<file.size();i++){
					JSONObject tem=new JSONObject();
					tem.accumulate("fname",file.get(i));
					list.add(tem);
				}
				send.accumulate("FileList",list);
			}
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
	
	public boolean DownloadIndex(String url,String file,int j,int fn) throws Exception{
		boolean f;
				JSONObject body=new JSONObject();
				JSONObject response=new JSONObject();
				IOHttp http=new IOHttp(url);
				body.accumulate("token","");
				body.accumulate("command","105");
				body.accumulate("user","wangyan");
				body.accumulate("file",file);
				
				GZipUntils gzip=new GZipUntils();
				String sends=gzip.S2Gzip(body.toString());
				response=http.sendPost(sends);
				
				int res=response.getInt("count");		
				if(res>0){		//判断段落总数
			        JSONArray objarry=response.getJSONArray("lawslist");
			        JSONObject tem=new JSONObject();

			        List<String[]> laws=new ArrayList<String[]>();
			        for(int i=0;i<objarry.size();i++){		
			        	tem=objarry.getJSONObject(i);
				        String[] law=new String[2];
			        	law[0]=tem.getString("path");
			        	law[1]=tem.getString("law");
			        	laws.add(law);
			        }
			        Map<String,List<String[]>> content=new HashMap<String,List<String[]>>();
			        content.put(response.getString("file"),laws);
			        String[] finfos=new String[2];
			        finfos[0]=response.getString("fpath");
			        finfos[1]=response.getString("type");
			        HandleLucene handle=new HandleLucene();
			        handle.AddIndexs(content,finfos,Path.indexpath,Path.filepath,j,fn);
					f=true;
				}
				else
					f=false;
			return f;
	}
	
	public void RemoveData(Vector<String> obj){
		data.remove(obj);
	}
	
	public Vector<Vector<String>> GetData(){
		return data;
	}
	
	public List<String> GetFiles(){
		List<String> file=new ArrayList<String>();
		int n=data.size();
		for(int i=0;i<n;i++){
			String fname=data.elementAt(i).elementAt(1);
			file.add(fname);
		}
		return file;
	}

	public void SetSearchButtonEnable(Boolean f){
		lbt.setEnabled(f);
	}
	
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
	
	public static SOLDownloadIndex getInstance(){
		if(single!=null){
			if(!single.isShowing())
				single=new SOLDownloadIndex();			
			else
				single.requestFocus();		
		}
		else
			single=new SOLDownloadIndex();
		return single;	
	}
}
