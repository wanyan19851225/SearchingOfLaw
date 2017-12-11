package comm;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;


@SuppressWarnings("serial")
public class SOLSelectIndex extends JFrame{
	
	@SuppressWarnings("unchecked")
	public SOLSelectIndex(String indexpath,final List<String> range,List<Boolean> defselect){
		
		String ipath=indexpath;
		
		Container contentpane=this.getContentPane();
		contentpane.setLayout(new BorderLayout(3,3));
		HandleLucene handle=new HandleLucene();
		Map<String,Integer> fre=new HashMap<String,Integer>();
		
		@SuppressWarnings("rawtypes")
		Vector columnName = new Vector();
		columnName.add("文件名");  
        columnName.add("法条总数");
        columnName.add("是否选择");
        
        @SuppressWarnings("rawtypes")
		Vector rowdata = new Vector();
        DefaultTableModel tableModel = new DefaultTableModel();
        
        fre=handle.GetTermFreq(ipath);
        
        if(!fre.isEmpty()){
        	for(String key:fre.keySet()){
        		Vector line=new Vector();
        		line.add(key);
        		line.add(fre.get(key));
        		rowdata.add(line);
        	}
        }
        
        tableModel.setDataVector(rowdata,columnName);
        
        JTable jt=new JTable(tableModel);
        
		jt.setRowHeight(35);
		jt.getColumnModel().getColumn(0).setPreferredWidth(266);
		jt.getColumnModel().getColumn(1).setPreferredWidth(85);
		jt.getColumnModel().getColumn(2).setPreferredWidth(85);
		jt.getColumnModel().getColumn(2).setCellEditor(jt.getDefaultEditor(Boolean.class));
		jt.getColumnModel().getColumn(2).setCellRenderer(jt.getDefaultRenderer(Boolean.class));
		
		if(defselect.isEmpty()){
			for(int i=0;i<jt.getRowCount();i++){
				jt.setValueAt(true,i,2);
				defselect.add(true);
			}
		}
		else{
			for(int i=0;i<jt.getRowCount();i++){
				jt.setValueAt(defselect.get(i),i,2);
			}
		}
		
		final JButton lbt=new JButton("确定");
		lbt.setPreferredSize(new Dimension(60,35));
		
		final JButton sbt=new JButton("全选");
		sbt.setPreferredSize(new Dimension(60,35));
		
		final JButton sbt1=new JButton("反选");
		sbt1.setPreferredSize(new Dimension(60,35));
		
		JScrollPane jsp=new JScrollPane();
		jsp.setPreferredSize(new Dimension(436,245));
		jsp.setViewportView(jt);
		
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();    
		r.setHorizontalAlignment(JLabel.CENTER);   
		jt.setDefaultRenderer(Object.class,r);
		

		lbt.addMouseListener(new SOLEvents.ExeEvent(range, jt, defselect,this));
					
		sbt.addActionListener(new SOLEvents.SelEvent(jt));
	
		sbt1.addActionListener(new SOLEvents.UnselEvent(jt));
		
		JPanel cpane=new JPanel();
	    cpane.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
	    JPanel spane=new JPanel();
	    spane.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
		
		
		cpane.add(jsp);
		spane.add(sbt);
		spane.add(sbt1);
		spane.add(lbt);
		
		contentpane.add(cpane,BorderLayout.CENTER);
		contentpane.add(spane,BorderLayout.SOUTH);
		
	    this.setTitle("Searching Of Laws");//窗体标签  
	    this.setSize(FrameSize.X,FrameSize.Y);//窗体大小  
	    this.setLocationRelativeTo(null);//在屏幕中间显示(居中显示)  
	    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//退出关闭JFrame  
	    this.setVisible(true);//显示窗体
	    this.setResizable(false); //锁定窗体
	}

}
