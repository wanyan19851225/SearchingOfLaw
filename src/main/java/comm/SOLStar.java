package comm;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import javax.swing.JLabel;
import javax.swing.JPanel;

import comm.SOLEvents.ShowSOLLoginEvent;

public class SOLStar extends JPanel{
	private JLabel sll,rl,sl,sgin;
	private String username;
	private static Boolean LoginStatus=false;
	private static Boolean RemoteStatus=false;

	SOLStar(Dimension size){

		this.setLayout(new FlowLayout(FlowLayout.LEFT,5,0));
//		this.setBorder(BorderFactory.createLineBorder(Color.red));
//		this.setBackground(Color.LIGHT_GRAY);
//		this.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		
		sll=new JLabel();
//		sll.setPreferredSize(new Dimension(400,24));
		sll.setPreferredSize(new Dimension(size.width*2/3,size.height));
//		sll.setBorder(BorderFactory.createLineBorder(Color.black));
		
        
        //ImageIcon image = new ImageIcon("D:\\Lucene\\conf\\2ebba7e7e1e01966da176955d7206062.png"); 
		//ImageIcon image = new ImageIcon(this.getClass().getClassLoader().getResource("wifi.png"));

		ImageIcon image=new ImageIcon(this.getClass().getClassLoader().getResource("wifi.png"));	//将图片资源文件打到Jar包里，使用资源路径访问图片，修改日期：2018.1.30
		image.setImage(image.getImage().getScaledInstance(24,24,Image.SCALE_DEFAULT));
		rl=new JLabel(image);		//远程登录标签
			
		image = new ImageIcon(this.getClass().getClassLoader().getResource("login.png"));
	    image.setImage(image.getImage().getScaledInstance(24,24,Image.SCALE_DEFAULT)); 
		sl=new JLabel(image);		//登录状态标签

		
		sgin= new JLabel();		//登录名标签
		this.SetLoginLabelText("Sign in");
		
		JPanel status=new JPanel();			//远程标识、登录状态、登录按钮面板
//		status.setPreferredSize(new Dimension(335,24));
		status.setPreferredSize(new Dimension(size.width*1/3,size.height-1));
//		status.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		status.setLayout(new FlowLayout(FlowLayout.RIGHT,5,0));
		status.add(rl);
		status.add(sl);
		status.add(sgin);
		
		this.SetLoginMarkVisable(false);
		this.SetLoginLabelVisabel(false);
		this.SetRemoteMarkVisable(false);

		this.add(sll);
		this.add(status);
		
		this.setPreferredSize(size);
	}
	
	public void setStatusText(String sr){
		sll.setText(sr);
	}
	
	public String getStatusText(){
		return sll.getText();
	}
	
	public void SetRemoteMarkVisable(Boolean f){
		rl.setVisible(f);
	}
	
	public void SetLoginMarkVisable(Boolean f){
		sl.setVisible(f);
	}
	
	public void SetLoginLabelText(String s){
		sgin.setText("<html><u><font color=blue face=\"微软雅黑\">"+s+"</font></u></html>");
	}
	
	public void SetLoginLabelVisabel(Boolean f){
		sgin.setVisible(f);
	}
	
	public Boolean GetLoginStatus(){
		return LoginStatus;
	}
	
	public void SetLoginStatus(Boolean f){
		LoginStatus=f;
	}
	
	public Boolean GetRemoteStatus(){
		return RemoteStatus;
	}
	
	public void SetRemoteStatus(Boolean f){
		RemoteStatus=f;
	}
	
	public void AddShowSOLLLoginEvent(ShowSOLLoginEvent e){
		sgin.addMouseListener(e);
	}
	
	public void RemoveShwoSOLLoginEvent(ShowSOLLoginEvent e){
		sgin.removeMouseListener(e);
	}
	
	public void SetUserName(String s){
		username=s;
	}
	
	public String GetUserName(){
		return username;
	}

}
