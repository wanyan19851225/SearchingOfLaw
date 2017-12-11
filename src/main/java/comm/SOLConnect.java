package comm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.apache.lucene.queryparser.classic.ParseException;



public class SOLConnect extends JFrame{
	
	private Boolean isconn=null;
	
	public SOLConnect() throws IOException, ParseException{
		
		Container contentpane=this.getContentPane();
		contentpane.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
		
		JLabel hostl=new JLabel("远程服务器地址：");
		hostl.setPreferredSize(new Dimension(104,35));
		final JTextField hostt=new JTextField(20);
		
		JLabel portl=new JLabel("端口：");
		portl.setPreferredSize(new Dimension(104,35));
		portl.setHorizontalAlignment(SwingConstants.RIGHT);
		final JTextField portt=new JTextField(20);

		final JButton cbt=new JButton("连接");
		cbt.setPreferredSize(new Dimension(90,35));
		
		final JButton rbt=new JButton("重试");
		rbt.setPreferredSize(new Dimension(90,35));
		
		final JButton ibt=new JButton("断开连接");
		ibt.setPreferredSize(new Dimension(90,35));
		
		final JLabel resl=new JLabel();
//		sta.setPreferredSize(new Dimension(22,100));
		
		JScrollPane sjs=new JScrollPane(resl);
		sjs.setPreferredSize(new Dimension(426,130));
		sjs.setViewportView(resl);

		class ConnectEvent extends MouseAdapter{
			public void mouseClicked(MouseEvent e){
				
				StringBuffer url=new StringBuffer();
				url.append("http://"+hostt.getText()+":"+portt.getText()+"/swd/ServletDemo");
				
				IOHttp iohttp=new IOHttp(url.toString());
				
				try {
					iohttp.Connect();
					isconn=true;
					resl.setText("连接成功！");
				} catch (IOException e1) {
					// TODO Auto-generated catch block	
					resl.setText(e1.toString());
					isconn=false;
				}
				
			}	
		}
		cbt.addMouseListener(new ConnectEvent());
					
		class RetryEvent implements ActionListener{
			public void actionPerformed(ActionEvent e) {

				StringBuffer url=new StringBuffer();
				url.append("http://"+hostt.getText()+":"+portt.getText()+"/swd/ServletDemo");
				
				IOHttp iohttp=new IOHttp(url.toString());
				
				try {
					iohttp.Connect();
					isconn=true;
					resl.setText("连接成功！");
				} catch (IOException e1) {
					// TODO Auto-generated catch block	
					resl.setText(e1.toString());
					isconn=false;
				}
				
				}
			}
		
		rbt.addActionListener(new RetryEvent());
		
		class DisconnectEvent implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				isconn=false;
			}
		}
		ibt.addActionListener(new DisconnectEvent());
		
		JPanel hostadress=new JPanel();		//远程服务器IP地址输入面板
		hostadress.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
		hostadress.add(hostl);
		hostadress.add(hostt);
		
	    JPanel portp=new JPanel();		//端口号输入面板
	    portp.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
	    portp.add(portl);
	    portp.add(portt);
	    
	    JPanel btp=new JPanel();		//连接按钮面板
	    portp.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
	    btp.add(cbt);
		btp.add(rbt);
		btp.add(ibt);
		
		JPanel res=new JPanel();		//连接结果面板
		res.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
		res.add(sjs);
		
		contentpane.add(hostadress);
		contentpane.add(portp);
		contentpane.add(btp);
		contentpane.add(res);
		
	    this.setTitle("Searching Of Laws");//窗体标签  
	    this.setSize(436,333);//窗体大小  
	    this.setLocationRelativeTo(null);//在屏幕中间显示(居中显示)  
	    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//退出关闭JFrame  
	    this.setVisible(true);//显示窗体
	    this.setResizable(false); //锁定窗体
	    
	}
	
	public Boolean GetSucces(){
		return isconn;
	}
	
	
	public static void main(String[] args) throws UnknownHostException, IOException, ParseException { 
		
	
		
		SOLConnect his=new SOLConnect();
	

		
	}
}
