package comm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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



public class SOLLogin extends JFrame{
	
	private Boolean islogin=false;
	private SOLStar star;
	
	public SOLLogin(JFrame jf,SOLStar star) throws IOException, ParseException{
		
		Container contentpane=this.getContentPane();
		contentpane.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
		
		this.star=star;
		
		JLabel userl=new JLabel("用户名：");

		JTextField usert=new JTextField(10);
		
		JLabel pwdl=new JLabel("密     码：");

		JTextField pwdt=new JTextField(10);

		JButton lt=new JButton("登录");

		JLabel resl=new JLabel();
		resl.setText("<html><u><font color=blue face=\"微软雅黑\">忘记密码</font></u></html>");

		lt.addMouseListener(new SOLEvents.LoginEvent(usert, pwdt,star));
	
		JPanel hostadress=new JPanel();		//远程服务器IP地址输入面板
		hostadress.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
		hostadress.add(userl);
		hostadress.add(usert);
		
	    JPanel portp=new JPanel();		//端口号输入面板
	    portp.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
	    portp.add(pwdl);
	    portp.add(pwdt);
	    
	    JPanel btp=new JPanel();		//连接按钮面板
	    portp.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
	    btp.add(lt);
	
		contentpane.add(hostadress);
		contentpane.add(portp);
		contentpane.add(btp);
		
		
		Rectangle pfl=jf.getBounds();
		
	    this.setTitle("Searching Of Laws");//窗体标签  
	    this.setSize(WindowSize.X/4,WindowSize.Y);//窗体大小  
	    this.setLocation(pfl.x+pfl.width,pfl.y);  
	    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//退出关闭JFrame  
	    this.setVisible(true);//显示窗体
	    this.setResizable(false); //锁定窗体
	    
	}
	
	public Boolean GetSucces(){
		return islogin;
	}
	
	
	public static void main(String[] args) throws UnknownHostException, IOException, ParseException { 
		
	
		JFrame jf=new JFrame();
		
	    jf.setTitle("Searching Of Laws-");//窗体标签  
	    jf.setSize(WindowSize.X,WindowSize.Y);//窗体大小  
	    jf.setLocationRelativeTo(null);//在屏幕中间显示(居中显示)  
	    jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//退出关闭JFrame  
	    jf.setVisible(true);//显示窗体
	    jf.setResizable(false); //锁定窗体
		
		SOLLogin his=new SOLLogin(jf,null);
	

		
	}
}
