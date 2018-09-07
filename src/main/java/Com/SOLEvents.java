package Com;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Demo.Demo;
import SOLAbout.SOLAbout;
import SOLAbout.SOLShowUpdate;
import SOLAddIndexs.SOLAddIndex;
import SOLAddIndexs.SOLAddIndexsProgress;
import SOLCommitIndexs.SOLCommitIndex;
import SOLCommitIndexs.SOLCommitIndexsProgress;
import SOLCommitIndexs.SOLShowConfirmCommitIndexs;
import SOLDownloadIndexs.SOLDownloadIndex;
import SOLDownloadIndexs.SOLDownloadIndexsProgress;
import SOLDownloadIndexs.SOLShowConfirmDownloadIndexs;
import SOLRemoteIndexs.SOLRemoteIndex;
import SOLRemoteIndexs.SOLShowConfirmDeleteRemoteIndexs;
import SOLRemoteIndexs.SOLShowRemoteLaws;
import SOLShowIndexs.SOLDeleteIndexsProgress;
import SOLShowIndexs.SOLShowConfirmDeleteIndexs;
import SOLShowIndexs.SOLShowIndex;
import SOLShowIndexs.SOLShowLaws;



public class  SOLEvents {
	
	public static class RemoteEvent implements ItemListener{

		private DisplayGui p;
		
		public RemoteEvent(DisplayGui p){
			this.p=p;
		}
		
		public void itemStateChanged(ItemEvent e) {
			// TODO Auto-generated method stub
			this.p.SetRangeButtonEnable(false);
			DisplayGui.star.SetRemoteStatus(true);
			DisplayGui.star.SetRemoteMarkVisable(true);
		}
	}
	
	public static class LocalEvent implements ItemListener{

		private DisplayGui p;
		
		public LocalEvent(DisplayGui p){
			this.p=p;
		}
		
		public void itemStateChanged(ItemEvent e) {
			// TODO Auto-generated method stub
			this.p.SetRangeButtonEnable(true);
			DisplayGui.star.SetRemoteStatus(false);
			DisplayGui.star.SetRemoteMarkVisable(false);
		}	
	}

	public static class ExeEvent extends MouseAdapter{
		
		SOLSelectIndex p;
		
		public ExeEvent(SOLSelectIndex p){
			this.p=p;
			
		}
		public void mouseClicked(MouseEvent e){			
			p.SetRange();
			p.dispose();//退出关闭SOLSelectIndex窗口 
		}	
	} 

	public static class SelEvent implements ActionListener{
		Object p;
		public SelEvent(Object p){
			this.p=p;
		}
		
		public void actionPerformed(ActionEvent e) {
			if(p instanceof SOLSelectIndex) {
				SOLSelectIndex dd=(SOLSelectIndex)this.p;
				dd.SelectAll();
			}
			else if(p instanceof SOLShowIndex) {
				SOLShowIndex dd=(SOLShowIndex)this.p;
				dd.SelectAll();
			}
			else if(p instanceof SOLCommitIndex) {
				SOLCommitIndex dd=(SOLCommitIndex)this.p;
				dd.SelectAll();
			}
			else if(p instanceof SOLDownloadIndex) {
				SOLDownloadIndex dd=(SOLDownloadIndex)this.p;
				dd.SelectAll();
			}
			else if(p instanceof SOLRemoteIndex) {
				SOLRemoteIndex dd=(SOLRemoteIndex)this.p;
				dd.SelectAll();
			}
		}
	}

	public static class UnselEvent implements ActionListener{
		Object p;
		public UnselEvent(Object p){
			this.p=p;
		}
		public void actionPerformed(ActionEvent e) {
			if(p instanceof SOLSelectIndex) {
				SOLSelectIndex dd=(SOLSelectIndex)this.p;
				dd.SelectInvert();
			}
			else if(p instanceof SOLShowIndex) {
				SOLShowIndex dd=(SOLShowIndex)this.p;
				dd.SelectInvert();
			}
			else if(p instanceof SOLCommitIndex) {
				SOLCommitIndex dd=(SOLCommitIndex)this.p;
				dd.SelectInvert();
			}
			else if(p instanceof SOLDownloadIndex) {
				SOLDownloadIndex dd=(SOLDownloadIndex)this.p;
				dd.SelectInvert();
			}
			else if(p instanceof SOLRemoteIndex) {
				SOLRemoteIndex dd=(SOLRemoteIndex)this.p;
				dd.SelectInvert();
			}
		}
	}
	
	public static class ShowSOLSelectIndexEvent extends MouseAdapter{
		
		public void mouseClicked(MouseEvent e){
			new SOLSelectIndex();
		}	
	}
	
	public static class AboutEvent implements ActionListener{

		private DisplayGui p;
		
		public AboutEvent(DisplayGui p){
			this.p=p;
		}
		
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			new SOLAbout(this.p);
		}		
	}

	public static class SearchEvent extends MouseAdapter{
		
		DisplayGui p;

		public SearchEvent(DisplayGui p){
			this.p=p;		
		}
		
		public void mouseClicked(MouseEvent e){
			String keywords=p.GetKeywordsInputText(p.GetFuzzyMode());
			Date date=new Date(System.currentTimeMillis());
			DateFormat dformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			p.SetSearchButtonEnable(false);
			
			try {
				if(p.GetRage())
					DisplayGui.range.clear();
				
				if(!keywords.isEmpty()){
					IOList iolist=new IOList();
					String ndate=dformat.format(date);
					iolist.add("Date@"+ndate+" "+keywords,p.GethTmpHis());
					iolist.add("Date@"+ndate+" "+keywords,p.GetHistory());
					p.solhis.UpdateHistory(p.GethTmpHis());
					SOLSearchIndexsProgress pb=new SOLSearchIndexsProgress(p,keywords);
					pb.execute();
				}else
					JOptionPane.showMessageDialog(null, "关键词不允许为空", "警告", JOptionPane.ERROR_MESSAGE);
			} catch (java.text.ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}					
			p.SetSearchButtonEnable(true);
		}		
	}
	
//	public static class RemoteEvent extends MouseAdapter{
//	
//		public void mouseClicked(MouseEvent e){
//			DisplayGui.star.SetRemoteStatus(true);
//			DisplayGui.star.SetRemoteMarkVisable(true);
//		}
//		
//	}
	
//	public static class UnRemoteEvent extends MouseAdapter{
//		
//		public void mouseClicked(MouseEvent e){
//			DisplayGui.star.SetRemoteStatus(false);
//			DisplayGui.star.SetRemoteMarkVisable(false);
//		}
//		
//	}
	
	/** 
	 * Copyright @ 2017 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2017-12-13 
	 * desc:窗口SOLLogin中的登录按钮_lt的鼠标事件调用该类，进行登录操作
	 * 
	 * 2017-12-21
	 * 			优化登陆时判断用户名和密码的代码结构
	 * 2018-2-1
	 * 			增加登录成功后，设置star类中的username字段，存储用户名
	 * 			
	 */
	
	public static class LoginEvent extends MouseAdapter{

		private SOLLogin sollogin;
		
		public LoginEvent(SOLLogin sollogin){
			this.sollogin=sollogin;
		}
		public void mouseClicked(MouseEvent e){
			
			StringBuffer ac=new StringBuffer();
			String user=sollogin.usert.getText();
			String pwd=sollogin.pwdt.getText();
			ac.append(user+":"+pwd);
			try {
				if(!user.isEmpty()&&!pwd.isEmpty()){
					IOFile f=new IOFile();
					Map<String, String> m = f.Reader(Path.userpath);		//从usr.ini中读取用户名和密码
					if(!m.containsKey(user)){		//判断usr.ini中是否有该用户，如果没有，则将用户名和密码写入文件并登陆
						f.Writer(ac.toString(),Path.userpath);
						sollogin.HidePanel(sollogin.loginoutpanel);
						sollogin.ShowPanel(sollogin.loginpanel);
						DisplayGui.star.SetLoginMarkVisable(true);
						DisplayGui.star.SetLoginStatus(true);
						DisplayGui.star.SetLoginLabelText(user);
						DisplayGui.star.SetUserName(user);
					}
					else if(m.get(user).equals(pwd)){		//判断用户名和密码是否正确，如果正确，则登陆成功
						sollogin.HidePanel(sollogin.loginoutpanel);
						sollogin.ShowPanel(sollogin.loginpanel);
						DisplayGui.star.SetLoginMarkVisable(true);
						DisplayGui.star.SetLoginLabelText(user);
						DisplayGui.star.SetLoginStatus(true);
						DisplayGui.star.SetUserName(user);
					}
					else {		//用户名和密码如果不一致，则提示错误框
						DisplayGui.star.SetLoginStatus(false);
						JOptionPane.showMessageDialog(null, "密码错误", "警告", JOptionPane.ERROR_MESSAGE);
					}
				
				}else
					JOptionPane.showMessageDialog(null, "用户名、密码不允许为空", "警告", JOptionPane.ERROR_MESSAGE);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}	
	} 
	
	/** 
	 * Copyright @ 2017 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2017-12-21
	 * desc:窗口SOLLogin中退出按钮_loginout的鼠标事件调用该类，退出登录 
	 * 
	 */
	
	public static class LoginOutEvent extends MouseAdapter{

		private SOLLogin sollogin;
		
		public LoginOutEvent(SOLLogin sollogin){
			this.sollogin=sollogin;
		}
		public void mouseClicked(MouseEvent e){
			sollogin.pwdt.setText("");	//退出登录后，清空密码输入框
			sollogin.HidePanel(sollogin.loginpanel);
			sollogin.ShowPanel(sollogin.loginoutpanel);
			DisplayGui.star.SetLoginMarkVisable(false);
			DisplayGui.star.SetLoginStatus(false);
			DisplayGui.star.SetLoginLabelText("Sign in");		
		}
		
	}
	
	
	/** 
	 * Copyright @ 2017 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2017-12-13 
	 * 
	 * 2017-12-19
	 * 			鼠标事件中，增加isexit标志，如果iseixt=true，表示SOLLogin窗口处于关闭状态中，则创建SOLLogin窗口，如果iseixt=false,表示SOLLogin窗口处于打开状态
	 * 2017-12-20
	 * 			鼠标事件中，判断SOLLogin窗口是否显示，如果显示则取消显示，如果不显示则显示
	 */

	public static class ShowSOLLoginEvent extends MouseAdapter{
		
		public ShowSOLLoginEvent(){
		
		}
		
		public void mouseClicked(MouseEvent e){
			if(DisplayGui.sollogin.isVisible())
				DisplayGui.sollogin.setVisible(false);
			else
				DisplayGui.sollogin.setVisible(true);
			
		}			
	}
	
	public static class AddIndexEvent implements ActionListener{
		
		private SOLAddIndex jf;
		
		public AddIndexEvent(SOLAddIndex jf){
			this.jf=jf;
		}
		
		public void actionPerformed(ActionEvent e) {
			String s=jf.GetFilePath();
//			jf.sbt.setEnabled(false);

			if(!s.isEmpty()){
				SOLAddIndexsProgress pb=new SOLAddIndexsProgress(jf);
				int size=pb.GetFileNum();
				jf.solstar.setProgressBarMaximum(size);
				pb.execute();
			}
			else
				JOptionPane.showMessageDialog(null, "请选择要添加索引的文档", "警告", JOptionPane.ERROR_MESSAGE);
//			jf.sbt.setEnabled(true);	
		}
	}
	
	/** 
	 * Copyright @ 2017 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2017-12-13
	 * desc:DispalyGui窗口中添加索引菜单项_addindex的监听事件，打开添加索引窗口
	 * 
	 */
	
	public static class ShowSOLAddIndexEvent implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			new SOLAddIndex();
		}
	}
	
	
	/** 
	 * Copyright @ 2017 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2017-12-21
	 * desc:DispalyGui窗口的关闭事件，关闭主窗口时，在关闭事件中将搜索记录写入history.cnf文件中
	 * 
	 */
	
	public static class DisplayGuiColseEvent extends WindowAdapter{
		
		private DisplayGui p;
		
		public DisplayGuiColseEvent(DisplayGui p){
			this.p=p;
		}
		
		public void windowClosing(WindowEvent e)
          {
//            super.windowClosing(e); 
			try {
				p.StoreHistory();
				HandleLucene.CloseIndexReader();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
          }
		
		public void windowClosed(WindowEvent e){
//			System.out.println("主界面已经关闭");
			Runtime rn = Runtime.getRuntime();
			try {
				String path=URLDecoder.decode(Path.upath,"UTF-8");
				rn.exec(path);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	
	/** 
	 * Copyright @ 2017 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2017-12-22
	 * desc:SOLShowIndex窗口中，删除按钮_lbt的删除事件
	 * 
	 * Modified 2018-8-30
	 * 		修改方法名称为ConfirmDeleteEvent
	 * 		修改为调用SOLShowConfirmDeleteIndexs窗口
	 */
	
	public static class ConfirmDeleteEvent implements ActionListener{
		
		SOLShowIndex p;
		
		public ConfirmDeleteEvent(SOLShowIndex p){
			this.p=p;
		}
	
		public void actionPerformed(ActionEvent e) {
			List<String> file=p.t.GetAllRowsDatasAtColumn(1);
			if(!file.isEmpty()) {
//				p.setEnabled(false);
//				p.setAlwaysOnTop(false);
				new SOLShowConfirmDeleteIndexs(this.p);	
			}
		}
	}
	
	/** 
	 * Copyright @ 2018 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2018-8-30
	 * desc:SOLShowConfirmDeleteIndexs窗口中，确认按钮_lbt的删除事件
	 * 
	 */
	
	public static class DeleteIndexEvent implements ActionListener{
		
		private SOLShowConfirmDeleteIndexs p;
		
		public DeleteIndexEvent(SOLShowConfirmDeleteIndexs p){
			this.p=p;
		}
	
		public void actionPerformed(ActionEvent e) {
			SOLShowIndex showindex=this.p.GetSOLShowIndexFrame();
			List<String> file=showindex.t.GetAllRowsDatasAtColumn(1);
//			Boolean f;
//			Map<String,Boolean> m=new HashMap<String,Boolean>();
			if(!file.isEmpty()){
				int size=file.size();
				SOLDeleteIndexsProgress pb=new SOLDeleteIndexsProgress(this.p,file);
				p.setProgressBarMaximum(size);
				pb.execute();
//				HandleLucene handle=new HandleLucene();
//				for(int i=0;i<file.size();i++){
//					String s=file.get(i).replaceAll("<[^>]+>","");		//删除html标签
//					f=handle.DeleteIndex(s,Path.indexpath,Path.filepath);	
//					if(f){
//						Vector<String> obj=p.t.GetDataID(s);
//						p.RemoveData(obj);
//						p.t.RemoveDataID(s);
//					}
//					m.put(s, f);					
//				}
//				p.t.LoadData(p.GetData());
//		        p.t.InitTable(false);
//		        
//		        new SOLShowDeleteIndexsResults(m);
			}
		}
	}
	
	/** 
	 * Copyright @ 2018 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2018-1-11
	 * desc:SOLRemoteIndex窗口中，删除按钮_lbt的删除事件,删除远程服务器上的索引文件
	 * 
	 */
	
	public static class DeleteRemoteIndexEvent implements ActionListener{
		
		private SOLShowConfirmDeleteRemoteIndexs p;
		
		public DeleteRemoteIndexEvent(SOLShowConfirmDeleteRemoteIndexs p){
			this.p=p;
		}
	
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			SOLRemoteIndex remoteindex=this.p.GetSOLShowIndexFrame();
			List<String> file=remoteindex.t.GetAllRowsDatasAtColumn(1);
			try {
				if(!file.isEmpty()){
					p.setButtonVisable(false);
					Boolean f=remoteindex.DeleteRemoteIndex(Path.urlpath,file);
					if(f){
						for(int i=0;i<file.size();i++){
							String s=file.get(i).replaceAll("<[^>]+>","");		//删除html标签
							Vector<String> obj=remoteindex.t.GetDataID(s);
							remoteindex.RemoveData(obj);
							remoteindex.t.RemoveDataID(s);
						}
				        p.SetConfirmInfomationText("<html><font size=4>删除成功！</center></font></html>");
					}
					remoteindex.t.LoadData(remoteindex.GetData());
			        remoteindex.t.InitTable(false);
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				JOptionPane.showMessageDialog(null,e1.getMessage(), "警告", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	public static class ConfirmDeleteRemoteIndexEvent implements ActionListener{
		
		SOLRemoteIndex p;
		
		public ConfirmDeleteRemoteIndexEvent(SOLRemoteIndex p){
			this.p=p;
		}
	
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			List<String> file=p.t.GetAllRowsDatasAtColumn(1);
			if(!file.isEmpty()){
				new SOLShowConfirmDeleteRemoteIndexs(this.p);
			}
		}
	}
	
	/** 
	 * Copyright @ 2017 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2017-12-25
	 * desc:DispalyGui窗口的显示SOLShowIndex事件，菜单按钮_showindex的监听事件
	 * 
	 */
	
	public static class ShowSOLShowIndexEvent implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			new SOLShowIndex();
		}
	}
	
	/** 
	 * Copyright @ 2017 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2018-1-4
	 * desc:DispalyGui窗口的显示SOLSynchronizeIndex事件，菜单按钮_synchronizeindex的监听事件
	 * 
	 */
	
	public static class ShowSOLSynchronizeIndexEvent implements ActionListener{
		public void actionPerformed(ActionEvent e) {
				new SOLCommitIndex();
		}
	}
	
	/** 
	 * Copyright @ 2018 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2018-1-11
	 * desc:查看远程服务器的索引文件，SOLRemoteIndex窗体_lbt事件
	 * 
	 */
	
	public static class ShowSOLRemoteIndexEvent implements ActionListener{
		public void actionPerformed(ActionEvent e) {
				new SOLRemoteIndex();
		}
	}
	
	/** 
	 * Copyright @ 2018 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2018-1-12
	 * 
	 */
	
	public static class ShowSOLDownloadIndexEvent implements ActionListener{
		public void actionPerformed(ActionEvent e) {
				new SOLDownloadIndex();
		}
	}
	
	/** 
	 * Copyright @ 2017 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2017-12-25
	 * desc:SOLShowIndex窗口的显示SOLShowLaws事件，表格_t的鼠标事件
	 * 
	 */
	
	public static class ShowSOLShowLawsEvent extends MouseAdapter{
		
			SOLShowIndex p;
		
		public ShowSOLShowLawsEvent(SOLShowIndex p){
			this.p=p;
		}
		
		public void mouseClicked(MouseEvent e){
			if(e.getClickCount() == 1){
				int c=p.t.columnAtPoint(e.getPoint()); //获取点击的列
				if(c!=p.t.getColumnCount()-1){
					int r=p.t.rowAtPoint(e.getPoint()); //获取点击的行
					int top=Integer.valueOf(p.t.GetOnceRowDataAtCloumn(2,r));//获取索引文件的法条总数
					String file=p.t.GetOnceRowDataAtCloumn(1,r);
					try {
						new SOLShowLaws(file,top);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			}
			
		}
	}
	
	/** 
	 * Copyright @ 2018 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2017-12-25
	 * desc:SOLRemoteIndex窗口的显示SOLShowRemoteLaws事件，表格_t的鼠标事件
	 * 
	 */
	
	public static class ShowSOLShowRemoteLawsEvent extends MouseAdapter{
		
			SOLRemoteIndex p;
		
		public ShowSOLShowRemoteLawsEvent(SOLRemoteIndex p){
			this.p=p;
		}
		
		public void mouseClicked(MouseEvent e){
			if(e.getClickCount() == 1){
				int c=p.t.columnAtPoint(e.getPoint()); //获取点击的列
				if(c!=p.t.getColumnCount()-1){
					int r=p.t.rowAtPoint(e.getPoint()); //获取点击的行
					int top=Integer.valueOf(p.t.GetOnceRowDataAtCloumn(2,r));//获取索引文件的法条总数
					String file=p.t.GetOnceRowDataAtCloumn(1,r);
					try {
						new SOLShowRemoteLaws(file,top);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			}
			
		}
	}
	
	/** 
	 * Copyright @ 2018 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2018-1-4
	 * desc:向远处服务器上传索引文件，SOLCommitIndex窗体_lbt事件
	 * 
	 * @2018-8-15
	 * 		修改当上传失败后，提交按钮无法恢复使用的bug
	 * 
	 */
	
	public static class ConfirmCommitIndexEvent implements ActionListener{
		
		private SOLCommitIndex p;
		
		public ConfirmCommitIndexEvent(SOLCommitIndex p){
			this.p=p;
		}

		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			List<String> file=p.t.GetAllRowsDatasAtColumn(1);
//			try {
				if(!file.isEmpty()){
					new SOLShowConfirmCommitIndexs(this.p);
//					p.SetSearchButtonEnable(false);
//					Map<String,int[]> m=new HashMap<String,int[]>();
//					for(int i=0;i<file.size();i++){
//						String s=file.get(i).replaceAll("<[^>]+>","");		//删除html标签
//						Vector<String> obj=p.t.GetDataID(s);
//						int[] res=p.CommitIndex(Path.urlpath,s,Path.indexpath);
//						m.put(s,res);
//						if(res[0]==res[1]&&res[0]!=-1&&res[1]!=-1){
//							p.RemoveData(obj);
//							p.t.RemoveDataID(s);
//						}
//					}
//					p.t.LoadData(p.GetData());
//			        p.t.InitTable(true);
//			        new SOLShowCommitIndexsResults(m);
//			        StringBuffer sb=new StringBuffer();
//			        for(Entry<String,int[]> entry:m.entrySet()){
//			        	if(entry.getValue()[0]==entry.getValue()[1]&&entry.getValue()[0]!=-1&&entry.getValue()[1]!=-1){
//			        		sb.append(entry.getKey()+" "+"法条总数："+entry.getValue()[0]+" "+"上传成功数:"+entry.getValue()[1]);
//			        		sb.append("\n");
//			        	}
//			        	else{
//			        		sb.append(entry.getKey()+" "+"提交失败");
//			        		sb.append("\n");
//			        	}
//			        }
//			        JOptionPane.showMessageDialog(null,sb.toString(), "信息", JOptionPane.INFORMATION_MESSAGE);
				}
//			} catch (Exception e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//				JOptionPane.showMessageDialog(null,e1.getMessage(), "警告", JOptionPane.ERROR_MESSAGE);
//			}
//	        p.SetSearchButtonEnable(true);
		}		
	}
	
	/** 
	 * Copyright @ 2018 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2018-8-30
	 * desc:向远处服务器上传索引文件，SOLCommitIndex窗体_lbt事件
	 * 
	 */
	
	public static class CommitIndexEvent implements ActionListener{
		
		private SOLShowConfirmCommitIndexs p;
		
		public CommitIndexEvent(SOLShowConfirmCommitIndexs p){
			this.p=p;
		}

		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			SOLCommitIndex commitindex=this.p.GetSOLShowCommitIndexFrame();
			List<String> file=commitindex.t.GetAllRowsDatasAtColumn(1);
//			try {
				if(!file.isEmpty()){
					int size=file.size();
					SOLCommitIndexsProgress pb=new SOLCommitIndexsProgress(this.p,file);
					p.setProgressBarMaximum(size);
					pb.execute();
//					p.SetSearchButtonEnable(false);
//					Map<String,int[]> m=new HashMap<String,int[]>();
//					for(int i=0;i<file.size();i++){
//						String s=file.get(i).replaceAll("<[^>]+>","");		//删除html标签
//						Vector<String> obj=p.t.GetDataID(s);
//						int[] res=p.CommitIndex(Path.urlpath,s,Path.indexpath);
//						m.put(s,res);
//						if(res[0]==res[1]&&res[0]!=-1&&res[1]!=-1){
//							p.RemoveData(obj);
//							p.t.RemoveDataID(s);
//						}
//					}
//					p.t.LoadData(p.GetData());
//			        p.t.InitTable(true);
//			        new SOLShowCommitIndexsResults(m);
//			        StringBuffer sb=new StringBuffer();
//			        for(Entry<String,int[]> entry:m.entrySet()){
//			        	if(entry.getValue()[0]==entry.getValue()[1]&&entry.getValue()[0]!=-1&&entry.getValue()[1]!=-1){
//			        		sb.append(entry.getKey()+" "+"法条总数："+entry.getValue()[0]+" "+"上传成功数:"+entry.getValue()[1]);
//			        		sb.append("\n");
//			        	}
//			        	else{
//			        		sb.append(entry.getKey()+" "+"提交失败");
//			        		sb.append("\n");
//			        	}
//			        }
//			        JOptionPane.showMessageDialog(null,sb.toString(), "信息", JOptionPane.INFORMATION_MESSAGE);
				}
//			} catch (Exception e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//				JOptionPane.showMessageDialog(null,e1.getMessage(), "警告", JOptionPane.ERROR_MESSAGE);
//			}
//	        p.SetSearchButtonEnable(true);
		}		
	}
	
	/** 
	 * Copyright @ 2018 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2018-1-12
	 * desc:向远处服务器请求索引文件时间，SOLSynchronizeIndex窗体_lbt事件
	 * 
	 */
	
	public static class DownloadIndexEvent implements ActionListener{
		private SOLShowConfirmDownloadIndexs p;
		
		public DownloadIndexEvent(SOLShowConfirmDownloadIndexs p){
			this.p=p;
		}

		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			SOLDownloadIndex commitindex=this.p.GetSOLShowCommitIndexFrame();
			List<String> file=commitindex.t.GetAllRowsDatasAtColumn(1);
			try {
				if(!file.isEmpty()){
					int size=file.size();
					SOLDownloadIndexsProgress pb=new SOLDownloadIndexsProgress(this.p,file);
					p.setProgressBarMaximum(size);
					pb.execute();
//					p.SetSearchButtonEnable(false);
//					//Map<String,int[]> m=new HashMap<String,int[]>();
//					for(int i=0;i<file.size();i++){
//						String s=file.get(i).replaceAll("<[^>]+>","");		//删除html标签
//						boolean f=p.DownloadIndex(Path.urlpath, s);
//						if(f){
//							Vector<String> obj=p.t.GetDataID(s);
//							p.RemoveData(obj);
//							p.t.RemoveDataID(s);
//						}
//						else
//							JOptionPane.showMessageDialog(null,s+" 导入本地仓库失败！", "警告", JOptionPane.ERROR_MESSAGE);
//					}
//					p.t.LoadData(p.GetData());
//			        p.t.InitTable(true);
//			        p.SetSearchButtonEnable(true);
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				JOptionPane.showMessageDialog(null,e1.getMessage(), "警告", JOptionPane.ERROR_MESSAGE);
			}	
		}	
	}
	
	public static class ConfirmDownloadIndexEvent implements ActionListener{
		private SOLDownloadIndex p;
		
		public ConfirmDownloadIndexEvent(SOLDownloadIndex p){
			this.p=p;
		}

		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			List<String> file=p.t.GetAllRowsDatasAtColumn(1);
			if(!file.isEmpty())
				new SOLShowConfirmDownloadIndexs(this.p);
		}	
	}
	
	public static class FilterEvent implements ActionListener{
		Object p;
		public FilterEvent(Object p){
			this.p=p;
		}
		
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(p instanceof SOLShowIndex){
				SOLShowIndex showindex=(SOLShowIndex)this.p;
				String keywords=showindex.GetKeywordsInputText(false);		//使用精确搜索
				FileIndexs findexs=new FileIndexs();
				if(!keywords.isEmpty()){		//判断输入框是否为空
					Map<String,String[]> finfo=findexs.QueryFiles(Path.filepath, keywords);
					Vector<Vector<String>> data = new Vector<Vector<String>>();
			        if(!finfo.isEmpty()){
			        	int i=0;
			        	for(Entry<String,String[]> entry: finfo.entrySet()){
			        		Vector<String> line=new Vector<String>();
			        		String[] infos=entry.getValue();
			        		line.add(String.valueOf(i++));
			        		line.add("<html>"+infos[3]+"</html>");
			        		line.add(infos[2]);
			        		data.add(line);
			        	}
			        }
			       showindex.t.LoadData(data);
			       showindex.t.InitTable(false);
				}
				else{
					Map<String,String[]> finfo=findexs.GetFileInfo(Path.filepath);
					Vector<Vector<String>> data = new Vector<Vector<String>>();
				       if(!finfo.isEmpty()){
				        	int i=0;
				        	for(Entry<String,String[]> entry: finfo.entrySet()){
				        		Vector<String> line=new Vector<String>();
				        		String[] infos=entry.getValue();
				        		line.add(String.valueOf(i++));
				        		line.add(entry.getKey());
				        		line.add(infos[2]);
				        		data.add(line);
				        	}
				        }
			       showindex.t.LoadData(data);
			       showindex.t.InitTable(false);
				}
			}
			else if(p instanceof SOLDownloadIndex){
				SOLDownloadIndex downindex=(SOLDownloadIndex)this.p;
				String keywords=downindex.GetKeywordsInputText(false);		//使用精确搜索
//				FileIndexs findexs=new FileIndexs();
				if(!keywords.isEmpty()){		//判断输入框是否为空
					Map<String,String[]> finfo=downindex.GetRemoteFileInfo(Path.urlpath,keywords);
					Vector<Vector<String>> data = new Vector<Vector<String>>();
			        if(!finfo.isEmpty()){
			        	int i=0;
			        	for(Entry<String,String[]> entry: finfo.entrySet()){
			        		Vector<String> line=new Vector<String>();
			        		String[] infos=entry.getValue();
			        		line.add(String.valueOf(i++));
			        		line.add("<html>"+infos[3]+"</html>");
			        		line.add(infos[2]);
			        		line.add(infos[0]);
			        		line.add(infos[1]);
			        		data.add(line);
			        	}
			        }
			       downindex.t.LoadData(data);
			       downindex.t.InitTable(true);
				}
				else{
					downindex.t.LoadData(downindex.GetData());
					downindex.t.InitTable(true);
				}
			}
			else if(p instanceof SOLRemoteIndex){
				SOLRemoteIndex remoteindex=(SOLRemoteIndex)this.p;
				String keywords=remoteindex.GetKeywordsInputText(false);		//使用精确搜索
				if(!keywords.isEmpty()){		
					Map<String,String[]> finfo=remoteindex.GetRemoteFileInfo(Path.urlpath,keywords);
					Vector<Vector<String>> data = new Vector<Vector<String>>();
			        if(!finfo.isEmpty()){
			        	int i=0;
			        	for(Entry<String,String[]> entry: finfo.entrySet()){
			        		Vector<String> line=new Vector<String>();
			        		String[] infos=entry.getValue();
			        		line.add(String.valueOf(i++));
			        		line.add("<html>"+infos[3]+"</html>");
			        		line.add(infos[2]);
			        		line.add(infos[0]);
			        		line.add(infos[1]);
			        		data.add(line);
			        	}
			        }
			        remoteindex.t.LoadData(data);
			        remoteindex.t.InitTable(false);
				}
				else{
					remoteindex.t.LoadData(remoteindex.GetData());
					remoteindex.t.InitTable(false);
				}
				
			}
			else if(p instanceof SOLCommitIndex){
				SOLCommitIndex commitindex=(SOLCommitIndex)this.p;
				String keywords=commitindex.GetKeywordsInputText(false);		//使用精确搜索
				if(!keywords.isEmpty()){
					Map<String,String[]> finfo=commitindex.GetFileInfo(Path.filepath, keywords);
					Vector<Vector<String>> data = new Vector<Vector<String>>();
			        if(!finfo.isEmpty()){
			        	int i=0;
			        	for(Entry<String,String[]> entry: finfo.entrySet()){
			        		Vector<String> line=new Vector<String>();
			        		String[] infos=entry.getValue();
			        		line.add(String.valueOf(i++));
			        		line.add("<html>"+infos[3]+"</html>");
			        		line.add(infos[2]);
			        		data.add(line);
			        	}
			        }
			        commitindex.t.LoadData(data);
			        commitindex.t.InitTable(true);
				}
				else{
					commitindex.t.LoadData(commitindex.GetData());
					commitindex.t.InitTable(true);
				}
				
			}
		}	
	}
	
	public static class ShowSOLShowUpadteEvent implements ActionListener{
		
		private SOLAbout p;
		private DisplayGui dp;
		
		public ShowSOLShowUpadteEvent(SOLAbout p){
			this.p=p;
			this.dp=this.p.GetDisplayGui();
		}

		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			new SOLShowUpdate(this.p);
			p.dispose();
			this.dp.setExtendedState(JFrame.ICONIFIED);
		}	
	}
	
}
