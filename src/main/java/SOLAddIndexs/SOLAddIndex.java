package SOLAddIndexs;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import Com.FrameSize;
import Com.IOTable;
import Com.SOLEvents;
import Com.SOLStar;
import Com.SOLEvents.AddIndexEvent;


public class SOLAddIndex extends JFrame{
	
	public IOTable t;
	public JTextField stf;
	private JButton sbt,lbt;
	public SOLStar solstar;
	private JScrollPane jsp;
	private Vector<Vector<String>> data;
	
	public SOLAddIndex(){
		
		Container contentpane=this.getContentPane();
		contentpane.setLayout(new BorderLayout(3,3));
		
		Vector<String> cname = new Vector<String>();
		cname.add("序号");
		cname.add("文件名");  
        cname.add("法条总数");
        cname.add("结果");
        cname.add("重新添加");
        
        data = new Vector<Vector<String>>();
        
        t=new IOTable(cname,data);
        t.InitTable(false);
        
		
		stf=new JTextField(56);
		stf.setPreferredSize(new Dimension(300,30));
		stf.setFont(new Font("宋体",Font.PLAIN,15));
		
		lbt=new JButton("浏览");
		lbt.setPreferredSize(new Dimension(60,30));
		
		sbt=new JButton("添加");
		sbt.setPreferredSize(new Dimension(60,30));
		
		jsp=new JScrollPane();
		jsp.setPreferredSize(new Dimension(FrameSize.X,FrameSize.Y-100));
		jsp.setViewportView(t);
		
		solstar=new SOLStar(new Dimension(FrameSize.X-18,24));
		if(solstar.GetLoginStatus())
			solstar.SetLoginMarkVisable(true);
		if(solstar.GetRemoteStatus())
			solstar.SetRemoteMarkVisable(true);
		
		class ChooseEvent implements ActionListener{	
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser fcdlg = new JFileChooser();
				fcdlg.setDialogTitle("请选择待搜索文档");
				fcdlg.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				int returnval=fcdlg.showOpenDialog(null);
				if(returnval==JFileChooser.APPROVE_OPTION){
					String path=fcdlg.getSelectedFile().getPath();
					stf.setText(path);
				}
			}
		}
		
		lbt.addActionListener(new ChooseEvent());
		sbt.addActionListener(new SOLEvents.AddIndexEvent(this));
		
		JPanel npane=new JPanel();
	    npane.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
		JPanel cpane=new JPanel();		//列表面板
	    cpane.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
	   
	    npane.add(stf);
	    npane.add(lbt);
	    npane.add(sbt);
	    cpane.add(jsp);
	    
		contentpane.add(npane,BorderLayout.NORTH);
		contentpane.add(cpane,BorderLayout.CENTER);
		contentpane.add(solstar,BorderLayout.SOUTH);
		
		this.setTableVisabel(false);
		
	    this.setTitle("添加文档");//窗体标签  
	    this.setSize(FrameSize.X,FrameSize.Y);//窗体大小  
	    this.setLocationRelativeTo(null);//在屏幕中间显示(居中显示)  
	    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//退出关闭JFrame  
	    this.setVisible(true);//显示窗体
	    this.setResizable(false); //锁定窗体
	  	   	    
	}
	
	public String GetFilePath(){
		return stf.getText();
	}
	
	public void setFrameEnable(Boolean f){
		lbt.setEnabled(f);
		sbt.setEnabled(f);
		if(!f)
			this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		else
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	public void setTableVisabel(Boolean f){
		jsp.setVisible(f);
		t.setVisible(f);
	}
	
	public void ClearData(){
		data.clear();
	}

}
