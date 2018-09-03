package comm;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class SOLShowUpdate extends JFrame{
	private JProgressBar jpb;
	private JLabel bl,ll;
	private JButton sbt,lbt;
	
	public SOLShowUpdate(){
		
		Container contentpane=this.getContentPane();
		contentpane.setLayout(new BorderLayout(3,1));
		
		jpb = new JProgressBar();		//进度条
        jpb.setMinimum(0);  
		jpb.setPreferredSize(new Dimension(FrameSize.X-60,(FrameSize.Y/4-100)/2));
        
        bl=new JLabel();		//进度条提示标签
		bl.setPreferredSize(new Dimension(FrameSize.X-60,(FrameSize.Y/4-100)/2));
		
		sbt=new JButton("取消");
		sbt.setPreferredSize(new Dimension(60,30));
		
		lbt=new JButton("立即重启");
		lbt.setPreferredSize(new Dimension(60,30));
		
		ll=new JLabel();
		ll.setHorizontalAlignment(JLabel.CENTER);
		ll.setText("<html><font size=4>更新下载完成，下次启动软件后安装生效</font></html>");
		ll.setPreferredSize(new Dimension(FrameSize.X-60,(FrameSize.Y/4-100)));
		
		JPanel cpane=new JPanel();		//创建进度条、进度条提示标签面板
	    cpane.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
		JPanel spane=new JPanel();		//取消按钮面板
	    spane.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
	    
	    cpane.add(jpb);
	    cpane.add(bl);
	    cpane.add(ll);
	    spane.add(sbt);
	    spane.add(lbt);
	    
		contentpane.add(cpane,BorderLayout.CENTER);
		contentpane.add(spane,BorderLayout.SOUTH);
		
		this.SetProgressBarVisabel(true);
		this.SetProgressBarLabelVisabel(true);
		this.SetConfirmInfomationVisabel(false);
		this.SetCancelButtonVisabel(true);
		this.SetRebootButtonVisabel(false);
		
	    this.setTitle("正在准备更新数据");//窗体标签  
	    this.setSize(FrameSize.X-36,FrameSize.Y/4);//窗体大小  
	    this.setLocationRelativeTo(null);//在屏幕中间显示(居中显示)  
	    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//退出关闭JFrame  
	    this.setVisible(true);//显示窗体
	    this.setResizable(false); //锁定窗体
//	    this.setAlwaysOnTop(true);
	    
		SOLUpdateProgress pb=new SOLUpdateProgress(this);
		this.setProgressBarMaximum(pb.GetFileSize());
		pb.execute();
	}
	
	public void SetProgressBarVisabel(Boolean f){
		jpb.setVisible(f);
	}
	
	public void SetProgressBarLabelVisabel(Boolean f){
		bl.setVisible(f);
	}
	
	public void SetConfirmInfomationVisabel(Boolean f){
		ll.setVisible(f);
	}
	
	public void SetRebootButtonVisabel(Boolean f){
		lbt.setVisible(f);
	}
	
	public void SetCancelButtonVisabel(Boolean f){
		sbt.setVisible(f);
	}
	
	public void setProgeressBarLabelText(String s){
		bl.setText(s);
	}
	
	public void setProgressBarValue(int n){
		jpb.setValue(n);
	}
	
	public void setProgressBarMaximum(int n){
        jpb.setMaximum(n); 
	}
}
