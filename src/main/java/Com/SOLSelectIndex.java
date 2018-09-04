package Com;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

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
	
	private IOTable t;
	
	@SuppressWarnings("unchecked")
	public SOLSelectIndex(){
		

		Container contentpane=this.getContentPane();
		contentpane.setLayout(new BorderLayout(3,3));
//		HandleLucene handle=new HandleLucene();
//		Map<String,Integer> fre=new HashMap<String,Integer>();
		FileIndexs findexs=new FileIndexs();
		Map<String,String[]> finfo=findexs.GetFileInfo(Path.filepath);
		
		Vector<String> cname = new Vector<String>();
		cname.add("序号");
		cname.add("文件名");  
        cname.add("法条总数");
        cname.add("是否选择");
        
		Vector<Vector<String>> data = new Vector<Vector<String>>();
        
//        fre=handle.GetTermFreq(Path.indexpath);
        
        if(!finfo.isEmpty()){
        	int i=0;
        	for(Entry<String,String[]> entry: finfo.entrySet()){
        		Vector<String> line=new Vector<String>();
        		line.add(String.valueOf(i++));
        		line.add(entry.getKey());
        		line.add(entry.getValue()[2]);
        		data.add(line);
        	}
        }
        
        t=new IOTable(cname,data);
        t.InitTable(DisplayGui.defselect);
        
  /*      
        tableModel.setDataVector(rowdata,columnName);
        
        jt=new JTable(tableModel);
        
		jt.setRowHeight(35);
		jt.getColumnModel().getColumn(0).setPreferredWidth(266);
		jt.getColumnModel().getColumn(1).setPreferredWidth(85);
		jt.getColumnModel().getColumn(2).setPreferredWidth(85);
		jt.getColumnModel().getColumn(2).setCellEditor(jt.getDefaultEditor(Boolean.class));
		jt.getColumnModel().getColumn(2).setCellRenderer(jt.getDefaultRenderer(Boolean.class));

		this.InitSelectStatus();
*/		
		JButton lbt=new JButton("确定");
		lbt.setPreferredSize(new Dimension(60,35));
		
		JButton sbt=new JButton("全选");
		sbt.setPreferredSize(new Dimension(60,35));
		
		JButton sbt1=new JButton("反选");
		sbt1.setPreferredSize(new Dimension(60,35));
		
		JScrollPane jsp=new JScrollPane();
		jsp.setPreferredSize(new Dimension(FrameSize.X,FrameSize.Y-88));	//修改于2018-7-26,自适应窗口大小
		jsp.setViewportView(t);
/*		
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();    
		r.setHorizontalAlignment(JLabel.CENTER);   
		jt.setDefaultRenderer(Object.class,r);
*/		

		lbt.addMouseListener(new SOLEvents.ExeEvent(this));
					
		sbt.addActionListener(new SOLEvents.SelEvent(this));
	
		sbt1.addActionListener(new SOLEvents.UnselEvent(this));
		
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
	
	public void SetRange(){
		DisplayGui.range.clear();
		int cnum=t.getColumnCount();
		int rnum=t.getRowCount();
		for(int i=0;i<rnum;i++){			
			if(((Boolean)t.getValueAt(i,cnum-1)).booleanValue())
				DisplayGui.range.add(t.getValueAt(i, 1).toString());	
			DisplayGui.defselect.set(i,((Boolean)t.getValueAt(i,cnum-1)).booleanValue());
		}
	}
/*	
	public void InitSelectStatus(){
		if(DisplayGui.defselect.isEmpty()){
			for(int i=0;i<t.getRowCount();i++){
				t.setValueAt(true,i,2);
				DisplayGui.defselect.add(true);
			}
		}
		else{
			for(int i=0;i<t.getRowCount();i++){
				t.setValueAt(DisplayGui.defselect.get(i),i,2);
			}
		}	
	}
*/	
	public void SelectAll(){
		int cnum=t.getColumnCount();
		int rnum=t.getRowCount();
		for(int i=0;i<rnum;i++){
			t.setValueAt(true,i,cnum-1);
		}
	}
	
	public void SelectInvert(){
		int cnum=t.getColumnCount();
		int rnum=t.getRowCount();
		for(int i=0;i<rnum;i++){
			
			if(((Boolean)t.getValueAt(i,cnum-1)).booleanValue())
				t.setValueAt(false,i,cnum-1);
			else
				t.setValueAt(true,i,cnum-1);
		}	
	}

}
