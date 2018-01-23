package comm;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class SOLAbout extends JFrame{
	
	public SOLAbout(){
		
		Container contentpane=this.getContentPane();
		contentpane.setLayout(new BorderLayout(3,3));

		JLabel about=new JLabel();
		about.setHorizontalAlignment(JLabel.CENTER);
		about.setFont(new Font("宋体",Font.BOLD,20));
		about.setText("<html>Searching Of Laws <br/><br/> Version: SOL Release (3.9.7) <br/><br/> 作者：王岩</html>");
		
		contentpane.add(about);
		
	    this.setTitle("Searching Of Laws");//窗体标签  
	    this.setSize(FrameSize.X,FrameSize.Y);//窗体大小  
	    this.setLocationRelativeTo(null);//在屏幕中间显示(居中显示)  
	    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//退出关闭JFrame  
	    this.setVisible(true);//显示窗体
	    this.setResizable(false); //锁定窗体	
	}
}
