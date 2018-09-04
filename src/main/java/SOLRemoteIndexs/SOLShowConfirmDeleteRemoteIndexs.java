package SOLRemoteIndexs;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import Com.FrameSize;
import Com.SOLEvents;

public class SOLShowConfirmDeleteRemoteIndexs extends JFrame{
	private SOLRemoteIndex p;
	private JLabel ll;
	private JButton lbt,sbt;
	
	public SOLShowConfirmDeleteRemoteIndexs(SOLRemoteIndex p){
		Container contentpane=this.getContentPane();
		contentpane.setLayout(new BorderLayout(3,1));
		
		this.p=p;
		
		ll=new JLabel();
		ll.setHorizontalAlignment(JLabel.CENTER);
		ll.setText("<html><font size=4>删除后将从远程仓库中移除，且以后不会再检索到该文档<br><center>确认是否删除？</center></font></html>");
		ll.setPreferredSize(new Dimension(FrameSize.X-60,(FrameSize.Y/4-100)));
		
		lbt=new JButton("确认");
		lbt.setPreferredSize(new Dimension(60,30));
		
		sbt=new JButton("取消");
		sbt.setPreferredSize(new Dimension(60,30));

		lbt.addActionListener(new SOLEvents.DeleteRemoteIndexEvent(this));
		sbt.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				SOLShowConfirmDeleteRemoteIndexs.this.dispose();
			}
		});

		JPanel cpane=new JPanel();		//创建进度条、进度条提示标签面板
	    cpane.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
		JPanel spane=new JPanel();		//确定按钮，取消按钮面板
	    spane.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
	    
	    cpane.add(ll);
	    spane.add(lbt);
	    spane.add(sbt);
	    
		contentpane.add(cpane,BorderLayout.CENTER);
		contentpane.add(spane,BorderLayout.SOUTH);
		
	    this.setTitle("确认删除");//窗体标签  
	    this.setSize(FrameSize.X-36,FrameSize.Y/4);//窗体大小  
	    this.setLocationRelativeTo(null);//在屏幕中间显示(居中显示)  
	    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//退出关闭JFrame  
	    this.setVisible(true);//显示窗体
	    this.setResizable(false); //锁定窗体
//	    this.setAlwaysOnTop(true);
	}
	
	public void SetConfirmInfomationText(String s){
		ll.setText(s);
	}
	
	public SOLRemoteIndex GetSOLShowIndexFrame(){
		return this.p;
	}
	
	public void setButtonVisable(Boolean f){
		lbt.setVisible(f);
		sbt.setVisible(f);
	}
}
