package comm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;


public class  SOLEvents {
	
	public static class LocalEvent extends MouseAdapter{
		JPanel jp;
		
		public LocalEvent(JPanel jp){
			this.jp=jp;
		}
		
		public void mouseClicked(MouseEvent e){	
			int count = jp.getComponentCount();
			jp.setEnabled(false);
			for (int i = 0; i < count; i++) {
			    Object obj = jp.getComponent(i);
			    if (obj instanceof JTextField) {
			    	JTextField jt=(JTextField)obj;
			        jt.setEnabled(false);
			    }
			    if(obj instanceof JLabel){
			    	JLabel jl=(JLabel)obj;
			        jl.setEnabled(false);
			    }
			}	
		}
		
	}
	
	public static class HostEvent extends MouseAdapter{
		JPanel jp;
		
		public HostEvent(JPanel jp){
			this.jp=jp;
		}
		
		public void mouseClicked(MouseEvent e){	
			int count = jp.getComponentCount();
			jp.setEnabled(true);
			for (int i = 0; i < count; i++) {
			    Object obj = jp.getComponent(i);
			    if (obj instanceof JTextField) {
			    	JTextField jt=(JTextField)obj;
			        jt.setEnabled(true);
			    }
			    if(obj instanceof JLabel){
			    	JLabel jl=(JLabel)obj;
			        jl.setEnabled(true);
			    }
			}	
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
		SOLSelectIndex p;
		
		public SelEvent(SOLSelectIndex p){
			this.p=p;
		}
		
		public void actionPerformed(ActionEvent e) {
			p.SelectAll();
		}
	}

	public static class UnselEvent implements ActionListener{
		SOLSelectIndex p;
		
		public UnselEvent(SOLSelectIndex p){
			this.p=p;
		}
		public void actionPerformed(ActionEvent e) {
			p.SelectInvert();
		}
	}
	
	public static class ShowSOLSelectIndexEvent extends MouseAdapter{
		
		public void mouseClicked(MouseEvent e){
			new SOLSelectIndex();
		}	
	}
	
	public static class AboutEvent implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			SOLAbout about=new SOLAbout();
		}		
	}

	public static class SearchEvent extends MouseAdapter{
		
//		JButton sbt;
//		JTextField adr,port;
//		JPanel npaneofcenter;
//		List<String> tmphis,history;
//		SOLHistory solhis;
//		SOLResult res;
//		SOLStar star;
		DisplayGui p;

		public SearchEvent(DisplayGui p){
			
//			this.npaneofnorth=npaneofnorth;
//			this.npaneofcenter=npaneofcenter;
//			this.tmphis=tmphis;
//			this.history=history;
//			this.solhis=solhis;
//			this.res=res;
//			this.star=star;
			this.p=p;
/*			
			int c=npaneofnorth.getComponentCount();
			
			for(int i=0;i<c;i++){
			    Object obj =npaneofnorth.getComponent(i);
			    if (obj instanceof JTextField){
			    	//this.stf=(JTextField)obj;
			    }
			    if(obj instanceof JButton){
			    	//this.sbt=(JButton)obj;  
			    }
			}
			
			int s=npaneofcenter.getComponentCount();
			for(int i=0;i<s;i++){
			    Object obj =npaneofcenter.getComponent(i);
			    JPanel jp=(JPanel)obj;
			    int a=jp.getComponentCount();
			    for(int j=0;j<a;j++){
				    Object obj1 =jp.getComponent(j);    
				    if(obj1 instanceof JTextField){
				    	JTextField jt=(JTextField)obj1;
				    	if(jt.getName().equals("adr")){
				    		//this.adr=jt;
				    	}
				    	else if(jt.getName().equals("port")){
				    		//this.port=jt;
				    	}
				    } 
			    }

			}
*/		
		}
		
		public void mouseClicked(MouseEvent e){

			HandleLucene handle=new HandleLucene();
			Map<String,List<String[]>> content=new HashMap<String,List<String[]>>();
			String keywords=p.GetKeywordsInputText();
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
					int top=p.GetTop();
					Boolean f=p.GetIsRemote();
					long start=System.currentTimeMillis();
					if(DisplayGui.range.isEmpty()){
						if(f){
							System.out.println("keywords:"+keywords+","+"address:"+p.GetRemoteAddress());
						}
						else
							content=handle.GetSearch(Path.indexpath,keywords,top);
					}
					else{					
//						多条件查询，指定在某个法条文档中查询	
						if(f){
							System.out.println("keywords:"+keywords+","+"address:"+p.GetRemoteAddress());
						}
						else{
							String[] fields=new String[]{"file","law"};
							content=handle.GetMultipleSearch(Path.indexpath,fields,DisplayGui.range,keywords,top);
						}
					}
							
					long end=System.currentTimeMillis();
					long total=p.solresult.UpdateText(content);
					p.SetStatusText(String.valueOf(end-start),total);
					
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
			p.SetSearchButtonEnable(true);
		}
/*	
		public Boolean IsRange(JPanel npaneofcenter){
			
			Boolean f=true;		
			int s=npaneofcenter.getComponentCount();
			for(int i=0;i<s;i++){
			    Object obj =npaneofcenter.getComponent(i);
			    JPanel jp=(JPanel)obj;
			    int a=jp.getComponentCount();
			    for(int j=0;j<a;j++){
				    Object obj1 =jp.getComponent(j);
				    if (obj1 instanceof JRadioButton){
				    	JRadioButton rb=(JRadioButton)obj1;
				    		if(rb.getName().equals("other")){
						    	if(rb.isSelected()){
				    			f=false;
				    			break;
						    	}
				    		}
				    }
			    }
			}
			
			return f;
			
		}
		
		public Boolean IsRemote(JPanel npaneofcenter){
			Boolean f=true;		
			int s=npaneofcenter.getComponentCount();
			for(int i=0;i<s;i++){
			    Object obj =npaneofcenter.getComponent(i);
			    JPanel jp=(JPanel)obj;
			    int a=jp.getComponentCount();
			    for(int j=0;j<a;j++){
				    Object obj1 =jp.getComponent(j);
				    if (obj1 instanceof JRadioButton){
				    	JRadioButton rb=(JRadioButton)obj1;
				    		if(rb.getName().equals("remote")){
						    	if(rb.isSelected()){
				    			f=false;
				    			break;
						    	}
				    		}
				    }
			    }
			}
			
			return f;
			
		}
	
		public int GetTop(JPanel npaneofcenter){
			int top=1000;
			
			int s=npaneofcenter.getComponentCount();
			for(int i=0;i<s;i++){
			    Object obj =npaneofcenter.getComponent(i);
			    JPanel jp=(JPanel)obj;
			    int a=jp.getComponentCount();
			    for(int j=0;j<a;j++){
				    Object obj1 =jp.getComponent(j);
				    if (obj1 instanceof JRadioButton){
				    	JRadioButton rb=(JRadioButton)obj1;
				    		if(rb.getName().equals("1000")){
						    	if(rb.isSelected()){
				    			top=1000;
				    			break;
						    	}
				    		}else if(rb.getName().equals("2000")){
						    	if(rb.isSelected()){
				    			top=2000;
				    			break;
						    	}
				    		}else if(rb.getName().equals("3000")){
						    	if(rb.isSelected()){
				    			top=3000;
				    			break;
						    	}
				    		}
				    }
			    }
			}
			
			
			return top;
		}
*/		
	}
	
	public static class RemoteEvent extends MouseAdapter{
	
		public void mouseClicked(MouseEvent e){
			DisplayGui.star.SetRemoteStatus(true);
			DisplayGui.star.SetRemoteMarkVisable(true);
		}
		
	}
	
	public static class UnRemoteEvent extends MouseAdapter{
		
		public void mouseClicked(MouseEvent e){
			DisplayGui.star.SetRemoteStatus(false);
			DisplayGui.star.SetRemoteMarkVisable(false);
		}
		
	}
	
	/** 
	 * Copyright @ 2017 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2017-12-13 
	 * desc:窗口SOLLogin中的登录按钮_lt的鼠标事件调用该类，进行登录操作
	 * 
	 * 2017-12-21
	 * 			优化登陆时判断用户名和密码的代码结构
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
					Map<String, String> m = f.Reader("D:\\Lucene\\conf\\usr.ini");		//从usr.ini中读取用户名和密码
					if(!m.containsKey(user)){		//判断usr.ini中是否有该用户，如果没有，则将用户名和密码写入文件并登陆
						f.Writer(ac.toString(),"D:\\Lucene\\conf\\usr.ini");
						sollogin.HidePanel(sollogin.loginoutpanel);
						sollogin.ShowPanel(sollogin.loginpanel);
						DisplayGui.star.SetLoginMarkVisable(true);
						DisplayGui.star.SetLoginStatus(true);
						DisplayGui.star.SetLoginLabelText(user);
					}
					else if(m.get(user).equals(pwd)){		//判断用户名和密码是否正确，如果正确，则登陆成功
						sollogin.HidePanel(sollogin.loginoutpanel);
						sollogin.ShowPanel(sollogin.loginpanel);
						DisplayGui.star.SetLoginMarkVisable(true);
						DisplayGui.star.SetLoginLabelText(user);
						DisplayGui.star.SetLoginStatus(true);
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

	/** 
	 * Copyright @ 2017 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2017-12-21 
	 * desc:窗口SOLCreateIndex中的创建按钮_sbt的监听事件，创建索引文件
	 * 
	 */
	
	public static class CreateIndexEvent implements ActionListener{
		
		private SOLCreateIndex jf;
		
		public CreateIndexEvent(SOLCreateIndex jf){
			this.jf=jf;
		}
		
		public void actionPerformed(ActionEvent e) {
			String s=jf.GetFilePath();
			jf.sbt.setEnabled(false);
			if(!s.isEmpty()){
				HandleLucene handle=new HandleLucene();
				try {
					long start=System.currentTimeMillis();
					int totalofindex=handle.CreateIndex(s,Path.indexpath);
					long end=System.currentTimeMillis();
					if(totalofindex==-1)
						JOptionPane.showMessageDialog(null, "未找到法条文档或者文档中未发现法条，请先将有法条内容的文档放入该目录下", "警告", JOptionPane.ERROR_MESSAGE);
					else
						jf.solstar.setStatusText("创建检索完毕!"+"耗时："+String.valueOf(end-start)+"ms "+"创建索引条数："+totalofindex);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else
				JOptionPane.showMessageDialog(null, "请选择要创建索引的文档", "警告", JOptionPane.ERROR_MESSAGE);
			jf.sbt.setEnabled(true);
		}
	}
	
	
	/** 
	 * Copyright @ 2017 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2017-12-13
	 * desc:DispalyGui窗口中新建索引菜单项_createindex的监听事件，打开创建索引窗口
	 * 
	 */
	
	public static class ShowSOLCreateIndexEvent implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			new SOLCreateIndex();
		}
	}
	
	
	/** 
	 * Copyright @ 2017 Beijing Beidouht Co. Ltd. 
	 * All right reserved. 
	 * @author: wanyan 
	 * date: 2017-12-21 
	 * desc:窗口SOLAddIndex中的创建按钮_sbt的监听事件，添加索引文件
	 * 
	 */
	
	public static class AddIndexEvent implements ActionListener{
		
		private SOLAddIndex jf;
		
		public AddIndexEvent(SOLAddIndex jf){
			this.jf=jf;
		}
		
		public void actionPerformed(ActionEvent e) {
			String s=jf.GetFilePath();
			jf.sbt.setEnabled(false);

			if(!s.isEmpty()){
				HandleLucene handle=new HandleLucene();
				try {
		
					long start=System.currentTimeMillis();
					int totalofindex=handle.AddIndex(s,Path.indexpath);
					long end=System.currentTimeMillis();
					if(totalofindex==-1)
						JOptionPane.showMessageDialog(null, "未找到法条文档或者文档中未发现法条，请先将有法条内容的文档放入该目录下", "警告", JOptionPane.ERROR_MESSAGE);
					else
						jf.solstar.setStatusText("创建检索完毕!"+"耗时："+String.valueOf(end-start)+"ms "+"创建索引条数："+totalofindex);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else
				JOptionPane.showMessageDialog(null, "请选择要添加索引的文档", "警告", JOptionPane.ERROR_MESSAGE);
			jf.sbt.setEnabled(true);	
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
	 * date: 2017-12-20 
	 * desc:窗口SOLLogin中窗体的焦点事件，焦点事件中判断是否处于登录状态
	 * 
	 * 2017-12-21
	 * 		删除该焦点事件
	 * 			
	 */

/*	
	public static class SOLLoginFocusEvent implements WindowFocusListener{
		
		private SOLLogin sollogin;
		
		public SOLLoginFocusEvent(SOLLogin sollogin){
			this.sollogin=sollogin;
		}
		
		public void windowGainedFocus(WindowEvent e) {
			// TODO Auto-generated method stub
			if(SOLLogin.islogin){
				sollogin.remove(sollogin.loginoutpanel);
				sollogin.add(sollogin.loginpanel);
			}
			else{
				sollogin.remove(sollogin.loginpanel);
				sollogin.add(sollogin.loginoutpanel);
			}
				
		}

		public void windowLostFocus(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}
	
	} 
*/
}
