package comm;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.IOException;
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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class SOLCommitIndex extends JFrame{
	public IOTable t;
	private Vector<Vector<String>> data;
	private JButton lbt;
	private JTextField stf;
	
	public SOLCommitIndex(){
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
//			HandleLucene handle=new HandleLucene();
			FileIndexs findexs=new FileIndexs();
			Map<String,String[]> lfre=findexs.GetFileInfo(Path.filepath);
		    if(!lfre.isEmpty()&&!rfre.isEmpty()){	
		    	for(String key:rfre.keySet()){
		        	if(lfre.containsKey(key))	
		        		lfre.remove(key);	
		    	}	   
		    }
	        if(!lfre.isEmpty()){
	        	int i=0;
	        	for(Entry<String,String[]> entry: lfre.entrySet()){
	        		Vector<String> line=new Vector<String>();
	        		line.add(String.valueOf(i++));
	        		line.add(entry.getKey());
	        		line.add(entry.getValue()[2]);
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
		
        stf=new JTextField(50);
		stf.setPreferredSize(new Dimension(300,30));
		JButton sbt2=new JButton("搜索");
		sbt2.setPreferredSize(new Dimension(60,30));
        lbt=new JButton("提交");
		lbt.setPreferredSize(new Dimension(60,30));
		
		if(DisplayGui.star.GetLoginStatus())	//判断是否处于登录状态，如果登录状态，则提交按钮启用，否则，提交按钮禁止，修改时间2018-1-31
			lbt.setEnabled(true);
		else
			lbt.setEnabled(false);
		
		JButton sbt=new JButton("全选");
		sbt.setPreferredSize(new Dimension(60,30));
		
		JButton sbt1=new JButton("反选");
		sbt1.setPreferredSize(new Dimension(60,30));
		
		JScrollPane jsp=new JScrollPane();
		jsp.setPreferredSize(new Dimension(FrameSize.X,FrameSize.Y-88));
		jsp.setViewportView(t);

		
		lbt.addActionListener(new SOLEvents.ConfirmCommitIndexEvent(this));
		sbt.addActionListener(new SOLEvents.SelEvent(this));
		sbt1.addActionListener(new SOLEvents.UnselEvent(this));
		sbt2.addActionListener(new SOLEvents.FilterEvent(this));
//		t.addMouseListener(new SOLEvents.ShowSOLShowLawsEvent(this));
		
		JPanel npane=new JPanel();		//搜索框、搜索按钮面板
	    npane.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
		JPanel cpane=new JPanel();		//列表面板
	    cpane.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
	    JPanel spane=new JPanel();		//全选，反选，删除按钮面板
	    spane.setLayout(new FlowLayout(FlowLayout.CENTER,5,0));
		
		npane.add(stf);
		npane.add(sbt2);
	    cpane.add(jsp);
		spane.add(sbt);
		spane.add(sbt1);
		spane.add(lbt);
		
		contentpane.add(npane,BorderLayout.NORTH);
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
		GZipUntils gzip=new GZipUntils();
		String sends=gzip.S2Gzip(body.toString());
		response=http.sendPost(sends);
	    JSONArray objarry=response.getJSONArray("FileList");
	    JSONObject tem=new JSONObject();
	    for(int i=0;i<objarry.size();i++){		
	    	tem=objarry.getJSONObject(i);
	        fre.put(tem.getString("file"),tem.getInt("lawnum")); 
	    }
		return fre;	
	}
	
	public int[] CommitIndex(String url,String file,String indexpath) throws Exception{
		int[] res={-1,-1};
		String user=DisplayGui.star.GetUserName();	//获取登录用户名
		HandleLucene handle=new HandleLucene();  
		Map<String,List<String[]>> content=handle.GetAllSegments(Path.indexpath,file);
			if(!content.isEmpty()){
				FileIndexs findexs=new FileIndexs();
				Map<String,String[]> finfo=new HashMap<String,String[]>();
				for(String keywords : content.keySet()){
					finfo=findexs.QueryFiles(Path.filepath,"\""+keywords+"\"");
				}
				JSONObject body=new JSONObject();
				body.accumulate("token","");
				body.accumulate("command","103");
				body.accumulate("user",user);
				body.accumulate("file",file);
				for(String[] v : finfo.values()){
					body.accumulate("fpath",v[4]);
					body.accumulate("type",v[5]);
				}
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
				GZipUntils gzip=new GZipUntils();
				String sends=gzip.S2Gzip(body.toString());
				JSONObject response=new JSONObject();
				IOHttp http=new IOHttp(url);
				response=http.sendPost(sends);
				
				int total=response.getInt("result");		//获取服务器写入索引文件成功的法条总数
				res[1]=total;	
			}
			return res;
	}
	
	public Map<String,String[]> GetFileInfo(String url,String keywords){
		Map<String,String[]> fre=new HashMap<String,String[]>();
		try {
			List<String> file=this.GetFiles();
			if(file.isEmpty())
				return fre;
			else{
				FileIndexs findexs=new FileIndexs();
				String[] fields={"file","fname"};
				fre=findexs.QueryFiles(url, fields, file, keywords);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,e.getMessage(), "警告", JOptionPane.ERROR_MESSAGE);
		}
		return fre;	
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
	
	public void SelectAll(){
		int cnum=t.getColumnCount();
		int rnum=t.getRowCount();
		for(int i=0;i<rnum;i++){
			t.setValueAt(true,i,cnum-1);
		}
	}
	
	public void SelectInvert(){
		int cnum=t.getColumnCount();
		int rnum=t.getRowCount();
		for(int i=0;i<rnum;i++){
			
			if(((Boolean)t.getValueAt(i,cnum-1)).booleanValue())
				t.setValueAt(false,i,cnum-1);
			else
				t.setValueAt(true,i,cnum-1);
		}	
	}
	
	public String GetKeywordsInputText(Boolean f){		//参数f判断是否使用模糊搜索方式

		StringBuffer s=new StringBuffer();
		String[] k=this.InputText2Keywords();
		if(k!=null){		//判断输入框是否为空
			if(k.length!=0){
				if(f)		//使用模糊搜索
					for(int i=0;i<k.length;i++){
						if(i==k.length-1)
							s.append(k[i]);
						else
							s.append(k[i]+" AND ");
					}
				else		//使用精确搜索
					for(int i=0;i<k.length;i++){
						if(i==k.length-1)
							s.append("\""+k[i]+"\"");
						else
							s.append("\""+k[i]+"\""+" AND ");
						
					}
			}
		}
		return s.toString();
	}
	
	public String[] InputText2Keywords(){
		String s=stf.getText().trim();
		String[] keywords=null;
		if(!s.isEmpty()){		//判断输入框是否为空
			UpdateString us=new UpdateString();
			String fk=us.FilterDoubleString(s," ");
			keywords=fk.split(" ");
		}
		return keywords;
	}
}
