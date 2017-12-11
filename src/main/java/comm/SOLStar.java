package comm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SOLStar extends JPanel{
	private JLabel sll;
	private JPanel status;
	SOLStar(Dimension size){

		this.setLayout(new FlowLayout(FlowLayout.LEFT,5,0));
//		this.setBorder(BorderFactory.createLineBorder(Color.red));
//		this.setBackground(Color.LIGHT_GRAY);
//		this.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		
		sll=new JLabel();
		sll.setPreferredSize(new Dimension(400,24));
		sll.setBorder(BorderFactory.createLineBorder(Color.red));
		
        Icon icon=new ImageIcon("D:\\Lucene\\conf\\53c653670f08a.png");
        
        ImageIcon image = new ImageIcon("D:\\Lucene\\conf\\53c653670f08a.png"); 
        image.setImage(image.getImage().getScaledInstance(24,24,Image.SCALE_DEFAULT)); 

		
		JLabel rl=new JLabel(image);
//		rl.setIcon(icon);
		JLabel sl=new JLabel("登录标识");
		JLabel sgin= new JLabel();
		sgin.setText("<html><u><font color=blue face=\"微软雅黑\">Sign in</font></u></html>");
		
		JPanel status=new JPanel();			//远程标识、登录状态、登录按钮面板
		status.setPreferredSize(new Dimension(335,24));
		status.setBorder(BorderFactory.createLineBorder(Color.red));
		status.setLayout(new FlowLayout(FlowLayout.RIGHT,5,0));
		status.add(rl);
		status.add(sl);
		status.add(sgin);


		
		this.add(sll);
		this.add(status);
		this.setPreferredSize(new Dimension(400,24));
	}
	
	public void setStatusText(String sr){
		sll.setText(sr);
	}
	
	public String getStatusText(){
		return sll.getText();
	}

}
