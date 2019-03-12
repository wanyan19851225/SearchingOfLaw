package SOLSetting;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import Com.FrameSize;
import Com.WindowSize;

public class SOLSetting extends JFrame{
	private static SOLSetting single=null;
	private final int w=143;
	private JPanel eepane;
	public SOLSetting(){
		Container contentpane=this.getContentPane();
		contentpane.setLayout(new BorderLayout(3,3));
		
		JLabel lb1=new JLabel("检索方式");
		lb1.setFont(new Font("微软雅黑",Font.PLAIN,13));
		lb1.setName("lb1");
//		lb1.setBorder(BorderFactory.createLineBorder(Color.red));
		JLabel lb2=new JLabel("检索范围");
		lb2.setFont(new Font("微软雅黑",Font.PLAIN,13));
		lb2.setName("lb2");
//		lb2.setBorder(BorderFactory.createLineBorder(Color.blue));
		JLabel lb3=new JLabel("检索源");
		lb3.setFont(new Font("微软雅黑",Font.PLAIN,13));
		lb3.setName("lb3");
//		lb3.setBorder(BorderFactory.createLineBorder(Color.green));
		

		
		
		JPanel wepane=new JPanel();
		wepane.setPreferredSize(new Dimension(w-15,FrameSize.Y));
		wepane.setLayout(new BoxLayout(wepane, BoxLayout.Y_AXIS));
		wepane.add(Box.createVerticalStrut(50));
		wepane.add(lb1);
		wepane.add(Box.createVerticalStrut(15));
		wepane.add(lb2);
		wepane.add(Box.createVerticalStrut(15));
		wepane.add(lb3);
		
		JPanel wwpane=new JPanel();
		wwpane.setPreferredSize(new Dimension(15,FrameSize.Y));
		
		JPanel wpane=new JPanel();
		wpane.setPreferredSize(new Dimension(w,FrameSize.Y));
		wpane.setLayout(new BorderLayout(3,3));
//		wpane.setBorder(BorderFactory.createLineBorder(Color.red));
		wpane.add(wepane,BorderLayout.EAST);
		wpane.add(wwpane,BorderLayout.WEST);
		
		eepane=new JPanel();
		eepane.setPreferredSize(new Dimension(FrameSize.X-w-200,FrameSize.Y));
		eepane.setLayout(new BoxLayout(eepane, BoxLayout.Y_AXIS));
		eepane.add(Box.createVerticalStrut(50));
//		eepane.add(accmode);
//		eepane.add(Box.createVerticalStrut(15));
//		eepane.add(fuzzymode);
		
		JPanel ewpane=new JPanel();
		ewpane.setPreferredSize(new Dimension(200,FrameSize.Y));
		
		JPanel epane=new JPanel();
		epane.setPreferredSize(new Dimension(FrameSize.X-w,FrameSize.Y));
		epane.setLayout(new BorderLayout(3,3));
		epane.add(eepane,BorderLayout.EAST);
		epane.add(ewpane,BorderLayout.WEST);
		
		contentpane.add(epane,BorderLayout.EAST);
		contentpane.add(wpane,BorderLayout.WEST);
		
		class Show extends MouseAdapter{
			public void mouseClicked(MouseEvent e){
				JLabel lb=(JLabel)e.getSource();
				if(lb.getName().equals("lb1")){
					JRadioButton accmode=new JRadioButton("精确",true);
					accmode.setLocation(20, 20);
					accmode.setSize(50, 20);
					JRadioButton fuzzymode=new JRadioButton("模糊");
					fuzzymode.setLocation(20, 20);
					fuzzymode.setSize(50, 20);
					ButtonGroup modebg=new ButtonGroup();
					modebg.add(accmode);
					modebg.add(fuzzymode);
//					eepane.add(Box.createVerticalStrut(50));
					SOLSetting.this.eepane.add(accmode);
					SOLSetting.this.eepane.add(Box.createVerticalStrut(15));
					SOLSetting.this.eepane.add(fuzzymode);
				}
				else if(lb.getName().equals("lb2")){
					
				}
				else if(lb.getName().equals("lb3")){
					
				}
			}
			
		}
		lb1.addMouseListener(new Show());
		
	    this.setTitle("设置"); 
	    this.setSize(FrameSize.X-45,FrameSize.Y-173);  
	    this.setLocationRelativeTo(null);  
	    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    this.setVisible(true);
	    this.setResizable(false);
	}
	public static SOLSetting getInstance(){
		if(single!=null){
			if(!single.isShowing())
				single=new SOLSetting();			
			else
				single.requestFocus();		
		}
		else
			single=new SOLSetting();
		return single;		
	}
}
