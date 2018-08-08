package comm;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;


/** 
 * Copyright @ 2017 Beijing Beidouht Co. Ltd. 
 * All right reserved. 
 * @author: wanyan 
 * date: 2017-10-29 
 */

public class DisplayGui extends JFrame{
	
	private List<String> history,tmphis;
	private JTextField stf,adr,port;
	private JButton sbt;
//	private JRadioButton lawnums_30,lawnums_50,lawnums_100,other,remote;
	private JRadioButton accmode,fuzzymode,other,remote;
	public  SOLHistory solhis;
	public SOLResult solresult;
	public static SOLLogin sollogin;
	public static SOLStar star;
	public static List<String> range=new ArrayList<String>();
	public static List<Boolean> defselect=new ArrayList<Boolean>();
	
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
//	    final JMenuItem createindex=new JMenuItem("新建索引");
	    final JMenuItem addindex=new JMenuItem("添加索引");
	    final JMenuItem showindex=new JMenuItem("查看索引");
	    JMenuItem synchronizeindex=new JMenuItem("提交索引");
	    JMenuItem downloadindex=new JMenuItem("导入索引");
	    JMenuItem remoteindex=new JMenuItem("查看仓库");
	    final JMenuItem aboutitem=new JMenuItem("关于");
//	    file.add(createindex);
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
		

/*
	    class CreateIndexEvent implements ActionListener{

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				CreateIndex createindex=new CreateIndex();
				
				createindex.dispaly(ipath);
				
				HandleLucene handle=new HandleLucene();

				
				createindex.setEnabled(false);
				
				try {
					long start=System.currentTimeMillis();
					int totalofindex=handle.CreateIndex("D:\\Lucene\\src\\","D:\\Lucene\\index\\");
					long end=System.currentTimeMillis();
				
					stbar.setStatusText("创建检索完毕!"+"耗时："+String.valueOf(end-start)+"ms "+"创建索引条数："+totalofindex);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			createindex.setEnabled(true);	
			
			
			}
	    	
	    }
*/	    

/*	    
	    class AddIndexEvent implements ActionListener{

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				AddIndex addindex=new AddIndex();
				
				addindex.dispaly(ipath);
				
				HandleLucene handle=new HandleLucene();

				
				createindex.setEnabled(false);
				
				try {
					long start=System.currentTimeMillis();
					int totalofindex=handle.CreateIndex("D:\\Lucene\\src\\","D:\\Lucene\\index\\");
					long end=System.currentTimeMillis();
				
					stbar.setStatusText("创建检索完毕!"+"耗时："+String.valueOf(end-start)+"ms "+"创建索引条数："+totalofindex);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			createindex.setEnabled(true);	
			
			
			}
	    	
	    }
*/
//	    addindex.addActionListener(new AddIndexEvent());
/*	    
	    class ShowIndexEvent implements ActionListener{
	    	public void actionPerformed(ActionEvent e) {
	    		// TODO Auto-generated method stub
	    		SOLShowIndex showindex=new ShowIndex();
	    		new SOLShowIndex();
	    		
	    		try {
					showindex.dispaly();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
	    	}
	    	
	    }
*/
	    
/*	    
		final JTextArea sta=new JTextArea(22,100);
		sta.setLineWrap(true);
		sta.setBorder(new LineBorder(new Color(127,157,185),0, false));
		sta.setFont(new Font("宋体",Font.PLAIN,15));
*/

	    solresult=new SOLResult();		//创建搜索结果面板
		
		stf=new JTextField(78);
		stf.setPreferredSize(new Dimension(300,34));
		stf.setFont(new Font("宋体",Font.PLAIN,15));
		
		sbt=new JButton("检索");
		sbt.setPreferredSize(new Dimension(100,34));
		
//		JLabel sll=new JLabel("请选择需要显示的条数：");
//		sll.setFont(new Font("宋体",Font.BOLD,12));
/*		
		ButtonGroup numbg=new ButtonGroup();
		
		lawnums_30=new JRadioButton("1000",true);
		lawnums_30.setLocation(20, 20);
		lawnums_30.setSize(50, 20);
		lawnums_30.setName("1000");
		
		lawnums_50=new JRadioButton("2000");
		lawnums_50.setLocation(20, 50);
		lawnums_50.setSize(50, 20);
		lawnums_50.setName("2000");
		
		lawnums_100=new JRadioButton("3000");
		lawnums_100.setLocation(20, 50);
		lawnums_100.setSize(50, 20);
		lawnums_100.setName("3000");
		
		numbg.add(lawnums_30);
		numbg.add(lawnums_50);
		numbg.add(lawnums_100);
*/		
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
		
		final JRadioButton all=new JRadioButton("所有文档",true);
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
		local.setName("local");
		
		
		remote=new JRadioButton("远程"); 
		remote.setLocation(20, 20);
		remote.setSize(50, 20);
		remote.setName("remote");
		
		sourcebg.add(local);
		sourcebg.add(remote);
		
		adr=new JTextField(19);
		adr.setText("http://");
		adr.setPreferredSize(new Dimension(300,23));
		adr.setEnabled(false);
		adr.setName("dar");
		
		JLabel jl=new JLabel(":");
		jl.setEnabled(false);
		
		port=new JTextField(4);
		port.setPreferredSize(new Dimension(300,23));
		port.setEnabled(false);
		port.setName("port");

		
		solhis=new SOLHistory(this);	//创建搜索历史面板
//		solhis.SetComponent(solresult);		//将搜索结果面板传参给搜索历史面板
//		solhis.SetComponent(star); 		//将状态栏面板传参给搜索历史面板
		history.clear();
/*		
		class SearchEvent implements ActionListener{
			public void actionPerformed(ActionEvent e) {

				HandleLucene handle=new HandleLucene();
//				Map<String,List<Integer>> files=new HashMap<String,List<Integer>>();
				Map<String,List<String[]>> content=new HashMap<String,List<String[]>>();
//				List<String[]> contentoflaw=new ArrayList<String[]>();
				StringBuffer keywords=new StringBuffer();
				int top=1000;
				Date date=new Date(System.currentTimeMillis());
				DateFormat dformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				sbt.setEnabled(false);
				keywords.append(stf.getText());					
				
				try {
					
					if(lawnums_30.isSelected())
						top=1000;
						else if(lawnums_30.isSelected())
						top=2000;
					else if(lawnums_30.isSelected())
						top=3000;
					
					if(all.isSelected())
						range.clear();
					
					if(!keywords.toString().isEmpty()){
						IOList iolist=new IOList();
						String ndate=dformat.format(date);
						iolist.add("Date@"+ndate+" "+keywords.toString(),tmphis);
						iolist.add("Date@"+ndate+" "+keywords.toString(),history);
						solhis.UpdateHistory(tmphis);
						long start=System.currentTimeMillis();
//						files=handle.ExecuteSearch("D:\\Lucene\\index\\",keywords.toString(),top);
						if(range.isEmpty()){
							content=handle.GetSearch(ipath,keywords.toString(),top);
						}
						else{					
//							多条件查询，指定在某个法条文档中查询						
							String[] fields=new String[]{"file","law"};
							content=handle.GetMultipleSearch(ipath,fields,range,keywords.toString(),top);
						}
							if(files==null)
								ste.setText("<i><b>未搜索到关键词</b></i>");
							else{
								contentoflaw=handle.GetContentOfLawByIndex(files,"D:\\Lucene\\src\\");
								if(contentoflaw==null)
									ste.setText("<i><b>未搜索到关键词</b></i>");
								else{
									StringBuffer text=new StringBuffer();
									for(int i=0;i<contentoflaw.size();i++){
										text.append("&emsp&emsp");
										text.append(contentoflaw.get(i)[3]);
										text.append("&emsp"+"<i>"+"--摘录自");
										text.append(contentoflaw.get(i)[0]);
										text.append("&emsp"+contentoflaw.get(i)[1]);
										text.append("&emsp"+contentoflaw.get(i)[2]+"</i>");
										text.append("<br/>");
										text.append("<br/>");
									}
									ste.setText(text.toString());					
								}
							
						long end=System.currentTimeMillis();
						long total=res.UpdateText(content);
//						stbar.setStatusText("检索完毕!"+" "+"耗时："+String.valueOf(end-start)+"ms"+" "+"共搜索到："+total);
						star.setStatusText("检索完毕!"+" "+"耗时："+String.valueOf(end-start)+"ms"+" "+"共搜索到："+total);
						
					}else
						JOptionPane.showMessageDialog(null, "关键词不允许为空", "警告", JOptionPane.ERROR_MESSAGE);

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InvalidTokenOffsetsException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (java.text.ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}					
				sbt.setEnabled(true);		
			}
		}
		sbt.addActionListener(new SearchEvent());
*/

		
		JPanel npaneofnorth=new JPanel();		/*搜索输入框和搜索按钮面板*/
//		npaneofnorth.setBorder(BorderFactory.createLineBorder(Color.red));
	    npaneofnorth.setLayout(new FlowLayout(FlowLayout.CENTER,0,1));
	    npaneofnorth.add(stf);		//添加搜索输入框
	    npaneofnorth.add(sbt);		//添加搜索按钮
		
		JPanel npaneofcenterleft=new JPanel();		/*搜索条数面板*/
		npaneofcenterleft.setBorder(BorderFactory.createTitledBorder("请选择检索方式"));
	    npaneofcenterleft.setLayout(new FlowLayout(FlowLayout.CENTER,5,0));
//	    npaneofcenterleft.add(sll);
//	    npaneofcenterleft.add(lawnums_30);		//添加搜索条数组件
//	    npaneofcenterleft.add(lawnums_50);
//	    npaneofcenterleft.add(lawnums_100);
	    npaneofcenterleft.add(accmode);
	    npaneofcenterleft.add(fuzzymode);
		
	    
		JPanel npaneofcenterright=new JPanel();		/*搜索范围面板*/
		npaneofcenterright.setBorder(BorderFactory.createTitledBorder("请选择查询范围"));
	    npaneofcenterright.setLayout(new FlowLayout(FlowLayout.CENTER,5,0));
//	    npaneofcenterright.add(jlb);		/*添加搜索范围组件*/
	    npaneofcenterright.add(all);
	    npaneofcenterright.add(other);
	    
		JPanel npaneofcenter1=new JPanel();		/*搜索源面板*/
		npaneofcenter1.setBorder(BorderFactory.createTitledBorder("请选择检索源"));
	    npaneofcenter1.setLayout(new FlowLayout(FlowLayout.CENTER,5,0));
	    npaneofcenter1.add(local);
	    npaneofcenter1.add(remote);
	    
		JPanel npaneofcenter2=new JPanel();		/*远程地址面板*/
		npaneofcenter2.setBorder(BorderFactory.createTitledBorder("远程地址"));
		npaneofcenter2.setEnabled(false);
	    npaneofcenter2.setLayout(new FlowLayout(FlowLayout.CENTER,5,0));
	    npaneofcenter2.add(adr);
	    npaneofcenter2.add(jl);
	    npaneofcenter2.add(port);
	    
		local.addMouseListener(new SOLEvents.LocalEvent(npaneofcenter2));
		remote.addMouseListener(new SOLEvents.HostEvent(npaneofcenter2));

	    
		JPanel npaneofcenter=new JPanel();			/*搜索条数、搜索范围、搜索源、远程地址面板*/
		npaneofcenter.setPreferredSize(new Dimension(400,50));
//		npaneofcenter.setBorder(BorderFactory.createTitledBorder("分组框"));
//		npaneofcenter.setBorder(BorderFactory.createLineBorder(Color.red));
	    npaneofcenter.setLayout(new FlowLayout(FlowLayout.LEFT,5,0));
	    npaneofcenter.add(npaneofcenterleft);		//添加搜索条数面板
	    npaneofcenter.add(npaneofcenterright);		//添加搜索范围面板
	    npaneofcenter.add(npaneofcenter1);		//添加搜索源面板
	    npaneofcenter.add(npaneofcenter2);		//添加远程地址面板
	    
	    
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
		
//		createindex.addActionListener(new SOLEvents.ShowSOLCreateIndexEvent());
		addindex.addActionListener(new SOLEvents.ShowSOLAddIndexEvent());
		showindex.addActionListener(new SOLEvents.ShowSOLShowIndexEvent());
		synchronizeindex.addActionListener(new SOLEvents.ShowSOLSynchronizeIndexEvent());
		downloadindex.addActionListener(new SOLEvents.ShowSOLDownloadIndexEvent());
		remoteindex.addActionListener(new SOLEvents.ShowSOLRemoteIndexEvent());
		aboutitem.addActionListener(new SOLEvents.AboutEvent());
		sbt.addMouseListener(new SOLEvents.SearchEvent(this));
		other.addMouseListener(new SOLEvents.ShowSOLSelectIndexEvent());
		remote.addMouseListener(new SOLEvents.RemoteEvent());
		local.addMouseListener(new SOLEvents.UnRemoteEvent());
		this.addWindowListener(new SOLEvents.DisplayGuiColseEvent(this));
		
	    this.setTitle("Searching Of Laws");//窗体标签  
	    this.setSize(WindowSize.X,WindowSize.Y);//窗体大小  
	    this.setLocationRelativeTo(null);//在屏幕中间显示(居中显示)  
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//退出关闭JFrame   
	    this.setVisible(true);//显示窗体
	    this.setResizable(false); //锁定窗体	
	    
		sollogin=new SOLLogin(this);	//创建登录窗口
 	
	}
/*
	private class StatusBar extends JPanel{
		
		private JLabel sll;
		
		StatusBar(Dimension size){
	
			this.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
//			this.setBorder(BorderFactory.createLineBorder(Color.red));
//			this.setBackground(Color.LIGHT_GRAY);
//			this.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
			
			sll=new JLabel();
			sll.setPreferredSize(size);
			
			this.add(sll);
		}
		
		public void setStatusText(String sr){
			sll.setText(sr);
		}
		
		public String getStatusText(){
			return sll.getText();
		}
	}
*/
/*	
	private class About extends JFrame{
		
		About(){
			
			Container contentpane=this.getContentPane();
			contentpane.setLayout(new BorderLayout(3,3));

			JLabel about=new JLabel();
			about.setHorizontalAlignment(JLabel.CENTER);
			about.setFont(new Font("宋体",Font.BOLD,20));
			about.setText("<html>Searching Of Laws <br/><br/> Version: SOL Release (2.3.4) <br/><br/> 作者：王岩</html>");
			
			contentpane.add(about);
			
		    this.setTitle("Searching Of Laws");//窗体标签  
		    this.setSize(FrameSize.X,FrameSize.Y);//窗体大小  
		    this.setLocationRelativeTo(null);//在屏幕中间显示(居中显示)  
		    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//退出关闭JFrame  
		    this.setVisible(true);//显示窗体
		    this.setResizable(false); //锁定窗体	
		}
	}
*/
/*	
	private class CreateIndex extends JFrame{
		
//		private StringBuffer fdir=new StringBuffer();
			
		public void dispaly(String indexpath){
			
			final String ipath=indexpath;
			
			Container contentpane=this.getContentPane();
			contentpane.setLayout(new BorderLayout(3,3));
			
			final JTextField stf=new JTextField(40);
			stf.setPreferredSize(new Dimension(300,35));
			stf.setFont(new Font("宋体",Font.PLAIN,15));
			
			final JButton lbt=new JButton("浏览");
			lbt.setPreferredSize(new Dimension(60,35));
			
			final JButton sbt=new JButton("创建");
			sbt.setPreferredSize(new Dimension(60,35));
			
			
			final StatusBar stbar=new StatusBar(new Dimension(426,20));
			
			class ChooseEvent implements ActionListener{	
				public void actionPerformed(ActionEvent e) {
					
					JFileChooser fcdlg = new JFileChooser();
					fcdlg.setDialogTitle("请选择待搜索文档");
					fcdlg.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int returnval=fcdlg.showOpenDialog(null);
					if(returnval==JFileChooser.APPROVE_OPTION){
						String path=fcdlg.getSelectedFile().getPath();
//						fdir.append(path);
						stf.setText(path);
					}
				}
			}
			lbt.addActionListener(new ChooseEvent());
				
			class CreateIndexEvent implements ActionListener{	
				public void actionPerformed(ActionEvent e) {
								
					HandleLucene handle=new HandleLucene();
					
					String t=stf.getText();

					sbt.setEnabled(false);
					
					if(!t.isEmpty()){
					
						try {
							long start=System.currentTimeMillis();
							int totalofindex=handle.CreateIndex(t,ipath);
							long end=System.currentTimeMillis();
							
							if(totalofindex==-1)
								JOptionPane.showMessageDialog(null, "未找到法条文档或者文档中未发现法条，请先将有法条内容的文档放入该目录下", "警告", JOptionPane.ERROR_MESSAGE);
							else
								stbar.setStatusText("创建检索完毕!"+"耗时："+String.valueOf(end-start)+"ms "+"创建索引条数："+totalofindex);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					else
						JOptionPane.showMessageDialog(null, "请选择要创建索引的文档", "警告", JOptionPane.ERROR_MESSAGE);
						
				sbt.setEnabled(true);	
//				fdir.delete(0,fdir.length());
				}
			}
			sbt.addActionListener(new CreateIndexEvent());
			
			JPanel cpane=new JPanel();
			JPanel spane=new JPanel();
			JPanel npane=new JPanel();
			JPanel npaneofnorth=new JPanel();
			JPanel npaneofsouth=new JPanel();
			
		    cpane.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
		    cpane.add(stf);
		    cpane.add(lbt);
		    cpane.add(sbt);
		    
		    spane.add(stbar);
		    
			contentpane.add(cpane,BorderLayout.CENTER);
			contentpane.add(spane,BorderLayout.SOUTH);
			
		    this.setTitle("Searching Of Laws");//窗体标签  
		    this.setSize(FrameSize.X,FrameSize.Y);//窗体大小  
		    this.setLocationRelativeTo(null);//在屏幕中间显示(居中显示)  
		    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//退出关闭JFrame  
		    this.setVisible(true);//显示窗体
		    this.setResizable(false); //锁定窗体
		}
		
	}
*/
/*	
	private class AddIndex extends JFrame{
		
//		private StringBuffer fdir=new StringBuffer();
			
		public void dispaly(String indexpath){
			
			final String ipath=indexpath;
			
			Container contentpane=this.getContentPane();
			contentpane.setLayout(new BorderLayout(3,3));
			
			final JTextField stf=new JTextField(40);
			stf.setPreferredSize(new Dimension(300,35));
			stf.setFont(new Font("宋体",Font.PLAIN,15));
			
			final JButton lbt=new JButton("浏览");
			lbt.setPreferredSize(new Dimension(60,35));
			
			final JButton sbt=new JButton("添加");
			sbt.setPreferredSize(new Dimension(60,35));
			
			
			final StatusBar stbar=new StatusBar(new Dimension(426,20));
			
			class ChooseEvent implements ActionListener{	
				public void actionPerformed(ActionEvent e) {
					
					JFileChooser fcdlg = new JFileChooser();
					fcdlg.setDialogTitle("请选择待搜索文档");
					fcdlg.setFileSelectionMode(JFileChooser.FILES_ONLY);
					int returnval=fcdlg.showOpenDialog(null);
					if(returnval==JFileChooser.APPROVE_OPTION){
						String path=fcdlg.getSelectedFile().getPath();
//						fdir.append(path);
						stf.setText(path);
					}
				}
			}
			lbt.addActionListener(new ChooseEvent());
			
			class AddIndexEvent implements ActionListener{	
				public void actionPerformed(ActionEvent e) {
								
					HandleLucene handle=new HandleLucene();
					
					String t=stf.getText();

					sbt.setEnabled(false);
					
					if(!t.isEmpty()){
					
						try {
							long start=System.currentTimeMillis();
							int totalofindex=handle.AddIndex(t,ipath);
							long end=System.currentTimeMillis();
					
							stbar.setStatusText("创建检索完毕!"+"耗时："+String.valueOf(end-start)+"ms "+"创建索引条数："+totalofindex);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
					}
					else
						JOptionPane.showMessageDialog(null, "请选择要添加索引的文档", "警告", JOptionPane.ERROR_MESSAGE);
				sbt.setEnabled(true);	
				
				}
			}
			sbt.addActionListener(new AddIndexEvent());
			
			JPanel cpane=new JPanel();
			JPanel spane=new JPanel();
			JPanel npane=new JPanel();
			JPanel npaneofnorth=new JPanel();
			JPanel npaneofsouth=new JPanel();
			
		    cpane.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
		    cpane.add(stf);
		    cpane.add(lbt);
		    cpane.add(sbt);
		    
		    spane.add(stbar);
		    
			contentpane.add(cpane,BorderLayout.CENTER);
			contentpane.add(spane,BorderLayout.SOUTH);
			
		    this.setTitle("Searching Of Laws");//窗体标签  
		    this.setSize(FrameSize.X,FrameSize.Y);//窗体大小  
		    this.setLocationRelativeTo(null);//在屏幕中间显示(居中显示)  
		    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//退出关闭JFrame  
		    this.setVisible(true);//显示窗体
		    this.setResizable(false); //锁定窗体
		  	   	    
		}
		
	}
*/	
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
//					s.append("\""+k+"\"");
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
/*	
	public int GetTop(){
		int top=1000;
		if(lawnums_30.isSelected())
			top=1000;
		if(lawnums_50.isSelected())
			top=2000;
		if(lawnums_100.isSelected())
			top=3000;
		return top;
	}
*/
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
	
	public String GetRemoteAddress(){
		StringBuffer s=new StringBuffer();
		s.append(adr.getText()+":"+port.getText());
		return s.toString();
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
	
	/** 
	 * Copyright @ 2017 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2017-10-29
	 * 
	 * @Modified Date:2017-11-20
	 * 			修复JTabel中添加JCheckBox时，不显示复选框框体的问题
	 */
/*	
	private class ShowIndex extends JFrame{
		
		public void dispaly() throws IOException, ParseException{
			
//			final String ipath=indexpath;
			
			Container contentpane=this.getContentPane();
			contentpane.setLayout(new BorderLayout(3,3));
			
			final HandleLucene handle=new HandleLucene();
			Map<String,Integer> fre=new HashMap<String,Integer>();
			final List<String> file=new ArrayList<String>();
			final Vector columnName = new Vector();
			columnName.add("文件名");  
	        columnName.add("法条总数");
	        columnName.add("是否删除");
	        final Vector rowdata = new Vector();
	        final DefaultTableModel tableModel = new DefaultTableModel();
	        
	        fre=handle.GetTermFreq(Path.indexpath);
	        
	        if(!fre.isEmpty()){
	        	for(Entry<String,Integer> entry: fre.entrySet()){
	        		Vector line=new Vector();
	        		line.add(entry.getKey());
	        		line.add(entry.getValue());
	        		rowdata.add(line);
	        	}
	        }
        
	        tableModel.setDataVector(rowdata,columnName);
	        
			JCheckBox jcb=new JCheckBox();
			jcb.setHorizontalAlignment(SwingConstants.CENTER);
			jcb.setBackground( Color.white);
	        
	        final JTable jt=new JTable(tableModel);
			jt.setRowHeight(35);
			jt.getColumnModel().getColumn(0).setPreferredWidth(266);
			jt.getColumnModel().getColumn(1).setPreferredWidth(85);
			jt.getColumnModel().getColumn(2).setPreferredWidth(85);
			jt.getColumnModel().getColumn(2).setCellEditor(jt.getDefaultEditor(Boolean.class));		//设置checkebox是否可以被选中
			jt.getColumnModel().getColumn(2).setCellRenderer(jt.getDefaultRenderer(Boolean.class));		//设置显示复选框框体
			for(int i=0;i<jt.getRowCount();i++){
				jt.setValueAt(false,i,2);
			}
			
			final JButton lbt=new JButton("删除");
			lbt.setPreferredSize(new Dimension(60,35));
			
			JScrollPane jsp=new JScrollPane();
			jsp.setPreferredSize(new Dimension(436,245));
			jsp.setViewportView(jt);
			
			DefaultTableCellRenderer r = new DefaultTableCellRenderer();    
			r.setHorizontalAlignment(JLabel.CENTER);   
			jt.setDefaultRenderer(Object.class,r);

			class DeleteEvent implements ActionListener{
				public void actionPerformed(ActionEvent e) {
					
					for(int i=0;i<jt.getRowCount();i++){			
						if(((Boolean)jt.getValueAt(i,2)).booleanValue()){
							file.add(jt.getValueAt(i, 0).toString());
						}							
					}
					if(!file.isEmpty()){
						for(int i=0;i<file.size();i++){
							try {
								handle.DeleteIndex(file.get(i),Path.indexpath);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}	
						}
					rowdata.clear();
					Map<String,Integer> fret=new HashMap<String,Integer>();
			        fret=handle.GetTermFreq(Path.indexpath); 
					
					if(!fret.isEmpty()){
						for(String key:fret.keySet()){
							Vector line=new Vector();
							line.add(key);
							line.add(fret.get(key));
							rowdata.add(line);
						}
					}
					tableModel.setDataVector(rowdata,columnName);
					jt.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(new JCheckBox()));
					for(int i=0;i<jt.getRowCount();i++){
						jt.setValueAt(false,i,2);
					}		
					}
					else
						JOptionPane.showMessageDialog(null, "请选择要删除的索引", "警告", JOptionPane.ERROR_MESSAGE);	
				}	
			}
			lbt.addActionListener(new DeleteEvent());
						
			class MouseEvents extends MouseAdapter{
				public void mouseClicked(MouseEvent e){
					if(e.getClickCount() == 1){
						int column=jt.columnAtPoint(e.getPoint()); //获取点击的列
			            if(column!=2){
			            	int row=jt.rowAtPoint(e.getPoint()); //获取点击的行
			            	int top=Integer.valueOf(jt.getValueAt(row,1).toString());
			            	String file=jt.getValueAt(row,0).toString();
			            	ShowLaws showlaws=new ShowLaws();
			            	try {
			            		showlaws.dispaly(file,top);
			            	} catch (IOException e1) {
			            		// TODO Auto-generated catch block
			            		e1.printStackTrace();
			            	} catch (ParseException e1) {
			            		// TODO Auto-generated catch block
			            		e1.printStackTrace();
			            	} catch (InvalidTokenOffsetsException e1) {
			            		// TODO Auto-generated catch block
			            		e1.printStackTrace();
			            	}
						}
					}
				}
			}
			jt.addMouseListener(new MouseEvents());
			
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
		
		
	}
*/
	/** 
	 * Copyright @ 2017 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2017-10-29
	 * 
	 * @Modified Date:2017-11-20
	 * 			修复JTabel中添加JCheckBox时，不显示复选框框体的问题
	 */
/*	
	private class ShowLaws extends JFrame{
		public void dispaly(String file,int top) throws IOException, ParseException, InvalidTokenOffsetsException{
			
//			final String ipath=indexpath;
			
			Container contentpane=this.getContentPane();
			contentpane.setLayout(new BorderLayout(3,3));
			
			final HandleLucene handle=new HandleLucene();
			
			final Vector columname = new Vector();
			columname.add("路径");  
	        columname.add("法条内容");
	        columname.add("是否删除");
	        final Vector rowdata = new Vector();
	        
	        Map<String,List<String[]>> contentoflaw=new HashMap<String,List<String[]>>();
	        
	        contentoflaw=handle.GetTermSearch(Path.indexpath,file,top);
	        List<String[]> laws=contentoflaw.get(file);
	        
	        for(int i=0;i<laws.size();i++){
        		Vector line=new Vector();
        		line.add(laws.get(i)[0]);
        		line.add(laws.get(i)[1]);
        		rowdata.add(line);
	        	
	        }
	        
	        final DefaultTableModel tableModel = new DefaultTableModel();
	        tableModel.setDataVector(rowdata,columname);
	        
	        final JTable jt=new JTable(tableModel);
			jt.setRowHeight(35);
			jt.getColumnModel().getColumn(0).setPreferredWidth(85);
			jt.getColumnModel().getColumn(1).setPreferredWidth(793);
			jt.getColumnModel().getColumn(2).setPreferredWidth(85);
			jt.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(new JCheckBox()));
			jt.getColumnModel().getColumn(2).setCellRenderer(jt.getDefaultRenderer(Boolean.class));
			for(int i=0;i<jt.getRowCount();i++){
				jt.setValueAt(false,i,2);
			}
			
			final JButton lbt=new JButton("删除");
			lbt.setPreferredSize(new Dimension(60,35));
			
			JScrollPane jsp=new JScrollPane();
			jsp.setPreferredSize(new Dimension(963,500));
			jsp.setViewportView(jt);
			
			DefaultTableCellRenderer r = new DefaultTableCellRenderer();    
			r.setHorizontalAlignment(JLabel.CENTER);   
			jt.setDefaultRenderer(Object.class,r);

			class DeleteEvent implements ActionListener{
				public void actionPerformed(ActionEvent e) {
					
					for(int i=0;i<jt.getRowCount();i++){			
						if(((Boolean)jt.getValueAt(i,2)).booleanValue()){
							file.add(jt.getValueAt(i, 0).toString());
						}							
					}
					if(!file.isEmpty()){
						for(int i=0;i<file.size();i++){
							try {
								handle.DeleteIndex(file.get(i),"D:\\Lucene\\index\\");
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}	
						}
					rowdata.clear();
					Map<String,Integer> fret=new HashMap<String,Integer>();
			        fret=handle.GetTermFreq("D:\\Lucene\\index\\"); 
					
					if(!fret.isEmpty()){
						for(String key:fret.keySet()){
							Vector line=new Vector();
							line.add(key);
							line.add(fret.get(key));
							rowdata.add(line);
						}
					}
					tableModel.setDataVector(rowdata,columname);
					jt.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(new JCheckBox()));
					for(int i=0;i<jt.getRowCount();i++){
						jt.setValueAt(false,i,2);
					}		
					}
					else
						JOptionPane.showMessageDialog(null, "请选择要删除的索引", "警告", JOptionPane.ERROR_MESSAGE);	
				}	
			}
			lbt.addActionListener(new DeleteEvent());
						
			JPanel cpane=new JPanel();
		    cpane.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
		    JPanel spane=new JPanel();
			
			
			cpane.add(jsp);
			spane.add(lbt);
			
			contentpane.add(cpane,BorderLayout.CENTER);
			contentpane.add(spane,BorderLayout.SOUTH);
			
		    this.setTitle("Searching Of Laws-"+file);//窗体标签  
		    this.setSize(WindowSize.X,WindowSize.Y);//窗体大小  
		    this.setLocationRelativeTo(null);//在屏幕中间显示(居中显示)  
		    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//退出关闭JFrame  
		    this.setVisible(true);//显示窗体
		    this.setResizable(false); //锁定窗体
		}
		
	}
*/	
	/** 
	 * Copyright @ 2017 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2017-11-18
	 * 
	 * @Modified Date:2017-11-20
	 * 			修复JTabel中添加JCheckBox时，不显示复选框框体的问题
	 */
/*
	private class SelectIndex extends JFrame{

		public void dispaly(String indexpath,final List<String> range,final List<Boolean> defselect) throws IOException, ParseException{
			
			final String ipath=indexpath;
			
			Container contentpane=this.getContentPane();
			contentpane.setLayout(new BorderLayout(3,3));
			
			final HandleLucene handle=new HandleLucene();
			Map<String,Integer> fre=new HashMap<String,Integer>();
			final List<String> file=new ArrayList<String>();
			final Vector columnName = new Vector();
			columnName.add("文件名");  
	        columnName.add("法条总数");
	        columnName.add("是否选择");
	        final Vector rowdata = new Vector();
	        final DefaultTableModel tableModel = new DefaultTableModel();
	        
	        fre=handle.GetTermFreq(ipath);
	        
	        if(!fre.isEmpty()){
	        	for(String key:fre.keySet()){
	        		Vector line=new Vector();
	        		line.add(key);
	        		line.add(fre.get(key));
	        		rowdata.add(line);
	        	}
	        }
        
	        tableModel.setDataVector(rowdata,columnName);
	        
			JCheckBox jcb=new JCheckBox();
			jcb.setHorizontalAlignment(SwingConstants.CENTER);
			jcb.setBackground( Color.white);
	        
	        final JTable jt=new JTable(tableModel);
			jt.setRowHeight(35);
			jt.getColumnModel().getColumn(0).setPreferredWidth(266);
			jt.getColumnModel().getColumn(1).setPreferredWidth(85);
			jt.getColumnModel().getColumn(2).setPreferredWidth(85);
			jt.getColumnModel().getColumn(2).setCellEditor(jt.getDefaultEditor(Boolean.class));
			jt.getColumnModel().getColumn(2).setCellRenderer(jt.getDefaultRenderer(Boolean.class));
			if(defselect.isEmpty()){
				for(int i=0;i<jt.getRowCount();i++){
					jt.setValueAt(true,i,2);
					defselect.add(true);
				}
			}
			else{
				for(int i=0;i<jt.getRowCount();i++){
					jt.setValueAt(defselect.get(i),i,2);
				}
			}
			
			final JButton lbt=new JButton("确定");
			lbt.setPreferredSize(new Dimension(60,35));
			
			final JButton sbt=new JButton("全选");
			sbt.setPreferredSize(new Dimension(60,35));
			
			final JButton sbt1=new JButton("反选");
			sbt1.setPreferredSize(new Dimension(60,35));
			
			JScrollPane jsp=new JScrollPane();
			jsp.setPreferredSize(new Dimension(436,245));
			jsp.setViewportView(jt);
			
			DefaultTableCellRenderer r = new DefaultTableCellRenderer();    
			r.setHorizontalAlignment(JLabel.CENTER);   
			jt.setDefaultRenderer(Object.class,r);

			class ExeEvent extends MouseAdapter{
				public void mouseClicked(MouseEvent e){
					range.clear();
					for(int i=0;i<jt.getRowCount();i++){			
						if(((Boolean)jt.getValueAt(i,2)).booleanValue()){
							range.add(jt.getValueAt(i, 0).toString());	
						}
						defselect.set(i,((Boolean)jt.getValueAt(i,2)).booleanValue());
					}
					SelectIndex.this.dispose();//退出关闭JFrame 
				}	
			}
			lbt.addMouseListener(new ExeEvent());
						
			class SelEvent implements ActionListener{
				public void actionPerformed(ActionEvent e) {
					for(int i=0;i<jt.getRowCount();i++){
						jt.setValueAt(true,i,2);
					}
				}
			}
			sbt.addActionListener(new SelEvent());
			
			class UnselEvent implements ActionListener{
				public void actionPerformed(ActionEvent e) {
					for(int i=0;i<jt.getRowCount();i++){
					
						if(((Boolean)jt.getValueAt(i,2)).booleanValue())
							jt.setValueAt(false,i,2);
						else
							jt.setValueAt(true,i,2);
					}	
				}
			}
			sbt1.addActionListener(new UnselEvent());
			
			JPanel cpane=new JPanel();
		    cpane.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
		    JPanel spane=new JPanel();
		    spane.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
			
			
			cpane.add(jsp);
			spane.add(sbt);
			spane.add(sbt1);
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
	}
*/	
	public static void main(String[] args) throws IOException, java.text.ParseException {
		new DisplayGui();

	}

}