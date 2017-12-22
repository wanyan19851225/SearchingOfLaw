package comm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.apache.lucene.queryparser.classic.ParseException;


/** 
 * Copyright @ 2017 Beijing Beidouht Co. Ltd. 
 * All right reserved. 
 * @author: wanyan 
 * date: 2017-12-13 
 * 
 * 2017-12-20
 * 			去掉构造函数中的SOLStar参数，使用静态类SOLStar
 * 			修改islogin变量为静态类
 */

public class SOLLogin extends JFrame{
	
//	public static Boolean islogin=false;
	public JPanel loginoutpanel,loginpanel;
	public JTextField usert,pwdt;
	
	public SOLLogin(JFrame jf){
	
		JLabel userl=new JLabel("用户名：");

		usert=new JTextField(10);
		
		JLabel pwdl=new JLabel("密     码：");

		pwdt=new JTextField(10);

		JButton lt=new JButton("登录");

		JLabel resl=new JLabel();
		resl.setText("<html><u><font color=blue face=\"微软雅黑\">忘记密码</font></u></html>");
		
		JLabel loggedin=new JLabel();
		loggedin.setText("<html><u><i><font color=blue face=\"微软雅黑\">已登录</font></i></u></html>");
		
		JButton loginout=new JButton("退出");

		JPanel hostadress=new JPanel();		//用户名输入面板
		hostadress.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
		hostadress.add(userl);
		hostadress.add(usert);
		
	    JPanel portp=new JPanel();		//密码输入面板
	    portp.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
	    portp.add(pwdl);
	    portp.add(pwdt);
	    
	    JPanel btp=new JPanel();		//登录按钮面板
	    portp.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
	    btp.add(lt);
	    
	    BoxLayout lo;
	    
	    loginoutpanel=new JPanel();  //未登录面板
	    lo=new BoxLayout(loginoutpanel,BoxLayout.Y_AXIS);
	    loginoutpanel.setLayout(lo);
//	    loginoutpanel.setPreferredSize(new Dimension(WindowSize.X/4,WindowSize.Y));
//	    loginoutpanel.setBorder(BorderFactory.createLineBorder(Color.red));
	    
	    loginoutpanel.add(hostadress);
	    loginoutpanel.add(Box.createVerticalStrut(5));
	    loginoutpanel.add(portp);
	    loginoutpanel.add(Box.createVerticalStrut(5));
	    loginoutpanel.add(btp);
	    
	    loginpanel=new JPanel(); //已登录面板
	    lo=new BoxLayout(loginpanel,BoxLayout.Y_AXIS);
	    loginpanel.setLayout(lo);
	    loginpanel.setVisible(false);		//初始化话SOLLogin时，默认不显示已登录面板
//	    loginpanel.setPreferredSize(new Dimension(WindowSize.X/4,WindowSize.Y));
//	    loginpanel.setBorder(BorderFactory.createLineBorder(Color.red));
	    
	    loginpanel.add(loggedin);
	    loginpanel.add(Box.createVerticalStrut(5));
	    loginpanel.add(loginout);
		
	    this.SetPanel(loginoutpanel);		//装载未登录面板
	    this.SetPanel(loginpanel);		//装载已登录面板
		
		Rectangle pfl=jf.getBounds();
		
//		System.out.println(pfl);
/*		
		class MoveEvent extends ComponentAdapter{
			public void componentMoved(ComponentEvent e) {
			    SOLLogin.this.setLocation(pfl.x+pfl.width,pfl.y);
			    System.out.println(pfl);
	          }	
		}
		this.addComponentListener(new MoveEvent());
*/		
		lt.addMouseListener(new SOLEvents.LoginEvent(this));
		loginout.addMouseListener(new SOLEvents.LoginOutEvent(this));
		
	    this.setTitle("Login");//窗体标签  
	    this.setSize(WindowSize.X/4,WindowSize.Y);//窗体大小 
	    this.setLocation(pfl.x+pfl.width,pfl.y);
	    this.setVisible(false);//初始化窗口时，默认不显示该窗口
	    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//退出关闭JFrame  
	    this.setResizable(false); //锁定窗体
	    
	}
	
	public void SetPanel(JPanel jp){
		Container contentpane=this.getContentPane();
		contentpane.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
		contentpane.add(jp);
	}
	
	public void HidePanel(JPanel jp){
		jp.setVisible(false);
	}
	
	public void ShowPanel(JPanel jp){
		jp.setVisible(true);
	}
	
	
	public static void main(String[] args) throws UnknownHostException, IOException, ParseException { 
		
	
		JFrame jf=new JFrame();
		
	    jf.setTitle("Searching Of Laws-");//窗体标签  
	    jf.setSize(WindowSize.X,WindowSize.Y);//窗体大小  
	    jf.setLocationRelativeTo(null);//在屏幕中间显示(居中显示)  
	    jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//退出关闭JFrame  
	    jf.setVisible(true);//显示窗体
	    jf.setResizable(false); //锁定窗体
		
		SOLLogin his=new SOLLogin(null);
	

		
	}
}
