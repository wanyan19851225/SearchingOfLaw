package Com;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.apache.commons.collections4.map.LinkedMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/** 
 * Copyright @ 2017 Beijing Beidouht Co. Ltd. 
 * All right reserved. 
 * @author: wanyan 
 * date: 2017-10-29 
 */

public class DisplayGui extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<String> history,tmphis;
	private JTextField stf;
	private JButton sbt;
	private JRadioButton accmode,fuzzymode,all,other,remote;
	public SOLHistory solhis;
	public SOLResult solresult;
	public static SOLLogin sollogin;
	public static SOLStar star;
	public static List<String> range=new ArrayList<String>();
	public static List<Boolean> defselect=new ArrayList<Boolean>();
	public static Boolean isver=false;
	/*
	 *
	 * Copyright @ 2017 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2017-10-29 
	 * 
	 * 显示搜索应用的首页
	 *
	 * @params 
	 * 			
	 * @return	   
	 * 						  
	 * 该方法修改于2017.10.29 21:00:00，使用JEditorPane代替JTextArea,能够高亮显示搜索结果的效果
	 * @2017-10-31
	 * 				新增变量int totaloflaws,在状态条中显示搜索结果的记录数
	 * @2017-12-20
	 * 				修改SOLStar为静态类
	 * 				初始化Display时，实例化SOLLogin，默认不显示
	 * @2017-12-21
	 * 				将该方法修改成构造函数
	 * 				修改range\defselect为静态变量
	 * Modified Date:2018-08-08
	 * 				删除创建索引窗口及功能，统一使用SOLAddIndex窗口添加索引
	 * 
	 */
	
	public DisplayGui() throws IOException, java.text.ParseException{
		
		tmphis=new ArrayList<String>();
		IOHistory iohis=new IOHistory();
		history=iohis.HistoryReaderByList(Path.historypath);
		tmphis.addAll(history);
		
		Container contentpane=this.getContentPane();
		contentpane.setLayout(new BorderLayout(0,0));
		
	    JMenuBar menubar=new JMenuBar();
	    JMenu file=new JMenu("文件");
	    JMenu about=new JMenu("关于");
	    final JMenuItem addindex=new JMenuItem("添加索引");
	    final JMenuItem showindex=new JMenuItem("查看索引");
	    JMenuItem synchronizeindex=new JMenuItem("提交索引");
	    JMenuItem downloadindex=new JMenuItem("导入索引");
	    JMenuItem remoteindex=new JMenuItem("查看仓库");
	    final JMenuItem aboutitem=new JMenuItem("关于");
	    file.add(addindex);
	    file.add(showindex);
	    file.add(synchronizeindex);
	    file.add(downloadindex);
	    file.add(remoteindex);
	    about.add(aboutitem);
	    menubar.add(file);
	    menubar.add(about);
	    this.setJMenuBar(menubar);
	    
	   star=new SOLStar(new Dimension(WindowSize.X-213-15,24));		//创建状态栏面板
	   star.SetLoginLabelVisabel(true); //初始化时，显示登录用户标签
	   star.AddShowSOLLLoginEvent(new SOLEvents.ShowSOLLoginEvent());		//初始化时，对登录用户名标签装载ShowSOLLogin鼠标事件
		
	    solresult=new SOLResult();		//创建搜索结果面板
		
		stf=new JTextField(78);
		stf.setPreferredSize(new Dimension(300,34));
		stf.setFont(new Font("宋体",Font.PLAIN,15));
		
		sbt=new JButton("检索");
		sbt.setPreferredSize(new Dimension(100,34));
				
		ButtonGroup modebg=new ButtonGroup();
		accmode=new JRadioButton("精确",true);
		accmode.setLocation(20, 20);
		accmode.setSize(50, 20);
		
		fuzzymode=new JRadioButton("模糊");
		fuzzymode.setLocation(20, 20);
		fuzzymode.setSize(50, 20);
		
		modebg.add(accmode);
		modebg.add(fuzzymode);
		
		ButtonGroup rangebg=new ButtonGroup();
		all=new JRadioButton("所有文档",true);
		all.setLocation(20, 20);
		all.setSize(50, 20);
		all.setName("all");
		
		other=new JRadioButton("自定义文档"); 
		other.setLocation(20, 20);
		other.setSize(50, 20);
		other.setName("other");
		
		rangebg.add(all);
		rangebg.add(other);
		
		ButtonGroup sourcebg=new ButtonGroup();
		final JRadioButton local=new JRadioButton("本地",true);
		local.setLocation(20, 20);
		local.setSize(50, 20);

		remote=new JRadioButton("远程"); 
		remote.setLocation(20, 20);
		remote.setSize(50, 20);

		sourcebg.add(local);
		sourcebg.add(remote);
		
		solhis=new SOLHistory(this);	//创建搜索历史面板
		history.clear();

		JPanel npaneofnorth=new JPanel();		/*搜索输入框和搜索按钮面板*/
//		npaneofnorth.setBorder(BorderFactory.createLineBorder(Color.red));
	    npaneofnorth.setLayout(new FlowLayout(FlowLayout.CENTER,0,1));
	    npaneofnorth.add(stf);		//添加搜索输入框
	    npaneofnorth.add(sbt);		//添加搜索按钮
		
		JPanel npaneofcenterleft=new JPanel();		/*搜索条数面板*/
		npaneofcenterleft.setBorder(BorderFactory.createTitledBorder("请选择检索方式"));
	    npaneofcenterleft.setLayout(new FlowLayout(FlowLayout.CENTER,5,0));
	    npaneofcenterleft.add(accmode);
	    npaneofcenterleft.add(fuzzymode);
		
		JPanel npaneofcenterright=new JPanel();		/*搜索范围面板*/
		npaneofcenterright.setBorder(BorderFactory.createTitledBorder("请选择查询范围"));
	    npaneofcenterright.setLayout(new FlowLayout(FlowLayout.CENTER,5,0));
	    npaneofcenterright.add(all);
	    npaneofcenterright.add(other);
	    
		JPanel npaneofcenter1=new JPanel();		/*搜索源面板*/
		npaneofcenter1.setBorder(BorderFactory.createTitledBorder("请选择检索源"));
	    npaneofcenter1.setLayout(new FlowLayout(FlowLayout.CENTER,5,0));
	    npaneofcenter1.add(local);
	    npaneofcenter1.add(remote);
	    
		JPanel npaneofcenter=new JPanel();			/*搜索条数、搜索范围、搜索源*/
		npaneofcenter.setPreferredSize(new Dimension(400,50));
	    npaneofcenter.setLayout(new FlowLayout(FlowLayout.CENTER,5,0));
	    npaneofcenter.add(npaneofcenterleft);		//添加搜索条数面板
	    npaneofcenter.add(npaneofcenterright);		//添加搜索范围面板
	    npaneofcenter.add(npaneofcenter1);		//添加搜索源面板
//	    npaneofcenter.add(npaneofcenter2);		//添加远程地址面板
	    
		JPanel npane=new JPanel();		/*搜索输入框、搜索按钮、搜索条数、搜索范围面板*/
	    npane.setLayout(new BorderLayout(0,0));
	    npane.add(npaneofnorth,BorderLayout.NORTH);		//添加搜索输入框和搜索按钮面板
		npane.add(npaneofcenter,BorderLayout.CENTER);	//添加搜索条数和搜索范围面板
		
		JPanel searchpanel=new JPanel();			/*搜索面板*/
		searchpanel.setPreferredSize(new Dimension(WindowSize.X-213,588));
//		searchpanel.setBorder(BorderFactory.createLineBorder(Color.red));
		searchpanel.setLayout(new BorderLayout(0,0));
		searchpanel.add(npane,BorderLayout.NORTH);		//添加搜索输入框、搜索按钮、搜索条数、搜索范围面板
		searchpanel.add(solresult,BorderLayout.CENTER);		//添加搜索结果面板
		searchpanel.add(star,BorderLayout.SOUTH);		//添加状态栏面板
		
		contentpane.add(searchpanel,BorderLayout.EAST);
		contentpane.add(solhis,BorderLayout.WEST);
		
		addindex.addActionListener(new SOLEvents.ShowSOLAddIndexEvent());
		showindex.addActionListener(new SOLEvents.ShowSOLShowIndexEvent());
		synchronizeindex.addActionListener(new SOLEvents.ShowSOLSynchronizeIndexEvent());
		downloadindex.addActionListener(new SOLEvents.ShowSOLDownloadIndexEvent());
		remoteindex.addActionListener(new SOLEvents.ShowSOLRemoteIndexEvent());
		aboutitem.addActionListener(new SOLEvents.AboutEvent(this));
		sbt.addMouseListener(new SOLEvents.SearchEvent(this));
		other.addMouseListener(new SOLEvents.ShowSOLSelectIndexEvent());
		remote.addItemListener(new SOLEvents.RemoteEvent(this));
		local.addItemListener(new SOLEvents.LocalEvent(this));
		this.addWindowListener(new SOLEvents.DisplayGuiColseEvent(this));
		
	    this.setTitle("Searching Of Laws");//窗体标签  
	    this.setSize(WindowSize.X,WindowSize.Y);//窗体大小  
	    this.setLocationRelativeTo(null);//在屏幕中间显示(居中显示)  
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//退出关闭JFrame   
	    this.setVisible(true);//显示窗体
	    this.setResizable(false); //锁定窗体	
	    
		sollogin=new SOLLogin(this);	//创建登录窗口
		
		String nv=this.GetVersion(Path.urlpath);		//获取版本信息
		if(!AppInfo.version.equals(nv)&&nv!=null){
			this.SetStatusText("<html><font color=red>您有新版本等待升级！</font></html>");
			isver=true;
		}
	}	
	public void StoreHistory() throws IOException{
		IOHistory iohis=new IOHistory();
        iohis.HistoryWriter(history,Path.historypath);		
	}

	public List<String> LoadHistory(String filename) throws IOException{
		IOHistory iohis=new IOHistory();
		List<String> his=iohis.HistoryReaderByList(filename);
		return his;
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
	
	public void SetStatusText(String s,long total){
		star.setStatusText("检索完毕!"+" "+"耗时："+s+"ms"+" "+"共搜索到："+total);
	}
	public void SetStatusText(String s){
		star.setStatusText(s);
	}
	public Boolean GetFuzzyMode(){
		return fuzzymode.isSelected();
	}
	
	public Boolean GetRage(){
		Boolean f=true;
		if(other.isSelected())
			f=false;
		return f;
	}
	
	public void SetSearchButtonEnable(Boolean f){
		sbt.setEnabled(f);
	}
	
	public void SetRangeButtonEnable(Boolean f){
		all.setEnabled(f);
		other.setEnabled(f);
	}
	
	public Boolean GetIsRemote(){
		Boolean f=false;
		if(remote.isSelected())
			f=true;
		return f;
	}
	
	public List<String> GethTmpHis(){
		return tmphis;
	}
	
	public List<String> GetHistory(){
		return history;
	}
	
	public Map<String,List<String[]>> QueryRemoteSegments(String url,String keywords){
		Map<String,List<String[]>> res=new LinkedMap<String,List<String[]>>();
		JSONObject send=new JSONObject();
		IOHttp http=new IOHttp(url);
		JSONObject response;
		try {
			send.accumulate("command","101");
			send.accumulate("token","");	
			send.accumulate("keywords",keywords);
			
			GZipUntils gzip=new GZipUntils();
			String body = gzip.S2Gzip(send.toString());
			response=http.sendPost(body);
			
			JSONArray results=response.getJSONArray("ResultList");
			JSONObject tem=new JSONObject();
			for(int i=0;i<results.size();i++){
				List<String[]> segments=new ArrayList<String[]>();
				tem=results.getJSONObject(i);
				JSONArray sg=tem.getJSONArray("segments");
				JSONObject tem1=new JSONObject();
				for(int j=0;j<sg.size();j++){
					tem1=sg.getJSONObject(j);
					String[] segment=new String[4];
					segment[0]=tem1.getString("path");
					segment[1]=tem1.getString("segment");
					segment[2]=tem.getString("fpath");
					segment[3]=tem.getString("type");
					segments.add(segment);
				}
				res.put(tem.getString("file"),segments);
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	public String GetVersion(String url){
		String nversion=null;
		JSONObject send=new JSONObject();
		IOHttp http=new IOHttp(url);
		JSONObject response;
		try {
			send.accumulate("command","109");
			send.accumulate("token","");	
			send.accumulate("cversion",AppInfo.version);
			
			GZipUntils gzip=new GZipUntils();
			String body = gzip.S2Gzip(send.toString());
			response=http.sendPost(body);
			nversion=response.getString("nversion");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		return nversion;
	}
}