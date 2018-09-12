package SOLAbout;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Com.AppInfo;
import Com.DisplayGui;
import Com.FrameSize;
import Com.SOLEvents;

@SuppressWarnings("serial")
public class SOLAbout extends JFrame{
	private DisplayGui p;
	public SOLAbout(DisplayGui p){
		
		this.p=p;
		
		Container contentpane=this.getContentPane();
		contentpane.setLayout(new BorderLayout(3,3));

		JLabel about=new JLabel();
		about.setHorizontalAlignment(JLabel.CENTER);
//		about.setFont(new Font("宋体",Font.BOLD,20));
		about.setText("<html><font size=4><center>版本信息</center></font></html>");
		
		JLabel version=new JLabel();
		version.setHorizontalAlignment(JLabel.CENTER);
		version.setText("<html><font size=4><center>"+AppInfo.name+" "+AppInfo.version+"</center></font></html>");

		JLabel author=new JLabel();
		author.setHorizontalAlignment(JLabel.CENTER);
		author.setText("<html><font size=4><center>作者</center></font></html>");
		
		JLabel name=new JLabel();
		name.setHorizontalAlignment(JLabel.CENTER);
		name.setText("<html><font size=4><center>王岩</center></font></html>");

		JLabel hl=new JLabel();
		hl.setHorizontalAlignment(JLabel.CENTER);
		hl.setText("<html><font size=4><center>软件帮助</center></font></html>");
		
		JButton help=new JButton();
		help.setText("查看帮助");
		
		JLabel ul=new JLabel();
		ul.setHorizontalAlignment(JLabel.CENTER);
		ul.setText("<html><font size=4><center>恭喜！你的软件已是新版本！</center></font></html>");
		ul.setVisible(false);
//		ul.setBorder(BorderFactory.createLineBorder(Color.yellow));
		
		JButton update=new JButton();
		update.setText("点击启动更新");
		update.setVisible(false);
		
		update.addActionListener(new SOLEvents.ShowSOLShowUpadteEvent(this));
		
		JPanel apane=new JPanel();
		apane.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
		apane.add(about);
		apane.add(version);
		
		JPanel aupane=new JPanel();
		aupane.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
		aupane.add(author);
		aupane.add(name);
		
		JPanel hpane=new JPanel();
		hpane.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));	
		hpane.add(hl);
		hpane.add(help);
		
		JPanel blank1pane=new JPanel();
		
		JPanel cont1pane=new JPanel();
		cont1pane.setLayout(new GridLayout(3,0));
		cont1pane.add(apane);
		cont1pane.add(aupane);
		cont1pane.add(hpane);
		
		JPanel contpane=new JPanel();
		contpane.setLayout(new GridLayout(2,0));
		contpane.add(cont1pane);
		contpane.add(blank1pane);

		JPanel blankpane=new JPanel();
		blankpane.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
//		blankpane.setBorder(BorderFactory.createLineBorder(Color.black));
		
		JPanel cpane=new JPanel();
		cpane.add(contpane);
		cpane.add(blankpane);
		cpane.setLayout(new BoxLayout(cpane, BoxLayout.Y_AXIS));
//		cpane.setBorder(BorderFactory.createLineBorder(Color.red));
		
		JPanel spane=new JPanel();
		spane.setLayout(new FlowLayout(FlowLayout.LEFT,150,5));
		spane.add(ul);
		spane.add(update);
//		spane.setBorder(BorderFactory.createLineBorder(Color.black));
		
		contentpane.add(cpane,BorderLayout.CENTER);
		contentpane.add(spane,BorderLayout.SOUTH);
		
		if(DisplayGui.isver)
			update.setVisible(true);
		else
			ul.setVisible(true);
		
	    this.setTitle("Searching Of Laws");//窗体标签  
	    this.setSize(FrameSize.X-45,FrameSize.Y-173);//窗体大小  
	    this.setLocationRelativeTo(null);//在屏幕中间显示(居中显示)  
	    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//退出关闭JFrame  
	    this.setVisible(true);//显示窗体
	    this.setResizable(false); //锁定窗体	
	}
	
	public DisplayGui GetDisplayGui(){
		return this.p;
	}
}
