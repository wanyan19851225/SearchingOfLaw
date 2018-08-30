package comm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class SOLShowConfirmDeleteIndexs extends JFrame{
	
	private SOLShowIndex p;
	private JProgressBar jpb;
	private JLabel bl,ll;
	private JButton lbt,sbt;
	
	public SOLShowConfirmDeleteIndexs(SOLShowIndex p){
		this.p=p;
		
		Container contentpane=this.getContentPane();
		contentpane.setLayout(new BorderLayout(3,1));
		
		ll=new JLabel();
		ll.setHorizontalAlignment(JLabel.CENTER);
		ll.setText("<html><font size=4>删除后你将的本地仓库中移除，且以后不会再检索到该文档<br><center>确认是否删除？</center></font></html>");
		ll.setPreferredSize(new Dimension(FrameSize.X-60,(FrameSize.Y/4-100)));
//		ll.setBorder(BorderFactory.createLineBorder(Color.red));
		
		jpb = new JProgressBar();		//进度条
        jpb.setMinimum(0);  
		jpb.setPreferredSize(new Dimension(FrameSize.X-60,(FrameSize.Y/4-100)/2));
        
        bl=new JLabel();		//进度条提示标签
		bl.setPreferredSize(new Dimension(FrameSize.X-60,(FrameSize.Y/4-100)/2));
        
		lbt=new JButton("确认");
		lbt.setPreferredSize(new Dimension(60,30));
		
		sbt=new JButton("取消");
		sbt.setPreferredSize(new Dimension(60,30));

		lbt.addActionListener(new SOLEvents.DeleteIndexEvent(this));
		sbt.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				SOLShowConfirmDeleteIndexs.this.dispose();
			}
		});
        
		JPanel cpane=new JPanel();		//创建进度条、进度条提示标签面板
	    cpane.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
		JPanel spane=new JPanel();		//确定按钮，取消按钮面板
	    spane.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
	    
	    cpane.add(jpb);
	    cpane.add(bl);
	    cpane.add(ll);
	    spane.add(lbt);
	    spane.add(sbt);
	    
		contentpane.add(cpane,BorderLayout.CENTER);
		contentpane.add(spane,BorderLayout.SOUTH);
		
		this.SetProgressBarVisabel(false);
		this.SetProgressBarLabelVisabel(false);
		this.SetConfirmInfomationVisabel(true);
		
	    this.setTitle("确认删除");//窗体标签  
	    this.setSize(FrameSize.X-36,FrameSize.Y/4);//窗体大小  
	    this.setLocationRelativeTo(null);//在屏幕中间显示(居中显示)  
	    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//退出关闭JFrame  
	    this.setVisible(true);//显示窗体
	    this.setResizable(false); //锁定窗体
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
	
	public SOLShowIndex GetSOLShowIndexFrame(){
		return this.p;
	}
	
	public void setProgeressBarLabelText(String s){
		bl.setText(s);
	}
	
	public void setProgressBarValue(int n){
		jpb.setValue(n);
	}
	
	public void setEnable(Boolean f){
		lbt.setEnabled(f);
		sbt.setEnabled(f);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	
	public void setProgressBarMaximum(int n){
        jpb.setMaximum(n); 
	}
}
