package comm;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SOLCreateIndex extends JFrame{
	
	public JTextField stf;
	public JButton sbt;
	public SOLStar solstar;
	
	public SOLCreateIndex(){
		
		Container contentpane=this.getContentPane();
		contentpane.setLayout(new BorderLayout(3,3));
		
		stf=new JTextField(40);
		stf.setPreferredSize(new Dimension(300,35));
		stf.setFont(new Font("宋体",Font.PLAIN,15));
		
		JButton lbt=new JButton("浏览");
		lbt.setPreferredSize(new Dimension(60,35));
		
		sbt=new JButton("创建");
		sbt.setPreferredSize(new Dimension(60,35));
		
		
		solstar=new SOLStar(new Dimension(FrameSize.X-18,24));
		if(solstar.GetLoginStatus())
			solstar.SetLoginMarkVisable(true);
		if(solstar.GetRemoteStatus())
			solstar.SetRemoteMarkVisable(true);
				
		class ChooseEvent implements ActionListener{	
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser fcdlg = new JFileChooser();
				fcdlg.setDialogTitle("请选择待搜索文档");
				fcdlg.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnval=fcdlg.showOpenDialog(null);
				if(returnval==JFileChooser.APPROVE_OPTION){
					String path=fcdlg.getSelectedFile().getPath();
//					fdir.append(path);
					stf.setText(path);
				}
			}
		}
		lbt.addActionListener(new ChooseEvent());
		
		JPanel cpane=new JPanel();
	
	    cpane.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
	    cpane.add(stf);
	    cpane.add(lbt);
	    cpane.add(sbt);
	    
		contentpane.add(cpane,BorderLayout.CENTER);
		contentpane.add(solstar,BorderLayout.SOUTH);
		
		sbt.addActionListener(new SOLEvents.CreateIndexEvent(this));
		
	    this.setTitle("Searching Of Laws");//窗体标签  
	    this.setSize(FrameSize.X,FrameSize.Y);//窗体大小  
	    this.setLocationRelativeTo(null);//在屏幕中间显示(居中显示)  
	    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//退出关闭JFrame  
	    this.setVisible(true);//显示窗体
	    this.setResizable(false); //锁定窗体
	
	}

	public String GetFilePath(){
		return stf.getText();
	}
	
}
