package SOLShowIndexs;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Com.FrameSize;
import Com.IOTable;

public class SOLShowDeleteIndexsResults extends JFrame{
	public IOTable t;
	private Vector<Vector<String>> data;
	
	public SOLShowDeleteIndexsResults(Map<String,Boolean> results) {
		Container contentpane=this.getContentPane();
		contentpane.setLayout(new BorderLayout(3,3));
		
		Vector<String> cname = new Vector<String>();
		cname.add("序号");
		cname.add("文件名");  
        cname.add("结果");
        cname.add("重新删除");
        
        data = new Vector<Vector<String>>();
        
        int i=0;
        for(Entry<String,Boolean> entry:results.entrySet()){
        	Vector<String> line=new Vector<String>();
        	line.add(String.valueOf(i++));
        	line.add(entry.getKey());
        	if(entry.getValue())
        		line.add("成功");
        	else
        		line.add("<html><b><font color=red>失败</font></b><html>");
        	data.add(line);
        }
        
        t=new IOTable(cname,data);
        t.InitTable(false);
        
		JScrollPane jsp=new JScrollPane();
		jsp.setPreferredSize(new Dimension(FrameSize.X,FrameSize.Y-88));
		jsp.setViewportView(t);
		
		JPanel cpane=new JPanel();		//列表面板
	    cpane.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
	    
	    cpane.add(jsp);
	    
		contentpane.add(cpane,BorderLayout.CENTER);
		
	    this.setTitle("删除结果");//窗体标签  
	    this.setSize(FrameSize.X,FrameSize.Y);//窗体大小  
	    this.setLocationRelativeTo(null);//在屏幕中间显示(居中显示)  
	    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//退出关闭JFrame  
	    this.setVisible(true);//显示窗体
	    this.setResizable(false); //锁定窗体
	}

}
