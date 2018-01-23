package comm;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class SOLDownloadIndex extends JFrame{
	public IOTable t;
	private Vector<Vector<String>> data;
	private JButton lbt;
	
	public SOLDownloadIndex(){
		Container contentpane=this.getContentPane();
		contentpane.setLayout(new BorderLayout(3,3));

		Vector<String> cname = new Vector<String>();
		cname.add("序号");
		cname.add("文件名");  
        cname.add("法条总数");
        cname.add("是否提交");
        data = new Vector<Vector<String>>();
		try {
			Map<String, Integer> rfre = this.GetRemoteIndex(Path.urlpath);
			HandleLucene handle=new HandleLucene();
			Map<String,Integer> lfre=handle.GetTermFreq(Path.indexpath);
		    if(!lfre.isEmpty()&&!rfre.isEmpty()){	
		    	for(String key:lfre.keySet()){
		        	if(rfre.containsKey(key))	
		        		rfre.remove(key);	
		    	}	   
		    }
	        if(!rfre.isEmpty()){
	        	int i=0;
	        	for(Entry<String,Integer> entry: rfre.entrySet()){
	        		Vector<String> line=new Vector<String>();
	        		line.add(String.valueOf(i++));
	        		line.add(entry.getKey());
	        		line.add(String.valueOf(entry.getValue()));
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
		
		lbt=new JButton("提交");
		lbt.setPreferredSize(new Dimension(60,35));
		
		JScrollPane jsp=new JScrollPane();
		jsp.setPreferredSize(new Dimension(FrameSize.X,FrameSize.Y-88));
		jsp.setViewportView(t);

		
		lbt.addActionListener(new SOLEvents.DownloadIndexEvent(this));
		
		//t.addMouseListener(new SOLEvents.ShowSOLShowLawsEvent(this));
		
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
	public Map<String,Integer> GetRemoteIndex(String url) throws Exception{
		Map<String,Integer> fre=new HashMap<String,Integer>();
		Map<String,String> send=new HashMap<String,String>();
		IOHttp http=new IOHttp(url);
		JSONObject data,response;	
		data=new JSONObject();
        data.put("user_name","");

        send.put("command","102");
        send.put("token","");		
        send.put("data",data.toString());
        
        
        JSONObject body=http.Map2Json(send);
		response=http.sendPost(body.toString());
	    JSONArray objarry=response.getJSONArray("FileList");
	    JSONObject tem=new JSONObject();
	    for(int i=0;i<objarry.size();i++){		
	    	tem=objarry.getJSONObject(i);
	        fre.put(tem.getString("file"),tem.getInt("lawnum")); 
	    }
		return fre;	
	}
	
	public int[] CommitIndex(String url,String file,String indexpath) throws Exception{
		int[] res=new int[2];
		HandleLucene handle=new HandleLucene();  
		Map<String,List<String[]>> content=handle.GetTermSearch(Path.indexpath,file);
			if(!content.isEmpty()){
				JSONObject body=new JSONObject();
				JSONObject response=new JSONObject();
				IOHttp http=new IOHttp(url);
				body.accumulate("token","");
				body.accumulate("command","103");
				body.accumulate("user","tmp");
				body.accumulate("file",file);
				List<String[]> laws=content.get(file);
				int count = laws.size();		//传给服务器的法条总数
				res[0]=count;
				body.accumulate("count",count);
		        JSONArray lawslist=new JSONArray();
				for(int i=0;i<count;i++){
					String[] law=laws.get(i);
					JSONObject tem=new JSONObject();
					tem.accumulate("number",i);
					tem.accumulate("path",law[0]);
					tem.accumulate("law",law[1]);
					lawslist.add(tem);
				}
				body.accumulate("lawslist",lawslist);
			//	System.out.println(body.toString());
				response=http.sendPost(body.toString());
				
				int total=response.getInt("result");		//获取服务器写入索引文件成功的法条总数
				res[1]=total;	
			}
			return res;
	}
	
	public boolean DownloadIndex(String url,String file) throws Exception{
		boolean f;
				JSONObject body=new JSONObject();
				JSONObject response=new JSONObject();
				IOHttp http=new IOHttp(url);
				body.accumulate("token","");
				body.accumulate("command","105");
				body.accumulate("user","wangyan");
				//JSONArray fileslist=new JSONArray();
				//for(int i=0;i<file.size();i++){
					//JSONObject tem=new JSONObject();
				body.accumulate("file",file);
					//fileslist.add(tem);
				//}
				//body.accumulate("fileslist",fileslist);
				response=http.sendPost(body.toString());
				
				int res=response.getInt("result");		//获取服务器写入索引文件成功的法条总数
				if(res==1){
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
			        HandleLucene handle=new HandleLucene();
			        int tatol=handle.AddIndex(content,Path.indexpath);
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

	public void SetSearchButtonEnable(Boolean f){
		lbt.setEnabled(f);
	}
	
}
